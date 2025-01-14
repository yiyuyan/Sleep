package cn.ksmcbrigade.sleep.client;

import cn.ksmcbrigade.sleep.ClientRegistry;
import cn.ksmcbrigade.sleep.Sleep;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Sleep.MOD_ID,value = Dist.CLIENT)
public class SleepClient {

    private static final KeyMapping BLOCK_SLEEP = ClientRegistry.registerKeyBinding(new KeyMapping("key.sleep.sleep_block", InputConstants.KEY_I,KeyMapping.CATEGORY_GAMEPLAY));
    private static final KeyMapping PLAYER_SLEEP = ClientRegistry.registerKeyBinding(new KeyMapping("key.sleep.sleep_please", InputConstants.KEY_U,KeyMapping.CATEGORY_GAMEPLAY));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onInputKey(InputEvent.Key event){
        Minecraft MC = Minecraft.getInstance();
        Player player = MC.player;
        Level level = MC.level;
        if(player==null || level==null) return;
        if(BLOCK_SLEEP.isDown() && Minecraft.getInstance().hitResult instanceof BlockHitResult blockHitResult){
            BlockState block = level.getBlockState(blockHitResult.getBlockPos());
            if(ClientConfig.ALLOW_FORCE_SLEEP_ON_BED.get() || (!(block.getBlock() instanceof BedBlock))){
                boolean full = Block.isShapeFullBlock(block.getShape(level,blockHitResult.getBlockPos()));
                BlockPos pos = blockHitResult.getBlockPos();

                Sleep.tmp = blockHitResult.getDirection();

                Sleep.channel.sendToServer(new Sleep.Message(full?pos.above():pos,blockHitResult.getDirection(),-1));
            }
        }
        if(PLAYER_SLEEP.isDown() && Minecraft.getInstance().hitResult instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof Player target){
            BlockPos pos = target.getOnPos().above();
            Sleep.tmp = target.getDirection();
            Sleep.channel.sendToServer(new Sleep.Message(pos,target.getDirection(),target.getId()));
        }
    }
}
