package org.kinimod.asciidoctor.sdedit;

import java.io.File;
import java.util.Map;

/**
 * Interface for generating images from block content or file source.
 * 
 */
public interface ImageGenerator {

	/**
	 * Generates an image from a file source to an output file in outDir.
	 * 
	 * @param input
	 *            the input file
	 * @param outDir
	 *            the output directory
	 * @param attributes
	 *            asciidoctor attributes from the macro or block definition.
	 * @return the generated file's name (not the full path).
	 */
	String generateImage(File input, File outDir, Map<String, Object> attributes);

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
