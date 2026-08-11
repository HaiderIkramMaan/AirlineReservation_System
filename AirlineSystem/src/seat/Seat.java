package seat;

public abstract class Seat {

    private final String seatId;
    private final String seatNumber;
    private final int row;
    private final int column;
    private boolean isAvailable;
    private final double priceMultiplier;

    protected Seat(String seatId, String seatNumber, int row, int column, double priceMultiplier) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.row = row;
        this.column = column;
        this.priceMultiplier = priceMultiplier;
        this.isAvailable = true;
    }


    public boolean book() {
        if (!isAvailable) {
            return false;
        }
        isAvailable = false;
        return true;
    }

    public void release() {
        isAvailable = true;
    }

    public double getPrice(double basePrice) {
        return basePrice * priceMultiplier;
    }


    public abstract String getLayoutColor();


    public abstract TravelClass getTravelClass();

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getSeatId() {
        return seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    protected double getPriceMultiplier() {
        return priceMultiplier;
    }
}
