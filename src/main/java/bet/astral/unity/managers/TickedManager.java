package bet.astral.unity.managers;

import bet.astral.unity.Unity;
import bet.astral.unity.entity.Ticked;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class TickedManager {
	private final HashSet<Ticked> tickers = new HashSet<>();
	private final Unity unity;

	public TickedManager(Unity unity) {
		this.unity = unity;
	}

	public void init(){
		unity.getServer()
				.getAsyncScheduler()
				.runAtFixedRate(
						unity,
						t->tick(),
						1,
						50,
						TimeUnit.MILLISECONDS
				);
	}


	public void register(Ticked ticked){
		tickers.add(ticked);
	}

	public void unregister(Ticked ticked){
		tickers.remove(ticked);
	}

	public void tick(){
		tickers.forEach(Ticked::tick);
	}
}
