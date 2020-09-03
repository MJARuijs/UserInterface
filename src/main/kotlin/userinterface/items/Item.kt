package userinterface.items

import graphics.Quad
import devices.Mouse
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.constraints.ConstraintSet
import userinterface.items.backgrounds.Background
import userinterface.items.backgrounds.ColoredBackground
import userinterface.window.TitleBar
import userinterface.window.UIWindow

open class Item(val id: String, val constraints: ConstraintSet, var background: Background) {

    private val children = ArrayList<Item>()
    private var quad = Quad()

    var baseBackground = background

    var translation = Vector2()

    var scale = Vector2()

    open fun init(parentTranslation: Vector2 = Vector2(), parentScale: Vector2, parentChildren: ArrayList<Item>) {
        val data = constraints.apply(parentTranslation, parentScale, parentChildren)
        translation = data.translation
        scale = data.scale
        println("$id $scale")
        if (this is UIWindow && this.hasTitleBar()) {
            val titleBarHeight = this.getTitleBar().height

            val childScale = Vector2(scale.x, scale.y)
            childScale.y -= titleBarHeight * scale.y

            val childTranslation = Vector2(translation.x, translation.y)
            childTranslation.y -= titleBarHeight * scale.y

            children.forEach { child ->
                if (child is TitleBar) {
                    child.init(translation, scale, children)
                } else {
                    child.init(childTranslation, childScale, children)
                }
            }
        } else {
            children.forEach { child -> child.init(translation, scale, children) }
        }
    }

    fun add(item: Item) {
        children += item
    }

    fun findById(id: String): Item? {
        return children.find { item -> item.id == id }
    }

    open fun draw(shaderProgram: ShaderProgram) {
        if (id == "testButton1") {
//            println("$translation, $scale")
        }
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