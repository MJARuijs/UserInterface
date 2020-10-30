package userinterface.animation

import math.vectors.Vector2
import userinterface.MovableUIContainer
import userinterface.items.Item
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintSet
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

class Animator {

    private val animations = ArrayList<Pair<MovableUIContainer, Animation>>()
    private val animationQueue = LinkedList<List<Pair<MovableUIContainer, Animation>>>()
    private val postPonedChildren = ConcurrentHashMap<String, MovableUIContainer>()
    private val computedChildren = ArrayList<String>()
    
    operator fun plusAssign(animations: List<Pair<MovableUIContainer, Animation>>) {
        animationQueue += animations
    }

    fun apply(item: MovableUIContainer, parent: MovableUIContainer, constraints: ConstraintSet, duration: Float, extraAnimations: ArrayList<Pair<MovableUIContainer, Animation>> = ArrayList()) {
        val newDimensions = constraints.computeResult(parent.getGoalDimensions(), parent)
        animateLayoutTransition(item, duration, newDimensions, extraAnimations)
    }

    fun apply(item: MovableUIContainer, children: ArrayList<Item>, layout: UILayout, duration: Float) {
        postPonedChildren.clear()
        computedChildren.clear()

        for (child in children) {
            applyChild(item, child, layout, duration)
        }
        
        val itemChanges = layout.getColorChange(item.id)
        if (itemChanges != null) {
            val colorAnimation = ValueAnimation(duration, itemChanges.first.color, ValueType.COLOR, AnimationType.SET, itemChanges.second)
            animationQueue += arrayListOf<Pair<MovableUIContainer, Animation>>(Pair(item, colorAnimation))
        }
    }

    private fun applyChild(item: MovableUIContainer, child: MovableUIContainer, layout: UILayout, duration: Float) {
        val childConstraints = layout.getConstraintsChange(child.id) ?: return
        val requiredIds = childConstraints.determineRequiredIds()
    
        if (computedChildren.containsAll(requiredIds)) {
            val childDimensions = childConstraints.computeResult(item.getGoalDimensions(), item)
            child.setGoalTranslation(childDimensions.first)
            child.setGoalScale(childDimensions.second)
            println("${child.id} ${childDimensions.first} ${child.getTranslation()}")

            computedChildren += child.id

            if (postPonedChildren.containsKey(child.id)) {
                postPonedChildren.remove(child.id)
            }
            
            for (postPonedChild in postPonedChildren) {
                applyChild(item, postPonedChild.value, layout, duration)
            }
            animateLayoutTransition(child, duration, child.getGoalDimensions())
        } else {
            postPonedChildren[child.id] = child
        }
        child.apply(layout, duration)
    }

    fun update(deltaTime: Float) {
        val removableAnimations = ArrayList<Pair<MovableUIContainer, Animation>>()
        
        animations.forEach { animation ->
            if (animation.second.apply(deltaTime)) {
                animation.second.onFinish()
                removableAnimations += animation
            }
        }

        if (removableAnimations.isNotEmpty()) {
            animations.removeAll(removableAnimations)
        }
        
        if (animations.isEmpty() && animationQueue.isNotEmpty()) {
            val queuedAnimation = animationQueue.poll()
            animations += queuedAnimation
        }
    }

    private fun animateLayoutTransition(item: MovableUIContainer, duration: Float, newDimensions: Pair<Vector2, Vector2>, extraAnimations: ArrayList<Pair<MovableUIContainer, Animation>> = ArrayList()) = animateLayoutTransition(item, duration, newDimensions.first, newDimensions.second, extraAnimations)
    
    private fun animateLayoutTransition(item: MovableUIContainer, duration: Float, newTranslation: Vector2, newScale: Vector2, extraAnimations: ArrayList<Pair<MovableUIContainer, Animation>> = ArrayList()) {
        val requiredAnimations = ArrayList<Pair<MovableUIContainer, Animation>>()
        requiredAnimations.addAll(extraAnimations)
        
        if (newTranslation.x != item.getTranslation().x) {
            requiredAnimations += Pair(item, ValueAnimation(duration, newTranslation.x, ValueType.TRANSLATION_X, AnimationType.SET, item))
        }

        if (newTranslation.y != item.getTranslation().y) {
            requiredAnimations += Pair(item, ValueAnimation(duration, newTranslation.y, ValueType.TRANSLATION_Y, AnimationType.SET, item))
        }

        if (newScale.x != item.getScale().x) {
            requiredAnimations += Pair(item, ValueAnimation(duration, newScale.x, ValueType.SCALE_X, AnimationType.SET, item))
        }

        if (newScale.y != item.getScale().y) {
            requiredAnimations += Pair(item, ValueAnimation(duration, newScale.y, ValueType.SCALE_Y, AnimationType.SET, item))
        }
        
        animationQueue += requiredAnimations
    }
}