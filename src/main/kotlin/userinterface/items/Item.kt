package userinterface.items

import graphics.Quad
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.UIContainer
import userinterface.constraints.ConstraintSet
import userinterface.items.backgrounds.Background

open class Item(val id: String, val constraints: ConstraintSet, var background: Background) : UIContainer() {

    private val quad = Quad()

    var baseBackground = background

    var translation = Vector2()
    var scale = Vector2()

    var requiredIds = constraints.requiredIds

    fun getPositionData() = ItemPosition(translation, scale)

    open fun position(parentTranslation: Vector2 = Vector2(), parentScale: Vector2, parentChildren: ArrayList<Item> = ArrayList()) {
        val itemPosition = constraints.apply(parentTranslation, parentScale, parentChildren)
        translation = itemPosition.translation
        scale = itemPosition.scale

        children.forEach { child ->
            child.position(translation, scale, children)
        }
    }

    fun translate(translation: Vector2) {
        this.translation += translation
        children.forEach { child ->
            child.translate(translation)
        }
    }

    fun findById(id: String): Item? {
        return children.find { item -> item.id == id }
    }

    open fun draw(shaderProgram: ShaderProgram) {
        shaderProgram.set("translation", translation)
        shaderProgram.set("scale", scale)
        background.setProperties(shaderProgram)
        quad.draw()
        children.forEach { child ->
            child.draw(shaderProgram)
        }
    }

    fun destroy() {
        quad.destroy()
    }
}