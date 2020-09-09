package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class ConstraintSet(vararg val constraints: Constraint) {

    val requiredIds = ArrayList<String>()

    init {
        for (constraint in constraints) {
            if (constraint is PixelConstraint) {
                if (constraint.anchorId != "parent") {
                    requiredIds += constraint.anchorId
                }
            } else if (constraint is RelativeConstraint) {
                if (constraint.anchorId != "parent") {
                    requiredIds += constraint.anchorId
                }
            }
        }
    }

    var translation = Vector2()
        private set

    var scale = Vector2()
        private set

    fun findConstraint(type: ConstraintType, direction: ConstraintDirection): Constraint? {
        return constraints.find { constraint ->
            val isCorrectType = when (type) {
                ConstraintType.CENTER -> constraint is CenterConstraint
                ConstraintType.PIXEL -> constraint is PixelConstraint
                ConstraintType.ASPECT_RATIO -> constraint is AspectRatioConstraint
                ConstraintType.RELATIVE -> constraint is RelativeConstraint
            }

            isCorrectType && constraint.direction == direction
        }
    }

    fun apply(parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item> = ArrayList()): ItemPosition {
        val parentPosition = ItemPosition(parentTranslation, parentScale)
        var currentPosition = ItemPosition(translation, scale)

        constraints.sortWith(compareBy {
            return@compareBy when (it) {
                is PixelConstraint -> 1
                else -> -1
            }
        })

        for (constraint in constraints) {
           currentPosition = constraint.apply(currentPosition, parentPosition, siblings)
        }

        return ItemPosition(currentPosition.translation, currentPosition.scale)
    }

}