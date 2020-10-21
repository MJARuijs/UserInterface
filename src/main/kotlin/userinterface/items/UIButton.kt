package userinterface.items

import devices.Button
import devices.Mouse
import math.Color
import userinterface.MovableUIContainer
import userinterface.UIColor
import userinterface.UniversalParameters
import userinterface.animation.Animation
import userinterface.animation.effects.Effect
import userinterface.items.backgrounds.Background
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.CenterConstraint
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint
import userinterface.text.font.Font

open class UIButton(id: String, constraints: ConstraintSet, private var onClick: () -> Unit = {}, background: Background = UniversalParameters.BUTTON_BACKGROUND()) : Item(id, constraints, background) {

    private var isClicked = false
    private var isHovered = false
    private val hoverAnimations = ArrayList<Effect>()
    private val clickAnimations = ArrayList<Animation>()

    fun setOnClick(function: () -> Unit) {
        onClick = function
    }
    
    fun setText(text: String, color: Color, font: Font) {
        add(TextBox("${id}_text", ConstraintSet(
            CenterConstraint(ConstraintDirection.HORIZONTAL),
            CenterConstraint(ConstraintDirection.VERTICAL),
            RelativeConstraint(ConstraintDirection.HORIZONTAL, 1.0f),
            RelativeConstraint(ConstraintDirection.VERTICAL, 1.0f)
        ), text, color, font, 1f))
    }
    
    override fun position(parent: MovableUIContainer?, duration: Float) {
        super.position(parent, duration)
        if (id == "testButton2") {
            println("Button translation: ${getTranslation()}")
            println(findById("testButton2_text")!!.getTranslation())
        }
    }
    
    fun setText(text: String, color: UIColor, font: Font) = setText(text, color.color, font)
    
    fun addOnHoverAnimation(animation: Effect) {
        hoverAnimations += animation
    }
    
    fun addOnClickAnimation(animation: Animation) {
        clickAnimations += animation
    }
    
    override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        if (super.update(mouse, aspectRatio, deltaTime)) {
            return true
        }

        val isNowHovered = isHovered(mouse, aspectRatio)

        if (isHovered && !isNowHovered) {
            for (effect in hoverAnimations) {
                effect.removeFrom(this)
            }
        }
        if (!isHovered && isNowHovered) {
            for (effect in hoverAnimations) {
                effect.applyOn(this)
            }
        }
        
        isHovered = isNowHovered
        
        if (isHovered && (mouse.isPressed(Button.LEFT) || mouse.isPressed(Button.LEFT))) {
            onClick()
            isClicked = true
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