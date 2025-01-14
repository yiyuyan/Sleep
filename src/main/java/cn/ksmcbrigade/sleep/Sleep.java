package cn.ksmcbrigade.sleep;

import cn.ksmcbrigade.sleep.client.ClientConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Sleep.MOD_ID)
public class Sleep {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "sleep";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static Direction tmp = Direction.UP;
    public static final SimpleChannel channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID,"sync"),()->"1",(a)->true,(b)->true);

    public Sleep() {
        MinecraftForge.EVENT_BUS.register(this);
        channel.registerMessage(0,Message.class,Message::encode,Message::decode,(msg,context)->{
            ServerPlayer player = context.get().getSender();
            if(player!=null && msg.target!=-1){
                boolean found = false;
                for (ServerPlayer serverPlayer : player.serverLevel().players()) {
                    if(serverPlayer.getId()==msg.target){
                        player = serverPlayer;
                        found = true;
                        break;
                    }
                }
                if(!found) player=null;
            }
            if(player!=null){
                tmp = msg.direction;

                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,()->()->tmp = msg.direction);  //sync tmp

                player.startSleepInBed(msg.pos);
            }
            context.get().setPacketHandled(true);
        });
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        LOGGER.info("Sleep mod loaded.");
    }

    public record Message(BlockPos pos, Direction direction,int target){
        public static void encode(Message msg, FriendlyByteBuf buf){
            buf.writeBlockPos(msg.pos());
            buf.writeEnum(msg.direction());
            buf.writeInt(msg.target());
        }

        public static Message decode(FriendlyByteBuf buf){
            return new Message(buf.readBlockPos(),buf.readEnum(Direction.class),buf.readInt());
        }
    }

    @SubscribeEvent
    public void onSleep(SleepingTimeCheckEvent event) {
        event.setResult(Event.Result.ALLOW);
    }
}
