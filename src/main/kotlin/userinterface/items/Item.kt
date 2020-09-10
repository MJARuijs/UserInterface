package userinterface.items

import graphics.Quad
import graphics.shaders.ShaderProgram
import userinterface.MovableUIContainer
import userinterface.constraints.ConstraintSet
import userinterface.items.backgrounds.Background

open class Item(id: String, constraints: ConstraintSet, var background: Background) : MovableUIContainer(id, constraints) {

    private val quad = Quad()
    var isAnimating = false

    var baseBackground = background

    var requiredIds = constraints.requiredIds

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