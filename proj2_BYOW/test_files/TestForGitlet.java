package test_files;

import gitlet.Commit;
import gitlet.Gitlet;
import org.junit.Test;

import java.io.File;
import java.util.TreeMap;
import java.util.TreeSet;

public class TestForGitlet {
    public static TreeMap<String, String> testForBranches = new TreeMap<>();
    public static TreeSet<String> testForStaged = new TreeSet<>();
    public static TreeSet<String> testForRemvoed = new TreeSet<>();
    public static TreeMap<String, String> testForModified = new TreeMap<>();
    public static TreeSet<String> testForUntracked = new TreeSet<>();

    @Test
    public void testPrintFormat() {
//        testForBranches.put("branch1", new Commit());
//        testForBranches.put("test", new Commit());
//        testForBranches.put("Ray_Branch", new Commit());
//        testForBranches.put("branch2", new Commit());
//        testForBranches.put("Lubin_Branch", new Commit());
//        testForBranches.put("main", new Commit());
//        testForBranches.put("master", new Commit());

        testForStaged.add("test.txt");
        testForStaged.add("Final");
        testForStaged.add("homework");
        testForStaged.add("aFile");
        testForStaged.add("bFile");
        testForStaged.add("abFile");
        testForStaged.add("A");

        testForRemvoed.add("remove1");
        testForRemvoed.add("AFile");
        testForRemvoed.add("CFile");
        testForRemvoed.add("A");
        testForRemvoed.add("a");
        testForRemvoed.add("C");
        testForRemvoed.add("B");

        testForModified.put("remove1.word", "deleted");
        testForModified.put("fFile", "deleted");
        testForModified.put("dFile", "deleted");
        testForModified.put("mFile", "modified");
        testForModified.put("cFile", "deleted");
        testForModified.put("aFile", "modified");
        testForModified.put("eFile", "deleted");

        testForUntracked.add("aFile");
        testForUntracked.add("z");
        testForUntracked.add("f");
        testForUntracked.add("b");
        testForUntracked.add("B");
        testForUntracked.add("a");
        testForUntracked.add("e");

        Gitlet.printFormate(testForBranches, testForStaged, testForRemvoed, testForModified, testForUntracked);
    }

    @Test
    public void testInit() {
        Gitlet.init();
//        Gitlet.add();
    }
    @Test
    public void testAdd() {
        Gitlet.add("test2.txt");
//        Gitlet.add();
    }
    @Test
    public void testCommit() {
        Gitlet.commit("this is commit, added test2", 0);
//        Gitlet.headInfo();

//        Gitlet.add();
    }
    @Test
    public void testCheckout() {
        Gitlet.checkout_fileName("test3.txt");

    }
    @Test
    public void testLog() {
        Gitlet.log();
    }

    @Test
    public void testRm() {
        Gitlet.remove("test3.txt");
//        Gitlet.add();
    }

    @Test
    public void testBranch(){
        testInit();
        Gitlet.branch("other");
        Gitlet.add("test3.txt");
//        Gitlet.commit("MSG1", 0);
//        Gitlet.add("test2.txt");
        Gitlet.commit("MSG2", 0);
        new File("3.txt").delete();
//        new File("test2.txt").delete();
//        Gitlet.checkout_branch("other");

    }

    @Test
    public void testStatus() {
        Gitlet.status();
//        Gitlet.add();
    }

    @Test
    public void testFind() {
        Gitlet.find("this is commit");
    }

    @Test
    public void setTestForBranches() {
        Gitlet.init();
        Gitlet.branch("branch1");
        Gitlet.add("test1.txt");
        Gitlet.commit("Commit1 for branch1", 0);
//        Gitlet.checkout_branch("branch1"); //No need to check out the current branch.
//        Gitlet.checkout_branch("branch5");//No such branch exists.
//        Gitlet.checkout_branch("main");
    }
    @Test
    public void badcheckout1(){
        Gitlet.init();
        Gitlet.add("test2.txt");
        Gitlet.commit("message2", 0);
        Gitlet.log();
    }
    @Test
    public void badcheckout2(){
        Gitlet.checkout_commentID("8ad69cfb0b07243b784192210917b1d962445280", "test.txt");
//        String[] = new String[]{"checkout", }
    }
}
