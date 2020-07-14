package userinterface.items.backgrounds

import graphics.shaders.ShaderProgram
import userinterface.effects.Effect

interface Background {

    fun applyEffects(effects: ArrayList<Effect>)

    fun setProperties(shaderProgram: ShaderProgram)

}