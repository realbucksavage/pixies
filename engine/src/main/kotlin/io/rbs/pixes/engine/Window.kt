package io.rbs.pixes.engine

import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import javax.swing.JFrame

class Window(private val device: GraphicsDevice) : JFrame() {
    companion object {
        val DEFAULT_DIM = Dimension(1366, 768)
    }

    constructor() : this(GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice)

    private var tickRate: Int = 50
    private var maxFrameSkip: Int = 10
    private var skipTicks = 1000 / tickRate;

    private var gameLoopRunning = false
    private var interpolation: Double = 0.0

    private var currentLevel: Level? = null
    private var mainComponent: Component? = null

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    fun setRates(tickRate: Int, maxFrameSkip: Int) {
        this.tickRate = tickRate
        this.maxFrameSkip = maxFrameSkip
        this.skipTicks = 1000 / tickRate
    }

    fun getRates(): Pair<Int, Int> {
        return Pair(this.tickRate, this.maxFrameSkip)
    }

    fun loadLevel(level: Level) {
        val comp = InterpolatedComponent(level, this)
        contentPane.removeAll()
        contentPane.add(comp)
        this.currentLevel = level
        this.mainComponent = comp
    }

    fun unloadLevel() {
        contentPane.removeAll()
        this.currentLevel = null
        this.mainComponent = null
    }

    fun runGameLoop() {
        this.gameLoopRunning = true
        var nextGameTick = System.currentTimeMillis()
        var loops = 0

        val gameThread = Thread {
            while (this.gameLoopRunning) {
                while (System.currentTimeMillis() > nextGameTick && loops < this.maxFrameSkip) {
                    nextTick()
                    nextGameTick += skipTicks
                    loops++
                }

                interpolation = (System.currentTimeMillis() + skipTicks - nextGameTick / skipTicks.toDouble())
                this.mainComponent?.repaint()
            }
        }

        gameThread.start()
    }

    private fun nextTick() {
        this.currentLevel ?: run {
            this.gameLoopRunning = false
            println("stopping game loop: no loaded level")
            return
        }

        this.currentLevel!!.nextTick()
    }

    private class InterpolatedComponent(private var paintedResource: PaintedResource, private var window: Window) :
        Component() {
        override fun paint(g: Graphics) {
            println("painting with interpolation ${window.interpolation}")
            paintedResource.paint(g, window.interpolation)
        }
    }
}