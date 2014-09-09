package org.kinimod.asciidoctor.sdedit;

import java.io.File;
import java.util.Map;

public interface ImageGenerator {

	String generateImage(File input, File outDir, Map<String, Object> attributes);

	String generateImage(String content, File outDir,
			Map<String, Object> attributes);

}
