import java.util.Date;

public class Order {
    private String username;
    private String name;
    private int number;
    private Float total_price;
    private Float until_price;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Float total_price) {
        this.total_price = total_price;
    }

    public Float getUntil_price() {
        return until_price;
    }

    public void setUntil_price(Float until_price) {
        this.until_price = until_price;
    }

}
