package userinterface

import devices.Mouse
import math.vectors.Vector2
import userinterface.animation.*
import userinterface.items.ItemDimensions
import userinterface.items.backgrounds.Background
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintSet
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

open class MovableUIContainer(id: String, var constraints: ConstraintSet, var background: Background) : UIContainer(id) {
    
    private val animations = ArrayList<Animation>()
    private val postPonedChildren = ConcurrentHashMap<String, MovableUIContainer>()
    private val computedChildren = ArrayList<String>()
    
    private var goalTranslation: Vector2? = null
    private var goalScale: Vector2? = null
    
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
    
    fun translate(translation: Vector2) {
        constraints.translate(translation)
    }
    
    fun place(placement: Vector2) {
        constraints.place(placement)
    }
    
    fun addToScale(scale: Vector2) {
        constraints.addToScale(scale)
    }
    
    fun setScale(scale: Vector2) {
        constraints.setScale(scale)
    }
    
    override fun apply(layout: UILayout, duration: Float, parentDimensions: ItemDimensions?) {
        postPonedChildren.clear()
        computedChildren.clear()
  
        for (child in children) {
            applyChild(child, layout, duration)
        }
    }
    
    private fun applyChild(child: MovableUIContainer, layout: UILayout, duration: Float) {
        val childConstraints = layout.getConstraint(child.id)
        if (childConstraints != null) {
            val requiredIds = childConstraints.determineRequiredIds()
            if (computedChildren.containsAll(requiredIds)) {
                val childDimensions = childConstraints.computeResult(getGoalDimensions(), this)
                child.goalTranslation = childDimensions.translation
                child.goalScale = childDimensions.scale
                
                computedChildren += child.id
        
                if (postPonedChildren.containsKey(child.id)) {
                    postPonedChildren.remove(child.id)
                }
                for (postPonedChild in postPonedChildren) {
                    applyChild(postPonedChild.value, layout, duration)
                }
                child.animateLayoutTransition(duration, child.getGoalDimensions())
            } else {
                postPonedChildren[child.id] = child
            }
        }
        child.apply(layout, duration, getGoalDimensions())
    }
    
    private fun animateLayoutTransition(duration: Float, newDimensions: ItemDimensions) {
        if (newDimensions.translation.x != getTranslation().x) {
            animations += XTransitionAnimation(duration, newDimensions.translation.x, this, TransitionType.PLACEMENT)
        }

        if (newDimensions.translation.y != getTranslation().y) {
            animations += YTransitionAnimation(duration, newDimensions.translation.y, this, TransitionType.PLACEMENT)
        }

        if (newDimensions.scale.x != getScale().x) {
            animations += XScaleAnimation(duration, newDimensions.scale.x, this)
        }

        if (newDimensions.scale.y != getScale().y) {
            animations += YScaleAnimation(duration, newDimensions.scale.y, this)
        }
    }
    
    override fun update(mouse: Mouse, aspectRatio: Float, deltaTime: Float): Boolean {
        val removableAnimations = ArrayList<Animation>()

        animations.forEach { animation ->
            if (animation.apply(deltaTime)) {
                removableAnimations += animation
            }
        }

        animations.removeAll(removableAnimations)
        return super.update(mouse, aspectRatio, deltaTime)
    }
}