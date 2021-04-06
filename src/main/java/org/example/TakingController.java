package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TakingController {


    public TableView<Taking> takingsTable;
    public TableColumn<Taking, String> orderTypeCol;
    public TableColumn<Taking, java.sql.Date> dateCol;
    public TableColumn<Taking, Double> totalCol;

    public DatePicker datePicker;
    public Label totalOfTheDay;


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

    public void goToSelectedDateButtonClick(MouseEvent mouseEvent) throws SQLException, ParseException {

        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        final ObservableList<Taking> allTakings = FXCollections.observableArrayList();

        ResultSet resultSetTaking = getConnection().createStatement().executeQuery("SELECT * FROM `TAKINGS` WHERE totalDate='"+localDate.toString()+"'");
        while(resultSetTaking.next()){
            allTakings.add(new Taking(resultSetTaking.getString(1),resultSetTaking.getString(2), resultSetTaking.getDouble(3)));
        }
        orderTypeCol.setCellValueFactory(new PropertyValueFactory<Taking, String>("orderType"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Taking, java.sql.Date>("totalDate"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Taking, Double>("totalSum"));

        takingsTable.setItems(null);
        takingsTable.setItems(allTakings);

        totalSelectedDateProductsSum();

    }

    public void goTodayButtonClick(MouseEvent mouseEvent) throws SQLException {
        java.util.Date dateToday = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        String strDate = formatter.format(dateToday);
        System.out.println(strDate);

        final ObservableList<Taking> allTakings = FXCollections.observableArrayList();

        ResultSet resultSetTaking = getConnection().createStatement().executeQuery("SELECT * FROM `TAKINGS` WHERE totalDate='"+strDate+"'");
        while(resultSetTaking.next()){
            allTakings.add(new Taking(resultSetTaking.getString(1),resultSetTaking.getString(2), resultSetTaking.getDouble(3)));
        }
        orderTypeCol.setCellValueFactory(new PropertyValueFactory<Taking, String>("orderType"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Taking, java.sql.Date>("totalDate"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Taking, Double>("totalSum"));

        takingsTable.setItems(null);
        takingsTable.setItems(allTakings);

        totalSumProducts();
    }

    public void homeButtonClicked(MouseEvent mouseEvent) throws SQLException, IOException {
        App.setRoot("primary");
    }




    public void totalSumProducts() throws SQLException {
        java.util.Date dateToday = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
        String strDate = formatter.format(dateToday);

        PreparedStatement sumOfProducts = getConnection().prepareStatement("SELECT SUM(totalSum) as sum_total FROM `TAKINGS` WHERE totalDate = '"+strDate+"'");
        ResultSet rsSum = sumOfProducts.executeQuery();
        if(rsSum.next()){
            String sumString = rsSum.getString("sum_total");
            totalOfTheDay.setText("£"+sumString);

        }
        sumOfProducts.close();
        rsSum.close();
    }

    public void totalSelectedDateProductsSum() throws SQLException {
        LocalDate localDate = datePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        PreparedStatement sumOfProducts = getConnection().prepareStatement("SELECT SUM(totalSum) as sum_total FROM `TAKINGS` WHERE totalDate = '"+localDate.toString()+"'");
        ResultSet rsSum = sumOfProducts.executeQuery();
        if(rsSum.next()){
            String sumString = rsSum.getString("sum_total");
            totalOfTheDay.setText("£"+sumString);

        }
        sumOfProducts.close();
        rsSum.close();

    }

}
