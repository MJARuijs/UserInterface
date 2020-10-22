package userinterface

import devices.Mouse
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.window.UIWindow

class UserInterface(private val aspectRatio: Float) {

    private val shaderProgram = ShaderProgram.load("shaders/ui.vert", "shaders/ui.frag")
    private val iconProgram = ShaderProgram.load("shaders/icon.vert", "shaders/icon.frag")
    
    private val textProgram = ShaderProgram.load("shaders/text.vert", "shaders/text.frag")
    
    private val pages = ArrayList<UIPage>()
    private val windows = ArrayList<UIWindow>()

    private var showingId = ""
    
    operator fun plusAssign(page: UIPage) {
        pages += page
    }

    operator fun plusAssign(window: UIWindow) {
        windows += window
    }

    fun showPage(name: String) {
        if (pages.any { page -> page.id == name }) {
            pages.forEach { page ->
                page.shouldShow = page.id == name
            }
            showingId = name
        }
    }

    fun showWindow(name: String) {
        if (windows.any { window -> window.id == name }) {
            windows.forEach { window ->
                window.shouldShow = window.id == name
            }
            showingId = name
        }
    }

    fun hideWindows() {
        windows.forEach { window ->
            window.shouldShow = false
        }
    }

    fun isShowing(): Boolean {
        for (window in windows) {
            if (window.shouldShow) {
                return true
            }
        }
        for (page in pages) {
            if (page.shouldShow) {
                return true
            }
        }
        return false
    }

    fun draw(windowWidth: Int, windowHeight: Int) {
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)
        GraphicsContext.enable(GraphicsOption.ALPHA_BLENDING)
        shaderProgram.start()
        shaderProgram.set("aspectRatio", aspectRatio)
        shaderProgram.set("viewPort", Vector2(windowWidth, windowHeight))
        for (page in pages) {
            if (page.shouldShow) {
                page.draw(shaderProgram, iconProgram, textProgram, aspectRatio)
            }
        }
        for (window in windows) {
            if (window.shouldShow) {
                window.draw(shaderProgram, iconProgram, textProgram, aspectRatio)
            }
        }
        shaderProgram.stop()
        GraphicsContext.enable(GraphicsOption.DEPTH_TESTING)
        GraphicsContext.disable(GraphicsOption.ALPHA_BLENDING)
    }

    fun update(mouse: Mouse, deltaTime: Float) {
        windows.forEach { window ->
            if (window.shouldShow) {
                window.update(mouse, aspectRatio, deltaTime)
            }
        }
        
        pages.forEach { page ->
            if (page.shouldShow) {
                page.update(mouse, aspectRatio, deltaTime)
            }
        }

        if (showingId.isNotBlank()) {
            windows.forEach { window ->
                if (window.id == showingId && !window.shouldShow) {
                    showingId = ""
                    mouse.capture()
                }
            }
        }
    }

    fun destroy() {
        windows.forEach { window -> window.destroy() }
        pages.forEach { page -> page.destroy() }
    }
}