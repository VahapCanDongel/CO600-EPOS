package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.sql.*;

public class Taking {
    public SimpleStringProperty orderType;
    public SimpleStringProperty totalDate;
    public SimpleDoubleProperty totalSum;

    public Taking(String orderType,String totalDate , Double totalSum) {
        this.orderType = new SimpleStringProperty(orderType);
        this.totalDate = new SimpleStringProperty(totalDate);
        this.totalSum = new SimpleDoubleProperty(totalSum);
    }

    public String getOrderType() {
        return orderType.get();
    }

    public SimpleStringProperty orderTypeProperty() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType.set(orderType);
    }



    public double getTotalSum() {
        return totalSum.get();
    }

    public SimpleDoubleProperty totalSumProperty() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum.set(totalSum);
    }

    public String getTotalDate() {
        return totalDate.get();
    }

    public SimpleStringProperty totalDateProperty() {
        return totalDate;
    }

    public void setTotalDate(String totalDate) {
        this.totalDate.set(totalDate);
    }
}
