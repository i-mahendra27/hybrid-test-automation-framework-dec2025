package api.payloads;

import helpers.DataFakerHelper;
import helpers.PasswordHelper;
import managers.ConfigManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class AuthPayloads {
    private AuthPayloads() {
        // Utility class
    }

    public static Map<String, Object> validLoginPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", ConfigManager.getValidLoginEmail());
        payload.put("password", ConfigManager.getValidLoginPassword());
        return payload;
    }

    public static Map<String, Object> invalidLoginPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", ConfigManager.getValidLoginEmail());
        payload.put("password", ConfigManager.getValidLoginPassword() + "_wrong");
        return payload;
    }

    public static Map<String, Object> missingFieldLoginPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", ConfigManager.getValidLoginEmail());
        return payload;
    }

    public static Map<String, Object> unregisteredLoginPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", "unregistered-" + UUID.randomUUID() + "@example.com");
        payload.put("password", ConfigManager.getValidLoginPassword());
        return payload;
    }

    public static Map<String, Object> registerPayload(){
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", DataFakerHelper.getFaker().internet().emailAddress());
        payload.put("password", PasswordHelper.generateValidPassword());
        return payload;
    }

    public static Map<String, Object> registerPayloadWithInvalidEmailFormat() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", "invalid-email");
        payload.put("password", PasswordHelper.generateValidPassword());
        return payload;
    }

    public static Map<String, Object> registerPayloadWithPasswordTooShort() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", "password-too-short-" + UUID.randomUUID() + "@example.com");
        payload.put("password", "123");
        return payload;
    }

    public static Map<String, Object> registerPayloadWithInvalidEmailFormatAndPasswordTooShort() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", "invalid-email");
        payload.put("password", "123");
        return payload;
    }

    public static Map<String, Object> registerPayloadWithRegisteredEmail() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", ConfigManager.getValidLoginEmail());
        payload.put("password", PasswordHelper.generateValidPassword());
        return payload;
    }
}
