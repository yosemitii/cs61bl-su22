package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import jdk.swing.interop.SwingInterOpUtils;

import java.awt.*;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.io.*;
import java.util.HashSet;
import java.util.concurrent.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public final int WIDTH = 80;
    public final int HEIGHT = 50;
    TETile buffer;
    Avatar avatar;
    TETile[][] map = new TETile[WIDTH][HEIGHT];
    TETile[][] cloud = new TETile[WIDTH][HEIGHT];
    World actualWorld;


    private String numbers = "1234567890";

    boolean gameOver;

    boolean isCloud = true;

    int totalTime = 30;

    int timeUsed = 0;

    TETile lastMouseHover = null;

    HashSet<Point> offLights;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        map = new TETile[80][50];
        cloud = new TETile[80][50];
        mainMenuDisplay();
        char option = menuInput();
        switch (option) {
            case 'n':
                Long seed = intoGame();
                //generate a world
                System.out.println("seed in Big: " + seed);
                actualWorld = new World(seed);
                System.out.println("initilizing world: " + seed);
                initialCanvas(map, cloud, actualWorld);

                avatar = Avatar.generateAvatar(actualWorld.rooms);
                buffer = map[avatar.x][avatar.y];
                map[avatar.x][avatar.y] = Tileset.AVATAR;
                actualWorld.setAvatar(avatar);
                ter.renderFrame(map);

                interactWithKey();
                break;
            case 'l':
                loadGame();
                break;
        }
    }

    public void save() {
        System.out.println(actualWorld.seed);
        System.out.println(actualWorld == null);
        File outFile = new File("gameFile.txt");
        Utils.writeContents(outFile, Utils.serialize(actualWorld));
    }

    public void loadGame_test() {
        World testWorld = new World(123L);
//        testWorld.seed = 123L;
        TETile[][] newMap = new TETile[80][50];
        TETile[][] cloud = new TETile[80][50];
        initialCanvas(newMap, cloud, testWorld);
//        for (int i = 0; i < newMap.length; i++) {
//            for (int j = 0; j < newMap[0].length; j++) {
//                newMap[i][j] = Tileset.NOTHING;
//                cloud[i][j] = Tileset.NOTHING;
//            }
//        }

//        testWorld.generateRoom(newMap);
//        testWorld.generateHallway(newMap);
//        World.generateWall(newMap);
        ter.renderFrame(newMap);
    }
    public void loadGame() {
        actualWorld = load();
        Font smallFont = new Font("", Font.PLAIN,15);
        map = actualWorld.map;
        avatar = actualWorld.avatar;
        buffer = actualWorld.buffer;
        cloud = actualWorld.cloud;
        World.generateCloud(map, cloud, avatar);

        for (Light light : actualWorld.lights) {
            if (light.equals(Tileset.OFFLIGHT)) {
                buffer = Light.off(map, light.getX(), light.getY(), buffer);
            }
        }

        StdDraw.setFont(smallFont);

        if (!isCloud) {
            ter.renderFrame(actualWorld.map);
        } else {
            ter.renderFrame(cloud);
        }
        interactWithKey();
    }

    public World load() {
        World world;
        File inFile = new File("gameFile.txt");
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            world = (World) inp.readObject();
            System.out.println("loading..." + world.seed);
            inp.close();
        } catch (IOException | ClassNotFoundException excp) {
            System.out.println("load is not working");
            world = null;
        }

        return world;
    }

    private void interactWithKey() {
        boolean gameOver = false;
        Runnable keyInteraction = new KeyThread();
        Runnable mouseInteraction = new MouseThread();
        TimerThead timer = new TimerThead();
        int count = 300;
//        ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorService executor = Executors.newFixedThreadPool(32);

        while (!gameOver) {
//            System.out.println("===========>" + count);
//            executorService.execute(new KeyThread());
//            executorService.execute(new MouseThread());
//            executorService.execute(new TimerThead());
//            mouseInteraction.run();
            Future<?> future0 = executor.submit(new MouseThread());
            try {
//                Object result = future.get(1000, TimeUnit.MILLISECONDS);
                future0.get(100, TimeUnit.MILLISECONDS);
//                System.out.println("Completed successfully");
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (TimeoutException e) {
                System.out.println("Timed out. Cancelling the runnable...");
                future0.cancel(true);
            }
            Future<?> future = executor.submit(new KeyThread());
//            future.get(2, TimeUnit.SECONDS);

//            executor.invokeAll(keyInteraction), 1, TimeUnit.SECONDS); // Timeout of 10 minutes.
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            Future<?> future = executor.submit(keyInteraction);
            try {
//                Object result = future.get(1000, TimeUnit.MILLISECONDS);
                future.get(1000, TimeUnit.MILLISECONDS);
//                System.out.println("Completed successfully");
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (TimeoutException e) {
                System.out.println("Timed out. Cancelling the runnable...");
                future.cancel(true);
            }

//            executor.shutdown();
//            keyInteraction.run();
            timer.run();
            if (totalTime - timeUsed <= 0 ) {
                gameOver = true;
            }
        }
    }

    private class MouseThread implements Runnable {
        @Override
        public void run() {
//            System.out.println("mouse threading");
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();
//            System.out.println("________>" + mouseX + " " + mouseY);
            displayMouse(mouseX, mouseY, map);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private class KeyThread implements Runnable {
        Font smallFont = new Font("", Font.PLAIN,15);
        @Override
        public void run() {
//            System.out.println("keyboard threading");
            StdDraw.setFont(smallFont);
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(1);
                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();
                if (mouseX > 0 || mouseX < map.length - 1) {
                    if (mouseY > 0 || mouseY < map[0].length - 1) {
                        displayMouse(mouseX, mouseY, map);
                    }
                }

            }

            char key = StdDraw.nextKeyTyped();
            if (key == 'c') {
                isCloud = true;
            }
            if (key == 'm') {
                isCloud = false;
            }
//            if (key != 'c') {
//                char key2 = StdDraw.nextKeyTyped();
            if (key == ':') {
                while (!StdDraw.hasNextKeyTyped()) {
                    StdDraw.pause(1);
                }
                char key3 = StdDraw.nextKeyTyped();
                if (Character.toLowerCase(key3) == 'q') {
                    actualWorld.setBuffer(buffer);
                    actualWorld.setAvatar(avatar);
                    actualWorld.setMap(map);
                    actualWorld.setCloud(cloud);
                    save();
//                        System.out.println("buffer: " + buffer);
//                        System.out.println("avatar: " + avatar);
                    World.cleanCloud(cloud);
                    ter.renderFrame(cloud);
                    drawFrameAdd("Game will be saved", 40, 48, 30);
                    StdDraw.pause(1000);
                    System.exit(0);
                }
            }
//                buffer = operationWithKey(key, buffer, map, avatar);
//                ter.renderFrame(map);
//                addText("press 'C' to turn on the cloud mod", 60, 48, smallFont);
//                addText("press 'O/F' to turn on/off the light, remember to step on it", 60, 47, smallFont);
//            }
            buffer = operationWithKey(key, buffer, map, avatar);
            if (isCloud) {
                World.cleanCloud(cloud);
                World.generateCloud(map, cloud, avatar);
                ter.renderFrame(cloud);
//                World.cleanCloud(cloud);
                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();
//            System.out.println("________>" + mouseX + " " + mouseY);
                displayMouse(mouseX, mouseY, map);
            } else {
//                buffer = operationWithKey(key, buffer, map, avatar);
                ter.renderFrame(map);

            }
//            return 0;
        }

    }

    private class TimerThead implements Runnable {
        LocalTime initialTime = LocalTime.now();
        @Override
        public void run() {
//            System.out.println("timer threading");
            LocalTime time = LocalTime.now();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            java.util.Date d1 = null;
            java.util.Date d2 = null;
            try {
                d1 = format.parse(initialTime.toString());
                d2 = format.parse(time.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long diff = d2.getTime() - d1.getTime();
            Long diffSeconds = diff / 1000;
            timeUsed = diffSeconds.intValue();
//            Duration timeElapsed = Duration.between(initialTime, time);
            drawFrameAdd("You have " + (totalTime - timeUsed) + "s to escape the room, RUN!", 40, 48, 30);
        }
    }

    private Long intoGame() {
        seedInputDisplay();
        String stringInput = keyboardInputString().toLowerCase();
//        System.out.println("input -p: " + stringInput);
        String seedString = stringInput.substring(0, stringInput.length());
        System.out.println("----------------actuall seed: " + seedString);
//        for (int i = 0; i < stringInput.length(); i++) {
//            System.out.println("char: " + i + ", " + stringInput.charAt(i));
//        }
//        System.out.println("stringInput.length(): " + stringInput.length());
//        System.out.println(seedString);
        if (seedString == "") {
            return 0L;
        }
        Long seed = Long.parseLong(seedString);
        Font smallFont = new Font("", Font.PLAIN,15);
        StdDraw.setFont(smallFont);

        return seed;
    }
    private TETile operationWithKey(char key, TETile buffer, TETile[][] map, Avatar avatar) {
        switch (Character.toLowerCase(key)) {
            case 'w':
                System.out.println("w");
                buffer = avatar.moveTo(avatar.x, avatar.y + 1, map, buffer);
                break;
            case 'a':
                System.out.println("a");
                buffer = avatar.moveTo(avatar.x - 1, avatar.y, map, buffer);
                break;
            case 's':
                System.out.println("s");
                buffer = avatar.moveTo(avatar.x, avatar.y - 1, map, buffer);
                break;
            case 'd':
                System.out.println("d");
                buffer = avatar.moveTo(avatar.x + 1, avatar.y, map, buffer);
                break;
            case 'f':
                TETile temp = map[avatar.getX()][avatar.getY()];
                buffer = Light.off(map, avatar.getX(), avatar.getY(), buffer);

                TETile compare = new TETile('✶', Color.white, Color.BLACK, "off light");
                System.out.println("-->" + map[avatar.getX()][avatar.getY()]);
                if (map[avatar.getX()][avatar.getY()].equals(compare)) {
                    updateOffLights(avatar.getX(), avatar.getY());
                }
                map[avatar.getX()][avatar.getY()] = temp;
                break;
            case 'o':
                buffer = Light.on(map, avatar.getX(), avatar.getY(), buffer);
                break;

        }

        return buffer;
    }

    private void updateOffLights(int x, int y) {
        System.out.println("adding time");
        if (offLights == null) {
            offLights = new HashSet<>();
        }
        Point newOffLight = new Point(x, y);
        if (!offLights.contains(newOffLight)) {
            offLights.add(newOffLight);
            drawFrameAdd("add 5 seconds", 40, 50, 20);
            totalTime += 5;
        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
//        TERenderer ter = new TERenderer();
        TETile[][] finalWorldFrame = null;
        ter.initialize(80, 50);
        finalWorldFrame = new TETile[80][50];
        TETile[][] cloud;
        String seedString = input.substring(1, input.length() - 1);
        System.out.println("input -s: " + input + "---------------------------");
        System.out.println(seedString);
        Long seed = Long.valueOf(seedString);

        for (int i = 0; i < finalWorldFrame.length; i++) {
            for (int j = 0; j < finalWorldFrame[0].length; j++) {
                finalWorldFrame[i][j] = Tileset.NOTHING;
            }
        }

        World actualWorld = new World(seed, null);
        actualWorld.generateRoom(finalWorldFrame);
        actualWorld.generateHallway(finalWorldFrame);
        World.generateWall(finalWorldFrame);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }


    public void drawFrame(String s) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("TimesRoman", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.WIDTH / 2, this.HEIGHT / 2, s);
        StdDraw.show();
    }
    public void drawFrameAdd(String s, int x, int y, int fontSize) {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("TimesRoman", Font.ITALIC, fontSize);
        StdDraw.setFont(fontBig);
//        StdDraw.text(x, y, " ");
        StdDraw.text(x, y, s);
        StdDraw.show();
    }

    public void addText(String s, int x, int y, Font small) {
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Plain", Font.BOLD, 15);
        StdDraw.setFont(fontBig);
        StdDraw.text(x, y, s);
        StdDraw.show();
        StdDraw.setFont(small);
    }

    private String keyboardInputString() {
        String result = "";
        while (!result.toLowerCase().contains("s")) {
//            System.out.println(result.toLowerCase());
            if (StdDraw.hasNextKeyTyped()) {
                char next = StdDraw.nextKeyTyped();
                String nextString = Character.toString(next);
                System.out.println(next);
                if (numbers.contains(nextString)) {
                    result += nextString;
                    drawFrame(result);
                } else if (next == 's' || next == 'S') {
                    return result;
                } else {
                    drawFrameAdd("Invalid input, please input again", 60, 48, 30);
                    StdDraw.pause(50);
                    drawFrame(result);
                }
            }
        }
        return result;
    }

    private char menuInput() {
        char result = ' ';
        while (result != 'n' && result != 'l') {
            while (!StdDraw.hasNextKeyTyped()) {
                StdDraw.pause(1);
            }
            result = Character.toLowerCase(StdDraw.nextKeyTyped());
        }
        return result;
    }

    private void mainMenuDisplay() {
        ter.initialize(80, 50);
//        drawFrameFree("Wecome to the game!", WIDTH/2, HEIGHT / 2 + 2);
//        drawFrameUP("PLEASE INPUT SEED UP");
        drawFrame("♚Welcome to BYOW by Ray and Lubin♚");
        drawFrameAdd("================================", 40, 23, 30);
        drawFrameAdd("♖New game (N)", 40, 21, 30);
        drawFrameAdd("♖Load game (L)", 40, 19, 30);
        drawFrameAdd("♖Quit game (L)", 40, 17, 30);
    }

    private void seedInputDisplay() {
        drawFrame("Please Input seed: ");
    }

    /**
     * Generate rooms
     * @param map
     * @param actualWorld
     * @return
     */
    private void initialCanvas(TETile[][] map, TETile[][] cloud, World actualWorld) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = Tileset.NOTHING;
                cloud[i][j] = Tileset.NOTHING;
            }
        }

        actualWorld.generateRoom(map);
        actualWorld.generateHallway(map);
        World.generateWall(map);
        actualWorld.generateLights(map);
    }

    private void displayMouse(double mouseX, double mouseY, TETile[][] tileArray) {
        if (mouseX <= 0 || mouseX >= WIDTH || mouseY <= 0 || mouseY >= HEIGHT) {
            return;
        }
        Double tileX = mouseX;
        Double tileY = mouseY;
        Integer intTileX = tileX.intValue();
        Integer intTileY = tileY.intValue();
//        System.out.println("mouse input: " + mouseX + " " + mouseY);
//        System.out.println("mouse input: " + intTileX + " " + intTileY);
        if (tileArray[intTileX][intTileY] == Tileset.NOTHING) {
            upDisplay("this tile is nothing");
        } else if (tileArray[intTileX][intTileY] == Tileset.WALL) {
            upDisplay("this tile is wall");
        } else {
            upDisplay("this tile is floor");
        }
        lastMouseHover = tileArray[intTileX][intTileY];
    }

    private void upDisplay(String s) {
//        StdDraw.clear(Color.BLACK);
        drawFrameAdd(s, 75, 49, 20);
    }
}
