package io.rbs.pixes.engine

import io.rbs.pixes.engine.errors.AddEntityError
import io.rbs.pixes.engine.errors.RemoveEntityError
import java.awt.Dimension
import java.awt.Graphics
import java.io.FileReader
import java.io.Reader
import java.util.*

class Level(levelName: String) {

    val entities: Map<String, Entity> = mutableMapOf()
    var terrain: Array<Array<Tile>>

    init {
        val baseName = "${getGameDir()}/$levelName"

        val levelProps = Properties()
        FileReader("$baseName.properties").use { levelProps.load(it) }

        val tileDimension = Dimension(
            levelProps.getProperty("tileHeight").toInt(),
            levelProps.getProperty("tileWidth").toInt(),
        )

        val levelDimension = Dimension(
            levelProps.getProperty("height").toInt(),
            levelProps.getProperty("width").toInt(),
        )

        var tilePropsMap: Map<String, TileOptions> = mutableMapOf()

        levelProps.forEach { (k, v) ->
            val key = k.toString()
            val value = v.toString()

            if (!key.startsWith("tile.")) {
                return@forEach
            }

            val splits = key.substring(5).split(".")
            if (splits.size != 2) {
                throw RuntimeException("key syntax error for $key: invalid number of dots")
            }

            val id = splits[0]
            val prop = splits[1]
            val tileOption = tilePropsMap.getOrDefault(id, TileOptions())

            when (prop) {
                "collision" -> tileOption.collision = value.trim().lowercase() == "true"
                "sprite" -> tileOption.sprite = value
            }

            tilePropsMap = tilePropsMap.plus(Pair(id, tileOption))
        }

        val terrainFile = "$baseName.trn"
        println("loading terrain: $terrainFile")
        this.terrain = FileReader(terrainFile).use { loadTerrain(it, levelDimension, tileDimension, tilePropsMap) }
    }

    fun addEntity(entity: Entity) {
        if (this.entities.containsKey(entity.id)) {
            throw AddEntityError(entity, "entity exists")
        }

        this.entities.plus(Pair(entity.id, entity))
    }

    fun removeEntity(entity: Entity) {
        if (!this.entities.containsKey(entity.id)) {
            throw RemoveEntityError(entity, "entity doesn't exists")
        }

        this.entities.minus(entity.id)
    }

    fun paint(gfx: Graphics) {
        for (tileRow in terrain) {
            for (tile in tileRow) {
                println("painting ${tile.tClass} tile at ${tile.posX},${tile.posY} -> ${tile.size.height}x${tile.size.width}")

                val newGfx = gfx.create(tile.posX, tile.posY, tile.size.width, tile.size.height)
                tile.paint(newGfx)
            }
        }
    }

    private fun loadTerrain(
        reader: Reader,
        levelDimension: Dimension,
        tileDimension: Dimension,
        tilePropsMap: Map<String, TileOptions>
    ): Array<Array<Tile>> {

        val terrain = Array(levelDimension.height) { Array(levelDimension.width) { Tile(tileDimension) } }

        var lineNum = 0
        reader.forEachLine { line ->
            val tileClasses = line.trim().split(",").map { it.trim() }
            if (tileClasses.size != levelDimension.width) {
                throw RuntimeException("line ${lineNum + 1}: invalid number of tiles")
            }

            val tiles = terrain[lineNum]
            val posY = lineNum * tileDimension.height

            tileClasses.forEachIndexed { i, tileClass ->
                val tile = tiles[i]
                val tileOption = tilePropsMap[tileClass]

                tile.posX = i * tileDimension.width
                tile.posY = posY
                tile.tClass = tileClass
                tile.tOptions = tileOption
            }

            lineNum++
        }

        return terrain
    }
}