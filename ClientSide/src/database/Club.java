package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Club {
    private String name;
    private List<Player> playerList;
    private List<String> countryList;

    public Club(String name, List<Player> playerList) {
        this.name = name;
        this.playerList = playerList;
        for(Player player: playerList){
            if(!countryList.contains(player.getCountry())){
                countryList.add(player.getCountry());
            }
        }
    }

    public Club(String name) {
        this.name = name;
    }
    public void addPlayer(Player player){
        playerList.add(player);
        if(!countryList.contains(player.getCountry())){
            countryList.add(player.getCountry());
        }
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
