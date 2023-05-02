package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketDataBaseIT {


    private static TicketDAO ticketDAO;


    private static ParkingSpot parkingSpot;

    public static DataBaseConfig dataBaseConfig = new DataBaseConfig();

    @BeforeAll
    private static void createAndRegisterTicketInTheDB() throws Exception {

        ticketDAO = new TicketDAO();
        int dayToSubstract = 5;

        for (int i = 2; i < 6; i++) {
            Ticket ticket = new Ticket();
            parkingSpot = new ParkingSpot(i, ParkingType.CAR, true);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -dayToSubstract);

            Date inDate = calendar.getTime();
            calendar.add(Calendar.HOUR, 1);
            Date outDate = calendar.getTime();


            ticket.setId(i);
            ticket.setParkingSpot(parkingSpot);
            ticket.setInTime(inDate);
            ticket.setOutTime(outDate);
            ticket.setVehicleRegNumber("TTTTT");
            ticket.setPrice(1.5);

            ticketDAO.saveTicket(ticket);

            dayToSubstract = dayToSubstract+10;
        }
    }

    @AfterAll
    private static void clearDB() {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            Statement stmt = con.createStatement();
            System.out.println("Suppression...");
            String sqlFirstTest = "DELETE FROM ticket WHERE VEHICLE_REG_NUMBER = 'TTTTT'";

            stmt.executeUpdate(sqlFirstTest);

            System.out.println("Les tickets avec la plaque TTTTT ont été supprimés avec succès...");
            //étape 5: fermez l'objet de connexion

            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//

    //control if the list of ticket with the same vehicle register number from DB is correctly created during the methode "getTicket()"
    @Test
    public void getListOfTicketsRegisterInDataBase() {
        //GIVEN
        ticketDAO = new TicketDAO();
        //WHEN
        List<Ticket> listOfTicketsWhichHaveLessThan30Days = ticketDAO.getListOfTicketsWhichHaveLessThan30Days("TTTTT");
        int sizeOfTheList = listOfTicketsWhichHaveLessThan30Days.size();
        //THEN
        assertEquals(sizeOfTheList, 3);

    }
}


