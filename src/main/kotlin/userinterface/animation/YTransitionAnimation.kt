package userinterface.animation

import math.vectors.Vector2
import userinterface.items.Item
import kotlin.math.abs

class YTransitionAnimation(duration: Float, translation: Float, item: Item, transitionType: TransitionType) : Animation(item) {
    
    private val speed: Float
    private val finalPoint: Float
    
    init {
        if (transitionType == TransitionType.MOVEMENT) {
            speed = translation / duration
            finalPoint = item.getTranslation().y + translation
        } else {
            speed = (translation - item.getTranslation().y) / duration
            finalPoint = translation
        }
    }
    
    override fun apply(deltaTime: Float): Boolean {
        val increase = deltaTime * speed
        
        if (abs(item.getTranslation().y - finalPoint) < abs(increase)) {
            item.place(Vector2(item.getTranslation().x, finalPoint))
            return true
        } else {
            item.translate(Vector2(0f, increase))
        }
        return false
    }
}