package ie.cit.architect.protracker.model;

/**
 * Created by brian on 04/05/17.
 */
public class Fee {

    private int price;
    private double vat;

    public Fee() {
    }

    public Fee(int price) {
        this.price = price;
        setVat();
    }


    public double getVat() {
        return vat;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setVat() {
        this.vat = 22.5;
    }

    public double calculateFee() {
        return price + vat;
    }


    public static void main(String[] args) {
        Fee t = new Fee(100);
        System.out.println(t.calculateFee());
    }
}



