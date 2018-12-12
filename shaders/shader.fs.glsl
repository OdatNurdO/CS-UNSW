#version 130

uniform sampler2D sampler;

uniform float red;
uniform float green;
uniform float blue;

varying vec2 texcoords;

void main() {

    gl_FragColor = texture(sampler, texcoords)* vec4(red, green, blue, 1.0);

}