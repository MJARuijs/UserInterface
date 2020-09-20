package userinterface.animation

import userinterface.MovableUIContainer
import userinterface.items.Item
import userinterface.items.ItemDimensions
import userinterface.layout.UILayout
import userinterface.layout.constraints.ConstraintSet
import java.util.concurrent.ConcurrentHashMap

class Animator {

    private val animations = ArrayList<Animation>()
    private val postPonedChildren = ConcurrentHashMap<String, MovableUIContainer>()
    private val computedChildren = ArrayList<String>()

    operator fun plusAssign(animation: Animation) {
        animations.add(animation)
    }

    fun apply(item: MovableUIContainer, parent: MovableUIContainer, constraints: ConstraintSet, duration: Float) {
        val newDimensions = constraints.computeResult(parent.getGoalDimensions(), parent)
        animateLayoutTransition(item, duration, newDimensions)
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
            val childDimensions = childConstraints.computeResult(item.getGoalDimensions(), item)
            child.goalTranslation = childDimensions.translation
            child.goalScale = childDimensions.scale

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

    fun update(deltaTime: Float): Int {
        val removableAnimations = ArrayList<Animation>()

        animations.forEach { animation ->
            if (animation.apply(deltaTime)) {
                animation.onFinish()
                removableAnimations += animation
            }
        }

        animations.removeAll(removableAnimations)
        return animations.size
    }

    private fun animateLayoutTransition(item: MovableUIContainer, duration: Float, newDimensions: ItemDimensions) {
        if (newDimensions.translation.x != item.getTranslation().x) {
            animations += XTransitionAnimation(duration, newDimensions.translation.x, item, TransitionType.PLACEMENT)
        }

        if (newDimensions.translation.y != item.getTranslation().y) {
            animations += YTransitionAnimation(duration, newDimensions.translation.y, item, TransitionType.PLACEMENT)
        }

        if (newDimensions.scale.x != item.getScale().x) {
            animations += XScaleAnimation(duration, newDimensions.scale.x, item)
        }

        if (newDimensions.scale.y != item.getScale().y) {
            animations += YScaleAnimation(duration, newDimensions.scale.y, item)
        }
    }
}