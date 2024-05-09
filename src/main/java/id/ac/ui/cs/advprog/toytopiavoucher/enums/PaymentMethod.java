package id.ac.ui.cs.advprog.toytopiavoucher.enums;

public enum PaymentMethod {
    BANK_TRANSFER("BANK_TRANSFER"),
    CREDIT_CARD("CREDIT_CARD"),
    DIGITAL_WALLET("DIGITAL_WALLET"),
    ANY("ANY");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public static PaymentMethod fromString(String method) {
        for (PaymentMethod p : PaymentMethod.values()) {
            if (p.value.equals(method)) {
                return p;
            }
        }
        return null;
    }

    public static boolean contains(String test) {
        for (PaymentMethod pm : PaymentMethod.values()) {
            if (pm.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString() {
        return value;
    }
}
