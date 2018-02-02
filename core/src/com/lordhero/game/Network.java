package com.lordhero.game;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

public class Network implements INetwork {

	private static final String RequestWorldName = "requestWorldName";
	private static final String CloseConnection = "closeConnection";

	private int _homePort;	
	private int _visitorPort;
	
    private BufferedInputStream _inputStream;
    private PrintWriter _outputStream;
    
	private IGameMode _gameMode;
	
	public Network(IGameMode gameMode, int homePort, int visitorPort) {
		_gameMode = gameMode;
		
		_homePort = homePort;
		_visitorPort = visitorPort;
	}

	@Override
	public void connectToServer(ConnectionType connectionType) {
		Socket socket;

        int port = connectionType == ConnectionType.Local ? _homePort : _visitorPort;
        
        if (_outputStream != null) {
        	closeCurrentConnection();
        }
		
        try {
			socket = new Socket("127.0.0.1", port);
	        _outputStream = new PrintWriter(socket.getOutputStream(), true);
	        _inputStream = new BufferedInputStream(socket.getInputStream());

	        // Read the name of the world the client is connected to
	        _outputStream.println(RequestWorldName);

	        byte[] fileLengthInBytes = new byte[4];
	        _inputStream.read(fileLengthInBytes, 0, 4);
	        int worldNameLength = new BigInteger(fileLengthInBytes).intValue();
	        
	        byte[] worldNameAsArray = new byte[worldNameLength];
	        _inputStream.read(worldNameAsArray, 0, worldNameLength);
	        
	        String worldName = new String(worldNameAsArray, "UTF-8");
	        _gameMode.setWorldName(worldName);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] requestMap(String currentMap) {
		byte[] fileAsArray = null;
        _outputStream.println("sendMap:" + currentMap);

        byte[] fileLengthInBytes = new byte[4];
        try {
			_inputStream.read(fileLengthInBytes, 0, 4);
	        int fileLength = new BigInteger(fileLengthInBytes).intValue();
	        
	        fileAsArray = new byte[fileLength];
	        _inputStream.read(fileAsArray, 0, fileLength);

		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return fileAsArray;
	}

	private void closeCurrentConnection() {
		_outputStream.println(CloseConnection);
		
		// Do I know hot to implement proper network connections ?
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			_inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_outputStream.close();

		_inputStream = null;
		_outputStream = null;		
	}
	
}
