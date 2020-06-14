#!/bin/bash
rm -r doc/

echo "generating docs directories"
# server dirs
mkdir -p doc/Rhapsody/server/communications
mkdir -p doc/Rhapsody/server/interfaces
mkdir -p doc/Rhapsody/server/proxies
mkdir -p doc/Rhapsody/server/sharedRegions
mkdir -p doc/Rhapsody/server/stubs
# client dirs
mkdir -p doc/Rhapsody/client/communications
mkdir -p doc/Rhapsody/client/entities
mkdir -p doc/Rhapsody/client/stubs
# common dirs
mkdir -p doc/Rhapsody/common

echo "generating docs"

javadoc -d docs -sourcepath Rhapsody -subpackages *.java */*.java */*/*.java */*/*/*.java