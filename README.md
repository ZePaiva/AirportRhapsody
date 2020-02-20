# AirportRhapsody

## Entities
### Passenger
	* Departing (Boolean)
		> True if departing, False if arriving
	* Baggage (BaggageType)

### BusDriver
	* Passengers (List of PassengerType)
	
### Porter
	* Bags (List of BaggageType)

### Plane
	* Bags (List of BaggageType)
	* Passengers (List of PassengerType)

### Airport
	* Voyages (List of VoyageType)
	* Planes (List of Planes)

### Voyage
	* Passengers (List of PassengerType)

