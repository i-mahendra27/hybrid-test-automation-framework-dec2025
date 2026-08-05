package hooks;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm;
import listeners.CucumberReportListener;
import managers.ConfigManager;
import utils.LogUtils;

public class ReportHandler implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        if (ConfigManager.isAllureReportEnabled()) {
            new AllureCucumber7Jvm().setEventPublisher(publisher);
            LogUtils.info("Allure Cucumber report plugin enabled");
        } else {
            LogUtils.info("Allure Cucumber report plugin disabled by config");
        }

        if (ConfigManager.isExtentReportEnabled()) {
            new ExtentCucumberAdapter("").setEventPublisher(publisher);
            new CucumberReportListener().setEventPublisher(publisher);
            LogUtils.info("Extent Cucumber report plugin enabled");
        } else {
            LogUtils.info("Extent Cucumber report plugin disabled by config");
        }
    }
}
