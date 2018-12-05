package com.martejj.minesweeper.graphics;

public class Colour {

    public static final Colour RED = new Colour(1, 0, 0);
    public static final Colour GREEN = new Colour(0, 1, 0);
    public static final Colour BLUE = new Colour(0, 0, 1);

    float red;
    float green;
    float blue;

    public Colour(float red, float green, float blue) {

        this.red = red;
        this.blue = blue;
        this.green = green;

    }

    public Colour() {

        red = 0;
        green = 0;
        blue = 0;

    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public void setColour(float red, float green, float blue) {

        this.red = red;
        this.green = green;
        this.blue = blue;

    }
}