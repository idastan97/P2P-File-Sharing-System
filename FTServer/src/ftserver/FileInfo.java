/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftserver;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Админ
 */
public class FileInfo implements Serializable{
    
    private static final long serialVersionUID = 5950169519310163000L;
    String name;
    String type;
    long size;
    Date date;
    Peer owner;
    
    public FileInfo(String name, String type, long size, Date date, Peer owner){
        this.name = name;
        this.type = type;
        this.size = size;
        this.date = date;
        this.owner = owner;
    }
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
    
    public void setOwner(Peer owner){
        this.owner = owner;
    }
    
    public long getSize(){
        return size;
    }
    
    public Peer getOwner(){
        return owner;
    }
    
    public Date getDate(){
        return date;
    }
    
    public String toForm(){
        return name+", "+type+", "+size;
    }
    
    @Override
    public String toString(){
        return name+", "+type+", "+size+", "+date+", "+owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()){
            return false;
        }
        FileInfo obj = (FileInfo) o;
        if (obj==this){
            return true;
        }
        return (obj.getName().equals(this.name) && obj.getType().equals(this.type) && obj.getSize()==this.size && obj.getDate().equals(this.date) && obj.getOwner().equals(this.owner));
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.name.hashCode();
        hash = 97 * hash + this.type.hashCode();
        hash = 97 * hash + (int) (this.size ^ (this.size >>> 32));
        hash = 97 * hash + this.date.hashCode();
        hash = 97 * hash + this.owner.hashCode();
        return hash;
    }
}
