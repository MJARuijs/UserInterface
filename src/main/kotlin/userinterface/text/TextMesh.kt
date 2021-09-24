package userinterface.text

import org.lwjgl.opengl.GL45.*

class TextMesh(vertices: FloatArray, textureCoords: FloatArray) {

    private val vao = glCreateVertexArrays()
    private val vbo = glCreateBuffers()
    private val tbo = glCreateBuffers()
    private var count = vertices.size / 2

    init {
        glNamedBufferData(vbo, vertices, GL_DYNAMIC_DRAW)
        glNamedBufferData(tbo, textureCoords, GL_DYNAMIC_DRAW)
    
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 8)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glEnableVertexArrayAttrib(vao, 0)
        
        glVertexArrayVertexBuffer(vao, 1, tbo, 0, 8)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 0)
        glEnableVertexArrayAttrib(vao, 1)
    }

    fun draw() {
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, count)
        glBindVertexArray(0)
    }

    fun destroy() {
        glDeleteBuffers(vbo)
        glDeleteBuffers(tbo)
        glDeleteVertexArrays(vao)
    }
}