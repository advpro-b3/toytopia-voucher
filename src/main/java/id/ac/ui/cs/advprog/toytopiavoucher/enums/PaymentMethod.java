package id.ac.ui.cs.advprog.toytopiavoucher.enums;

public enum PaymentMethod {
    ANY("ANY"),
    BANK_TRANSFER("BANK_TRANSFER"),
    CREDIT_CARD("CREDIT_CARD"),
    DIGITAL_WALLET("DIGITAL_WALLET");

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

    public static boolean contains(String method) {
        PaymentMethod p = PaymentMethod.fromString(method);
        return p != null;
    }
    @Override
    public String toString() {
        return value;
    }
}
