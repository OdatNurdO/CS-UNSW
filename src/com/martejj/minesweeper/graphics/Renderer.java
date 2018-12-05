package com.martejj.minesweeper.graphics;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;

public class Renderer {

    public Model rectangle;

    public Shader shader;

    Canvas canvas;

    private int initialWidth;
    private int initialHeight;

    private Stack<Matrix4f> transformStack;

    private Matrix4f currentTransformation;

    public Renderer(Canvas canvas) {

        this.canvas = canvas;

        this.initialHeight = getCurrentHeight();

        this.initialWidth = getCurrentWidth();

        // Vertices and indices for a rectangle

        float[] vertices = {
                -1f,   1f,   0, // TOP LEFT
                1f,   1f,   0, // TOP RIGHT
                1f,  -1f,   0, // BOTTOM RIGHT
                -1f,  -1f,   0  // BOTTOM LEFT
        };

        int[] indices = {
                0,  1,  2,
                0,  2,  3
        };

        shader = new Shader("shader");

        rectangle = new Model(vertices, indices, GL11.GL_TRIANGLES);

        // Setup the transformation stack. TODO

        transformStack = new Stack<>();

        currentTransformation = new Matrix4f().ortho2D(0, getInitialWidth(), 0, getInitialHeight());

        transformStack.push(currentTransformation);


        // Code for testing polygon factory
        /*
        ArrayList<Vector2d> vectors = new ArrayList<>();
        vectors.add(new Vector2d(1,1));
        vectors.add(new Vector2d(1,-1));
        vectors.add(new Vector2d(-1,1));
        vectors.add(new Vector2d(-1,-1));
        Model square = ConvexPolygonModelFactory.makeModel(vectors);
        */

    }

    /**
     * Draws a filled rectangle
     * @param width of the rectangle
     * @param height of the rectangle
     * @param x of the centre of the rectangle
     * @param y of the centre of the rectangle
     * @param colour RGB colour value
     * @param angle rotation in radians in the x-y plane anticlockwise
     */

    public void drawRectangle(double width, double height, double x, double y, Colour colour, double angle) {

        // pushTransform(new Matrix4f().identity().scale(x, y, 1));
        Matrix4f projection = new Matrix4f()
                .ortho2D((float) -x, (float) (this.getInitialWidth() - x), (float) -y, (float) (this.getInitialHeight() - y));

        // Need to divide by two as we scale by this number in both up and down and left and right directions
        Matrix4f scale = new Matrix4f().scaling((float) width/2, (float) height/2, 1);

        Matrix4f rotation = new Matrix4f().rotation(new AxisAngle4f().rotate((float) angle));

        shader.bind();

        setUniforms(shader, colour, projection, scale, rotation);

        rectangle.render();

        // popTransform();

    }

    public void drawModel(double width, double height, double x, double y, Colour colour, double angle, Model model) {

        Matrix4f projection = new Matrix4f()
                .ortho2D((float) -x, (float) (this.getInitialWidth() - x), (float) -y, (float) (this.getInitialHeight() - y));

        Matrix4f scale = new Matrix4f().scaling((float) width, (float) height, 1);

        Matrix4f rotation = new Matrix4f().rotation(new AxisAngle4f().rotate((float) angle));

        setUniforms(shader, colour, projection, scale, rotation);

        model.render();

    }

    public void drawModel(Colour colour, Model model) {

        Matrix4f projection = new Matrix4f()
                .ortho2D(0, (float) this.getInitialWidth(), 0, (float) this.getInitialHeight());

        Matrix4f scale = new Matrix4f().identity();

        Matrix4f rotation = new Matrix4f().identity();

        setUniforms(shader, colour, projection, scale, rotation);

        model.render();
    }

    public void setUniforms(Shader shader, Colour colour, Matrix4f projection, Matrix4f scale, Matrix4f rotation) {

        shader.bind();

        shader.setColour(colour);

        shader.setUniform("projection", projection);
        shader.setUniform("scale", scale);
        shader.setUniform("rotation", rotation);

    }

    public int getCurrentWidth() {

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(canvas.getWindow(), w, h);

        return w.get(0);

    }

    public int getCurrentHeight() {

        IntBuffer w = BufferUtils.createIntBuffer(1);
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(canvas.getWindow(), w, h);

        return h.get(0);

    }

    public int getInitialWidth() {
        return initialWidth;
    }

    public int getInitialHeight() {
        return initialHeight;
    }

    public void pushTransform(Matrix4f matrix) {

        currentTransformation.mul(matrix);
        transformStack.push(matrix);

    }

    public void popTransform() {

        currentTransformation.mul(transformStack.pop().invert());

    }

}