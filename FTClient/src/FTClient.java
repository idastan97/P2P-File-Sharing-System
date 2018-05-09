/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class FTClient {
    static ClientGui clientGui;

    public static void main(String []args) throws Exception{

        System.out.println("Please enter the ip adress of File tracker server:");

        Scanner scanner = new Scanner(System.in);
        String ipAdress = scanner.nextLine();

        ClientServer cS = new ClientServer();
        cS.start();

        Socket socket = null;
        try {
            socket = new Socket(ipAdress, 8080);
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }

        if(socket != null){
            Commands comm = new Commands(socket);

            try {
                comm.getoutServer().reset();
                comm.getoutServer().writeObject("HELLO");
                comm.getoutServer().flush();
                String rec = (String) comm.getinServer().readObject();
                if (rec!=null && !rec.equals("HI")){
                    socket.close();
                    return;
                }
                // System.out.println(FileManager.getAllSharedFiles());
                comm.getoutServer().flush();
                comm.getoutServer().writeObject(FileManager.getAllSharedFiles());
                comm.getoutServer().reset();

                ClientGui clientGui = new ClientGui(comm);
                clientGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }catch (IOException ex) {
                ex.printStackTrace();
            }


        }

    }
}

