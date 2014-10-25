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
# Param =7 : 
# Param =8 : 
# Param =9 : 
# Param =10 : 

cd HeatedEarth
cd bin

x="$1"
b="$2"
directory="/home/ubuntu/git/CS6310_P2_T11/Reports/Test Results/Grid Spacing_15 Time Step_1"

if [ $x -eq 1 ] 
then java EarthSim.Demo -b$b >> $directory/results_GUI1.csv
elif [ $x -eq 2 ] 
then java EarthSim.Demo -s -p -b$b >> $directory/results_GUI3.csv
elif [ $x -eq 3 ] 
then java EarthSim.Demo -t -b$b >> $directory/results_SE1.csv
elif [ $x -eq 4 ] 
then java EarthSim.Demo -s -p -t -b$b >> $directory/results_SE3.csv
elif [ $x -eq 5 ] 
then java EarthSim.Demo -r -b$b >> $directory/results_P1.csv
elif [ $x -eq 6 ] 
then java EarthSim.Demo -s -p -r -b$b >> $directory/results_P3.csv
fi
