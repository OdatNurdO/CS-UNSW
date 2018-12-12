package com.martejj.minesweeper.graphics;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static final int VERTICES_ID = 0;
    public static final int TEXTURES_ID = 1;

    private int program; // OpenGL binding for this shader
    private int vs; // Describes shape
    private int fs; // Describes colour

    private String filename;

    public Shader(String filename) {

        this.filename = filename;

        program = glCreateProgram();

        // Compile the vertex shader

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename + ".vs"));
        glCompileShader(vs);

        // Check if there was an error
        if(glGetShaderi(vs, GL_COMPILE_STATUS) == GLFW_FALSE) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        // Compile the fragment shader

        fs = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fs, readFile(filename + ".fs"));
        glCompileShader(fs);

        // Check if there was an error
        if(glGetShaderi(fs, GL_COMPILE_STATUS) == GLFW_FALSE) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        glAttachShader(program, vs);
        glAttachShader(program, fs);

        // We only ever link the initial vertices once. Afterwards we use the projection uniform to scale/transform/rotate it
        glBindAttribLocation(program, VERTICES_ID, "vertices");

        glBindAttribLocation(program, TEXTURES_ID, "textures");

        glLinkProgram(program);

        // Check for more errors

        if(glGetProgrami(program, GL_LINK_STATUS) == GLFW_FALSE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }

        glValidateProgram(program);

        if(glGetProgrami(program, GL_VALIDATE_STATUS) == GLFW_FALSE) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }

    }

    /**
     * @param name name of uniform/shader property to change
     * @param value int value to set it to
     */
    public void setUniform(String name, int value) {

        int location = glGetUniformLocation(program, name);

        checkValidUniformName(location, name, filename);

        glUniform1i(location, value);

    }

    /**
     * @param name name of uniform/shader property to change
     * @param value float value to set it to
     */
    public void setUniform(String name, float value) {

        int location = glGetUniformLocation(program, name);

        checkValidUniformName(location, name, filename);

        glUniform1f(location, value);

    }

    /**
     * @param name uniform/shader property to change
     * @param value matrix value to change it to
     */
    public void setUniform(String name, Matrix4f value) {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(4*4);

        value.get(buffer); // Load values into buffer

        int location = glGetUniformLocation(program, name);

        checkValidUniformName(location, name, filename);

        glUniformMatrix4fv(location, false, buffer);

    }

    public void checkValidUniformName(int location, String name, String filename) {

        if (location == -1) {

            System.out.println("Cannot find uniform " + name + " in " + filename);

            System.exit(1);
        }

    }

    public void setColour(float red, float green, float blue) {

        setUniform("red", red);
        setUniform("green", green);
        setUniform("blue",  blue);

    }

    public void setColour(Colour colour) {

        setUniform("red", colour.getRed());
        setUniform("green", colour.getGreen());
        setUniform("blue",  colour.getBlue());

    }

    /**
     * Call when intending rendering this shader
     */
    public void bind() {

        glUseProgram(program);

    }

    private String readFile(String filename) {

        StringBuilder string = new StringBuilder();

        BufferedReader reader;

        try {

            reader = new BufferedReader(new FileReader(new File("./shaders/" + filename + ".glsl")));
            String line;

            while ((line = reader.readLine()) != null) {
                string.append(line);
                string.append("\n"); // As reader ignores new lines
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return string.toString();

    }
}