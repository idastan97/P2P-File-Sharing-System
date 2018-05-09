/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftserver;

import java.io.Serializable;

/**
 *
 * @author Админ
 */
public class IntegerRef implements Serializable{
    private static final long serialVersionUID = 5950169519310162999L;
    private int val;
    
    public IntegerRef(int val){
        this.val=val;
    }
    
    public int getVal(){
        return val;
    }
    
    public void setVal(int val){
        this.val=val;
    }
    
    public void plus(int p){
        val=val+p;
    }
    
    @Override
    public String toString(){
        return val+"";
    }
}
