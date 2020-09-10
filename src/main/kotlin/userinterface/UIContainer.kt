package userinterface

import devices.Mouse
import userinterface.items.Item
import userinterface.items.ItemDimensions
import java.util.concurrent.ConcurrentHashMap

open class UIContainer(val id: String) {

    private val postPonedItems = ConcurrentHashMap<Item, ArrayList<String>>()

    val children = ArrayList<Item>()
    
    fun findById(id: String): Item? {
        return children.find { item -> item.id == id }
    }
    
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

    open fun positionChild(item: Item) {
        item.position()
        children += item
    }

    open fun update(mouse: Mouse, aspectRatio: Float): Boolean {
        children.forEach { child ->
            if (child.update(mouse, aspectRatio)) {
                return true
            }
        }
        return false
    }
}