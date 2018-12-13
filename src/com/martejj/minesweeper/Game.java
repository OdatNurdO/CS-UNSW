package com.martejj.minesweeper;

import com.martejj.minesweeper.graphics.*;
import com.martejj.minesweeper.map.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Game {

    private Canvas canvas;

    private Renderer renderer;

    Map map;

    private double mouseX, mouseY;

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

    }

    Texture texture;
    Model model;

    public void render() {

        //renderer.shader.setUniform("sampler", 0);
        //renderer.drawModel(100, 100, 110, 100, 0, model);
        map.render(renderer);
        //model.render();

        //renderer.drawRectangle(100, 100, 60, 60, Colour.RED, 0);
        //map.render(renderer);

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
