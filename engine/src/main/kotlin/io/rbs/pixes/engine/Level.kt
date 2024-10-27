package io.rbs.pixes.engine

import io.rbs.pixes.engine.errors.AddEntityError
import io.rbs.pixes.engine.errors.RemoveEntityError
import java.awt.Component
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.io.FileReader
import java.io.Reader
import java.util.*

class Level(levelName: String) : Component(), PaintedResource {

    private val entities: MutableMap<String, Entity> = mutableMapOf()
    private var terrain: Array<Array<Tile>>

    private val tileDimension: Dimension
    private val levelDimension: Dimension

    val center: Point

    init {
        val baseName = "${getGameDir()}/$levelName"

        val levelProps = Properties()
        FileReader("$baseName.properties").use { levelProps.load(it) }

        tileDimension = Dimension(
            levelProps.getProperty("tileHeight").toInt(),
            levelProps.getProperty("tileWidth").toInt(),
        )

        levelDimension = Dimension(
            levelProps.getProperty("height").toInt(),
            levelProps.getProperty("width").toInt(),
        )

        val heightTotal = tileDimension.height * levelDimension.height
        val widthTotal = tileDimension.width * levelDimension.width
        center = Point(widthTotal / 2, heightTotal / 2)

        val tilePropsMap: MutableMap<String, TileOptions> = mutableMapOf()
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

            tilePropsMap[id] = tileOption
        }

        val terrainFile = "$baseName.trn"
        println("loading terrain: $terrainFile")
        this.terrain = FileReader(terrainFile).use { loadTerrain(it, levelDimension, tileDimension, tilePropsMap) }
    }

    fun addEntity(entity: Entity) {
        if (this.entities.containsKey(entity.id)) {
            throw AddEntityError(entity, "entity exists")
        }

        this.entities[entity.id] = entity
    }

    fun removeEntity(entity: Entity) {
        if (!this.entities.containsKey(entity.id)) {
            throw RemoveEntityError(entity, "entity doesn't exists")
        }

        this.entities.remove(entity.id)
    }

    override fun paint(gfx: Graphics, interpolation: Double) {
        for (tileRow in terrain) {
            for (tile in tileRow) {
                val newGfx = gfx.create(tile.pos.x, tile.pos.y, tile.size.width, tile.size.height)
                tile.paint(newGfx)
            }
        }

        this.entities.forEach { (id, ent) ->
            val newGfx = gfx.create(ent.pos.x, ent.pos.y, ent.size.width, ent.size.height)
            ent.paint(newGfx, interpolation)
        }
    }

    fun nextTick() {
        this.entities.forEach { it.value.nextTick() }
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

                tile.pos = Point(i * tileDimension.width, posY)
                tile.tClass = tileClass
                tile.tOptions = tileOption
            }

            lineNum++
        }

        return terrain
    }
}