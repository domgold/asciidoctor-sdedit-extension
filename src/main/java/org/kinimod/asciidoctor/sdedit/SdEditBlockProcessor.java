package org.kinimod.asciidoctor.sdedit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

public class SdEditBlockProcessor extends BlockProcessor {

	private static Map<String, Object> configs = new HashMap<String, Object>() {
		{
			put("contexts", Arrays.asList(":listing", ":literal", ":open"));
			put("content_model", ":simple");
			put("pos_attrs", Arrays.asList(
					SdEditImageGenerator.OUTPUTFILENAME_ATTRIBUTE,
					SdEditImageGenerator.TYPE_ATTRIBUTE,
					SdEditImageGenerator.FORMAT_ATTRIBUTE));
		}
	};

	public SdEditBlockProcessor(String name, Map<String, Object> config) {
		super(name, configs);
	}

	@Override
	public Object process(AbstractBlock parent, Reader reader,
			Map<String, Object> attributes) {
		String content = reader.read();
		ImageGenerator generator = new SdEditImageGenerator();
		String outputFileName = generator.generateImage(content,
				AsciidoctorHelpers.getImageDir(parent.document()
						.getAttributes()), attributes);
		return AsciidoctorHelpers.createImageBlock(outputFileName, attributes,
				parent, this);
	}

}