package org.kinimod.asciidoctor.sdedit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.Options;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;
import org.asciidoctor.internal.RubyHashUtil;
import org.jruby.RubyHash;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

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
		// String content = reader.read();
		//
		// String outputFileName = generator.generateImage(content,
		// AsciidoctorHelpers.getImageDir(parent.document().getAttributes()),
		// attributes);
		// return AsciidoctorHelpers.createImageBlock(outputFileName,
		// attributes, parent, this);
		// Block b = createBlock(parent, "section",
		// "|===\n|a |b \n|1 |2 \n|===", attributes, new HashMap<Object,
		// Object>());
		// String t = b.render();
		return createSection(parent, " Hello.\n\n== Subsection\n\nAha.\n\n",
				attributes);
	}

	public Object createSection(AbstractBlock parent, String content,
			Map<String, Object> attributes) {
		IRubyObject rubyClass = rubyRuntime
				.evalScriptlet("Asciidoctor::Section");
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(Options.SOURCE, content);
		options.put(Options.ATTRIBUTES, attributes);
		RubyHash convertMapToRubyHashWithSymbols = RubyHashUtil
				.convertMapToRubyHashWithSymbolsIfNecessary(rubyRuntime,
						options);
		// FIXME hack to ensure we have the underlying Ruby instance
		try {
			parent = parent.delegate();
		} catch (Exception e) {
		}

		// parent = nil, level = nil, numbered = true, opts = {}
		Object[] parameters = { parent, null, null,
				convertMapToRubyHashWithSymbols };
		return JavaEmbedUtils.invokeMethod(rubyRuntime, rubyClass, "new",
				parameters, Object.class);
	}
}