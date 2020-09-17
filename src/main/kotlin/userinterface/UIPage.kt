package userinterface

import graphics.Quad
import graphics.shaders.ShaderProgram
import userinterface.items.Item
import userinterface.items.ItemDimensions
import userinterface.items.backgrounds.Background
import userinterface.layout.UILayout

class UIPage(id: String, private val background: Background) : UIContainer(id) {

    private var quad = Quad()

    init {
        children.forEach { child -> child.position() }
    }

    fun draw(shaderProgram: ShaderProgram) {
        background.setProperties(shaderProgram)
        quad.draw()
        children.forEach { child -> child.draw(shaderProgram) }
    }

    fun destroy() {
        quad.destroy()
        children.forEach { item -> item.destroy() }
    }
    
    override fun apply(layout: UILayout, duration: Float, parentDimensions: ItemDimensions?) {
        children.forEach { child ->
            child.apply(layout, duration, parentDimensions)
        }
    }
    
}