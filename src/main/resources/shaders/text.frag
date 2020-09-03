#version 450

in vec2 passTexCoords;

uniform sampler2D textureAtlas;

out vec4 outColor;

void main() {

    outColor = vec4(1.0, 0.0, 0.0, 1.0);
    outColor = vec4(1.0, 0.0, 0.0, texture(textureAtlas, passTexCoords).a);
}
