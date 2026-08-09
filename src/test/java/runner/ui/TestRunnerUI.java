package runner.ui;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import managers.ConfigManager;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

@CucumberOptions(
        features = "src/test/java/features/UI",
        glue = {"steps", "steps.ui", "hooks"},
        tags = "",
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-reports/ui-report.html",
                "json:target/cucumber-reports/ui-report.json",
                "hooks.ReportHandler"
        }
)
public class TestRunnerUI extends AbstractTestNGCucumberTests {
    @Override
    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context) {
        ConfigManager.configureCucumberTagsForRunner("@ui");
        super.setUpClass(context);
    }
}
