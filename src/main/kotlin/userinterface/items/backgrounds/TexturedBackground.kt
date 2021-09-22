package userinterface.items.backgrounds

import graphics.samplers.ClampMode
import graphics.samplers.SampleFilter
import graphics.samplers.Sampler
import graphics.shaders.ShaderProgram
import graphics.textures.ImageMap
import math.Color
import resources.images.ImageCache
import userinterface.UIColor

class TexturedBackground(private val texturePath: String, private var backgroundColor: Color? = null, var overlayColor: Color = UIColor.TRANSPARENT.color, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: Color = Color()) : Background(cornerRadius, outline, outlineColor) {
    
    private val textureMap = ImageMap(ImageCache.get(texturePath))
    
    constructor(texturePath: String, backgroundColor: UIColor? = null, overlayColor: UIColor = UIColor.TRANSPARENT, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: UIColor = UIColor.TRANSPARENT) : this(texturePath, backgroundColor?.color, overlayColor.color, cornerRadius, outline, outlineColor.color)
    
    private val sampler = Sampler(0, SampleFilter.LINEAR, SampleFilter.NEAREST, ClampMode.EDGE, true)

    override fun setProperties(shaderProgram: ShaderProgram) {
        super.setProperties(shaderProgram)
        if (backgroundColor != null) {
            shaderProgram.set("color", backgroundColor!!)
            shaderProgram.set("hasBackground", true)
        } else {
            shaderProgram.set("hasBackground", false)
        }
        shaderProgram.set("hasOverlay", true)
        shaderProgram.set("overlayColor", overlayColor)
        shaderProgram.set("textured", true)
        shaderProgram.set("sampler", sampler.index)
        sampler.bind(textureMap)
    }
    
    fun fromString(string: String): TexturedBackground {
        val startIndex = string.indexOf('(') + 1
        val endIndex = string.indexOf(')')
        val valuesString = string.substring(startIndex, endIndex)
        val values = valuesString.split(',')
        val texturePath = values[0]
        return TexturedBackground(texturePath, Color.fromString(values[1]), Color.fromString(values[2]), values[3].toFloat(), values[4].toFloat(), Color.fromString(values[5]))
    }
    
    override fun toString(): String {
        return "TexturedBackground($texturePath,$backgroundColor,$overlayColor,$cornerRadius,$outline,$outlineColor)"
    }
}