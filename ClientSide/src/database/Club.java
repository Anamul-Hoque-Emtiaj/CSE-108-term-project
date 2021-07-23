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

    synchronized public String getPassword() {
        return password;
    }

    synchronized public double getBalance() {
        return balance;
    }

    synchronized public void changePassword(String password){
        this.password = password;
    }

    synchronized public void setBalance(double balance) {
        this.balance = balance;
    }

    synchronized public int pendingPlayerCount(){
        return pendingList.size();
    }

    synchronized public List<Player> getPendingList(){
       return pendingList;
    }

    synchronized public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    synchronized public void setPendingList(List<Player> pendingList) {
        this.pendingList = pendingList;
    }

    synchronized public void addPlayer(Player player){
        player.setClub(name);
        player.setAmount(0);
        player.setInPending(false);
        playerList.add(player);
        if(!countryList.contains(player.getCountry())){
            countryList.add(player.getCountry());
        }
    }

    public void setCountryList(List<String> countryList) {
        this.countryList = countryList;
    }

    synchronized public void deletePlayer(Player player){
        for (Player p: playerList){
            if(player.getName().equals(p.getName())){
                int in = playerList.indexOf(p);
                playerList.remove(in);
                break;
            }
        }
        int count = 0;
        for (Player p: playerList){
            if(p.getCountry().equals(player.getCountry())){
                count++;
            }
        }
        if(count==1){
            for (String country: countryList){
                if(country.equals(player.getCountry())){
                    int in = countryList.indexOf(country);
                    countryList.remove(in);
                    break;
                }
            }
        }
        for (Player p: pendingList){
            if(player.getName().equals(p.getName())){
                int in = pendingList.indexOf(p);
                pendingList.remove(in);
                break;
            }
        }
    }

    synchronized public void sellRequest(String playerName, double amount){
        for (Player p: playerList){
            if(p.getName().equals(playerName)){
                p.setAmount(amount);
                p.setInPending(true);
                pendingList.add(p);
                break;
            }
        }
        for (Player p: pendingList){
            if(p.getName().equals(playerName)){
                p.setAmount(amount);
                p.setInPending(true);
                break;
            }
        }
    }

    synchronized public void editPlayer(Player p){
        for(Player player: playerList){
            if(p.getName().equals(player.getName())){
                player.setImageName(p.getImageName());
                player.setAge(p.getAge());
                player.setHeight(p.getHeight());
                player.setWeeklySalary(p.getWeeklySalary());
                player.setNumber(p.getNumber());
                break;
            }
        }
        for(Player player: pendingList){
            if(p.getName().equals(player.getName())){
                player.setImageName(p.getImageName());
                player.setAge(p.getAge());
                player.setHeight(p.getHeight());
                player.setWeeklySalary(p.getWeeklySalary());
                player.setNumber(p.getNumber());
                break;
            }
        }
    }

    synchronized public void soldPlayer(String playerName){
        for (Player player: playerList){
            if(player.getName().equals(playerName)){
                balance+=player.getAmount();
                deletePlayer(player);
                break;
            }
        }
    }

    synchronized public void buyPlayer(Player player){
        balance-=player.getAmount();
        player.setClub(name);
        player.setAmount(0);
        player.setInPending(false);
        addPlayer(player);
    }

    synchronized public void deleteSellRequest(Player player){
        for (Player p: pendingList){
            if(p.getName().equals(player.getName())){
                p.setInPending(false);
                p.setAmount(0);
                int in = pendingList.indexOf(p);
                pendingList.remove(in);
                break;
            }
        }
        for (Player p: playerList){
            if(p.getName().equals(player.getName())){
                p.setInPending(false);
                p.setAmount(0);
                break;
            }
        }
    }

    synchronized public double totalYearlySalary(){
        double salary=0;
        for(Player player: playerList){
            salary+=player.getWeeklySalary();
        }
        salary*=52.0;
        return salary;
    }
    synchronized public List<Player> playersWithMaximumHeight(){
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

    synchronized public List<Player> playersWithMaximumAge(){
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

    synchronized public List<Player> playersWithMaximumWeeklySalary(){
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

    synchronized public List<Player> playersWithMinimumHeight(){
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

    synchronized public List<Player> playersWithMinimumAge(){
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

    synchronized public List<Player> playersWithMinimumWeeklySalary(){
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

    synchronized public int playerCount(){
        return playerList.size();
    }

    synchronized public List<Player> getPlayerList() {
        return playerList;
    }

    synchronized public String getName() {
        return name;
    }

    synchronized public List<String> getCountryList() {
        return countryList;
    }

    synchronized public HashMap<String,Integer> countryWisePlayerCount(){
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
