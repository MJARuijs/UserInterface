package userinterface.effects

import math.Color
import userinterface.items.backgrounds.ColoredBackground
import userinterface.items.Item
import userinterface.items.ItemData
import userinterface.items.backgrounds.Background
import userinterface.items.backgrounds.TexturedBackground

class ColorEffect(private val backgroundColor: Color, private val overlayColor: Color = Color(0.0f, 0.0f, 0.0f, 0.0f)) : Effect {

    override fun applyOn(item: Item) {
        if (item.background is ColoredBackground) {
            val background = item.baseBackground as ColoredBackground
            item.background = ColoredBackground(background.color + backgroundColor)
        } else if (item.background is TexturedBackground) {
            val background = item.baseBackground as TexturedBackground
            item.background = TexturedBackground(background.textureMap, backgroundColor, overlayColor)
        }
    }

    override fun removeFrom(item: Item) {
        item.background = item.baseBackground
//        if (item.background is ColoredBackground) {
//            val background = item.baseBackground as ColoredBackground
//            item.background = item.baseBackground
//        } else if (item.background is TexturedBackground) {
//            val background = item.baseBackground as TexturedBackground
//            item.background = TexturedBackground(background.textureMap, backgroundColor, overlayColor)
//        }
    }

}