package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];


        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        Random r = new Random();
        System.out.println(r.nextInt());
        System.out.println(r.nextInt());

        addPlus(world, WIDTH/2, HEIGHT/2, 2, r.nextInt());

        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static TETile[][] addPlus(TETile[][] world, int col, int row, int size, int seed){
        if ((col + 1.5 * size > WIDTH) || (col - 1.5 * size < 0)){
            System.out.println("colum index out of boundary");
            return world;
        }
        if ((row + 1.5 * size > HEIGHT) || (row - 1.5 * size < 0)){
            System.out.println("colum index out of boundary");
            return world;
        }
        if (size % 2 == 0) {
            for (int i = col - (size * 3) / 2; i < col + (size * 3) / 2; i++) {
                for (int j = row - (size / 2); j < row + (size / 2); j++) {
                    Random r = new Random(seed);
                    world[i][j] = Tileset.FLOWER;
                    world[i][j] = TETile.colorVariant(world[i][j], 50, 50, 50, r);
                }
            }
            for (int j = row - (size * 3) / 2; j < row + (size * 3) / 2; j++) {
                for (int i = col - (size / 2); i < col + (size / 2); i++) {
                    Random r = new Random(seed);
                    world[i][j] = Tileset.FLOWER;
                    world[i][j] = TETile.colorVariant(world[i][j], 50, 50, 50, r);
                }
            }
        }
        else {
            for (int i = col - (size * 3) / 2; i <= col + (size * 3) / 2; i++) {

                for (int j = row - (size / 2); j <= row + (size / 2); j++) {
                    Random r = new Random(seed);
                    world[i][j] = Tileset.FLOWER;
                    world[i][j] = TETile.colorVariant(world[i][j], 50, 50, 50, r);
                }
            }
            for (int j = row - (size * 3) / 2; j <= row + (size * 3) / 2; j++) {
                for (int i = col - (size / 2); i <= col + (size/ 2); i++) {
                    Random r = new Random(seed);
                    world[i][j] = Tileset.FLOWER;
                    world[i][j] = TETile.colorVariant(world[i][j], 50, 50, 50, r);
                }
            }
        }
        return world;
    }



    private class Point{
        int x;
        int y;

        private Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        private boolean isOutOfBoundary(int width, int height){
            if ((x >0) && (x < width)){
                if ((y>0) && (y < height)){
                    return true;
                }
            }
            return false;
        }
    }
}

