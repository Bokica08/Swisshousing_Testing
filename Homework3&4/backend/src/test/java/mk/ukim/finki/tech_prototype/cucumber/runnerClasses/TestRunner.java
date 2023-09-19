package mk.ukim.finki.tech_prototype.cucumber.runnerClasses;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/mk/ukim/finki/tech_prototype/cucumber/featureFiles", // Location of your feature files
        glue = "mk.ukim.finki.tech_prototype.cucumber.stepDefinitions", // Package where your step definitions are located
        plugin = {"pretty", "html:target/cucumber-reports"} // Optional: Define output format and location
)
public class TestRunner {
}

