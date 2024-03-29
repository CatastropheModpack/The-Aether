package com.legacy.aether.client.audio.music;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import com.legacy.aether.Aether;
import com.legacy.aether.AetherConfig;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AetherMusicTicker implements IUpdatePlayerListBox {

	private final Random rand = new Random();
	private final Minecraft mc;
	private ISound currentMusic;
	private int timeUntilNextMusic = 100;

	public AetherMusicTicker(Minecraft mcIn) {
		this.mc = mcIn;
	}

	public void update() {
		TrackType tracktype = this.getRandomTrack();

		if (this.mc.thePlayer != null) {
			if (this.mc.gameSettings.getSoundLevel(SoundCategory.MUSIC) == 0.0F) {
				if (this.currentMusic != null) {
					this.stopMusic();
					this.currentMusic = null;
				}

				return;
			}

			if (this.mc.thePlayer.dimension != AetherConfig.getAetherDimensionID()) {
				this.stopMusic();
			} else if (this.mc.thePlayer.dimension == AetherConfig.getAetherDimensionID()) {
				if (this.currentMusic != null) {
					if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
						this.currentMusic = null;
						this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, tracktype.getMinDelay(), tracktype.getMaxDelay()), this.timeUntilNextMusic);
					}
				}

				this.timeUntilNextMusic = Math.min(this.timeUntilNextMusic, tracktype.getMaxDelay());

				if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
					this.playMusic(tracktype);
				}
			}
		}
	}

	public boolean playingMusic() {
		return this.currentMusic != null;
	}

	public AetherMusicTicker.TrackType getRandomTrack() {
		int num = this.rand.nextInt(4);

		return num == 0 ? TrackType.TRACK_ONE : num == 1 ? TrackType.TRACK_TWO : num == 2 ? TrackType.TRACK_THREE : TrackType.TRACK_FOUR;
	}

	public void playMusic(TrackType requestedMusicType) {
		this.currentMusic = PositionedSoundRecord.func_147673_a(requestedMusicType.getMusicLocation());
		this.mc.getSoundHandler().playSound(this.currentMusic);
		this.timeUntilNextMusic = Integer.MAX_VALUE;
	}

	public void stopMusic() {
		if (this.currentMusic != null) {
			this.mc.getSoundHandler().stopSound(this.currentMusic);
			this.currentMusic = null;
			this.timeUntilNextMusic = 0;
		}
	}

	@SideOnly(Side.CLIENT)
	public static enum TrackType {
		TRACK_ONE(Aether.locate("music.aether1"), 1200, 1500),
		TRACK_TWO(Aether.locate("music.aether2"), 1200, 1500),
		TRACK_THREE(Aether.locate("music.aether3"), 1200, 1500),
		TRACK_FOUR(Aether.locate("music.aether4"), 1200, 1500);

		private final ResourceLocation musicLocation;
		private final int minDelay;
		private final int maxDelay;

		private TrackType(ResourceLocation musicLocationIn, int minDelayIn, int maxDelayIn) {
			this.musicLocation = musicLocationIn;
			this.minDelay = minDelayIn;
			this.maxDelay = maxDelayIn;
		}

		public ResourceLocation getMusicLocation() {
			return this.musicLocation;
		}

		public int getMinDelay() {
			return this.minDelay;
		}

		public int getMaxDelay() {
			return this.maxDelay;
		}
	}

}