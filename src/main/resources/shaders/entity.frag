#version 450

struct AmbientLight {
    vec4 color;
};

struct DirectionalLight {
    vec4 color;
    vec3 direction;
};

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float shininess;
};

const float AMBIENT_STRENGTH = 0.2f;

in vec4 worldPosition;
in vec3 passNormal;

uniform AmbientLight ambient;
uniform DirectionalLight sun;
uniform Material material;

uniform vec3 cameraPosition;

out vec4 outColor;

vec4 computeAmbientColor() {
    return material.ambient * ambient.color * AMBIENT_STRENGTH;
}

vec4 computeDiffuseColor() {
    vec3 lightDirection = normalize(sun.direction);
    float brightness = clamp(dot(normalize(passNormal), lightDirection), 0.0, 1.0f);
    return brightness * material.diffuse * sun.color;
}

vec4 computeSpecularColor() {
    vec3 normal = normalize(passNormal);
    vec3 lightDirection = normalize(sun.direction);
    vec3 toCameraVector = normalize(cameraPosition - worldPosition.xyz);

    vec3 lightReflectionDirection = 2 * (dot(lightDirection, normal)) * normal - lightDirection;

    float brightness = clamp(pow(dot(toCameraVector, lightReflectionDirection), material.shininess), 0.0f, 1.0f);
    return brightness * material.specular * sun.color;
}

void main() {
    outColor = computeAmbientColor() + computeDiffuseColor() + computeSpecularColor();
}
