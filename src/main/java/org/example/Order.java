package org.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class Order {


    public SimpleStringProperty poroductName;
    public SimpleDoubleProperty productPrice;


    public Order(String poroductName, Double productPrice) {

        this.poroductName = new SimpleStringProperty(poroductName);
        this.productPrice = new SimpleDoubleProperty(productPrice);
    }



    public void setPoroductName(String poroductName) {
        this.poroductName.set(poroductName);
    }

    public void setProductPrice(double productPrice) {
        this.productPrice.set(productPrice);
    }

    public String getPoroductName() {
        return poroductName.get();
    }

    public SimpleStringProperty poroductNameProperty() {
        return poroductName;
    }

    public double getProductPrice() {
        return productPrice.get();
    }

    public SimpleDoubleProperty productPriceProperty() {
        return productPrice;
    }
}