package userinterface.window

import devices.Mouse
import graphics.Quad
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.CenterConstraint
import userinterface.layout.constraints.constrainttypes.RelativeConstraint
import userinterface.effects.Effect
import userinterface.items.Item
import userinterface.items.UIButton
import userinterface.items.backgrounds.Background

class UIWindow(id: String, constraints: ConstraintSet, background: Background, var shouldShow: Boolean = false, titleBarData: TitleBarData) : MovableUIContainer(id, constraints, background) {

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
            
            
            constraints.translate(Vector2(0f, -titleBar!!.getScale().y))
            constraints.addToScale(Vector2(0f, -titleBar!!.getScale().y))
            
            println(getTranslation().y + getScale().y)
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
        item.position(this)
        children += item
    }

    override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        if (hasTitleBar()) {
            titleBar!!.update(mouse, aspectRatio, deltaTime)
        }
        return super.update(mouse, aspectRatio, deltaTime)
    }

    fun destroy() {
        quad.destroy()
    }
}