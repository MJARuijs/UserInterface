package userinterface.items

import devices.Mouse
import math.Color
import userinterface.constraints.ConstraintSet
import userinterface.effects.Effect
import userinterface.items.backgrounds.Background
import userinterface.items.backgrounds.ColoredBackground

class Button(id: String, constraintSet: ConstraintSet, background: Background) : Item(id, constraintSet, background) {

    override fun update(mouse: Mouse, aspectRatio: Float) {
        super.update(mouse, aspectRatio)

    }

//    private fun isClicked(mouse: Mouse, aspectRatio: Float): Boolean {
//        if ()
//    }


}