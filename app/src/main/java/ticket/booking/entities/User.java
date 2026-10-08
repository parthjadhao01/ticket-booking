package ticket.booking.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class User{
    private String username;
    private String hashedpassword;

    @JsonIgnore
    private String password;

    private List<Ticket> ticketsBooked;
    private String userId;

    // Jackson needs a constructor with no arguments: when reading users.json it first creates
    // an empty User, then fills in each field using the setters/fields.
    // Without this, loading any user from the file fails.
    public User(){
    }

    public User(String username,String password ,String hashedpassword,List<Ticket> ticketsBooked,String userId){
        this.username = username;
        this.password = password;
        this.hashedpassword = hashedpassword;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
    }

    public List<Ticket> getTicketsBooked(){
        return ticketsBooked;
    }

    public String getName(){
        return this.username;
    }

    public String getHashedpassword(){
        return this.hashedpassword;
    }

    @JsonIgnore
    public String getPassword(){
        return this.password;
    }

    public void getBooking(){
        if (ticketsBooked == null || ticketsBooked.isEmpty()) {
            System.out.println("No bookings found");
            return;
        }
        for(int i = 0; i < ticketsBooked.size(); i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public String getUserId(){
        return this.userId;
    }

    public void setName(String username){
        this.username = username;
    }

    public void setHashedpassword(String hashedpassword){
        this.hashedpassword = hashedpassword;
    }

    @JsonIgnore
    public void setPassword(String password){
         this.password = password;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked){
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }


}
