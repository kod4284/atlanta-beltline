package model;

public enum UserStatus {
    ALL("-- ALL --"), APPROVED("Approved"), PENDING("Pending"), DECLINED
            ("Declined");

    final String user_status;

    UserStatus(String type) {
        user_status = type;
    }

    @Override
    public String toString() {
        return user_status;
    }
}
