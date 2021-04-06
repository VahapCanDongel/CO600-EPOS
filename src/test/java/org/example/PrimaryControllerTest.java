package org.example;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class PrimaryControllerTest {
    private PrimaryController primaryController = new PrimaryController();

    @Test
    public void testCUSTOMERDETAILSConnection() {
        Connection result = primaryController.getConnection();
        assertEquals(result != null, true);
    }


    @Test
    public void testRECOMMENDERConnection() {
        Connection result = primaryController.getRecommenderDatabaseConnection();
        assertEquals(result != null, true);
    }


    @Test
    public void testCustomerDetailsSaveDelivery() throws SQLException {
        String firstName = "TestFirst";
        String lastName = "TestLast";
        String addressLine = "TestAddressLine";
        String postCode = "TesPostCode";
        String phoneNumber = "TestPhoneNumber";

        PreparedStatement saveCustomerDetailsQuery = primaryController.getConnection().prepareStatement("INSERT INTO `customerdetails`(firstName, lastName, addressLine, postCode, phoneNumber) VALUES('"+firstName+"','"+lastName+"', '"+addressLine+"', '"+postCode+"', '"+phoneNumber+"') ");
        saveCustomerDetailsQuery.executeUpdate();

        PreparedStatement getInsertedData = primaryController.getConnection().prepareStatement("SELECT * FROM `customerdetails`");
        ResultSet resultSet = getInsertedData.executeQuery();

        while(resultSet.next()){
            assertEquals(firstName, resultSet.getString("firstName"));
            assertEquals(lastName, resultSet.getString("lastName"));
            assertEquals(addressLine, resultSet.getString("addressLine"));
            assertEquals(postCode, resultSet.getString("postCode"));
            assertEquals(phoneNumber, resultSet.getString("phoneNumber"));
        }
    }

    @Test
    public void testMostlyOrderedSection() throws SQLException {
        String number = "07393731736";
        PreparedStatement preparedStatement = primaryController.getRecommenderDatabaseConnection().prepareStatement("SELECT `sectionOrdered`,\n" +
                "             COUNT(`sectionOrdered`) AS `value_occurrence` \n" +
                "    FROM     `recommenderData`\n" +
                "    WHERE     customerNumber = '"+number+"'\n" +
                "    GROUP BY `sectionOrdered`\n" +
                "    ORDER BY `value_occurrence` DESC\n" +
                "    LIMIT    1;");

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            assertEquals("kebab", resultSet.getString("sectionOrdered"));
        }
    }


}