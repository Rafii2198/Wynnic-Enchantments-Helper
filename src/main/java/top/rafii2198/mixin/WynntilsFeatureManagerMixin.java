package top.rafii2198.mixin;

import com.wynntils.core.consumers.features.Feature;
import com.wynntils.core.consumers.features.FeatureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.rafii2198.wynntils.features.PlayerInfoFeature;

@Mixin(FeatureManager.class)
public abstract class WynntilsFeatureManagerMixin {
    @Shadow
    protected abstract void registerFeature(Feature feature);

    @Inject(method = "init", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        registerFeature(new PlayerInfoFeature());
    }
}
