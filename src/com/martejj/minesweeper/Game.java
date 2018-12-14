package com.martejj.minesweeper;

import com.martejj.minesweeper.graphics.*;
import com.martejj.minesweeper.input.InputHandler;
import com.martejj.minesweeper.map.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private Canvas canvas;

    private Renderer renderer;

    Map map;

    public Game() {

        canvas = new Canvas();

    }

    public void init() {

        renderer = new Renderer(canvas);

        map = new Map(17, 9, 0.1, 13, this);

    }

    public void run() {

        while (canvas.preRender()) {

            this.update();

            this.render();

            canvas.postRender();

        }

    }

    public void update() {

        InputHandler.update();

        if (InputHandler.getKeyState(GLFW_KEY_ESCAPE) == InputHandler.KEY_RELEASE)
            glfwSetWindowShouldClose (getWindow(), true);

        map.update();

    }

    public void render() {

        map.render(renderer);

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

}
