package server;

import database.Club;
import database.Player;
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ServerThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private static List<Player> playerList;
    private static List<Club> clubList;
    private static List<String> countryList;
    private static List<Player> pendingPlayerList;
    private static HashMap<NetworkUtil,String> networkUtilStringHashMap;
    private static String adminName;
    private static String adminPassword;


    public ServerThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public static void load(List<Player> playerLists, List<Club> clubLists, List<String> countryLists, List<Player> pendingPlayerLists, String aName,String aPassword){
        playerList = playerLists;
        clubList = clubLists;
        countryList = countryLists;
        adminName = aName;
        adminPassword = aPassword;
        pendingPlayerList = pendingPlayerLists;
        networkUtilStringHashMap = new HashMap<>();
    }

    public void sendUpdatedPlayerListToAll(){
        PlayerSellThread p = new PlayerSellThread(networkUtilStringHashMap,pendingPlayerList);
        try {
            p.getThr().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendUpdatedClub(String clubName){
        SendUpdatedClubThread p = new SendUpdatedClubThread(networkUtil,clubName,playerList,clubList,pendingPlayerList);
        try {
            p.getThr().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendUpdatedClubToAll(String clubName){
        SendUpdatedClubToAllThread p = new SendUpdatedClubToAllThread(networkUtilStringHashMap,clubName,playerList,clubList,pendingPlayerList);
        try {
            p.getThr().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendUpdatedPlayersListToAdmin(){
        List<Player> t = new ArrayList<>();
        for (Player player: playerList){
            t.add(player);
        }
        for (NetworkUtil util: networkUtilStringHashMap.keySet()){
            if(networkUtilStringHashMap.get(util).equals(adminName)){
                try {
                    util.write(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void run() {
        try {
            while (true) {
                String str = (String) networkUtil.read();
                System.out.println("Server read: "+ str);
                if(str.equals("clubOwner,login")){
                    String read = (String) networkUtil.read();
                    read.trim();
                    String[] auth = read.split(",");
                    boolean isValidClub = false;
                    for(Club club: clubList){

                        if(club.getName().equals(auth[0].trim())&& club.getPassword().equals(auth[1].trim())){
                            networkUtil.write("login successful");
                            sendUpdatedClub(club.getName());
                            networkUtilStringHashMap.put(networkUtil,club.getName());
                            isValidClub = true;
                            break;
                        }
                    }
                    if(!isValidClub){
                        networkUtil.write("login failed");
                    }
                }else if(str.equals("clubOwner,sendMyClub")){
                    String name = (String) networkUtil.read();
                    sendUpdatedClub(name);

                }else if(str.equals("clubOwner,editPlayer")){
                    Player p = (Player) networkUtil.read();
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
                    for(Player player: pendingPlayerList){
                        if(p.getName().equals(player.getName())){
                            player.setImageName(p.getImageName());
                            player.setAge(p.getAge());
                            player.setHeight(p.getHeight());
                            player.setWeeklySalary(p.getWeeklySalary());
                            player.setNumber(p.getNumber());
                            break;
                        }

                    }
                    for(Club club: clubList){
                        if(club.getName().equals(p.getClub())){
                            club.editPlayer(p);
                            break;
                        }
                    }
                    sendUpdatedClubToAll(p.getClub());
                    sendUpdatedPlayerListToAll();
                    sendUpdatedPlayersListToAdmin();
                }else if(str.equals("clubOwner,sellRequest")){
                    String s = (String) networkUtil.read();
                    String[] info = s.split(",");
                    String clubName = info[0];
                    String playerName = info[1];
                    double amount = Double.parseDouble(info[2]);
                    boolean alreadyExit = false;
                    for (Player player: pendingPlayerList){
                        if(player.getName().equals(playerName)){
                            alreadyExit = true;
                            break;
                        }
                    }
                    if(alreadyExit){
                        networkUtil.write("already requested");
                    }else {
                        for (Club club: clubList){
                            if(club.getName().equals(clubName)){
                                club.sellRequest(playerName,amount);
                                break;
                            }
                        }
                        for (Player player: playerList){
                            if(player.getName().equals(playerName)){
                                player.setAmount(amount);
                                player.setInPending(true);
                                pendingPlayerList.add(player);
                                break;
                            }
                        }
                        for (Player player: pendingPlayerList){
                            if(player.getName().equals(playerName)){
                                player.setAmount(amount);
                                player.setInPending(true);
                                break;
                            }
                        }
                        networkUtil.write("request accepted");
                        sendUpdatedPlayerListToAll();
                        sendUpdatedClubToAll(clubName);
                        sendUpdatedPlayersListToAdmin();
                    }
                }else if(str.equals("clubOwner,deletePlayer")){
                    Player player = (Player) networkUtil.read();
                    for (Club club: clubList){
                        if (club.getName().equals(player.getClub())){
                            club.deletePlayer(player);
                            break;
                        }
                    }

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
                    for (Player p: pendingPlayerList){
                        if(player.getName().equals(p.getName())){
                            int in = pendingPlayerList.indexOf(p);
                            pendingPlayerList.remove(in);
                            break;
                        }
                    }
                    sendUpdatedPlayerListToAll();
                    sendUpdatedClubToAll(player.getClub());
                    sendUpdatedPlayersListToAdmin();
                }else if(str.equals("Add player")){
                    Player player = (Player) networkUtil.read();
                    boolean canAdded = true;
                    for (Player player1: playerList){
                        if(player1.getName().equals(player.getName())){
                            canAdded = false;
                        }
                    }
                    if(canAdded){
                        playerList.add(player);
                        if(!countryList.contains(player.getCountry())){
                            countryList.add(player.getCountry());
                        }
                        for (Club club: clubList){
                            if(club.getName().equals(player.getClub())){
                                club.addPlayer(player);
                                break;
                            }
                        }
                        networkUtil.write("Player Added successfully");
                        sendUpdatedClubToAll(player.getClub());
                        sendUpdatedPlayersListToAdmin();
                    }else {
                        networkUtil.write("Adding failed");
                    }

                }else if(str.equals("change password")){
                    String s = (String) networkUtil.read();
                    String[] auth = s.split(",");
                    for (Club club: clubList){
                        if(club.getName().equals(auth[0])){
                            club.changePassword(auth[1]);
                            break;
                        }
                    }
                    sendUpdatedClubToAll(auth[0]);
                }else if(str.equals("send updated buy list")){
                    List<Player> t = new ArrayList<>();
                    for (Player player: pendingPlayerList){
                        t.add(player);
                    }
                    networkUtil.write(t);
                }else if(str.equals("delete request")){
                    Player player = (Player) networkUtil.read();
                    for (Player p: pendingPlayerList){
                        if(p.getName().equals(player.getName())){
                            p.setInPending(false);
                            p.setAmount(0);
                            int in = pendingPlayerList.indexOf(p);
                            pendingPlayerList.remove(in);
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
                    for (Club club: clubList){
                        if(club.getName().equals(player.getClub())){
                            club.deleteSellRequest(player);
                        }
                    }
                    sendUpdatedClubToAll(player.getClub());
                    sendUpdatedPlayerListToAll();
                    sendUpdatedPlayersListToAdmin();
                }else if(str.equals("buy Player")){
                    String newClubName = (String) networkUtil.read();
                    Player player = (Player) networkUtil.read();
                    String oldClubName = player.getClub();
                    boolean canBuy = false;
                    for (Player p: pendingPlayerList){
                        if(p.getName().equals(player.getName())){
                            canBuy = true;
                            break;
                        }
                    }
                    if(canBuy){
                        for (Club club: clubList){
                            if(club.getName().equals(newClubName)){
                                club.buyPlayer(player);
                            }
                            if(club.getName().equals(oldClubName)){
                                club.soldPlayer(player.getName());
                            }
                        }
                        for (Player p: playerList){
                            if(p.getName().equals(player.getName())){
                                p.setClub(newClubName);
                                p.setAmount(0);
                                p.setInPending(false);
                            }
                        }
                        for (Player p: pendingPlayerList){
                            if(p.getName().equals(player.getName())){
                                int in = pendingPlayerList.indexOf(p);
                                pendingPlayerList.remove(in);
                                break;
                            }
                        }
                        networkUtil.write("Successful");
                        sendUpdatedPlayerListToAll();
                        sendUpdatedClubToAll(newClubName);
                        sendUpdatedClubToAll(oldClubName);
                        sendUpdatedPlayersListToAdmin();
                    }else {
                        networkUtil.write("failed");
                    }
                }else if(str.equals("logout")){
                    String clubName = (String) networkUtil.read();
                    networkUtilStringHashMap.remove(networkUtil);
                }
                else if(str.equals("exit")){
                    for (NetworkUtil util: networkUtilStringHashMap.keySet() ){
                        util.closeConnection();
                    }
                    networkUtilStringHashMap.clear();
                    //Server.exit(playerList,clubList);
                }else if(str.equals("admin,login")){
                    String read = (String) networkUtil.read();
                    read.trim();
                    String[] auth = read.split(",");
                    if(auth[0].equals(adminName)&&auth[1].equals(adminPassword)){
                        networkUtilStringHashMap.put(networkUtil,adminName);
                        networkUtil.write("login successful");
                    }else {
                        networkUtil.write("login failed");
                    }
                }else if(str.equals("send all players list")){
                    List<Player> t = new ArrayList<>();
                    for (Player player: playerList){
                        t.add(player);
                    }
                    networkUtil.write(t);
                }else if(str.equals("search Club")){
                    String clubName = (String) networkUtil.read();
                    boolean found = false;
                    for (Club club: clubList){
                        if(club.getName().equals(clubName)){
                            found = true;
                            break;
                        }
                    }
                    if(found){
                        networkUtil.write("valid club");
                        sendUpdatedClub(clubName);
                    }else {
                        networkUtil.write("invalid club");
                    }
                }else if(str.equals("add club")){
                    String clubName = (String) networkUtil.read();
                    Club newClub = new Club(clubName);
                    clubList.add(newClub);
                    networkUtil.write(newClub);
                }else if(str.equals("change admin info")){
                    networkUtil.write(adminName+","+adminPassword);
                    String s1 = (String) networkUtil.read();
                    if(s1.equals("change password")){
                        String s = (String) networkUtil.read();
                        String[] auth = s.split(",");
                        adminName = auth[0];
                        adminPassword = auth[1];
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
