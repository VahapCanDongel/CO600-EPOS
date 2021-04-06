package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.*;

public class TakingsLoginController {
    public TextField userNameTextField;
    public PasswordField passwordTextField;
    public Label notCorrectError;


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

    public void loginButtonClicked(MouseEvent mouseEvent) throws SQLException, IOException {
        if(loginFunction(userNameTextField.getText(), passwordTextField.getText())){
            App.setRoot("takings");
        }else{
            notCorrectError.setVisible(true);
        }

    }

    public boolean loginFunction(String username, String password) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM `adminlogindata` WHERE username = ? and password = ?");
        ResultSet resultSet =  null;
        try {
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){return false;}
        finally {
            statement.close();
            resultSet.close();
        }
    }
}
