package app.model;

import java.util.Date;

public class Card {
    private String type;
    private String Cardnum;
    private String Carduser;
    private Date exp_date;
    private String cvc;

    public Card(String type, String Cardnum, String Carduser, String cvc, Date exp_date) {
        this.type = type;
        this.Cardnum = Cardnum;
        this.Carduser = Carduser;
        this.cvc = cvc;
        this.exp_date = exp_date;
    }
    public Card(){}
}
