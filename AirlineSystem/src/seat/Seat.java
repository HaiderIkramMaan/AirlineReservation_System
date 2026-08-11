package seat;

public class Seat {

    private String seatId;
    private String seatNo;
    private String seatClass;
    private boolean isAvailable;

    public Seat(String seatId, String seatNo, String seatClass) {
        this.seatId = seatId;
        this.seatNo = seatNo;
        this.seatClass = seatClass;
        this.isAvailable = true;
    }

    public void reserve() {
        if (!isAvailable) {
            throw new IllegalStateException("Seat " + seatNo + " is already reserved");
        }
        isAvailable = false;
    }

    public void release() {
        isAvailable = true;
    }

    public String getSeatId() {
        return seatId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}

