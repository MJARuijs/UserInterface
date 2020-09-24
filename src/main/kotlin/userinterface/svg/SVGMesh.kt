package userinterface.svg

import math.vectors.Vector2
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL30.*

class SVGMesh(private val vertices: FloatArray) {
    
    private val vao = glGenVertexArrays()
    private val vbo = glGenBuffers()
    
    private val children = ArrayList<SVGMesh>()

    init {
        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }
    
    operator fun plusAssign(child: SVGMesh) {
        children += child
    }
    
    operator fun plusAssign(childPoints: ArrayList<Vector2>) {
        var childVertices = FloatArray(0)
        childPoints.forEach { childPoint ->
            childVertices += childPoint.toArray()
        }
        children += SVGMesh(childVertices)
    }

    fun draw() {
        glBindVertexArray(vao)
//        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE)
//        glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.size / 2)
        glDrawArrays(GL11.GL_LINE_STRIP, 0, vertices.size / 2)
//        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL)
    
        glBindVertexArray(0)
        for (i in 0 until children.size) {
            children[i].draw()
        }

        
    }

    fun destroy() {
        glDeleteBuffers(vbo)
        glDeleteVertexArrays(vao)
        children.forEach { child -> child.destroy() }
    
    }
}