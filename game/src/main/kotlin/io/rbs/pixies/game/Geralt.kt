package io.rbs.pixies.game

import io.rbs.pixes.engine.Entity
import io.rbs.pixes.engine.PaintedResource
import io.rbs.pixes.engine.resources.ResourceLoader
import java.awt.Dimension
import java.awt.Graphics

class Geralt : Entity("geralt") {

    init {
        size = Dimension(48, 48)
    }

    private val idleAnimation = ResourceLoader.loadAnimatedSprite(
        "geralt/idle.png",
        size,
        GameValues.animationRate,
        4
    )

    private val attackAnimations: Array<PaintedResource> = arrayOf(
        ResourceLoader.loadAnimatedSprite(
            "geralt/attack1.png",
            size,
            GameValues.animationRate,
            4
        ),
        ResourceLoader.loadAnimatedSprite(
            "geralt/attack2.png",
            size,
            GameValues.animationRate,
            4
        ),
        ResourceLoader.loadAnimatedSprite(
            "geralt/attack3.png",
            size,
            GameValues.animationRate,
            4
        ),
    )

    private var currentAnimation: PaintedResource? = idleAnimation
        set(value) {
            value?.reset()
            field = value
        }

    override fun paint(gfx: Graphics, interpolation: Double) {
        currentAnimation?.paint(gfx, interpolation)
    }
}