package teddy.minecraftautomation.utils;

public class Tooltip {
    private final String modId;
    private final String path;
    private final String tooltipName;
    private final String value;

    public Tooltip(String modId, String path, String tooltipName, String value) {
        this.modId = modId;
        this.path = path;
        this.tooltipName = tooltipName;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getTranslationKey() {
        return "block." + this.modId + "." + this.path + ".tooltip." + this.tooltipName;
    }
}
