package userinterface.effects

import userinterface.items.Item
import userinterface.items.ItemData

interface Effect {

    fun applyOn(item: Item): ItemData

}