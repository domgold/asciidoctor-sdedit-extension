package org.kinimod;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.junit.rules.TemporaryFolder;
import org.kinimod.asciidoctor.sdedit.SdEditBlockMacroProcessor;
import org.kinimod.asciidoctor.sdedit.SdEditBlockProcessor;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDef {

	private static final String STROKE_DASHARRAY_5_5 = "stroke-dasharray=\"5,5\"";
	File asciidocInputFile;
	File asciidocOutputFile;

	private Asciidoctor asciidoctor;
	private TemporaryFolder folder;
	private File outputFolder;
	private File inputFolder;

	@Before
	public void scenario(Scenario scenario) {

	}

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

	public enum Switch {
		enabling, disabling
	}

	@Given("^an sdedit config file named \"(.*?)\" (enabling|disabling) return arrows is in the same folder as the source document$")
	public void an_sdedit_config_file_named_turning_on_off_return_arrows_is_in_the_same_folder_as_the_source_document(
			String confFilename, Switch arrowState) throws Throwable {
		File sdEditConfigFile = folder.newFile();
		String switchState = arrowState.equals(Switch.enabling) ? "true"
				: "false";
		InputStream resourceAsStream = ClassLoader.getSystemClassLoader()
				.getResourceAsStream("net/sf/sdedit/config/default.conf");
		String string = IOUtils.toString(resourceAsStream, "UTF-8");
		String replaceAll = string.replaceAll(
				"<property name=\"returnArrowVisible\" value=\".*\" />",
				"<property name=\"returnArrowVisible\" value=\"" + switchState
						+ "\" />");
		FileUtils.writeStringToFile(sdEditConfigFile, replaceAll);
		FileUtils.copyFile(sdEditConfigFile,
				new File(inputFolder, confFilename));
	}

	@When("^I register the SdEditBlockProcessor$")
	public void i_register_the_SdEditBlockProcessor() throws Throwable {
		asciidoctor.javaExtensionRegistry().block(
				SdEditBlockProcessor.SDEDIT_BLOCK_NAME,
				SdEditBlockProcessor.class);
	}

	@When("^I register the SdEditBlockMacroProcessor$")
	public void i_register_the_SdEditBlockMacroProcessor() throws Throwable {
		asciidoctor.javaExtensionRegistry().blockMacro(
				SdEditBlockMacroProcessor.SDEDIT_BLOCK_MACRO_NAME,
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
		File f = asciidocOutputFile;
		assertFileContainsSnippets(snippets, f);
	}

	@Then("^the file \"(.*?)\" exists in the output directory\\.$")
	public void the_file_exists_in_the_output_directory(String arg1)
			throws Throwable {
		File file = new File(outputFolder, arg1);
		assertTrue(
				String.format("File expected to exist: %s",
						file.getAbsolutePath()), file.exists());
	}

	@Then("^the file \"(.*?)\" contains ([0-9]) return arrows\\.$")
	public void the_file_contains_num_return_arrows(String filename,
			int num_return_arrows) throws Throwable {
		String content = FileUtils.readFileToString(new File(outputFolder,
				filename));
		assertEquals(String.format("expected %s times %s in:\n%s",
				STROKE_DASHARRAY_5_5, num_return_arrows, content),
				num_return_arrows,
				content.split(STROKE_DASHARRAY_5_5).length - 1);
	}

	public void assertFileContainsSnippets(List<String> snippets, File f)
			throws IOException {
		String content = FileUtils.readFileToString(f);
		for (String snippet : snippets) {
			assertTrue(String.format("%s expected in :\n%s", snippet, content),
					content.contains(snippet));
		}
	}

}
