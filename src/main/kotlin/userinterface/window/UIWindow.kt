package userinterface.window

import math.vectors.Vector2
import userinterface.constraints.CenterConstraint
import userinterface.constraints.ConstraintDirection
import userinterface.constraints.ConstraintSet
import userinterface.constraints.RelativeConstraint
import userinterface.effects.Effect
import userinterface.items.Button
import userinterface.items.Item
import userinterface.items.backgrounds.Background

class UIWindow(id: String, constraintSet: ConstraintSet, background: Background) : Item(id, constraintSet, background) {

    private var titleBarId = ""

    constructor(id: String, scale: Vector2, background: Background) : this(
        id,
        ConstraintSet(
            CenterConstraint(ConstraintDirection.HORIZONTAL),
            CenterConstraint(ConstraintDirection.VERTICAL),
            RelativeConstraint(ConstraintDirection.HORIZONTAL, scale.x),
            RelativeConstraint(ConstraintDirection.VERTICAL, scale.y)
        ),
        background
    )

    fun hasTitleBar() = titleBarId != ""

    fun getTitleBar() = findById(titleBarId) as TitleBar

    fun setTitleBar(id: String, height: Float, background: Background = this.baseBackground, closeButtonAlignment: ButtonAlignment) {
        titleBarId = id
        add(TitleBar(id, height, background, closeButtonAlignment))
    }

    fun addButtonHoverEffects(buttonId: String, effect: Effect) {
        val titleBar = findById(titleBarId) ?: return
        val closeButton = titleBar.findById(buttonId) ?: return
        (closeButton as Button).addHoverEffect(effect)
    }

    fun addButtonOnClickEffects(buttonId: String, effect: Effect) {
        val titleBar = findById(titleBarId) ?: return
        val closeButton = titleBar.findById(buttonId) ?: return
        (closeButton as Button).addOnClickEffect(effect)
    }

}