package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Stage implements Serializable {
    public static HashMap<String, String> addingMap; // adding staging area, if the file is different during commit, refuse to commit
    public static HashMap<String, String> deletingMap; // deleting staging area
    private static String ADDING_STAGE_PATH = ".gitlet/stageArea/addingStage/";
    private static String DELETING_STAGE_PATH = ".gitlet/stageArea/deletingStage/";

    public static boolean addToAddingArea(String fileName) { // pay attention on the delete and add conflict
        //TODO: finish the output
        File f = new File(fileName);
        byte[] contentByte = Utils.readContents(f);
        String contentSha1 = Utils.sha1(contentByte);
        if (addingMap == null){
            addingMap = new HashMap<>();
        }
        addingMap.put(fileName, contentSha1);
        File stageFile = new File(ADDING_STAGE_PATH + fileName);
        if (!stageFile.exists()){
            Utils.writeContents(stageFile, contentByte);
        }
        return true;
    }

    public static boolean addToDeletingArea(String fileName) { // pay attention on the delete and add conflict
        //transfer file content to SHA-1
        File f = new File(fileName);
        byte[] contentByte = Utils.readContents(f);
        String contentSha1 = Utils.sha1(contentByte);
        if (deletingMap == null){
            deletingMap = new HashMap<>();
        }
        //check if this file is in the adding area
        File toRemove = new File(ADDING_STAGE_PATH + fileName);
        if (toRemove.exists()){
            toRemove.delete();

            return true;
        }
        //stage it for remove
        File stageFile = new File(DELETING_STAGE_PATH + fileName);
        if (!stageFile.exists()){
            Utils.writeContents(stageFile, contentByte);
        }
        if (f.exists()){
            f.delete();
        }
        return true;
    }
}
