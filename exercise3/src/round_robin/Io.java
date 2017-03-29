package round_robin;

import java.util.LinkedList;
import java.util.Random;

/**
 * This class implements functionality associated with
 * the I/O device of the simulated system.
 */
public class Io {
    private Process activeProcess = null;
    private final LinkedList<Process> ioQueue;
    private final long avgIoTime;
    private final Statistics statistics;
    /**
     * Creates a new I/O device with the given parameters.
     * @param ioQueue		The I/O queue to be used.
     * @param avgIoTime		The average duration of an I/O operation.
     * @param statistics	A reference to the statistics collector.
     */
    public Io(LinkedList<Process> ioQueue, long avgIoTime, Statistics statistics) {
        // Incomplete
        this.ioQueue = ioQueue;
        this.avgIoTime = avgIoTime;
        this.statistics = statistics;
    }

    /**
     * Adds a process to the I/O queue, and initiates an I/O operation
     * if the device is free.
     * @param requestingProcess	The process to be added to the I/O queue.
     * @param clock				The time of the request.
     * @return					The event ending the I/O operation, or null
     *							if no operation was initiated.
     */
    public Event addIoRequest(Process requestingProcess, long clock) {
        ioQueue.add(requestingProcess);
        if(activeProcess != null){
            return null;
        }
        return startIoOperation(clock);
    }


    /**
     * Starts a new I/O operation if the I/O device is free and there are
     * processes waiting to perform I/O.
     * @param clock		The global time.
     * @return			An event describing the end of the I/O operation that was started,
     *					or null	if no operation was initiated.
     */
    public Event startIoOperation(long clock) {
        if(ioQueue.isEmpty()) return null;
        activeProcess = ioQueue.remove();
        activeProcess.timeSpentInIOQueue(clock);
        // Make IO-time a bit random (100 -> 200% of avgIoTime)
        return new Event(Event.END_IO, (long) (clock+avgIoTime + avgIoTime*Math.random()));
    }

    /**
     * This method is called when a discrete amount of time has passed.
     * @param timePassed	The amount of time that has passed since the last call to this method.
     */
    public void timePassed(long timePassed) {
        statistics.ioQueueLengthTime += ioQueue.size()*timePassed;
        if (ioQueue.size() > statistics.ioQueueLargestLength) {
            statistics.ioQueueLargestLength = ioQueue.size();
        }
    }

    /**
     * Removes the process currently doing I/O from the I/O device.
     * @return	The process that was doing I/O, or null if no process was doing I/O.
     */
    public Process removeActiveProcess(long clock) {
        // Incomplete
        Process p = activeProcess;
        p.timeSpentInIO(clock);
        activeProcess = null;
        return p;
    }

    public Process getActiveProcess() {
        return activeProcess;
    }
}
