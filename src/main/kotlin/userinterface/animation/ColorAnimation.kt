package userinterface.animation

import math.Color
import math.vectors.Vector4
import userinterface.MovableUIContainer
import userinterface.UIColor
import userinterface.items.backgrounds.ColorType
import userinterface.items.backgrounds.ColoredBackground
import userinterface.items.backgrounds.TexturedBackground
import kotlin.math.abs

class ColorAnimation(val duration: Float, private val changeToColor: Color, private val colorType: ColorType, item: MovableUIContainer, onFinish: () -> Unit = {}) : Animation(item, onFinish) {

    private val rSpeed: Float
    private val gSpeed: Float
    private val bSpeed: Float
    private val aSpeed: Float

    init {
        val currentColor = if (colorType == ColorType.BACKGROUND_COLOR) {
            if (item.background is ColoredBackground) {
                (item.background as ColoredBackground).color
            } else {
                (item.background as TexturedBackground).overlayColor
            }
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
                val currentColor = (item.background as ColoredBackground).color.copy()
     
                (item.background as ColoredBackground).color = increaseValues(currentColor, deltaTime)
                
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
        } else {
            if (colorType == ColorType.BACKGROUND_COLOR) {
                (item.background as TexturedBackground).overlayColor = increaseValues(Color((item.background as TexturedBackground).overlayColor), deltaTime)
                if ((item.background as TexturedBackground).overlayColor == changeToColor) {
                    return true
                }
            } else if (colorType == ColorType.OUTLINE_COLOR) {
                (item.background as TexturedBackground).outlineColor = increaseValues(Color((item.background as TexturedBackground).outlineColor), deltaTime)
                if ((item.background as TexturedBackground).outlineColor == changeToColor) {
                    return true
                }
            }
            return false
        }
    }

    private fun increaseValues(currentColor: Color, deltaTime: Float): Color {
        val increaseValues = Vector4(
            deltaTime * rSpeed,
            deltaTime * gSpeed,
            deltaTime * bSpeed,
            deltaTime * aSpeed
        )

//        var newR = if (abs(currentColor.r - changeToColor.r) < abs(increaseValues[0])) {
//            changeToColor.r
//        } else {
//            currentColor.r + increaseValues[0]
//        }
//        var newG = if (abs(currentColor.g - changeToColor.g) < abs(increaseValues[1])) {
//            changeToColor.g
//        } else {
//            currentColor.g + increaseValues[1]
//        }
//        var newB = if (abs(currentColor.b - changeToColor.b) < abs(increaseValues[2])) {
//            changeToColor.b
//        } else {
//            currentColor.b + increaseValues[2]
//        }
//        var newA = if (abs(currentColor.a - changeToColor.a) < abs(increaseValues[3])) {
//            changeToColor.a
//        } else {
//            currentColor.a + increaseValues[3]
//        }
//        return Color(newR, newG, newB, newA)
        
        
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

