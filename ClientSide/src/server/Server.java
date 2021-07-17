package server;

import database.Club;
import database.Country;
import database.Player;
import util.NetworkUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;

    public static final String PLAYER_FILE_NAME = "players.txt";
    public static final String CLUB_FILE_NAME = "clubs.txt";
    private static List<Player> playerList;
    private static List<Club> clubList;
    private static List<Country> countryList;
    private static HashMap<Player,Double> sellPendingPlayers;

    Server() {
        try {
            serverSocket = new ServerSocket(33333);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client Accepted");
                NetworkUtil networkUtil = new NetworkUtil(clientSocket);
                new ServerThread(networkUtil,playerList,clubList);
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public static void readFromFile() throws Exception {
        playerList = new ArrayList();
        countryList = new ArrayList();
        clubList = new ArrayList();
        sellPendingPlayers = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(PLAYER_FILE_NAME));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            String[] tokens = line.split(",");
            Player player = new Player();
            player.setName(tokens[0]);
            player.setCountry(tokens[1]);
            player.setAge(Integer.parseInt(tokens[2]));
            player.setHeight(Double.parseDouble(tokens[3]));
            player.setClub(tokens[4]);
            player.setPosition(tokens[5]);
            player.setNumber(Integer.parseInt(tokens[6]));
            player.setWeeklySalary(Double.parseDouble(tokens[7]));
            player.setImageName(tokens[8]);
            playerList.add(player);

            boolean clubAlreadyExist = false;
            for(Club club: clubList){
                if(club.getName().equalsIgnoreCase(tokens[4])) {
                    clubAlreadyExist = true;
                    club.addPlayer(player);
                    break;
                }
            }
            if(!clubAlreadyExist){
                Club club = new Club(tokens[4],player);
                clubList.add(club);
            }

            boolean countryAlreadyExist = false;
            for(Country country: countryList){
                if(country.getName().equalsIgnoreCase(tokens[1])) {
                    countryAlreadyExist = true;
                    country.addPlayer(player);
                    break;
                }
            }
            if(!countryAlreadyExist){
                Country country = new Country(tokens[1],player);
                countryList.add(country);
            }
        }
        br.close();

        br = new BufferedReader(new FileReader(CLUB_FILE_NAME));
        while (true){
            String line = br.readLine();
            if (line == null) break;
            String[] tokens = line.split(",");
            for(Club club: clubList){
                if(club.getName().equalsIgnoreCase(tokens[0])){
                    club.changePassword(tokens[1]);
                    club.setBalance(Double.parseDouble(tokens[2]));
                    int count = Integer.parseInt(tokens[3]);
                    for(int i=1;i<=count;i++){
                        line = br.readLine();
                        if (line == null) break;
                        tokens = line.split(",");
                        for (Player player: playerList){
                            if(player.getName().equalsIgnoreCase(tokens[0])){
                                sellPendingPlayers.put(player,Double.parseDouble(tokens[1]));
                                club.playerSellRequest(player,Double.parseDouble(tokens[1]));
                                break;
                            }
                        }
                    }
                    break;
                }
            }
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
        for(Club club: clubList){
            bw.write(club.getName()+","+club.getPassword()+","+club.getBalance()+","+club.pendingCount()+"\n");
            for(Player player: club.getSellPendingPlayers().keySet()){
                bw.write(player.getName()+","+club.getPlayerSellAmount(player)+"\n");
            }
        }
        bw.close();
    }

    public static void main(String args[]) {
        try {
            readFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server server = new Server();
    }
}
