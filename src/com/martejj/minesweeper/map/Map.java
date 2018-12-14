package com.martejj.minesweeper.map;

import com.martejj.minesweeper.Game;
import com.martejj.minesweeper.graphics.Model;
import com.martejj.minesweeper.graphics.Renderer;
import com.martejj.minesweeper.graphics.Texture;

import java.util.Random;

import java.lang.Math;

import com.martejj.minesweeper.input.InputHandler;
import static org.lwjgl.glfw.GLFW.*;

public class Map {

    public static final int MAX_NEIGHBOUR_MINES = 8;

    boolean[][] isVisible, isMine, isFlagged;

    int[][] numAdjacentMines;

    int xSize, ySize;

    double difficulty;

    public static final double margin = 50;

    double ySceneSize, xSceneSize;

    double tileSize;

    Game game;

    Model[] tileModels;

    Model flagModel;

    public Map(int xSize, int ySize, double difficulty, int seed, Game game) {

        this.game = game;

        this.difficulty = difficulty;

        this.xSize = xSize;
        this.ySize = ySize;

        isVisible = new boolean[xSize][ySize];
        isMine = new boolean[xSize][ySize];
        isFlagged = new boolean[xSize][ySize];

        numAdjacentMines = new int[xSize][ySize];

        generateMap(seed);

        ySceneSize = game.getInitialHeight() - margin*2;
        xSceneSize = game.getInitialWidth() - margin*2;

        double yTileSize = ySceneSize/ySize;
        double xTileSize = xSceneSize/xSize;

        // Set it to the smaller of the tiles

        tileSize = (yTileSize < xTileSize ? yTileSize : xTileSize);

        // Load tile textures
        tileModels = new Model[MAX_NEIGHBOUR_MINES];

        for (int i = 0; i < MAX_NEIGHBOUR_MINES; i++) {

            tileModels[i] = Renderer.createQuadModel();
            tileModels[i].setTexture(new Texture(Integer.toString(i)));

        }

        flagModel = Renderer.createQuadModel();
        flagModel.setTexture(new Texture("flag"));

    }

    public void render(Renderer renderer) {

        for (int y = 0; y < ySize; y++) {

            for (int x = 0; x < xSize; x++) {

                Model model;

                if (!isVisible[x][y] && !isFlagged[x][y]) {
                    model = tileModels[0];
                } else if (isFlagged[x][y]) {
                    model = flagModel;
                } else if (isVisible[x][y] && !isMine[x][y]){
                    model = tileModels[numAdjacentMines[x][y]];
                } else {
                    model = tileModels[0];
                }

                renderer.drawModel(tileSize/2, tileSize/2, getX(x), getY(y), 0, model);
            }

        }

    }

    public void update() {

        if (InputHandler.getButtonState(GLFW_MOUSE_BUTTON_LEFT) == InputHandler.BUTTON_RELEASE) {
            cascadeVisibility(screenCoordsToTileCoords(InputHandler.getMouseX()), screenCoordsToTileCoords(InputHandler.getMouseY()), 0);
        } else if (InputHandler.getButtonState(GLFW_MOUSE_BUTTON_RIGHT) == InputHandler.BUTTON_RELEASE) {

        }

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

        // Recursively cascade visibility
        cascadeVisibility(x + 1, y, dst);
        cascadeVisibility(x - 1, y, dst);
        cascadeVisibility(x, y + 1, dst);
        cascadeVisibility(x, y - 1, dst);

    }

    public double tileCoordsToScreenCoords(int tileCoord) {
        return tileCoord*tileSize + margin;
    }

    public int screenCoordsToTileCoords(double screenCoord) {
        return (int) ((screenCoord - margin)/tileSize);
    }

    public double getX(int x) {

        return x*tileSize + margin;

    }

    public double getY(int y) {

        return y*tileSize + margin;

    }

}
