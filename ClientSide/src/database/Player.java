package database;

import java.io.Serializable;

public class Player implements Serializable {
    private String Name;
    private String Country;
    private Integer Age;
    private Double Height;
    private String Club;
    private String Position;
    private Integer Number;
    private Double WeeklySalary;

    public void setName(String name) {
        Name = name;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setAge(Integer age) {
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

    public String getName() {
        return Name;
    }

    public String getCountry() {
        return Country;
    }

    public Integer getAge() {
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

    public Player() {
    }

    public Player(String name, String country, Integer age, Double height, String club, String position, Integer number, Double weeklySalary) {
        Name = name;
        Country = country;
        Age = age;
        Height = height;
        Club = club;
        Position = position;
        Number = number;
        WeeklySalary = weeklySalary;
    }
}
