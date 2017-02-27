/**
 * This class implements the barber's part of the
 * Barbershop thread synchronization example.
 * One barber instance corresponds to one consumer in
 * the producer/consumer problem.
 */
public class Barber implements Runnable {
	/**
	 * Creates a new barber. Make sure to save these variables in the class.
	 * @param queue		The customer queue.
	 * @param gui		The GUI.
	 * @param pos		The position of this barber's chair
	 */

	private final CustomerQueue customerQueue;
	private final Gui gui;
	private Thread thread;
	// Keep track over the number of each barber
	// This will be increased each time a barber is created
    private final int pos;


	public Barber(CustomerQueue queue, Gui gui, int pos) {
		this.gui = gui;
		this.customerQueue = queue;
        this.pos = pos;
	}

	/**
	 * This is the code that will run when a new thread is
	 * created for this instance.
	 */
	@Override
	public void run(){
		// Try to get a customer, then sleep
            int millis;
        for (int i = 0; i < 500; i++) {
            synchronized (this) {
                if (!customerQueue.isEmpty()) {
                    Customer customer = customerQueue.next();
                    gui.fillBarberChair(pos, customer);
                    gui.println(String.format("Barber #%s got customer. ", pos));
                    millis =Globals.barberWork + (int) (Math.random() * (Constants.MAX_BARBER_WORK - Constants.MIN_BARBER_WORK));
                    sleep(millis);
                    gui.emptyBarberChair(pos);
                } else {
                    millis = Globals.barberSleep+
                            (int) (Math.random() * (Constants.MAX_BARBER_SLEEP - Globals.barberSleep));
                    gui.println(String.format("Barber #%s waiting for customer. ", pos));
                    sleep(millis);

                }
            }
        }
    }

	/**
	 * Starts the barber running as a separate thread.
	 */
	public void startThread() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stops the barber thread.
	 */
	public void stopThread() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			System.err.printf("Could not stop barber thread number %s.", pos);
			gui.println("Barber interrupted from sleep");
		}
	}


	private void sleep(int millis){
		try {
            System.out.printf("Barber sleeping for %d millis\n", millis);
			gui.barberIsSleeping(pos);
			Thread.sleep(millis);
            gui.barberIsAwake(pos);
		} catch (InterruptedException e) {
			System.err.println("Could not sleep thread!");
		}
	}
	// Add more methods as needed
}

