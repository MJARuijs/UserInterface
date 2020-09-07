package userinterface.animation

import math.vectors.Vector2
import userinterface.items.Item
import kotlin.math.abs

class TransitionAnimation(duration: Float, transitionTo: Vector2, val item: Item) : Animation() {

    private val xSpeed = (transitionTo.x) / duration
    private val ySpeed = (transitionTo.y) / duration

    private val endTranslation = item.translation + transitionTo

    override fun apply(deltaTime: Float): Boolean {
        val currentTranslation = item.translation

        val xIncrease = deltaTime * xSpeed
        val yIncrease = deltaTime * ySpeed

        if (abs(currentTranslation.x - endTranslation.x) < xIncrease) {
            currentTranslation.x = endTranslation.x
        } else {
            currentTranslation.x += xIncrease
        }

        if (abs(currentTranslation.y - endTranslation.y) < yIncrease) {
            currentTranslation.y = endTranslation.y
        } else {
            currentTranslation.y += yIncrease
        }

        item.translation = currentTranslation

        if (currentTranslation == endTranslation) {
            return true
        }

        return false
    }
}