package tgpr.bank.model;



public abstract class Security {

    private static User loggedUser = null;

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void login(User user) {
        loggedUser = user;
    }

    public static boolean isManager() {
        return loggedUser != null && loggedUser.isManager();
    }

    public static boolean isLogged() {
        return loggedUser != null;
    }

    public static boolean isLoggedUser(User user) {
        return loggedUser.equals(user);
    }

    public static void logout() {
        login(null);
    }
    public static boolean isAdmin() {
        return true; // loggedUser != null && loggedUser.isAdmin();
    }

}




