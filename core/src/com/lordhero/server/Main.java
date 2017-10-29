package com.lordhero.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Main implements ApplicationListener {

	private static final int PortNumber = 12345;
	private static final String CommandSendMap = "sendMap";

	@Override
	public void create() {
	    try {
			ServerSocket serverSocket = new ServerSocket(PortNumber);
		    Socket clientSocket = serverSocket.accept();
		    BufferedOutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
		    BufferedReader inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));			
			
			String receivedMessage;
			
			while ((receivedMessage = inputStream.readLine()) != null) {
				String[] tokens = receivedMessage.split(":");
				if (tokens[0].equals(CommandSendMap)) {
					
					FileHandle mapHandle = Gdx.files.internal(tokens[1] + ".tmx");

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
