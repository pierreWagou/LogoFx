package logogui;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.canvas.GraphicsContext;

public class LogoGraphContext {
	// Pause entre 2 accès à la queue des lignes
	private int thread_sleep = 3;
	//private static LogoGraphContext logoGraphContext;
	// Queue où sont placés les quadruplets pour les tracés
	ConcurrentLinkedQueue<GraphParameter> lineQueue = new ConcurrentLinkedQueue<>();
	// GraphicsContext associé au canvas de l'interface graphique
	GraphicsContext gclocal;
	ExecutorService pool;

	public LogoGraphContext(GraphicsContext gclocal) {
		this.gclocal = gclocal;
		pool = Executors.newFixedThreadPool(2);
		// Lance la tâche qui effectue les tracés à partir des GraphParameters de la queue
		pool.execute(drawTask);
	}

	public void addLine(GraphParameter gp) {
		lineQueue.add(gp);
	}
	public void stop() {
		lineQueue.clear();
	}
	public void setThreadSleep(int mstime) {
		thread_sleep = mstime;
	}
	
	public String getThreadSleep() {
		return String.valueOf(thread_sleep);
	}
	public void shutDown() {
		pool.shutdown();
	}
	Runnable drawTask = () -> {	
		while (true) {
			GraphParameter gp = lineQueue.poll();
			if (gp != null) {
				// Platform.runLater(() -> {
				gclocal.setStroke(gp.color);
				gclocal.strokeLine(gp.initx, gp.inity, gp.endx, gp.endy);

				try {
					Thread.sleep(thread_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
}
