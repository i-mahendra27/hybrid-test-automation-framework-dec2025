package steps.api.auth;

import api.assertions.AuthAssertions;
import api.context.ApiTestContext;
import api.payloads.AuthPayloads;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class StepsRegisterUser {
    private final ApiTestContext context = ApiTestContext.getInstance();
    private final AuthAssertions authAssertions = new AuthAssertions();

    @Given("I prepare register API payload")
    public void i_prepare_register_api_payload(){
        context.setRequestPayload(AuthPayloads.registerPayload());
        ApiReportHelper.attachPayloadEvidence("Register API Payload", context.getRequestPayload());
    }

    @Given("I prepare register API payload with invalid email format")
    public void i_prepare_register_api_payload_with_invalid_email_format() {
        context.setRequestPayload(AuthPayloads.registerPayloadWithInvalidEmailFormat());
        ApiReportHelper.attachPayloadEvidence("Register API Payload - Invalid Email Format", context.getRequestPayload());
    }

    @Given("I prepare register API payload with password too short")
    public void i_prepare_register_api_payload_with_password_too_short() {
        context.setRequestPayload(AuthPayloads.registerPayloadWithPasswordTooShort());
        ApiReportHelper.attachPayloadEvidence("Register API Payload - Password Too Short", context.getRequestPayload());
    }

    @Given("I prepare register API payload with invalid email format and password too short")
    public void i_prepare_register_api_payload_with_invalid_email_format_and_password_too_short() {
        context.setRequestPayload(AuthPayloads.registerPayloadWithInvalidEmailFormatAndPasswordTooShort());
        ApiReportHelper.attachPayloadEvidence("Register API Payload - Invalid Email And Short Password", context.getRequestPayload());
    }

    @Given("I prepare register API payload with registered email")
    public void i_prepare_register_api_payload_with_registered_email() {
        context.setRequestPayload(AuthPayloads.registerPayloadWithRegisteredEmail());
        ApiReportHelper.attachPayloadEvidence("Register API Payload - Registered Email", context.getRequestPayload());
    }

    @And("the register request body should match register request schema")
    public void the_register_request_body_should_match_register_request_schema() {
        ApiReportHelper.attachRequestSchemaEvidence("register-request.schema.json", context.getRequestPayload());
        authAssertions.assertRegisterRequestSchema(context.getRequestPayload());
    }
}
