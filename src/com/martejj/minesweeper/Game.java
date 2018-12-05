package com.martejj.minesweeper;

import com.martejj.minesweeper.graphics.Canvas;
import com.martejj.minesweeper.graphics.Colour;
import com.martejj.minesweeper.graphics.Renderer;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private Canvas canvas;

    private Renderer renderer;

    private double mouseX, mouseY;

    public Game() {

        canvas = new Canvas();

    }

    public void run() {

        renderer = new Renderer(canvas);

        while (canvas.preRender()) {

            this.update();

            this.render();

            canvas.postRender();

        }

    }

    public void init() {

    }

    public void update() {

    }

    public void render() {

        renderer.drawRectangle(100, 100, 60, 60, Colour.RED, 0);

    }

    public static void main(String[] args) {

        var game = new Game();

        game.init();

        game.run();

    }

    public int getInitialHeight() {

        return renderer.getInitialHeight();

    }

    public int getInitialWidth() {

        return renderer.getInitialWidth();

    }

    public long getWindow() {

        return this.canvas.getWindow();

    }

    public double getMouseX() {

        return mouseX;

    }

    public double getMouseY() {

        return mouseY;

    }

}
