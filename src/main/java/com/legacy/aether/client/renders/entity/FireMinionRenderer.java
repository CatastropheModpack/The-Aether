package com.legacy.aether.client.renders.entity;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.legacy.aether.Aether;
import com.legacy.aether.client.models.entities.SunSpiritModel;
import com.legacy.aether.entities.bosses.EntityFireMinion;

public class FireMinionRenderer extends RenderBiped {

    private static final ResourceLocation SPIRIT = Aether.locate("textures/bosses/sun_spirit/sun_spirit.png");

    private static final ResourceLocation FROZEN_SPIRIT = Aether.locate("textures/bosses/sun_spirit/frozen_sun_spirit.png");

    public FireMinionRenderer() {
        super(new SunSpiritModel(0.0F, 0.0F), 0.4F);
        this.shadowSize = 0.8F;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        EntityFireMinion sunSpirit = (EntityFireMinion) entity;

        if (sunSpirit.hasCustomNameTag() && "JorgeQ".equals(sunSpirit.getCustomNameTag())) {
            return FROZEN_SPIRIT;
        } else {
            return SPIRIT;
        }
    }

}