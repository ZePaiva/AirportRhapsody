#!/bin/bash
echo "creating necessary binaris directories"
# server dirs
mkdir -p bin/Rhapsody/server/communications
mkdir -p bin/Rhapsody/server/interfaces
mkdir -p bin/Rhapsody/server/proxies
mkdir -p bin/Rhapsody/server/sharedRegions
mkdir -p bin/Rhapsody/server/stubs
# client dirs
mkdir -p bin/Rhapsody/client/communications
mkdir -p bin/Rhapsody/client/entities
mkdir -p bin/Rhapsody/client/stubs
# common dirs
mkdir -p bin/Rhapsody/common

echo "compiling all java code"
javac Rhapsody/server/*.java
javac Rhapsody/client/*.java

echo "moving code to binaries dirs"
# moving server code
mv Rhapsody/server/*.class bin/Rhapsody/server
mv Rhapsody/server/communications/*.class bin/Rhapsody/server/communications
mv Rhapsody/server/interfaces/*.class bin/Rhapsody/server/interfaces
mv Rhapsody/server/proxies/*.class bin/Rhapsody/server/proxies
mv Rhapsody/server/sharedRegions/*.class bin/Rhapsody/server/sharedRegions
mv Rhapsody/server/stubs/*.class bin/Rhapsody/server/stubs
# moving client code
mv Rhapsody/client/*.class bin/Rhapsody/client
mv Rhapsody/client/communications/*.class bin/Rhapsody/client/communications
mv Rhapsody/client/entities/*.class bin/Rhapsody/client/entities
mv Rhapsody/client/stubs/*.class bin/Rhapsody/client/stubs
# moving common code
mv Rhapsody/common/*.class bin/Rhapsody/common

echo "build done, ready to deploy"