package ticket.booking.entities;


import java.util.List;

public class User{
    private String usernmae;
    private String hashedpassword;
    private String password;
    private List<Ticket> ticketsBooked;
    private String userId;

    User(String username,String password ,String hashedpassword,List<Ticket> ticketsBooked,String userId){
        this.usernmae = username;
        this.password = password;
        this.hashedpassword = hashedpassword;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
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

    public void getListTicket(){
        for(int i = 0; i < ticketsBooked.size(); i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
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
    public void setListTicket(){
        for(int i = 0; i < ticketsBooked.size(); i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }


}