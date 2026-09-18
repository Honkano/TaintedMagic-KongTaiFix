package taintedmagic.common.items.tools.sa;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import mods.flammpfeil.slashblade.ItemSlashBlade;
import mods.flammpfeil.slashblade.ability.StylishRankManager;
import mods.flammpfeil.slashblade.entity.EntityWitherSword;
import mods.flammpfeil.slashblade.specialattack.SpecialAttackBase;

public class SAPrimalStorm extends SpecialAttackBase {

    private static final Random rand = new Random();

    @Override
    public String toString() {
        return "SAPrimalStorm";
    }

    @Override
    public void doSpacialAttack(ItemStack stack, EntityPlayer player) {
        World world = player.worldObj;
        if (world.isRemote) return;

        NBTTagCompound tag = ItemSlashBlade.getItemTagCompound(stack);
        ItemSlashBlade blade = (ItemSlashBlade) stack.getItem();

        // ---- 消耗 30 耀魂，不够就扣耐久 ----
        int cost = -30;
        if (!ItemSlashBlade.ProudSoul.tryAdd(tag, cost, false)) {
            ItemSlashBlade.damageItem(stack, 15, player);
        }

        // ---- 锁定最近的目标 ----
        Entity target = getEntityToWatch(player);

        // ---- 伤害计算 ----
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
        int rank = StylishRankManager.getStylishRank(player);
        float magicDamage = 5.0F + ItemSlashBlade.AttackAmplifier.get(tag) * (0.5F + level / 5.0F);
        magicDamage += rank * 1.5F;

        int swordCount = 3 + rank;

        // ---- 生成追踪幻影剑（随机 红 / 黑） ----
        for (int i = 0; i < swordCount; i++) {
            EntityWitherSword sword = new EntityWitherSword(world, player, magicDamage, 90.0F);
            if (sword == null) continue;

            double angle = Math.PI * 2 / swordCount * i;
            double dist = 2.0;
            double x = Math.sin(angle) * dist;
            double z = Math.cos(angle) * dist;

            sword.setLocationAndAngles(
                player.posX + x,
                player.posY + 1.5,
                player.posZ + z,
                (float) Math.toDegrees(angle),
                0F);
            sword.setInterval(5 + i);
            sword.setLifeTime(40);
            sword.setBurst(true);

            // ============================================================
            // 🔴⚫ 随机红黑幻影剑
            // 红 = 0xFF0000 黑 = 0x000000
            // ============================================================
            int color = rand.nextBoolean() ? 0xFF0000 : 0x000000;
            sword.setColor(color);
            // ============================================================

            if (target != null) {
                sword.setTargetEntityId(target.getEntityId());
            }

            world.spawnEntityInWorld(sword);
        }

        // ---- 雷击 + 吸血 ----
        if (target != null) {
            EntityLightningBolt bolt = new EntityLightningBolt(world, target.posX, target.posY, target.posZ);
            world.spawnEntityInWorld(bolt);

            player.heal(3.0F);
            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 0));
        } else {
            AxisAlignedBB bb = player.boundingBox.copy()
                .expand(8.0, 4.0, 8.0);
            List<EntityLivingBase> nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            int hit = 0;
            for (EntityLivingBase e : nearby) {
                if (e == player || !e.isEntityAlive()) continue;
                if (hit >= 5) break;

                EntityLightningBolt bolt = new EntityLightningBolt(world, e.posX, e.posY, e.posZ);
                world.spawnEntityInWorld(bolt);
                hit++;

                player.heal(1.5F);
            }
        }

        // ---- 音效 ----
        world.playSoundAtEntity(player, "taintedmagic:primal_swing", 1.0F, 1.0F);
        world.playSoundAtEntity(player, "ambient.weather.thunder", 0.5F, 1.0F);

        // ---- 动作序列 ----
        ItemSlashBlade.setComboSequence(tag, ItemSlashBlade.ComboSequence.SlashDim);
    }

    private Entity getEntityToWatch(EntityPlayer player) {
        World world = player.worldObj;
        Entity target = null;
        for (int dist = 2; dist < 20; dist += 2) {
            AxisAlignedBB bb = player.boundingBox.copy();
            Vec3 vec = player.getLookVec()
                .normalize();
            bb = bb.expand(2.0D, 0.25D, 2.0D);
            bb = bb.offset(vec.xCoord * dist, vec.yCoord * dist, vec.zCoord * dist);

            List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, bb);
            float distance = 50.0F;
            for (Entity cur : list) {
                if (!(cur instanceof EntityLivingBase)) continue;
                if (!cur.isEntityAlive()) continue;
                float curDist = cur.getDistanceToEntity(player);
                if (curDist < distance) {
                    target = cur;
                    distance = curDist;
                }
            }
            if (target != null) break;
        }
        return target;
    }
}
