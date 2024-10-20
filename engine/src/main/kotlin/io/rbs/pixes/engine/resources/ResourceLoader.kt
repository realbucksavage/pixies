package io.rbs.pixes.engine.resources

import io.rbs.pixes.engine.getGameDir
import java.awt.Dimension
import java.awt.Graphics
import java.util.LinkedHashMap


class LRUCache<KeyT, ValT>(private val cap: Int, loadFactor: Float, accessOrder: Boolean) :
    LinkedHashMap<KeyT, ValT>(cap, loadFactor, accessOrder) {

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<KeyT, ValT>?): Boolean {
        return this.size > cap;
    }
}

interface Resource {
    fun paint(gfx: Graphics)
}

class ResourceLoader {
    companion object {
        private val graphicsCache = LRUCache<String, Resource>(1024, .75F, true)

        fun loadImage(name: String, size: Dimension): Resource {
            val path = "${getGameDir()}/$name"
            var res = graphicsCache[path]
            if (res != null) {
                return res
            }

            println("loading resource $path")

            res = ImageResource(path, size)
            graphicsCache[path] = res

            return res
        }
    }
}