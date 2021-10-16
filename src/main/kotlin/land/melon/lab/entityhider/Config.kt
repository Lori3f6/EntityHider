package land.melon.lab.entityhider

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Config(
    @Expose
    @SerializedName("maxVDistance")
    val maxViewDistance: Int = 64,
    @Expose
    @SerializedName("frontViewAngle")
    val frontViewAngle: Double = 90.toDouble(),
    @Expose
    @SerializedName("backViewAngle")
    val backViewAngle: Double = 90.toDouble(),
    @Expose
    @SerializedName("pitchViewAngle")
    val pitchViewAngle: Double = 90.toDouble(),

    @Expose
    @SerializedName("ignoreBlocks")
    val ignoreBlocks: List<String> = mutableListOf(
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
