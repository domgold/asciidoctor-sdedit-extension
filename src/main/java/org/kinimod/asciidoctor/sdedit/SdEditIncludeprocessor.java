package org.kinimod.asciidoctor.sdedit;

import java.io.File;
import java.util.Map;

import org.asciidoctor.ast.DocumentRuby;
import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.extension.PreprocessorReader;

public class SdEditIncludeprocessor extends IncludeProcessor {

	public SdEditIncludeprocessor() {
		super();
	}

	public SdEditIncludeprocessor(Map<String, Object> config) {
		super(config);
	}

	@Override
	public boolean handles(String arg0) {
		return arg0.startsWith("sdedit:");
	}

	@Override
	public void process(DocumentRuby document, PreprocessorReader reader,
			String target, Map<String, Object> attributes) {
		target = target.substring(7);
		Map<String, Object> docAttrs = document.getAttributes();
		String docdir = AsciidoctorHelpers.getAttribute(docAttrs, "docdir",
				null, false);
		ImageGenerator imgGenerator = new SdEditImageGenerator(docdir);
		document.attributes();
		String outputFilename = "missing.png";
		try {
			outputFilename = imgGenerator.generateImage(
					new File(docdir, target),
					AsciidoctorHelpers.getImageDir(docAttrs), attributes);
		} catch (Exception ex) {
			// ignore we return missing.png
		}
		reader.push_include("image::" + outputFilename + "[]", target, target,
				1, attributes);
	}

}
