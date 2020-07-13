package graphics.models

import graphics.models.meshes.Mesh
import resources.Resource

data class Model(val shapes: List<Shape>): Resource {

    override fun destroy() {
        shapes.map(Shape::mesh).distinct().forEach(Mesh::destroy)
    }

}