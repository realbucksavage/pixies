package io.rbs.pixes.engine

import io.rbs.pixes.engine.resources.ResourceLoader
import java.awt.Dimension
import java.awt.Graphics

class Tile(val size: Dimension) {
    var posX: Int = 0
    var posY: Int = 0

    var tClass: String? = null
    var tOptions: TileOptions? = null

    fun paint(gfx: Graphics) {
        if (tOptions?.sprite.isNullOrEmpty()) {
            return
        }

        val resource = ResourceLoader.loadImage(tOptions?.sprite!!, this.size)
        resource.paint(gfx)
    }
}