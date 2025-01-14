package cn.ksmcbrigade.sleep.mixin;

import net.minecraft.world.level.block.BedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(method = {"canSetSpawn"},at = @At("RETURN"),cancellable = true)
    private static void can(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }
}
