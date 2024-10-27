package io.rbs.pixes.engine

import io.rbs.pixes.engine.resources.ResourceLoader
import java.awt.Dimension
import java.awt.Graphics

class Tile(val size: Dimension) {
    private var resource: PaintedResource? = null

    var posX: Int = 0
    var posY: Int = 0

    var tClass: String? = null

    var tOptions: TileOptions? = null
        set(tOptions) {
            if (!tOptions?.sprite.isNullOrEmpty()) {
                this.resource = ResourceLoader.loadImage(tOptions.sprite!!, this.size)
            }

            field = tOptions
        }

    fun paint(gfx: Graphics) {
        resource?.paint(gfx, 0.0)
    }
}