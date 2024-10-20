package io.rbs.pixes.engine

import java.awt.Dimension
import java.awt.Graphics
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import javax.swing.JFrame
import javax.swing.JPanel

class Window(private val device: GraphicsDevice) : JFrame() {
    companion object {
        val DEFAULT_DIM = Dimension(1366, 768)
    }

    constructor() : this(GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    fun loadLevel(level: Level) {
        contentPane.removeAll()
        contentPane.add(LevelPane(level))
    }

    class LevelPane(private val level: Level) : JPanel() {
        override fun paint(g: Graphics) {
            super.paintComponents(g)
            level.paint(g);
        }
    }
}