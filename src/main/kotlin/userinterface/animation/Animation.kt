package userinterface.animation

import userinterface.MovableUIContainer

abstract class Animation(val item: MovableUIContainer, val onFinish: () -> Unit = {}) {
    
    abstract fun apply(deltaTime: Float): Boolean

}