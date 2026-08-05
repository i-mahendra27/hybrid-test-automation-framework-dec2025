package listeners;

import managers.ConfigManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.LogUtils;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = ConfigManager.getRetryCount();

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            LogUtils.warn("Retrying test: " + result.getName() + " (Attempt " + retryCount + " of " + MAX_RETRY_COUNT + ")");
            return true;
        }
        return false;
    }

    public void resetRetryCount() {
        retryCount = 0;
    }
}
