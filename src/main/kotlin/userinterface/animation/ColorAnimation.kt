package userinterface.animation

import math.Color
import math.vectors.Vector4
import userinterface.MovableUIContainer
import userinterface.items.backgrounds.ColorType
import userinterface.items.backgrounds.ColoredBackground
import kotlin.math.abs

class ColorAnimation(val duration: Float, private val changeToColor: Color, private val colorType: ColorType, item: MovableUIContainer, onFinish: () -> Unit = {}) : Animation(item, onFinish) {

    private val rSpeed: Float
    private val gSpeed: Float
    private val bSpeed: Float
    private val aSpeed: Float

    init {
        val currentColor = if (colorType == ColorType.BACKGROUND_COLOR) {
            (item.background as ColoredBackground).color
        } else {
            (item.background as ColoredBackground).outlineColor
        }
        rSpeed = (changeToColor.r - currentColor.r) / duration
        gSpeed = (changeToColor.g - currentColor.g) / duration
        bSpeed = (changeToColor.b - currentColor.b) / duration
        aSpeed = (changeToColor.a - currentColor.a) / duration
    }

    override fun apply(deltaTime: Float): Boolean {
        if (item.background is ColoredBackground) {
            if (colorType == ColorType.BACKGROUND_COLOR) {
                (item.background as ColoredBackground).color = increaseValues(Color((item.background as ColoredBackground).color), deltaTime)
                if ((item.background as ColoredBackground).color == changeToColor) {
                    return true
                }
            } else if (colorType == ColorType.OUTLINE_COLOR) {
                (item.background as ColoredBackground).outlineColor = increaseValues(Color((item.background as ColoredBackground).outlineColor), deltaTime)
                if ((item.background as ColoredBackground).outlineColor == changeToColor) {
                    return true
                }
            }
            return false
        }
        throw IllegalArgumentException("Couldn't animate background color, as the background is a texture!")
    }

    private fun increaseValues(currentColor: Color, deltaTime: Float): Color {
        val increaseValues = Vector4(
            deltaTime * rSpeed,
            deltaTime * gSpeed,
            deltaTime * bSpeed,
            deltaTime * aSpeed
        )

        for (i in 0 until 4) {
            if (abs(currentColor[i] - changeToColor[i]) < abs(increaseValues[i])) {
                currentColor[i] = changeToColor[i]
            } else {
                currentColor[i] = currentColor[i] + increaseValues[i]
            }
        }
        return currentColor
    }
}

