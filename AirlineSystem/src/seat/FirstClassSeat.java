package seat;

public class FirstClassSeat extends Seat {

    private static final double MULTIPLIER = 2.5;
    private static final String LAYOUT_COLOR = "#FFD700"; // gold

    public FirstClassSeat(String seatId, String seatNumber, int row, int column) {
        super(seatId, seatNumber, row, column, MULTIPLIER);
    }

    @Override
    public double getPrice(double basePrice) {
        return basePrice * MULTIPLIER;
    }

    @Override
    public String getLayoutColor() {
        return LAYOUT_COLOR;
    }

    @Override
    public TravelClass getTravelClass() {
        return TravelClass.FIRST_CLASS;
    }
}
