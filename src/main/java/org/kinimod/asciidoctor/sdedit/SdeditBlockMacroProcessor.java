package org.kinimod.asciidoctor.sdedit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockMacroProcessor;

public class SdeditBlockMacroProcessor extends BlockMacroProcessor {

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

	public SdeditBlockMacroProcessor(String macroName,
			Map<String, Object> config) {
		super(macroName, configs);
	}

	@Override
	protected Object process(AbstractBlock parent, String target,
			Map<String, Object> attributes) {
		// TODO Auto-generated method stub
		// RubyHash h;
		// h.fetch(rubyRuntime, key, block)
		return null;
	}

}
