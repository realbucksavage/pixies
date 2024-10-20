package io.rbs.pixes.engine.resources

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Image
import java.io.FileInputStream
import javax.imageio.ImageIO

class ImageResource(path: String, private val size: Dimension) : Resource {

    private val image: Image

    init {
        FileInputStream(path).use {
            this.image = ImageIO.read(it).getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH)
        }
    }


    override fun paint(gfx: Graphics) {
        gfx.drawImage(this.image, 0, 0, size.width, size.height, null, null)
    }
}