package teddy.minecraftautomation.utils;

public class Tooltip {
    private final String modId;
    private final String blockPath;
    private final String tooltipName;
    private final String value;

    public Tooltip(String modId, String blockPath, String tooltipName, String value) {
        this.modId = modId;
        this.blockPath = blockPath;
        this.tooltipName = tooltipName;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getTranslationKey() {
        return "block." + this.modId + "." + this.blockPath + ".tooltip." + this.tooltipName;
    }

    public static float getSeconds(float ticks) {
        float seconds = ticks / 20f;

        // Round to .01
        return ((float) Math.round(seconds * 100f)) / 100f;
    }
}
