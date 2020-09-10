package userinterface.animation

import userinterface.items.Item

abstract class Animation(val item: Item) {

    abstract fun apply(deltaTime: Float): Boolean

}