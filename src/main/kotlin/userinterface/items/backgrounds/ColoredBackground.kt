package userinterface.items.backgrounds

import graphics.shaders.ShaderProgram
import math.Color
import userinterface.UIColor

class ColoredBackground(var color: Color, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: Color = Color()) : Background(cornerRadius, outline, outlineColor) {
    
    constructor(color: UIColor, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: Color = Color()) : this((color.color), cornerRadius, outline, outlineColor)

    constructor(color: UIColor, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: UIColor) : this(color.color, cornerRadius, outline, outlineColor.color)

    constructor(color: Color, cornerRadius: Float = 0.0f, outline: Float = 0.0f, outlineColor: UIColor) : this(color, cornerRadius, outline, outlineColor.color)

    constructor(background: ColoredBackground) : this(background.color, background.cornerRadius, background.outline, background.outlineColor)
    
    override fun setProperties(shaderProgram: ShaderProgram) {
        super.setProperties(shaderProgram)
        shaderProgram.set("color", color)
        shaderProgram.set("textured", false)
    }
}