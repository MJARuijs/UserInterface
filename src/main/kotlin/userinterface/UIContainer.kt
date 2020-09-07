package userinterface

import devices.Mouse
import math.vectors.Vector2
import userinterface.items.Item
import java.util.concurrent.ConcurrentHashMap

open class UIContainer {

    val children = ArrayList<Item>()
    private val postPonedItems = ConcurrentHashMap<Item, ArrayList<String>>()

    fun add(item: Item, requiredIds: ArrayList<String>) {
        if (requiredIds.isNotEmpty()) {
            addItemWithDependencies(item, requiredIds)
        } else {
            addItem(item)
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
            addItem(item)
        } else {
            postPonedItems[item] = requiredIds
        }
    }

    private fun addItem(item: Item) {
        initChild(item)

        for (postPonedItem in postPonedItems) {
            addItemWithDependencies(postPonedItem.key, postPonedItem.value)
        }
    }

    open fun initChild(item: Item, translation: Vector2 = Vector2(), scale: Vector2 = Vector2(), children: ArrayList<Item> = ArrayList()) {
        if (children.any { child -> child.id == item.id }) {
            return
        }

        item.init(translation, scale, children)
        children += item
    }

    open fun update(mouse: Mouse, aspectRatio: Float) {
        children.forEach { child -> child.update(mouse, aspectRatio) }
    }
}