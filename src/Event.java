public class Event {
    private String eventName;
    private int seatCount;
    private int reservedSeatCount;
    private final Seat[] seats;
    private final int eventId;
    private static int eventCount = 0;

    public Event(String eventName, int seatCount){
        if (seatCount <= 0){
            throw new IllegalArgumentException("Seat count must be greater than 0");
        } else if (eventName == null || eventName.isBlank()) {
            throw new IllegalArgumentException("Event name cannot be blank");
        }

        this.eventName = eventName;
        this.seatCount = seatCount;
        this.reservedSeatCount = 0;
        this.eventId = ++eventCount;

        seats = new Seat[seatCount];

        for(int i = 0; i < seatCount; i++){
            seats[i] = new Seat(i + 1);
        }
    }

    // Reserve and Cancel
    public boolean reserveSeat(int seatNumber) {
        if (seatNumber <= 0 || seatNumber > seatCount) {
            throw new IllegalArgumentException("Seat number must be between 1 and " + seatCount);
        }

        if(!seats[seatNumber - 1].isAvailable()) return false;

        seats[seatNumber - 1].setAvailable(false);
        reservedSeatCount++;
        return true;
    }
    public boolean cancelReservation(int seatNumber){
        if (seatNumber <= 0 || seatNumber > seatCount) {
            throw new IllegalArgumentException("Seat number must be between 1 and " + seatCount);
        }

        if(seats[seatNumber - 1].isAvailable()){
            return false;
        }

        seats[seatNumber - 1].setAvailable(true);
        reservedSeatCount--;
        return true;
    }

    // Deep copy, for preserving encapsulation
    public Seat[] listReservations(){
        if(reservedSeatCount == 0){
            return null;
        }

        Seat[] seats = new Seat[reservedSeatCount];

        for (int i = 0, j = 0; i < this.seats.length; i++) {
            if(this.seats[i].isAvailable()){
                continue;
            }
            seats[j] = new Seat(i + 1, false);
            j++;
        }

        return seats;
    }
    public Seat[] getSeats(){
        if(seatCount == 0){
            return null;
        }

        Seat[] seats = new Seat[seatCount];

        for (int i = 0; i < this.seats.length; i++) {
            seats[i] = new Seat(i + 1, this.seats[i].isAvailable());
        }

        return seats;
    }

    // Getters and Setters
    public int getSeatCount() {
        return seatCount;
    }
    public int getEventCount() {
        return eventCount;
    }
    public int getEventId(){
        return eventId;
    }
    public int getReservedSeatCount(){
        return reservedSeatCount;
    }

    @Override
    public String toString() {
        return "Event ID: " + eventId + '\n'
                + "Event name: " + eventName + '\n'
                + "Seat count: " + seatCount + '\n'
                + "Available Seats: " + (seatCount - reservedSeatCount) + '\n';
    }

    class Seat {
        private final int seatNumber;
        private boolean isAvailable;

        private Seat(int seatNumber) {
            this.seatNumber = seatNumber;
            isAvailable = true;
        }
        private Seat(int seatNumber, boolean isAvailable) {
            this.seatNumber = seatNumber;
            this.isAvailable = isAvailable;
        }

        public boolean isAvailable(){
            return isAvailable;
        }
        public void setAvailable(boolean available){
            isAvailable = available;
        }

        @Override
        public String toString() {
            String availability = isAvailable ? "Available" : "Reserved";
            return "Seat number: " + seatNumber + " | Availability: " + availability;
        }
    }
}

