package userinterface.items

import userinterface.UniversalParameters
import userinterface.animation.ColorAnimation
import userinterface.animation.TransitionType
import userinterface.animation.XTransitionAnimation
import userinterface.items.backgrounds.ColoredBackground
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.AspectRatioConstraint
import userinterface.layout.constraints.constrainttypes.CenterConstraint
import userinterface.layout.constraints.constrainttypes.PixelConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint

class Switch(id: String, constraints: ConstraintSet,
             private val trackOnBackground: ColoredBackground = UniversalParameters.SWITCH_TRACK_ON_BACKGROUND,
             private val thumbOnBackground: ColoredBackground = UniversalParameters.SWITCH_THUMB_ON_BACKGROUND,
             private val trackOffBackground: ColoredBackground = UniversalParameters.SWITCH_TRACK_OFF_BACKGROUND,
             private val thumbOffBackground: ColoredBackground = UniversalParameters.SWITCH_THUMB_OFF_BACKGROUND,
             private var switchOn: Boolean = false)
    : Item(id, constraints, trackOnBackground) {

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

    init {
        if (switchOn) {
            background = trackOnBackground
            thumb = UIButton("${id}_thumb", switchOnConstraints, UniversalParameters.SWITCH_THUMB_ON_BACKGROUND, { toggle() })
        } else {
            background = trackOffBackground
            thumb = UIButton("${id}_thumb", switchOffConstraints, UniversalParameters.SWITCH_THUMB_OFF_BACKGROUND, { toggle() })
        }
        children += thumb
    }

    fun turnOn(duration: Float) {
        switchOn = true
        println("Tuning on ${thumbOnBackground.color}")
        animator.apply(thumb, this, switchOnConstraints, duration)
        thumb.animator += ColorAnimation(duration, UniversalParameters.SWITCH_THUMB_ON_BACKGROUND.color, thumb) {
            println("RESULT THUMB ON: ${thumbOnBackground.color}")
            println("RESULT THUMB OFF: ${thumbOffBackground.color}")
            println("RESULT BACKGROUND: ${(thumb.background as ColoredBackground).color}")
        }

    }

    fun turnOff(duration: Float) {
        switchOn = false
        println("Tuning off ${ UniversalParameters.SWITCH_THUMB_OFF_BACKGROUND.color}")

        animator.apply(thumb, this, switchOffConstraints, duration)

        thumb.animator += ColorAnimation(duration, UniversalParameters.SWITCH_THUMB_OFF_BACKGROUND.color, thumb){
            println("RESULT THUMB ON: ${thumbOnBackground.color}")
            println("RESULT THUMB OFF: ${thumbOffBackground.color}")
            println("RESULT BACKGROUND: ${(thumb.background as ColoredBackground).color}")
        }
    }

    fun toggle(duration: Float = 0.1f) {
        if (switchOn) {
            turnOff(duration)
        } else {
            turnOn(duration)
        }
    }
}