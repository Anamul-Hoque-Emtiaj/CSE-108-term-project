package database;

import javafx.scene.control.Button;

import java.io.Serializable;

public class Player implements Serializable {
    private String Name;
    private String Country;
    private Double Age;
    private Double Height;
    private String Club;
    private String Position;
    private Integer Number;
    private Double WeeklySalary;
    private String imageName;
    private double Amount;
    private boolean inPending;
    private Button buyButton;
    private Button deleteButton;


    public Player() {
        Amount = 0;
        inPending = false;
       /* buyButton = new Button("Buy");
        deleteButton = new Button("Delete");*/
    }

    public Player(String name, String country, Double age, Double height, String club, String position, Integer number, Double weeklySalary) {
        Name = name;
        Country = country;
        Age = age;
        Height = height;
        Club = club;
        Position = position;
        Number = number;
        WeeklySalary = weeklySalary;
        Amount = 0;
        inPending = false;
        buyButton = new Button("Buy");
        deleteButton = new Button("Delete");
    }
    public void setName(String name) {
        Name = name;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setAge(Double age) {
        Age = age;
    }

    public void setHeight(Double height) {
        Height = height;
    }

    public void setClub(String club) {
        Club = club;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public void setNumber(Integer number) {
        Number = number;
    }

    public void setWeeklySalary(Double weeklySalary) {
        WeeklySalary = weeklySalary;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
    public void setInPending(boolean inPending) {
        this.inPending = inPending;
    }

    public double getAmount() {
        return Amount;
    }

    public boolean isInPending() {
        return inPending;
    }

    public String getImageName() {
        return imageName;
    }

    public String getName() {
        return Name;
    }

    public String getCountry() {
        return Country;
    }

    public Double getAge() {
        return Age;
    }

    public Double getHeight() {
        return Height;
    }

    public String getClub() {
        return Club;
    }

    public String getPosition() {
        return Position;
    }

    public Integer getNumber() {
        return Number;
    }

    public Double getWeeklySalary() {
        return WeeklySalary;
    }

    public void buy(String newClubName){
        this.Club = newClubName;
        this.inPending = false;
        this.Amount = 0;
    }
    public void deleteRequest(){
        this.inPending = false;
        this.Amount = 0;
    }
    public void sellRequest(double amount){
        this.Amount = amount;
        this.inPending = true;
    }
}
