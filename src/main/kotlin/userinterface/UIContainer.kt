package userinterface

import devices.Mouse
import graphics.Quad
import graphics.shaders.ShaderProgram
import userinterface.items.Item
import userinterface.layout.UILayout
import java.util.concurrent.ConcurrentHashMap

abstract class UIContainer(val id: String, private val layouts: ArrayList<UILayout> = ArrayList()) {

    private val postPonedItems = ConcurrentHashMap<Item, ArrayList<String>>()
    private val boundlessChildren = ArrayList<String>()
    
    protected val iconProgram = ShaderProgram.load("shaders/icon.vert", "shaders/icon.frag")
    protected val quad = Quad()
    protected val children = ArrayList<Item>()
    
    // TODO: Can this be done? (And then of course be removed from children)
//    init {
//        children.forEach { child -> child.position() }
//    }
    
    fun findById(id: String): Item? {
        return children.find { item -> item.id == id }
    }
    
    fun allowChildToIgnoreBounds(id: String) {
        boundlessChildren += id
    }
    
    fun isChildBoundless(id: String): Boolean {
        return boundlessChildren.contains(id)
    }
    
    open operator fun plusAssign(item: Item) {
        add(item, item.requiredIds())
    }
    
    operator fun plusAssign(layout: UILayout) {
        layouts += layout
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
        if (children.any { child -> child.id == item.id }) {
            return
        }

        positionChild(item)

        for (postPonedItem in postPonedItems) {
            addItemWithDependencies(postPonedItem.key, postPonedItem.value)
        }
    }

    open fun positionChild(item: Item) {
        item.position()
        children += item
    }

    open fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        children.forEach { child ->
            child.update(mouse, aspectRatio, deltaTime)
//            if (child.update(mouse, aspectRatio, deltaTime)) {
//                return true
//            }
        }
        return false
    }
    
    fun applyLayout(id: String, duration: Float) {
        val layout = layouts.find { layout -> layout.id == id }
        if (layout == null) {
            println("No layout with id: $id was found for UIContainer with id: ${this.id}..")
            return
        }
        
        apply(layout, duration)
    }
    
    open fun apply(layout: UILayout, duration: Float) {}
    
    fun destroy() {
        quad.destroy()
        children.forEach { item -> item.destroy() }
    }

}