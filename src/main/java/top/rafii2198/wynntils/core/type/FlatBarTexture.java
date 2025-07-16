package top.rafii2198.wynntils.core.type;

import net.minecraft.resources.ResourceLocation;

public enum FlatBarTexture {
    CLEAR(0),
    SEGMENTED_2(1),
    SEGMENTED_4(2);

    private final int level;
    private final int x = 119;
    private final int y = 30;
    private final ResourceLocation resource =
            ResourceLocation.fromNamespaceAndPath("we-helper", "textures/gui/flat_bars.png");

    FlatBarTexture(int level) {
        this.level = level;
    }

    public ResourceLocation getResource() {
        return resource;
    }

    public float X1() {
        return 0;
    }

    public float X2() {
        return 1;
    }

    public float backgroundY1() {
        return (float) (y * this.level) / (values().length * y);
    }

    public float backgroundY2() {
        return (y * this.level + (float) y / 2) / (values().length * y);
    }

    public float foregroundY1() {
        return (y * (this.level + 1) - (float) y / 2) / (values().length * y);
    }

    public float foregroundY2() {
        return (float) (y * (this.level + 1)) / (values().length * y);
    }
}
