package io.rbs.pixies.game

import io.rbs.pixes.engine.Level
import io.rbs.pixes.engine.Window

fun main() {
    val mainWindow = Window()
    mainWindow.size = Window.DEFAULT_DIM

    val level = Level("levels/test_room")
    println("loading player entity")
    val geralt = Geralt()
    level.addEntity(geralt)
    geralt.pos = level.center

    mainWindow.loadLevel(level)
    mainWindow.isVisible = true

    mainWindow.runGameLoop()
}