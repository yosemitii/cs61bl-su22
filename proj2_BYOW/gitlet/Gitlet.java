package gitlet;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Sirui Li
 *
 * This class is an encapsulation for all the functions.
 */

public class Gitlet {
    private static final String GITLET_PATH = ".gitlet";
    private static final String OBJECT_PATH = GITLET_PATH + "/objects";
    private static final String STAGEAREA_PATH = GITLET_PATH + "/stageArea";
    private static final String STAGEFILE_PATH = STAGEAREA_PATH + "/stageFile";
    private static final String STAGEADD_PATH = STAGEAREA_PATH + "/addingStage";
    private static final String STAGEDLETE_PATH = STAGEAREA_PATH + "/deletingStage";
    private static final String FILEMAPS_PATH_FOLDER = ".gitlet/filemaps";
    private static final String COMMITS_PATH_FOLDER = ".gitlet/commits";
    private static final String COMMITS_FOLDER = ".gitlet/commits/";
    private static final String COMMITMAP_FILE_Path = ".gitlet/commitMap";
    private static final String BRANCH_INFO_PATH = ".gitlet/BRANCHINFO";

    private static final String SPLIT_POINTS = ".gitlet/splitPoints";
    private static final File HEAD_STR = new File(".gitlet/HEAD_STR");
    private static File commitedFiles_File = new File(GITLET_PATH + "/" + "committedFiles");
    private static HashMap<String, String> fileMaps = new HashMap<>(); //file's name -> file's sha1code
    private static File stageFile;
    public static Stage stagingArea;
//    public static String currBranch;

//    private static TreeMap<String, String> branchMap;

    private static final String BRANCH_MAP = ".gitlet/branchMap";


    public static void testWritingFile(String key, String value) {
        File commitMapFile = new File(COMMITMAP_FILE_Path);
        if (!commitMapFile.exists()) {
            Utils.writeContents(commitMapFile, "");
        }
        CommitMap commitMap = readCommitMap(commitMapFile);
        commitMap.setCommitedFiles(key, value);
        Utils.writeObject(commitMapFile, commitMap);
        for (Map.Entry<String, String> each : commitMap.getCommitedFiles().entrySet()) {
            System.out.println("all the content");
            System.out.println("key : " + each.getKey() + "value: " + each.getValue());
        }
    }

    /**
     * Initialization all the classes:
     * create the .gitlet folder,
     * initialize a "commits" folder to store the commits,
     * initialize an empty stage area folder with adding and deleting folders
     */
    public static void init() {
        File gitlet = new File(GITLET_PATH);
        if (!gitlet.exists()) {
            gitlet.mkdir(); // After This, a ".gitlet" folder exists

            /*
            Create a folder called commits with two sha1 code as start and rest of sha1 as file with serialized Commit object
             */
            new File(COMMITS_PATH_FOLDER).mkdir();

            /*
            Created an empty staging area as a folder, where adding stage area (a folder),
            a deleting stage area (a folder) are stored, and a file called staging content
            with the staging information are stored
             */
            Stage stage = new Stage();
            new File(STAGEAREA_PATH).mkdir();
            stageFile = new File(STAGEFILE_PATH);
            stageFile = writeStage(stage);

            /*
            create adding stage and deleting stage area
             */
            new File(STAGEADD_PATH).mkdir();
            new File(STAGEDLETE_PATH).mkdir();


            /*
            Create an empty object folder for storing the copies of the files
             */
            new File(OBJECT_PATH).mkdir();

            /*
            Create a new map for branch
             */
            File branchMapFile = new File(BRANCH_MAP);
            Utils.writeObject(branchMapFile, new TreeMap<String, String>());

            File splitPointsFile = new File(SPLIT_POINTS);
            Utils.writeObject(splitPointsFile, new HashMap<String, String>());

            /*
            A file maps folder that storing the files for one commit
             */
            new File(FILEMAPS_PATH_FOLDER).mkdir();
            Utils.writeContents(HEAD_STR, "");
            commit("initial commit", 9527);

            /*
            Create a new commitMap file
             */
            File commitMapFile = new File(COMMITMAP_FILE_Path);
            CommitMap commitMap = new CommitMap();
            Utils.writeObject(commitMapFile, commitMap);



            /*
            Create a new fileMap for status
             */
            //Utils.writeContents(commitedFiles_File, Utils.serialize(new HashMap<String, String>())); //initialize a hashMap
        }
        else System.err.println("A Gitlet version-control system already exists in the current directory.");
    }

    /**
     * Add a file into the stating adding area
     * @param fileName to be added to the adding area
     */
    public static void add(String fileName) {
        File f = new File(fileName);
        if (!f.exists()){
            System.err.println("File does not exist.");
            return;
        }
        //checking deleting area: if it exist in the deleting area, then add it back (but not in the adding area)
        File deleted = new File(STAGEDLETE_PATH + "/" + fileName);
        if (deleted.exists()){
            deleted.delete();
            return;
        }
        Commit head = getHead();
        String fileSHA1 = Utils.sha1(Utils.readContents(f));

        if (head.getHashMap().containsKey(fileName)){
            if (head.getHashMap().get(fileName).equals(fileSHA1)){
            return;
            }
        }

        stagingArea.addToAddingArea(fileName);
    }

    public static void remove(String fileName) {
        File toRemove = new File(STAGEADD_PATH + "/" + fileName);
        Commit head = getHead();
        if ((!toRemove.exists()) && (!head.getHashMap().containsKey(fileName))){
            System.err.println("No reason to remove the file.");
            return;
        }
        File origin = new File(fileName);
        if (!origin.exists()){
            checkout_fileName(fileName);
        }
        stagingArea.addToDeletingArea(fileName);
    }

    /**
     * Set up a new Commit object
     * @param commit_Message input by user
     * @param code For now, the special case spcific for the first commit
     */
    public static void commit(String commit_Message, int code) {
        //if nothing to commit
        List<String> addingFiles = Utils.plainFilenamesIn(STAGEADD_PATH + "/");
        if (addingFiles.isEmpty() && code == 0) {
            List<String> deletingFiles = Utils.plainFilenamesIn(STAGEDLETE_PATH + "/");
            if (deletingFiles.isEmpty()) {
                System.err.println("No changes added to the commit.");
                return;
            }
        }
        Commit commit = new Commit(commit_Message, code);

        if (code == 0){
            fileMaps = getHead().getHashMap();
        }
//        System.out.println("load last filemap:" + fileMaps);

        //add the files into the HASH_MAPS (object), clean staging area, add the file into object folder
        addCommitIntoFolder_addStage();

        //Set up the hashMap for deleting files
        setUpFileHash_deleting();

        //write HASH_MAPS and set the sha1code of the map into the commit
        commit.setMapSha1(saveHashMap(fileMaps));

        //further setting up this commit: set message, set parent, point HEAD
        commit.setCommit_message(commit_Message);

        setUpParent(commit);

        //writeCommit(commit, HEAD);
        updateHead(commit);
//        if (getHead() != null) headInfo();

        storeCommit(commit);

        //clean the map
        fileMaps = new HashMap<>();



        TreeMap<String, String> branchMap = readBranchMap();

        if (code == 9527) {
            branchMap.put("main", getHeadSHA1());
            File currBranch_file = new File(BRANCH_INFO_PATH);
            Utils.writeContents(currBranch_file, "main");
        } else {
            branchMap.put(getCurrentBranch(), getHeadSHA1());
        }
        writeBranchMap(branchMap);
    }

    /**
     * Print out all the commit according to the time order
     */
    public static void log() {
        Commit head = getHead();
        logHelper(head, getHeadSHA1());
    }

    /**
     * Override a certain file with most recent commit in the working directory
     * @param fileName to be overridden
     */
    public static void checkout_fileName(String fileName) {
        Commit currCmt = getHead();
        currCmt.checkouSingletFile(fileName);
    }

    /**
     * Override a certain file with most recent commit in the working directory
     * @param commitID the version wanted to check out
     * @param fileName the file's name
     */
    public static void checkout_commentID(String commitID, String fileName) {
        File folder_check = new File(COMMITS_PATH_FOLDER + "/" + commitID.substring(0, 2));
        if (!folder_check.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        String[] filesInFolder = folder_check.list();
        if (filesInFolder.length == 0){
            System.out.println("No commit with that id exists.");
            return;
        }
        boolean isExist = false;
        for (String fileNameInFolder: filesInFolder){
            if (fileNameInFolder.substring(0, 4).equals(commitID.substring(2, 6))){
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit currCmt = getCommit(commitID);
        currCmt.checkouSingletFile(fileName);
        updateHead(currCmt);
    }
    public static void headInfo(){
        Commit head = getHead();
        System.out.println(head.toString());

//        TreeMap<String, String> branchMap = readBranchMap();
        System.out.println("current branch:" + getCurrentBranch());

        HashMap<String, String> hmp = head.getHashMap();
        System.out.printf(hmp.toString());
    }
    public static void global_log() {
        File commitFolder = new File(COMMITS_FOLDER);
        for (File file : commitFolder.listFiles()) {
            File subFolder = new File(COMMITS_FOLDER + file.getName() + "/");
            for (File each : subFolder.listFiles()) {
                String sha1 = file.getName() + each.getName();
                Commit commit = getCommit(sha1);
                System.out.println("===");
                System.out.println("commit " + sha1);
                System.out.println("Date: " + commit.getDateString());
                System.out.println(commit.getCommit_message());
                System.out.println();
            }
        }
    }
    public static void find(String commitMessage) {
        if (!find_helper(commitMessage)) {
            System.err.println("Found no commit with that message.");
        }
    }


    public static void status() {

        TreeSet<String> removed = scanRemoved();
        TreeSet<String> staged = scanStaged();
        TreeMap<String, String> modified = scanModified();
        TreeSet<String> untracked = scanUntracked();
        TreeMap<String, String> branches = readBranchMap();


        printFormate(branches, staged, removed, modified, untracked);
    }
    //Do not test this method only if you check all the necessary files in Commit.checkoutAll()
    public static void checkout_branch(String branchName, boolean isMerge) {
        TreeMap<String, String> branchMap = readBranchMap();

        //check before checkout
        if (!branchMap.containsKey(branchName)){
            System.out.println("No such branch exists.");
            return;
        }
        if (branchName.equals(getCurrentBranch()) && !isMerge){
            HashMap<String, String> splitPoints = readSplitPoints();
            //branch point still points to split point
            if (!branchMap.get(branchName).equals(splitPoints.get(branchName))){
                System.out.println("No need to checkout the current branch.");
                return;
            }
        }
        TreeSet<String> untrackedFiles = scanUntracked();
        if (!untrackedFiles.isEmpty() && !isMerge){
//            System.out.println("untrackedFiles:" + untrackedFiles);
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }
        TreeMap<String, String> modifiedFiles = scanCurrModified();
        if (!modifiedFiles.isEmpty() && !isMerge){
//            System.out.println("modifiedFiles:" + modifiedFiles);
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }
        TreeSet<String> trackedNotInHead = trackedButNotInHead();
        if (!trackedNotInHead.isEmpty() && !isMerge){
//            System.out.println("trackedNotInHead:" + trackedNotInHead);
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }
        Commit targetCommit = getCommit(branchMap.get(branchName));
        targetCommit.checkoutAllFiles();
        setCurrentBranch(branchName);
        updateHead(targetCommit);
    }

    /* create new branch
     */
    public static void branch(String branchName) {
        //read branch map
        if (!new File(BRANCH_MAP).exists()){
            System.err.println("Branch map does not exist");
            return;
        }
        TreeMap<String, String> branchMap = readBranchMap();
        //branch already exists
        if (branchMap.containsKey(branchName)){
            System.out.println("A branch with that name already exists.");
            return;
        }
        else{
            //setup branch
            branchMap.put(branchName, getHeadSHA1());
            branchMap.put(getCurrentBranch(), getHeadSHA1());
            writeBranchMap(branchMap);
//            setCurrentBranch("temp");
        }
        //add split points
        HashMap<String, String> splitPoints = readSplitPoints();
        splitPoints.put(branchName, getHeadSHA1());
        writeSplitPoints(splitPoints);


    }
    public static void removeBranch(String branchName) {
        TreeMap<String, String> branchMap = readBranchMap();
        if (!branchMap.containsKey(branchName)){
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (getCurrentBranch().equals(branchName)){
            System.out.println("Cannot remove the current branch.");
            return;
        }
        branchMap.remove(branchName);
        writeBranchMap(branchMap);
//        setCurrentBranch(branchName);
    }
    public static void reset(String commitID) {
        TreeSet<String> untrackedFiles = scanUntracked();
        if (!untrackedFiles.isEmpty()){
//            System.out.println("untrackedFiles:" + untrackedFiles);
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }
        TreeMap<String, String> modifiedFiles = scanCurrModified();
        if (!modifiedFiles.isEmpty()){
//            System.out.println("modifiedFiles:" + modifiedFiles);
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }
        TreeSet<String> trackedNotInHead = trackedButNotInHead();
        if (!trackedNotInHead.isEmpty()){
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return;
        }
        //incase sha1 is a short ID
        File folder_check = new File(COMMITS_PATH_FOLDER + "/" + commitID.substring(0, 2));
        if (!folder_check.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        String[] filesInFolder = folder_check.list();
        if (filesInFolder.length == 0){
            System.out.println("No commit with that id exists.");
            return;
        }
        boolean isExist = false;
        for (String fileNameInFolder: filesInFolder){
            if (fileNameInFolder.substring(0, 4).equals(commitID.substring(2, 6))){
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            System.out.println("No commit with that id exists.");
            return;
        }
        //find commit
        Commit currCmt = getCommit(commitID);
        HashMap<String, String> fileMap = readHashMap(currCmt.getMapSHA1());
        //Removes tracked files that are not present in that commit.
        List<String> files = Utils.plainFilenamesIn(System.getProperty("user.dir"));
        for (String fileName : files) {
            if (fileMap.containsKey(fileName)){
                File fileInCWD = new File(fileName);
                if (fileMap.get(fileName).equals(Utils.sha1(Utils.readContents(fileInCWD)))){
                    fileInCWD.delete();
                }
            }
        }
        //claer staging area
        File addingFolder = new File(STAGEADD_PATH);
        for (File addingFile : addingFolder.listFiles()){
            addingFile.delete();
        }
        File deletingFolder = new File(STAGEDLETE_PATH);
        for (File deletingFile : deletingFolder.listFiles()){
            deletingFile.delete();
        }
        //chekout each file
        for (Map.Entry<String, String> eachFile : fileMap.entrySet()) {
            checkout_commentID(commitID, eachFile.getKey());
        }
        //update current branch
        String branch = getCurrentBranch();
        TreeMap<String, String> branchMap = readBranchMap();
        branchMap.put(branch, commitID);
        updateHead(currCmt);

//        Commit wantedCommit = getCommit(sha1);
//        HashMap<String, String> fileMap = readHashMap(wantedCommit.getMapSHA1());
//        for (Map.Entry<String, String> eachFile : fileMap.entrySet()) {
//            checkout_commentID(sha1, eachFile.getKey());
//        }
//        updateHead(wantedCommit);
    }

    public static void merge(String givenBranch) {
        // failure cases
//        System.out.println("merge start");
        if (failedMergeTest(givenBranch)) {
            return;
        }

        //TODO: get the most recent ancestor
        TreeMap<String, String> branchMaps = readBranchMap();
        Commit given = getCommit(branchMaps.get(givenBranch));
        Commit curr = getHead();
        Commit ancestor = getCommonAncestor(given, curr);
        if (ancestor == null) {
            System.out.println("ancestor not found");
            return;
        }


        HashMap<String, String> filesToAdd = new HashMap<>();
        HashMap<String, String> filesToDelete = new HashMap<>();
        HashMap<String, String> givenFiles = readHashMap(given.getMapSHA1());
        HashMap<String, String> currFiles = readHashMap(curr.getMapSHA1());
        HashMap<String, String> ancestorFiles = readHashMap(ancestor.getMapSHA1());
        ArrayList<Map.Entry<String, String>> totalFiles = new ArrayList<>();
        //null error in AG
        if (!(givenFiles.entrySet() == null)) {
            for (Map.Entry<String, String> each : givenFiles.entrySet()) {
                totalFiles.add(each);
            }
        }
        if (!(givenFiles.entrySet() == null)) {
            for (Map.Entry<String, String> each : currFiles.entrySet()) {
                totalFiles.add(each);
            }
        }
        if (!(givenFiles.entrySet() == null)) {
            for (Map.Entry<String, String> each : ancestorFiles.entrySet()) {
                totalFiles.add(each);
            }
        }
        /*
            Three loops:
                //loop CWD:
                loop given:
                loop curr:
                loop ancestor?
         */
        //Loop given
        HashSet<String> alreadyMerged = new HashSet<>();
        for (Map.Entry<String, String> each : totalFiles) {
            boolean modifiedInGiven = givenFiles.containsKey(each.getKey())
                    && (ancestorFiles.containsKey(each.getKey())
                    && !ancestorFiles.get(each.getKey()).equals(givenFiles.get(each.getKey())))
                    || !ancestorFiles.containsKey(each.getKey()),
                    modifiedInCurrent = currFiles.containsKey(each.getKey())
                            && (ancestorFiles.containsKey(each.getKey())
                            && !ancestorFiles.get(each.getKey()).equals(currFiles.get(each.getKey())))
                            || !ancestorFiles.containsKey(each.getKey()),
                    presentAtSplit = ancestorFiles.containsKey(each.getKey()),
                    presentAtGiven = givenFiles.containsKey(each.getKey()),
                    presentAtCurrent = currFiles.containsKey(each.getKey());

//            System.out.println("checking file:" + each.getKey());
//            System.out.println("modifiedInGiven:" + modifiedInGiven);
//            System.out.println("modifiedInCurrent:" + modifiedInCurrent);
//            System.out.println("presentAtSplit:" + presentAtSplit);
//            System.out.println("presentAtGiven:" + presentAtGiven);
//            System.out.println("presentAtCurrent:" + presentAtCurrent);

            if (modifiedInGiven && !modifiedInCurrent && presentAtCurrent){
                given.checkoutTo(STAGEADD_PATH, each.getKey());
//                System.out.println("modifiedInGiven && !modifiedInCurrent && presentAtCurrent");
            }
            else if (!presentAtSplit && !presentAtCurrent && presentAtGiven){
                given.checkoutTo(STAGEADD_PATH, each.getKey());
//                System.out.println("!presentAtSplit && !presentAtCurrent && presentAtGive");
            }
            else if (presentAtSplit && !modifiedInCurrent && !presentAtGiven) {
                curr.checkoutTo(STAGEDLETE_PATH, each.getKey());
//                System.out.println("presentAtSplit && !modifiedInCurrent && !presentAtGiven");
            }
            else if (editedOrDeletedOrAdded(ancestor, curr, each.getKey())
                    && editedOrDeletedOrAdded(ancestor, given, each.getKey())){
                //conflict??
                if (alreadyMerged.contains(each.getKey())) break;
                System.out.println("Encountered a merge conflict.");
                //writing conflict file
                String contentInCurr= "";
                if (curr.getHashMap().containsKey(each.getKey())) {
                    String fileInCurrSHA = curr.getHashMap().get(each.getKey());
                    contentInCurr= Utils.readContentsAsString(new File(OBJECT_PATH + "/" + convertTo_2_38(fileInCurrSHA)));
                }

                String contentInGiven= "";
                if (given.getHashMap().containsKey(each.getKey())) {
                    String fileInGivenSHA = given.getHashMap().get(each.getKey());
                    contentInGiven= Utils.readContentsAsString(new File(OBJECT_PATH + "/" + convertTo_2_38(fileInGivenSHA)));
                }
                String newContent = "<<<<<<< HEAD\n" + contentInCurr + "=======\n" +contentInGiven + ">>>>>>>\n";
                given.checkoutTo(STAGEADD_PATH, each.getKey());
//                String newContent = "";
//                if (curr.getHashMap().containsKey(each.getKey()) && given.getHashMap().containsKey(each.getKey())){
//                    String fileInCurrSHA = curr.getHashMap().get(each.getKey());
//                    String fileInGivenSHA = given.getHashMap().get(each.getKey());
//                    String contentInCurr= Utils.readContentsAsString(new File(OBJECT_PATH + "/" + convertTo_2_38(fileInCurrSHA)));
//                    String contentInGiven= Utils.readContentsAsString(new File(OBJECT_PATH + "/" + convertTo_2_38(fileInGivenSHA)));
//                    newContent = "<<<<<<< HEAD\n" + contentInCurr + "=======\n" +contentInGiven + ">>>>>>>";
//                }
//                else if (curr.getHashMap().containsKey(each.getKey()) && !given.getHashMap().containsKey(each.getKey())){
//                    String fileInCurrSHA = curr.getHashMap().get(each.getKey());
//                    String contentInCurr= Utils.readContentsAsString(new File(OBJECT_PATH + "/" + convertTo_2_38(fileInCurrSHA)));
//                    newContent = "<<<<<<< HEAD\n" + contentInCurr + "=======\n" + ">>>>>>>";
//                }
//                else if (!curr.getHashMap().containsKey(each.getKey()) && given.getHashMap().containsKey(each.getKey())){
//                    String fileInGivenSHA = given.getHashMap().get(each.getKey());
//                    String contentInGiven= Utils.readContentsAsString(new File(OBJECT_PATH + "/" + convertTo_2_38(fileInGivenSHA)));
//                    newContent = "<<<<<<< HEAD\n" + "=======\n" + contentInGiven + ">>>>>>>";
//                }
//                else if (!curr.getHashMap().containsKey(each.getKey()) && !given.getHashMap().containsKey(each.getKey())){
//
//                    newContent = "<<<<<<< HEAD\n" + "=======\n" + ">>>>>>>";
//                }
                alreadyMerged.add(each.getKey());
                Utils.writeContents(new File(STAGEADD_PATH + "/" + each.getKey()), newContent);
//                System.out.println(newContent);

            }
            /*
            add(eachGiven
             */
        }
//        System.out.println("testing before commit");
        commit("Merged " + givenBranch + " into "+ getCurrentBranch() + ".", 0);
        checkout_branch(getCurrentBranch(), true);
    }


    private static boolean editedOrDeletedOrAdded(Commit splitPoint, Commit comparedCommit, String fileName){
        //added
        if (!splitPoint.getHashMap().containsKey(fileName)){
            if (comparedCommit.getHashMap().containsKey(fileName)) {return true;}
        }
        //deleted
        if (splitPoint.getHashMap().containsKey(fileName)){
            if (!comparedCommit.getHashMap().containsKey(fileName)){return true;}
        }
        //edited
        if (splitPoint.getHashMap().containsKey(fileName)){
            if (comparedCommit.getHashMap().containsKey(fileName)) {
                if (!splitPoint.getHashMap().get(fileName).equals(comparedCommit.getHashMap().get(fileName))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Helper function to write a stage in to a file and return
     * @param toBeWriten the stage object
     * @return the file writen with stage object
     */
    private static File writeStage(Stage toBeWriten){
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(stageFile));
            out.writeObject(toBeWriten);
            out.close();
        } catch (IOException excp) {
            System.err.println("writeStage is not working");
        }
        return stageFile;
    }

    private static File writeBranchMap(TreeMap<String, String> toBeWriten){
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new FileOutputStream(BRANCH_MAP));
            out.writeObject(toBeWriten);
            out.close();
        } catch (IOException excp) {
            System.err.println("writeStage is not working");
        }
        return stageFile;
    }

    private static TreeMap<String, String> readBranchMap() {
        File toBeRead = new File(BRANCH_MAP);
        TreeMap<String, String> result = null;
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(toBeRead));
            result = (TreeMap<String, String>) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.err.println("readBranchMap is not working");
        }
        return result;
    }

    /**
     * A helper function that convert previously stored file into a CommitMap object
     * @param toBeRead the file wanted to convert to the Commit map
     * @return the converted commit map
     */
    private static Stage readStage(File toBeRead) {
        Stage result = null;
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(toBeRead));
            result = (Stage) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.err.println("readStage is not working");
        }
        return result;
    }

    /**
     * Write a commit into file.
     * @param toBeWriten the commit to be written
     * @param file that store the commit
     * @return the file
     */
    private static File writeCommit(Commit toBeWriten, File file) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(toBeWriten);
            out.close();
        } catch (IOException excp) {
            System.err.println("writeCommit is not working");
        }
        return file;
    }
    private static Commit readCommit(File toBeRead) {
        Commit result = null;
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(toBeRead));
            result = (Commit) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.err.println("readCommit is not working");
        }

        return result;
    }
    private static HashMap<String, String> readCommitFiles(File toBeRead) {
        HashMap<String, String> result = new HashMap<>();
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(toBeRead));
            result = (HashMap<String, String>) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.err.println("readCommitFiles is not working");
        }

        return result;
    }

    private static CommitMap readCommitMap(File toBeRead) {
        CommitMap result = null;
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(toBeRead));
            result = (CommitMap) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.err.println("readCommitMap is not working");
            //TODO: clarify this print
        }

        return result;
    }

    /** Save the name-to-hash map and then return its SHA-1 value as a string
     *
     */
    private static String saveHashMap(HashMap<String, String> map){
        ObjectOutputStream out;
        byte[] mapByte = Utils.serialize(map);
        String mapSha1 = Utils.sha1(mapByte);
        String dir = FILEMAPS_PATH_FOLDER + "/" + mapSha1.substring(0, 2);
        String savedFileName = mapSha1.substring(2);
        File savedFile = new File(dir + "/" + savedFileName);
        File savedDir = new File(dir);
        if (!savedDir.exists()){
            savedDir.mkdir();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream(savedFile));
            out.writeObject(map);
            out.close();
        } catch (IOException excp) {
            System.err.println("Saving name-to-hash map is not working");
        }
        return mapSha1;
    }

    /**
     * Write this commit into the file.
     * @param commit the commit to be stored
     */
    private static void storeCommit(Commit commit){
        String sha1OfCommit = Utils.sha1(Utils.serialize(commit));
        String commits_folder = COMMITS_PATH_FOLDER + "/" + sha1OfCommit.substring(0, 2);
        String commits_path = COMMITS_PATH_FOLDER + "/" + convertTo_2_38(sha1OfCommit);

        File folderOfCommit = new File(commits_folder);
        File commitFile = new File(commits_path);

        if (!folderOfCommit.exists()) folderOfCommit.mkdir();
        Utils.writeContents(commitFile, "");
        writeCommit(commit, commitFile);
    }

    private static HashMap<String, String> readHashMap(String sha1) {
        if (sha1.length() == 0) return null;
        HashMap<String, String> result = new HashMap<>();
        String filePath = FILEMAPS_PATH_FOLDER + "/" + convertTo_2_38(sha1);
        File toBeRead = new File(filePath);
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(toBeRead));
            result = (HashMap<String, String>) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.err.println("readCommit is not working");
        }

        return result;
    }

    /**
     * Get the head, which is the previous commit
     * @return previous commit
     */
    private static Commit getHead(){
        Commit returnCmt;
        String headSHA1 = Utils.readContentsAsString(new File(".gitlet/HEAD_STR"));
        returnCmt = getCommit(headSHA1);
        return returnCmt;
    }

    /**
     * Get a commit with certain sha1 code of the commit
     * @param sha1code input sha1 code of the commit
     * @return the commit corresponding to that sha1 code
     */
    private static Commit getCommit(String sha1code){
        if (sha1code.length() == 0) return null;
        String searchDir = sha1code.substring(0, 2);
        String searchFile = sha1code.substring(2, 6);
        if (!new File(COMMITS_FOLDER + searchDir).exists()) return null;
        String[] fileList = new File(COMMITS_FOLDER + searchDir).list();
        Commit returnCmt = new Commit();
        assert fileList != null;
        for (String fileNames : fileList){
            if (fileNames.substring(0,4).equals(searchFile)){
                File inFile = new File(COMMITS_FOLDER + searchDir + "/" + fileNames);
                try {
                    ObjectInputStream inp =
                            new ObjectInputStream(new FileInputStream(inFile));
                    returnCmt = (Commit) inp.readObject();
                    inp.close();
                } catch (IOException | ClassNotFoundException excp) {
                    return null;
                }
            }
            return returnCmt;
        }
        return null;

    }

    /**
     * Get the sha1 code of previous commit
     * @return the sha1 code of previous commit
     */
    private static String getHeadSHA1(){
        return Utils.readContentsAsString(new File(".gitlet/HEAD_STR"));
    }

    /**
     * Print out all the commits, from the most recent one to the initial one
     * @param cmt the commit
     * @param cmtID the commit's sha1 code
     */
    private static void logHelper(Commit cmt, String cmtID){
        if (cmt == null){return;}
        System.out.println("===");
        System.out.println("commit " + cmtID);
        System.out.println("Date: " + cmt.getDateString());
        System.out.println(cmt.getCommit_message());
        System.out.println();
        if ((cmt.getParent0() == null) || (cmt.getParent0() == "")){return;}
        logHelper(getCommit(cmt.getParent0()), cmt.getParent0());
    }

    /**
     * Set up a parent for a commit
     * @param commit to be set a parent
     */
    private static void setUpParent(Commit commit) {
        Path filePath = Path.of(".gitlet/HEAD_STR");
        String contentOfHead = "";
        try {
            contentOfHead = Files.readString(filePath);
        } catch (IOException e) {
            System.err.println("setUpParent is not working");
        }

        commit.addParent(contentOfHead);
    }

    /**
     * Update the head after a new commit
     * @param commit to be set as head
     */
    private static void updateHead(Commit commit) {
        String sha1Code = Utils.sha1(Utils.serialize(commit));
        Utils.writeContents(HEAD_STR ,sha1Code);
    }


    /**
     * Add the files to adding stage area and to fileMap.
     */
    private static void addCommitIntoFolder_addStage() {

        List<String> files = Utils.plainFilenamesIn(STAGEADD_PATH + "/");
        HashMap<String, String> committedFiles;
        // all committed files
        if (commitedFiles_File.exists()) {
            committedFiles = readCommitFiles(commitedFiles_File);
        } else committedFiles = new HashMap<>();


        for (String fileName : files) {
            String stageAddFile = STAGEADD_PATH + "/" + fileName;

            File file_Stage_Add = new File(stageAddFile); //Get file in stage add folder
            byte[] file_Stage_Add_InByte = Utils.readContents(file_Stage_Add);
            String sha1CodeOfFile = Utils.sha1(file_Stage_Add_InByte); //sha1 code of the file in adding folder

            //put file name and sha1 code of it into the FILE_MAPS
            fileMaps.put(fileName, sha1CodeOfFile);
            committedFiles.put(fileName, sha1CodeOfFile); // put the file into a total file map storing all committed file

            //store every file into the object folder
            String restOfSha1 = sha1CodeOfFile.substring(2);
            String objectPath = OBJECT_PATH + "/" + sha1CodeOfFile.substring(0, 2) + "/";
            File objectFolder = new File(objectPath);
            File objectFile = new File(objectPath + restOfSha1);

            if (objectFile.exists()) {// file exist in the object folder

            } else {
                if (!objectFolder.exists()) objectFolder.mkdir();
                Utils.writeContents(objectFile, file_Stage_Add_InByte);
            }
            file_Stage_Add.delete();
        }

        Utils.writeContents(commitedFiles_File, Utils.serialize(committedFiles));
    }
    private static String convertTo_2_38(String sha1Code) {
        String folderName = sha1Code.substring(0, 2);
        String fileWithRest = sha1Code.substring(2);
        return folderName + "/" + fileWithRest;
    }
    public static void printFormate(TreeMap<String, String> branches,
                                     TreeSet<String> staged,
                                     TreeSet<String> remvoed,
                                     TreeMap<String, String> modified,
                                     TreeSet<String> untracked
                                     ) {
        System.out.println("=== Branches ===");
        System.out.println("*" + getCurrentBranch());//change
        for (Map.Entry<String, String> branch : branches.entrySet()) {
            if (branch.getKey().equals(getCurrentBranch())) continue;
            System.out.println(branch.getKey());
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for (String file : staged) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String file : remvoed) {
            System.out.println(file);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        for (Map.Entry<String, String> file : modified.entrySet()) {
            System.out.println(file.getKey() + " (" + file.getValue() + ")");
        }
        System.out.println();

        System.out.println("=== Untracked Files ===");
        for (String file : untracked) {
            System.out.println(file);
        }
        System.out.println();

    }

    private static TreeSet<String> scanRemoved() {
        TreeSet<String> result = new TreeSet<>();
        //get all the file's name from deleting area
        List<String> files = Utils.plainFilenamesIn(STAGEDLETE_PATH + "/");
        //error
        if (!(files == null)){
            result.addAll(files);
        }
        return result;
    }

    private static TreeSet<String> scanStaged() {
        TreeSet<String> staged = new TreeSet<>();
        List<String> files = Utils.plainFilenamesIn(STAGEADD_PATH + "/");
        //get all the file names from staging adding area
        if (!(files == null)) {
            staged.addAll(files);
        }
        return staged;
    }

    private static TreeMap<String, String> scanModified() {
        TreeMap<String, String> result = new TreeMap<>();
        String deleted = "deleted";
        String modified = "modified";
        //TODO: get all the files in CWD which are different from HEAD or different from staging area

        //TODO: Tracked in the current commit, changed in the working directory, but not staged; or
        HashMap<String, String> head_hashMap= getHead().getHashMap();
        for (Map.Entry<String, String> file: head_hashMap.entrySet()) {
            File fileInCWD = new File(file.getKey());
            if (fileInCWD.exists()) {
                byte[] bytes_CWD = Utils.readContents(fileInCWD);
                String sha1_CWD = Utils.sha1(bytes_CWD);
                if (!sha1_CWD.equals(file.getValue())) {
                    String addedFilePath = STAGEADD_PATH + "/" + file.getKey();
                    //TODO: Not staged for removal, but tracked in the current commit and deleted from the working directory.
                    if (!(new File(addedFilePath).exists())) {//Not in adding area
                        result.put(file.getKey(), modified);
                    } else if (!Utils.sha1(Utils.readContents(new File(addedFilePath))).equals(file.getValue())) {
                        result.put(file.getKey(), modified);
                    }
                }
            }
        }

        List<String> addedFiles = Utils.plainFilenamesIn(STAGEADD_PATH + "/");
        for (String filename : addedFiles) {
            //TODO: Staged for addition, but deleted in the working directory; or
            if (!(new File(filename).exists())) {
                result.put(filename, deleted);
            } else if (!Utils.sha1(Utils.readContents(new File(filename))).equals( //TODO: Staged for addition, but with different contents than in the working directory; or
                    Utils.sha1(Utils.readContents(new File(STAGEADD_PATH + "/" + filename)))
                    )) {
                result.put(filename, modified);
            }
        }
//        System.out.println(result);
        return result;
    }

    private static TreeSet<String> scanUntracked() {
        TreeSet<String> result = new TreeSet<>();
        //TODO: finish method body
        //TODO: scan all the file in CWD and get the ones that are not in objects folder or file map folder
        List<String> files = Utils.plainFilenamesIn(System.getProperty("user.dir"));
        for (String fileName : files) {
            HashMap<String, String> commitFiles = readCommitFiles(commitedFiles_File);
            if (!commitFiles.containsKey(fileName)) {
                List<String> addedFiles = Utils.plainFilenamesIn(STAGEADD_PATH + "/");
                if (!addedFiles.contains(fileName)) {
                    result.add(fileName);
                }
            }
        }
        return result;
    }
    /*
     *@to scan CWD modified files
     */
    private static TreeMap<String, String> scanCurrModified() {
        TreeMap<String, String> result = new TreeMap<>();
        String modified = "modified";
        //TODO: get all the files in CWD which are different from HEAD or different from staging area

        //TODO: Tracked in the current commit, changed in the working directory, but not staged; or
        HashMap<String, String> head_hashMap = getHead().getHashMap();
        for (Map.Entry<String, String> file : head_hashMap.entrySet()) {
            File fileInCWD = new File(file.getKey());
            if (fileInCWD.exists()) {
                byte[] bytes_CWD = Utils.readContents(fileInCWD);
                String sha1_CWD = Utils.sha1(bytes_CWD);
                if (!sha1_CWD.equals(file.getValue())) {
                    String addedFilePath = STAGEADD_PATH + "/" + file.getKey();
                    //TODO: Not staged for removal, but tracked in the current commit and deleted from the working directory.
                    if (!(new File(addedFilePath).exists())) {//Not in adding area
                        result.put(file.getKey(), modified);
                    } else if (!Utils.sha1(Utils.readContents(new File(addedFilePath))).equals(file.getValue())) {
                        result.put(file.getKey(), modified);
                    }
                }
            }
        }
//        System.out.println(result);
        return result;
    }

    private static void setUpFileHash_deleting() {
        List<String> files_rm = Utils.plainFilenamesIn(STAGEDLETE_PATH + "/");
        for (String file : files_rm) {
            fileMaps.remove(file);
            String fileName = STAGEDLETE_PATH + "/" + file;
            File file_deleting = new File(fileName);
            file_deleting.delete();
        }
    }

    /**
     * This is a helper function for find, check every commit in commits folder
     * if there is any of them with the wanting commit message, print it, return null
     * otherwise, return false.
     * @param commitMessage the message with which the commit wanted
     * @return whether the commit with this commit message exist
     */
    private static boolean find_helper(String commitMessage) {
        boolean exist = false;
        //TODO: change it to get the file name
        File commitFolder = new File(COMMITS_FOLDER);
        for (File file : commitFolder.listFiles()) {
            File subFolder = new File(COMMITS_FOLDER + file.getName() + "/");
            for (File each : subFolder.listFiles()) {
                String sha1 = file.getName() + each.getName();
                Commit commit = getCommit(sha1);
                if (commit.getCommit_message().equals(commitMessage)) {
                    exist = true;
                    System.out.println(sha1);
                }
            }
        }

        return exist;
    }

    public static String getCurrentBranch(){
        return Utils.readContentsAsString(new File(BRANCH_INFO_PATH));
    }

    public static void setCurrentBranch(String branchName){
        Utils.writeContents(new File(BRANCH_INFO_PATH), branchName);
    }

    private static HashMap<String, String> readSplitPoints() {
        File toBeRead = new File(SPLIT_POINTS);
        HashMap<String, String> result = null;
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(toBeRead));
            result = (HashMap<String, String>) inp.readObject();
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.err.println("readSplitPoints is not working");
        }
        return result;
    }

    private static File writeSplitPoints(HashMap<String, String> toBeWriten){
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(new FileOutputStream(SPLIT_POINTS));
            out.writeObject(toBeWriten);
            out.close();
        } catch (IOException excp) {
            System.err.println("writeStage is not working");
        }
        return stageFile;
    }

    public static void test2(){
        List<String> necessaryFiles = Arrays.asList(new String[]{"commits", "filemaps", "objects", "stageArea", "commitMap", "commitedFiles", ".gitlet", "gitlet", "GitletTests.java"});
        String[] cwdFiles = new File(System.getProperty("user.dir")).list();
        for (String fileName : cwdFiles){
            if (!necessaryFiles.contains(fileName)){
                System.out.println(fileName);
            }
        }
    }

    public static void test3() {
        scanCurrModified();
    }


    public static boolean failedMergeTest(String givenBranch) {
        // failure cases for merge
        if (!stageIsClean()) {
            System.err.println("You have uncommitted changes.");
            return true;
        }

        TreeMap<String, String> branchMap = readBranchMap();
        if (!branchMap.containsKey(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return true;
        }

        if (givenBranch.equals(getCurrentBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            return true;
        }

        TreeSet<String> untrackedFiles = scanUntracked();
        if (!untrackedFiles.isEmpty()){
            System.out.println("untrackedFiles:" + untrackedFiles);
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return true;
        }

        TreeMap<String, String> modifiedFiles = scanCurrModified();
        if (!modifiedFiles.isEmpty()) {
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return true;
        }
        TreeSet<String> trackedNotInHead = trackedButNotInHead();
        if (!trackedNotInHead.isEmpty()){
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            return true;
        }
        return false;
    }

    private static boolean stageIsClean() {
        return isClean(STAGEADD_PATH + "/") && isClean(STAGEDLETE_PATH + "/");
    }

    private static boolean isClean(String path) {
        File[] files = new File(path).listFiles();
        return files.length == 0;
    }

    /**
     * This is an N^2 time complexity way to find the most recent ancestor
     * @param commit1 a branch
     * @param commit2 a branch
     * @return the most common ancestor
     */
    private static Commit getCommonAncestor(Commit commit1, Commit commit2) {
        Commit ancestor = new Commit();
        if (commit1 == commit2) {
            System.err.println("shouldn't print this message. The program should be aborted before this. This is getCommonAncestor");
        }
        Commit pointer1 = commit1;
        Commit pointer2 = commit2;

        //Bet on that there won't be a complicated test that
        //              commit1.1.1
        //            /             \
        // commit 1 <-                  <- commit2
        //            \             /
        //              commit1.2.1
        // Only got parent0
        while(pointer1.getParent0() != "") {
            while (pointer2.getParent0() != "") {
                if (pointer1.toString().equals(pointer2.toString())) {
                    ancestor = pointer2;
                    break;
                }
                pointer2 = getCommit(pointer2.getParent0());
            }
            pointer2 = commit2;
            pointer1 = getCommit(pointer1.getParent0());
        }

        return ancestor;
    }
    private static TreeSet<String> trackedButNotInHead() {
        TreeSet<String> result = new TreeSet<>();
        List<String> files = Utils.plainFilenamesIn(System.getProperty("user.dir"));
        HashMap<String, String> headHashMap = getHead().getHashMap();
        HashMap<String, String> commitFiles = readCommitFiles(commitedFiles_File);
        for (String fileName : files) {
            if (!headHashMap.containsKey(fileName)){
                if (commitFiles.containsKey(fileName)){
                    File fileInCWD = new File(fileName);
                    String fileSha1 = Utils.sha1(Utils.readContents(fileInCWD));
                    if (!fileSha1.equals(commitFiles.get(fileName))) {
                        result.add(fileName);
                    }
                }
            }

        }
        return result;
    }

}
