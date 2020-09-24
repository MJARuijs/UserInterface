import math.vectors.Vector2
import userinterface.svg.SVGLoader
import userinterface.svg.Triangulation
import java.util.regex.Pattern

fun main() {
    val vertices = ArrayList<Vector2>()
    
    vertices += Vector2(0.556698f, -0.14745313f)
    vertices += Vector2(0.51674837f, -0.14745313f)
    vertices += Vector2(0.17831258f, -0.74649024f)
    vertices += Vector2(0.04822343f, -0.51623243f)
    vertices += Vector2(0.008273747f, -0.51623243f)
    vertices += Vector2(0.008273747f, -0.5869414f)
    vertices += Vector2(0.15833776f, -0.8525508f)
    vertices += Vector2(0.19828744f, -0.8525508f)
    vertices += Vector2(0.556698f, -0.21816406f)
    vertices += Vector2(0.556698f, -0.14745313f)
    vertices += Vector2(0.556698f, -0.14745313f)
    
    val res = Triangulation().process(vertices)
    res?.forEach {
        point -> println("$point")
    }
}