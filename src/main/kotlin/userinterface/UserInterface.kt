package userinterface

import devices.Mouse
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.items.Item

class UserInterface(private val aspectRatio: Float) {

    private val shaderProgram = ShaderProgram.load("shaders/ui.vert", "shaders/ui.frag")
    private val items = ArrayList<Item>()

    fun init() {
        items.forEach { item -> item.init(Vector2(), Vector2(1.0f, 1.0f), items) }
    }

    fun add(newItem: Item) {
        if (items.any { item -> item.id == newItem.id }) {
            println("Duplicate ID error: there already exists an item with id: ${newItem.id}")
        } else {
            items += newItem
        }
    }

    fun draw(windowWidth: Int, windowHeight: Int) {
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)
        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)
        shaderProgram.start()
        shaderProgram.set("aspectRatio", aspectRatio)
        shaderProgram.set("viewPort", Vector2(windowWidth, windowHeight))
        items.forEach { item -> item.draw(shaderProgram) }
        shaderProgram.stop()
        GraphicsContext.enable(GraphicsOption.DEPTH_TESTING)
        GraphicsContext.disable(GraphicsOption.ALPHA_BLENDING)
    }

    fun update(mouse: Mouse) {
        items.forEach { item -> item.update(mouse, aspectRatio) }
    }

    fun destroy() {
        items.forEach { item -> item.destroy() }
    }

}