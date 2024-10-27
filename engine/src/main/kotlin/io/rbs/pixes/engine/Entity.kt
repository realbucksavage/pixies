package io.rbs.pixes.engine

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point

open class Entity(val id: String) : PaintedResource, TickAware {

    var pos: Point = Point(0, 0)
    var size: Dimension = Dimension(0, 0)

    var displayResource: PaintedResource? = null

    override fun paint(gfx: Graphics, interpolation: Double) {
        // does nothing
    }

    override fun nextTick() {
        // does nothing
    }
}