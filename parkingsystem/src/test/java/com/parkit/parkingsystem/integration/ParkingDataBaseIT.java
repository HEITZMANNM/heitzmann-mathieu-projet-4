package com.parkit.parkingsystem.integration;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;


    @Mock
    private static InputReaderUtil inputReaderUtil;


    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }



    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

@Test
    public void testParkingACar(){

        //GIVEN...
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //WHEN

        parkingService.processIncomingVehicle();


        final Ticket expectedSavedTicket = new Ticket();
        expectedSavedTicket.setVehicleRegNumber("ABCDEF");//create a ticket with the same car reg number than which expected register in DB

        boolean availabilityOfParkingSpotOfIncomingCar = ticketDAO.getTicket("ABCDEF").getParkingSpot().isAvailable();

        //THEN
        assertEquals(expectedSavedTicket.getVehicleRegNumber(), ticketDAO.getTicket("ABCDEF").getVehicleRegNumber());
        assertFalse(availabilityOfParkingSpotOfIncomingCar);

    }

    @Test
    public void testParkingLotExit(){

        //GIVEN...

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Ticket ticket = ticketDAO.getTicket("ABCDEF");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ticket.getInTime());
        calendar.add(Calendar.HOUR, -1);

        ticket.setOutTime(new Date());

        ticket.setInTime(calendar.getTime());

        ticketDAO.updateTicket(ticket);



        //WHEN

        parkingService.processExitingVehicle();


        double fareExpected = ticketDAO.getTicket("ABCDEF").getPrice();
        Date outDateExpected = ticketDAO.getTicket("ABCDEF").getOutTime();

        //THEN
        assertNotNull(outDateExpected);
        assertNotEquals(fareExpected, 0);//check if the price of ticket was claculated so that the out Date was registered.




    }

}
