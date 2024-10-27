package io.rbs.pixes.engine

import io.rbs.pixes.engine.resources.ResourceLoader
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point

class Tile(val size: Dimension) {
    private var resource: PaintedResource? = null

    var pos: Point = Point(0, 0)

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