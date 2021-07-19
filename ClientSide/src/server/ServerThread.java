package server;

import database.Club;
import database.Player;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;


public class ServerThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private List<NetworkUtil>networkUtilList;
    private static List<Player> playerList;
    private static List<Club> clubList;
    private static List<String> countryList;
    private static List<Player> pendingPlayerList;


    public ServerThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        thr.start();
    }

    public static void load(List<Player> playerLists, List<Club> clubLists, List<String> countryLists, List<Player> pendingPlayerLists){
        playerList = playerLists;
        clubList = clubLists;
        countryList = countryLists;
        pendingPlayerList = pendingPlayerLists;
    }

    public void sendUpdatedPlayerList(){
        networkUtilList = Server.getNetworkUtilList();
        new PlayerSellThread(networkUtilList,pendingPlayerList);
    }

    public void sendUpdatedClub(String clubName){
        new SendUpdatedClubThread(networkUtil,clubName,playerList,clubList,pendingPlayerList);
    }

    public void printPlayerList(){
        System.out.println(playerList.size());
        System.out.println(pendingPlayerList.size());
    }

    public void run() {
        try {
            while (true) {
                String str = (String) networkUtil.read();
                System.out.println("Server read: "+ str);
                if(str.equals("clubOwner,login")){
                    String read = (String) networkUtil.read();
                    read.trim();
                    System.out.println(read);
                    String[] auth = read.split(",");
                    boolean isValidClub = false;
                    for(Club club: clubList){

                        if(club.getName().equals(auth[0].trim())&& club.getPassword().equals(auth[1].trim())){
                            networkUtil.write("login successful");
                            sendUpdatedClub(club.getName());
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
                            System.out.println("Server MainList: "+ player.getImageName());
                            break;
                        }
                    }
                    for(Club club: clubList){
                        if(club.getName().equals(p.getClub())){
                            for(Player player: club.getPlayerList()){
                                if(p.getName().equals(player.getName())){
                                    player.setImageName(p.getImageName());
                                    player.setAge(p.getAge());
                                    player.setHeight(p.getHeight());
                                    player.setWeeklySalary(p.getWeeklySalary());
                                    player.setNumber(p.getNumber());
                                    System.out.println("Server Clublist: "+ player.getImageName());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }else if(str.equals("clubOwner,sellRequest")){
                    String s = (String) networkUtil.read();
                    String[] info = s.split(",");
                    String clubName = info[0];
                    String playerName = info[1];
                    double amount = Double.parseDouble(info[2]);
                    boolean alreadyExit = false;
                    for (Player player: playerList){
                        if(player.getName().equals(playerName)){
                            alreadyExit = pendingPlayerList.contains(player);
                            if(!alreadyExit){
                                player.sellRequest(amount);
                                pendingPlayerList.add(player);
                            }
                            break;
                        }
                    }
                    if(!alreadyExit){
                        for (Club club: clubList){
                            if(club.getName().equals(clubName)){
                                for (Player player: club.getPlayerList()){
                                    if(player.getName().equals(playerName)){
                                        player.sellRequest(amount);
                                        club.sellRequest(player);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    if(alreadyExit){
                        networkUtil.write("already requested");
                    }else{
                        networkUtil.write("request accepted");
                        sendUpdatedPlayerList();
                    }
                }else if(str.equals("clubOwner,deletePlayer")){
                    printPlayerList();
                    Player player = (Player) networkUtil.read();
                    System.out.println(playerList.contains(player));
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
                    for (Player p: pendingPlayerList){
                        if(player.getName().equals(p.getName())){
                            int in = pendingPlayerList.indexOf(p);
                            pendingPlayerList.remove(in);
                            break;
                        }
                    }
                    sendUpdatedPlayerList();
                    printPlayerList();
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
                        for (Club club: clubList){
                            if(club.getName().equals(player.getClub())){
                                club.addPlayer(player);
                                break;
                            }
                        }
                        networkUtil.write("Player Added successfully");
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
                }else if(str.equals("send updated buy list")){
                    sendUpdatedPlayerList();
                }else if(str.equals("delete request")){
                    Player player = (Player) networkUtil.read();
                    for (Player p: pendingPlayerList){
                        if(p.getName().equals(player.getName())){
                            p.deleteRequest();
                            int in = pendingPlayerList.indexOf(p);
                            pendingPlayerList.remove(in);
                            break;
                        }
                    }
                    for (Club club: clubList){
                        if(club.getName().equals(player.getClub())){
                            club.deleteSellRequest(player);
                        }
                    }
                    sendUpdatedPlayerList();
                }
                else if(str.equals("exit")){
                    Server.exit(playerList,clubList);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
