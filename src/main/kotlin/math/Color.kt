package math

import math.vectors.Vector3
import math.vectors.Vector4

/**
 * A color defined by w red, green, and blue color channels and an alpha value.
 * @param r the red color channel's value (default 0.0f)
 * @param g the green color channel's value (default 0.0f)
 * @param b the blue color channel's value (default 0.0f)
 * @param b the alpha value (default 1.0f)
 * @constructor
 */
 class Color(var values: Vector4 = Vector4(0f, 0f, 0f, 1f)) {

    constructor(color: Color) : this(color.values)
    
    constructor(r: Float, g: Float, b: Float, a: Float = 1.0f): this(Vector4(r, g, b, a))

    constructor(r: Int, g: Int, b: Int, a: Int = 255): this(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f)

    constructor(rgb: Vector3, a: Float) : this(rgb[0], rgb[1], rgb[2], a)
    
    fun r() = values[0]
    
    fun g() = values[1]
    
    fun b() = values[2]
    
    fun a() = values[3]

//    operator fun plus(other: Color) = Color(r + other.r, g + other.g, b + other.b, a + other.b)

//    operator fun minus(other: Color) = Color(r - other.r, g - other.g, b - other.b, a - other.b)

//    operator fun times(other: Color) = Color(r * other.r, g * other.g, b * other.b, a * other.b)

//    operator fun times(factor: Float) = Color(r * factor, g * factor, b * factor, a * factor)

//    operator fun div(factor: Float) = Color(r / factor, g / factor, b / factor, a / factor)
    
    operator fun unaryMinus() = Color(-values[0], -values[1], -values[2], values[3])

    operator fun get(i: Int): Float {
        if (i in 0..3) {
            return values[i]
        } else {
            throw IndexOutOfBoundsException("Tried to access element at position $i, which doesn't exist in a Color..")
        }
    }

    operator fun set(i: Int, value: Float) {
        if (i in 0..3) {
            values[i] = value
        } else {
            throw IndexOutOfBoundsException("Tried to set element at position $i, which doesn't exist in a Color..")
        }
    }

//    operator fun plusAssign(other: Color) {
//        r += other.r
//        g += other.g
//        b += other.b
//        a += other.a
//    }

//    operator fun minusAssign(other: Color) {
//        r -= other.r
//        g -= other.g
//        b -= other.b
//        a -= other.a
//    }

    fun toArray(): FloatArray = values.toArray()

    fun rgb() = Vector3(values[0], values[1], values[2])

    companion object {

//        fun average(first: Color, second: Color): Color = (first + second) / 2.0f

//        fun mix(first: Color, second: Color, weight: Float) = (first * (1.0f - weight)) + (second * weight)

    }

}