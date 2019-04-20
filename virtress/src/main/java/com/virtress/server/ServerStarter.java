/**
 * 
 */
package com.virtress.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author ThisIsDef
 *
 */
public class ServerStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean serverOn = true;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(2800);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Unable to create server socket.");
		}
		while (serverOn) {
			try (Socket socket = serverSocket.accept()) {
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				boolean extractBody = false;
				int bodyLength = 0;
				StringBuffer buffer = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null && !line.isEmpty()) {
					System.out.println(line);
					buffer.append(line + "\r\n");
					if (line.startsWith("POST")) {
			            extractBody = true;
			        }
			        if (line.toLowerCase().startsWith("content-length:")) {
			            bodyLength = Integer.valueOf(line.substring(line.indexOf(' ') + 1, line.length()));
			        }
				}
				
				String requestHeader = buffer.toString();
			    String requestBody = "";
		
			    if (extractBody) {
			        char[] body = new char[bodyLength];
			        reader.read(body, 0, bodyLength);
			        requestBody = new String(body);
			    }
			    
			    System.out.println(requestHeader);
			    System.out.println(requestBody);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("There was an error while extracting the body.");
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("An unknown exception happened.");
			}

		}
	}

}
