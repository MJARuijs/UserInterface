import math.vectors.Vector2
import userinterface.svg.SVGLoader
import userinterface.svg.Triangulation
import java.util.regex.Pattern

fun main() {
    val vertices = ArrayList<Vector2>()
    vertices += Vector2(0, 6) 
    vertices += Vector2(0, 0) 
    vertices += Vector2(3, 0) 
    vertices += Vector2(4, 1) 
    vertices += Vector2(6, 1) 
    vertices += Vector2(8, 0) 
    vertices += Vector2(12, 0) 
    vertices += Vector2(13, 2) 
    vertices += Vector2(8, 2) 
    vertices += Vector2(8, 4) 
    vertices += Vector2(11, 4) 
    vertices += Vector2(11, 6) 
    vertices += Vector2(6, 6) 
    vertices += Vector2(4, 3) 
    vertices += Vector2(2, 6) 
    
     val res = Triangulation().process(vertices)
    res?.forEach {
        point -> println("$point")
    }
}