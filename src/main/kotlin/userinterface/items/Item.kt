package userinterface.items

import Quad
import devices.Mouse
import graphics.shaders.ShaderProgram
import graphics.textures.TextureMap
import math.Color
import math.vectors.Vector2
import userinterface.constraints.ConstraintSet

open class Item(val id: String, private val constraintSet: ConstraintSet, private var background: Background) {

    private val children = ArrayList<Item>()
    private var quad = Quad()

    var translation = Vector2()
        private set

    var scale = Vector2()
        private set

    fun init(parentTranslation: Vector2 = Vector2(), parentScale: Vector2, parentChildren: ArrayList<Item>) {
        val data = constraintSet.apply(parentTranslation, parentScale, parentChildren)
        translation = data.translation
        scale = data.scale

        children.forEach { child -> child.init(translation, scale, children) }
    }

    fun add(item: Item) {
        children += item
    }

    fun draw(shaderProgram: ShaderProgram) {
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