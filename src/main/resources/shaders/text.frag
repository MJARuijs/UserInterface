#version 450

const float characterWidth = 0.5f;
const float edgeWidth = 0.03f;

const float borderWidth = 0.3;
const float borderEdge = 0.4;

const vec2 offset = vec2(0.000, 0.000);

const vec3 outlineColor = vec3(0.2, 0.2, 0.2);

in vec2 passTexCoords;

uniform sampler2D textureAtlas;
uniform vec4 color;

out vec4 outColor;


void main() {
//    float distance = 1.0 - texture(textureAtlas, passTexCoords).a;
//    float alpha = 1.0f - smoothstep(characterWidth, characterWidth + edgeWidth, distance);
//
//    outColor = vec4(color.rgb, alpha);

    float distance = 1.0 - texture(textureAtlas, passTexCoords).a;
    float alpha = 1.0 - smoothstep(characterWidth, characterWidth + edgeWidth, distance);

    float distance2 = 1.0 - texture(textureAtlas, passTexCoords + offset).a;
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, color.rgb, alpha / overallAlpha);

    outColor = vec4(overallColor, overallAlpha);

}
