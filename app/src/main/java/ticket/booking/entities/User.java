package ticket.booking.entities;


import java.util.List;

public class User{
    private String usernmae;
    private String hashedpassword;
    private String password;
    private List<Ticket> ticketsBooked;
    private String userId;

    public User(String username,String password ,String hashedpassword,List<Ticket> ticketsBooked,String userId){
        this.usernmae = username;
        this.password = password;
        this.hashedpassword = hashedpassword;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
    }

    public List<Ticket> getTicketsBooked(){
        return ticketsBooked;
    }

    public String getName(){
        return this.usernmae;
    }

    public String getHashedpassword(){
        return this.hashedpassword;
    }

    public String getPassword(){
        return this.password;
    }

    public void getBooking(){
        for(int i = 0; i < ticketsBooked.size(); i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public String getUserId(){
        return this.userId;
    }

    public void setName(String username){
        this.usernmae = username;
    }

    public void setHashedpassword(String hashedpassword){
        this.hashedpassword = hashedpassword;
    }

    public void setPassword(String password){
         this.password = password;
    }

//    implement setter of ticket List
    public void setBooking(){
        for(int i = 0; i < ticketsBooked.size(); i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }


}