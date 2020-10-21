package userinterface.items

import math.vectors.Vector2

data class ItemDimensions(var translation: Vector2 = Vector2(), var scale: Vector2 = Vector2(1.0f, 1.0f)) {
    
    fun copy(): ItemDimensions {
        return ItemDimensions(Vector2(translation), Vector2(scale))
    }
    
}