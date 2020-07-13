package userinterface.items

import devices.Mouse
import userinterface.constraints.ConstraintSet

class Button(id: String, constraintSet: ConstraintSet, background: Background) : Item(id, constraintSet, background) {

    override fun update(mouse: Mouse, aspectRatio: Float) {
        super.update(mouse, aspectRatio)

    }

    private fun isMouseOnButton(mouse: Mouse, aspectRatio: Float): Boolean {
        val minX = (translation.x - scale.x)/aspectRatio
        val maxX = (translation.x + scale.x)/aspectRatio
        val minY = translation.y - scale.y
        val maxY = translation.y + scale.y

        val scaledMouseX = mouse.x * 2.0f
        val scaledMouseY = mouse.y * 2.0f

        if (scaledMouseX < minX || scaledMouseX > maxX) {
            return false
        }
        if (scaledMouseY < minY || scaledMouseY > maxY) {
            return false
        }
        return true
    }

}