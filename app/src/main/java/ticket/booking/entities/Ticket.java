package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private Date dateOfTravel;
    private String trainId;
    private int seatRow;
    private int seatColumn;

    public Ticket(){
    }

    public Ticket(String ticketId, String userId, String source, String destination, Date dateOfTravel,
                  String trainId, int seatRow, int seatColumn){
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.trainId = trainId;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
    }

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

    public String getTrainId(){
        return this.trainId;
    }

    public int getSeatRow(){
        return this.seatRow;
    }

    public int getSeatColumn(){
        return this.seatColumn;
    }

    @JsonIgnore
    public String getTicketInfo(){
        return "Ticket id: " + this.ticketId + " | " + this.source + " -> " + this.destination
                + " | " + this.dateOfTravel + " | seat: " + this.seatRow + " " + this.seatColumn;
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

    public void setTrainId(String trainId){
        this.trainId = trainId;
    }

    public void setSeatRow(int seatRow){
        this.seatRow = seatRow;
    }

    public void setSeatColumn(int seatColumn){
        this.seatColumn = seatColumn;
    }


}
