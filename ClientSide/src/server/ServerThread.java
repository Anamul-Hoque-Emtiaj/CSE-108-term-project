package server;

import database.Club;
import database.Player;
import util.NetworkUtil;

import java.io.IOException;
import java.util.List;


public class ServerThread implements Runnable{
    private Thread thr;
    private NetworkUtil networkUtil;
    private List<Player> playerList;
    private List<Club> clubList;


    public ServerThread(NetworkUtil networkUtil, List<Player>playerList, List<Club>clubList) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        this.playerList = playerList;
        this.clubList = clubList;
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                String str = (String) networkUtil.read();
                System.out.println("Server read: "+ str);
                if(str.equals("clubOwner,login")){
                    networkUtil.write("provide your name and password");
                    String read = (String) networkUtil.read();
                    read.trim();
                    System.out.println(read);
                    String[] auth = read.split(",");
                    boolean isValidClub = false;
                    for(Club club: clubList){

                        if(club.getName().equals(auth[0].trim())&& club.getPassword().equals(auth[1].trim())){
                            networkUtil.write("login successful");
                            String r = (String) networkUtil.read();
                            networkUtil.write(club);
                            isValidClub = true;
                            break;
                        }
                    }
                    if(!isValidClub){
                        networkUtil.write("login failed");
                    }
                }else if(str.equals("clubOwner,sendMyClub")){
                    networkUtil.write("Provide your Club's Name");
                    String name = (String) networkUtil.read();
                    for(Club club:clubList){
                        if(club.getName().equals(name)){
                            networkUtil.write(club);
                            break;
                        }
                    }
                }else if(str.equals("clubOwner,editPlayer")){
                    networkUtil.write("Provide Updated Player");
                    Player p = (Player) networkUtil.read();
                    String clubName = null;
                    for(Player player: playerList){
                        if(p.getName().equals(player.getName())){
                            clubName = p.getClub();
                            player.setImageName(p.getImageName());
                            player.setAge(p.getAge());
                            player.setHeight(p.getHeight());
                            player.setWeeklySalary(p.getWeeklySalary());
                            player.setNumber(p.getNumber());
                        }
                    }
                    for(Club club: clubList){
                        if(club.getName().equals(clubName)){
                            for(Player player: club.getPlayerList()){
                                if(p.getName().equals(player.getName())){
                                    clubName = p.getClub();
                                    player.setImageName(p.getImageName());
                                    player.setAge(p.getAge());
                                    player.setHeight(p.getHeight());
                                    player.setWeeklySalary(p.getWeeklySalary());
                                    player.setNumber(p.getNumber());
                                }
                            }
                        }
                    }
                    networkUtil.write("Player Edited Successfully");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
