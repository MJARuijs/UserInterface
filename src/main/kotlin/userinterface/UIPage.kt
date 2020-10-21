package userinterface

import graphics.shaders.ShaderProgram
import userinterface.items.backgrounds.Background
import userinterface.layout.UILayout

class UIPage(id: String, private val background: Background, var shouldShow: Boolean = false) : UIContainer(id) {

    init {
        children.forEach { child -> child.position() }
    }

    fun draw(shaderProgram: ShaderProgram, iconProgram: ShaderProgram, textProgram: ShaderProgram, aspectRatio: Float) {
        shaderProgram.set("allowedToOverdraw", true)
        background.setProperties(shaderProgram)
        quad.draw()
        children.forEach { child -> child.draw(shaderProgram, iconProgram, textProgram, aspectRatio, null) }
    }
    
    override fun apply(layout: UILayout, duration: Float) {
        children.forEach { child ->
            child.apply(layout, duration)
        }
    }
    
}