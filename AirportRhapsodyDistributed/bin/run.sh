#!/bin/bash
cd bin/
echo "Running Shared Memories"
java Rhapsody.server.GeneralRepositoryMain &> GeneralRepositoryLog &
java Rhapsody.server.ArrivalExitMain &> ArrivalExitLog &
java Rhapsody.server.ArrivalLoungeMain &> ArrivalLoungeLog &
java Rhapsody.server.ArrivalQuayMain &> ArrivalQuayLog &
java Rhapsody.server.BaggageCollectionMain &> BaggageCollectionLog &
java Rhapsody.server.BaggageReclaimMain &> BaggageReclaimLog &
java Rhapsody.server.DepartureEntranceMain &> DepartureEntranceLog &
java Rhapsody.server.DepartureQuayMain &> DepartureQuayLog &
java Rhapsody.server.StorageAreaMain &> StorageAreaLog &

echo "Running clients"
java Rhapsody.client.PorterMain &> PorterLog &
java Rhapsody.client.BusDriverMain &> BusDriverLog & 
java Rhapsody.client.PassengerMain &> PassengerLog &


echo "Now we wait"
