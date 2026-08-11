package seat;

public class EconomySeat extends Seat {

    private static final double MULTIPLIER = 1.0;
    private static final String LAYOUT_COLOR = "#4CAF50"; // green

    public EconomySeat(String seatId, String seatNumber, int row, int column) {
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
        return TravelClass.ECONOMY;
    }
}
