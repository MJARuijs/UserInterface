package userinterface.items

import graphics.shaders.ShaderProgram
import userinterface.MovableUIContainer
import userinterface.UniversalParameters
import userinterface.layout.constraints.ConstraintSet
import userinterface.items.backgrounds.Background
import userinterface.svg.SVGIcon
import kotlin.math.min

open class Item(id: String, constraints: ConstraintSet, background: Background = UniversalParameters.BUTTON_BACKGROUND) : MovableUIContainer(id, constraints, background) {

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

    open fun draw(shaderProgram: ShaderProgram, iconProgram: ShaderProgram, aspectRatio: Float, parent: MovableUIContainer?) {
        shaderProgram.set("translation", constraints.translation())
        shaderProgram.set("scale", constraints.scale())
        
        if (parent != null) {
            shaderProgram.set("allowedToOverdraw", parent.isChildBoundless(id))
            shaderProgram.set("parentTranslation", parent.getTranslation())
            shaderProgram.set("parentScale", parent.getScale())
        }


        
        background.setProperties(shaderProgram)

        quad.draw()
        if (this::icon.isInitialized) {
            icon.draw(iconProgram, aspectRatio)
            shaderProgram.start()
        }
        
        children.forEach { child ->
            child.draw(shaderProgram, iconProgram, aspectRatio, this)
        }

    }
}