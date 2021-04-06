package org.example;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class ListOfOrders {


    public SimpleStringProperty poroductName;
    public SimpleIntegerProperty productPrice;


    public ListOfOrders(String poroductName, Integer productPrice) {
        this.poroductName = new SimpleStringProperty(poroductName);
        this.productPrice = new SimpleIntegerProperty(productPrice);

    }

    public String getPoroductName() {
        return poroductName.get();
    }

    public SimpleStringProperty poroductNameProperty() {
        return poroductName;
    }

    public void setPoroductName(String poroductName) {
        this.poroductName.set(poroductName);
    }

    public int getProductPrice() {
        return productPrice.get();
    }

    public SimpleIntegerProperty productPriceProperty() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice.set(productPrice);
    }
}
