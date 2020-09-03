package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class AspectRatioConstraint(direction: ConstraintDirection, private val aspectRatio: Float) : Constraint(direction) {

    override fun type() = ConstraintType.ASPECT_RATIO

    override fun apply(translation: Vector2, scale: Vector2, parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition {

        if (direction == ConstraintDirection.VERTICAL) {
            scale.y = scale.x * aspectRatio

            if (scale.y > parentScale.y) {
                println("UI Item tried to go out of bounds of parent!")
                scale.y = parentScale.y
            }
        }
        if (direction == ConstraintDirection.HORIZONTAL) {
            scale.x = scale.y * aspectRatio

            if (scale.x > parentScale.x) {
                println("UI Item tried to go out of bounds of parent!")
                scale.x = parentScale.x
            }
        }

        return ItemPosition(translation, scale)
    }
}