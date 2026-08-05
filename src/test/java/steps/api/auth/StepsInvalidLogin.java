package steps.api.auth;

import api.context.ApiTestContext;
import api.payloads.AuthPayloads;
import helpers.ApiReportHelper;
import io.cucumber.java.en.Given;

public class StepsInvalidLogin {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("I prepare invalid login API payload")
    public void i_prepare_invalid_login_api_payload() {
        context.setRequestPayload(AuthPayloads.invalidLoginPayload());
        ApiReportHelper.attachPayloadEvidence("Invalid Login API Payload", context.getRequestPayload());
    }
}
