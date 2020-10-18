package userinterface.items

import devices.Button
import devices.Mouse
import userinterface.UniversalParameters
import userinterface.items.backgrounds.Background
import userinterface.layout.constraints.ConstraintSet

open class UIButton(id: String, constraints: ConstraintSet, private var onClick: () -> Unit = {}, private val text: String = "", background: Background = UniversalParameters.BUTTON_BACKGROUND) : Item(id, constraints, background) {

    private var isClicked = false
    private var isHovered = false

    init {
        if (text.isNotBlank()) {

        }
    }

    fun setOnClick(function: () -> Unit) {
        onClick = function
    }
    
    override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        if (super.update(mouse, aspectRatio, deltaTime)) {
            return true
        }

        isHovered = isHovered(mouse, aspectRatio)

        if (isHovered && (mouse.isPressed(Button.LEFT) || mouse.isPressed(Button.LEFT))) {
            onClick()
            isClicked = true
        } else if (isHovered) {

        } else {

        }

        if (isClicked && (mouse.isReleased(Button.RIGHT) || mouse.isReleased(Button.LEFT))) {
            isClicked = false
        }

        if (isHovered || isClicked) {
            return true
        }
        return false
    }

    private fun isHovered(mouse: Mouse, aspectRatio: Float): Boolean {
        return isMouseOnButton(mouse, aspectRatio)
    }

    private fun isMouseOnButton(mouse: Mouse, aspectRatio: Float): Boolean {
        val minX = (getTranslation().x - getScale().x) / aspectRatio
        val maxX = (getTranslation().x + getScale().x) / aspectRatio
        val minY = getTranslation().y - getScale().y
        val maxY = getTranslation().y + getScale().y

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