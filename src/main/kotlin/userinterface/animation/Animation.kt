package userinterface.animation

abstract class Animation {

    abstract fun apply(deltaTime: Float): Boolean

}