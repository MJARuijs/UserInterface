package userinterface.items

import graphics.Quad
import devices.Mouse
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.constraints.ConstraintSet
import userinterface.effects.Effect
import userinterface.items.backgrounds.Background

open class Item(val id: String, private val constraintSet: ConstraintSet, var background: Background) {

    private val children = ArrayList<Item>()
    private var quad = Quad()

    val hoverEffects = ArrayList<Effect>()

    var baseBackground = background

    var baseTranslation = Vector2()
        private set

    var baseScale = Vector2()
        private set

    var translation = Vector2()
        private set

    var scale = Vector2()
        private set

    fun init(parentTranslation: Vector2 = Vector2(), parentScale: Vector2, parentChildren: ArrayList<Item>) {
        val data = constraintSet.apply(parentTranslation, parentScale, parentChildren)
        baseTranslation = data.translation
        baseScale = data.scale
        translation = data.translation
        scale = data.scale

        children.forEach { child -> child.init(translation, scale, children) }
    }

    fun add(item: Item) {
        children += item
    }

    fun addHoverEffect(effect: Effect) {
        hoverEffects += effect
    }

    fun draw(shaderProgram: ShaderProgram) {
        shaderProgram.set("translation", translation)
        shaderProgram.set("scale", scale)
        background.setProperties(shaderProgram)
        quad.draw()
        children.forEach { child -> child.draw(shaderProgram) }
    }

    open fun update(mouse: Mouse, aspectRatio: Float) {
        if (isHovered(mouse, aspectRatio)) {
            hoverEffects.forEach { effect ->
                val itemData = effect.applyOn(this)
                translation = itemData.translation
                scale = itemData.scale
                background = itemData.background
            }
        } else {
            translation = baseTranslation
            scale = baseScale
            background = baseBackground
        }

        children.forEach { child -> child.update(mouse, aspectRatio) }
    }

    fun isHovered(mouse: Mouse, aspectRatio: Float): Boolean {
        return isMouseOnButton(mouse, aspectRatio)
    }

    fun isMouseOnButton(mouse: Mouse, aspectRatio: Float): Boolean {
        val minX = (translation.x - scale.x)/aspectRatio
        val maxX = (translation.x + scale.x)/aspectRatio
        val minY = translation.y - scale.y
        val maxY = translation.y + scale.y

        val scaledMouseX = mouse.x * 2.0f
        val scaledMouseY = mouse.y * 2.0f

        if (scaledMouseX < minX || scaledMouseX > maxX) {
            return false
        }
        if (scaledMouseY < minY || scaledMouseY > maxY) {

            return false
        }
        return true
    }

    fun destroy() {
        quad.destroy()
    }

}