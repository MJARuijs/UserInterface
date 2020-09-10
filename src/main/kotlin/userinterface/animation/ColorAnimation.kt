package userinterface.animation

import math.Color
import userinterface.items.Item
import userinterface.items.backgrounds.ColoredBackground
import kotlin.math.abs

class ColorAnimation(duration: Float, private val changeToColor: Color, item: Item) : Animation(item) {

    private val rSpeed: Float
    private val gSpeed: Float
    private val bSpeed: Float
    private val aSpeed: Float

    init {
        val currentColor = (item.background as ColoredBackground).color
        rSpeed = (changeToColor.r - currentColor.r) / duration
        gSpeed = (changeToColor.g - currentColor.g) / duration
        bSpeed = (changeToColor.b - currentColor.b) / duration
        aSpeed = (changeToColor.a - currentColor.a) / duration
    }

    override fun apply(deltaTime: Float): Boolean {
//        for (item in items) {
            if (item.background is ColoredBackground) {
                val currentColor = (item.background as ColoredBackground).color

                val rIncrease = deltaTime * rSpeed
                val gIncrease = deltaTime * gSpeed
                val bIncrease = deltaTime * bSpeed
                val aIncrease = deltaTime * aSpeed

                if (abs(currentColor.r - changeToColor.r) < rIncrease) {
                    currentColor.r = changeToColor.r
                } else {
                    currentColor.r += rIncrease
                }

                if (abs(currentColor.g - changeToColor.g) < gIncrease) {
                    currentColor.g = changeToColor.g
                } else {
                    currentColor.g += gIncrease
                }

                if (abs(currentColor.b - changeToColor.b) < gIncrease) {
                    currentColor.b = changeToColor.b
                } else {
                    currentColor.b += bIncrease
                }

                if (abs(currentColor.a - changeToColor.a) < gIncrease) {
                    currentColor.a = changeToColor.a
                } else {
                    currentColor.a += aIncrease
                }

                (item.background as ColoredBackground).color = currentColor
                if (currentColor == changeToColor) {
                    return true
                }
                return false
            }
        throw IllegalArgumentException("Couldn't animate background color, as the background is a texture!")
    }
}