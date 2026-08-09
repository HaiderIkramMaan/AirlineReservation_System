package payment;

import java.util.UUID;

/**
 * Payment made in cash at a physical counter/desk.
 */
public class CashPayment extends Payment {

    private String counterLocation;
    private String receiptNumber;

    public CashPayment(double amount, String counterLocation) {
        super(amount);
        this.counterLocation = counterLocation;
    }

    @Override
    public boolean processPayment() {
        if (counterLocation == null || counterLocation.isBlank()) {
            setStatus(STATUS_FAILED);
            return false;
        }

        // Cash payments are confirmed on the spot, so we just generate a receipt.
        this.receiptNumber = generateReceiptNumber();
        setStatus(STATUS_COMPLETED);
        return true;
    }

    private String generateReceiptNumber() {
        return "RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public String getCounterLocation() {
        return counterLocation;
    }
}
