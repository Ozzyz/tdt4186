import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.Queue;

/**
 * This class implements the doorman's part of the
 * Barbershop thread synchronization example.
 * One doorman instance corresponds to one producer in
 * the producer/consumer problem.
 */
public class Doorman implements Runnable {
	/**
	 * Creates a new doorman. Make sure to save these variables in the class.
	 * @param queue		The customer queue.
	 * @param gui		A reference to the GUI interface.
	 */
	private CustomerQueue customerQueue;
	private final Gui gui;
    private Thread thread;

	public Doorman(CustomerQueue queue, Gui gui) { 
		this.customerQueue = queue;
		this.gui = gui;
	}

	/**
	 * This is the code that will run when a new thread is
	 * created for this instance.
	 */
	@Override
	public void run() {
                    Customer customer = new Customer();
                    customerQueue.add(customer);
                    gui.println("Doorman was notified of free chair");
                } else {
                    gui.println("Doorman waiting for free chair");
                }
                sleep();
            }
        }
    }

	public void startThread() {
        thread = new Thread(this);
        thread.start();
	}

	public void stopThread() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            gui.println("Could not stop doorman thread :O ");
        }
    }

	private void sleep(){
        try {
            // If customer queue not full, bring in new customer, then sleep
            int millis = Globals.doormanSleep+
                    (int) (Math.random() * (Constants.MAX_DOORMAN_SLEEP - Constants.MIN_DOORMAN_SLEEP));
            //int millis = Globals.doormanSleep;
            gui.println("Doorman sleeping!");
            System.out.printf("Size of queue: %s\n", customerQueue.size());
            System.out.printf("Doorman sleeping for %d millis\n", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            gui.println("Doorman interrupted from sleep!");
        }
    }
}
