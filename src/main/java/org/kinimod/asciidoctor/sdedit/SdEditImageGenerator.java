package org.kinimod.asciidoctor.sdedit;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import net.sf.sdedit.config.Configuration;
import net.sf.sdedit.config.ConfigurationManager;
import net.sf.sdedit.diagram.Diagram;
import net.sf.sdedit.editor.DiagramLoader;
import net.sf.sdedit.error.SemanticError;
import net.sf.sdedit.error.SyntaxError;
import net.sf.sdedit.server.Exporter;
import net.sf.sdedit.text.TextHandler;
import net.sf.sdedit.ui.ImagePaintDevice;
import net.sf.sdedit.ui.components.configuration.Bean;
import net.sf.sdedit.util.DocUtil.XMLException;
import net.sf.sdedit.util.ObjectFactory;
import net.sf.sdedit.util.Pair;

public class SdEditImageGenerator implements ImageGenerator {

	private static final String DEFAULT_FORMAT_A4 = "A4";
	private static final String ORIENTATION_ATTRIBUTE = "orientation";
	private static final String PORTRAIT_ORIENTATION = "Portrait";
	private static final String LANDSCAPE_ORIENTATION = "Landscape";
	public static final String OUTPUTFILENAME_ATTRIBUTE = "outputfilename";
	public static final String FORMAT_ATTRIBUTE = "format";
	public static final String TYPE_ATTRIBUTE = "type";
	public static final String DEFAULT_TYPE_PNG = "png";

	@Override
	public String generateImage(File inputFile, File outputDir,
			Map<String, Object> attributes) {
		String type = AsciidoctorHelpers.getAttribute(attributes,
				TYPE_ATTRIBUTE, DEFAULT_TYPE_PNG, false);
		String outputFileName = AsciidoctorHelpers.getAttribute(attributes,
				OUTPUTFILENAME_ATTRIBUTE, "sdedit", true);
		outputFileName += "." + type;

		File outputFile = new File(outputDir, outputFileName);

		try {
			createImageFromFile(inputFile, outputFile, attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return outputFileName;
	}

	@Override
	public String generateImage(String content, File outputDir,
			Map<String, Object> attributes) {
		String type = AsciidoctorHelpers.getAttribute(attributes,
				TYPE_ATTRIBUTE, DEFAULT_TYPE_PNG, false);
		String outputFileName = AsciidoctorHelpers.getAttribute(attributes,
				OUTPUTFILENAME_ATTRIBUTE, "sdedit", true);
		outputFileName += "." + type;

		File outputFile = new File(outputDir, outputFileName);

		try {
			createImageFromString(content, outputFile, attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return outputFileName;
	}

	private void createImageFromFile(File inFile, File outFile,
			Map<String, Object> attributes) throws IOException, XMLException,
			SyntaxError, SemanticError {
		InputStream in = null;
		in = new FileInputStream(inFile);
		createImageFromInputStream(outFile, attributes, in);
	}

	private void createImageFromString(String content, File outFile,
			Map<String, Object> attributes) throws FileNotFoundException,
			SemanticError, SyntaxError, IOException, XMLException {
		InputStream in = new ByteArrayInputStream(content.getBytes("utf-8"));
		createImageFromInputStream(outFile, attributes, in);
	}

	private void createImageFromInputStream(File outFile,
			Map<String, Object> attributes, InputStream in)
			throws FileNotFoundException, IOException, XMLException,
			SemanticError, SyntaxError {
		String type = AsciidoctorHelpers.getAttribute(attributes,
				TYPE_ATTRIBUTE, DEFAULT_TYPE_PNG, true);
		String format = AsciidoctorHelpers.getAttribute(attributes,
				FORMAT_ATTRIBUTE, DEFAULT_FORMAT_A4, true);
		String orientation = AsciidoctorHelpers.getAttribute(attributes,
				ORIENTATION_ATTRIBUTE, PORTRAIT_ORIENTATION, true);
		if (orientation.length() > 0) {
			orientation = orientation.substring(0, 1).toUpperCase()
					+ orientation.substring(1);
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(outFile);
			try {
				Pair<String, Bean<Configuration>> pair = DiagramLoader.load(in,
						ConfigurationManager.getGlobalConfiguration()
								.getFileEncoding());
				TextHandler th = new TextHandler(pair.getFirst());
				Bean<Configuration> conf = pair.getSecond();
				configure(conf, attributes);
				if (type.equals("png")) {
					ImagePaintDevice paintDevice = new ImagePaintDevice();
					new Diagram(conf.getDataObject(), th, paintDevice)
							.generate();
					paintDevice.writeToStream(out);
				} else {
					Exporter paintDevice = Exporter.getExporter(type,
							orientation, format, out);
					new Diagram(conf.getDataObject(), th, paintDevice)
							.generate();
					paintDevice.export();
				}
				out.flush();
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

	private void configure(Bean<Configuration> conf, Map<?, Object> attributes) {
		for (Object optionKeyObject : attributes.keySet()) {
			if (optionKeyObject instanceof String) {
				String optionKey = (String) optionKeyObject;

				PropertyDescriptor property = conf.getProperty(optionKey);
				if (property != null) {
					Object value = ObjectFactory.createFromString(property
							.getPropertyType(), AsciidoctorHelpers
							.getAttribute((Map<String, Object>) attributes,
									optionKey, "", true));
					conf.setValue(property, value);
				}
			}
		}
	}
}
