package ticket.booking.entities;

import java.util.Date;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private Date dateOfTravel;

    public String getTicketId(){
        return this.ticketId;
    }

    public String getUserId(){
        return this.userId;
    }

    public String getSource(){
        return this.source;
    }

    public String getDestination(){
        return this.destination;
    }

    public Date getDateOfTravel(){
        return this.dateOfTravel;
    }

    public String getTicketInfo(){
        return this.ticketId + " " + this.userId + " " + this.source + " " + this.destination + " " + this.dateOfTravel;
    }

    public void setTicketId(String ticketId){
        this.ticketId = ticketId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setSource(String source){
        this.source = source;
    }

    public void setDestination(String destination){
       this.destination = destination;
    }

    public void setDateOfTravel(Date dateOfTravel){
        this.dateOfTravel = dateOfTravel;
    }


}
