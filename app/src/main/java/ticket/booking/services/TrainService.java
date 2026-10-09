package ticket.booking.services;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TrainService {

    private List<Train> trains;

    private static final String TRAIN_DB_PATH = "app/src/main/java/ticket/booking/localDB/trains.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private void loadTrainList() throws IOException {
        File trainsFile = new File(TRAIN_DB_PATH);

        if (!trainsFile.exists() || trainsFile.length() == 0) {
            this.trains = new ArrayList<>();
            return;
        }
        this.trains = OBJECT_MAPPER.readValue(trainsFile, new TypeReference<List<Train>>() {});
    }

    // NEW: the opposite of loadTrainList(). Your TODO "update the trains.json" needs this -
    // every change to a train (booking a seat, freeing a seat, adding/deleting a train)
    // only lives in memory until it is written back here.
    private void saveTrainListToFile() throws IOException {
        File trainsFile = new File(TRAIN_DB_PATH);
        OBJECT_MAPPER.writeValue(trainsFile, this.trains);
    }

    public TrainService(){
        try {
            loadTrainList();
        }catch (IOException ex){
            System.out.println("Failed to fetch trains");
            this.trains = new ArrayList<>();
        }
    }

    public List<Train> fetchTrains(String source,String destination){
        return this.trains.stream()
                .filter(train -> {
                    Map<String, Date> stations = train.getStations();
                    if (stations == null || !stations.containsKey(source) || !stations.containsKey(destination)) {
                        return false;
                    }
                    return stations.get(source).before(stations.get(destination));
                })
                .collect(Collectors.toList());
    }

    public void trainInfo(Train train){
        System.out.println("--------------------");
        // BEFORE: "Train No" + ... printed "Train No12345" with no space.
        // AFTER:  added ": " so the number is readable.
        System.out.println("Train No: " + train.getTrainNo());
        System.out.println("Train Id: " + train.getTrainId());
        System.out.println("--------------------");
    }

    public void trainsInfo(List<Train> trains){
        if (trains.isEmpty()){
            System.out.println("No train available");
            return;
        }

        for (Train train : trains) {
            trainInfo(train);
        }
    }

    public Train bookTicketTrain(String trainNo){
        // BEFORE: t.getTrainNo().equals(trainNo)
        // AFTER:  trainNo.equals(t.getTrainNo())
        // REASON: a train in the JSON with no trainNo would make t.getTrainNo() null and crash.
        //         trainNo (what the user typed) is never null, so calling .equals on it is safe.
        Optional<Train> trainToBook = this.trains.stream().filter(t -> trainNo.equals(t.getTrainNo())).findFirst();
        return trainToBook.orElse(null);
    }

    // NEW: find a train by its id. Cancelling uses this - a ticket stores the trainId, not the Train object.
    public Train findTrainById(String trainId){
        return this.trains.stream().filter(t -> trainId.equals(t.getTrainId())).findFirst().orElse(null);
    }

    public void fetchAvailableTrainSeat(Train train){
        List<List<Integer>> trainSeats = train.getSeats();
        // BEFORE: no check - a train without seats in the JSON crashed with a NullPointerException.
        if (trainSeats == null) {
            System.out.println("This train has no seats");
            return;
        }
        System.out.println("Free seats (row seat):");
        for(int i = 0 ; i < trainSeats.size() ; i++){
            for(int j = 0; j < trainSeats.get(i).size(); j++){
                if(trainSeats.get(i).get(j).equals(0)){
                    System.out.println("seat number : "+i+" "+j);
                }
            }
        }
    }

    // Small helper: is (row, column) a real seat in this train?
    // Without this, a typo like row 99 throws IndexOutOfBoundsException and crashes the app.
    private boolean isValidSeat(List<List<Integer>> seats, int row, int column){
        return seats != null
                && row >= 0 && row < seats.size()
                && column >= 0 && column < seats.get(row).size();
    }

    // BEFORE: bookingTrainSeat(Train train, String seatNumber)
    //           - split the input with seatNumber.split(""), which splits into single CHARACTERS:
    //             "12" became ["1","2"], so row 12 or seat 10+ could never be booked,
    //             and "1 2" became ["1"," ","2"].
    //           - always printed "Ticket booked succesfully" without booking anything.
    //           - had no return statement, so the file didn't compile.
    // AFTER:  takes row and column as ints (App reads them as two numbers), then does your TODOs:
    //           1) seat must be 0 (free), else return false
    //           2) set it to 1 (booked)
    //           3) save trains.json
    // REASON: separate numbers avoid all the string-splitting problems, and returning true/false lets
    //         App decide what to do next (only create a ticket if the seat was really booked).
    public boolean bookingTrainSeat(Train train, int row, int column){
        List<List<Integer>> trainSeats = train.getSeats();

        // Step 1: bad cases first.
        if (!isValidSeat(trainSeats, row, column)) {
            System.out.println("No such seat");
            return false;
        }
        if (!trainSeats.get(row).get(column).equals(0)) {
            System.out.println("Seat is already booked");
            return false;
        }

        // Step 2: book it. trainSeats is the same list object that lives inside this.trains,
        // so changing it changes what saveTrainListToFile() writes (same idea as this.user in UserBookingService).
        trainSeats.get(row).set(column, 1);

        // Step 3: save. If saving fails, undo the change so memory and file stay the same.
        try {
            saveTrainListToFile();
            return true;
        } catch (IOException ex) {
            trainSeats.get(row).set(column, 0);
            System.out.println("Failed to save booking");
            return false;
        }
    }

    // NEW: the opposite of bookingTrainSeat - used when a ticket is cancelled.
    public boolean freeTrainSeat(String trainId, int row, int column){
        Train train = findTrainById(trainId);
        if (train == null || !isValidSeat(train.getSeats(), row, column)) {
            return false;
        }
        train.getSeats().get(row).set(column, 0);
        try {
            saveTrainListToFile();
            return true;
        } catch (IOException ex) {
            train.getSeats().get(row).set(column, 1);
            return false;
        }
    }

    // given below methods should be called by admins only
    protected void addTrain(Train train){
        // BEFORE: this.trains.add(train); loadTrainList();
        // AFTER:  this.trains.add(train); saveTrainListToFile();
        // REASON: loadTrainList() READS the file and replaces this.trains with what's on disk -
        //         so the train you just added was thrown away immediately. Adding must SAVE.
        try {
            this.trains.add(train);
            saveTrainListToFile();
        } catch (IOException e) {
            this.trains.remove(train);
            System.out.println("Failed to add train");
        }
    }

    protected void deleteTrain(String trainId){
        // BEFORE: found the train with a stream, then this.trains.remove(trainToDelete);
        // AFTER:  removeIf(...), then save.
        // REASON: 1) trainToDelete was an Optional<Train>, not a Train. remove(Optional) looks for an
        //            Optional inside a List<Train>, never finds one, and silently removes nothing.
        //         2) when the train wasn't found it printed a message but kept going (no return).
        //         3) the change was never saved to trains.json.
        boolean removed = this.trains.removeIf(train -> trainId.equals(train.getTrainId()));
        if (!removed){
            System.out.println("No such train is present");
            return;
        }
        try {
            saveTrainListToFile();
        } catch (IOException e) {
            System.out.println("Failed to delete train");
        }
    }

}
