#version 130

attribute vec3 vertices;

attribute vec2 textures;

varying vec2 texcoords;

uniform mat4 projection;
uniform mat4 scale;
uniform mat4 rotation;

void main() {

    texcoords = textures;

    gl_Position = projection * (rotation * (scale *  vec4(vertices, 1)));

}