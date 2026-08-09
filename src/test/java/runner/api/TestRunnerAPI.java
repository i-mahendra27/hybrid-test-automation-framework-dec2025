package runner.api;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import managers.ConfigManager;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;

@CucumberOptions(
        features = "src/test/java/features/API",
        glue = {"steps", "steps.api", "hooks"},
        tags = "",
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-reports/api-report.html",
                "json:target/cucumber-reports/api-report.json",
                "hooks.ReportHandler"
        }
)
public class TestRunnerAPI extends AbstractTestNGCucumberTests {
    @Override
    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context) {
        ConfigManager.configureCucumberTagsForRunner("@api");
        super.setUpClass(context);
    }
}
