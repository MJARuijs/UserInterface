package userinterface.layout.constraints.constrainttypes

import userinterface.MovableUIContainer
import userinterface.layout.constraints.ConstraintDirection
import userinterface.items.ItemDimensions

abstract class Constraint(var direction: ConstraintDirection) {
    
    abstract fun apply(itemDimensions: ItemDimensions, parentDimensions: ItemDimensions?, parent: MovableUIContainer?)

    abstract fun copy(): Constraint
    
}