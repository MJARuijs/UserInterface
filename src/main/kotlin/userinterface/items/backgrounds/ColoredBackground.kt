package userinterface.items.backgrounds

import graphics.shaders.ShaderProgram
import math.Color
import userinterface.effects.Effect

class ColoredBackground(var color: Color, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: Color = Color()) : Background(cornerRadius, outline, outlineColor) {

    override fun applyEffects(effects: ArrayList<Effect>) {

    }

    override fun setProperties(shaderProgram: ShaderProgram) {
        super.setProperties(shaderProgram)
        shaderProgram.set("color", color)
        shaderProgram.set("textured", false)
    }
}