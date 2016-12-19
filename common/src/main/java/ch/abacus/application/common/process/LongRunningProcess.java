package ch.abacus.application.common.process;

public interface LongRunningProcess {

	void performLongRunningProcess();

	boolean isProcessRunning();

	String getResult();
}
