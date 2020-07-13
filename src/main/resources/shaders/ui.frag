#version 450 core

uniform sampler2D sampler;
uniform vec4 color;
uniform vec4 overlayColor;
uniform bool textured;
uniform bool hasBackground;
uniform bool hasOverlay;

in vec2 passTexCoords;

out vec4 outColor;

void main() {
    if (textured) {
        vec4 textureColor = texture(sampler, passTexCoords);
        outColor = textureColor;

        if (hasBackground) {
            if (hasOverlay) {
                outColor = textureColor.a * overlayColor + (1.0 - textureColor.a) * color;
            } else {
                outColor.rgb = textureColor.a * textureColor.rgb + (1.0 - textureColor.a) * color.rgb;
            }
        } else {
            if (hasOverlay) {
                outColor.rgb = textureColor.a * overlayColor.rgb + (1.0 - textureColor.a) * textureColor.rgb;
            }
        }
    } else {
        outColor = color;
    }
}