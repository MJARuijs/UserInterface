package userinterface

import devices.Mouse
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.animation.Animation
import userinterface.items.Item
import userinterface.window.UIWindow

class UserInterface(private val aspectRatio: Float) {

    private val shaderProgram = ShaderProgram.load("shaders/ui.vert", "shaders/ui.frag")
    private val items = ArrayList<Item>()
    private val animations = ArrayList<Animation>()
    private val pages = ArrayList<UIPage>()
    private val windows = ArrayList<UIWindow>()

    private var start = 0L

    fun init() {
        items.forEach { item -> item.init(Vector2(), Vector2(1.0f, 1.0f), items) }
        windows.forEach { window -> window.init(Vector2(), Vector2(1.0f, 1.0f), ArrayList()) }
    }

    operator fun plusAssign(page: UIPage) {
        pages += page
    }

    operator fun plusAssign(window: UIWindow) {
        windows += window
    }

    operator fun plusAssign(newItem: Item) {
        if (items.any { item -> item.id == newItem.id }) {
            println("Duplicate ID error: there already exists an item with id: ${newItem.id}")
        } else {
            items += newItem
            println("added item: ${newItem.id}")
        }
    }

    operator fun plusAssign(animation: Animation) {
        animations += animation
    }

    fun showPage(name: String) {

    }

    fun showWindow(name: String) {
        windows.forEach { window ->
            window.shouldShow = window.id == name
            println("Should show window")
        }
    }

    fun hideWindow(name: String) {
        windows.forEach { window ->
            if (window.id == name) {
                window.shouldShow = false
            }
        }
    }

    fun draw(windowWidth: Int, windowHeight: Int) {
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)
        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)
        shaderProgram.start()
        shaderProgram.set("aspectRatio", aspectRatio)
        shaderProgram.set("viewPort", Vector2(windowWidth, windowHeight))
        for (window in windows) {
            if (window.shouldShow) {
                window.draw(shaderProgram)
            }
        }
//        items.forEach { item -> item.draw(shaderProgram) }
        shaderProgram.stop()
        GraphicsContext.enable(GraphicsOption.DEPTH_TESTING)
        GraphicsContext.disable(GraphicsOption.ALPHA_BLENDING)
    }

    fun update(mouse: Mouse, deltaTime: Float) {
        items.forEach { item -> item.update(mouse, aspectRatio) }
        val removableAnimations = ArrayList<Animation>()

        animations.forEach { animation ->
            if (animation.apply(deltaTime)) {
                println(System.currentTimeMillis() - start)
                removableAnimations += animation
            }
        }

        animations.removeAll(removableAnimations)
    }

    fun destroy() {
        items.forEach { item -> item.destroy() }
    }

}