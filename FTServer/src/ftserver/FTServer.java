/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Админ
 */
public class FTServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        Tracker tracker = new Tracker();
        
        ServerSocket welcomeSocket = new ServerSocket(8080);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("New user: "+connectionSocket);

            new PeerThread(connectionSocket, tracker).start();
            
        }
        
        
    }
    
}
