package graphics

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE

enum class GraphicsOption(private val index: Int) {

    FACE_CULLING(GL_CULL_FACE),
    DEPTH_TESTING(GL_DEPTH_TEST),
    ALPHA_BLENDING(GL_BLEND),
    TEXTURE_MAPPING(GL_TEXTURE),
    DISTANCE_CLIPPING(GL_CLIP_PLANE0),
    MULTI_SAMPLING(GL_MULTISAMPLE);

    fun enable() {
        if (!glIsEnabled(index)) {
            glEnable(index)
        }
    }

    fun disable() {
        if (glIsEnabled(index)) {
            glDisable(index)
        }
    }

}