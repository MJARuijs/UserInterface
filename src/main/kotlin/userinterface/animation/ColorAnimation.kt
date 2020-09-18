package userinterface.animation

import math.Color
import math.vectors.Vector4
import userinterface.MovableUIContainer
import userinterface.UniversalParameters
import userinterface.items.Item
import userinterface.items.backgrounds.ColoredBackground
import kotlin.math.abs

class ColorAnimation(val duration: Float, val changeToColor: Color, item: MovableUIContainer, onFinish: () -> Unit = {}) : Animation(item, onFinish) {

    private val rSpeed: Float
    private val gSpeed: Float
    private val bSpeed: Float
    private val aSpeed: Float

    init {
        println("Change to: ${changeToColor}")
        val currentColor = (item.background as ColoredBackground).color
        rSpeed = (changeToColor.r - currentColor.r) / duration
        gSpeed = (changeToColor.g - currentColor.g) / duration
        bSpeed = (changeToColor.b - currentColor.b) / duration
        aSpeed = (changeToColor.a - currentColor.a) / duration
    }

    override fun apply(deltaTime: Float): Boolean {
        if (item.background is ColoredBackground) {
            val currentColor = Color((item.background as ColoredBackground).color)

            (item.background as ColoredBackground).color = increaseValues(currentColor, deltaTime)
            println("CHECK: ${UniversalParameters.SWITCH_THUMB_OFF_BACKGROUND.color}")
            if ((item.background as ColoredBackground).color == changeToColor) {
                println("DONE")
                return true
            }
            return false
        }
        throw IllegalArgumentException("Couldn't animate background color, as the background is a texture!")
    }

    private fun increaseValues(currentColor: Color, deltaTime: Float): Color {
        val resultColor = Color()
        val increaseValues = Vector4(
            deltaTime * rSpeed,
            deltaTime * gSpeed,
            deltaTime * bSpeed,
            deltaTime * aSpeed
        )

        for (i in 0 until 4) {
            if (abs(currentColor[i] - changeToColor[i]) < abs(increaseValues[i])) {
                resultColor[i] = changeToColor[i]
            } else {
                resultColor[i] = currentColor[i] + increaseValues[i]
            }
        }
        return resultColor
    }
}