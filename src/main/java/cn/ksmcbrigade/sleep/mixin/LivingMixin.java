package cn.ksmcbrigade.sleep.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingMixin {

    @Redirect(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z",ordinal = 0))
    public boolean noSleep(LivingEntity instance){
        return false;
    }
}
