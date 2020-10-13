package userinterface.items

import devices.Mouse
import userinterface.UniversalParameters.SWITCH_THUMB_OFF_BACKGROUND
import userinterface.UniversalParameters.SWITCH_THUMB_ON_BACKGROUND
import userinterface.UniversalParameters.SWITCH_TRACK_OFF_BACKGROUND
import userinterface.UniversalParameters.SWITCH_TRACK_ON_BACKGROUND
import userinterface.animation.ColorAnimation
import userinterface.items.backgrounds.ColorType
import userinterface.items.backgrounds.ColoredBackground
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.AspectRatioConstraint
import userinterface.layout.constraints.constrainttypes.CenterConstraint
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint

class Switch(id: String, constraints: ConstraintSet, private var switchOn: Boolean = false,
             private var onStateChanged: (Boolean) -> Unit = {},
             private val trackOnBackground: ColoredBackground = SWITCH_TRACK_ON_BACKGROUND,
             private val thumbOnBackground: ColoredBackground = SWITCH_THUMB_ON_BACKGROUND,
             private val trackOffBackground: ColoredBackground = SWITCH_TRACK_OFF_BACKGROUND,
             private val thumbOffBackground: ColoredBackground = SWITCH_THUMB_OFF_BACKGROUND)
    : Item(id, constraints, trackOffBackground) {

    private var thumb: Item

    private val switchOffConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_LEFT),
        CenterConstraint(ConstraintDirection.VERTICAL),
        RelativeConstraint(ConstraintDirection.VERTICAL, 1.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
    )

    private val switchOnConstraints = ConstraintSet(
        PixelConstraint(ConstraintDirection.TO_RIGHT),
        CenterConstraint(ConstraintDirection.VERTICAL),
        RelativeConstraint(ConstraintDirection.VERTICAL, 1.5f),
        AspectRatioConstraint(ConstraintDirection.HORIZONTAL, 1.0f)
    )
    
    private var previousState = switchOn

    init {
        if (switchOn) {
            background = ColoredBackground(trackOnBackground)
            thumb = UIButton("${id}_thumb", switchOnConstraints, { toggle() }, background = ColoredBackground(thumbOnBackground))
        } else {
            background = ColoredBackground(trackOffBackground)
            thumb = UIButton("${id}_thumb", switchOffConstraints, { toggle() }, background = ColoredBackground(thumbOffBackground))
        }
        children += thumb
    }

    fun turnOn(duration: Float = 0.1f) {
        switchOn = true
        animator.apply(thumb, this, switchOnConstraints, duration)
        animator += ColorAnimation(duration, trackOnBackground.outlineColor, ColorType.OUTLINE_COLOR, this)
        animator += ColorAnimation(duration, thumbOnBackground.color, ColorType.BACKGROUND_COLOR, thumb)
    }

    fun turnOff(duration: Float = 0.1f) {
        switchOn = false
        animator.apply(thumb, this, switchOffConstraints, duration)
        animator += ColorAnimation(duration, trackOffBackground.outlineColor, ColorType.OUTLINE_COLOR, this)
        animator += ColorAnimation(duration, thumbOffBackground.color, ColorType.BACKGROUND_COLOR, thumb)
    }

    fun toggle(duration: Float = 0.1f) {
        if (switchOn) {
            turnOff(duration)
        } else {
            turnOn(duration)
        }
    }
    
    override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        if (previousState != switchOn) {
            onStateChanged(switchOn)
        }
        previousState = switchOn
        return super.update(mouse, aspectRatio, deltaTime)
    }
}