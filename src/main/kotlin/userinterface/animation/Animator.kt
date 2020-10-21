package userinterface.animation

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.items.Item
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintSet
import java.util.concurrent.ConcurrentHashMap

class Animator {

    private val animations = ArrayList<Animation>()
    private val postPonedChildren = ConcurrentHashMap<String, MovableUIContainer>()
    private val computedChildren = ArrayList<String>()
    
    var isAnimating = false

    operator fun plusAssign(animation: Animation) {
        animations.add(animation)
    }

    fun apply(item: MovableUIContainer, parent: MovableUIContainer, constraints: ConstraintSet, duration: Float) {
        val goalDimension = parent.getGoalDimensions()
        val newDimensions = constraints.computeResult(goalDimension.first, goalDimension.second, parent)
        animateLayoutTransition(item, duration, newDimensions.first, newDimensions.second)
    }

    fun apply(item: MovableUIContainer, children: ArrayList<Item>, layout: UILayout, duration: Float) {
        postPonedChildren.clear()
        computedChildren.clear()

        for (child in children) {
            applyChild(item, child, layout, duration)
        }
        val itemChanges = layout.getColorChange(item.id)
        if (itemChanges != null) {
            animations += ColorAnimation(duration, itemChanges.first.color, itemChanges.second, item)
        }
    }

    private fun applyChild(item: MovableUIContainer, child: MovableUIContainer, layout: UILayout, duration: Float) {
        val childConstraints = layout.getConstraintsChange(child.id) ?: child.constraints
        val requiredIds = childConstraints.determineRequiredIds()
        if (computedChildren.containsAll(requiredIds)) {
            val goalDimension = item.getGoalDimensions()
            val childDimensions = childConstraints.computeResult(goalDimension.first, goalDimension.second, item)
            child.goalTranslation = childDimensions.first
            child.goalScale = childDimensions.second

            computedChildren += child.id

            if (postPonedChildren.containsKey(child.id)) {
                postPonedChildren.remove(child.id)
            }
            for (postPonedChild in postPonedChildren) {
                applyChild(item, postPonedChild.value, layout, duration)
            }
            val childGoalDimensions = child.getGoalDimensions()
            animateLayoutTransition(child, duration, childGoalDimensions.first, childGoalDimensions.second)
        } else {
            postPonedChildren[child.id] = child
        }
        child.apply(layout, duration)
    }

    fun update(deltaTime: Float): Int {
        val removableAnimations = ArrayList<Animation>()
        if (animations.isNotEmpty()) {
            isAnimating = true
        }
        
        animations.forEach { animation ->
            if (animation.apply(deltaTime)) {
                animation.onFinish()
                removableAnimations += animation
            }
        }

        animations.removeAll(removableAnimations)
        
        if (animations.isEmpty()) {
            isAnimating = false
        }
        return animations.size
    }

    private fun animateLayoutTransition(item: MovableUIContainer, duration: Float, newTranslation: Vector2, newScale: Vector2) {
        if (newTranslation.x != item.getTranslation().x) {
            animations += XTransitionAnimation(duration, newTranslation.x, item, TransitionType.PLACEMENT)
        }

        if (newTranslation.y != item.getTranslation().y) {
            animations += YTransitionAnimation(duration, newTranslation.y, item, TransitionType.PLACEMENT)
        }

        if (newScale.x != item.getScale().x) {
            animations += XScaleAnimation(duration, newScale.x, item)
        }

        if (newScale.y != item.getScale().y) {
            animations += YScaleAnimation(duration, newScale.y, item)
        }
    }
}