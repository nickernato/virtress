/**
 * 
 */
package com.virtress.assets;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ThisIsDef
 *
 */
public class AssetLoader {
	private static final String assetsPath = "com/virtress/servers/assets/";
	
	public List<Asset> loadAssets() {
		List<Asset> allAssets = new ArrayList<>();
		
		for (File assetFile : getResourceFolderFiles(assetsPath)) {
			System.out.println("FOUND ASSET: " + assetFile.getName());
		}
		
		return allAssets;
	}
	
	private File[] getResourceFolderFiles (String folder) {
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    URL url = loader.getResource(folder);
	    String path = url.getPath();
	    return new File(path).listFiles();
	  }
}
