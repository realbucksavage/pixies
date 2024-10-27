package io.rbs.pixes.engine.resources

import io.rbs.pixes.engine.PaintedResource
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Image
import java.io.FileInputStream
import javax.imageio.ImageIO

class AnimatedSpriteResource(
    path: String,
    private val size: Dimension,
    private val rate: Int,
    private val frames: Int
) : PaintedResource {

    private val image: Image

    init {
        FileInputStream(path).use {
            this.image = ImageIO.read(it)
        }
    }

    var currentFrame = 0
    var paintCounter = 0

    override fun paint(gfx: Graphics, interpolation: Double) {
        paintCounter++
        if (paintCounter >= rate) {
            currentFrame = (currentFrame + 1) % frames
            paintCounter = 0  // Reset the counter
        }

        val offsetX = currentFrame * size.width

        gfx.drawImage(
            this.image,
            0,
            0,
            size.width,
            size.height,
            offsetX,
            0,
            offsetX + size.width,
            size.height,
            null
        )
    }

    override fun reset() {
        this.currentFrame = 0
    }
}