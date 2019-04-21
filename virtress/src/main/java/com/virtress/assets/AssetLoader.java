/**
 * 
 */
package com.virtress.assets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ThisIsDef
 *
 */
public class AssetLoader {
	private static final String assetsPath = "/com/virtress/server/assets/";

	public List<Asset> loadAssets() {
		List<Asset> allAssets = new ArrayList<>();

		for (String assetFileName : getResourceFiles(assetsPath)) {
			System.out.println("FOUND ASSET: " + assetFileName);
			// Load assets here
			ObjectMapper mapper = new ObjectMapper();
			InputStream is = Asset.class.getResourceAsStream(assetsPath + assetFileName);
			try {
				allAssets.add(mapper.readValue(is, Asset.class));
			} catch (IOException e) {
				System.out.println("Failure mapping asset.");
				e.printStackTrace();
			}
		}

		return allAssets;
	}

	private List<String> getResourceFiles(String path) {
		List<String> filenames = new ArrayList<>();

		try (InputStream in = getResourceAsStream(path);
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String resource;

			while ((resource = br.readLine()) != null) {
				filenames.add(resource);
			}
		} catch (IOException e) {
			System.out.println("Error while reading buffered reader.");
			e.printStackTrace();
		}

		return filenames;
	}

	private InputStream getResourceAsStream(String resource) {
		final InputStream in = getContextClassLoader().getResourceAsStream(resource);

		return in == null ? getClass().getResourceAsStream(resource) : in;
	}

	private ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
}
