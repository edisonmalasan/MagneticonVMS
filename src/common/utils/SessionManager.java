package common.utils;

import common.models.Volunteer;

public class SessionManager {
    private static Volunteer currentUser;

    public static void setCurrentUser(Volunteer volunteer) {
        currentUser = volunteer;
    }

    public static Volunteer getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }

    public static boolean isAdmin() {
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }
}