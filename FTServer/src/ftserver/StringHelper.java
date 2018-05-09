/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftserver;

import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Админ
 */
public class StringHelper {
    
    public static Pair<String, Integer> parseScore(String scoreText) throws Exception{
        
        String[] tokens = scoreText.split("\\s+");
        
//        for (int i=0; i<tokens.length; i++){
//            System.out.println(tokens[i]);
//        }
        
        if (tokens.length==4  && tokens[0].equals("of") && tokens[2].equals(":")){
            String ip=tokens[1];
            
            int score;
            
            try{
                score = Integer.parseInt(tokens[3]);
            } catch (NumberFormatException e){
                throw new Exception("INCORRECT FORMAT");
            }
            
            //System.out.println(peer);
            //System.out.println(new Pair<>(peer, score));
            return new Pair<>(ip, score);
        }
        throw new Exception("INCORRECT FORMAT");
    }
    
    public static String listFilesToString(List<FileInfo> files){
        String res = "";
        for (FileInfo fileIt : files){
            res = res + fileIt.toString() + " ";
        }
        return res;
    }
    
}
