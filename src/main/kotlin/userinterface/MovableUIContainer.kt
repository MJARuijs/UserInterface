package userinterface

import devices.Mouse
import math.vectors.Vector2
import userinterface.animation.*
import userinterface.items.backgrounds.Background
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.ConstraintType
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

abstract class MovableUIContainer(id: String, var constraints: ConstraintSet, var background: Background) : UIContainer(id) {
    
    private val postPonedChildren = ConcurrentHashMap<String, MovableUIContainer>()
    private val computedChildren = ArrayList<String>()
    
    private var goalTranslation: Vector2? = null
    private var goalScale: Vector2? = null
    
    var x = 0.0f
    var y = 0.0f
    
    var xScale = 1.0f
    var yScale = 1.0f

    val animator = Animator()

    var basePosition = Vector2()
    var baseScale = Vector2(1.0f, 1.0f)
    
    fun requiredIds() = constraints.determineRequiredIds()
    
    fun getTranslation() = Vector2(x, y)
    
    fun getScale() = Vector2(xScale, yScale)

    fun getGoalDimensions(): Pair<Vector2, Vector2> {
        val translation = goalTranslation ?: getTranslation()
        val scale = goalScale ?: getScale()
        return Pair(translation, scale)
    }
    
    fun getGoalTranslation() = goalTranslation ?: getTranslation()
    
    fun setGoalTranslation(goal: Vector2) {
        goalTranslation = goal
    }
    
    fun getGoalScale() = goalScale ?: getScale()
    
    fun setGoalScale(goal: Vector2) {
        goalScale = goal
    }
    
    open fun position(parent: UIContainer? = null, duration: Float = 0.0f) {
        constraints.apply(this, parent)

        basePosition = getTranslation()
        baseScale = getScale()
        
        children.forEach { child ->
            child.position(this, duration)
        }
    }
    
    open fun translate(translation: Vector2) {
        x += translation.x
        y += translation.y
        
        children.forEach { child ->
            child.translate(translation)
        }
    }
    
    fun setTranslation(translation: Vector2) = place(translation)
    
    fun place(placement: Vector2) {
        x += placement.x
        y += placement.y
    }
    
    fun addToScale(scale: Vector2) {
        xScale += scale.x
        yScale += scale.y
    }
    
    fun setScale(scale: Vector2) {
        xScale = scale.x
        yScale = scale.y
    }
    
    fun updateConstraint(type: ConstraintType, direction: ConstraintDirection, newValue: Float) {
        constraints.updateConstraint(type, direction, newValue)
    }
    
    override fun apply(layout: UILayout, duration: Float) {
        animator.apply(this, children, layout, duration)
    }

    override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        animator.update(deltaTime)
        return super.update(mouse, aspectRatio, deltaTime)
    }
}