package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListOfOrdersController {


    public TableView<ListOfOrders> deliveryTable;
    public TableColumn<ListOfOrders, String> nameColDelivery;
    public TableColumn<ListOfOrders, Integer> priceColDelivery;


    public TableView<ListOfOrders> takeawayTable;
    public TableColumn<ListOfOrders, String> nameColTakeaway;
    public TableColumn<ListOfOrders, Integer> priceColTakeaway;


    public TableView<ListOfOrders> walkinTable;
    public TableColumn<ListOfOrders, String> nameColWalkin;
    public TableColumn<ListOfOrders, Integer> priceColWalkin;



    //Sum Labels
    public Label totalDelivery;
    public Label totalTakeaway;
    public Label totalWalkin;

    public Connection getConnection(){
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:DATABASE";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }



    public void loadDataForAllTablesButtonClick(MouseEvent mouseEvent) throws SQLException {
        //Delivery table
        final ObservableList<ListOfOrders> deliveryOrders = FXCollections.observableArrayList();

        ResultSet resultSetDelivery = getConnection().createStatement().executeQuery("SELECT productName, count(*) as quantity FROM `delivery` GROUP BY productName");

        PreparedStatement sumOfProductsDelivery =getConnection().prepareStatement("SELECT SUM(productPrice) as sum_total FROM `delivery`");
        ResultSet rsSumDelivery = sumOfProductsDelivery.executeQuery();
        if(rsSumDelivery.next()){
            String sumString = rsSumDelivery.getString("sum_total");
            totalDelivery.setText("£"+sumString);

        }
        sumOfProductsDelivery.close();
        rsSumDelivery.close();
        while(resultSetDelivery.next()){
            System.out.println(resultSetDelivery.getString(1));

            deliveryOrders.add(new ListOfOrders(resultSetDelivery.getString(1), resultSetDelivery.getInt(2)));

        }
        nameColDelivery.setCellValueFactory(new PropertyValueFactory<ListOfOrders, String>("poroductName"));
        priceColDelivery.setCellValueFactory(new PropertyValueFactory<ListOfOrders, Integer>("productPrice"));

        deliveryTable.setItems(null);
        deliveryTable.setItems(deliveryOrders);
        resultSetDelivery.close();


        //Takeaway Table
        final ObservableList<ListOfOrders> takeawayOrders = FXCollections.observableArrayList();

        ResultSet resultSetTakeaway = getConnection().createStatement().executeQuery("SELECT productName, count(*) as quantity FROM `takeaway` GROUP BY productName");

        PreparedStatement sumOfProductsTakeaway = getConnection().prepareStatement("SELECT SUM(productPrice) as sum_total FROM `takeaway`");
        ResultSet rsSumTakeaway = sumOfProductsTakeaway.executeQuery();
        if(rsSumTakeaway.next()){
            String sumString = rsSumTakeaway.getString("sum_total");
            totalTakeaway.setText("£"+sumString);

        }
        sumOfProductsTakeaway.close();
        rsSumTakeaway.close();
        while(resultSetTakeaway.next()){
            System.out.println(resultSetTakeaway.getString(1));

            takeawayOrders.add(new ListOfOrders(resultSetTakeaway.getString(1), resultSetTakeaway.getInt(2)));

        }
        nameColTakeaway.setCellValueFactory(new PropertyValueFactory<ListOfOrders, String>("poroductName"));
        priceColTakeaway.setCellValueFactory(new PropertyValueFactory<ListOfOrders, Integer>("productPrice"));

        takeawayTable.setItems(null);
        takeawayTable.setItems(takeawayOrders);
        resultSetTakeaway.close();

        //Walk-in Table
        final ObservableList<ListOfOrders> walkinOrders = FXCollections.observableArrayList();

        ResultSet resultSetWalkin = getConnection().createStatement().executeQuery("SELECT productName, count(*) as quantity FROM `walkin` GROUP BY productName");

        PreparedStatement sumOfProductsWalkin = getConnection().prepareStatement("SELECT SUM(productPrice) as sum_total FROM `walkin`");
        ResultSet rsSumWalkin = sumOfProductsWalkin.executeQuery();
        if(rsSumWalkin.next()){
            String sumString = rsSumWalkin.getString("sum_total");
            totalWalkin.setText("£"+sumString);

        }
        sumOfProductsWalkin.close();
        rsSumWalkin.close();
        while(resultSetWalkin.next()){
            System.out.println(resultSetWalkin.getString(1));

            walkinOrders.add(new ListOfOrders(resultSetWalkin.getString(1), resultSetWalkin.getInt(2)));

        }
        nameColWalkin.setCellValueFactory(new PropertyValueFactory<ListOfOrders, String>("poroductName"));
        priceColWalkin.setCellValueFactory(new PropertyValueFactory<ListOfOrders, Integer>("productPrice"));

        walkinTable.setItems(null);
        walkinTable.setItems(walkinOrders);
        resultSetWalkin.close();
    }




    public void completeButtonClick(MouseEvent mouseEvent) throws SQLException, IOException {
        java.util.Date dateToday = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        String strDate = formatter.format(dateToday);
        System.out.println(strDate);

        PreparedStatement statementDelivery = getConnection().prepareStatement("INSERT INTO `TAKINGS`(orderType, totalDate, totalSum) VALUES('delivery', '"+strDate+"','"+totalDelivery.getText()+"')");
        statementDelivery.executeUpdate();
        statementDelivery.close();

        PreparedStatement statementTakeaway = getConnection().prepareStatement("INSERT INTO `TAKINGS`(orderType, totalDate, totalSum) VALUES('takeaway', '"+strDate+"','"+totalTakeaway.getText()+"')");
        statementTakeaway.executeUpdate();
        statementTakeaway.close();

        PreparedStatement statementWalkin = getConnection().prepareStatement("INSERT INTO `TAKINGS`(orderType, totalDate, totalSum) VALUES('walk-in', '"+strDate+"','"+totalWalkin.getText()+"')");
        statementWalkin.executeUpdate();
        statementWalkin.close();


        PreparedStatement deleteStatementDelivery = getConnection().prepareStatement("DELETE FROM `delivery`");
        deleteStatementDelivery.executeUpdate();
        deleteStatementDelivery.close();

        PreparedStatement deleteStatementTakeaway = getConnection().prepareStatement("DELETE FROM `takeaway`");
        deleteStatementTakeaway.executeUpdate();
        deleteStatementTakeaway.close();

        PreparedStatement deleteStatementWalkin= getConnection().prepareStatement("DELETE FROM `walkin`");
        deleteStatementWalkin.executeUpdate();
        deleteStatementWalkin.close();

        App.setRoot("primary");
    }

    public void homeButtonClicked(MouseEvent mouseEvent) throws SQLException, IOException {
        PreparedStatement deleteData = getConnection().prepareStatement("DELETE FROM `customerorder`");
        deleteData.executeUpdate();
        deleteData.close();
        App.setRoot("primary");
    }
}
