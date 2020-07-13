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
    private val itemMap = HashMap<String, Item>()

    fun init() {
        items.forEach { item -> item.init(Vector2(), Vector2(aspectRatio, 1.0f), items) }
    }

    fun add(newItem: Item) {
        if (items.any { item -> item.id == newItem.id }) {
            println("Duplicate ID error: there already exists an item with id: ${newItem.id}")
        } else {
            items += newItem
        }
    }

    fun findItem(id: String): Item? {
        return items.find { item -> item.id == id }
    }

    fun draw() {
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)
        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)
        shaderProgram.start()
        shaderProgram.set("aspectRatio", aspectRatio)
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