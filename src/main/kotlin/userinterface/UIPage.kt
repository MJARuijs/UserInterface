package userinterface

import graphics.shaders.ShaderProgram
import userinterface.items.Item

class UIPage(private val name: String, private val items: ArrayList<Item> = ArrayList()) {

    fun draw(shaderProgram: ShaderProgram) {

    }

    fun destroy() {
        items.forEach { item -> item.destroy() }
    }

}