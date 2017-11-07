package com.lordhero.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Main implements ApplicationListener {

	private static final int PortNumber = 12345;
	private static final String CommandSendMap = "sendMap";
	
	private Path _mapPath;

	@Override
	public void create() {
		Path path = Paths.get(System.getProperty("user.home"), "Lords'n'Heroes");
		
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		_mapPath = Paths.get(System.getProperty("user.home"), "Lords'n'Heroes", "maps");
		
		if (!Files.exists(_mapPath)) {
			try {
				Files.createDirectory(_mapPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		}

		Path sourcePath = Paths.get(".");

		DirectoryStream.Filter<Path> dirFilter = new DirectoryStream.Filter<Path>() {
            public boolean accept(Path path) throws IOException {
            	PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:./*.tmx");
                return matcher.matches(path);
            }
        };
        
		try {
			for (final Path copiedFilePath : Files.newDirectoryStream(sourcePath)) {
				if (dirFilter.accept(copiedFilePath)) {
					Path targetPath = Paths.get(_mapPath.toString(), copiedFilePath.getFileName().toString());
					if (!Files.exists(targetPath)) {
						Files.copy(copiedFilePath, targetPath);						
					}						
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	    try {
			ServerSocket serverSocket = new ServerSocket(PortNumber);
		    Socket clientSocket = serverSocket.accept();
		    BufferedOutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
		    BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));			
			
			String receivedMessage;
			
			while ((receivedMessage = inputStream.readLine()) != null) {
				String[] tokens = receivedMessage.split(":");
				if (tokens[0].equals(CommandSendMap)) {
		
					Path mapToLoad = Paths.get(_mapPath.toString(), tokens[1] + ".tmx");
					FileHandle mapHandle = Gdx.files.internal(mapToLoad.toString());

					byte[] fileLengthAsByteArray = new byte[4];
			        int fileLength = (int)mapHandle.length();
			        ByteBuffer.wrap(fileLengthAsByteArray).putInt(fileLength);
			        outputStream.write(fileLengthAsByteArray);
			        outputStream.flush();
					
					outputStream.write(mapHandle.readBytes(), 0, fileLength);
					outputStream.flush();					
				}
			}			
		} catch (IOException ioEx) {
			System.out.println(ioEx.getMessage());
		}		
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

}
