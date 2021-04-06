package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Random;


public class WalkinController{

    public TableView<Product> productTable;
    public TableColumn<Product, ImageView> idColProduct;
    public TableColumn<Product, String> nameColProduct;
    public TableColumn<Product, Double> priceColProduct;

    public TableView<Order> orderTable;
    public TableColumn<Order, String> nameColOrder;
    public TableColumn<Order, Double> priceColOrder;

    //Total Label
    public Label totalTextLabel;


    //Database connection
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




    //Navigation and product table populating
    public void kebabButtonClick(MouseEvent mouseEvent) throws SQLException {
        final ObservableList<Product> data = FXCollections.observableArrayList();

        ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT * FROM `kebab`");
        while(resultSet.next()){
            System.out.println(resultSet.getString(1));
            ImageView testImage = new ImageView(new Image(getClass().getResourceAsStream(resultSet.getString(1))));
            data.add(new Product(testImage, resultSet.getString(2),resultSet.getDouble(3)));

        }
        idColProduct.setCellValueFactory(new PropertyValueFactory<Product, ImageView>("productImage"));
        nameColProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("poroductName"));
        priceColProduct.setCellValueFactory(new PropertyValueFactory<Product, Double>("productPrice"));

        productTable.setItems(null);
        productTable.setItems(data);
    }

    public void chickenButtonClick(MouseEvent mouseEvent) throws SQLException {

    }

    public void burgerButtonClick(MouseEvent mouseEvent) throws SQLException {

    }

    public void wrapButtonClick(MouseEvent mouseEvent) throws SQLException {

    }

    public void sideButtonClick(MouseEvent mouseEvent) throws SQLException {

    }

    public void vegeterianButtonClick(MouseEvent mouseEvent) throws SQLException {

    }

    public void kidsButtonClick(MouseEvent mouseEvent) throws SQLException {

    }

    public void offerButtonClick(MouseEvent mouseEvent) throws SQLException {

    }






    //Order table completing, remvoing and duplicating functions
    public void compeleteOrderButtonClick(MouseEvent mouseEvent) throws IOException, SQLException {
        receiptPrintingFunction();

        PreparedStatement listOfOrdersData = getConnection().prepareStatement("INSERT INTO `walkin`(productName, productPrice) SELECT productName, productPrice FROM `customerorder` ");
        PreparedStatement deleteData = getConnection().prepareStatement("DELETE FROM `customerorder`");
        listOfOrdersData.executeUpdate();
        deleteData.executeUpdate();
        listOfOrdersData.close();
        deleteData.close();

        App.setRoot("primary");
    }

    public void removeProductButtonClick(MouseEvent mouseEvent) throws SQLException {
        Order product = orderTable.getSelectionModel().getSelectedItem();
        PreparedStatement resultSet1 = getConnection().prepareStatement("DELETE FROM `customerorder` WHERE productName = '"+product.getPoroductName()+"'");
        resultSet1.executeUpdate();
        orderTable.getItems().removeAll(orderTable.getSelectionModel().getSelectedItem());
        resultSet1.close();


        PreparedStatement sumOfProducts = getConnection().prepareStatement("SELECT SUM(productPrice) as sum_total FROM `customerorder`");
        ResultSet rsSum = sumOfProducts.executeQuery();
        if(rsSum.next()){
            String sumString = rsSum.getString("sum_total");
            if(sumString ==  null){
                totalTextLabel.setText("£"+ "0");
            }else{
                totalTextLabel.setText("£"+sumString);
            }

        }
        sumOfProducts.close();
        rsSum.close();
    }


    public void addOrderButtonClick(MouseEvent mouseEvent) throws SQLException {
        Product product = productTable.getSelectionModel().getSelectedItem();
        PreparedStatement resultSet1 = getConnection().prepareStatement("INSERT INTO `customerorder`(productImage, productName, productPrice) VALUES('"+product.getProductImage()+"','"+product.getPoroductName()+"', '"+product.getProductPrice()+"')");
        resultSet1.executeUpdate();



        final ObservableList<Order> data = FXCollections.observableArrayList();

        ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT * FROM `customerorder`");
        while(resultSet.next()){
            data.add(new Order(resultSet.getString(2), resultSet.getDouble(3)));
        }

        nameColOrder.setCellValueFactory(new PropertyValueFactory<Order, String>("poroductName"));
        priceColOrder.setCellValueFactory(new PropertyValueFactory<Order, Double>("productPrice"));

        orderTable.setItems(null);
        orderTable.setItems(data);


        resultSet1.close();
        resultSet.close();

        PreparedStatement sumOfProducts = getConnection().prepareStatement("SELECT SUM(productPrice) as sum_total FROM `customerorder`");
        ResultSet rsSum = sumOfProducts.executeQuery();
        if(rsSum.next()){
            String sumString = rsSum.getString("sum_total");
            totalTextLabel.setText("£"+sumString);
        }
        sumOfProducts.close();
        rsSum.close();

    }

    //Compleing order and printing order function
    public void receiptPrintingFunction() throws IOException, SQLException {
        Random r = new Random();
        int low = 1881;
        int high = 1938;
        int result = r.nextInt(high-low) + low;
        String receiptNumber = Integer.toString(result);
        FileWriter writer = new FileWriter(new File("C:\\Users\\Vahap\\Desktop\\Epos", "receipt"+receiptNumber+".txt"));


        try{


            //CUSTOMER DETAILS SQL
            Connection customerOrderConnection = getConnection();
            PreparedStatement sumOfProducts = customerOrderConnection.prepareStatement("SELECT SUM(productPrice) as sum_total FROM `customerorder`");
            ResultSet rsSum = sumOfProducts.executeQuery();

            //CUSTOMER ORDER
            Statement st = customerOrderConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT productName, productPrice FROM `customerorder`");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            writer.write("############################ Order Number: " + receiptNumber + " ############################");
            writer.write("\n");
            writer.write("\n");
            writer.write("                            OLLIES KEBAB SUTTON");
            writer.write("\n");
            writer.write("                      SUTTON STATION, HIGH ST, SM1 1DE");
            writer.write("\n");
            writer.write("                              020 8642 5050");
            writer.write("\n");
            writer.write("\n");


            writer.write("\n");
            writer.write("                  ########## ORDER TYPE: WALK-IN ##########");
            writer.write("\n");
            writer.write("+-----------------+--------------------+\n");
            writer.write("|    Item Name    |     Price          |\n");
            writer.write("+-----------------+--------------------+\n");
            writer.write("\n");
            while (rs.next()) {
                for(int i = 1 ; i <= columnsNumber; i++){
                    writer.write(rs.getString(i) + "                  "); //Print one element of a row
                }
                writer.write("\n");//Move to the next line to print the next row.
            }

            if(rsSum.next()){
                String sumString = rsSum.getString("sum_total");
                writer.write("                                 +---------------------+\n");
                writer.write("                                 |     TOTAL: £"+sumString+"    |\n");
                writer.write("                                 +---------------------+\n");
                writer.write("\n");
                writer.write("############################################################################");
            }

            writer.flush();
            writer.close();
            sumOfProducts.close();
            rsSum.close();
            rs.close();

        }catch (Exception e){System.out.println(e);}




    }

    public void homeButtonClicked(MouseEvent mouseEvent) throws SQLException, IOException {
        PreparedStatement deleteData = getConnection().prepareStatement("DELETE FROM `customerorder`");
        deleteData.executeUpdate();
        deleteData.close();
        App.setRoot("primary");


    }
}
