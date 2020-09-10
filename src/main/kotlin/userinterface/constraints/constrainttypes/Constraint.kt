package userinterface.constraints.constrainttypes

import userinterface.MovableUIContainer
import userinterface.constraints.ConstraintDirection
import userinterface.items.Item
import userinterface.items.ItemDimensions

abstract class Constraint(var direction: ConstraintDirection) {
    
    abstract fun apply(itemDimensions: ItemDimensions, parent: MovableUIContainer?): ItemDimensions

}