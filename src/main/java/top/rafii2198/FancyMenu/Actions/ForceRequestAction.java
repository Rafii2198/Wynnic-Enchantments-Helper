package top.rafii2198.FancyMenu.Actions;

import de.keksuccino.fancymenu.customization.action.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rafii2198.RemoteData.RemoteManager;

public class ForceRequestAction extends Action {

    public ForceRequestAction() {
        super("we-force-request");
    }

    @Override
    public boolean hasValue() {
        return false;
    }

    @Override
    public void execute(@Nullable String s) {
        RemoteManager.ForceRequestAll();
    }

    @Override
    public @NotNull Component getActionDisplayName() {
        return Component.translatable("we-helper.fm.action.we-force-request");
    }

    @Override
    public @NotNull Component[] getActionDescription() {
        return new MutableComponent[] {Component.translatable("we-helper.fm.action.we-force-request.desc")};
    }

    @Override
    public @Nullable Component getValueDisplayName() {
        return null;
    }

    @Override
    public @Nullable String getValueExample() {
        return null;
    }
}
