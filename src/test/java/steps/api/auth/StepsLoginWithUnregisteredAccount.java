package steps.api.auth;

import api.context.ApiTestContext;
import api.payloads.AuthPayloads;
import helpers.ApiReportHelper;
import io.cucumber.java.en.Given;

public class StepsLoginWithUnregisteredAccount {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("I prepare unregistered login API payload")
    public void i_prepare_unregistered_login_api_payload() {
        context.setRequestPayload(AuthPayloads.unregisteredLoginPayload());
        ApiReportHelper.attachPayloadEvidence("Unregistered Login API Payload", context.getRequestPayload());
    }
}
