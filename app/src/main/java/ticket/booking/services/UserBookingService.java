package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private static final String USER_DB_PATH = "app/src/main/java/ticket/booking/localDB/users.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    UserBookingService(User user) throws IOException {
        this.user = user;
        File users = new File(USER_DB_PATH);
        userList = OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equal(user.getName()) && UserServiceUtils.checkPassword(user.getPassword())
        }).findFirst();

        return foundUser;
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

}
