#version 450 core

const float SMOOTH_DISTANCE = 0.001f;

in vec2 position;
in vec2 passTexCoords;

uniform sampler2D sampler;
uniform vec4 color;
uniform vec4 overlayColor;
uniform vec4 outlineColor;
uniform float outline;

uniform bool textured;
uniform bool hasBackground;
uniform bool hasOverlay;

uniform float cornerRadius;

uniform float aspectRatio;
uniform vec2 viewPort;

uniform vec2 scale;
uniform vec2 translation;

out vec4 outColor;

vec4 checkCorner(vec2 currentPoint, float x, float y, float scaledCornerRadius, vec4 outColor) {
    float distance = distance(currentPoint, vec2(x, y));
    float minScale = min(scale.x, scale.y);
    float scaledOutline = outline * 2 * minScale;

    if (distance > scaledCornerRadius) {
        if (cornerRadius > 0.0f) {
            outColor.a = 0.0f;
        }
    } else if (distance > scaledCornerRadius - scaledOutline) {
        outColor = outlineColor;
    }

    return outColor;
}

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

    if (cornerRadius > 0.0f) {
        vec2 currentPoint = gl_FragCoord.xy;
        currentPoint.x /= viewPort.x;
        currentPoint.y /= viewPort.y;
        currentPoint.x = (currentPoint.x * 2.0) - 1.0;
        currentPoint.y = (currentPoint.y * 2.0) - 1.0;

        currentPoint.x *= aspectRatio;

        float minScale = min(scale.x, scale.y);
        float scaledCornerRadius = cornerRadius / 90.0;
        scaledCornerRadius *= minScale;

        float leftBound = (translation.x - scale.x + scaledCornerRadius);
        float topBound = translation.y + scale.y - scaledCornerRadius;
        float rightBound = (translation.x + scale.x - scaledCornerRadius);
        float bottomBound = translation.y - scale.y + scaledCornerRadius;

        bool outOfLeftBound = false;
        bool outOfTopBound = false;
        bool outOfRightBound = false;
        bool outOfBottomBound = false;

        if (currentPoint.x > rightBound) {
            outOfRightBound = true;
        }
        if (currentPoint.x < leftBound) {
            outOfLeftBound = true;
        }
        if (currentPoint.y > topBound) {
            outOfTopBound = true;
        }
        if (currentPoint.y < bottomBound) {
            outOfBottomBound = true;
        }

        float scaledOutline = outline *2 * minScale;

        if (outOfTopBound) {
            if (outOfLeftBound) {
                outColor = checkCorner(currentPoint, leftBound, topBound, scaledCornerRadius, outColor);
            } else if (outOfRightBound) {
                outColor = checkCorner(currentPoint, rightBound, topBound, scaledCornerRadius, outColor);
            }
        }

        if (outOfBottomBound) {
            if (outOfLeftBound) {
                outColor = checkCorner(currentPoint, leftBound, bottomBound, scaledCornerRadius, outColor);
            } else if (outOfRightBound) {
                outColor = checkCorner(currentPoint, rightBound, bottomBound, scaledCornerRadius, outColor);
            }
        }

        if (outline > 0.0f) {
            float scaledOutline = outline * 2 * minScale;

            if (outOfRightBound && !outOfTopBound && !outOfBottomBound) {
                if (currentPoint.x > translation.x + scale.x - scaledOutline) {
                    outColor = outlineColor;
                }
            }

            if (outOfLeftBound && !outOfTopBound && !outOfBottomBound) {
                if (currentPoint.x < translation.x - scale.x + scaledOutline) {
                    outColor = outlineColor;
                }
            }

            if (outOfTopBound && !outOfLeftBound && !outOfRightBound) {
                if (currentPoint.y > translation.y + scale.y - scaledOutline) {
                    outColor = outlineColor;
                }
            }

            if (outOfBottomBound && !outOfLeftBound && !outOfRightBound) {
                if (currentPoint.y < translation.y - scale.y + scaledOutline) {
                    outColor = outlineColor;
                }
            }
        }
    } else if (outline > 0.0f) {
        float minScale = min(scale.x, scale.y);

        float scaledOutline = outline * 2 * minScale;
        vec2 currentPoint = gl_FragCoord.xy;
        currentPoint.x /= viewPort.x;
        currentPoint.y /= viewPort.y;
        currentPoint.x = (currentPoint.x * 2.0) - 1.0;
        currentPoint.y = (currentPoint.y * 2.0) - 1.0;

        currentPoint.x *= aspectRatio;

        if (currentPoint.x > translation.x + scale.x - scaledOutline) {
            outColor = outlineColor;
        }

        if (currentPoint.x < translation.x - scale.x + scaledOutline) {
            outColor = outlineColor;
        }

        if (currentPoint.y > translation.y + scale.y - scaledOutline) {
            outColor = outlineColor;
        }

        if (currentPoint.y < translation.y - scale.y + scaledOutline) {
            outColor = outlineColor;
        }
    }
}