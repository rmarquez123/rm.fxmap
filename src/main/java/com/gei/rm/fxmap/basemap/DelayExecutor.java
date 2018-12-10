package com.gei.rm.fxmap.basemap;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author rmarquez
 */
public class DelayExecutor {

  private Task task;
  private Timer timer;
  private final int delay;

  /**
   *
   * @param delay
   */
  public DelayExecutor(int delay) {
    this.delay = delay;
  }

  /**
   *
   * @param task
   */
  public synchronized void setTask(final Task task) {
    if (this.delay > 0) {
      this.reset();
      DelayExecutor self = this;
      this.timer = new Timer();
      this.timer.schedule(new TimerTask() {
        @Override
        public void run() {
          self.task = task;
          self.task.execute();
          if (timer != null) {
            self.timer.cancel();
            self.timer = null;
          }

        }
      }, this.delay);
    } else {
      task.execute();
    }
  }

  /**
   *
   */
  private void reset() {
    if (this.timer != null) {
      this.timer.cancel();
      this.timer = null;
    }
    if (this.task != null) {
      this.task.cancel();
      this.task = null;
    }
  }

}
