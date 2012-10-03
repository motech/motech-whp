package org.motechproject.whp.integration;

import static org.junit.Assert.fail;

public abstract class TimedRunner {

    private int tries;
    private int intervalSleep;

    public TimedRunner() {
        this(5, 1000);
    }

    public TimedRunner(int tries, int intervalSleep) {
        this.tries = tries;
        this.intervalSleep = intervalSleep;
    }

    /*
    * Function to run within the timeout. It returns a boolean. If the value is true, the code
    * will break out of the loop immediately else try again within the timeout period.
    */
    protected abstract void run();

    public void executeWithTimeout() {
        for (int i = 0; i < tries; i++) {
            try {
                run();
                return;
            } catch (Exception e) {
                try {
                    Thread.sleep(intervalSleep);
                    continue;
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Thread was interrupted.", e);
                }
            }
        }
        fail("Expected condition not satisfied");
    }

}
