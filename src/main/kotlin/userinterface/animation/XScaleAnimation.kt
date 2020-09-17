package userinterface.animation

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.items.Item
import kotlin.math.abs

class XScaleAnimation(duration: Float, toScale: Float, item: MovableUIContainer) : Animation(item) {
    
    private var speed = 0f
    private var finalScale = 0f
    
    init {
        speed = (toScale - item.getScale().x) / duration
        finalScale = toScale
    }
    
    override fun apply(deltaTime: Float): Boolean {
        val increase = deltaTime * speed
        
        if (abs(item.getScale().x - finalScale) < abs(increase)) {
            item.setScale(Vector2(finalScale, item.getScale().y))
            return true
        } else {
            item.addToScale(Vector2(increase, 0.0f))
        }
        
        return false
    }
}