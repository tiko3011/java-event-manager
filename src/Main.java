import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        List<Event> events = new ArrayList<>();
        Event currentEvent = null;
        Scanner scanner = new Scanner(System.in);

        showMenu();
        int currentChoice;

        while((currentChoice = makeChoice(scanner)) != 8){
            switch (currentChoice){
                case 1:
                    currentEvent = createEventAction(scanner);
                    events.add(currentEvent);
                    System.out.println("Event created. Event ID: " + currentEvent.getEventId());
                    break;
                case 2:
                    reserveSeatAction(currentEvent, scanner);
                    break;
                case 3:
                    cancelReservationAction(currentEvent, scanner);
                    break;
                case 4:
                    listSeatsAction(currentEvent);
                    break;
                case 5:
                    listReservationsAction(currentEvent);
                    break;
                case 6:
                    currentEvent = listEventsAction(events, scanner);
                    break;
                case 7:
                    listAvailableEventsAction(events);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

            System.out.println();
            showMenu();
        }

        System.out.println("Thank you for using our Event Manager!");
        scanner.close();
    }

    // Menu and Choice
    public static void showMenu(){
        System.out.print
                ("""
                        1. Create Event
                        2. Reserve Seat
                        3. Cancel Reservation
                        4. List Seats
                        5. List Reservations
                        6. Manage Events
                        7. List Available Events
                        8. Exit
                        \nYour Choice:\s""");
    }
    public static int makeChoice(Scanner scanner){
        try {
            return scanner.nextInt();
        } catch(InputMismatchException e){
            return -1;
        } finally{
            scanner.nextLine();
        }
    }

    // Input Event Details, Create event
    public static String inputEventName(Scanner scanner){
        System.out.print("Enter Event Name: ");
        String eventName = scanner.nextLine();

        while (eventName == null || eventName.isBlank()){
            System.out.print("\nInvalid Event Name!\nEnter Event Name: ");
            eventName = scanner.nextLine();
        }

        return eventName;
    }
    public static int inputSeatCount(Scanner scanner){
        System.out.print("Enter number of seats: ");
        int seatCount = 0;

        while (seatCount <= 0) {
            try {
                seatCount = scanner.nextInt();
                if (seatCount <= 0) {
                    System.out.print("\nInvalid seat count!\nTry again: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("\nInvalid input!\nTry again: ");
                scanner.nextLine();
            }
        }

        return seatCount;
    }
    public static Event createEvent(String eventName, int seatCount){
        return new Event(eventName, seatCount);
    }

    // Choice 1
    public static Event createEventAction(Scanner scanner){
        String eventName = inputEventName(scanner);
        int seatCount = inputSeatCount(scanner);
        return createEvent(eventName, seatCount);
    }

    // Choice 2
    public static void reserveSeatAction(Event event, Scanner scanner){
        Integer seatNumber = inputSeatNumber(event, scanner);
        if(seatNumber == null){
            return;
        }

        boolean isActionCompleted = event.reserveSeat(seatNumber);
        if (isActionCompleted) {
            System.out.println("Seat has been successfully reserved");
        } else {
            System.out.println("Seat is already reserved!");
        }
    }

    // Choice 3
    public static void cancelReservationAction(Event event, Scanner scanner){
        Integer seatNumber = inputSeatNumber(event, scanner);
        if(seatNumber == null){
            return;
        }

        boolean isActionCompleted = event.cancelReservation(seatNumber);
        if (isActionCompleted) {
            System.out.println("Reservation has been successfully cancelled");
        } else {
            System.out.println("You do not have reservation to cancel!");
        }
    }

    // Helper for Choice 2 and 3
    public static Integer inputSeatNumber(Event event, Scanner scanner) {
        if(event == null){
            System.out.println("There is no active event!");
            return null;
        }

        boolean isNumberInvalid = true;
        int seatNumber = 0;

        while (isNumberInvalid){
            System.out.print("Enter seat number: ");

            try {
                seatNumber = scanner.nextInt();
                if(seatNumber <= 0 || seatNumber > event.getSeatCount()){
                    scanner.nextLine();
                    System.out.println("Invalid seat!");
                } else {
                    isNumberInvalid = false;
                }
            }
            catch(InputMismatchException e){
                scanner.nextLine();
                System.out.println("Invalid input!");
            }
        }

        return seatNumber;
    }

    // Choice 4
    public static void listReservationsAction(Event event){
        if(event == null){
            System.out.println("There is no active event!");
            return;
        }

        Event.Seat[] reservedSeats = event.listReservations();

        if (reservedSeats == null) {
            System.out.println("You do not have reservations to list!");
        } else {
            for(var seat : reservedSeats){
                System.out.println(seat);
            }
        }
    }

    // Choice 5
    public static void listSeatsAction(Event event){
        if(event == null){
            System.out.println("There is no active event!");
            return;
        }

        Event.Seat[] seats = event.getSeats();

         for(var seat : seats){
             System.out.println(seat);
         }
    }

    // Choice 6
    public static Event listEventsAction(List<Event> events, Scanner scanner){
        if(events == null || events.isEmpty()){
            System.out.println("There are no active events!");
            return null;
        }

        System.out.println("\n##################");
        System.out.println("### Event List ###");
        System.out.println("##################\n");

        for (int i = 0; i < events.size(); i++) {
            System.out.println("Event #" + (i + 1) + ".\n" + events.get(i));
        }

        return setEvent(events, scanner);
    }

    // Go to event
    public static Event setEvent(List<Event> events, Scanner scanner){
        int eventId = 0;
        boolean isEventIdInvalid = true;

        while (isEventIdInvalid){
            System.out.print("Choose Event to manage: ");
            try {
                eventId = scanner.nextInt();
                for (Event event : events) {
                    if (event.getEventId() == eventId) {
                        isEventIdInvalid = false;
                        System.out.println("You are now managing event with ID: " + eventId);
                        return event;
                    }
                }
                System.out.println("Invalid event!");
                scanner.nextLine();
            } catch(InputMismatchException e){
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }

        throw new IllegalArgumentException("Event with ID " + eventId + " does not exist.");
    }

    public static List<Event> getNotFullEvents(List<Event> events){
        List<Event> notFullEvents = null;

        for (Event event : events){
            if (event.getSeatCount() - event.getReservedSeatCount() > 0){
                if(notFullEvents == null){
                    notFullEvents = new ArrayList<>();
                }
                notFullEvents.add(event);
            }
        }

        return notFullEvents == null ? new ArrayList<>() : notFullEvents;
    }

    public static void listAvailableEventsAction(List<Event> events){
        List<Event> notFullEvents = getNotFullEvents(events);

        if (notFullEvents.isEmpty()){
            System.out.println("There are no available events!");
            return;
        }

        System.out.println("\n###################");
        System.out.println("# Available Events #");
        System.out.println("#####################\n");

        for (Event event : notFullEvents) {
            System.out.println(event);
        }
    }
}