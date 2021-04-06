package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {

    public TableView<Product> productTable;
    public TableColumn<Product, ImageView> idColProduct;
    public TableColumn<Product, String> nameColProduct;
    public TableColumn<Product, Double> priceColProduct;

    //New Product Insertion Section
    public TextField newProductImageTextField;
    public TextField newProductNameTextField;
    public TextField newProductPriceTextField;
    public TextField newProductSectionTextField;
    public Button saveNewProductButton;


    //Edit Product Section
    public TextField editProductPriceTextField;
    public Button saveEditProductButton;


    String visibleSection;


    /*
    USER LOGIN DETAILS SETTINGS
     */
    public TableView<UserDetails> userTable;
    public TableColumn<UserDetails, String> userUsernameCol;
    public TableColumn<UserDetails, String> userPasswordCol;

    public TextField userUsernameTextField;
    public TextField userPasswordTextField;

    public TableView<UserDetails> adminTable;
    public TableColumn<UserDetails, String> adminUsernameCol;
    public TableColumn<UserDetails, String> adminPasswordCol;

    public TextField adminUsernameTextField;
    public TextField adminPasswordTextField;
     /*
    USER LOGIN DETAILS SETTINGS
     */

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


    public void kebabButtonClick(MouseEvent mouseEvent) throws SQLException {
        visibleSection = "kebab";
        System.out.println(visibleSection);
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

    public void chickenButtonClick(MouseEvent mouseEvent) {
        visibleSection = "chicken";
        System.out.println(visibleSection);

    }

    public void burgerButtonClick(MouseEvent mouseEvent) {
    }

    public void wrapButtonClick(MouseEvent mouseEvent) {
    }

    public void sideButtonClick(MouseEvent mouseEvent) {
    }

    public void vegeterianButtonClick(MouseEvent mouseEvent) {
    }

    public void kidsButtonClick(MouseEvent mouseEvent) {
    }

    public void offerButtonClick(MouseEvent mouseEvent) {
    }


    public void loadImageButtonClick(MouseEvent mouseEvent) throws IOException {
        if(newProductImageTextField.getText().isEmpty()){
            System.out.println("Please name your file first");
        }
        else if(!newProductImageTextField.getText().isEmpty()){
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            if(selectedFile != null){
                //selectedFile.renameTo(new File("C:\\Users\\Vahap\\Desktop\\Epos\\src\\main\\resources\\org\\example\\'"+newProductImageTextField.getText()+'"'));
                selectedFile.renameTo(new File("C:\\Users\\Vahap\\Desktop\\Epos\\src\\main\\resources\\org\\example\\"+newProductImageTextField.getText()));
            }
        }
    }

    public void saveNewProductButtonClick(MouseEvent mouseEvent) throws SQLException, IOException {
        double newProductPrice = Double.parseDouble(newProductPriceTextField.getText());
        PreparedStatement insertNewProduct = getConnection().prepareStatement("INSERT INTO `"+newProductSectionTextField.getText()+"`(productImage, productName, productPrice) VALUES('"+newProductImageTextField.getText()+"', '"+newProductNameTextField.getText()+"', '"+newProductPrice+"')      ");
        insertNewProduct.executeUpdate();
        insertNewProduct.close();

    }


    public void editProductButtonClick(MouseEvent mouseEvent) throws SQLException {
        Product product = productTable.getSelectionModel().getSelectedItem();
        double priceValue = product.getProductPrice();
        editProductPriceTextField.setText(Double.toString(priceValue));
        System.out.println(visibleSection);

    }

    public void deleteProducButton(MouseEvent mouseEvent) throws SQLException {
        Product product = productTable.getSelectionModel().getSelectedItem();
        PreparedStatement getSectionToBeRemoved = getConnection().prepareStatement("DELETE FROM '"+visibleSection+"' WHERE productName = '"+product.getPoroductName()+"'");
        getSectionToBeRemoved.executeUpdate();
        getSectionToBeRemoved.close();
        updateSectionTable();

    }


    public void saveEditedProductPriceButtonClick(MouseEvent mouseEvent) throws SQLException {
        Product product = productTable.getSelectionModel().getSelectedItem();
        double textFieldData = Double.parseDouble(editProductPriceTextField.getText());
        System.out.println(visibleSection);
        PreparedStatement updateProductPrice = getConnection().prepareStatement("UPDATE `"+visibleSection+"`  SET productPrice = ? WHERE productName = ?");
        updateProductPrice.setDouble(1,textFieldData);
        updateProductPrice.setString(2, product.getPoroductName());
        updateProductPrice.executeUpdate();
        updateProductPrice.close();
        updateSectionTable();
    }

    public void homeButtonClicked(MouseEvent mouseEvent) throws SQLException, IOException {
        App.setRoot("primary");
    }

    public void loginDetailsButtonClick(MouseEvent mouseEvent) throws IOException, SQLException {
        App.setRoot("settingschangedetailslogin");
    }

    public void updateSectionTable() throws SQLException {
        final ObservableList<Product> data = FXCollections.observableArrayList();

        ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT * FROM '"+visibleSection+"'");
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








    /*
    * ------------------   USERNAME AND PASSWORD SECTION   ----------------------- *
     */


    public void editUserDetailsButtonClick(MouseEvent mouseEvent) throws SQLException {
        UserDetails userDetails = userTable.getSelectionModel().getSelectedItem();
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();

        userUsernameTextField.setText(username);
        userPasswordTextField.setText(password);
    }

    public void saveUserDetailsEdited(MouseEvent mouseEvent) throws SQLException {
        UserDetails userDetails = userTable.getSelectionModel().getSelectedItem();
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.getPassword());
        PreparedStatement updateUserDetails = getConnection().prepareStatement("UPDATE `userlogindata` SET username = ? , password =  ?");
        updateUserDetails.setString(1, userUsernameTextField.getText());
        updateUserDetails.setString(2, userPasswordTextField.getText());
        updateUserDetails.executeUpdate();
        updateUserDetails.close();

        updateTable();

    }

    public void editAdminDetailsButtonClick(MouseEvent mouseEvent) {
        UserDetails userDetails = adminTable.getSelectionModel().getSelectedItem();
        String username = userDetails.getUsername();
        String password = userDetails.getPassword();

        adminUsernameTextField.setText(username);
        adminPasswordTextField.setText(password);
    }

    public void saveAdminDetailsEdited(MouseEvent mouseEvent) throws SQLException {
        UserDetails userDetails = adminTable.getSelectionModel().getSelectedItem();
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.getPassword());
        PreparedStatement updateUserDetails = getConnection().prepareStatement("UPDATE `adminlogindata` SET username = ? , password =  ?");
        updateUserDetails.setString(1, adminUsernameTextField.getText());
        updateUserDetails.setString(2, adminPasswordTextField.getText());
        updateUserDetails.executeUpdate();
        updateUserDetails.close();

        updateTableAdmin();
    }


    public void loadUserDetailsData(MouseEvent mouseEvent) throws SQLException {
        updateTable();
        updateTableAdmin();
    }



    //Update table when edit has been made
    public void updateTable() throws SQLException {
        final ObservableList<UserDetails> data = FXCollections.observableArrayList();

        ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT * FROM `userlogindata`");
        while(resultSet.next()){

            data.add(new UserDetails(resultSet.getString(1),resultSet.getString(2)));
        }
        userUsernameCol.setCellValueFactory(new PropertyValueFactory<UserDetails, String>("username"));
        userPasswordCol.setCellValueFactory(new PropertyValueFactory<UserDetails, String>("password"));

        userTable.setItems(null);
        userTable.setItems(data);
    }


    public void updateTableAdmin() throws SQLException {
        final ObservableList<UserDetails> data = FXCollections.observableArrayList();

        ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT * FROM `adminlogindata`");
        while(resultSet.next()){

            data.add(new UserDetails(resultSet.getString(1),resultSet.getString(2)));
        }
        adminUsernameCol.setCellValueFactory(new PropertyValueFactory<UserDetails, String>("username"));
        adminPasswordCol.setCellValueFactory(new PropertyValueFactory<UserDetails, String>("password"));

        adminTable.setItems(null);
        adminTable.setItems(data);
    }



}
