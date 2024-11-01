package io.rbs.pixies.game.geralt

import io.rbs.pixes.engine.Direction
import io.rbs.pixes.engine.Entity
import io.rbs.pixes.engine.PaintedResource
import io.rbs.pixes.engine.resources.ResourceLoader
import io.rbs.pixies.game.AbstractListenerBase
import io.rbs.pixies.game.GameValues
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.util.Collections

class Geralt : Entity("geralt"), AbstractListenerBase {

    private val moveSpeed = 4

    private var playerState = PlayerState.IDLE
    private var movementDirections = Collections.synchronizedSet<Direction>(mutableSetOf())

    init {
        size = Dimension(64, 64)
    }

    private val idleAnimation = ResourceLoader.loadAnimatedSprite(
        "geralt/idle.png",
        size,
        GameValues.Companion.animationRate,
        4,
        48
    )

    private val runAnimation = ResourceLoader.loadAnimatedSprite(
        "geralt/run.png",
        size,
        GameValues.Companion.animationRate,
        6,
        48
    )

    private val attackAnimations: Array<PaintedResource> = arrayOf(
        ResourceLoader.loadAnimatedSprite(
            "geralt/attack1.png",
            size,
            GameValues.Companion.animationRate,
            6,
            48
        ),
        ResourceLoader.loadAnimatedSprite(
            "geralt/attack2.png",
            size,
            GameValues.Companion.animationRate,
            6,
            48
        ),
        ResourceLoader.loadAnimatedSprite(
            "geralt/attack3.png",
            size,
            GameValues.Companion.animationRate,
            6,
            48
        ),
    )

    init {
        displayResource = idleAnimation
    }

    override fun nextTick() {
        if (playerState == PlayerState.MOVE) {
            movementDirections.forEach {
                when (it) {
                    Direction.UP -> pos.y -= moveSpeed
                    Direction.LEFT -> pos.x -= moveSpeed
                    Direction.DOWN -> pos.y += moveSpeed
                    Direction.RIGHT -> pos.x += moveSpeed
                }
            }
        }
    }

    override fun keyPressed(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_W -> startMovement(Direction.UP)
            KeyEvent.VK_A -> startMovement(Direction.LEFT)
            KeyEvent.VK_S -> startMovement(Direction.DOWN)
            KeyEvent.VK_D -> startMovement(Direction.RIGHT)
        }
    }

    override fun keyReleased(e: KeyEvent) {
        when (e.keyCode) {
            KeyEvent.VK_W -> stopMovement(Direction.UP)
            KeyEvent.VK_A -> stopMovement(Direction.LEFT)
            KeyEvent.VK_S -> stopMovement(Direction.DOWN)
            KeyEvent.VK_D -> stopMovement(Direction.RIGHT)
        }
    }

    override fun mousePressed(e: MouseEvent) {

        stopMovement(Direction.UP)
        stopMovement(Direction.LEFT)
        stopMovement(Direction.DOWN)
        stopMovement(Direction.RIGHT)

        if (playerState != PlayerState.ATTACK) {
            playerState = PlayerState.ATTACK
            displayResource = attackAnimations.random()
        }
    }

    private fun startMovement(direction: Direction) {
        // can't move while attacking
        if (playerState == PlayerState.ATTACK) {
            return
        }

        if (playerState != PlayerState.MOVE) {
            playerState = PlayerState.MOVE
            displayResource = runAnimation
        }

        movementDirections.add(direction)
    }

    private fun stopMovement(direction: Direction) {
        movementDirections.remove(direction)

        if (movementDirections.isEmpty() && playerState == PlayerState.MOVE) {
            playerState = PlayerState.IDLE
            displayResource = idleAnimation
        }
    }
}