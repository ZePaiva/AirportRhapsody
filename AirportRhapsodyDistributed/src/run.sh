#!/bin/bash
cd bin/
echo "Running Shared Memories"
java Rhapsody.server.GeneralRepositoryMain &
java Rhapsody.server.ArrivalExitMain &
java Rhapsody.server.ArrivalLoungeMain &
java Rhapsody.server.ArrivalQuayMain &
java Rhapsody.server.BaggageCollectionMain &
java Rhapsody.server.BaggageReclaimMain &
java Rhapsody.server.DepartureEntranceMain &
java Rhapsody.server.DepartureQuayMain &
java Rhapsody.server.StorageAreaMain &

echo "Running clients"
java Rhapsody.client.PorterMain &
java Rhapsody.client.BusDriverMain & 
java Rhapsody.client.PassengerMain &


echo "Now we wait"
