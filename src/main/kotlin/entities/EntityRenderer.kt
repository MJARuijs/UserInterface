package entities

import graphics.Camera
import graphics.lights.AmbientLight
import graphics.lights.DirectionalLight
import graphics.shaders.ShaderProgram

object EntityRenderer {

    private val shaderProgram = ShaderProgram.load("shaders/entity.vert", "shaders/entity.frag")

    fun render(camera: Camera, entities: List<Entity>, ambient: AmbientLight, directional: DirectionalLight) {

        shaderProgram.start()
        shaderProgram.set("projection", camera.projectionMatrix)
        shaderProgram.set("view", camera.viewMatrix)
        shaderProgram.set("ambient.color", ambient.color)
        shaderProgram.set("sun.color", directional.color)
        shaderProgram.set("sun.direction", directional.direction)
        shaderProgram.set("cameraPosition", camera.position)

        for (entity in entities) {
            val model = entity.model
            val transformation = entity.transformation
            shaderProgram.set("model", transformation)
            for (shape in model.shapes) {
                shaderProgram.set("material.ambient", shape.material.ambient)
                shaderProgram.set("material.diffuse", shape.material.diffuse)
                shaderProgram.set("material.specular", shape.material.specular)
                shaderProgram.set("material.shininess", shape.material.shininess)
                shape.mesh.draw()
            }
        }

        shaderProgram.stop()
    }

    fun destroy() {
        shaderProgram.destroy()
    }

}