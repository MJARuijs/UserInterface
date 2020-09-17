package userinterface.animation

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.items.Item
import kotlin.math.abs

class YScaleAnimation(duration: Float, toScale: Float, item: MovableUIContainer) : Animation(item) {
    
    private var speed = 0f
    private var finalScale = 0f
    
    init {
        speed = (toScale - item.getScale().y) / duration
        finalScale = toScale
    }
    
    override fun apply(deltaTime: Float): Boolean {
        val increase = deltaTime * speed
        
        if (abs(item.getScale().y - finalScale) < abs(increase)) {
            item.setScale(Vector2(item.getScale().x, finalScale))
            return true
        } else {
            item.addToScale(Vector2(0f, increase))
        }
        return false
    }
}