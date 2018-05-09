import ftserver.FileInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class ClientThread extends Thread {

    private Socket socket;
    private ObjectInputStream inClient;
    private ObjectOutputStream outClient;



    public ClientThread(Socket socket) throws Exception{
        this.socket = socket;
        this.outClient = new ObjectOutputStream(this.socket.getOutputStream());
        this.outClient.flush();
        this.inClient = new ObjectInputStream(this.socket.getInputStream());
    }
    public void run() {
        try {

            String req = (String)this.inClient.readObject();
            if((req).equals("DOWNLOAD: ")) {
                FileInfo fileInfo = (FileInfo) this.inClient.readObject();
                int randomNum = ThreadLocalRandom.current().nextInt(1, 101);
                if(randomNum < 50) {
                    this.outClient.reset(); this.outClient.writeObject("NO");
                }else {
                    this.outClient.reset(); this.outClient.writeObject("FILE: ");
                    byte[] sendData = FileManager.getFileByFileInfo(fileInfo);
                    this.outClient.reset(); this.outClient.writeObject(sendData);
                }
                this.outClient.flush();
            }else {
                this.outClient.reset(); this.outClient.writeObject("Something bad happened");
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }
}
