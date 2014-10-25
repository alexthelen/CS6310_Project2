#!/bin/bash
# Author: Alexander Thelen
# Version 1.0
# This script is used to automate the command line launch of tests for the HeatedEarth program.
# A parameter number is passed to switch the configuration of HeatedEarth's command line parameters
# and the results are piped into a file.
# Run script in BASH shell using sh ./runTest.sh [param #] [buffer size]

# Param =1 : GUI, SE, Pres on 1 thread, GUI with initiative
# Param =2 : GUI, SE, Pres on seperate threads, GUI with initiative
# Param =3 : GUI, SE, Pres on 1 thread, SE with initiative
# Param =4 : GUI, SE, Pres on seperate thread, SE with initiative
# Param =5 : GUI, SE, Pres on 1 thread, Pres with initiative
# Param =6 : GUI, SE, Pres on seperate thread, Pres with initiative

cd HeatedEarth
cd bin

x="$1"
b="$2"

if [ $x -eq 1 ] 
then java EarthSim.Demo -b$b >> results_GUI1.csv
elif [ $x -eq 2 ] 
then java EarthSim.Demo -s -p -b$b >> results_GUI3.csv
elif [ $x -eq 3 ] 
then java EarthSim.Demo -t -b$b >> results_SE1.csv
elif [ $x -eq 4 ] 
then java EarthSim.Demo -s -p -t -b$b >> results_SE3.csv
elif [ $x -eq 5 ] 
then java EarthSim.Demo -r -b$b >> results_P1.csv
elif [ $x -eq 6 ] 
then java EarthSim.Demo -s -p -r -b$b >> results_P3.csv
fi
