package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class CommitMap implements Serializable {
    private static TreeMap<String, Commit> branchesMap = new TreeMap<>(); // branch name -> most recent commit
    private static String currentBranch = "main";
    private static HashMap<String, String> commitedFiles = new HashMap<>();

    public void setCommitedFiles(String fileName, String fileSha1) {
        commitedFiles.put(fileName, fileSha1);
    }
    public HashMap<String, String> getCommitedFiles() {
        return commitedFiles;
    }
    public static TreeMap<String, Commit> getBranches() {
        return branchesMap;
    }

    public static String getCurrentBranch() {
        return currentBranch;
    }

    public void updateBranche(Commit newCommit) {
        branchesMap.put(currentBranch, newCommit);
    }

    public TreeMap<String, Commit> getBranchesMap(){
        return branchesMap;
    }
    public void setCurrentBranch(String branchName){
        currentBranch = branchName;
    }
}
