#!/bin/bash
cd bin/
echo "Running Shared Memories"
java Rhapsody.server.GeneralRepositoryMain &
sleep .5
java Rhapsody.server.ArrivalExitMain &
java Rhapsody.server.ArrivalLoungeMain &
java Rhapsody.server.ArrivalQuayMain &
java Rhapsody.server.BaggageCollectionMain &
java Rhapsody.server.BaggageReclaimMain &
java Rhapsody.server.DepartureEntranceMain &
java Rhapsody.server.DepartureQuayMain &
java Rhapsody.server.StorageAreaMain &

echo "Running clients"
sleep 1
java Rhapsody.client.PorterMain &
java Rhapsody.client.BusDriverMain & 
java Rhapsody.client.PassengerMain &


echo "Now we wait"
