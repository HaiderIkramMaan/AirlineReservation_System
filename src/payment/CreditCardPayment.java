package payment;

public class CreditCardPayment extends Payment {

        private String cardNumber;
        private String cardHolderName;
        private String expiryDate; // stored as month/year format
        private String cvv;

        public CreditCardPayment(double amount, String cardNumber, String cardHolderName,
                                 String expiryDate, String cvv) {
            super(amount);
            this.cardNumber = cardNumber;
            this.cardHolderName = cardHolderName;
            this.expiryDate = expiryDate;
            this.cvv = cvv;
        }

        @Override
        public boolean processPayment() {
            if (!isCardValid()) {
                setStatus(STATUS_FAILED);
                return false;
            }

            // In a real system this is where a payment gateway call would happen.
            // Here we simulate a successful authorization.
            setStatus(STATUS_COMPLETED);
            return true;
        }

        private boolean isCardValid() {
            if (cardNumber == null || cardNumber.replaceAll("\\s", "").length() < 13) {
                return false;
            }
            if (cvv == null || !cvv.matches("\\d{3,4}")) {
                return false;
            }
            if (expiryDate == null || !expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                return false;
            }
            return cardHolderName != null && !cardHolderName.isBlank();
        }

        /**
         * Returns the card number with all but the last 4 digits masked,
         * e.g. "**** **** **** 1234".
         */
        public String maskCardNumber() {
            if (cardNumber == null || cardNumber.length() < 4) {
                return "****";
            }
            String digitsOnly = cardNumber.replaceAll("\\s", "");
            String lastFour = digitsOnly.substring(digitsOnly.length() - 4);
            return "**** **** **** " + lastFour;
        }

        public String getCardHolderName() {
            return cardHolderName;
        }

        public String getExpiryDate() {
            return expiryDate;
        }
    }
