package Rhapsody.entities;

import java.util.LinkedList;
import java.util.Queue;

import Rhapsody.entities.states.BusDriverState;
import Rhapsody.sharedMems.ArrivalTerminalTransfer;
import Rhapsody.sharedMems.DepartureTerminalTransfer;
import Rhapsody.sharedMems.GeneralRepository;

/**
 * Bus Driver entity data-type
 * 
 * 
 * @author Jose Paiva
 * @author Andre Mourato
 */
public class BusDriver extends Thread {
	
	private BusDriverState currentState;

	private GeneralRepository generalRepository;
	private ArrivalTerminalTransfer arrivalTerminalTransfer;
	private DepartureTerminalTransfer departureTerminalTransfer;

	private final long maxWait;
	private final int maxSeats;
	private Queue<Integer> busSeats;
	public static final String ANSI_PURPLE = "\u001B[35m";
	
	public BusDriver(int busSeats, long maxWait, GeneralRepository generalRepository, 
						ArrivalTerminalTransfer arrivalTerminalTransfer, 
						DepartureTerminalTransfer departureTerminalTransfer) {
		this.currentState=BusDriverState.PARKING_AT_THE_ARRIVAL_LOUNGE;
		this.arrivalTerminalTransfer=arrivalTerminalTransfer;
		this.generalRepository=generalRepository;
		this.departureTerminalTransfer=departureTerminalTransfer;
		this.maxWait=maxWait;
		this.busSeats=new LinkedList<>();
		this.maxSeats=busSeats;
	}

	public BusDriverState getBusDriverState(){
		return this.currentState;
	}

	public void setBusDriverState(BusDriverState busDriverState) {
		this.currentState=busDriverState;
	}

	@Override
	public void run() {
		System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus PARKED AT ARRIVAL AND WAITING\n");
		while(arrivalTerminalTransfer.hasDaysWorkEnded())  {
			
			// starts boarding process
			System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus Driver is announcing boarding\n");
			arrivalTerminalTransfer.announcingBusBoarding();
			
			// starts voyage to departure terminal
			System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus is going to DEPARTURE TERMINAL\n");
			this.busSeats=arrivalTerminalTransfer.goToDepartureTerminal();

			// arrives at the departure terminal and waits until all passengers exit the bus
			System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus PARKED AT DEPARTURE AND WAITNG\n");
			departureTerminalTransfer.parkTheBusAndLetPassOff(this.busSeats);

			// starts voyage to the arrival terminal
			System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus going to ARRIVAL TERMINAL\n");
			departureTerminalTransfer.goToArrivalTerminal();

			// arrives to the arrival terminal and alerts that the bus has arrived to the clients
			System.out.printf(ANSI_PURPLE+"[BUSDRIVER] Bus PARKED AT ARRIVAL AND WAITING\n");
			arrivalTerminalTransfer.parkTheBus();
		}
	}
}
