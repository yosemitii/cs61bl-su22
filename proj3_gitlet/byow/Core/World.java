package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;

public class World implements Serializable {
    //1600 * 1200
    int width;
    int height;
    Long seed;
    Avatar avatar;
    List<Room> rooms;
    Set<Light> lights;

    Set<Point> roomCenters;
    TETile buffer;
    TETile[][] map;
    TETile[][] cloud;

    public void setMap(TETile[][] map) {
        this.map = map;
    }
    public void setCloud(TETile[][] cloud) {
        this.cloud = cloud;
    }

    public World() {
    }
    public World(Long seed, Set<Light> lights) {
        this.seed = seed;
        this.rooms = new ArrayList<>();
        this.lights = lights;
        this.width = 80;
        this.height = 60;
        this.roomCenters = new HashSet<>();
    }

    public World(Long seed) {
        this(seed, new HashSet<>());
    }

    public void addLight(Light light) {
        lights.add(light);
    }
    public void save() { }
    public void generateRoom(TETile[][] map) {
        generateRoomObj();
        for (Room rooms: rooms) {
            for (int i = rooms.getLeft(); i < rooms.getRight(); i++) {
                for (int j = rooms.getLower(); j < rooms.getUpper(); j++) {
                    map[i][j] = Tileset.FLOOR;
                }
            }
        }
    }
    public void generateRoomObj() {
        System.out.println("-----room: " + seed);
        Random r = new Random(seed);
        int roomNum = 3 + Math.floorMod(r.nextInt(), 4);
        System.out.println(roomNum);
        for (int i = 0; i < roomNum; i++) {
            int x = r.nextInt();
            System.out.println("x: " + x);
            Room pendingRoom = generateSingleRoom(x);
//            System.out.println(pendingRoom.toString());
//            System.out.println("Is overlapped: " + isRoomOverlapped(pendingRoom));
            while (isRoomOverlapped(pendingRoom)) {
                pendingRoom = generateSingleRoom(r.nextInt());
            }
            rooms.add(pendingRoom);
            roomCenters.add(pendingRoom.gerCenter());
        }
    }

    public void generateHallway(TETile[][] map) {
        Hallway.generate(roomCenters, map);
    }
    public static void generateWall(TETile[][] map) {
//        System.out.println("length: " + (map[0].length - 2) + ", " + (map.length - 2));
        for (int x = 0; x < map.length - 1; x++) {
            for (int y = 0; y < map[0].length - 1; y++) {
                setWall(x, y, map);
//                System.out.println(x + ", " + y);
            }
        }
    }

    public static void generateCloud(TETile[][] map, TETile[][] cloud, Avatar avatar) {
//        check the boundary
        int size = 3;
        int x = avatar.getX();
        int y = avatar.getY();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cloud[x + i][y + j] = map[x + i][y + j];
                cloud[x + i][y - j] = map[x + i][y - j];
                cloud[x - i][y + j] = map[x - i][y + j];
                cloud[x - i][y - j] = map[x - i][y - j];
            }
        }
    }

    public static void cleanCloud(TETile[][] cloud) {
        for (int i = 0; i < cloud.length; i++) {
            for (int j = 0; j < cloud[0].length; j++) {
                cloud[i][j] = Tileset.NOTHING;
            }
        }
    }
    public void generateLights(TETile[][] map) {
        for (Point point : roomCenters) {
            addLight(new Light(point.getX(), point.getY(), "onLight"));
//            map[point.getX()][point.getY()] = Tileset.WALL;
            Light.generateLight(map, point.getX(), point.getY());
        }
    }

    private Room generateSingleRoom(int roomSeed) {
        Random r = new Random(roomSeed);
        int centerX = 8 + Math.floorMod(r.nextInt(), 64);
        int centerY = 8 + Math.floorMod(r.nextInt(), 34);
        int halfHeight = 1 + Math.abs(Math.floorMod(r.nextInt(), 5));
        int halfWidth = 1 + Math.abs(Math.floorMod(r.nextInt(), 5));
        return new Room(new Point(centerX, centerY), centerY + halfHeight,
                centerY - halfHeight, centerX - halfWidth, centerX + halfWidth);
    }

    private boolean isRoomOverlapped(Room pendingRoom) {
        if (rooms.isEmpty()) {
            return false;
        }
        for (Room existRoom: rooms) {
            if (!(pendingRoom.getLower() > existRoom.getUpper()
                    || pendingRoom.getUpper() < existRoom.getLower()
                    || pendingRoom.getLeft() > existRoom.getRight()
                    || pendingRoom.getRight() < existRoom.getLeft())) {
                return true;
            }
        }
        return false;
    }

    private static void setWall(int x, int y, TETile[][] map) {
        boolean hasNothing = false;
        boolean besideFloor = false;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (map[x + i][y + j].equals(Tileset.NOTHING)) {
                    hasNothing = true;
                }
                if (map[x + i][y + j].equals(Tileset.FLOOR)) {
                    besideFloor = true;
                }
            }
        }

        if (hasNothing && besideFloor) {
//            System.out.println("into this loop");
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
//                    System.out.println((x + i) + ", " + (y + i));
                    if (map[x + i][y + j].equals(Tileset.NOTHING)) {
                        map[x + i][y + j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void setBuffer(TETile buffer) {
        this.buffer = buffer;
    }

    public List<Room> getRooms() {
        return rooms;
    }

}
