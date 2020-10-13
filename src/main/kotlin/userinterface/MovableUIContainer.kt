package userinterface

import devices.Mouse
import math.vectors.Vector2
import userinterface.animation.*
import userinterface.items.ItemDimensions
import userinterface.items.backgrounds.Background
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintDirection
import userinterface.layout.constraints.ConstraintSet
import userinterface.layout.constraints.constrainttypes.Constraint
import userinterface.layout.constraints.constrainttypes.ConstraintType
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

open class MovableUIContainer(id: String, var constraints: ConstraintSet, var background: Background) : UIContainer(id) {
    
    private val postPonedChildren = ConcurrentHashMap<String, MovableUIContainer>()
    private val computedChildren = ArrayList<String>()

    val animator = Animator()
    
    var goalTranslation: Vector2? = null
    var goalScale: Vector2? = null
    
    fun requiredIds() = constraints.requiredIds
    
    fun getTranslation() = constraints.translation()
    
    fun getScale() = constraints.scale()

    fun getGoalDimensions(): ItemDimensions {
        val translation = goalTranslation ?: getTranslation()
        val scale = goalScale ?: getScale()
        return ItemDimensions(translation, scale)
    }
    
    open fun position(parent: MovableUIContainer? = null, duration: Float = 0.0f) {
        constraints.apply(parent)
        
        children.forEach { child ->
            child.position(this, duration)
        }
    }
    
    open fun translate(translation: Vector2) {
        constraints.translate(translation)
    }

    fun setTranslation(translation: Vector2) = place(translation)
    
    fun place(placement: Vector2) {
        constraints.place(placement)
    }
    
    fun addToScale(scale: Vector2) {
        constraints.addToScale(scale)
    }
    
    fun setScale(scale: Vector2) {
        constraints.setScale(scale)
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