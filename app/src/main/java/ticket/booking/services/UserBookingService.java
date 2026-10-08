package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;

    private List<User> userList;

    private static final String USER_DB_PATH = "app/src/main/java/ticket/booking/localDB/users.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private void loadUserListFromFile() throws IOException {
        File users = new File(USER_DB_PATH);
        // A missing or empty file just means "no users yet" - start with an empty list
        // instead of crashing (Jackson can't read a 0-byte file).
        if (!users.exists() || users.length() == 0) {
            userList = new ArrayList<>();
            return;
        }
        userList = OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {});
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }

    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    private boolean isLoggedIn() {
        if (user == null) {
            System.out.println("Please login first");
            return false;
        }
        return true;
    }

    public Boolean loginUser() {
        if (user == null) {
            return Boolean.FALSE;
        }

        Optional<User> foundUser = userList.stream()
                .filter(u -> u.getName().equals(user.getName())
                        && UserServiceUtils.checkPassword(user.getPassword(), u.getHashedpassword()))
                .findFirst();

        if (foundUser.isPresent()) {
            this.user = foundUser.get();
            return Boolean.TRUE;
        }

        this.user = null;
        return Boolean.FALSE;
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USER_DB_PATH);
        OBJECT_MAPPER.writeValue(usersFile, userList);
    }

    public Boolean signUp(User user1) {
        boolean nameTaken = userList.stream()
                .anyMatch(u -> u.getName().equals(user1.getName()));
        if (nameTaken) {
            System.out.println("Username already exists, choose another one");
            return Boolean.FALSE;
        }

        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }

    public void fetchBooking() {
        if (!isLoggedIn()) {
            return;
        }
        this.user.getBooking();
    }

    public Boolean cancelBooking(String ticketId) {
        if (!isLoggedIn()) {
            return Boolean.FALSE;
        }
        if (ticketId == null || ticketId.isEmpty()) {
            return Boolean.FALSE;
        }
        if (user.getTicketsBooked() == null) {
            return Boolean.FALSE;
        }

        boolean removed = user.getTicketsBooked().removeIf(t -> ticketId.equals(t.getTicketId()));
        if (!removed) {
            System.out.println("No ticket found with id " + ticketId);
            return Boolean.FALSE;
        }

        try {
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }
}
