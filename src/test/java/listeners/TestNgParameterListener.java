package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.LogUtils;

import java.util.Map;

public class TestNgParameterListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        applyParameter(suite.getXmlSuite().getAllParameters(), "HEADLESS");
        applyParameter(suite.getXmlSuite().getAllParameters(), "BROWSER");
        applyParameter(suite.getXmlSuite().getAllParameters(), "env");
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
