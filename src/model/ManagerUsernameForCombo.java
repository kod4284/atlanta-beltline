package model;

public class ManagerUsernameForCombo {
    private final String manager;
    private final String username;

    public ManagerUsernameForCombo(String manager, String username) {
        this.manager = manager;
        this.username = username;
    }

    public String getManager() {
        return manager;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return manager;
    }
}
