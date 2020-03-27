#!/bin/bash

declare -i var
var=0

moreBad() {
	let "var+=1"
}

#trap 'moreBad' SIGINT

trap 'exit' SIGINT

start=`date +%s`

for i in $(seq 1 1000)
do
	echo -e "\nRun n." $i
	java -cp bin/ Rhapsody.Airport
	cp logs/log.txt logs/log${i}.txt
done

end=`date +%s`

runtime=$((end-start))
echo -e "\nBad Runs: " $var