/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftserver;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;

/**
 *
 * @author Админ
 */
public class Peer implements Serializable{
    private static final long serialVersionUID = 5950169519310162000L;
    private InetAddress ip;
    private int port;
    private IntegerRef numOfUploads;
    private IntegerRef numOfRequests;

    public Peer(InetAddress ip, int port){
        this.ip=ip;
        this.port=port;
        this.numOfUploads=null;
        this.numOfRequests=null;
    }

    public void setNumOfUploads(IntegerRef numOfUploads){
        this.numOfUploads=numOfUploads;
    }

    public void setNumOfRequests(IntegerRef numOfRequests){
        this.numOfRequests=numOfRequests;
    }

    public double getScore(){
        if (numOfRequests==null || numOfUploads==null){
            return -1.0;
        }
        if (numOfRequests.getVal()==0){
            return 0.0;
        }
        return (double)numOfUploads.getVal()/numOfRequests.getVal();
    }

    public void setIP(InetAddress ip){
        this.ip=ip;
    }

    public InetAddress getIP(){
        return ip;
    }

    public int getPort(){
        return port;
    }

    public void setPort(int port){
        this.port=port;
    }

    @Override
    public String toString(){
        return ip+", "+port+", score: "+String.format(Locale.US, "%.2f", this.getScore()*100);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.ip.getHostAddress().hashCode();
        hash = 83 * hash + this.port;
        return hash;
    }

    @Override
    public boolean equals(Object o){
        if (this.getClass()!=o.getClass()){
            return false;
        }
        Peer obj = (Peer) o;
        if (this==obj){
            return true;
        }
        return (ip.getHostAddress().equals(obj.getIP().getHostAddress()) && port==obj.getPort());
    }
}
