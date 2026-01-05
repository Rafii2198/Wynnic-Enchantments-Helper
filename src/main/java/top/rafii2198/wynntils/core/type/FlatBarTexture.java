package top.rafii2198.wynntils.core.type;

import net.minecraft.resources.Identifier;

public enum FlatBarTexture {
    CLEAR(0),
    SEGMENTED_2(1),
    SEGMENTED_4(2);

    private final int level;
    private final int x = 119;
    private final int y = 30;
    private final Identifier identifier = Identifier.fromNamespaceAndPath("we-helper", "textures/gui/flat_bars.png");

    FlatBarTexture(int level) {
        this.level = level;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public int backgroundY1() {
        return y * level;
    }

    public int backgroundY2() {
        return y * level + y / 2;
    }

    public int foregroundY1() {
        return y * this.level + y / 2;
    }

    public int foregroundY2() {
        return y * this.level + y;
    }

    public int textureWidth() {
        return x;
    }

    public int textureHeight() {
        return y * values().length;
    }
}
