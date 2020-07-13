package userinterface.constraints

import math.vectors.Vector2
import userinterface.items.Item
import userinterface.items.ItemPosition

class ConstraintSet(private val constraints: ArrayList<Constraint> = ArrayList()) {

    constructor(vararg constraints: Constraint) : this() {
        this.constraints += constraints
    }

    var translation = Vector2()
        private set

    var scale = Vector2()
        private set

    private lateinit var xConstraint: Constraint
    private lateinit var yConstraint: Constraint
    private lateinit var widthConstraint: Constraint
    private lateinit var heightConstraint: Constraint

    operator fun plusAssign(constraint: Constraint) {
        constraints += constraint
    }

    fun setX(constraint: Constraint) {
        xConstraint = constraint
    }

    fun setY(constraint: Constraint) {
        yConstraint = constraint
    }

    fun setWidth(constraint: Constraint) {
        widthConstraint = constraint
    }

    fun setHeight(constraint: Constraint) {
        heightConstraint = constraint
    }

    fun apply(parentTranslation: Vector2, parentScale: Vector2, siblings: ArrayList<Item>): ItemPosition {
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