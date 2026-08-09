package payment;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base class representing a payment made for a Booking.
 * Concrete payment methods (CreditCardPayment, CashPayment, BankTransferPayment)
 * extend this class and implement processPayment().
 */
public abstract class Payment {

    // Status constants (status is kept as String per the UML, these are just
    // the allowed values so we don't scatter magic strings everywhere)
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_REFUNDED = "REFUNDED";

    private String paymentId;
    private double amount;
    private LocalDateTime paymentDate;
    private String status;

    protected Payment(double amount) {
        this.paymentId = UUID.randomUUID().toString();
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.status = STATUS_PENDING;
    }

    // Allows subclasses / persistence layer to rebuild a Payment with a known id
    protected Payment(String paymentId, double amount, LocalDateTime paymentDate, String status) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    /**
     * Attempts to process this payment. Each payment type implements its own
     * validation/processing logic and is responsible for updating {@code status}
     * before returning.
     *
     * @return true if the payment was processed successfully, false otherwise
     */
    public abstract boolean processPayment();

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Refunds this payment, provided it was previously completed.
     * Subclasses may override this if a payment method needs extra
     * refund logic (e.g. contacting a bank API).
     *
     * @return true if the refund succeeded, false otherwise
     */
    public boolean refund() {
        if (!STATUS_COMPLETED.equals(status)) {
            return false;
        }
        this.status = STATUS_REFUNDED;
        return true;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    protected void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "paymentId='" + paymentId + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", status='" + status + '\'' +
                '}';
    }
}
