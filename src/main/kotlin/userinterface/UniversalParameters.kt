package userinterface

import math.Color
import userinterface.items.backgrounds.ColoredBackground

object UniversalParameters {

    val SWITCH_THUMB_OFF_BACKGROUND = ColoredBackground(Color(185, 185, 185), 90f)
    val SWITCH_THUMB_ON_BACKGROUND = ColoredBackground(Color(65, 162, 239), 90f)

    val SWITCH_TRACK_OFF_BACKGROUND = ColoredBackground(UIColor.TRANSPARENT, 90f, 0.09f, Color(74, 74, 74))
    val SWITCH_TRACK_ON_BACKGROUND = ColoredBackground(UIColor.TRANSPARENT, 90f, 0.09f, Color(33, 73, 107))
    
    val BUTTON_BACKGROUND = ColoredBackground(UIColor.BLUE, 0.0f, 0.09f, UIColor.WHITE)
    
    val PROGRESS_BAR_BACKGROUND = ColoredBackground(UIColor.GREY, 0.0f)
    val PROGRESS_BAR_COLOR = UIColor.BLUE
    val PROGRESS_BAR_PAUSED_COLOR = UIColor.YELLOW_LIGHT
    val PROGRESS_BAR_COMPLETED_COLOR = UIColor.GREEN_LIGHT

}