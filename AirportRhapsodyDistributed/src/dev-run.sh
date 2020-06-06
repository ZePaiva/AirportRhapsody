#!/bin/bash
cd bin/
echo "Running Shared Memories"
konsole -e java Rhapsody.server.GeneralRepositoryMain &> GeneralRepositoryLog &
konsole -e java Rhapsody.server.ArrivalExitMain &> ArrivalExitLog &
konsole -e java Rhapsody.server.ArrivalLoungeMain &> ArrivalLoungeLog &
konsole -e java Rhapsody.server.ArrivalQuayMain &> ArrivalQuayLog &
konsole -e java Rhapsody.server.BaggageCollectionMain &> BaggageCollectionLog &
konsole -e java Rhapsody.server.BaggageReclaimMain &> BaggageReclaimLog &
konsole -e java Rhapsody.server.DepartureEntranceMain &> DepartureEntranceLog &
konsole -e java Rhapsody.server.DepartureQuayMain &> DepartureQuayLog &
konsole -e java Rhapsody.server.StorageAreaMain &> StorageAreaLog &

echo "Running clients"
konsole -e java Rhapsody.client.PorterMain &> PorterLog &
konsole -e java Rhapsody.client.BusDriverMain &> BusDriverLog & 
konsole -e java Rhapsody.client.PassengerMain &> PassengerLog &


echo "Now we wait"
