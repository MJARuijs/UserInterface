package userinterface.effects

import userinterface.items.Item

interface Effect {

    fun applyOn(item: Item)

    fun removeFrom(item: Item)

}