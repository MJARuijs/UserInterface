package userinterface.items.backgrounds

import graphics.shaders.ShaderProgram
import math.Color
import userinterface.effects.Effect
import userinterface.items.backgrounds.Background

class ColoredBackground(var color: Color) : Background {

    override fun applyEffects(effects: ArrayList<Effect>) {

    }

    override fun setProperties(shaderProgram: ShaderProgram) {
        shaderProgram.set("color", color)
        shaderProgram.set("textured", false)
    }
}