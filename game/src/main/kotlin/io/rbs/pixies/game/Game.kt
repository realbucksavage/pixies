package io.rbs.pixies.game

import io.rbs.pixes.engine.Level
import io.rbs.pixes.engine.Window

fun main() {

    val mainWindow = Window()
    mainWindow.size = Window.DEFAULT_DIM
    mainWindow.loadLevel(Level("levels/test_room"))
    mainWindow.isVisible = true
}