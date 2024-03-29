package com.legacy.aether.player.abilities;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;

import com.legacy.aether.api.player.IPlayerAether;
import com.legacy.aether.api.player.util.IAetherAbility;
import com.legacy.aether.items.ItemsAether;
import com.legacy.aether.player.movement.AetherLiquidMovement;

public class AbilityArmor implements IAetherAbility {

	private final AetherLiquidMovement player_movement;

	private final Random random = new Random();

	private final IPlayerAether player;

	private boolean jumpBoosted;

	public AbilityArmor(IPlayerAether player) {
		this.player = player;
		this.player_movement = new AetherLiquidMovement(player);
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void onUpdate() {
		if (this.player.getAccessoryInventory().isWearingNeptuneSet()) {
			this.player_movement.onUpdate();
		}

		if (this.player.getAccessoryInventory().isWearingGravititeSet()) {
			if (this.player.isJumping() && !this.jumpBoosted) {
				this.player.getEntity().motionY = 1D;
				this.jumpBoosted = true;
			}

			this.player.getEntity().fallDistance = -1F;
		}

		if (this.player.getEntity().isWet()) {
			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(ItemsAether.phoenix_boots))) {
				this.damagePhoenixArmor(this.player.getEntity(), ItemsAether.obsidian_boots, 0);
			}

			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(ItemsAether.phoenix_leggings))) {
				this.damagePhoenixArmor(this.player.getEntity(), ItemsAether.obsidian_leggings, 1);
			}

			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(ItemsAether.phoenix_chestplate))) {
				this.damagePhoenixArmor(this.player.getEntity(), ItemsAether.obsidian_chestplate, 2);
			}

			if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(ItemsAether.phoenix_helmet))) {
				this.damagePhoenixArmor(this.player.getEntity(), ItemsAether.obsidian_helmet, 3);
			}
		}

		if (this.player.getAccessoryInventory().isWearingPhoenixSet()) {
			this.player.getEntity().extinguish();
			this.player_movement.onUpdate();

			if (!this.player.getEntity().worldObj.isRemote) {
				((WorldServer) this.player.getEntity().worldObj).func_147487_a("flame", this.player.getEntity().posX + (this.random.nextGaussian() / 5D), this.player.getEntity().posY + (this.random.nextGaussian() / 5D), this.player.getEntity().posZ + (this.random.nextGaussian() / 3D), 0, 0.0D, 0.0D, 0.0D, 0.0D);
			}
		}

		if (this.player.getAccessoryInventory().wearingArmor(new ItemStack(ItemsAether.sentry_boots))) {
			this.player.getEntity().fallDistance = 0F;
		}

		if (!this.player.isJumping() && this.player.getEntity().onGround) {
			this.jumpBoosted = false;
		}

	}

	public void damagePhoenixArmor(Entity entity, Item outcome, int slot) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityLiving = (EntityLivingBase) entity;
			ItemStack stack = entityLiving.getEquipmentInSlot(slot + 1);

			stack.damageItem(1, entityLiving);

			if (stack.stackSize <= 0) {
				entityLiving.setCurrentItemOrArmor(slot + 1, new ItemStack(outcome));
			}
		}
	}

}