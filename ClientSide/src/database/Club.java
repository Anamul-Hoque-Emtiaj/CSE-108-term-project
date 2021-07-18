package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Club implements Serializable {
    private String name;
    private String password;
    private double balance;
    private List<Player> playerList;
    private List<String> countryList;
    private List<Player> pendingList;


    public Club(String name, String password, double balance, List<Player > playerList, List<String> pName) {
        this.name = name;
        this.password = password;
        this.balance = balance;
        this.playerList = playerList;
        pendingList = new ArrayList<>();
        countryList = new ArrayList();
        for (Player player: playerList){
            if(!countryList.contains(player.getCountry())){
                countryList.add(player.getCountry());
            }
            if(pName.contains(player.getName())){
                pendingList.add(player);
            }
        }
    }

    public Club(String name) {
        this.name = name;
        password = name;
        this.balance = 50000000;
        playerList = new ArrayList();
        countryList = new ArrayList();
        pendingList = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int pendingPlayerCount(){
        return pendingList.size();
    }

    public List<Player> getPendingList(){
       return pendingList;
    }

    public void addPlayer(Player player){
        playerList.add(player);
        if(!countryList.contains(player.getCountry())){
            countryList.add(player.getCountry());
        }
    }

    public void deletePlayer(Player player){
        playerList.remove(player);
        if(pendingList.contains(player)){
            pendingList.remove(player);
        }
        boolean found = false;
        for (Player p: playerList){
            if(p.getCountry().equals(player.getCountry())){
                found = true;
                break;
            }
        }
        if(!found){
            countryList.remove(player.getCountry());
        }
    }

    public void sellRequest(Player player){
        pendingList.add(player);
    }

    public void soldPlayer(Player player, double amount){
        deletePlayer(player);
        balance+=amount;
    }

    public void buyPlayer(Player player, double amount){
        addPlayer(player);
        balance-=amount;
    }

    public void deleteSellRequest(Player player){
        pendingList.remove(player);
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
        double max=-1,temp;
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

    public List<Player> playersWithMinimumHeight(){
        double min=100000000,temp;
        for(Player player: playerList){
            temp = player.getHeight();
            if(temp<min)
                min=temp;
        }
        List<Player> searchPlayers = new ArrayList<>();
        for(Player player: playerList){
            if(player.getHeight()==min){
                searchPlayers.add(player);
            }
        }
        return searchPlayers;
    }

    public List<Player> playersWithMinimumAge(){
        double min=100000000,temp;
        for(Player player: playerList){
            temp = player.getAge();
            if(temp<min)
                min=temp;
        }
        List<Player> searchPlayers = new ArrayList<>();
        for(Player player: playerList){
            if(player.getAge()==min){
                searchPlayers.add(player);
            }
        }
        return searchPlayers;
    }

    public List<Player> playersWithMinimumWeeklySalary(){
        double min=1000000000,temp;
        for(Player player: playerList){
            temp = player.getWeeklySalary();
            if(temp<min)
                min=temp;
        }
        List<Player> searchPlayers = new ArrayList<>();
        for(Player player: playerList){
            if(player.getWeeklySalary()==min){
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
