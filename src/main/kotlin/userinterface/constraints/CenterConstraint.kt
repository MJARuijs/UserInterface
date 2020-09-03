package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class CenterConstraint(direction: ConstraintDirection) : Constraint(direction) {

    override fun type() = ConstraintType.CENTER

    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition {

        if (direction == ConstraintDirection.HORIZONTAL) {
            translation.x = parentTranslation.x
        }
        if (direction == ConstraintDirection.VERTICAL) {
            translation.y = parentTranslation.y
        }

        return ItemPosition(translation, scale)
    }
}