package com.lordhero.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Properties;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.lordhero.game.Consts;
import com.lordhero.game.IGameMode.SaveType;

import net.dermetfan.gdx.maps.tiled.TmxMapWriter;
import net.dermetfan.gdx.maps.tiled.TmxMapWriter.Format;

public class Main implements ApplicationListener {

	private static final String SendMap = "sendMap";
	private static final String SendEntities = "sendEntities";
	private static final String RequestWorldName = "requestWorldName";
	private static final String CreateMapFromTemplate = "createMapFromTemplate";
	private static final String CloseConnection = "closeConnection";

	private int _port;
	
	private String _worldName;

	private Path _mapPath;
	
	private Path _entitiesPath;
	
	public Main(String configPath) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(configPath);

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			_worldName =  prop.getProperty("worldname");
			_port = Integer.parseInt(prop.getProperty("port"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void create() {		
		setupPaths();

		initialCopyOfOriginalMaps();
		
	    handleClientConnections();		
	}

	private void handleClientConnections() {
		try {
			ServerSocket serverSocket = new ServerSocket(_port);
	    	while (true) {
				Socket clientSocket = serverSocket.accept();
			    BufferedOutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
			    BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));			
				
				String receivedMessage;
				
				while ((receivedMessage = inputStream.readLine()) != null) {
					String[] tokens = receivedMessage.split(":");
					if (tokens[0].equals(SendMap)) {		
						Path mapToLoad = Paths.get(_mapPath.toString(), tokens[1] + ".tmx");						

						sendFile(outputStream, Gdx.files.internal(mapToLoad.toString()));
					}
					else if (tokens[0].equals(SendEntities)) {
						sendFile(outputStream, Gdx.files.internal(_entitiesPath.toString()));
					}
					else if (tokens[0].equals(RequestWorldName)) {
						byte[] worldNameAsByteArray = _worldName.getBytes();

						byte[] fileLengthAsByteArray = new byte[4];
						ByteBuffer.wrap(fileLengthAsByteArray).putInt(worldNameAsByteArray.length);
						
						outputStream.write(fileLengthAsByteArray);
						outputStream.flush();
						
						outputStream.write(worldNameAsByteArray, 0, worldNameAsByteArray.length);
						outputStream.flush();
					}
					else if (tokens[0].equals(CreateMapFromTemplate)) {
						String result = "";
						if (tokens.length == 6) {
							result = copyTemplateMap(tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));
						}
						byte[] resultAsByteArray = result.getBytes();

						byte[] resultLengthAsByteArray = new byte[4];
						ByteBuffer.wrap(resultLengthAsByteArray).putInt(resultAsByteArray.length);

						outputStream.write(resultLengthAsByteArray);
						outputStream.write(resultAsByteArray, 0, resultAsByteArray.length);					
						outputStream.flush();
					}
					else if (tokens[0].equals(CloseConnection)) {
						clientSocket.close();
						break;
					}
				}
	    	}	    	
		} catch (IOException ioEx) {
			System.out.println(ioEx.getMessage());
		}
	}

	private void setupPaths() {
		Path path = Paths.get(System.getProperty("user.home"), "Lords'n'Heroes");
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		path = Paths.get(path.toString(), _worldName);
		
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		_mapPath = Paths.get(path.toString(), "maps");
		
		if (!Files.exists(_mapPath)) {
			try {
				Files.createDirectory(_mapPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		}

		_entitiesPath = Paths.get(path.toString(), "entities.xml");
	}
	
	private String copyTemplateMap(String templateMapName, String newMapName, String parentMapName, int exitTargetX, int exitTargetY) {
		String result = "error";
		
		Path sourcePath = Paths.get(_mapPath.toString(), templateMapName + ".tmx");		
		Path targetPath = Paths.get(_mapPath.toString(), newMapName + ".tmx");		
		
		if (!Files.exists(targetPath)) {
			try {
				Files.copy(sourcePath, targetPath);
				
				// Add the meta data for the map exit to the newly created map
				TiledMap tiledMap = new TmxMapLoader().load(targetPath.toString());
				
				MapProperties mapProperties = tiledMap.getProperties();
				
				int xExitToParent = (Integer)mapProperties.get("DefaultEntryX");
				int yExitToParent = (Integer)mapProperties.get("DefaultEntryY");
				
		        MapLayer siteLayer = (MapLayer)tiledMap.getLayers().get(Consts.SiteLayer);        
		        RectangleMapObject mapObject = new RectangleMapObject(xExitToParent * Consts.TileWidth, yExitToParent * Consts.TileHeight, Consts.TileWidth, Consts.TileHeight);	        
				mapObject.getProperties().put(Consts.TargetMap, parentMapName);
				mapObject.getProperties().put("x", xExitToParent * Consts.TileWidth);
				mapObject.getProperties().put("y", yExitToParent * Consts.TileHeight);
				mapObject.getProperties().put(Consts.ChildMapEntryX, exitTargetX);
				mapObject.getProperties().put(Consts.ChildMapEntryY, exitTargetY);
				
		        MapObjects objects = siteLayer.getObjects();
		        objects.add(mapObject);
		        
		        // Save the newly created map with its properties
				FileWriter fileWriter;
				try {
					fileWriter = new FileWriter(targetPath.toString(), false);
					TmxMapWriter tmxWriter = new TmxMapWriter(fileWriter);
					tmxWriter.tmx(tiledMap, Format.Base64Zlib);
					tmxWriter.flush();
					tmxWriter.close();
					result = Integer.toString(xExitToParent) + ":" + Integer.toString(yExitToParent);					
				} catch (IOException e) {
					System.err.println("Could not write newly created map: " + e.getMessage());
					e.printStackTrace();
				}		
				
			}
			catch (Exception e) {
				System.err.println("Could not copy " + templateMapName + " to " + newMapName + ": " + e.getMessage());
			}
		}
		
		return result;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub		
	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	private void initialCopyOfOriginalMaps()
	{
		DirectoryStream.Filter<Path> dirFilter = createDirectoryFilter("glob:./*.tmx");        
		copyFilteredFiles(dirFilter, "");
		
		dirFilter = createDirectoryFilter("glob:./*.tsx");        
		copyFilteredFiles(dirFilter, "");

		dirFilter = createDirectoryFilter("glob:./*.png");        
		copyFilteredFiles(dirFilter, "");

		dirFilter = createDirectoryFilter("glob:./data/tileset/*.tsx");
		copyFilteredFiles(dirFilter, "data/tileset");

		dirFilter = createDirectoryFilter("glob:./data/tileset/*.png");
		copyFilteredFiles(dirFilter, "data/tileset");
	}

	private DirectoryStream.Filter<Path> createDirectoryFilter(final String filter) {
		DirectoryStream.Filter<Path> dirFilter = new DirectoryStream.Filter<Path>() {
            public boolean accept(Path path) throws IOException {
            	PathMatcher matcher = FileSystems.getDefault().getPathMatcher(filter);
                return matcher.matches(path);
            }
        };
		return dirFilter;
	}

	private void copyFilteredFiles(DirectoryStream.Filter<Path> dirFilter, String subDirectory) {
		Path sourcePath = Paths.get(".", subDirectory);

		try {
			for (final Path copiedFilePath : Files.newDirectoryStream(sourcePath)) {
				if (dirFilter.accept(copiedFilePath)) {
					Path targetPath = Paths.get(_mapPath.toString(), subDirectory);
									
					if (!Files.exists(targetPath)) {
						try {
							Files.createDirectories(targetPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					targetPath = Paths.get(targetPath.toString(), copiedFilePath.getFileName().toString());
					
					if (!Files.exists(targetPath)) {
						Files.copy(copiedFilePath, targetPath);						
					}						
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendFile(OutputStream outputStream, FileHandle mapHandle) throws IOException {
		byte[] fileLengthAsByteArray = new byte[4];

		int fileLength = 0;
		try {
			fileLength = (int)mapHandle.length();
	        ByteBuffer.wrap(fileLengthAsByteArray).putInt(fileLength);
	        outputStream.write(fileLengthAsByteArray);
	        outputStream.flush();
			
			outputStream.write(mapHandle.readBytes(), 0, fileLength);
			outputStream.flush();
		} catch (Exception e) {
			// error handling: file not found, just send 0 file size
	        ByteBuffer.wrap(fileLengthAsByteArray).putInt(fileLength);
	        outputStream.write(fileLengthAsByteArray);
	        outputStream.flush();			
		}        					
	}
}
