package land.melon.lab.entityhider

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Config(
    @Expose
    @SerializedName("sightTracerThreadsAmount")
    val sightTracerThreadsAmount: Int = Runtime.getRuntime().availableProcessors(),
    @Expose
    @SerializedName("maxViewDistance")
    val maxViewDistance: Double = 96.0,
    @Expose
    @SerializedName("ExposureDistance")
    val exposureDistance: Double = 16.0,
    @Expose
    @SerializedName("ignorePassableBlocks")
    val ignorePassableBlocks: Boolean = true,
    @Expose
    @SerializedName("ignoreLiquidBlocks")
    val ignoreLiquidBlocks: Boolean = true,
    @Expose
    @SerializedName("alwaysShowTeammates")
    val alwaysShowTeammates: Boolean = true,
    @Expose
    @SerializedName("StrictMode")
    val strictMode: Boolean = false,
    @Expose
    @SerializedName("absoluteInvisibility")
    val absoluteInvisibility: Boolean = false,
    @Expose
    @SerializedName("ignoreBlocks")
    val ignoreBlocks: List<String> = mutableListOf(
        "LECTERN",
        "HOPPER",
        "DAYLIGHT_DETECTOR",
        "FARMLAND",
        "CHORUS_PLANT",
        "CHORUS_FLOWER",
        "LANTERN",
        "SOUL_LANTERN",
        "BELL",
        "STONECUTTER",
        "GRINDSTONE",
        "CHAIN",
        "IRON_BARS",
        "CACTUS",
        "ENCHANTING_TABLE",
        "END_PORTAL_FRAME",
        "ANVIL",
        "CHIPPED_ANVIL",
        "DAMAGED_ANVIL",
        "SLIME_BLOCK",
        "SCAFFOLDING",
        "FLOWER_POT",
        "SKELETON_SKULL",
        "WITHER_SKELETON_SKULL",
        "PLAYER_HEAD",
        "ZOMBIE_HEAD",
        "CREEPER_HEAD",
        "DRAGON_HEAD",
        "SKELETON_WALL_SKULL",
        "WITHER_SKELETON_WALL_SKULL",
        "PLAYER_WALL_HEAD",
        "ZOMBIE_WALL_HEAD",
        "CREEPER_WALL_HEAD",
        "DRAGON_WALL_HEAD",

        "OAK_SLAB",
        "SPRUCE_SLAB",
        "BIRCH_SLAB",
        "JUNGLE_SLAB",
        "ACACIA_SLAB",
        "DARK_OAK_SLAB",
        "CRIMSON_SLAB",
        "WARPED_SLAB",
        "STONE_SLAB",
        "SMOOTH_STONE_SLAB",
        "SANDSTONE_SLAB",
        "CUT_SANDSTONE_SLAB",
        "PETRIFIED_OAK_SLAB",
        "COBBLESTONE_SLAB",
        "BRICK_SLAB",
        "STONE_BRICK_SLAB",
        "NETHER_BRICK_SLAB",
        "QUARTZ_SLAB",
        "RED_SANDSTONE_SLAB",
        "CUT_RED_SANDSTONE_SLAB",
        "PURPUR_SLAB",
        "PRISMARINE_SLAB",
        "PRISMARINE_BRICK_SLAB",
        "DARK_PRISMARINE_SLAB",
        "POLISHED_GRANITE_SLAB",
        "SMOOTH_RED_SANDSTONE_SLAB",
        "MOSSY_STONE_BRICK_SLAB",
        "POLISHED_DIORITE_SLAB",
        "MOSSY_COBBLESTONE_SLAB",
        "END_STONE_BRICK_SLAB",
        "SMOOTH_SANDSTONE_SLAB",
        "SMOOTH_QUARTZ_SLAB",
        "GRANITE_SLAB",
        "ANDESITE_SLAB",
        "RED_NETHER_BRICK_SLAB",
        "POLISHED_ANDESITE_SLAB",
        "DIORITE_SLAB",
        "BLACKSTONE_SLAB",
        "POLISHED_BLACKSTONE_SLAB",
        "POLISHED_BLACKSTONE_BRICK_SLAB",

        "PURPUR_STAIRS",
        "OAK_STAIRS",
        "COBBLESTONE_STAIRS",
        "BRICK_STAIRS",
        "STONE_BRICK_STAIRS",
        "NETHER_BRICK_STAIRS",
        "SANDSTONE_STAIRS",
        "SPRUCE_STAIRS",
        "BIRCH_STAIRS",
        "JUNGLE_STAIRS",
        "CRIMSON_STAIRS",
        "WARPED_STAIRS",
        "QUARTZ_STAIRS",
        "ACACIA_STAIRS",
        "DARK_OAK_STAIRS",
        "PRISMARINE_STAIRS",
        "PRISMARINE_BRICK_STAIRS",
        "DARK_PRISMARINE_STAIRS",
        "RED_SANDSTONE_STAIRS",
        "POLISHED_GRANITE_STAIRS",
        "SMOOTH_RED_SANDSTONE_STAIRS",
        "MOSSY_STONE_BRICK_STAIRS",
        "POLISHED_DIORITE_STAIRS",
        "MOSSY_COBBLESTONE_STAIRS",
        "END_STONE_BRICK_STAIRS",
        "STONE_STAIRS",
        "SMOOTH_SANDSTONE_STAIRS",
        "SMOOTH_QUARTZ_STAIRS",
        "GRANITE_STAIRS",
        "ANDESITE_STAIRS",
        "RED_NETHER_BRICK_STAIRS",
        "POLISHED_ANDESITE_STAIRS",
        "DIORITE_STAIRS",
        "BLACKSTONE_STAIRS",
        "POLISHED_BLACKSTONE_STAIRS",
        "POLISHED_BLACKSTONE_BRICK_STAIRS",

        "CHEST",
        "TRAPPED_CHEST",
        "OAK_LEAVES",
        "SPRUCE_LEAVES",
        "BIRCH_LEAVES",
        "JUNGLE_LEAVES",
        "ACACIA_LEAVES",
        "DARK_OAK_LEAVES",
        "IRON_DOOR",
        "OAK_DOOR",
        "SPRUCE_DOOR",
        "BIRCH_DOOR",
        "JUNGLE_DOOR",
        "ACACIA_DOOR",
        "DARK_OAK_DOOR",
        "CRIMSON_DOOR",
        "WARPED_DOOR",
        "IRON_TRAPDOOR",
        "OAK_TRAPDOOR",
        "SPRUCE_TRAPDOOR",
        "BIRCH_TRAPDOOR",
        "JUNGLE_TRAPDOOR",
        "ACACIA_TRAPDOOR",
        "DARK_OAK_TRAPDOOR",
        "CRIMSON_TRAPDOOR",
        "WARPED_TRAPDOOR",
        "OAK_FENCE_GATE",
        "SPRUCE_FENCE_GATE",
        "BIRCH_FENCE_GATE",
        "JUNGLE_FENCE_GATE",
        "ACACIA_FENCE_GATE",
        "DARK_OAK_FENCE_GATE",
        "CRIMSON_FENCE_GATE",
        "WARPED_FENCE_GATE",
        "NETHER_BRICK_FENCE",
        "OAK_FENCE",
        "SPRUCE_FENCE",
        "BIRCH_FENCE",
        "JUNGLE_FENCE",
        "ACACIA_FENCE",
        "DARK_OAK_FENCE",
        "CRIMSON_FENCE",
        "WARPED_FENCE",
        "WHITE_BED",
        "ORANGE_BED",
        "MAGENTA_BED",
        "LIGHT_BLUE_BED",
        "YELLOW_BED",
        "LIME_BED",
        "PINK_BED",
        "GRAY_BED",
        "LIGHT_GRAY_BED",
        "CYAN_BED",
        "PURPLE_BED",
        "BLUE_BED",
        "BROWN_BED",
        "GREEN_BED",
        "RED_BED",
        "BLACK_BED",
        "GLASS",
        "GLASS_PANE",
        "TINTED_GLASS",
        "WHITE_STAINED_GLASS",
        "ORANGE_STAINED_GLASS",
        "MAGENTA_STAINED_GLASS",
        "LIGHT_BLUE_STAINED_GLASS",
        "YELLOW_STAINED_GLASS",
        "LIME_STAINED_GLASS",
        "PINK_STAINED_GLASS",
        "GRAY_STAINED_GLASS",
        "LIGHT_GRAY_STAINED_GLASS",
        "CYAN_STAINED_GLASS",
        "PURPLE_STAINED_GLASS",
        "BLUE_STAINED_GLASS",
        "BROWN_STAINED_GLASS",
        "GREEN_STAINED_GLASS",
        "RED_STAINED_GLASS",
        "BLACK_STAINED_GLASS",
        "WHITE_STAINED_GLASS_PANE",
        "ORANGE_STAINED_GLASS_PANE",
        "MAGENTA_STAINED_GLASS_PANE",
        "LIGHT_BLUE_STAINED_GLASS_PANE",
        "YELLOW_STAINED_GLASS_PANE",
        "LIME_STAINED_GLASS_PANE",
        "PINK_STAINED_GLASS_PANE",
        "GRAY_STAINED_GLASS_PANE",
        "LIGHT_GRAY_STAINED_GLASS_PANE",
        "CYAN_STAINED_GLASS_PANE",
        "PURPLE_STAINED_GLASS_PANE",
        "BLUE_STAINED_GLASS_PANE",
        "BROWN_STAINED_GLASS_PANE",
        "GREEN_STAINED_GLASS_PANE",
        "RED_STAINED_GLASS_PANE",
        "BLACK_STAINED_GLASS_PANE"
    )

)
