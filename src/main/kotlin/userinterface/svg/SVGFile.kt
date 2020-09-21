package userinterface.svg

import resources.Resource

class SVGFile(val svgMesh: SVGMesh) : Resource {
    override fun destroy() {
        svgMesh.destroy()
    }
}