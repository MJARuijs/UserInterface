package userinterface

import devices.Mouse
import math.vectors.Vector2
import userinterface.animation.*
import userinterface.items.Item
import userinterface.items.ItemDimensions
import userinterface.items.backgrounds.Background
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintSet
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

open class MovableUIContainer(id: String, var constraints: ConstraintSet, var background: Background) : UIContainer(id) {
    
    private val animations = ArrayList<Animation>()
    
//    private var goalDimensions = ItemDimensions(getTranslation(), getScale())
    
    private var goalTranslation: Vector2? = null
    private var goalScale: Vector2? = null
    
    fun requiredIds() = constraints.requiredIds
    var requiredIds = constraints.requiredIds
    
    fun getTranslation() = constraints.translation()
    
    fun getScale() = constraints.scale()

//    fun getGoalDimensions() = goalDimensions

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
    
    private val postPonedChildren = ConcurrentHashMap<String, MovableUIContainer>()
    private val computedChildren = ArrayList<String>()
    
    override fun apply(layout: UILayout, duration: Float, parentDimensions: ItemDimensions?, parent: MovableUIContainer?) {
        postPonedChildren.clear()
        computedChildren.clear()
  
        for (child in children) {
            if (parentDimensions == null) {
                if (id == "options_menu") {
                    println("Giving child: ${getTranslation()} ${getGoalDimensions().scale}")
                }
                applyChild(child, layout, duration, getGoalDimensions())
            } else {
                applyChild(child, layout, duration, parentDimensions)
            }
        }
        
        if (id == "testButton1") {
            println(getTranslation().y + getScale().y)
        }
    }
    
    private fun applyChild(child: MovableUIContainer, layout: UILayout, duration: Float, parentDimensions: ItemDimensions?) {
        val childConstraints = layout.getConstraint(child.id)
        if (childConstraints != null) {
            val requiredIds = childConstraints.determineRequiredIds()
            if (computedChildren.containsAll(requiredIds)) {
                println("Contains all to compute child ${child.id}")
                val childDimensions = childConstraints.computeResult(getGoalDimensions(), this)
                child.goalTranslation = childDimensions.translation
                child.goalScale = childDimensions.scale
                
                computedChildren += child.id
        
                if (postPonedChildren.containsKey(child.id)) {
                    postPonedChildren.remove(child.id)
                }
                for (postPonedChild in postPonedChildren) {
                    println("Trying again for ${postPonedChild.key}")
                    applyChild(postPonedChild.value, layout, duration, constraints.dimensions)
                }
                child.animateLayoutTransition(duration, child.getGoalDimensions())
            } else {
                println("Postponing child ${child.id}")
                postPonedChildren[child.id] = child
            }
        }
        println("$id ${getGoalDimensions().translation} ${getGoalDimensions().scale} :: ${child.id}")
        child.apply(layout, duration, getGoalDimensions(), this)
    }
    
    private fun computeChildren(childrenToBeComputed: ArrayList<Item>, layout: UILayout, duration: Float, calculatedChildren: ArrayList<String>) {
        val postPonedChildren = ArrayList<Item>()
    
        var childFound = false
    
        for (child in childrenToBeComputed) {
            if (layout.containsConstraintForItem(child.id)) {
                val newChildConstraint = layout.getConstraint(child.id)!!
                
                if (calculatedChildren.containsAll(newChildConstraint.requiredIds)) {
                    println("$id: All required children found ${child.id}")
                    child.apply(layout, duration, constraints.dimensions, this)
                    calculatedChildren += child.id
                } else {
                    postPonedChildren += child
                }
                childFound = true
            }
        }
    
        if (!childFound) {
            for (child in children) {
                println("$id no child found, resuming with ${child.id}")
    
                child.apply(layout, duration, constraints.dimensions, this)
            }
        }
        
        if (postPonedChildren.isNotEmpty()) {
            computeChildren(postPonedChildren, layout, duration, calculatedChildren)
        }
    }
    
    fun animateLayoutTransition(duration: Float, newDimensions: ItemDimensions) {
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