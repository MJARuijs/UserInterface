package userinterface.text.font

import graphics.textures.ImageMap
import resources.Resource
import userinterface.text.Character
import userinterface.text.MetaData

class Font(val textureAtlas: ImageMap, val metaData: MetaData, val characters: ArrayList<Character>) : Resource {

    override fun destroy() {
        textureAtlas.destroy()
    }
}