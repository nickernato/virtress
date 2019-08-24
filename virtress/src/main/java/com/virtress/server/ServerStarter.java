/**
 * 
 */
package com.virtress.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.virtress.assets.Asset;
import com.virtress.assets.AssetLoader;
import com.virtress.assets.Group;
import com.virtress.assets.Header;
import com.virtress.common.HttpRequestMethod;
import com.virtress.common.HttpResponseCode;
import com.virtress.server.config.ConfigurationLoader;
import com.virtress.server.config.ServerConfig;

/**
 * @author ThisIsDef
 *
 */
public class ServerStarter {
	
	private static int defaultPort = 2800;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = defaultPort;
		for (String arg : args) 
		{
			if (arg.startsWith("port")) 
			{
				try 
				{
					port = Integer.parseInt(arg.split("=")[1]);
				}
				catch (Exception ex) 
				{
					System.out.println("Failed to parse port from program argument. Please use \"port=" + defaultPort + "\" Using the default port (2800).");
				}
			}
		}
		
		ConfigurationLoader configLoader = new ConfigurationLoader();
		ServerConfig serverConfig = configLoader.loadServerConfig();
		if (port == defaultPort) {
			if (serverConfig != null) {
				port = serverConfig.getPort();
			}
		}
		
		boolean serverOn = true;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started on port " + port + ".");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Unable to create server socket.");
		}
		
		while (serverOn) {
			try (Socket socket = serverSocket.accept()) {
			    socket.setKeepAlive(true);
			    socket.setSoLinger(true, 10000);
				socket.setSoTimeout(10000);
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				BufferedReader reader = new BufferedReader(isr);
				List<String> requestHeaders = new ArrayList<>();
				boolean extractBody = false;
				int bodyLength = 0;
				String urlPath = "";
				StringBuffer buffer = new StringBuffer();
				String line = "";
				String httpMethod = "";
				String contentType = "";
				while ((line = reader.readLine()) != null && !line.isEmpty()) {
					System.out.println(line);
					requestHeaders.add(line);
					buffer.append(line + "\r\n");
					for (HttpRequestMethod method : HttpRequestMethod.values()) {
						if (line.startsWith(method.name())) {
							urlPath = line.split(" ")[1];
						}
					}
					String[] spacedArray = line.split(" ");
					if (spacedArray.length > 0) {
						if (HttpRequestMethod.contains(spacedArray[0])) {
							httpMethod = spacedArray[0];
						}
					}
					if (line.startsWith(HttpRequestMethod.POST.name())) {
			            extractBody = true;
			        }
					if (line.toLowerCase().startsWith("content-length:")) {
			            bodyLength = Integer.valueOf(line.substring(line.indexOf(' ') + 1));
			        }
					if (line.toLowerCase().startsWith("content-type:")) {
			            contentType = line.substring(line.indexOf(' ') + 1);
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
			    
			    // Send Response
			    AssetLoader assetLoader = new AssetLoader();
			    List<Asset> allAssets = assetLoader.loadAssets();
			    String assetResponse = "";
			    Group matchedGroup = null;
			    String res = "";
			    try {
				    for (Asset asset : allAssets) {
				    	matchedGroup = asset.getGroup(urlPath, requestHeaders, httpMethod, requestBody, contentType);
				    	if (matchedGroup != null) {
				    		System.out.println("Setting assetResponse to matcher response: " + matchedGroup.getResponse());
				    		assetResponse = matchedGroup.getResponse();
				    		// We found the matching asset response. No need to continue.
				    		break;
				    	}
				    }
				    SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:Ss z");
				    res = "HTTP/1.0 " + matchedGroup.getResponseCode()  + " " + HttpResponseCode.getHttpResponseCode(matchedGroup.getResponseCode()).name() + "\n"
				            + "Server: HTTP server/0.1\n"
				            + "Date: "+format.format(new java.util.Date())+"\n"
				      + "Content-type: " + (matchedGroup != null ? matchedGroup.getContentType() : "text/html")  + "; charset=UTF-8\n"
				            + "Content-Length: " + (assetResponse.isEmpty() ? "0" : assetResponse.length()) + "\n";
				    if (matchedGroup.getResponseHeaders() != null) {
				    	for (Header responseHeader : matchedGroup.getResponseHeaders()) {
				    		res += responseHeader.getName() + ": " + responseHeader.getValue() + "\n";
				    	}
				    }
			    }
			    catch (NullPointerException npe) {
			    	// Usually has a first loop trigger and can't find assets.
			    	System.out.println("There was an error matching an asset: " + npe.getMessage());
			    }
			    res += "\n"; // Separates the response headers from the body.
			    res += (assetResponse.isEmpty() ? "{ \"status\":\"Asset not found\"" : assetResponse);
			    BufferedWriter out = new BufferedWriter(
			    	    new OutputStreamWriter(
			    	        new BufferedOutputStream(socket.getOutputStream()), "UTF-8"));
			    out.write(res);
			    out.flush();
			    out.close();
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
