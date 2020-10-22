package userinterface.items

import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.UIColor
import userinterface.UIContainer
import userinterface.UniversalParameters
import userinterface.layout.constraints.ConstraintSet
import userinterface.items.backgrounds.Background
import userinterface.items.backgrounds.ColoredBackground
import userinterface.items.backgrounds.TexturedBackground
import userinterface.svg.SVGIcon
import kotlin.math.min

open class Item(id: String, constraints: ConstraintSet, background: Background = UniversalParameters.ITEM_BACKGROUND()) : MovableUIContainer(id, constraints, background) {

    private lateinit var icon: SVGIcon
    
    val baseBackgroundColor = when (background) {
        is ColoredBackground -> background.color
        is TexturedBackground -> background.overlayColor
        else -> UIColor.TRANSPARENT.color
    }
    
    init {
        if (requiredIds().contains(id)) {
            println("UI ERROR: item with id: $id is dependent of itself")
        }
    }
    
    fun setIcon(icon: SVGIcon) {
        this.icon = icon
    }
    
    override fun position(parent: UIContainer?, duration: Float) {
        super.position(parent, duration)
        if (this::icon.isInitialized) {
            icon.translate(getTranslation())
            icon.setScale(min(getScale().x, getScale().y))
        }
    }

    open fun draw(shaderProgram: ShaderProgram, iconProgram: ShaderProgram, textProgram: ShaderProgram, aspectRatio: Float, parent: MovableUIContainer?) {
        shaderProgram.set("translation", getTranslation())
        shaderProgram.set("scale", getScale())
        
//        if (parent != null) {
            shaderProgram.set("allowedToOverdraw", parent?.isChildBoundless(id) ?: true)
            shaderProgram.set("parentTranslation", parent?.getTranslation() ?: Vector2())
            shaderProgram.set("parentScale", parent?.getScale() ?: Vector2(1.0f, 1.0f))
//        }
        
        background.setProperties(shaderProgram)

        quad.draw()
        if (this::icon.isInitialized) {
            icon.draw(iconProgram, aspectRatio)
            shaderProgram.start()
        }
        
        children.forEach { child ->
            child.draw(shaderProgram, iconProgram, textProgram, aspectRatio, this)
        }
    }
}