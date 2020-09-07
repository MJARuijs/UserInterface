package userinterface.window

import devices.Mouse
import graphics.Quad
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.UIContainer
import userinterface.constraints.CenterConstraint
import userinterface.constraints.ConstraintDirection
import userinterface.constraints.ConstraintSet
import userinterface.constraints.RelativeConstraint
import userinterface.effects.Effect
import userinterface.items.Button
import userinterface.items.Item
import userinterface.items.backgrounds.Background

class UIWindow(val id: String, val constraints: ConstraintSet, val background: Background, var shouldShow: Boolean = false, titleBarData: TitleBarData) : UIContainer() {

    private var titleBar: TitleBar? = null
    private var quad = Quad()

    private var translation = Vector2()
    private var scale = Vector2()

    private var childTranslation = translation
    private var childScale = scale

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
        if (titleBarData.height > 0.0f) {
            titleBar = TitleBar("${id}_title_bar", titleBarData) {
                shouldShow = false
            }
        }

        val data = constraints.apply(Vector2(), Vector2(1.0f, 1.0f))
        translation = data.translation
        scale = data.scale

        if (hasTitleBar()) {
            childScale = Vector2(scale.x, scale.y)
            childScale.y -= titleBarData.height * scale.y

            childTranslation = Vector2(translation.x, translation.y)
            childTranslation.y -= titleBarData.height * scale.y

            titleBar!!.init(translation, scale, children)

            children.forEach { child ->
                child.init(childTranslation, childScale, children)
            }
        } else {
            children.forEach { child -> child.init(translation, scale, children) }
        }
    }

    fun hasTitleBar() = titleBar != null

    fun draw(shaderProgram: ShaderProgram) {
        shaderProgram.set("translation", translation)
        shaderProgram.set("scale", scale)
        background.setProperties(shaderProgram)
        quad.draw()
        if (hasTitleBar()) {
            titleBar!!.draw(shaderProgram)
        }
        children.forEach { child -> child.draw(shaderProgram) }
    }

    fun addButtonHoverEffects(buttonId: String, effect: Effect) {
        val closeButton = titleBar?.findById(buttonId) ?: return
        (closeButton as Button).addHoverEffect(effect)
    }

    fun addButtonOnClickEffects(buttonId: String, effect: Effect) {
        val closeButton = titleBar?.findById(buttonId) ?: return
        (closeButton as Button).addOnClickEffect(effect)
    }

    override fun initChild(item: Item, translation: Vector2, scale: Vector2, children: ArrayList<Item>) {
        super.initChild(item, childTranslation, childScale, this.children)
    }

    override fun update(mouse: Mouse, aspectRatio: Float) {
        super.update(mouse, aspectRatio)
        if (hasTitleBar()) {
            titleBar!!.update(mouse, aspectRatio)
        }
    }

    operator fun plusAssign(item: Item) {
        add(item, item.requiredIds)
    }

    fun destroy() {
        quad.destroy()
    }
}