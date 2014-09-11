package org.kinimod;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.junit.rules.TemporaryFolder;
import org.kinimod.asciidoctor.sdedit.SdEditBlockMacroProcessor;
import org.kinimod.asciidoctor.sdedit.SdEditBlockProcessor;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDef {

	File asciidocInputFile;
	File asciidocOutputFile;

	private Asciidoctor asciidoctor;
	private TemporaryFolder folder;
	private File outputFolder;
	private File inputFolder;

	@Before
	public void reset() throws IOException {
		folder = new TemporaryFolder(new File("target"));
		folder.create();
		inputFolder = folder.newFolder();
		outputFolder = folder.newFolder();
		asciidocInputFile = new File(inputFolder, "in.adoc");
		asciidoctor = Asciidoctor.Factory.create();
	}

	@Given("^the following asciidoctor content$")
	public void the_following_asciidoctor_content(String asciidocContent)
			throws Throwable {
		FileUtils.writeStringToFile(asciidocInputFile, asciidocContent);
	}

	@Given("^a file named \"(.*?)\" with the following content$")
	public void the_file_named_with_the_following_content(String filename,
			String content) throws Throwable {
		FileUtils.writeStringToFile(new File(inputFolder, filename), content);
	}

	@When("^I register the SdEditBlockProcessor$")
	public void i_register_the_SdEditBlockProcessor() throws Throwable {
		asciidoctor.javaExtensionRegistry().block("sdedit",
				SdEditBlockProcessor.class);
	}

	@When("^I register the SdEditBlockMacroProcessor$")
	public void i_register_the_SdEditBlockMacroProcessor() throws Throwable {
		asciidoctor.javaExtensionRegistry().blockMacro("sdedit",
				SdEditBlockMacroProcessor.class);
	}

	@When("^I render the asciidoctor content to html$")
	public void i_render_the_asciidoctor_content_to_html() throws Throwable {

		Attributes attrs = AttributesBuilder.attributes()
				.attribute("outdir", outputFolder.getAbsolutePath()).get();
		Options options = OptionsBuilder.options().safe(SafeMode.UNSAFE)
				.attributes(attrs).mkDirs(true).toDir(outputFolder)
				.destinationDir(outputFolder).baseDir(inputFolder)
				.backend("html5").get();
		asciidocOutputFile = new File(outputFolder,
				FilenameUtils.getBaseName(asciidocInputFile.getAbsolutePath())
						+ ".html");
		asciidoctor.convertFile(asciidocInputFile, options);
	}

	@Then("^the rendered file contains the following text snippets:$")
	public void the_rendered_file_contains_the_following_text_snippets(
			List<String> snippets) throws Throwable {
		String content = FileUtils.readFileToString(asciidocOutputFile);
		for (String snippet : snippets) {
			assertTrue(content.contains(snippet));
		}
	}

	@Then("^the file \"(.*?)\" exists in the output directory\\.$")
	public void the_file_exists_in_the_output_directory(String arg1)
			throws Throwable {
		assertTrue(new File(outputFolder, arg1).exists());
	}

}
