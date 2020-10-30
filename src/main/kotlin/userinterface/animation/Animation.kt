package userinterface.animation

import userinterface.MovableUIContainer

abstract class Animation(val onFinish: () -> Unit = {}) {
    
    abstract fun apply(deltaTime: Float): Boolean

}