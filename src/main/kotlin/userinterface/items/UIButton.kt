package userinterface.items

import devices.Button
import devices.Mouse
import userinterface.layout.constraints.ConstraintSet
import userinterface.effects.Effect
import userinterface.items.backgrounds.Background

class UIButton(id: String, constraints: ConstraintSet, background: Background, private val onClick: () -> Unit = {}, private val text: String = "") : Item(id, constraints, background) {

    private val hoverEffects = ArrayList<Effect>()
    private val onClickEffects = ArrayList<Effect>()
    private var isClicked = false
    private var isHovered = false

    init {
        if (text.isNotBlank()) {

        }
    }

    override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        if (super.update(mouse, aspectRatio, deltaTime)) {
            return true
        }

        isHovered = isHovered(mouse, aspectRatio)

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

        if (isClicked && (mouse.isReleased(Button.RIGHT) || mouse.isReleased(Button.LEFT))) {
            isClicked = false
            onClickEffects.forEach { effect ->
                effect.removeFrom(this)
            }
        }

        if (isHovered || isClicked) {
            return true
        }
        return false
    }

    fun addHoverEffect(effect: Effect): UIButton {
        hoverEffects += effect
        return this
    }

    fun addOnClickEffect(effect: Effect): UIButton {
        onClickEffects += effect
        return this
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