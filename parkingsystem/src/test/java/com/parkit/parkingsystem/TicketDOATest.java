package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketDOATest {

    private static TicketDAO ticketDAO;

    //create a list of ticket with 10 tickets
    private List<Ticket> getLoyalCustomerList(){

        List<Ticket> ListsOfTicketsByVehiclRegisterWithMoreThanTenDates = new ArrayList<>();

        for(int i=0; i<10; i++) {
            Ticket ticket = new Ticket();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -i);

            Date inDate = calendar.getTime();
            inDate.setTime(System.currentTimeMillis() - (20 * 60 * 1000));
            Date outDate = new Date();

            ticket.setInTime(inDate);
            ticket.setOutTime(outDate);
            ListsOfTicketsByVehiclRegisterWithMoreThanTenDates.add(ticket);
        }
        return ListsOfTicketsByVehiclRegisterWithMoreThanTenDates;
    }

    //create a list of ticket with less than ten tickets
    private List<Ticket> getNonLoyalCustomerList(){

        List<Ticket> ListsOfTicketsByVehiclRegisterWithLessThanTenDates = new ArrayList<>();

        for(int i=0; i<8; i++) {
            Ticket ticket = new Ticket();

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -i);

            Date inDate = calendar.getTime();
            inDate.setTime(System.currentTimeMillis() - (20 * 60 * 1000));
            Date outDate = new Date();

            ticket.setInTime(inDate);
            ticket.setOutTime(outDate);
            ListsOfTicketsByVehiclRegisterWithLessThanTenDates.add(ticket);
        }
        return ListsOfTicketsByVehiclRegisterWithLessThanTenDates;
    }



    @Test
    public void verifyIfCustomerIsLoyalWithTenTicketsByMonth(){
    //GIVEN
        ticketDAO = new TicketDAO();
        List<Ticket> ListsOfTicketsByVehiclRegisterWithMoreThanTenDates = getLoyalCustomerList();

    //WHEN
       boolean actualResult =  ticketDAO.controlIfCustomerIsLoyal(ListsOfTicketsByVehiclRegisterWithMoreThanTenDates);

        //THEN
        assertEquals(actualResult, true);
    }

    @Test
    public void verifyIfCustomerIsNonLoyalWithLessThanTenTicketsByMonth(){
        //GIVEN
        ticketDAO = new TicketDAO();
       List<Ticket> ListsOfTicketsByVehiclRegisterWithLessThanTenDates = getNonLoyalCustomerList();

        //WHEN
        boolean actualResult = ticketDAO.controlIfCustomerIsLoyal(ListsOfTicketsByVehiclRegisterWithLessThanTenDates);

        //THEN
        assertEquals(actualResult, false);
    }

}
