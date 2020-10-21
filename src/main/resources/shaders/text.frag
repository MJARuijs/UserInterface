#version 450

in vec2 passTexCoords;

uniform sampler2D textureAtlas;
uniform vec4 color;

out vec4 outColor;

void main() {
    outColor = vec4(color.rgb, texture(textureAtlas, passTexCoords).a);
}
