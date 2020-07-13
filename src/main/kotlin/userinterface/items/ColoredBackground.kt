package userinterface.items

import graphics.shaders.ShaderProgram
import math.Color

class ColoredBackground(var color: Color) : Background {

    override fun setProperties(shaderProgram: ShaderProgram) {
        shaderProgram.set("color", color)
        shaderProgram.set("textured", false)
    }

}