package org.example;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import com.google.maps.GeoApiContext;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;



public class PrimaryController {
    DeliveryController deliveryController = new DeliveryController();
    TakeawayController takeawayController = new TakeawayController();


    public AnchorPane mainMenuAnchorPane;
    public AnchorPane callerIDAcnhorPane;

    //Google API Resources
    public WebView webView;
    private final String API_KEY = "AIzaSyCTSr_8aA1iYSu28dUZut1ya7gp2Xf6OfY";
    private final GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
    private OkHttpClient client = new OkHttpClient();
    private String source = "CT11XQ";
    public Label showMilageLabel;
    public Label postCodeValidationLabel;


    //Delivery Details Menu
    public AnchorPane deliveryDetailsPane;
    public TextField firstNameTextFieldDelivery;
    public TextField lastNameTextFieldDelivery;
    public TextField addressLineTextFieldDelivery;
    public TextField postCodeTextFieldDelivery;
    public TextField numberTextFieldDelivery;
    public Button saveCustomerDeliveryButton;

    //Takeaway Details Menu
    public AnchorPane takeawayDetailsPane;
    public TextField firstNameTextFieldTakeaway;
    public TextField lastNameTextFieldTakeaway;
    public TextField addressLineTextFieldTakeaway;
    public TextField postCodeTextFieldTakeaway;
    public TextField numberTextFieldTakeaway;

    //Caller ID
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label addressLineLabel;
    public Label postCoodeLabel;
    public Label numberLabel;


    //Recommender
    public Label recommendLabelOne;
    public Label recommendLabelMain;
    public Label recommendLabelTwo;



    //In coming caller ID check
    public void mainMenuUpdate(MouseEvent mouseEvent) {

            Connection connection = getConnection();
            try(Scanner scnr = new Scanner( new File("C:\\Users\\Vahap\\Desktop\\Epos\\IncomingCall.txt") ); ){
                String save = "";
                while( scnr.hasNextLine() )
                {
                    String line = scnr.nextLine();
                    if( line.startsWith( "NMBR=" ) )
                    {
                        save = line.substring(5);
                    }
                }
                PreparedStatement findCustomerDetails = connection.prepareStatement("SELECT * FROM `customerdetails` WHERE phoneNumber='" + save + "'");
                ResultSet resultSet = findCustomerDetails.executeQuery();
                if(resultSet.next()){
                    String firstNameString = resultSet.getString("firstName");
                    firstNameLabel.setText(firstNameString);
                    String lastNameString = resultSet.getString("lastName");
                    lastNameLabel.setText(lastNameString);
                    String addressLineString = resultSet.getString("addressLine");
                    addressLineLabel.setText(addressLineString);
                    String postCodeString = resultSet.getString("postCode");
                    postCoodeLabel.setText(postCodeString);
                    numberLabel.setText(save);
                    numberLabel.setText("");
                    numberLabel.setText(save);
                    deliveryDetailsPane.setVisible(false);

                }else if(!resultSet.next()){
                    firstNameLabel.setText("");
                    lastNameLabel.setText("");
                    addressLineLabel.setText("");
                    postCoodeLabel.setText("");
                    numberLabel.setText(save);
                    numberTextFieldDelivery.setText(save);
                }
                findCustomerDetails.close();
                resultSet.close();

            }catch (FileNotFoundException | SQLException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }



    }


    //Database Connections
    public Connection getConnection(){
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:CUSTOMERDETAILS";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Connection getRecommenderDatabaseConnection(){
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



    //Navigation
    public void deliveryButtonClick(MouseEvent mouseEvent) throws IOException {
        if(firstNameLabel.getText().isEmpty() && lastNameLabel.getText().isEmpty() && addressLineLabel.getText().isEmpty() && postCoodeLabel.getText().isEmpty()){
            deliveryDetailsPane.setVisible(true);
            takeawayDetailsPane.setVisible(false);
        }else if(!firstNameLabel.getText().isEmpty() && !lastNameLabel.getText().isEmpty() && !addressLineLabel.getText().isEmpty() && !postCoodeLabel.getText().isEmpty()){
            App.setRoot("delivery");

            deliveryController.staticNameLabel.setText(firstNameLabel.getText());
            deliveryController.staticLastNameLabel.setText(lastNameLabel.getText());
            deliveryController.staticAddressLineLabel.setText(addressLineLabel.getText());
            deliveryController.staticPostCodeLabel.setText(postCoodeLabel.getText());
            deliveryController.staticNumberLabel.setText(numberLabel.getText());
        }

    }

    public void takeawayButtonClick(MouseEvent mouseEvent) throws IOException {
        if(firstNameLabel.getText().isEmpty() && lastNameLabel.getText().isEmpty() && addressLineLabel.getText().isEmpty() && postCoodeLabel.getText().isEmpty()){
            takeawayDetailsPane.setVisible(true);
            deliveryDetailsPane.setVisible(false);
        }else if(!firstNameLabel.getText().isEmpty() && !lastNameLabel.getText().isEmpty() && !addressLineLabel.getText().isEmpty() && !postCoodeLabel.getText().isEmpty()){
            App.setRoot("takeaway");

            takeawayController.staticNameLabel.setText(firstNameLabel.getText());
            takeawayController.staticLastNameLabel.setText(lastNameLabel.getText());
            takeawayController.staticAddressLineLabel.setText(addressLineLabel.getText());
            takeawayController.staticPostCodeLabel.setText(postCoodeLabel.getText());
            takeawayController.staticNumberLabel.setText(numberLabel.getText());
        }

    }

    public void walkInButtonClick(MouseEvent mouseEvent) throws IOException {
        App.setRoot("walkin");

    }

    public void orderListButtonClick(MouseEvent mouseEvent) throws IOException {
        App.setRoot("listoforderslogin");
    }

    public void takingsButtonClick(MouseEvent mouseEvent) throws IOException {
        App.setRoot("takingslogin");
    }

    public void settingsButtonCLciked(MouseEvent mouseEvent) throws IOException {
        App.setRoot("settings");
    }



    //Delivery Details Functionality
    public void saveCustomerDeliveryClick(MouseEvent mouseEvent) throws SQLException, IOException {
        Connection connection = getConnection();
        PreparedStatement saveCustomerDetailsQuery = connection.prepareStatement("INSERT INTO `customerdetails`(firstName, lastName, addressLine, postCode, phoneNumber) VALUES('"+firstNameTextFieldDelivery.getText()+"','"+lastNameTextFieldDelivery.getText()+"', '"+addressLineTextFieldDelivery.getText()+"', '"+postCodeTextFieldDelivery.getText()+"', '"+numberTextFieldDelivery.getText()+"') ");
        saveCustomerDetailsQuery.executeUpdate();
        saveCustomerDetailsQuery.close();
        App.setRoot("delivery");
        deliveryController.staticNameLabel.setText(firstNameTextFieldDelivery.getText());
        deliveryController.staticLastNameLabel.setText(lastNameTextFieldDelivery.getText());
        deliveryController.staticAddressLineLabel.setText(addressLineTextFieldDelivery.getText());
        deliveryController.staticPostCodeLabel.setText(postCodeTextFieldDelivery.getText());
        deliveryController.staticNumberLabel.setText(numberTextFieldDelivery.getText());

    }

    public void checkPostCodeClick(MouseEvent mouseEvent) throws IOException {
        googleMapsAPIImplementation();

    }



    //Takeaway Details Functionality
    public void saveCustomerTakeAwayClick(MouseEvent mouseEvent) throws SQLException, IOException {
        Connection connection = getConnection();
        PreparedStatement saveCustomerDetailsQueryTakeway = connection.prepareStatement("INSERT INTO `customerdetails`(firstName, lastName, addressLine, postCode, phoneNumber) VALUES('"+firstNameTextFieldTakeaway.getText()+"','"+lastNameTextFieldTakeaway.getText()+"', '"+addressLineTextFieldTakeaway.getText()+"', '"+postCodeTextFieldTakeaway.getText()+"', '"+numberTextFieldTakeaway.getText()+"') ");
        saveCustomerDetailsQueryTakeway.executeUpdate();
        saveCustomerDetailsQueryTakeway.close();
        App.setRoot("takeaway");
        takeawayController.staticNameLabel.setText(firstNameTextFieldTakeaway.getText());
        takeawayController.staticLastNameLabel.setText(lastNameTextFieldTakeaway.getText());
        takeawayController.staticAddressLineLabel.setText(addressLineTextFieldTakeaway.getText());
        takeawayController.staticPostCodeLabel.setText(postCodeTextFieldTakeaway.getText());
        takeawayController.staticNumberLabel.setText(numberTextFieldTakeaway.getText());
    }



    public void googleMapsAPIImplementation() throws IOException {
        webView.setVisible(true);
        File f = new File("C:\\Users\\Vahap\\Desktop\\Epos\\GoogleMaps.html");
        WebEngine webEngine = webView.getEngine();
        webEngine.load(f.toURI().toString());
        String postCadeTextField = postCodeTextFieldDelivery.getText();
        FileWriter writerFile = new FileWriter(new File("C:\\Users\\Vahap\\Desktop\\WebServer\\PostCode.txt"));
        writerFile.write(postCadeTextField);
        writerFile.flush();
        writerFile.close();
        // DESTINATION MATRIX FOR DELIVERY AVAILIBITLY
        String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+source+"&destinations="+postCadeTextField+"&key="+ API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        FileWriter writer = new FileWriter("GoogleMapsJsonData.txt");
        writer.write(response.body().string());
        writer.flush();
        writer.close();

        FileReader reader = new FileReader("C:\\Users\\Vahap\\Desktop\\Epos\\GoogleMapsJsonData.txt");
        BufferedReader br = new BufferedReader(reader);
        String distanceData;
        String line;


        while((line = br.readLine()) != null){
            if(line.contains("text")){
                distanceData = line.split(":")[1].trim();
                String km = distanceData.substring(1,4);

                double kmToMile = Double.parseDouble(km)*0.621371;
                System.out.println(kmToMile);

                String longDistanceString = Double.toString(kmToMile);
                String displayString = longDistanceString.split(":")[0].trim().substring(0,4);
                System.out.println(longDistanceString.split(":")[0].trim().substring(0,4));

                if(kmToMile > 5.0){
                    postCodeValidationLabel.setVisible(true);
                    postCodeValidationLabel.setText("Delivery not available for the given postcode");
                    saveCustomerDeliveryButton.setVisible(false);
                    showMilageLabel.setText(displayString + " miles away");
                }
                else if(kmToMile < 5.0){
                    saveCustomerDeliveryButton.setVisible(true);
                    postCodeValidationLabel.setVisible(false);
                    showMilageLabel.setText(displayString + " miles away");
                }
                break;
            }
            if(line.contains("text")){
                distanceData = line.split("duration")[1].trim();
                System.out.println(distanceData);
                String km = distanceData.substring(1,4);

                System.out.println(km);

                break;
            }
        }

    }


    //Recommender
    public void getMostOrderedSection() throws SQLException {
        PreparedStatement preparedStatement = getRecommenderDatabaseConnection().prepareStatement("SELECT `sectionOrdered`,\n" +
                "             COUNT(`sectionOrdered`) AS `value_occurrence` \n" +
                "    FROM     `recommenderData`\n" +
                "    WHERE     customerNumber = '"+numberLabel.getText()+"'\n" +
                "    GROUP BY `sectionOrdered`\n" +
                "    ORDER BY `value_occurrence` DESC\n" +
                "    LIMIT    1;");

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            System.out.println(resultSet.getString("sectionOrdered" )+ " " +resultSet.getString("value_occurrence"));
            if(resultSet.getString("sectionOrdered").equals(resultSet.getString("sectionOrdered"))){
                System.out.println("Recommended product is: " + recommendAProduct(resultSet.getString("sectionOrdered")));
                recommendLabelOne.setVisible(true);
                recommendLabelTwo.setVisible(true);
                recommendLabelMain.setText(recommendAProduct(resultSet.getString("sectionOrdered")));

            }
        }
        resultSet.close();
    }


    public String recommendAProduct(String sectionName) throws SQLException {
        PreparedStatement selectRandomProduct = getRecommenderDatabaseConnection().prepareStatement("SELECT * FROM "+sectionName+" ORDER BY random() LIMIT 1");
        ResultSet resultSet = selectRandomProduct.executeQuery();
        String itemName = "";
        while(resultSet.next()){
            itemName = resultSet.getString(2);
        }
        return itemName;


    }


    public void recommendProductButtonClick(MouseEvent mouseEvent) throws SQLException {
        getMostOrderedSection();

    }


}
