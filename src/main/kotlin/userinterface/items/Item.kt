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
    
    lateinit var icon: SVGIcon
    
    init {
        if (requiredIds().contains(id)) {
            println("UI ERROR: item with id: $id is dependent of itself")
        }
    }

    open fun draw(shaderProgram: ShaderProgram) {
        shaderProgram.set("isIcon", false)
//        println("$id ${min(getScale().x, getScale().y)}")

        shaderProgram.set("translation", constraints.translation())
        shaderProgram.set("scale", constraints.scale())
        background.setProperties(shaderProgram)

        quad.draw()
        if (this::icon.isInitialized) {
//            icon.translation = getTranslation()
//            icon.size = min(getScale().x, getScale().y)
//            icon.setScale(min(getScale().x, getScale().y))
//            icon.size = 0.3f
            icon.draw(shaderProgram)
        }
        children.forEach { child ->
            child.draw(shaderProgram)
        }
    }

    fun destroy() {
        quad.destroy()
    }
}