package server;

import database.Club;
import database.Player;
import util.NetworkUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;

    public static final String PLAYER_FILE_NAME = "players.txt";
    public static final String CLUB_FILE_NAME = "clubs.txt";
    private static List<Player> playerList;
    private static List<Club> clubList;
    private static List<String> countryList;
    private static List<Player> pendingPlayerList;
    private static String adminName;
    private static String adminPassword;

    Server() {
        try {
            serverSocket = new ServerSocket(44444);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Accepted");
                NetworkUtil networkUtil = new NetworkUtil(clientSocket);
                new ServerThread(networkUtil);
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }


    public static void readFromFile() throws Exception {
        playerList = new ArrayList();
        countryList = new ArrayList();
        clubList = new ArrayList();
        pendingPlayerList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(PLAYER_FILE_NAME));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            String[] tokens = line.split(",");
            Player player = new Player();
            player.setName(tokens[0]);
            player.setCountry(tokens[1]);
            player.setAge(Double.parseDouble(tokens[2]));
            player.setHeight(Double.parseDouble(tokens[3]));
            player.setClub(tokens[4]);
            player.setPosition(tokens[5]);
            player.setNumber(Integer.parseInt(tokens[6]));
            player.setWeeklySalary(Double.parseDouble(tokens[7]));
            player.setImageName(tokens[8]);
            playerList.add(player);
            if(!countryList.contains(player.getCountry())){
                countryList.add(player.getCountry());
            }
        }
        br.close();

        br = new BufferedReader(new FileReader(CLUB_FILE_NAME));
        String line = br.readLine();
        String[] auth = line.split(",");
        adminName = auth[0];
        adminPassword = auth[1];
        while (true){
            line = br.readLine();
            if (line == null) break;
            String[] tokens = line.split(",");
            String clubName = tokens[0];
            String clubPassword = tokens[1];
            double clubAmount = Double.parseDouble(tokens[2]);
            int count = Integer.parseInt(tokens[3]);
            List<String> pName = new ArrayList<>();
            for(int i=1; i<=count;i++){
                line = br.readLine();
                String[] str = line.split(",");
                pName.add(str[0]);
                for (Player player: playerList){
                    if(player.getName().equals(str[0])){
                        player.setAmount(Double.parseDouble(str[1]));
                        player.setInPending(true);
                        pendingPlayerList.add(player);
                    }
                }
            }
            List<Player> clubPlayer = new ArrayList<>();
            for (Player player: playerList){
                if(player.getClub().equals(clubName)){
                    clubPlayer.add(player);
                }
            }
            Club club = new Club(clubName,clubPassword,clubAmount,clubPlayer,pName);
            clubList.add(club);
        }
        br.close();
    }

    public static void writeToFile() throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(PLAYER_FILE_NAME));
        for (Player player : playerList) {
            bw.write(player.getName() + "," + player.getCountry() + "," + player.getAge() + "," + player.getHeight()
                    + "," + player.getClub()+ "," + player.getPosition()+ "," + player.getNumber()+ "," + player.getWeeklySalary());
            bw.write(","+player.getImageName()+"\n");
        }
        bw.close();
        bw = new BufferedWriter(new FileWriter(CLUB_FILE_NAME));
        bw.write(adminName+","+adminPassword+"\n");
        for(Club club: clubList){
            bw.write(club.getName()+","+club.getPassword()+","+club.getBalance()+","+club.pendingPlayerCount()+"\n");
            for(Player player: club.getPendingList()){
                bw.write(player.getName()+","+player.getAmount()+"\n");
            }
        }
        bw.close();
    }

    public static void exit(List<Player> playerList,List<Club> clubList){
        Server.playerList = playerList;
        Server.clubList = clubList;
        /*try {
            writeToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.exit(1);
    }

    public static void main(String args[]) {
        try {
            readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServerThread.load(playerList,clubList,countryList,pendingPlayerList,adminName,adminPassword);
        Server server = new Server();
    }
}
