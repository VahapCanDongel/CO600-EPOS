package org.example;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class TakingControllerTest {
    private TakingController takingController = new TakingController();

    @Test
    public void testConnection() {
        Connection result = takingController.getConnection();
        assertEquals(result != null, true);
    }

    @Test
    public void testGoToSelectedDate() throws SQLException {
        String date = "2021-03-03";
        ResultSet resultSetTaking = takingController.getConnection().createStatement().executeQuery("SELECT * FROM `TAKINGS` WHERE totalDate='"+date+"'");
        while(resultSetTaking.next()){
            assertEquals(238.76, resultSetTaking.getDouble(3), 0);
        }
    }

    @Test
    public void testGoToTodayDate() throws SQLException {
        java.util.Date dateToday = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        String strDate = formatter.format(dateToday);

        ResultSet resultSetTaking = takingController.getConnection().createStatement().executeQuery("SELECT * FROM `TAKINGS` WHERE totalDate='"+strDate+"'");
        while(resultSetTaking.next()){
            assertEquals("", resultSetTaking.getString("orderType"));
        }
    }
}