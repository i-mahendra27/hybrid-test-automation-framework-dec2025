package context;

import pages.dto.CredentialsDataObject;
import pages.dto.RegisterFormDataObject;

/**
 * Domain-specific context for authentication operations.
 * Thread-safe for parallel execution.
 */
public class AuthContext {

    private static final ThreadLocal<CredentialsDataObject> credentials = new ThreadLocal<>();
    private static final ThreadLocal<RegisterFormDataObject> registerData = new ThreadLocal<>();

    // Credentials
    public void setCredentials(CredentialsDataObject data) {
        credentials.set(data);
    }

    public CredentialsDataObject getCredentials() {
        return credentials.get();
    }

    // Register data
    public void setRegisterData(RegisterFormDataObject data) {
        registerData.set(data);
    }

    public RegisterFormDataObject getRegisterData() {
        return registerData.get();
    }

    // Clear all auth context
    public void clear() {
        credentials.remove();
        registerData.remove();
    }

    // Check if has credentials
    public boolean hasCredentials() {
        return credentials.get() != null;
    }

    // Check if has register data
    public boolean hasRegisterData() {
        return registerData.get() != null;
    }
}
