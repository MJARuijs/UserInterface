package userinterface.window

import devices.Mouse
import graphics.Quad
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.constraints.ConstraintDirection
import userinterface.constraints.ConstraintSet
import userinterface.constraints.constrainttypes.CenterConstraint
import userinterface.constraints.constrainttypes.PixelConstraint
import userinterface.constraints.constrainttypes.RelativeConstraint
import userinterface.effects.Effect
import userinterface.items.Item
import userinterface.items.UIButton
import userinterface.items.backgrounds.Background

class UIWindow(id: String, constraints: ConstraintSet, val background: Background, var shouldShow: Boolean = false, titleBarData: TitleBarData) : MovableUIContainer(id, constraints) {

    private var titleBar: TitleBar? = null
    private var quad = Quad()

    constructor(id: String, scale: Vector2, background: Background, titleBarHeight: Float, titleBarBackground: Background = background, closeButtonAlignment: ButtonAlignment = ButtonAlignment.HIDDEN, shouldShow: Boolean = false) : this(
        id,
        ConstraintSet(
            CenterConstraint(ConstraintDirection.HORIZONTAL),
            CenterConstraint(ConstraintDirection.VERTICAL),
            RelativeConstraint(ConstraintDirection.HORIZONTAL, scale.x),
            RelativeConstraint(ConstraintDirection.VERTICAL, scale.y)
        ),
        background,
        shouldShow,
        TitleBarData(titleBarHeight, titleBarBackground, closeButtonAlignment)
    )

    init {
        constraints.apply(this)
        
        if (titleBarData.height > 0.0f) {
            titleBar = TitleBar("${id}_title_bar", titleBarData) {
                shouldShow = false
            }
            add(titleBar!!)
        }
    }

    private fun hasTitleBar() = titleBar != null

    fun draw(shaderProgram: ShaderProgram) {
        shaderProgram.set("translation", constraints.translation())
        shaderProgram.set("scale", constraints.scale())
        background.setProperties(shaderProgram)
        quad.draw()
        if (hasTitleBar()) {
            titleBar!!.draw(shaderProgram)
        }
        children.forEach { child -> child.draw(shaderProgram) }
    }

    fun addButtonHoverEffects(buttonId: String, effect: Effect) {
        val closeButton = titleBar?.findById(buttonId) ?: return
        (closeButton as UIButton).addHoverEffect(effect)
    }

    fun addButtonOnClickEffects(buttonId: String, effect: Effect) {
        val closeButton = titleBar?.findById(buttonId) ?: return
        (closeButton as UIButton).addOnClickEffect(effect)
    }

    override fun positionChild(item: Item) {
        if (hasTitleBar() && item.id != titleBar!!.id) {
            for (constraint in item.constraints.constraints) {
                if (constraint is PixelConstraint && constraint.direction == ConstraintDirection.TO_TOP && constraint.anchorId == "parent") {
                    constraint.anchorId = titleBar!!.id
                    constraint.direction = ConstraintDirection.TO_BOTTOM
                }
            }
        }
        item.position(this)
        children += item
    }

    override fun update(mouse: Mouse, aspectRatio: Float): Boolean {
        if (hasTitleBar()) {
            titleBar!!.update(mouse, aspectRatio)
        }
        return super.update(mouse, aspectRatio)
    }

    fun destroy() {
        quad.destroy()
    }
}