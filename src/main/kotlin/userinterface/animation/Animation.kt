package userinterface.animation

import userinterface.MovableUIContainer
import userinterface.items.Item

abstract class Animation(val item: MovableUIContainer, val onFinish: () -> Unit = {}) {

    var startTime = 0L
    var started = false
    
    fun start() {
        startTime = System.currentTimeMillis()
        started = true
        println("Animation Started")
    }
    
    fun stop() {
        println("Animation took ${(System.currentTimeMillis() - startTime).toFloat() / 1000.0f} seconds")
    }
    
    abstract fun apply(deltaTime: Float): Boolean

}