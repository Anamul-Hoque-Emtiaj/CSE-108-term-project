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

    public Player() {
    }

    public Player(String name, String country, Double age, Double height, String club, String position, Integer number, Double weeklySalary, String imageName) {
        Name = name;
        Country = country;
        Age = age;
        Height = height;
        Club = club;
        Position = position;
        Number = number;
        WeeklySalary = weeklySalary;
        this.imageName = imageName;
    }
    synchronized public void setName(String name) {
        Name = name;
    }

    synchronized public void setCountry(String country) {
        Country = country;
    }

    synchronized public void setAge(Double age) {
        Age = age;
    }

    synchronized public void setHeight(Double height) {
        Height = height;
    }

    synchronized public void setClub(String club) {
        Club = club;
    }

    synchronized public void setPosition(String position) {
        Position = position;
    }

    synchronized public void setNumber(Integer number) {
        Number = number;
    }

    synchronized public void setWeeklySalary(Double weeklySalary) {
        WeeklySalary = weeklySalary;
    }

    synchronized public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    synchronized public void setAmount(double amount) {
        Amount = amount;
    }
    synchronized public void setInPending(boolean inPending) {
        this.inPending = inPending;
    }

    synchronized public double getAmount() {
        return Amount;
    }

    synchronized public boolean isInPending() {
        return inPending;
    }

    synchronized public String getImageName() {
        return imageName;
    }

    synchronized public String getName() {
        return Name;
    }

    synchronized public String getCountry() {
        return Country;
    }

    synchronized public Double getAge() {
        return Age;
    }

    synchronized public Double getHeight() {
        return Height;
    }

    synchronized public String getClub() {
        return Club;
    }

    synchronized public String getPosition() {
        return Position;
    }

    synchronized public Integer getNumber() {
        return Number;
    }

    synchronized public Double getWeeklySalary() {
        return WeeklySalary;
    }

}
