package bet.astral.unity.gui.prebuilt.banner.exception;

import lombok.Getter;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;

@Getter
public class UnknownDyeColorException extends RuntimeException {
  @NotNull
  private final DyeColor dyeColor;

  public UnknownDyeColorException(DyeColor color) {
    super("Encountered unknown dye color: " + color.name());
    this.dyeColor = getDyeColor();
  }
}
