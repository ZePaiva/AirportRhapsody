#!/bin/bash
cd bin
echo "Running Shared Memories"
xterm -e java Rhapsody.server.GeneralRepositoryMain &
sleep .5
xterm -e java Rhapsody.server.ArrivalExitMain &
xterm -e java Rhapsody.server.ArrivalLoungeMain &
xterm -e java Rhapsody.server.ArrivalQuayMain &
xterm -e java Rhapsody.server.BaggageCollectionMain &
xterm -e java Rhapsody.server.BaggageReclaimMain &
xterm -e java Rhapsody.server.DepartureEntranceMain &
xterm -e java Rhapsody.server.DepartureQuayMain &
xterm -e java Rhapsody.server.StorageAreaMain &

echo "Running clients"
sleep .5
xterm -e java Rhapsody.client.PorterMain &
xterm -e java Rhapsody.client.BusDriverMain & 
xterm -e java Rhapsody.client.PassengerMain &

echo "Now we wait"
