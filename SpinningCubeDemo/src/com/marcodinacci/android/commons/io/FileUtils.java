package com.marcodinacci.android.commons.io;

import java.io.File;
import java.io.IOException;

/**
 * Collection of file related utilities.
 * 
 * @author Marco Dinacci <dev@dinointeractive.com>
 *
 */
public class FileUtils {
	/**
	 * Mimic the behaviour of the linux <em>touch</em> utility.
	 * 
	 * @param File The File to touch. Informations about the path are taken from the file
	 */
	public static void touch(File file) throws IOException {
		if(!file.exists()) {
			File parent = file.getParentFile();
			if(parent != null) 
				if(!parent.exists())
					if(!parent.mkdirs())
						throw new IOException("Cannot create parent directories for file: " + file);
			
				file.createNewFile();
		}
		
		boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success)
            throw new IOException("Unable to set the last modification time for " + file);
	}
	
	/**
	 * Mimic the behaviour of the linux <em>touch</em> utility.
	 * 
	 * @param String The path of the file to touch.
	 */
	public static void touch(String path) throws IOException {
		touch(new File(path));
	}
}
