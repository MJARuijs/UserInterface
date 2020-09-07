package userinterface.items

import graphics.Quad
import devices.Mouse
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.constraints.ConstraintSet
import userinterface.items.backgrounds.Background

open class Item(val id: String, val constraints: ConstraintSet, var background: Background) {

    private val children = ArrayList<Item>()
    private val quad = Quad()

    var baseBackground = background

    var translation = Vector2()
    var scale = Vector2()

    var requiredIds = constraints.requiredIds

    open fun init(parentTranslation: Vector2 = Vector2(), parentScale: Vector2, parentChildren: ArrayList<Item> = ArrayList()) {
        val data = constraints.apply(parentTranslation, parentScale, parentChildren)
        translation = data.translation
        scale = data.scale

        children.forEach { child -> child.init(translation, scale, children) }
    }

    fun add(item: Item) {
        children += item
    }

    fun hasDependencies() = requiredIds.isNotEmpty()

    fun findById(id: String): Item? {
        return children.find { item -> item.id == id }
    }

    open fun draw(shaderProgram: ShaderProgram) {
        shaderProgram.set("translation", translation)
        shaderProgram.set("scale", scale)
        background.setProperties(shaderProgram)
        quad.draw()
        children.forEach { child -> child.draw(shaderProgram) }
    }

    open fun update(mouse: Mouse, aspectRatio: Float) {
        children.forEach { child -> child.update(mouse, aspectRatio) }
    }

    fun destroy() {
        quad.destroy()
    }
}