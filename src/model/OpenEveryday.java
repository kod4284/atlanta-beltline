package model;

public enum  OpenEveryday {
    ALL("-- ALL --"),YES("Yes"), NO("No");

    final String isOpen;

    OpenEveryday(String isOpen) {
        this.isOpen = isOpen;
    }

    @Override
    public String toString() {
        return isOpen;
    }
}
