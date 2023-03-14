package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();//convert the date of entrance in millisecond
        long outHour = ticket.getOutTime().getTime();//convert the date of exit in millisecond

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = (outHour - inHour)/60000;//make the difference between the date of entrance and exit in minutes
        double durationInDecimal = duration/60;//convert duration in hour's decimal

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationInDecimal * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationInDecimal * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}