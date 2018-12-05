package com.martejj.minesweeper.graphics;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Model {

    // OpenGL ID for vertices in memory
    private int vID;

    // OpenGL ID for indices in memory
    private int iID;

    private int drawCount;

    // GL_* mode, e.g. GL_TRIANGLES for polygons
    private int mode;

    public Model(float[] vertices, int[] indices, int mode) {

        this.mode = mode;

        drawCount = indices.length;

        // Bind vID and attach it to the vertices
        vID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vID);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL_STATIC_DRAW);

        // Bind iID and attach it to the indices
        iID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, createIntBuffer(indices), GL_STATIC_DRAW);

        // Clear the bindings so they arent accidently edited/rendered later
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void render() {

        glEnableClientState(GL_VERTEX_ARRAY);

        // Bind the indicies and verticies we put in memory earlier

        glBindBuffer(GL_ARRAY_BUFFER, vID);
        glVertexPointer(3, GL_FLOAT, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iID);
        // Draw the triangles described by the vertices and indices
        glDrawElements(this.mode, drawCount, GL_UNSIGNED_INT, 0);

        // Clear the bindings so they are not edited
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Needs to be called so we do not leak memory
     */

    public void delete() {

        glDeleteBuffers(vID);
        glDeleteBuffers(iID);

    }

    public static FloatBuffer createFloatBuffer(float[] floats) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(floats.length);
        buffer.put(floats);
        buffer.flip();
        return buffer;

    }

    public static IntBuffer createIntBuffer(int[] ints) {

        IntBuffer buffer = BufferUtils.createIntBuffer(ints.length);
        buffer.put(ints);
        buffer.flip(); // Needed - maybe to do with stack.
        return buffer;

    }
}