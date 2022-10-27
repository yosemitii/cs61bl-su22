package gitlet;

import java.io.File;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Sirui Li Ray
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        if (args[0].equals("init")){
            Gitlet.init();
        }
        else{
            File init = new File(".gitlet");
            if(init.exists()){
                switch(firstArg) {
                    case "test":
                        System.out.println(args[1]);
                        break;
                    case "add":
                        Gitlet.add(args[1]);
                        break;
                    case "commit":
                        if (args.length <= 1) {
                            System.err.println("Please enter a commit message.");
                        } else if (args[1].equals("")) {
                            System.err.println("Please enter a commit message.");
                        }
                        Gitlet.commit(args[1], 0);
                        break;
                    case "checkout":
//                if ((args.length == 3) && (args[1].equals("--"))) {
                        if ((args.length == 3) && (args[1].equals("--"))){
                            Gitlet.checkout_fileName(args[2]);
                        } else if ((args.length == 4) && (args[2].equals("--"))){
                            Gitlet.checkout_commentID(args[1], args[3]);
                        } else if (args.length == 2) {
                            Gitlet.checkout_branch(args[1], false);
                        } else{
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "log":
                        Gitlet.log();
                        break;
                    case "status":
                        //TODO: status
                        Gitlet.status();
                        break;
                    case "rm":
                        //TODO: what about other cases, throw err?
                        String fileName = args[1];
                        Gitlet.remove(fileName);
                        break;
                    case "global-log":
                        Gitlet.global_log();
                        break;
                    case "find":
                        Gitlet.find(args[1]);
                        break;
                    case "branch":
                        Gitlet.branch(args[1]);
                        break;
                    case "rm-branch":
                        Gitlet.removeBranch(args[1]);
                        break;
                    case "reset":
                        //TODO: reset with commit id as input
                        Gitlet.reset(args[1]);
                        break;
                    case "merge":
                        Gitlet.merge(args[1]);
                        break;
                    case "info":
                        Gitlet.headInfo();
                        break;
                    case "test1":
                        Gitlet.testWritingFile(args[1], args[2]);
                        break;
                    case "test2":
                        Gitlet.test2();
                        break;
                    case "test3"://modified scan
                        Gitlet.test3();
                        break;
                    default:
                        System.out.println("No command with that name exists.");
                        break;
                }
                return;
            }
            else{
                System.out.println("Not in an initialized Gitlet directory.");
                return;
            }
        }
    }
}
