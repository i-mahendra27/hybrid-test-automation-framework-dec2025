package listeners;

import io.qameta.allure.testng.AllureTestNg;
import managers.ConfigManager;
import org.testng.IClassListener;
import org.testng.IConfigurationListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.List;

public class AllureReportListener implements ISuiteListener, ITestListener, IInvokedMethodListener,
        IClassListener,
        IConfigurationListener, IMethodInterceptor {

    private final AllureTestNg allureTestNg = new AllureTestNg();

    private boolean enabled() {
        return ConfigManager.isAllureReportEnabled();
    }

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        return enabled() ? allureTestNg.intercept(methods, context) : methods;
    }

    @Override
    public void onStart(ISuite suite) {
        if (enabled()) {
            allureTestNg.onStart(suite);
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        if (enabled()) {
            allureTestNg.onFinish(suite);
        }
    }

    @Override
    public void onStart(ITestContext context) {
        if (enabled()) {
            allureTestNg.onStart(context);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (enabled()) {
            allureTestNg.onFinish(context);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        if (enabled()) {
            allureTestNg.onTestStart(result);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (enabled()) {
            allureTestNg.onTestSuccess(result);
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (enabled()) {
            allureTestNg.onTestFailure(result);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (enabled()) {
            allureTestNg.onTestSkipped(result);
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        if (enabled()) {
            allureTestNg.onTestFailedButWithinSuccessPercentage(result);
        }
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        if (enabled()) {
            allureTestNg.onTestFailure(result);
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (enabled()) {
            allureTestNg.beforeInvocation(method, testResult);
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (enabled()) {
            allureTestNg.afterInvocation(method, testResult);
        }
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        if (enabled()) {
            allureTestNg.onConfigurationSuccess(itr);
        }
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        if (enabled()) {
            allureTestNg.onConfigurationFailure(itr);
        }
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        if (enabled()) {
            allureTestNg.onConfigurationSkip(itr);
        }
    }

    @Override
    public void onBeforeClass(ITestClass testClass) {
        if (enabled()) {
            allureTestNg.onBeforeClass(testClass);
        }
    }

    @Override
    public void onAfterClass(ITestClass testClass) {
        if (enabled()) {
            allureTestNg.onAfterClass(testClass);
        }
    }
}
