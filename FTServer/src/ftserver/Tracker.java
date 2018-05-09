/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author Админ
 */
public class Tracker {
    
    private static Map<String, Set<FileInfo>> sharedFiles;
    private static Map<String, IntegerRef> numOfUploads;
    private static Map<String, IntegerRef> numOfRequests;
    
    public Tracker(){
        sharedFiles = Collections.synchronizedMap(new HashMap());
        numOfUploads = Collections.synchronizedMap(new HashMap());
        numOfRequests = Collections.synchronizedMap(new HashMap());
    }
    
    public synchronized void register(Peer peer, List<FileInfo> filesInfo) {
        
        if (!numOfUploads.containsKey(peer.getIP().getHostAddress() )){
            numOfUploads.put(peer.getIP().getHostAddress(), new IntegerRef(0));
        }
        
        if (!numOfRequests.containsKey(peer.getIP().getHostAddress() )){
            numOfRequests.put(peer.getIP().getHostAddress(), new IntegerRef(0));
        }
        
        peer.setNumOfUploads(numOfUploads.get(peer.getIP().getHostAddress() ));
        peer.setNumOfRequests(numOfRequests.get(peer.getIP().getHostAddress() ));
        
        for (FileInfo fileIt : filesInfo){
            if (peer.getPort()==0){
                peer.setPort(fileIt.getOwner().getPort());
            }
            fileIt.setOwner(peer);
            if (sharedFiles.get(fileIt.getName())==null){
                sharedFiles.put(fileIt.getName(), Collections.synchronizedSet(new HashSet()));
            }
            sharedFiles.get(fileIt.getName()).add(fileIt);
        }
        
        System.out.println("! update Shared Files: "+sharedFiles+"\n");
    }
    
    public void addNewFile(FileInfo newFile){
        if (sharedFiles.get(newFile.getName())==null){
            sharedFiles.put(newFile.getName(), Collections.synchronizedSet(new HashSet()));
        } 
        sharedFiles.get(newFile.getName()).add(newFile);
        System.out.println("! update Shared Files: "+sharedFiles+"\n");
    }
    
    public synchronized void deletePeer(List<FileInfo> peersFiles){
        if (peersFiles == null){
            return;
        }
        for (FileInfo fileIt : peersFiles){
            Set<FileInfo> fileSet = sharedFiles.get(fileIt.getName());
            if (fileSet==null){
                continue;
            }
            fileSet.remove(fileIt);
            if (sharedFiles.get( fileIt.getName() ).isEmpty() ){
                sharedFiles.remove(fileIt.getName());
            }
        }
        System.out.println("! update Shared Files: "+sharedFiles+"\n");
    }
    
    public Set<FileInfo> searchFile(String fileName) throws Exception{
        Set<FileInfo> res = sharedFiles.get(fileName);
        if (res==null){
            throw new Exception("NOT FOUND");
        }
        return res;
    }
    
    public synchronized void updateScore(Pair<String, Integer> scoreUpd) {
        
        IntegerRef req = numOfRequests.get(scoreUpd.getKey());
        IntegerRef upl = numOfUploads.get(scoreUpd.getKey());
        if (req==null || upl==null){
            System.out.println("  !!!!  ERROR: null pointer at updateScore");
        }
        req.plus(1);
        upl.plus(scoreUpd.getValue());
        
    }
    
}
