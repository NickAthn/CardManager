package app.model;

import java.io.Serializable;
import java.util.Date;

public class Card implements Serializable {
    private String type;
    private String number;
    private String cardholder;
    private Date expirationDate;
    private String cvc;

    public Card(String type, String number, String cardholder, String cvc, Date expirationDate) {
        this.type = type;
        this.number = number;
        this.cardholder = cardholder;
        this.cvc = cvc;
        this.expirationDate = expirationDate;
    }

    // Getters
    public String getType() {return type;}
    public String getNumber() { return number;}
    public String getCardholder() { return cardholder;}
    public Date getExpirationDate() { return expirationDate;}
    public String getCvc() { return cvc;}

    // Setters
    public void setType(String type) {this.type = type;}
    public void setNumber(String number) { this.number = number;}
    public void setCardholder(String cardholder) { this.cardholder = cardholder;}
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate;}
    public void setCvc(String cvc) { this.cvc = cvc;}

}
