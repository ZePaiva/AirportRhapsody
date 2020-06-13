#!/bin/bash
cd bin/
echo "Running Shared Memories"
konsole -e java -cp bin Rhapsody.server.GeneralRepositoryMain &> GeneralRepositoryLog &
konsole -e java -cp bin Rhapsody.server.ArrivalExitMain &> ArrivalExitLog &
konsole -e java -cp bin Rhapsody.server.ArrivalLoungeMain &> ArrivalLoungeLog &
konsole -e java -cp bin Rhapsody.server.ArrivalQuayMain &> ArrivalQuayLog &
konsole -e java -cp bin Rhapsody.server.BaggageCollectionMain &> BaggageCollectionLog &
konsole -e java -cp bin Rhapsody.server.BaggageReclaimMain &> BaggageReclaimLog &
konsole -e java -cp bin Rhapsody.server.DepartureEntranceMain &> DepartureEntranceLog &
konsole -e java -cp bin Rhapsody.server.DepartureQuayMain &> DepartureQuayLog &
konsole -e java -cp bin Rhapsody.server.StorageAreaMain &> StorageAreaLog &

echo "Running clients"
konsole -e java -cp bin Rhapsody.client.PorterMain &> PorterLog &
konsole -e java -cp bin Rhapsody.client.BusDriverMain &> BusDriverLog & 
konsole -e java -cp bin Rhapsody.client.PassengerMain &> PassengerLog &

echo "Now we wait"
