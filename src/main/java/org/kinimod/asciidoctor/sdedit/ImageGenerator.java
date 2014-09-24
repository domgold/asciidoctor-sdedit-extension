package org.kinimod.asciidoctor.sdedit;

import java.io.File;
import java.util.Map;

/**
 * Interface for generating images from block content or file source.
 * 
 */
public interface ImageGenerator {

	/**
	 * Generates an image from string content to an output file in outDir.
	 * 
	 * @param content
	 * @param outDir
	 * @param attributes
	 * @return the generated file's name.
	 */
	String generateImage(String content, File outDir,
			Map<String, Object> attributes);

}
