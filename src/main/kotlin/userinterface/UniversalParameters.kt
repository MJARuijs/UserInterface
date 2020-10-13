package userinterface

import graphics.textures.ImageMap
import math.Color
import resources.images.ImageCache
import userinterface.items.backgrounds.ColoredBackground
import userinterface.items.backgrounds.TexturedBackground

object UniversalParameters {

    private val PRIMARY_COLOR = UIColor.BLUE
    
    val SWITCH_THUMB_OFF_BACKGROUND = ColoredBackground(Color(185, 185, 185), 90f)
    val SWITCH_THUMB_ON_BACKGROUND = ColoredBackground(Color(65, 162, 239), 90f)

    val SWITCH_TRACK_OFF_BACKGROUND = ColoredBackground(UIColor.TRANSPARENT, 90f, 0.09f, Color(74, 74, 74))
    val SWITCH_TRACK_ON_BACKGROUND = ColoredBackground(UIColor.TRANSPARENT, 90f, 0.09f, Color(33, 73, 107))
    
    val BUTTON_BACKGROUND = ColoredBackground(PRIMARY_COLOR, 10.0f, 0.09f, UIColor.WHITE)
    val BUTTON_ON_HOVER_BACKGROUND = ColoredBackground(PRIMARY_COLOR, 0.0f, 0.09f, UIColor.WHITE)
    
    val PROGRESS_BAR_BACKGROUND = ColoredBackground(UIColor.GREY, 0.0f)
    val PROGRESS_BAR_COLOR = PRIMARY_COLOR
    val PROGRESS_BAR_PAUSED_COLOR = UIColor.YELLOW_LIGHT
    val PROGRESS_BAR_COMPLETED_COLOR = UIColor.GREEN_LIGHT
    
    val CLOSE_BUTTON_BACKGROUND = TexturedBackground(ImageMap(ImageCache.get("textures/userinterface/close_button.png")), overlayColor = UIColor.WHITE)
    
    val CHECK_BOX_UNCHECKED_BACKGROUND = ColoredBackground(UIColor.TRANSPARENT, 15f, 0.1f, UIColor.WHITE)
    val CHECK_BOX_CHECKED_BACKGROUND = TexturedBackground(ImageMap(ImageCache.get("textures/userinterface/check_mark.png")), PRIMARY_COLOR, UIColor.WHITE, 15f, 0.1f, outlineColor =  UIColor.WHITE)
    
}