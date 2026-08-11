package seat;

public class BusinessSeat extends Seat {

    private static final double MULTIPLIER = 1.5;
    private static final String LAYOUT_COLOR = "#2196F3"; // blue

    public BusinessSeat(String seatId, String seatNumber, int row, int column) {
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
        return TravelClass.BUSINESS;
    }
}
