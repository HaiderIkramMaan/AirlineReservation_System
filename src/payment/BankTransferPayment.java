package payment;

import java.util.UUID;

/**
 * Payment made via direct bank transfer.
 */
public class BankTransferPayment extends Payment {

    private String accountNumber;
    private String bankName;
    private String transactionId;

    public BankTransferPayment(double amount, String accountNumber, String bankName) {
        super(amount);
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }

    @Override
    public boolean processPayment() {
        if (accountNumber == null || accountNumber.isBlank() || bankName == null || bankName.isBlank()) {
            setStatus(STATUS_FAILED);
            return false;
        }

        // Simulate contacting the bank and receiving a transaction reference.
        this.transactionId = generateTransactionId();
        setStatus(STATUS_COMPLETED);
        return true;
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
