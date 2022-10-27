package gitlet;

// TODO: any imports you need here

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Sirui Li
 */
public class Commit implements Serializable{
    private String mapSha1;
    private String parent0;
    private String parent1;
    private String commit_message;
    private String dateString;
    private static final String MAPS_FOLDER = ".gitlet/filemaps/";
    private static final String OBJECTS_PATH = ".gitlet/objects/";

    public Commit(HashMap<String, String> name_hash, String parent0, String commit_message) {
        this.commit_message = "";
        
        TimeZone time_zone = TimeZone.getTimeZone("PST");
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        simpleDateFormat.setTimeZone(time_zone);
        this.dateString = simpleDateFormat.format(new Date());

        this.parent0 = "";
        this.parent1 = "";
        this.mapSha1 = "";
    }
    public Commit(String commit_message, int code) {
        this.commit_message = commit_message;
        this.parent0 = "";
        this.parent1 = "";
        this.mapSha1 = "";
        setUpDate(code);
    }

    public Commit() {
        this("", 0);
    }

    private void setUpDate(int code) {
        TimeZone time_zone = TimeZone.getTimeZone("PST");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        simpleDateFormat.setTimeZone(time_zone);
        if (code == 9527) dateString = "Wed Dec 31 16:00:00 1969 -0800";
        else dateString = simpleDateFormat.format(new Date());
    }

    public void setMapSha1(String sha1CodeOfMapSha1) {
        mapSha1 = sha1CodeOfMapSha1;
    }

    public void addParent(String parentCode) {
        if (parent0.equals("")) {
            parent0 = parentCode;
        }
        else if (parent1.equals("")) parent1 = parentCode;
        else System.out.println("Only two Parents allowed, for now");
    }

    public String getDateString(){
        return dateString;
    }

    public String getMapSHA1(){
        return mapSha1;
    }

    public HashMap<String, String> getHashMap() {
        if (mapSha1.length() == 0 ) return null;
        String subFolder = mapSha1.substring(0, 2);
        String fileName = mapSha1.substring(2);
        File mapFile = new File(MAPS_FOLDER + subFolder + "/" + fileName);
        HashMap<String, String> fileMap;
        try{
            ObjectInputStream inp = new ObjectInputStream(new FileInputStream(mapFile));
            fileMap = (HashMap) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }

        return fileMap;
    }
    public void checkouSingletFile(String fileName) {
        //updateFileMap();
        HashMap<String, String> files = getHashMap();
        String fileSha1 = files.get(fileName);
        if (fileSha1 == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        String dirName = OBJECTS_PATH + fileSha1.substring(0,2);
        File storedFile = new File(dirName + "/" + fileSha1.substring(2));
        if (!storedFile.exists()){
            System.out.println("file does not exist");
        }
        byte[] contents = Utils.readContents(storedFile);
        File recoveredFile = new File(fileName);
        Utils.writeContents(recoveredFile, contents);
    }

    public void checkoutTo(String folder, String fileName) {
        //updateFileMap();
        HashMap<String, String> filesMap = getHashMap();
        String fileSha1 = filesMap.get(fileName);
        if (fileSha1 == null) {
            //error
//            System.out.println("File does not exist, merging");
            return;
        }
        String dirName = OBJECTS_PATH + fileSha1.substring(0,2);
        File storedFile = new File(dirName + "/" + fileSha1.substring(2));
        if (!storedFile.exists()){
            System.out.println("file does not exist");
        }
        byte[] contents = Utils.readContents(storedFile);
        File recoveredFile = new File(folder + "/" + fileName);
        Utils.writeContents(recoveredFile, contents);
    }

    public void checkoutFileHash(String fileName, String fileSha1){
        String dirName = OBJECTS_PATH + fileSha1.substring(0,2);
        File storedFile = new File(dirName + "/" + fileSha1.substring(2));
        byte[] contents = Utils.readContents(storedFile);
        File recoveredFile = new File(fileName);
        Utils.writeContents(recoveredFile, contents);
    }

    public void checkoutAllFiles(){
        HashMap<String, String> toCheckoutFiles = getHashMap();
        List<String> necessaryFiles = Arrays.asList(new String[]{".gitlet", "gitlet"});
        String[] cwdFiles = new File(System.getProperty("user.dir")).list();
        for (String fileName : cwdFiles){
            if (!necessaryFiles.contains(fileName)){
                new File(fileName).delete();
            }
        }
        toCheckoutFiles.forEach((key, value) -> {
            checkoutFileHash(key, value);
        });
    }
    public String getCommit_message() {
        return commit_message;
    }
    public void setCommit_message(String commit_message) {
        this.commit_message = commit_message;
    }
    public String getParent0(){
        return parent0;
    }

    public String toString() {
        return getCommit_message() + dateString;
    }
}
