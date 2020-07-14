package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

abstract class Constraint {

    fun apply(itemPosition: ItemPosition, parentPosition: ItemPosition, siblings: ArrayList<Item>) = apply(itemPosition.translation, itemPosition.scale, parentPosition.translation, parentPosition.scale, siblings)

    abstract fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition

    fun toLeftOf(id: String) {

    }

}