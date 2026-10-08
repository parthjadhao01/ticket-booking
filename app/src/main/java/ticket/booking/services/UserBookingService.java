package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private static final String USER_DB_PATH = "app/src/main/java/ticket/booking/localDB/users.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public UserBookingService(User user) throws IOException {
        this.user = user;
        File users = new File(USER_DB_PATH);
        userList = OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public UserBookingService() throws IOException {
        File users = new File(USER_DB_PATH);
        userList = OBJECT_MAPPER.readValue(users,new TypeReference<List<User>>(){} );
    }


    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtils.checkPassword(user.getPassword(),user1.getHashedpassword());
        }).findFirst();

        return foundUser.isPresent();
    }

    private void saveUserListToFile() throws IOException{
        File usersFIle = new File(USER_DB_PATH);
        OBJECT_MAPPER.writeValue(usersFIle,userList);
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    public void fetchBooking(){
        this.user.getBooking();
    }

    public Boolean cancelBooking(String ticketId){
        if (ticketId == null || ticketId.isEmpty()){
            return Boolean.FALSE;
        }

        Optional<User> dbUser = userList.stream().filter(u -> u.getName().equals(user.getName())).findFirst();

        if (!dbUser.isPresent() || dbUser.get().getTicketsBooked() == null){
            return Boolean.FALSE;
        }

        boolean removed = dbUser.get().getTicketsBooked().removeIf(t->ticketId.equals(t.getTicketId()));

        if (!removed){
            return Boolean.FALSE;
        }

        try{
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }

    }
}
