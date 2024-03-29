package com.legacy.aether.items.food;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.legacy.aether.blocks.BlocksAether;
import com.legacy.aether.registry.creative_tabs.AetherCreativeTabs;

public class ItemAmbrosiumShard extends Item {

	public ItemAmbrosiumShard() {
		this.setCreativeTab(AetherCreativeTabs.material);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = playerIn.getHeldItem();

		if (worldIn.getBlock(x, y, z) == BlocksAether.aether_grass) {
			if (!playerIn.capabilities.isCreativeMode) {
				--heldItem.stackSize;
			}

			worldIn.setBlock(x, y, z, BlocksAether.enchanted_aether_grass);

			return true;
		}

		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		ItemStack heldItem = playerIn.getHeldItem();

		if (playerIn.shouldHeal()) {
			if (!playerIn.capabilities.isCreativeMode) {
				--heldItem.stackSize;
			}

			playerIn.heal(2F);
			playerIn.getFoodStats().addStats(1, 0.5F);

			return heldItem;
		}

		return super.onItemRightClick(stack, worldIn, playerIn);
	}

}