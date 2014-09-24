/**
 * 
 */
package org.kinimod;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.kinimod.asciidoctor.sdedit.SdEditBlockProcessor;
import org.kinimod.asciidoctor.sdedit.SdEditBlockMacroProcessor;

/**
 * @author Dominik
 * 
 */
public class TestSdEditIncludeProcessor {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder(new File("target"));

	@Test
	public void test() throws IOException {
		folder.create();
		Asciidoctor asciidoctor = Asciidoctor.Factory.create();
		asciidoctor.javaExtensionRegistry().block(
				SdEditBlockProcessor.SDEDIT_BLOCK_NAME,
				SdEditBlockProcessor.class);
		asciidoctor.javaExtensionRegistry().blockMacro(
				SdEditBlockMacroProcessor.SDEDIT_BLOCK_MACRO_NAME,
				SdEditBlockMacroProcessor.class);
		File baseDir = new File("src/test/resources");
		File adoc = new File(baseDir, "sdinclude.adoc");
		File destDir = folder.newFolder();
		AttributesBuilder attributes = AttributesBuilder.attributes()
				.attribute("outdir", destDir.getAbsolutePath());
		OptionsBuilder options = OptionsBuilder.options().safe(SafeMode.UNSAFE)
				.mkDirs(true).toDir(destDir).destinationDir(destDir)
				.baseDir(baseDir).attributes(attributes);
		asciidoctor.convertFile(adoc, options.asMap());

		assertTrue(new File(destDir, "sdinclude.html").exists());
		assertTrue(new File(destDir, "testdiagram.png").exists());
		assertTrue(new File(destDir, "fromblock.svg").exists());
	}

}
