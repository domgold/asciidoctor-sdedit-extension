import gherkin.formatter.Formatter
import gherkin.formatter.PrettyFormatter
import gherkin.parser.Parser
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

String confFileContent = this.getClass().getResource( '/net/sf/sdedit/config/default.conf' ).text

def template_content = new File(project.build.testOutputDirectory, "../../src/test/resources/org/kinimod/sdedit_parameters.feature_template").text
def outputParentDir = new File(project.build.testOutputDirectory, "../../src/test/resources/org/kinimod/")
outputParentDir.exists() || outputParentDir.mkdirs()
def outputFile = new File(outputParentDir, "sdedit_parameters.feature")

def engine = new groovy.text.SimpleTemplateEngine()
def template = engine.createTemplate(template_content)

def conf = new XmlParser().parseText(confFileContent)

def params_all = conf["default-settings"].first().children().findAll() { it.attribute('name') != null }
def params_with_value = params_all.findAll() { it.attribute('value') != null }

// FIXME a little hack here : asciidoctor gets confused by attributes containing numbers (see https://github.com/asciidoctor/asciidoctorj/issues/203)
def params_hack_no_numbers = params_with_value.findAll() {  it.attribute('name') ==~ /[A-Za-z]+/ }

def binding=[parameters: params_hack_no_numbers]

def content = template.make(binding).toString()

def baos = new ByteArrayOutputStream()
def pw = new PrintWriter(baos);
def formatter = new PrettyFormatter(pw, true, false);

new Parser(formatter).parse(content, "", 0);
baos.flush();
def formatted = baos.toString();



outputFile.withWriter { out ->
    out.write(formatted)
}

println "Updated feature"