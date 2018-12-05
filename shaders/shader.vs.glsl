#version 120

attribute vec3 vertices;

uniform mat4 projection;
uniform mat4 scale;
uniform mat4 rotation;

void main() {

    gl_Position = projection * (rotation * (scale *  vec4(vertices, 1)));

}