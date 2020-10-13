package userinterface.items.backgrounds

import graphics.samplers.ClampMode
import graphics.samplers.SampleFilter
import graphics.samplers.Sampler
import graphics.shaders.ShaderProgram
import graphics.textures.TextureMap
import math.Color
import userinterface.UIColor
import userinterface.effects.Effect

class TexturedBackground(val textureMap: TextureMap, private var backgroundColor: Color? = null, private var overlayColor: Color? = null, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: Color = Color()) : Background(cornerRadius, outline, outlineColor) {
    
    constructor(textureMap: TextureMap, backgroundColor: UIColor? = null, overlayColor: UIColor?, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: UIColor = UIColor.TRANSPARENT) : this(textureMap, backgroundColor?.color, overlayColor?.color, cornerRadius, outline, outlineColor.color)
    
    private val sampler = Sampler(0, SampleFilter.LINEAR, SampleFilter.NEAREST, ClampMode.EDGE, true)

    override fun applyEffects(effects: ArrayList<Effect>) {

    }

    override fun setProperties(shaderProgram: ShaderProgram) {
        super.setProperties(shaderProgram)
        if (backgroundColor != null) {
            shaderProgram.set("color", backgroundColor!!)
            shaderProgram.set("hasBackground", true)
        } else {
            shaderProgram.set("hasBackground", false)
        }
        if (overlayColor != null) {
            shaderProgram.set("hasOverlay", true)
            shaderProgram.set("overlayColor", overlayColor!!)
        } else {
            shaderProgram.set("hasOverlay", false)
        }
        shaderProgram.set("textured", true)
        shaderProgram.set("sampler", sampler.index)
        sampler.bind(textureMap)
    }
}