package org.kinimod.asciidoctor.sdedit;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockMacroProcessor;

public class SdEditBlockMacroProcessor extends BlockMacroProcessor {

	@SuppressWarnings("serial")
	private static Map<String, Object> configs = new HashMap<String, Object>() {
		{
			// put("contexts", Arrays.asList(":listing", ":literal", ":open"));
			put("content_model", ":simple");
			put("pos_attrs", Arrays.asList(
					SdEditImageGenerator.OUTPUTFILENAME_ATTRIBUTE,
					SdEditImageGenerator.TYPE_ATTRIBUTE,
					SdEditImageGenerator.FORMAT_ATTRIBUTE));
		}
	};

	public SdEditBlockMacroProcessor(String macroName,
			Map<String, Object> config) {
		super(macroName, configs);
	}

	@Override
	protected Object process(AbstractBlock parent, String target,
			Map<String, Object> attributes) {
		// FIXME hack because in 1.5.0 attributes only contains a 'text' key,
		// not the real keys
		if (attributes.containsKey("text")) {
			attributes = hackConvertWrongAttributes(AsciidoctorHelpers
					.getAttribute(attributes, "text", "", true));
		}
		String outputFileName = "missing.png";
		try {
			Map<String, Object> docAttributes = parent.document()
					.getAttributes();
			String content = FileUtils.readFileToString(new File(
					AsciidoctorHelpers.getAttribute(docAttributes, "docdir",
							null, false), target));
			if (!attributes
					.containsKey(SdEditImageGenerator.OUTPUTFILENAME_ATTRIBUTE)) {
				attributes.put(SdEditImageGenerator.OUTPUTFILENAME_ATTRIBUTE,
						FilenameUtils.getBaseName(target));
			}
			ImageGenerator generator = new SdEditImageGenerator(
					AsciidoctorHelpers.getAttribute(docAttributes, "docdir",
							null, false));
			outputFileName = generator.generateImage(content,
					AsciidoctorHelpers.getImageDir(docAttributes), attributes);

		} catch (Exception e) {
			// ignore we output missing.png
		}
		return AsciidoctorHelpers.createImageBlock(outputFileName, attributes,
				parent, this);

	}

	private Map<String, Object> hackConvertWrongAttributes(String object) {
		Map<String, Object> newMap = new HashMap<String, Object>();

		String[] args = object.split(",");
		for (String arg : args) {
			String[] keyValue = arg.split("=");
			if (keyValue.length > 1) {
				newMap.put(keyValue[0], keyValue[1]);
			}
		}
		return newMap;
	}

}
