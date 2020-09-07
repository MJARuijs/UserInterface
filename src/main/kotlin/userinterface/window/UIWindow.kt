package userinterface.window

import devices.Mouse
import graphics.Quad
import graphics.shaders.ShaderProgram
import math.vectors.Vector2
import userinterface.constraints.CenterConstraint
import userinterface.constraints.ConstraintDirection
import userinterface.constraints.ConstraintSet
import userinterface.constraints.RelativeConstraint
import userinterface.effects.Effect
import userinterface.items.Button
import userinterface.items.Item
import userinterface.items.backgrounds.Background
import java.util.concurrent.ConcurrentHashMap

class UIWindow(val id: String, val constraints: ConstraintSet, val background: Background, titleBarHeight: Float = 0.0f, titleBarBackground: Background = background, closeButtonAlignment: ButtonAlignment = ButtonAlignment.HIDDEN, var shouldShow: Boolean = false) {

    private val children = ArrayList<Item>()
    private var titleBar: TitleBar? = null
    private var quad = Quad()

    private var translation = Vector2()
    private var scale = Vector2()

    private var childTranslation = translation
    private var childScale = scale

    private var postPonedItems = ConcurrentHashMap<Item, ArrayList<String>>()

    constructor(id: String, scale: Vector2, background: Background, titleBarHeight: Float, titleBarBackground: Background = background, closeButtonAlignment: ButtonAlignment = ButtonAlignment.HIDDEN, shouldShow: Boolean = false) : this(
        id,
        ConstraintSet(
            CenterConstraint(ConstraintDirection.HORIZONTAL),
            CenterConstraint(ConstraintDirection.VERTICAL),
            RelativeConstraint(ConstraintDirection.HORIZONTAL, scale.x),
            RelativeConstraint(ConstraintDirection.VERTICAL, scale.y)
        ),
        background,
        titleBarHeight,
        titleBarBackground,
        closeButtonAlignment,
        shouldShow
    )

    init {
        if (titleBarHeight > 0.0f) {
            titleBar = TitleBar("${id}_title_bar", titleBarHeight, titleBarBackground, closeButtonAlignment)
        }

        val data = constraints.apply(Vector2(), Vector2(1.0f, 1.0f))
        translation = data.translation
        scale = data.scale

        if (hasTitleBar()) {
            childScale = Vector2(scale.x, scale.y)
            childScale.y -= titleBarHeight * scale.y

            childTranslation = Vector2(translation.x, translation.y)
            childTranslation.y -= titleBarHeight * scale.y

            titleBar!!.init(translation, scale, children)

            children.forEach { child ->
                child.init(childTranslation, childScale, children)
            }
        } else {
            children.forEach { child -> child.init(translation, scale, children) }
        }
    }

    fun hasTitleBar() = titleBar != null

    fun init(parentTranslation: Vector2 = Vector2(), parentScale: Vector2, parentChildren: ArrayList<Item>) {
//        val data = constraints.apply(parentTranslation, parentScale, parentChildren)
//        translation = data.translation
//        scale = data.scale
//
//        if (hasTitleBar()) {
//            val titleBarHeight = titleBar!!.height
//
//            val childScale = Vector2(scale.x, scale.y)
//            childScale.y -= titleBarHeight * scale.y
//
//            val childTranslation = Vector2(translation.x, translation.y)
//            childTranslation.y -= titleBarHeight * scale.y
//
//            titleBar!!.init(translation, scale, children)
//
//            children.forEach { child ->
////                if (child is TitleBar) {
////                    child.init(translation, scale, children)
////                } else {
//                    child.init(childTranslation, childScale, children)
//                }
////            }
//        } else {
//            children.forEach { child -> child.init(translation, scale, children) }
//        }
    }

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

    fun add(item: Item) {
        if (item.hasDependencies()) {
            println("${item.id} has dependencies")
            val requiredIds = item.requiredIds

            children.forEach { child ->
                if (requiredIds.contains(child.id)) {
                    requiredIds.remove(child.id)
                }
            }
            if (requiredIds.isEmpty()) {
                addItem(item)
            } else {
                postPonedItems[item] = requiredIds
            }
        } else {
            addItem(item)
        }
    }

    fun checkItemDependencies(item: Item) {

    }

    private fun addItemWithDependencies(item: Item, requiredIds: ArrayList<String>) {
        children.forEach { child ->
            if (requiredIds.contains(child.id)) {
                requiredIds.remove(child.id)
            }
        }
        if (requiredIds.isEmpty()) {
            postPonedItems.remove(item)
            addItem(item)
        } else {
            postPonedItems[item] = requiredIds
        }
    }

    private fun addItem(item: Item) {
        item.init(childTranslation, childScale, children)
        children += item

        println("Added item: ${item.id}")

        for (postPonedItem in postPonedItems) {
            println("Checking ${postPonedItem.key.id} from ${item.id}")
            addItemWithDependencies(postPonedItem.key, postPonedItem.value)
        }
    }

    fun update(mouse: Mouse, aspectRatio: Float) {
        children.forEach { child -> child.update(mouse, aspectRatio) }
        if (hasTitleBar()) {
            titleBar!!.update(mouse, aspectRatio)
        }
    }

    operator fun plusAssign(item: Item) {
        add(item)
    }

    fun destroy() {
        quad.destroy()
    }
}