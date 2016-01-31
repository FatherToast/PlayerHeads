package toast.playerHeads;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class EventHandler
{
    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    /** Called by EntityPlayer.onDeath().
        EntityPlayer entityPlayer = the player dropping the items.
        DamageSource source = the source of the lethal damage.
        ArrayList<EntityItem> drops = the items being dropped.
        int lootingLevel = the attacker's looting level.
        boolean recentlyHit = if the player was recently hit by another player.
        int specialDropValue = recentlyHit ? entityPlayer.getRNG().nextInt(200) - lootingLevel : 0.
    */
    @ForgeSubscribe(priority = EventPriority.HIGHEST)
    public void onPlayerDrops(PlayerDropsEvent event) {
        if (_PlayerHeads.debug);
        else if (_PlayerHeads.pvp && !event.recentlyHit || _PlayerHeads.dropRates[3] <= 0.0 || _PlayerHeads.random.nextDouble() - 0.005 * (double)event.lootingLevel >= _PlayerHeads.dropRates[3])
            return;
        ItemStack head = new ItemStack(Item.skull, 1, 3);
        head.setTagInfo("SkullOwner", new NBTTagString("", event.entityPlayer.username));
        EntityItem drop = new EntityItem(event.entityPlayer.worldObj, event.entityPlayer.posX, event.entityPlayer.posY, event.entityPlayer.posZ, head);
        drop.delayBeforeCanPickup = 10;
        event.drops.add(drop);
    }
    
    /** Called by EntityLivingBase.onDeath().
        EntityLivingBase entityLiving = the entity dropping the items.
        DamageSource source = the source of the lethal damage.
        ArrayList<EntityItem> drops = the items being dropped.
        int lootingLevel = the attacker's looting level.
        boolean recentlyHit = if the player was recently hit by another player.
        int specialDropValue = recentlyHit ? entityLiving.getRNG().nextInt(200) - lootingLevel : 0.
    */
    @ForgeSubscribe(priority = EventPriority.HIGHEST)
    public void onLivingDrops(LivingDropsEvent event) {
        if (!event.recentlyHit)
            return;
        int id = -1;
        if (event.entityLiving instanceof EntityZombie)
            id = event.entityLiving instanceof EntityPigZombie ? -1 : 2;
        else if (event.entityLiving instanceof EntitySkeleton)
            id = ((EntitySkeleton)event.entityLiving).getSkeletonType() == 0 ? 0 : -1;
        else if (event.entityLiving instanceof EntityCreeper)
            id = 4;
        if (id < 0)
            return;
        if (_PlayerHeads.debug);
        else if (_PlayerHeads.dropRates[id] <= 0.0 || _PlayerHeads.random.nextDouble() - 0.005 * (double)event.lootingLevel >= _PlayerHeads.dropRates[id])
            return;
        ItemStack head = new ItemStack(Item.skull, 1, id);
        EntityItem drop = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, head);
        drop.delayBeforeCanPickup = 10;
        event.drops.add(drop);
    }
}