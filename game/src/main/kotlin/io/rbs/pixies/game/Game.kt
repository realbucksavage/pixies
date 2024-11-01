package io.rbs.pixies.game

import io.rbs.pixes.engine.Level
import io.rbs.pixes.engine.Window
import io.rbs.pixies.game.geralt.Geralt
import java.awt.Dimension

fun main() {
    val mainWindow = Window()
    mainWindow.size = Dimension(800, 600)

    val geralt = Geralt()

    val level = Level("levels/test_room")
    println("loading player entity")
    level.addEntity(geralt)
    geralt.pos = level.center
    level.addKeyListener(geralt)
    level.addMouseListener(geralt)

    mainWindow.loadLevel(level)
    mainWindow.isVisible = true

    mainWindow.runGameLoop()
}