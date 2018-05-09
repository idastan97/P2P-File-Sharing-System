/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ftserver.FileInfo;
import ftserver.Peer;
import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Commands {

    private Socket socket;
    private ObjectOutputStream outServer;
    private ObjectInputStream inServer;
    private BufferedReader inFromUser;

    public Commands(Socket socket) throws Exception{
        this.socket = socket;
        this.inFromUser = new BufferedReader(new InputStreamReader(System.in));

        this.outServer = new ObjectOutputStream(this.socket.getOutputStream());
        outServer.flush();
        this.inServer = new ObjectInputStream(this.socket.getInputStream());
    }

    public List<FileInfo> searchFile(String fileName) throws Exception {

        this.sendMessage("SEARCH: "+fileName);
        outServer.flush();
        String serverSentence = (String) inServer.readObject();
        List<FileInfo> res = new ArrayList<>();
        if (serverSentence.equals("FOUND: ")){

            Set<FileInfo> in = (Set<FileInfo>)inServer.readObject();
            res.addAll(in);
            ClientGui.errorLog.setForeground(Color.red);
            ClientGui.errorLog.setText("");
        } else {
            //System.out.println(serverSentence);
            ClientGui.errorLog.setForeground(Color.red);
            ClientGui.errorLog.setText("Server responded: " + serverSentence);
            //ClientGui.errorLog.setForeground(Color.red);
        }
        return  res;

    }

    public void sendScore(Peer peer, int score) throws Exception {
        this.sendMessage("SCORE of " + peer.getIP().getHostAddress() + " : " + score);
    }



    public void download(FileInfo fileInfo) throws Exception {
        Socket socketB = null;

        if(socket.getLocalAddress().getHostAddress().equals(fileInfo.getOwner().getIP().getHostAddress())){
            socketB = new Socket("localhost", fileInfo.getOwner().getPort());
        }else if(fileInfo.getOwner().getIP().getHostAddress().equals("127.0.0.1")){
            socketB = new Socket(socket.getInetAddress().getHostAddress(), fileInfo.getOwner().getPort());
        }else{
            socketB = new Socket(fileInfo.getOwner().getIP().getHostAddress(), fileInfo.getOwner().getPort());
        }



        ObjectOutputStream outToServerB;
        ObjectInputStream inFromServerB;

        outToServerB = new ObjectOutputStream(socketB.getOutputStream());
        outToServerB.flush();

        outToServerB.reset();
        outToServerB.writeObject("DOWNLOAD: ");
        outToServerB.reset();
        outToServerB.writeObject(fileInfo);
        outToServerB.flush();
        inFromServerB = new ObjectInputStream(socketB.getInputStream());

        String response = (String) inFromServerB.readObject();
        if (response.equals("FILE: ")){
            File file = new File(System.getProperty("user.dir") + "/sharedFiles/" +
                    fileInfo.getName() + "." + fileInfo.getType());
            byte[] content = (byte[]) inFromServerB.readObject();
            Files.write(file.toPath(), content);
            Pair<Peer, Integer> peer = new Pair<>(fileInfo.getOwner(), new Integer(1));
            this.sendScore(fileInfo.getOwner(), 1);
            this.sendMessage("DOWNLOADED");
            FileInfo fl = new FileInfo(fileInfo.getName(), fileInfo.getType(), fileInfo.getSize(), fileInfo.getDate(), new Peer(null, ClientServer.port));
            this.outServer.reset();
            this.outServer.writeObject(fl);
            this.outServer.flush();
            ClientGui.errorLog.setForeground(Color.green);
            ClientGui.errorLog.setText("Downloaded");
        } else {
            this.sendScore(fileInfo.getOwner(), 0);
            ClientGui.errorLog.setForeground(Color.red);
            ClientGui.errorLog.setText("Peer responded: " + response);
        }

    }

    public void sendMessage(String message) {
        try {
            outServer.reset();
            outServer.writeObject(message );
            outServer.flush();
        }catch (IOException e) {
            ClientGui.errorLog.setForeground(Color.red);
            ClientGui.errorLog.setText(e.getMessage());
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public BufferedReader getInFromUser() {
        return inFromUser;
    }

    public void setInFromUser(BufferedReader inFromUser) {
        this.inFromUser = inFromUser;
    }

    public ObjectOutputStream getoutServer(){
        return this.outServer;
    }

    public ObjectInputStream getinServer() {
        return inServer;
    }
}