#!/bin/bash
rm -r docs/
echo "generating docs"
javadoc -d docs -sourcepath Rhapsody -subpackages *.java */*.java */*/*.java */*/*/*.java