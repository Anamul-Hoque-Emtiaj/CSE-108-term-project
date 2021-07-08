package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Country {
    private String name;
    private List<Player> playerList;
    private List<String> clubList;

    public Country(String name, Player player) {
        this.name = name;
        playerList = new ArrayList();
        clubList = new ArrayList();
        playerList.add(player);
        clubList.add(player.getClub());
    }
    public void addPlayer(Player player){
        playerList.add(player);
        if(!clubList.contains(player.getClub())){
            clubList.add(player.getClub());
        }
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

    public List<String> getClubList() {
        return clubList;
    }
    public HashMap<String,Integer> clubWisePlayerCount(){
        HashMap<String,Integer> result = new HashMap<String, Integer>();
        for(String club: clubList){
            int count = 0;
            for(Player player: playerList){
                if(player.getClub().equalsIgnoreCase(club)) {
                    count++;
                }
            }
            result.put(club,count);
        }
        return result;
    }
}
