package com.martejj.minesweeper.map;

import com.martejj.minesweeper.graphics.Model;
import com.martejj.minesweeper.graphics.Renderer;
import com.martejj.minesweeper.graphics.Texture;

import java.util.Random;

import java.lang.Math;

import org.lwjgl.opengl.GL11;

public class Map {

    boolean[][] isVisible, isMine, isFlagged;

    int[][] numAdjacentMines;

    int xSize, ySize;

    double difficulty;

    Model square;

    public Map(int xSize, int ySize, double difficulty, int seed) {

        this.difficulty = difficulty;

        this.xSize = xSize;
        this.ySize = ySize;

        isVisible = new boolean[xSize][ySize];
        isMine = new boolean[xSize][ySize];
        isFlagged = new boolean[xSize][ySize];

        numAdjacentMines = new int[xSize][ySize];

        generateMap(seed);

        /*float[] vertices = {
                -1f,   1f,   0, // TOP LEFT
                1f,   1f,   0, // TOP RIGHT
                1f,  -1f,   0, // BOTTOM RIGHT
                -1f,  -1f,   0  // BOTTOM LEFT
        };

        int[] indices = {
                0,  1,  2,
                0,  2,  3
        };

        this.square = new Model(vertices, indices, GL11.GL_TRIANGLES);
        this.square.setTexture(new Texture("water"));*/

    }

    public void render(Renderer renderer) {

        //renderer.drawModel(64, 64, 400, 400, 0, this.square);

    }

    public void generateMap(int seed) {

        Random random = new Random(seed);

        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                // All start off as non-visible
                isVisible[x][y] = false;

                // Generate random number between 0 and 100
                int randomNumber = Math.abs(random.nextInt())%100;

                // On random chance make this a mine - TODO ensure winnable
                isMine[x][y] = randomNumber < difficulty * 100;
            }

        }

        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                numAdjacentMines[x][y] = getNumAdjacentMines(x, y);

                System.out.print(numAdjacentMines[x][y] + ", ");

            }

            System.out.println();

        }

    }

    int getNumAdjacentMines(int x, int y) {

        if (isMine[x][y]) return -1;

        int count = 0;

        if (x - 1 >= 0 && y - 1 >= 0 && isMine[x - 1][y - 1]) count++;
        // Check middle left
        if (x - 1 >= 0 && isMine[x - 1][y]) count++;
        // Check top left
        if (x - 1 >= 0 && y + 1 < ySize && isMine[x - 1][y + 1]) count++;

        // Check bottom right
        if (x + 1 < xSize && y - 1 >= 0 && isMine[x + 1][y - 1]) count++;
        // Check middle right
        if (x + 1 < xSize && isMine[x + 1][y]) count++;
        // Check top right
        if (x + 1 < xSize && y + 1 < ySize && isMine[x + 1][y + 1]) count++;

        // Check top middle
        if (y + 1 < ySize && isMine[x][y + 1]) count++;
        // Check bottom middle
        if (y - 1 >= 0 && isMine[x][y - 1]) count++;

        return count;

    }

    public void cascadeVisibility(int x, int y, int dst) {

        // Check we are within bounds
        if (x + 1 >= xSize) return;
        if (x - 1 < 0) return;
        if (y + 1 >= ySize) return;
        if (y - 1 < 0) return;

        // If we hit somewhere already visible stop
        if (isVisible[x][y]) return;

        // Distance in effect measures the distance to the most recent tile without a mine or adjacent mine
        if (numAdjacentMines[x][y] != 0) dst++;

        if (dst > 1) return;

        // Set this visibility
        isVisible[x][y] = true;

        cascadeVisibility(x + 1, y, dst);
        cascadeVisibility(x - 1, y, dst);
        cascadeVisibility(x, y + 1, dst);
        cascadeVisibility(x, y - 1, dst);

    }

}
