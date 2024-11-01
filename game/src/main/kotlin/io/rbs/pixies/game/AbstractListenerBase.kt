package io.rbs.pixies.game

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

interface AbstractListenerBase : KeyListener, MouseListener {
    override fun keyTyped(e: KeyEvent) {
        // does nothing
    }

    override fun keyPressed(e: KeyEvent) {
        // does nothing
    }

    override fun keyReleased(e: KeyEvent) {
        // does nothing
    }

    override fun mouseClicked(e: MouseEvent) {
        // does nothing
    }

    override fun mousePressed(e: MouseEvent) {
        // does nothing
    }

    override fun mouseReleased(e: MouseEvent) {
        // does nothing
    }

    override fun mouseEntered(e: MouseEvent) {
        // does nothing
    }

    override fun mouseExited(e: MouseEvent) {
        // does nothing
    }
}