package org.kinimod;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(monochrome = true, format = { "pretty",
		"html:target/cucumber", "rerun:target/rerun.txt" }
// ,tags= {"@now"}
)
public class RunCucumberTest {

}
