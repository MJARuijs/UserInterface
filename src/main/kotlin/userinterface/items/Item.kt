package userinterface.items

import graphics.Quad
import graphics.shaders.ShaderProgram
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintSet
import userinterface.items.backgrounds.Background

open class Item(id: String, constraints: ConstraintSet, background: Background) : MovableUIContainer(id, constraints, background) {

    private val quad = Quad()

    var baseBackground = background
    
    init {
        if (requiredIds().contains(id)) {
            println("UI ERROR: item with id: $id is dependent of itself")
        }
    }

    open fun draw(shaderProgram: ShaderProgram) {
        shaderProgram.set("translation", constraints.translation())
        shaderProgram.set("scale", constraints.scale())
        background.setProperties(shaderProgram)
        quad.draw()
        children.forEach { child ->
            child.draw(shaderProgram)
        }
    }

    fun destroy() {
        quad.destroy()
    }
}