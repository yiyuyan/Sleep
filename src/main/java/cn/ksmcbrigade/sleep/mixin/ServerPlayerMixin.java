package cn.ksmcbrigade.sleep.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Shadow public abstract ServerLevel serverLevel();

    @Inject(method = "bedInRange",at = @At("RETURN"),cancellable = true)
    public void range(BlockPos p_9117_, Direction p_9118_, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(true);
    }

    @Inject(method = "bedBlocked",at = @At("RETURN"),cancellable = true)
    public void block(BlockPos p_9117_, Direction p_9118_, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }

    @Redirect(method = "readAdditionalSaveData",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSleeping()Z"))
    public boolean noSleep(ServerPlayer instance){
        return false;
    }

    @Redirect(method = "startSleepInBed",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/DimensionType;natural()Z"))
    public boolean natural(DimensionType instance){
        return true;
    }

    @Redirect(method = "startSleepInBed",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/BlockPos;FZZ)V"))
    public void start(ServerPlayer instance, ResourceKey<Level> levelResourceKey, BlockPos p_9159_, float p_9160_, boolean p_9161_, boolean p_9162_){
        if(this.serverLevel().getBlockState(p_9159_).isBed(this.serverLevel(),p_9159_,instance) && this.serverLevel().dimensionType().natural()){
            instance.setRespawnPosition(levelResourceKey,p_9159_,p_9160_,p_9161_,p_9162_);
        }
    }
}
