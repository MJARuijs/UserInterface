package userinterface.animation

import math.vectors.Vector2
import userinterface.items.Item
import kotlin.math.abs

class XTransitionAnimation(private val duration: Float, translation: Float, item: Item, transitionType: TransitionType) : Animation(item) {
    
    private var speed = 0f
    private var finalPoint = 0f
    
    init {
        if (transitionType == TransitionType.MOVEMENT) {
            speed = translation / duration
            finalPoint = item.getTranslation().x - translation
        } else {
            speed = (translation - item.getTranslation().x) / duration
            finalPoint = translation
        }
    }
    
    fun changeGoal(translation: Float) {
        speed = translation / duration
        finalPoint = translation
    }
    
    override fun apply(deltaTime: Float): Boolean {
        val increase = deltaTime * speed
        
        if (abs(item.getTranslation().x - finalPoint) < abs(increase)) {
            item.place(Vector2(finalPoint, item.getTranslation().y))
            println("done")
            return true
        } else {
            item.translate(Vector2(increase, 0f))
        }
        return false
    }
}