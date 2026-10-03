package ticket.booking.entities;


import java.util.List;

public class User{
    private String usernmae;
    private String hashedpassword;
    private String password;
    private List<Ticket> ticketsBooked;
    private String userId;
}