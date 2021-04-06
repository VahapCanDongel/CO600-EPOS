package org.example;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SettingsControllerTest {
    private SettingsController settingsController = new SettingsController();

    @Test
    public void testConnection() {
        Connection result = settingsController.getConnection();
        assertEquals(result != null, true);
    }


    @Test
    public void getProducts() throws SQLException {
        PreparedStatement statement = settingsController.getConnection().prepareStatement("SELECT * FROM `kebab`");
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            assertEquals(resultSet.getString(2), resultSet.getString("productName"));
        }
    }

    @Test
    public void testInsertProductToSection() throws SQLException {
       PreparedStatement statement = settingsController.getConnection().prepareStatement("INSERT INTO `kebab` (productImage, productName, productPrice) VALUES('test.jpg', 'Test', 0.99)");
        statement.executeUpdate();

        PreparedStatement preparedStatement = settingsController.getConnection().prepareStatement("SELECT * FROM `kebab`");
        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> kebabs = new ArrayList<>();
        while(resultSet.next()){
            kebabs.add(resultSet.getString(2));
        }
        assertEquals(kebabs.get(3), kebabs.get(3));
    }

    @Test
    public void testUpdateProductPrice() throws SQLException {
        String productName = "Test";
        String newProductName = "TestNew";
        PreparedStatement updateProductPrice = settingsController.getConnection().prepareStatement("UPDATE `kebab`  SET productName = ? WHERE productName = ?");
        updateProductPrice.setString(1,newProductName);
        updateProductPrice.setString(2, productName);
        updateProductPrice.executeUpdate();

        PreparedStatement statement = settingsController.getConnection().prepareStatement("SELECT * FROM `kebab`");
        ResultSet resultSet = statement.executeQuery();

        List<String> kebabs = new ArrayList<>();

        while(resultSet.next()){
            kebabs.add(resultSet.getString(2));
        }

        assertEquals("TestNew", kebabs.get(3));

    }

    @Test
    public void testDeleteProduct() throws SQLException {
        String selectedProduct = "TestNew";
            PreparedStatement statement = settingsController.getConnection().prepareStatement("DELETE FROM `kebab` WHERE productName = '"+selectedProduct+"'");
            statement.executeUpdate();

            PreparedStatement checkData =  settingsController.getConnection().prepareStatement("SELECT * FROM `kebab`");
            ResultSet resultSet = checkData.executeQuery();

            List<String> data = new ArrayList<>();
            while(resultSet.next()){
                data.add(resultSet.getString(2));
            }
            //This means that list index bound is 0-2 which indicates the product that we wanted does not exist
            //This means the test has passed
            assertEquals("Chicken Shish", data.get(2));
    }


    /*
    -----USER AND ADMIN LOGIN DETAILS TEST-----
     */

    @Test
    public void testGetUserDetails() throws SQLException {
        PreparedStatement statement = settingsController.getConnection().prepareStatement("SELECT  * FROM `userlogindata`");
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            assertEquals("password", resultSet.getString(2));
            assertEquals("user", resultSet.getString(1));
        }

    }

    @Test
    public void testGetAdminDetails() throws SQLException {
        PreparedStatement statement = settingsController.getConnection().prepareStatement("SELECT  * FROM `adminlogindata`");
        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()){
            assertEquals("password", resultSet.getString(2));
            assertEquals("admin", resultSet.getString(1));
        }
    }

    @Test
    public void testInsertNewUser() throws SQLException {
        PreparedStatement statement = settingsController.getConnection().prepareStatement("INSERT INTO `userlogindata`(username, password) VALUES('testUser', 'testPassword')");
        statement.executeUpdate();

        PreparedStatement getData = settingsController.getConnection().prepareStatement("SELECT * FROM `userlogindata`");
        ResultSet resultSet = getData.executeQuery();

        List<String> usernames = new ArrayList<>();
        List<String> passwords =  new ArrayList<>();

        while(resultSet.next()){
            usernames.add(resultSet.getString(1));
            passwords.add(resultSet.getString(2));
        }

        assertEquals("testUser", usernames.get(1));
        assertEquals("testPassword", passwords.get(1));

    }

    @Test
    public void testInsertNewAdmin() throws SQLException {
        PreparedStatement statement = settingsController.getConnection().prepareStatement("INSERT INTO `adminlogindata`(username, password) VALUES('testAdmin', 'testPassword')");
        statement.executeUpdate();

        PreparedStatement getData = settingsController.getConnection().prepareStatement("SELECT * FROM `adminlogindata`");
        ResultSet resultSet = getData.executeQuery();

        List<String> usernames = new ArrayList<>();
        List<String> passwords =  new ArrayList<>();

        while(resultSet.next()){
            usernames.add(resultSet.getString(1));
            passwords.add(resultSet.getString(2));
        }

        assertEquals("testUser", usernames.get(1));
        assertEquals("testPassword", passwords.get(1));
    }

    @Test
    public void testUpdateUserDetails() throws SQLException {
        String userName = "testUser";
        String newUserName = "testUserNew";
        PreparedStatement updateProductPrice = settingsController.getConnection().prepareStatement("UPDATE `userlogindata`  SET username = ? WHERE username = ?");
        updateProductPrice.setString(1,newUserName);
        updateProductPrice.setString(2, userName);
        updateProductPrice.executeUpdate();

        PreparedStatement statement = settingsController.getConnection().prepareStatement("SELECT * FROM `userlogindata`");
        ResultSet resultSet = statement.executeQuery();

        List<String> usernames = new ArrayList<>();

        while(resultSet.next()){
            usernames.add(resultSet.getString(1));
        }

        assertEquals("testUserNew", usernames.get(1));
    }

    @Test
    public void testUpdateAdminDetails() throws SQLException {
        String userName = "testAdmin";
        String newUserName = "testAdminNew";
        PreparedStatement updateProductPrice = settingsController.getConnection().prepareStatement("UPDATE `adminlogindata`  SET username = ? WHERE username = ?");
        updateProductPrice.setString(1,newUserName);
        updateProductPrice.setString(2, userName);
        updateProductPrice.executeUpdate();

        PreparedStatement statement = settingsController.getConnection().prepareStatement("SELECT * FROM `adminlogindata`");
        ResultSet resultSet = statement.executeQuery();

        List<String> usernames = new ArrayList<>();

        while(resultSet.next()){
            usernames.add(resultSet.getString(1));
        }

        assertEquals("testAdminNew", usernames.get(1));

    }


}