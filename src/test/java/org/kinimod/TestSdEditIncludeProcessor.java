/**
 * 
 */
package org.kinimod;

import java.io.File;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.junit.AfterClass;
import org.junit.Test;
import org.kinimod.asciidoctor.sdedit.SdEditBlockProcessor;
import org.kinimod.asciidoctor.sdedit.SdEditIncludeprocessor;
import org.kinimod.asciidoctor.sdedit.SdeditBlockMacroProcessor;

/**
 * @author Dominik
 * 
 */
public class TestSdEditIncludeProcessor {

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test() {
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		asciidoctor.javaExtensionRegistry().includeProcessor(
				SdEditIncludeprocessor.class);
		asciidoctor.javaExtensionRegistry().block("sdedit",
				SdEditBlockProcessor.class);
		asciidoctor.javaExtensionRegistry().blockMacro("sdedit",
				SdeditBlockMacroProcessor.class);
		File baseDir = new File("src/test/resources");
		File adoc = new File(baseDir, "sdinclude.adoc");
		File destDir = new File("target");
		AttributesBuilder attributes = AttributesBuilder.attributes()
				.attribute("outdir", destDir.getAbsolutePath());
		OptionsBuilder options = OptionsBuilder.options().safe(SafeMode.UNSAFE)
				.mkDirs(true).toDir(destDir).destinationDir(destDir)
				.baseDir(baseDir).attributes(attributes);
		asciidoctor.convertFile(adoc, options.asMap());
	}

}
