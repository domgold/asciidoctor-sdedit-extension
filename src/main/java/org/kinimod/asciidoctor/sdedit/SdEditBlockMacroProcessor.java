package org.kinimod.asciidoctor.sdedit;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockMacroProcessor;

/**
 * Defines a block macro.
 * <p>
 * Processes sdedit markup files to png or svg images.
 * </p>
 * 
 * @author Dominik
 */
public class SdEditBlockMacroProcessor extends BlockMacroProcessor {

	/**
	 * The block macro's name as a constant.
	 */
	public static final String SDEDIT_BLOCK_MACRO_NAME = "sdedit";

	@SuppressWarnings("serial")
	private static Map<String, Object> configs = new HashMap<String, Object>() {
		{
			put("content_model", ":attributes");
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
			outputFileName = "error.png";
		}
		return AsciidoctorHelpers.createImageBlock(outputFileName, attributes,
				parent, this);

	}

}
