package environment.sky

import graphics.Quad
import resources.images.ImageCache
import graphics.GraphicsContext
import graphics.GraphicsOption
import graphics.samplers.Sampler
import graphics.shaders.ShaderProgram
import graphics.textures.ImageMap

class Sky {

    private val shaderProgram = ShaderProgram.load("shaders/sky/gradient.vert", "shaders/sky/gradient.frag")
    private val sampler = Sampler(0)
    private val texture = ImageMap(ImageCache.get("textures/sky/gradient.png"))
    private val quad = Quad()

    fun render() {
        GraphicsContext.disable(GraphicsOption.DEPTH_TESTING)

        shaderProgram.start()
        shaderProgram.set("gradient", sampler.index)

        sampler.bind(texture)
        quad.draw()

        shaderProgram.stop()

        GraphicsContext.enable(GraphicsOption.DEPTH_TESTING)
    }

    fun destroy() {
        quad.destroy()
        texture.destroy()
        shaderProgram.destroy()
    }

}