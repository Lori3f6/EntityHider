package land.melon.lab.entityhider

import com.comphenix.protocol.PacketType.Play.Server.*
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.google.gson.GsonBuilder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.BlockIterator
import org.bukkit.util.Vector
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.concurrent.ConcurrentHashMap


class Loader : JavaPlugin(), Listener {
    private val configFile = File(dataFolder, "config.json")
    private val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
    private val visibleEntityMap: ConcurrentHashMap<UUID, Set<UUID>> = ConcurrentHashMap()
    private val ignoreBlocks = HashSet<Material>()

    override fun onEnable() {
        dataFolder.mkdir()
        configFile.createNewFile()
        val config = readConfig(configFile)
        saveConfig(config, configFile)

        for (blockName in config.ignoreBlocks) {
            try {
                ignoreBlocks.add(Material.valueOf(blockName))
            } catch (exception: IllegalArgumentException) {
                this.logger.warning("$blockName is not a legal material name in this version of bukkit(case sensitive). Please make sure that you are using the corresponding bukkit version.")
            }
        }

        //setup protocol manager
        val protocolManager = ProtocolLibrary.getProtocolManager()

        // packet filter to prevent updating hidden entity
        protocolManager.addPacketListener(
            object : PacketAdapter(this, ListenerPriority.NORMAL, ENTITY_METADATA, ENTITY_VELOCITY, ENTITY_SOUND) {
                override fun onPacketSending(event: PacketEvent?) {
                    when (event?.packet?.type) {
                        ENTITY_METADATA,
                        ENTITY_VELOCITY -> {
                            event!!.isCancelled = event.packet.integers.values[0] !in visibleEntityMap.getOrDefault(
                                event.player.uniqueId,
                                HashSet()
                            ).map { uuid -> Bukkit.getEntity(uuid)!!.entityId }
                        }
                        ENTITY_SOUND -> {
                            event!!.isCancelled = event.packet.integers.values[2] !in visibleEntityMap.getOrDefault(
                                event.player.uniqueId,
                                HashSet()
                            ).map { uuid -> Bukkit.getEntity(uuid)!!.entityId }
                        }
                    }
                }
            }
        )

        //setup refresher
        this.server.scheduler.runTaskTimer(this, Runnable {
            val worldEntityMap =
                server.worlds.fold(HashMap<String, List<Player>>()) { map, world ->
                    map[world.name] = world.players; map
                }

            for (observer in this.server.onlinePlayers) {
                val visiblePlayersEntityIDSet = HashSet<UUID>()
                for (target in worldEntityMap[observer.world.name]!!.iterator()) {
                    val sourceLocation = observer.location.add(if(observer.isSneaking) SNEAK_EYE else STAND_EYE)
                    val targetPlayerLocation = target.location
                    val targetCorner1 = targetPlayerLocation.clone().add(CORNER1)
                    val targetCorner2 = targetPlayerLocation.clone().add(CORNER2)
                    val targetCorner3 = targetPlayerLocation.clone().add(CORNER3)
                    val targetCorner4 = targetPlayerLocation.clone().add(if(target.isSneaking) CORNER4_SNEAK else CORNER4_STAND)

                    if (isVisible(
                            sourceLocation,
                            targetCorner1,
                            config.maxViewDistance,config.exposureDistance,
                            config.ignorePassableBlocks, config.ignoreLiquidBlocks,
                            ignoreBlocks
                        ) || isVisible(
                            sourceLocation,
                            targetCorner2,
                            config.maxViewDistance,config.exposureDistance,
                            config.ignorePassableBlocks, config.ignoreLiquidBlocks,
                            ignoreBlocks
                        ) || isVisible(
                            sourceLocation,
                            targetCorner3,
                            config.maxViewDistance,config.exposureDistance,
                            config.ignorePassableBlocks, config.ignoreLiquidBlocks,
                            ignoreBlocks
                        ) || isVisible(
                            sourceLocation,
                            targetCorner4,
                            config.maxViewDistance,config.exposureDistance,
                            config.ignorePassableBlocks, config.ignoreLiquidBlocks,
                            ignoreBlocks
                        )
                    )
                        visiblePlayersEntityIDSet.add(target.uniqueId)

                }
                val differential =
                    differential(
                        visibleEntityMap.getOrDefault(observer.uniqueId, HashSet()),
                        visiblePlayersEntityIDSet
                    )

                // send destroy packet here
                val destroyEntityPacket = PacketContainer(ENTITY_DESTROY)
                destroyEntityPacket.intLists.write(
                    0,
                    differential.first.filter { uuid -> Bukkit.getEntity(uuid) != null }
                        .map { uuid -> Bukkit.getEntity(uuid)!!.entityId }.toList()
                )
                try {
                    protocolManager.sendServerPacket(observer, destroyEntityPacket)
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }

                // send update packet here
                for (newUUID in differential.second) {
                    protocolManager.updateEntity(Bukkit.getEntity(newUUID)!!, listOf(observer))
                }

//                if (differential.first.isNotEmpty())
//                    observer.sendMessage("${differential.first} left your sight.")
//                if (differential.second.isNotEmpty())
//                    observer.sendMessage("${differential.second} entered your sight.")
                visibleEntityMap[observer.uniqueId] = visiblePlayersEntityIDSet
            }
        }, 0L, 1L)

    }

    private fun <T> differential(originalSet: Set<T>, newSet: Set<T>): Pair<Set<T>, Set<T>> {
        return Pair(originalSet subtract newSet, newSet subtract originalSet)
    }

    private fun isVisible(
        sourceLocation: Location,
        targetLocation: Location,
        maxVisibleDistance: Double,
        exposureDistance:Double,
        ignorePassableBlock: Boolean,
        ignoreLiquidBlocks: Boolean,
        ignoredBlocks: Set<Material>
    ): Boolean {

        val distance = sourceLocation.distance(targetLocation)

        if (distance < 1)
            return true
        else if(distance < exposureDistance)
            return true
        else if(distance > maxVisibleDistance)
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
            if (block.isEmpty || (ignorePassableBlock && block.isPassable) || (ignoreLiquidBlocks && block.isLiquid) || block.type in ignoredBlocks)
                continue
            else
                return false
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
        visibleEntityMap.remove(event.player.uniqueId)
    }

    companion object {
        private val STAND_EYE = Vector(0.0, 1.62, 0.0)
        private val SNEAK_EYE = Vector(0.0,1.27,0.0)
        private val CORNER1 = Vector(0.4, 0.1, 0.4)
        private val CORNER2 = Vector(-0.4, 0.6, 0.4)
        private val CORNER3 = Vector(-0.4, 1.2, -0.4)
        private val CORNER4_SNEAK = Vector(0.4, 1.65, -0.4)
        private val CORNER4_STAND = Vector(0.4, 1.8, -0.4)
    }
}
