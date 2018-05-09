import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientServer extends Thread {
    public static int port;
    private static int stop = 0;
    ServerSocket serverSocket;
    public void run(){
        try {
            serverSocket = new ServerSocket(0);
            this.port = serverSocket.getLocalPort();
            while(ClientServer.stop != 1){
                Socket client = serverSocket.accept();
                System.out.println("accepted" + client.toString());
                ClientThread clientThread = new ClientThread(client);
                clientThread.start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public static void stopCS() {
        stop = 1;
    }

}
