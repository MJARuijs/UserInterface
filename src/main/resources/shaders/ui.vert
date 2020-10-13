#version 450 core

layout(location = 0) in vec2 inPosition;

uniform vec2 translation;
uniform vec2 scale;
uniform bool isIcon;
uniform float size;
uniform float aspectRatio;

out vec2 position;
out vec2 passTexCoords;

void main() {
    if (isIcon) {

        position = translation;
        position.x /= aspectRatio;
        position += inPosition * size;
//        position /= aspectRatio;
//        position *= size;
        gl_Position = vec4(position, 0, 1);
        return;
    }
    passTexCoords = (inPosition + 1.0) / 2.0;

    position = translation + scale * inPosition.xy;
    position.x /= aspectRatio;

    gl_Position = vec4(position, 0, 1);
}
