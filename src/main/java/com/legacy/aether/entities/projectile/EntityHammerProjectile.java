package com.legacy.aether.entities.projectile;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import com.legacy.aether.api.player.util.IAetherBoss;
import com.legacy.aether.entities.util.EntitySaddleMount;

public class EntityHammerProjectile extends EntityProjectileBase {

	public ArrayList<Block> harvestBlockBans = new ArrayList<Block>();

	public EntityHammerProjectile(World worldIn) {
		super(worldIn);
	}

	public EntityHammerProjectile(World worldIn, EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		this.worldObj.spawnParticle("reddust", this.posX, this.posY + 0.2F, this.posZ, 1.0D, 1.0D, 1.0D);

		if (this.ticksInAir > 100) {
			this.setDead();
		} else {
			this.ticksInAir++;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void onImpact(MovingObjectPosition object) {
		if (object.typeOfHit == MovingObjectType.ENTITY) {
			if (object.entityHit instanceof EntitySaddleMount && ((EntitySaddleMount) object.entityHit).isSaddled()) {

			} else if (object.entityHit != this.getThrower() && !(object.entityHit instanceof IAetherBoss)) {
				object.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), 5);
				object.entityHit.addVelocity(this.motionX, 0.6D, this.motionZ);
			}
		}

		for (int l = (int) (this.posX - 3); l <= this.posX + 3; l++) {
			for (int i1 = (int) (this.posY - 3); i1 <= this.posY + 3; i1++) {
				for (int j1 = (int) (this.posZ - 3); j1 <= this.posZ + 3; j1++) {
					if (this.worldObj.getBlock(l, i1, j1) instanceof BlockBush && this.getThrower() instanceof EntityPlayer) {
						Block prevBlock = this.worldObj.getBlock(l, i1, j1);

						if (!this.harvestBlockBans.contains(prevBlock)) {
							prevBlock.harvestBlock(this.getThrower().worldObj, (EntityPlayer) this.getThrower(), l, i1, j1, this.worldObj.getBlockMetadata(l, i1, j1));

							prevBlock.removedByPlayer(this.getThrower().worldObj, (EntityPlayer) this.getThrower(), l, i1, j1);
						}

						continue;
					}
				}
			}
		}

		for (int j = 0; j < 8; j++) {
			this.worldObj.spawnParticle("explode", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("explode", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("largesmoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			this.worldObj.spawnParticle("flame", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected float getBoundingBoxExpansion() {
		return 2.5F;
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}

}