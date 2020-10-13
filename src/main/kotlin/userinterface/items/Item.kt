package userinterface.items

import graphics.Quad
import graphics.shaders.ShaderProgram
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintSet
import userinterface.items.backgrounds.Background
import userinterface.svg.SVGIcon
import kotlin.math.min

open class Item(id: String, constraints: ConstraintSet, background: Background) : MovableUIContainer(id, constraints, background) {

    private val quad = Quad()
    
    private lateinit var icon: SVGIcon
    
    init {
        if (requiredIds().contains(id)) {
            println("UI ERROR: item with id: $id is dependent of itself")
        }
    }
    
    fun setIcon(icon: SVGIcon) {
        this.icon = icon
    }
    
    override fun position(parent: MovableUIContainer?, duration: Float) {
        super.position(parent, duration)
        if (this::icon.isInitialized) {
            icon.translate(getTranslation())
            icon.setScale(min(getScale().x, getScale().y))
        }
    }

    open fun draw(shaderProgram: ShaderProgram, iconProgram: ShaderProgram, aspectRatio: Float) {
        shaderProgram.set("translation", constraints.translation())
        shaderProgram.set("scale", constraints.scale())
        background.setProperties(shaderProgram)

        quad.draw()
        if (this::icon.isInitialized) {
            icon.draw(iconProgram, aspectRatio)
            shaderProgram.start()
        }
        
        children.forEach { child ->
            child.draw(shaderProgram, iconProgram, aspectRatio)
        }

    }

    fun destroy() {
        quad.destroy()
    }
}