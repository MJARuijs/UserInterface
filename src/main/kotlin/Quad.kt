import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.*

class Quad {

    private val vao = glGenVertexArrays()
    private val vbo = glGenBuffers()

    private val vertices = floatArrayOf(
        -1.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, 1.0f,

        1.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, -1.0f
    )

    init {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0)
        GL20.glEnableVertexAttribArray(0)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun draw() {
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, 6)
        glBindVertexArray(0)
    }

    fun destroy() {
        glDeleteVertexArrays(vao)
    }

}