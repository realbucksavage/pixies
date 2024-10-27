package io.rbs.pixes.engine

import java.awt.Graphics

interface PaintedResource {
    fun paint(gfx: Graphics, interpolation: Double)

    fun reset() {
        // does nothing, override me
    }
}

interface TickAware {
    fun nextTick()
}