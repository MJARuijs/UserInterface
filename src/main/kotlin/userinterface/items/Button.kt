package userinterface.items

import devices.Button
import devices.Mouse
import graphics.shaders.ShaderProgram
import userinterface.constraints.ConstraintSet
import userinterface.effects.Effect
import userinterface.items.backgrounds.Background

class Button(id: String, constraintSet: ConstraintSet, background: Background, private val onClick: () -> Unit = {}, private val text: String = "") : Item(id, constraintSet, background) {

    private val hoverEffects = ArrayList<Effect>()
    private val onClickEffects = ArrayList<Effect>()
    private var isClicked = false

    init {
        if (text.isNotBlank()) {

        }
    }

    override fun update(mouse: Mouse, aspectRatio: Float) {
        super.update(mouse, aspectRatio)

        val isHovered = isHovered(mouse, aspectRatio)

        if (isHovered && (mouse.isPressed(Button.LEFT) || mouse.isPressed(Button.LEFT))) {
            onClick()
            isClicked = true
            onClickEffects.forEach { effect ->
                effect.applyOn(this)
            }

        } else if (isHovered) {
            hoverEffects.forEach { effect ->
                effect.applyOn(this)
            }
        } else {
            hoverEffects.forEach { effect ->
                effect.removeFrom(this)
            }
        }

        if (isClicked && mouse.isReleased(Button.RIGHT)) {
            isClicked = false
            onClickEffects.forEach { effect ->
                effect.removeFrom(this)
            }
        }
    }

    fun addHoverEffect(effect: Effect) {
        hoverEffects += effect
    }

    fun addOnClickEffect(effect: Effect) {
        onClickEffects += effect
    }

    private fun isHovered(mouse: Mouse, aspectRatio: Float): Boolean {
        return isMouseOnButton(mouse, aspectRatio)
    }

    private fun isMouseOnButton(mouse: Mouse, aspectRatio: Float): Boolean {

        val minX = (translation.x - scale.x) / aspectRatio
        val maxX = (translation.x + scale.x) / aspectRatio
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