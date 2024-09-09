package bet.astral.unity.gui.core.data;

import bet.astral.unity.entity.Player;
import bet.astral.unity.gui.core.CachedData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PlayerCachedData extends CachedData {
	@NotNull
	private final OfflinePlayer managing;
	@NotNull
	private final UUID managingId;
	@Nullable
	private final Player fPlayer;
}
