package userinterface.items

import math.vectors.Vector2

data class ItemDimensions(var translation: Vector2 = Vector2(), var scale: Vector2 = Vector2(1.0f, 1.0f)) {
    constructor(itemDimensions: ItemDimensions) : this(Vector2(itemDimensions.translation.x, itemDimensions.translation.y), Vector2(itemDimensions.scale))
}