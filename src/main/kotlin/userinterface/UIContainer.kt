package userinterface

import devices.Mouse
import math.vectors.Vector2
import userinterface.items.Item
import java.util.concurrent.ConcurrentHashMap

open class UIContainer {

    private val postPonedItems = ConcurrentHashMap<Item, ArrayList<String>>()

    val children = ArrayList<Item>()

    operator fun plusAssign(item: Item) {
        add(item, item.requiredIds)
    }

    private fun add(item: Item, requiredIds: ArrayList<String>) {
        if (requiredIds.isNotEmpty()) {
            addItemWithDependencies(item, requiredIds)
        } else {
            add(item)
        }
    }

    private fun addItemWithDependencies(item: Item, requiredIds: ArrayList<String>) {
        children.forEach { child ->
            if (requiredIds.contains(child.id)) {
                requiredIds.remove(child.id)
            }
        }
        if (requiredIds.isEmpty()) {
            postPonedItems.remove(item)
            add(item)
        } else {
            postPonedItems[item] = requiredIds
        }
    }

    protected fun add(item: Item) {
        positionChild(item)

        for (postPonedItem in postPonedItems) {
            addItemWithDependencies(postPonedItem.key, postPonedItem.value)
        }
    }

    open fun positionChild(item: Item, translation: Vector2 = Vector2(), scale: Vector2 = Vector2(), childItems: ArrayList<Item> = ArrayList()) {
        item.position(translation, scale, ArrayList())
        children += item
    }

    open fun update(mouse: Mouse, aspectRatio: Float) {
        children.forEach { child -> child.update(mouse, aspectRatio) }
    }
}