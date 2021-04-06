package org.example;

import org.assertj.core.internal.bytebuddy.dynamic.scaffold.MethodRegistry;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DeliveryControllerTest {
    private DeliveryController deliveryController = new DeliveryController();

    @Test
    public void testConnection() {
        Connection result = deliveryController.getConnection();
        assertEquals(result != null, true);
    }

    @Test

    public void testAddToOrder() {
        try{
            //Clean the table before testing
            PreparedStatement deleteStatement =  deliveryController.getConnection().prepareStatement("DELETE FROM `customerorder`");
            deleteStatement.executeUpdate();


            PreparedStatement statement =  deliveryController.getConnection().prepareStatement("INSERT INTO `customerorder`(productImage, productName, productPrice) VALUES('test.jpg', 'Test', 0.99) ");
            statement.executeUpdate();

            PreparedStatement getData = deliveryController.getConnection().prepareStatement("SELECT * FROM `customerorder`");
            ResultSet resultSet = getData.executeQuery();
            while(resultSet.next()){
                assertEquals("test.jpg", resultSet.getString(1));
                assertEquals("Test", resultSet.getString(2));
                assertEquals(0.99, resultSet.getDouble(3), 0);
            }

        }catch (Exception e){e.printStackTrace();}
    }

    @Test
    public void testRemoveProduct() throws SQLException {
        String selectedProduct = "Test";
        try {
           PreparedStatement statement = deliveryController.getConnection().prepareStatement("DELETE FROM `customerorder` WHERE productName = '" + selectedProduct + "'");
            statement.executeUpdate();

            PreparedStatement checkIfDataExists = deliveryController.getConnection().prepareStatement("SELECT * FROM `customerorder`");
            ResultSet resultSet = checkIfDataExists.executeQuery();
            while (resultSet.next()) {
                //Checking whether the database contains empty data.
                //If it doesnt, deletion hasn't been successful and should give an error.
                assertEquals("", resultSet.getString(2));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Recommender functionality
    @Test
    public void testFindMatchingData() throws SQLException {
        PreparedStatement statement = deliveryController.getConnection().prepareStatement("SELECT * FROM `customerorder`");
        ResultSet statementRs = statement.executeQuery();
        List<String> statementList = new ArrayList<>();
        while(statementRs.next()){
            statementList.add(statementRs.getString(2));
        }

        PreparedStatement statementKebab = deliveryController.getConnection().prepareStatement("SELECT * FROM `kebab`");
        ResultSet statementKebabRs = statementKebab.executeQuery();
        List<String> statementKebabList = new ArrayList<>();

        while(statementKebabRs.next()){
            statementKebabList.add(statementKebabRs.getString(2));
        }

        //Checking whether index 0 of kebab which is Lamb Doner matches with index 0 of Basket.
        //If they're not matching then fail the test, indicating product in basket table cannot be found in kebab section
        assertEquals(statementKebabList.get(0), statementList.get(0));

    }


    @Test
    public void testCompleteOrderFunctionality() {
        try {
            PreparedStatement statement = deliveryController.getConnection().prepareStatement("INSERT INTO `delivery`(productName, productPrice) SELECT productName, productPrice FROM `customerorder` ");
            statement.executeUpdate();

            PreparedStatement deleteFromCurrentOrder = deliveryController.getConnection().prepareStatement("DELETE FROM `customerorder`");
            deleteFromCurrentOrder.executeUpdate();

            PreparedStatement getCustomerOrderData =  deliveryController.getConnection().prepareStatement("SELECT * FROM `customerorder`");
            ResultSet getRs =  getCustomerOrderData.executeQuery();
            while(getRs.next()){
                assertEquals("", getRs.getString(2));
            }

            PreparedStatement checkIfDataExists = deliveryController.getConnection().prepareStatement("SELECT * FROM `delivery`");
            ResultSet resultSet = checkIfDataExists.executeQuery();
            while (resultSet.next()) {
                //Checking whether the database contains empty data.
                //If it doesnt, deletion hasn't been successful and should give an error.
                assertEquals("Test", resultSet.getString(1));

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testSumOfBasketTable() throws SQLException {
        PreparedStatement sumOfProducts = deliveryController.getConnection().prepareStatement("SELECT SUM(productPrice) as sum_total FROM `customerorder`");
        ResultSet rsSum = sumOfProducts.executeQuery();
        while(rsSum.next()){
            Double sum = Double.parseDouble(rsSum.getString("sum_total"));
            assertEquals(0.99, sum, 0);
        }
    }
}