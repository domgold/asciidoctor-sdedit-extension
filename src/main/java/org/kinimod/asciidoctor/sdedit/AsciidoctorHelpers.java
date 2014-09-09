package org.kinimod.asciidoctor.sdedit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.Processor;
import org.jruby.RubyString;

public final class AsciidoctorHelpers {

	private static final String IMAGESOUTDIR_ATTRIBUTE = "imagesoutdir";
	private static final String TARGET_ATTRIBUTE = "target";
	private static final String IMAGE = "image";
	private static final String DOCDIR_ATTRIBUTE = "docdir";
	private static final String OUTDIR_ATTRIBUTE = "outdir";
	private static final String IMAGESDIR_ATTRIBUTE = "imagesdir";

	/**
	 * Gets a value from an asciidoctor attributes map by key.
	 * 
	 * @param attributes
	 *            an asciidoctor attributes map
	 * @param name
	 *            the key to look for
	 * @param defaultValue
	 * @param remove
	 *            wether or not to remove the looked up attribute
	 * @return The attribute value or the given default value (if default value
	 *         is null, the return value might also be null.)
	 */
	public static String getAttribute(Map<String, Object> attributes,
			String name, String defaultValue, boolean remove) {
		String type = defaultValue;
		if (attributes.containsKey(name)) {
			if (remove) {
				Object removedObject = attributes.remove(name);
				if (removedObject instanceof RubyString) {
					type = ((RubyString) removedObject).decodeString();
				} else if (removedObject instanceof String) {
					type = (String) removedObject;
				} else {
					type = removedObject.toString();
				}
			} else {
				Object retrievedObject = attributes.get(name);
				if (!(retrievedObject instanceof String)) {
					throw new RuntimeException(String.format(
							"Did not expect %s in attributes map.",
							retrievedObject.getClass().getName()));
				}
				type = (String) retrievedObject;
			}
		}
		return type;
	}

	public static Object createImageBlock(String target,
			Map<String, Object> attributes, AbstractBlock parent, Processor proc) {
		attributes.put(TARGET_ATTRIBUTE, target);
		return proc.createBlock(parent, IMAGE, new ArrayList<String>(),
				attributes, new HashMap<Object, Object>());

	}

	public static File getImageDir(Map<String, Object> documentAttributes) {
		File outDir = null;
		String dir = getAttribute(documentAttributes, IMAGESOUTDIR_ATTRIBUTE,
				null, false);
		if (dir == null) {
			dir = getAttribute(documentAttributes, OUTDIR_ATTRIBUTE, null,
					false);
		}
		if (dir == null) {
			dir = getAttribute(documentAttributes, DOCDIR_ATTRIBUTE, "", true);
		}
		if ("".equals(dir)) {
			throw new RuntimeException(
					"Invalid docdir, consider changing safemode to unsafe.");
		}
		String imagesdir = AsciidoctorHelpers.getAttribute(documentAttributes,
				IMAGESDIR_ATTRIBUTE, "", false);
		if (!("".equals(imagesdir))) {
			outDir = new File(dir, imagesdir);
		} else {
			outDir = new File(dir);
		}
		return outDir;
	}
}
