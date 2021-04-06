package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class MainLoginControllerTest {

    private MainLoginController mainLoginController = new MainLoginController();


    @Test
    public void testConnection() {
        Connection result = mainLoginController.getConnection();
        assertEquals(result != null, true);
    }

    @Test
    public void testGetLoginData() throws SQLException {
        String userName = "user";
        String password  = "password";
        try {
            PreparedStatement statement = mainLoginController.getConnection().prepareStatement("SELECT * FROM `userlogindata`");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                String exptUsername = resultSet.getString(1);
                String exptPassword =  resultSet.getString(2);
                assertEquals(exptUsername, userName);
                assertEquals(exptPassword, password);
            }
        }catch (Exception e){e.printStackTrace();}
    }



}