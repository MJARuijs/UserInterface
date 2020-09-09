package userinterface.animation

import math.vectors.Vector2
import userinterface.items.Item
import kotlin.math.abs

class TransitionAnimation(duration: Float, translation: Vector2, val item: Item) : Animation() {

    private val xSpeed = (translation.x) / duration
    private val ySpeed = (translation.y) / duration

    private val endTranslation = item.translation + translation

    private var xCompleted = false
    private var yCompleted = false

    init {
        if (xSpeed == 0.0f) {
            xCompleted = true
        }
        if (ySpeed == 0.0f) {
            yCompleted = true
        }
    }

    override fun apply(deltaTime: Float): Boolean {
        val currentTranslation = Vector2()

        val xIncrease = deltaTime * xSpeed
        val yIncrease = deltaTime * ySpeed

        if (!xCompleted) {
            if (abs(item.translation.x - endTranslation.x) < xIncrease) {
                currentTranslation.x = abs(item.translation.x - endTranslation.x)
                xCompleted = true
            } else {
                currentTranslation.x += xIncrease
            }
        }

        if (!yCompleted) {
            if (abs(item.translation.y - endTranslation.y) < abs(yIncrease)) {
                currentTranslation.y = abs(item.translation.y - endTranslation.y)
                yCompleted = true
            } else {
                currentTranslation.y += yIncrease
            }
        }

        item.translate(currentTranslation)

        if (xCompleted && yCompleted) {
            return true
        }

        return false
    }
}