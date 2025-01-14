package cn.ksmcbrigade.sleep.mixin;

import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Inject(method = {"bedWorks"},at = @At("RETURN"),cancellable = true)
    public void bed(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }
}