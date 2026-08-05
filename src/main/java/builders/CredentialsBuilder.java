package builders;

import pages.dto.CredentialsDataObject;

import java.util.Map;

/**
 * Fluent builder for CredentialsDataObject.
 * Supports method chaining and partial builds.
 */
public class CredentialsBuilder extends BaseBuilder<CredentialsDataObject> {

    public static final String EMAIL = "userEmail";
    public static final String PASSWORD = "userPassword";
    public static final String CONFIRM_PASSWORD = "confirmPassword";

    // Static Factory Methods

    public static CredentialsBuilder create() {
        return new CredentialsBuilder();
    }

    public static CredentialsBuilder valid() {
        return create()
                .withValidEmail()
                .withValidPassword();
    }

    public static CredentialsBuilder invalid() {
        return create()
                .withInvalidEmail()
                .withInvalidPassword();
    }

    public static CredentialsDataObject of(String email, String password) {
        return create().withEmail(email).withPassword(password).build();
    }

    // Fluent Setters

    public CredentialsBuilder withEmail(String email) {
        return (CredentialsBuilder) put(EMAIL, email);
    }

    public CredentialsBuilder withPassword(String password) {
        return (CredentialsBuilder) put(PASSWORD, password);
    }

    public CredentialsBuilder withConfirmPassword(String confirmPassword) {
        return (CredentialsBuilder) put(CONFIRM_PASSWORD, confirmPassword);
    }

    public CredentialsBuilder withSamePasswords() {
        String password = get(PASSWORD);
        return (CredentialsBuilder) put(CONFIRM_PASSWORD, password);
    }

    // Predefined Credentials

    public CredentialsBuilder withValidEmail() {
        return withEmail("testuser@example.com");
    }

    public CredentialsBuilder withInvalidEmail() {
        return withEmail("invalid-email");
    }

    public CredentialsBuilder withEmptyEmail() {
        return withEmail("");
    }

    public CredentialsBuilder withValidPassword() {
        return withPassword("Test@123");
    }

    public CredentialsBuilder withInvalidPassword() {
        return withPassword("123");
    }

    public CredentialsBuilder withEmptyPassword() {
        return withPassword("");
    }

    // DataFaker Integration

    public CredentialsBuilder withRandomEmail() {
        return withEmail(helpers.DataFakerHelper.getFaker().internet().emailAddress());
    }

    public CredentialsBuilder withFakerEmail() {
        return withRandomEmail();
    }

    // Build

    @Override
    public CredentialsDataObject build() {
        return CredentialsDataObject.builder()
                .userEmail(get(EMAIL))
                .userPassword(get(PASSWORD))
                .confirmPassword(get(CONFIRM_PASSWORD))
                .build();
    }

    @Override
    protected BaseBuilder<CredentialsDataObject> self() {
        return this;
    }

    // Convenience Methods

    // Build as Map for API requests
    public Map<String, Object> buildAsMap() {
        Map<String, Object> map = new java.util.HashMap<>();
        if (has(EMAIL)) map.put("email", get(EMAIL));
        if (has(PASSWORD)) map.put("password", get(PASSWORD));
        if (has(CONFIRM_PASSWORD)) map.put("confirmPassword", get(CONFIRM_PASSWORD));
        return map;
    }

    // Build as JSON string
    public String buildAsJson() {
        return toJson();
    }
}
