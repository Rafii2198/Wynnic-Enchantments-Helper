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

    public int X1() {
        return 0;
    }

    public int X2() {
        return x;
    }

    public int backgroundY1() {
        return y * this.level;
    }

    public int backgroundY2() {
        return (y * this.level + y / 2);
    }

    public int foregroundY1() {
        return y * (this.level + 1) - y / 2;
    }

    public int foregroundY2() {
        return y * (this.level + 1);
    }
}
