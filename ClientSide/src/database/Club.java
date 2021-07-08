package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Club {
    private String name;
    private String password;
    private double balance;
    private List<Player> playerList;
    private List<String> countryList;
    private HashMap<Player,Double> sellPendingPlayers;

    public Club(String name, Player player) {
        this.name = name;
        playerList = new ArrayList();
        countryList = new ArrayList();
        sellPendingPlayers = new HashMap<>();
        playerList.add(player);
        countryList.add(player.getCountry());
    }

    public Club(String name, String password, double balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
        playerList = new ArrayList();
        countryList = new ArrayList();
        sellPendingPlayers = new HashMap<>();
    }

    public String getPassword() {
        return password;
    }

    public int pendingCount(){
        return sellPendingPlayers.size();
    }

    public HashMap<Player, Double> getSellPendingPlayers() {
        return sellPendingPlayers;
    }

    public double getPlayerSellAmount(Player player){
        return sellPendingPlayers.get(player);
    }

    public double getBalance() {
        return balance;
    }

    public void addPlayer(Player player){
        playerList.add(player);
        if(!countryList.contains(player.getCountry())){
            countryList.add(player.getCountry());
        }
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void playerSellRequest(Player player, double amount){
        sellPendingPlayers.put(player,amount);
    }
    public void playerSold(Player player){
        double amount = sellPendingPlayers.remove(player);
        balance+=amount;
        boolean isRemove = playerList.remove(player);
    }
    public void buyPlayer(Player player, double amount){
        balance -=amount;
        player.setClub(name);
        playerList.add(player);
    }

    public double totalYearlySalary(){
        double salary=0;
        for(Player player: playerList){
            salary+=player.getWeeklySalary();
        }
        salary*=52.0;
        return salary;
    }
    public List<Player> playersWithMaximumHeight(){
        double max=-1,temp;
        for(Player player: playerList){
            temp = player.getHeight();
            if(temp>max)
                max=temp;
        }
        List<Player> searchPlayers = new ArrayList<>();
        for(Player player: playerList){
            if(player.getHeight()==max){
                searchPlayers.add(player);
            }
        }
        return searchPlayers;
    }

    public List<Player> playersWithMaximumAge(){
        int max=-1,temp;
        for(Player player: playerList){
            temp = player.getAge();
            if(temp>max)
                max=temp;
        }
        List<Player> searchPlayers = new ArrayList<>();
        for(Player player: playerList){
            if(player.getAge()==max){
                searchPlayers.add(player);
            }
        }
        return searchPlayers;
    }

    public List<Player> playersWithMaximumWeeklySalary(){
        double max=-1,temp;
        for(Player player: playerList){
            temp = player.getWeeklySalary();
            if(temp>max)
                max=temp;
        }
        List<Player> searchPlayers = new ArrayList<>();
        for(Player player: playerList){
            if(player.getWeeklySalary()==max){
                searchPlayers.add(player);
            }
        }
        return searchPlayers;
    }
    public int playerCount(){
        return playerList.size();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public String getName() {
        return name;
    }

    public List<String> getCountryList() {
        return countryList;
    }

    public HashMap<String,Integer> countryWisePlayerCount(){
        HashMap<String,Integer> result = new HashMap<String, Integer>();
        for(String country: countryList){
            int count = 0;
            for(Player player: playerList){
                if(player.getCountry().equalsIgnoreCase(country)) {
                    count++;
                }
            }
            result.put(country,count);
        }
        return result;
    }
}
