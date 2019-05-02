/**
 * 
 */
package com.virtress.server.config;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtress.assets.Asset;

/**
 * @author ThisIsDef
 *
 */
public class ConfigurationLoader {
	private static final String configsPath = "/com/virtress/server/config/";
	private static final String serverConfigName = "virtress_config.json";
	
	public ServerConfig loadServerConfig() {
		ServerConfig config = null;

		ObjectMapper mapper = new ObjectMapper();
		InputStream is = Asset.class.getResourceAsStream(configsPath + serverConfigName);
		try {
			config = mapper.readValue(is, ServerConfig.class);
		} catch (IOException e) {
			System.out.println("Failure mapping server config.");
		}

		return config;
	}
}
