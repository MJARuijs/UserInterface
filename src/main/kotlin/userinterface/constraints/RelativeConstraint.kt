package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class RelativeConstraint(private val percentage: Float, private val direction: ConstraintDirection) : Constraint() {

    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition {

        if (direction == ConstraintDirection.HORIZONTAL) {
            scale.x = parentScale.x * percentage
        }

        if (direction == ConstraintDirection.VERTICAL) {
            scale.y = parentScale.y * percentage
        }

        return ItemPosition(translation, scale)
    }
}