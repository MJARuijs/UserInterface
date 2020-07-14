package userinterface.effects

import math.Color
import userinterface.items.backgrounds.ColoredBackground
import userinterface.items.Item
import userinterface.items.ItemData
import userinterface.items.backgrounds.TexturedBackground

class ColorEffect(val backgroundColor: Color, val overlayColor: Color = Color(0.0f, 0.0f, 0.0f, 0.0f)) : Effect {

    override fun applyOn(item: Item): ItemData {

        val itemData = ItemData(item.baseTranslation, item.baseScale, item.background)
        if (item.background is ColoredBackground) {
            itemData.background = ColoredBackground((item.background as ColoredBackground).color + backgroundColor)
        } else if (item.background is TexturedBackground) {
            val background = item.baseBackground as TexturedBackground
            itemData.background = TexturedBackground(background.textureMap, backgroundColor, overlayColor)
        }

        return itemData
    }

}