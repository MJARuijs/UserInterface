package userinterface

import graphics.Quad
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.backgrounds.Background

class UIPage(private val name: String, private val background: Background, private val items: ArrayList<Item> = ArrayList()) : UIContainer() {

    private var quad = Quad()

    init {
        children.forEach { child -> child.init(Vector2(), Vector2(1.0f, 1.0f), children) }
    }

    fun draw(shaderProgram: ShaderProgram) {
        background.setProperties(shaderProgram)
        quad.draw()
        children.forEach { child -> child.draw(shaderProgram) }
    }

    operator fun plusAssign(item: Item) {
        add(item, item.requiredIds)
    }

    fun destroy() {
        quad.destroy()
        items.forEach { item -> item.destroy() }
    }

}