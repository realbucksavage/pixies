package io.rbs.pixes.engine.resources

import io.rbs.pixes.engine.PaintedResource
import io.rbs.pixes.engine.getGameDir
import java.awt.Dimension
import java.util.LinkedHashMap


class LRUCache<KeyT, ValT>(private val cap: Int, loadFactor: Float, accessOrder: Boolean) :
    LinkedHashMap<KeyT, ValT>(cap, loadFactor, accessOrder) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<KeyT, ValT>?): Boolean {
        return this.size > cap;
    }
}

class ResourceLoader {
    companion object {
        private val graphicsCache = LRUCache<String, PaintedResource>(1024, .75F, true)

        fun loadImage(name: String, size: Dimension): PaintedResource {
            val path = "${getGameDir()}/$name"
            var res = graphicsCache[path]
            if (res != null) {
                return res
            }

            println("loading resource $path")

            res = ImagePaintedResource(path, size)
            graphicsCache[path] = res

            return res
        }

        fun loadAnimatedSprite(
            name: String,
            size: Dimension,
            rate: Int,
            frames: Int,
        ): PaintedResource {
            val path = "${getGameDir()}/$name"
            val cacheKey = "$path:$rate@$frames"

            var res = graphicsCache[cacheKey]
            if (res != null) {
                return res
            }

            println("loading resource $path")

            res = AnimatedSpriteResource(path, size, rate, frames)
            graphicsCache[cacheKey] = res

            return res
        }
    }
}