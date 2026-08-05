package steps.api.auth;

import api.context.ApiTestContext;
import api.payloads.AuthPayloads;
import helpers.ApiReportHelper;
import io.cucumber.java.en.Given;

public class StepsSuccessLogin {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("I prepare valid login API payload")
    public void i_prepare_valid_login_api_payload() {
        context.setRequestPayload(AuthPayloads.validLoginPayload());
        ApiReportHelper.attachPayloadEvidence("Valid Login API Payload", context.getRequestPayload());
    }
}
