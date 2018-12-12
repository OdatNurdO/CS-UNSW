package com.martejj.minesweeper.graphics;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Model {

    // OpenGL ID for vertices, texture and indice coordinates in memory
    private int vID, iID, tID = 0;

    private int drawCount;

    // GL_* mode, e.g. GL_TRIANGLES for polygons
    private int mode;

    Texture texture = null;

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

    public Model(float[] vertices, int[] indices, float[] texcoords, int mode) {

        this(vertices, indices, mode);

        setTexCoords(texcoords);

    }

    public Model(float[] vertices, int[] indices, float[] texcoords, Texture texture, int mode) {

        this(vertices, indices, texcoords, mode);

        this.setTexture(texture);

    }

    public void render() {

        glEnableVertexAttribArray(Shader.VERTICES_ID);

        // Bind the indicies and verticies we put in memory earlier

        glBindBuffer(GL_ARRAY_BUFFER, vID);

        // Todo look at normalised
        glVertexAttribPointer(Shader.VERTICES_ID, 3, GL_FLOAT, false, 0, 0);

        if (texture != null) {

            // Tell opengl were giving it a texture and enable the attribute for texture coordinates
            glEnable(GL_TEXTURE_2D);
            glEnableVertexAttribArray(Shader.TEXTURES_ID);

            // Bind the texture coordinate data
            glBindBuffer(GL_ARRAY_BUFFER, tID);
            glVertexAttribPointer(Shader.TEXTURES_ID, 2, GL_FLOAT, false, 0, 0);

            // Bind the texture data
            texture.bind();

        }

        // Draw the triangles described by the vertices and indices
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iID);
        glDrawElements(this.mode, drawCount, GL_UNSIGNED_INT, 0);

        // Clear the bindings so they are not edited
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        if (texture != null) {

            glDisable(GL_TEXTURE_2D);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glDisableVertexAttribArray(Shader.TEXTURES_ID);

            texture.unbind();

        }

        glDisableVertexAttribArray(Shader.VERTICES_ID);

    }

    /**
     * Needs to be called so we do not leak memory
     */

    public void delete() {

        glDeleteBuffers(vID);
        glDeleteBuffers(iID);
        glDeleteBuffers(tID);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(float[] texcoords) {

        if (tID != 0)
            glDeleteBuffers(tID);

        tID = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, tID);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(texcoords), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

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
        buffer.flip();
        return buffer;

    }

}