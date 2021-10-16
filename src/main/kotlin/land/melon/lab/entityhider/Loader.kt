package land.melon.lab.entityhider

import com.google.gson.GsonBuilder
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.BlockIterator
import org.bukkit.util.Vector
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Loader : JavaPlugin(), Listener {

    private val configFile = File(dataFolder, "config.json")
    private val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
    private val visiblePlayerMap: ConcurrentHashMap<UUID, Set<UUID>> = ConcurrentHashMap()
    private val ignoreBlocks = HashSet<Material>()

    override fun onEnable() {
        dataFolder.mkdir()
        configFile.createNewFile()
        val config = readConfig(configFile)
        saveConfig(config, configFile)

        for (blockName in config.ignoreBlocks) {
            try {
                ignoreBlocks.add(Material.valueOf(blockName))
            } catch (e: IllegalArgumentException) {
                this.logger.warning("$blockName is not a legal material name in this version of bukkit(case sensitive). Please make sure that you are using the corresponding bukkit version.")
            }
        }

        //setup refresher
        this.server.scheduler.runTaskTimer(this, Runnable {
            for (sourcePlayer in this.server.onlinePlayers) {
                val visiblePlayers = HashSet<UUID>()
                for (target in sourcePlayer.world.entities) {
                    //TODO for debug only
                    if (target !is Zombie)
                        continue

                    val sourceLocation = sourcePlayer.location.add(EYE)
                    val targetPlayerLocation = target.location
                    val targetCorner1 = targetPlayerLocation.clone().add(CORNER1)
                    val targetCorner2 = targetPlayerLocation.clone().add(CORNER2)
                    val targetCorner3 = targetPlayerLocation.clone().add(CORNER3)
                    val targetCorner4 = targetPlayerLocation.clone().add(CORNER4)

                    if (isVisible(sourceLocation, targetCorner1, config.maxViewDistance, ignoreBlocks) || isVisible(
                            sourceLocation,
                            targetCorner2,
                            config.maxViewDistance,
                            ignoreBlocks
                        ) || isVisible(
                            sourceLocation,
                            targetCorner3,
                            config.maxViewDistance,
                            ignoreBlocks
                        ) || isVisible(sourceLocation, targetCorner4, config.maxViewDistance, ignoreBlocks)
                    )
                        visiblePlayers.add(target.uniqueId)

                }
                // TODO("send differential packets here")
                val differential =
                    differential(visiblePlayerMap.getOrDefault(sourcePlayer.uniqueId, HashSet()), visiblePlayers)
                if (differential.first.isNotEmpty())
                    sourcePlayer.sendMessage("${differential.first} left your sight.")
                if (differential.second.isNotEmpty())
                    sourcePlayer.sendMessage("${differential.second} entered your sight.")
                visiblePlayerMap[sourcePlayer.uniqueId] = visiblePlayers
            }
        }, 0L, 1L)
    }

    private fun <T> differential(originalSet: Set<T>, newSet: Set<T>): Pair<Set<T>, Set<T>> {
        return Pair(originalSet subtract newSet, newSet subtract originalSet)
    }

    private fun isVisible(
        sourceLocation: Location,
        targetLocation: Location,
        maxVisibleDistance: Int,
        transparentBlocks: Set<Material>
    ): Boolean {

        if (sourceLocation.distance(targetLocation) > maxVisibleDistance)
            return false

        val blockIterator = BlockIterator(
            sourceLocation.world!!,
            sourceLocation.toVector(),
            Vector(
                targetLocation.x - sourceLocation.x,
                targetLocation.y - sourceLocation.y,
                targetLocation.z - sourceLocation.z
            ),
            0.0,
            sourceLocation.distance(targetLocation).toInt()
        )

        for (block in blockIterator) {
            if (!block.isEmpty && block.type !in transparentBlocks) {
                return false
            }
        }
        return true
    }

    private fun readConfig(configFile: File): Config {
        return gson.fromJson(FileReader(configFile), Config::class.java) ?: Config()
    }

    private fun saveConfig(configInstance: Config, configFile: File) {
        val writer = FileWriter(configFile, false)
        writer.write(gson.toJson(configInstance))
        writer.flush()
        writer.close()
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        visiblePlayerMap.remove(event.player.uniqueId)
    }

    companion object {
        private val EYE = Vector(0.0, 1.62, 0.0)
        private val CORNER1 = Vector(0.5, 0.1, 0.5)
        private val CORNER2 = Vector(-0.5, 0.1, -0.5)
        private val CORNER3 = Vector(0.5, 2.0, -0.5)
        private val CORNER4 = Vector(-0.5, 2.0, 0.5)
    }
}
