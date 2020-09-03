#version 450

layout (location = 0) in vec2 inPosition;
layout (location = 1) in vec2 inTexCoords;

uniform vec2 translation;
//uniform vec2 scale;

out vec2 passTexCoords;

void main() {
    passTexCoords = inTexCoords;
    gl_Position = vec4(inPosition + translation * vec2(2, -2.0), 0.0, 1.0);
}
