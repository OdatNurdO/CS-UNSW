package com.martejj.minesweeper.input;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {

    public static final char KEY_RELEASE = GLFW_RELEASE;
    public static final char KEY_PRESS = GLFW_PRESS;
    public static final char KEY_HOLD = 2; // GLFW Doesnt seem to define this :/
    public static final char KEY_NOTHING = 3;

    private static char[] keys;

    private static Stack<Integer> releasedKeys;
    private static Stack<Integer> pressedKeys;

    public static final char BUTTON_RELEASE = GLFW_RELEASE;
    public static final char BUTTON_PRESS = GLFW_PRESS;
    public static final char BUTTON_HOLD = 2;
    public static final char BUTTON_NOTHING = 3;

    private static char[] buttons;

    private static Stack<Integer> releasedButtons;
    private static Stack<Integer> pressedButtons;

    private static double mouseX, mouseY;

    public static void setupHandler(long window) {

        // Setup key input

        keys = new char[GLFW_KEY_LAST];

        // We use this to store the keys just released so we can give them the custom
        releasedKeys = new Stack<>();

        pressedKeys = new Stack<>();

        // Set all the keys to NONE
        for (int i = 0; i < keys.length; i++) keys[i] = KEY_NOTHING;

        glfwSetKeyCallback(window, new GLFWKeyCallbackI() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {

                // We buffer these into a stack so we dont get duplicate events
                if (action == KEY_RELEASE)
                    releasedKeys.push(key);
                else if (action == KEY_PRESS)
                    pressedKeys.push(key);

                keys[key] = (char) action;

            }
        });

        // Setup mouse input

        buttons = new char[GLFW_MOUSE_BUTTON_LAST];

        releasedButtons = new Stack<>();

        pressedButtons = new Stack<>();

        for (int i = 0; i < keys.length; i++) keys[i] = KEY_NOTHING;

        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {

                // We buffer these into a stack so we dont get duplicate events
                if (action == BUTTON_RELEASE)
                    releasedButtons.push(button);
                else if (action == KEY_PRESS)
                    pressedButtons.push(button);

                buttons[button] = (char) action;

            }
        });

        // Setup mouse coordinates

        glfwSetCursorPosCallback(window, new GLFWCursorPosCallbackI() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        });

    }

    public static void update() {

        // Set each key just released from RELEASED to NOTHING
        while (!releasedKeys.empty()) keys[releasedKeys.pop()] = KEY_NOTHING;

        // Set each key just pressed from PRESSED to HOLD
        while (!pressedKeys.empty()) keys[pressedKeys.pop()] = KEY_HOLD;

        // Set each button just released from RELEASED to NOTHING
        while (!releasedButtons.empty()) buttons[releasedButtons.pop()] = BUTTON_NOTHING;

        // Set each button just pressed from PRESSED to HOLD
        while (!pressedButtons.empty()) buttons[pressedButtons.pop()] = BUTTON_HOLD;

        glfwPollEvents();

    }

    public static char getButtonState(int button) { return buttons[button]; }

    public static char getKeyState(int key) { return keys[key]; }

    public static double getMouseX() { return mouseX; }

    public static double getMouseY() { return mouseY; }

}
