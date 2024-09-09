package bet.astral.unity.gui.prebuilt.banner.data;

import bet.astral.more4j.tuples.Pair;
import bet.astral.unity.gui.prebuilt.banner.exception.UnknownDyeColorException;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BannerBuilder {
	@Getter
	private DyeColor base;
	private final Map<Integer, DyeColor> layerColors = new HashMap<>();
	private final Map<Integer, Pattern> layerPatterns = new HashMap<>();

	public BannerBuilder setBase(@NotNull DyeColor baseColor){
		this.base = baseColor;
		return this;
	}

	public BannerBuilder setLayer(int layer, @NotNull Pattern pattern, @NotNull DyeColor color){
		this.layerColors.put(layer, color);
		this.layerPatterns.put(layer, pattern);
		return this;
	}
	public BannerBuilder addLayer(@NotNull Pattern pattern, @NotNull DyeColor color){
		this.layerColors.put(layerColors.size(), color);
		this.layerPatterns.put(layerColors.size(), pattern);
		return this;
	}

	public ItemStack build(){
		return ItemStack.of(Material.WHITE_BANNER);
	}

	@NotNull
	public Pair<@Nullable Pattern, @Nullable DyeColor> getLayer(int layer){
		DyeColor dyeColor = layerColors.get(layer);
		Pattern pattern = layerPatterns.get(layer);
		return Pair.immutable(pattern, dyeColor);
	}

	@NotNull
	public List<@NotNull Pair<@NotNull Pattern, @NotNull DyeColor>> getLayers(){
		List<Pair<Pattern, DyeColor>> layers = new LinkedList<>();
		for (int i = 0; i < 10; i++){
			Pair<Pattern, DyeColor> layer = getLayer(i);
			if (layer.getFirst() == null) {
				continue;
			}
			if (layer.getSecond() == null) {
				continue;
			}
			layers.add(layer);
		}
		return layers;
	}

	@NotNull
	public static Material dyeToBanner(@NotNull DyeColor color) throws UnknownDyeColorException{
		switch (color){
			case RED -> {
				return Material.RED_BANNER;
			}
			case ORANGE -> {
				return Material.ORANGE_BANNER;
			}
			case YELLOW -> {
				return Material.YELLOW_BANNER;
			}
			case LIME -> {
				return Material.LIME_BANNER;
			}
			case GREEN -> {
				return Material.GREEN_BANNER;
			}
			case LIGHT_BLUE -> {
				return Material.LIGHT_BLUE_BANNER;
			}
			case CYAN -> {
				return Material.CYAN_BANNER;
			}
			case BLUE -> {
				return Material.BLUE_BANNER;
			}
			case PINK -> {
				return Material.PINK_BANNER;
			}
			case MAGENTA -> {
				return Material.MAGENTA_BANNER;
			}
			case PURPLE -> {
				return Material.PURPLE_BANNER;
			}
			case WHITE -> {
				return Material.WHITE_BANNER;
			}
			case LIGHT_GRAY -> {
				return Material.LIGHT_GRAY_BANNER;
			}
			case GRAY -> {
				return Material.GRAY_BANNER;
			}
			case BLACK -> {
				return Material.BLACK_BANNER;
			}
			case BROWN -> {
				return Material.BROWN_BANNER;
			}
		}
		throw new UnknownDyeColorException(color);
	}
	@NotNull
	public static Material dyeToDye(@NotNull DyeColor color) throws UnknownDyeColorException{
		switch (color){
			case RED -> {
				return Material.RED_DYE;
			}
			case ORANGE -> {
				return Material.ORANGE_DYE;
			}
			case YELLOW -> {
				return Material.YELLOW_DYE;
			}
			case LIME -> {
				return Material.LIME_DYE;
			}
			case GREEN -> {
				return Material.GREEN_DYE;
			}
			case LIGHT_BLUE -> {
				return Material.LIGHT_BLUE_DYE;
			}
			case CYAN -> {
				return Material.CYAN_DYE;
			}
			case BLUE -> {
				return Material.BLUE_DYE;
			}
			case PINK -> {
				return Material.PINK_DYE;
			}
			case MAGENTA -> {
				return Material.MAGENTA_DYE;
			}
			case PURPLE -> {
				return Material.PURPLE_DYE;
			}
			case WHITE -> {
				return Material.WHITE_DYE;
			}
			case LIGHT_GRAY -> {
				return Material.LIGHT_GRAY_DYE;
			}
			case GRAY -> {
				return Material.GRAY_DYE;
			}
			case BLACK -> {
				return Material.BLACK_DYE;
			}
			case BROWN -> {
				return Material.BROWN_DYE;
			}
		}
		throw new UnknownDyeColorException(color);
	}
}
