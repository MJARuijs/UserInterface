#version 450

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 inTexCoords;
layout (location = 2) in vec3 inNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 cameraPosition;

out vec4 worldPosition;
out vec3 passNormal;

void main() {
    worldPosition = model * vec4(inPosition, 1.0);
    passNormal = mat3(model) * inNormal;

    vec3 toCameraVector = normalize(cameraPosition - worldPosition.xyz);
    gl_Position = projection * view * worldPosition;
}
