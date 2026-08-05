package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.EmailSendUtils;
import utils.LogUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailReportListener implements ISuiteListener, ITestListener {
    private static final int PASSED = ITestResult.SUCCESS;
    private static final int FAILED = ITestResult.FAILURE;
    private static final int SKIPPED = ITestResult.SKIP;

    private final Map<String, Integer> scenarioResults = new ConcurrentHashMap<>();
    private long suiteStartMillis;

    @Override
    public void onStart(ISuite suite) {
        suiteStartMillis = System.currentTimeMillis();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        scenarioResults.put(resultKey(result), PASSED);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        scenarioResults.put(resultKey(result), FAILED);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        scenarioResults.putIfAbsent(resultKey(result), SKIPPED);
    }

    @Override
    public void onFinish(ISuite suite) {
        int passed = count(PASSED);
        int failed = count(FAILED);
        int skipped = count(SKIPPED);
        int total = passed + failed + skipped;

        if (total == 0) {
            LogUtils.warn("No TestNG results captured. Email report skipped.");
            return;
        }

        LogUtils.info("Email report summary: total=" + total
                + ", passed=" + passed
                + ", failed=" + failed
                + ", skipped=" + skipped);
        long durationMillis = Math.max(0, System.currentTimeMillis() - suiteStartMillis);
        EmailSendUtils.sendEmail(total, passed, failed, skipped, durationMillis);
    }

    private int count(int status) {
        return (int) scenarioResults.values().stream()
                .filter(result -> result == status)
                .count();
    }

    private String resultKey(ITestResult result) {
        return result.getMethod().getQualifiedName() + Arrays.deepToString(result.getParameters());
    }
}
