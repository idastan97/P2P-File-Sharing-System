/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author Админ
 */
public class PeerThread extends Thread {
    
    private final Tracker tracker;
    private final Socket connectionSocket;
    private final Peer me;
    private List<FileInfo> myFiles;
    
    public PeerThread(Socket connectionSocket, Tracker tracker){
        this.connectionSocket = connectionSocket;
        this.tracker = tracker;
        this.me = new Peer(connectionSocket.getInetAddress(), 0);
        this.myFiles=null;
    }
    
    @Override
    public void run(){
        try {
            
            String clientSentence;
            
            ObjectOutputStream outputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(connectionSocket.getInputStream());
            
            try {
                clientSentence = (String) inputStream.readObject();
            } catch (ClassNotFoundException ex) {
                System.out.println("error: "+connectionSocket);
                Logger.getLogger(PeerThread.class.getName()).log(Level.SEVERE, null, ex);
                connectionSocket.close();
                return;
            }
            if (!clientSentence.equals("HELLO")){
                System.out.println("not hello: "+connectionSocket);
                connectionSocket.close();
                return;
            }
            System.out.println("connected: "+connectionSocket);
            
            outputStream.reset();
            outputStream.writeObject("HI");
            outputStream.flush();
            
            try {
                this.myFiles = (List<FileInfo>) inputStream.readObject();
            } catch (ClassNotFoundException ex) {
                System.out.println(connectionSocket+": \n"+ex+"\n");
                connectionSocket.close();
                return;
            }
            System.out.println(connectionSocket+" Fyles: "+myFiles+"\n");
            
            if (myFiles.isEmpty()){
                System.out.println(connectionSocket+": no files, not accepted");
                connectionSocket.close();
                return;
            }
            
            tracker.register(me, myFiles);
            System.out.println("registered: "+me);
            
            
            while (true) {
                
                try {
                    clientSentence = (String) inputStream.readObject();
                } catch (ClassNotFoundException ex) {
                    System.out.println(connectionSocket + ": ");
                    Logger.getLogger(PeerThread.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("\n");
                    break;
                }
                
                System.out.println(connectionSocket+" : "+clientSentence);
                
                String[] tokens = clientSentence.split("\\s+", 2);
                
                if (tokens[0].equals("SEARCH:")){
                    
                    Set<FileInfo> searchRes;
                    
                    try{
                        searchRes = tracker.searchFile(tokens[1]);
                    } catch (Exception searchEx){
                        outputStream.reset();
                        outputStream.writeObject(searchEx.getMessage());
                        outputStream.flush();
                        System.out.println(connectionSocket + " Search Res: " +searchEx.getMessage());
                        continue;
                    }

                    if (searchRes.isEmpty()){
                        outputStream.reset();
                        outputStream.writeObject("NOT FOUND");
                        outputStream.flush();
                        System.out.println(connectionSocket + " Search Res: NOT FOUND");
                        continue;
                    }
                    System.out.println(connectionSocket + " Search Res: " +searchRes+"\n");
                    
                    outputStream.reset();
                    outputStream.writeObject("FOUND: ");
                    outputStream.flush();
                    outputStream.reset();
                    outputStream.writeObject(searchRes);
                    outputStream.flush();
                    
                } else if (tokens[0].equals("SCORE")){
                    
                    Pair<String, Integer> scoreUpd;
                    
                    try {
                        scoreUpd = StringHelper.parseScore(tokens[1]);
                    } catch (Exception ex) {
                        Logger.getLogger(PeerThread.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(connectionSocket + ": update error");
                        continue;
                    }
                    
                    tracker.updateScore(scoreUpd);
                        
                } else if (tokens[0].equals("BYE")){
                    break;
                } else if (tokens[0].equals("DOWNLOADED")){
                    FileInfo downloadedFile;
                    try {
                        downloadedFile = (FileInfo) inputStream.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(PeerThread.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println(connectionSocket + ": downloaded error");
                        continue;
                    }
                    downloadedFile.setOwner(me);
                    System.out.println(connectionSocket + " DOWNLOADED: " + downloadedFile);
                    myFiles.add(downloadedFile);
                    tracker.addNewFile(downloadedFile);
                } else {
                    outputStream.reset();
                    outputStream.writeObject("COMMAND NOT FOUND");
                    outputStream.flush();
                    System.out.println(connectionSocket + ": COMMAND NOT FOUND");
                }
                
            }
            
        } catch (IOException ex) {
            System.out.println(connectionSocket+": unexpected close");
            //Logger.getLogger(PeerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("conection closed: "+connectionSocket);
        tracker.deletePeer(myFiles);
        try {
            connectionSocket.close();
        } catch (IOException ex1) {
            Logger.getLogger(PeerThread.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
    
}
