import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class implements a queue of customers as a circular buffer.
 */
public class CustomerQueue {
	/**
	 * Creates a new customer queue. Make sure to save these variables in the class.
	 * @param queueLength	The maximum length of the queue.
	 * @param gui			A reference to the GUI interface.
	 */

    private final Gui gui;
    private final int MAX_QUEUE_LENGTH;
    private ArrayList<Customer> customerQueue = new ArrayList<>();
    /*
    private int firstPos = 0; // Index of longest waiting customer
    private int lastPos = 0; // Index of most recent customer
*/
    public CustomerQueue(int queueLength, Gui gui) {
        this.gui = gui;
        this.MAX_QUEUE_LENGTH = queueLength;
	}

	public void add(Customer customer) {
        customerQueue.add(customer);
        gui.fillLoungeChair(customerQueue.size()-1, customer);
	}

    public int size(){
        return customerQueue.size();
    }

	public Customer next() {
        gui.emptyLoungeChair(customerQueue.size()-1);
        return customerQueue.remove(0);
	}

    public boolean isEmpty(){
        return customerQueue.isEmpty();
    }

    public boolean isFull() {
        return customerQueue.size() == MAX_QUEUE_LENGTH;
    }
}
