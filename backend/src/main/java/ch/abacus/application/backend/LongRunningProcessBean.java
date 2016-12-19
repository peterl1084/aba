package ch.abacus.application.backend;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import ch.abacus.application.common.process.LongRunningProcess;

@Component
public class LongRunningProcessBean implements LongRunningProcess {

	private Executor executor;

	private long startTime;
	private final static long DURATION = 5000;

	public LongRunningProcessBean() {
		executor = Executors.newSingleThreadExecutor();
	}

	@Override
	public void performLongRunningProcess() {
		startTime = System.currentTimeMillis();

		executor.execute(() -> {
			System.out.println("Process starting!");
			try {
				Thread.sleep(DURATION);
			} catch (InterruptedException e) {
			}

			System.out.println("Process done");
		});
	}

	@Override
	public boolean isProcessRunning() {
		return System.currentTimeMillis() < startTime + DURATION;
	}

	@Override
	public String getResult() {
		return "Process finished";
	}
}
