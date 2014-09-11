package org.kinimod;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.junit.rules.TemporaryFolder;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDef {

	public static class TableEntry {
		public String role;
		public String url;
		public String data;
	}

	public enum A {
		Ab, Bb, Cb
	};

	

	String asciidocContent;
	File asciidocInputFile;
	File asciidocOutputFile;
	private String result;

	private Asciidoctor asciidoctor;
	private TemporaryFolder folder;

	@Before
	public void reset() throws IOException {
		File parentFolder = new File("target/testoutput");
		if(!parentFolder.exists()) {
			if(!parentFolder.mkdirs()) {
				throw new RuntimeException("Error creating folder " + parentFolder.getAbsolutePath());
			}
		}
		folder = new TemporaryFolder(parentFolder);
		asciidocContent = null;
		folder.create();
		asciidocInputFile = new File(folder.getRoot(), "in.adoc");
		asciidoctor = Asciidoctor.Factory.create();
	}

	@Given("^a simple message (Ab)$")
	public void a_simple_message(A en) throws Throwable {
	}

	@Given("^a second message$")
	public void a_second_message() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// throw new PendingException();
	}

	@Given("^a table of entries$")
	public void a_table_of_entries(List<List<String>> test) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
		// E,K,V must be a scalar (String, Integer, Date, enum etc)
		throw new PendingException();
	}

	@Given("^the following asciidoctor content$")
	public void the_following_asciidoctor_content(String arg1) throws Throwable {
		asciidocContent = arg1.replace("<outdir>", folder.getRoot()
				.getAbsolutePath());
		FileUtils.writeStringToFile(asciidocInputFile, asciidocContent);
	}

	@Given("^the sdedit file named \"(.*?)\" with the following content$")
	public void the_file_named_with_the_following_content(String filename,
			String content) throws Throwable {
		FileUtils.writeStringToFile(new File(folder.getRoot(), filename),
				content);
	}

	@When("^I register the SdEditBlockProcessor$")
	public void i_register_the_SdEditBlockProcessor() throws Throwable {
		asciidoctor.javaExtensionRegistry().block("sdedit",
				org.kinimod.asciidoctor.sdedit.SdEditBlockProcessor.class);
	}

	@When("^I register the SdEditBlockMacroProcessor$")
	public void i_register_the_SdEditBlockMacroProcessor() throws Throwable {
		// asciidoctor.javaExtensionRegistry().blockMacro("sdedit",
		// org.kinimod.asciidoctor.sdedit.SdEditBlockMacroProcessor.class);
	}

	@When("^I render the asciidoctor content to html$")
	public void i_render_the_asciidoctor_content_to_html() throws Throwable {
		Options options = OptionsBuilder.options().backend("html5").get();
		asciidoctor.convertFile(asciidocInputFile, options);
	}

	@Then("^the file \"(.*?)\" exists$")
	public void the_file_exists(String file) {
		assertTrue(new File(folder.getRoot(), file).exists());
	}

	@Then("^the file \"(.*?)\" contains the text \"(.*?)\" and \"(.*?)\"$")
	public void the_rendered_document_contains_the_text_and(String file,
			String arg1, String arg2) throws Throwable {
		String content = FileUtils.readFileToString(new File(folder.getRoot(),
				file));
		assertTrue(content.contains(arg1));
		assertTrue(content.contains(arg2));
	}
}
