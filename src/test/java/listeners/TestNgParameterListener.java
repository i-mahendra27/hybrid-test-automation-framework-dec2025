package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.LogUtils;

import java.util.Map;

public class TestNgParameterListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        // Apply parameters from testng XML, but prioritize Maven system properties
        // Maven system properties (set via -D or pom.xml) take precedence over XML values
        applyParameter(suite.getXmlSuite().getAllParameters(), "HEADLESS");
        applyParameter(suite.getXmlSuite().getAllParameters(), "BROWSER");

        // For env, check if Maven already set it (via -D or pom.xml profile)
        String mavenEnv = System.getProperty("env");
        if (mavenEnv != null && !mavenEnv.trim().isEmpty()) {
            // Maven set the env, keep it (don't override from XML)
            LogUtils.info("Using Maven-provided environment: " + mavenEnv);
        } else {
            // Maven didn't set env, use XML value
            applyParameter(suite.getXmlSuite().getAllParameters(), "env");
        }
    }

    private void applyParameter(Map<String, String> parameters, String key) {
        String value = parameters.get(key);
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        System.setProperty(key, value.trim());
        LogUtils.info("TestNG parameter applied: " + key + "=" + value.trim());
    }
}
