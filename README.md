# power-simulation

*CS 116 - Object Oriented Programming (Class Project)*

### Power Control Simulation System

This program allows users to run a power control simulation which handles the electricity usage across different locations. The user is asked to provide a warning level for the grid. If the total watt usage in a location exceeds the warning level, the system tries to reduce the power usage by first setting smart appliances to low mode. If the usage is still higher than the warning level, smart/regular appliances are turned off one by one in order of decreasing electricity usage. 

Future improvement idea: Turning off appliances based on priority. Some appliances like medical equipments, refrigerators etc. are essential and should not be turned off easily.

#### Programs

**PowerSimClient**  
-Main class, user interactive  
-Calls ApplianceGenerator to create a random list of appliances  
-Offers users a list of actions to choose from:  
  - Adding a new appliance to the list
  - Adding multiple appliances to the list via file
  - Removing an appliance from the list
  - Viewing details of an appliance via unique ID
  - Viewing all appliances in a given location
  - Viewing appliances(of same type) across all locations
  - Run a simulation and prepare reports

**PowerSim**  
-Stores the appliances created by ApplianceGenerator  
-Handles all user interactions  

**ApplianceGenerator**  
-Creates new Appliance objects by reading ApplianceDetail.txt  
-Prepares a random list of Appliance where each appliance is assigned to a location  
-The new appliances are written to output.txt  

**Report**  
-Prepares statistics and writes the results to files after the simulation is complete  
  - affectedApps : lists detailed info of all appliances that were turned off/low  
  - allApps : shows the number of smart and regular appliances, total watt usage before and after power control at each location  
  - appReport : groups the appliances by kind and lists the number of appliances in each group  

**PowerControl**  
-Called during simulation  
-Handles all power control actions including:  
  - Setting smart appliances to low mode  
  - Turning off smart appliances  
  - Turning off regular appliances  

**Appliance**  
-Object class for appliances   
-Used by Appliance Generator  

**Apps**  
-Object class for appliances  
-Used by PowerSim  

**Smart** extends Apps  
-Object class for smart appliances  

**Regular** extends Apps  
-Object class for regular appliances  

#### Files  
ApplianceDetail.txt - a static list of different type of appliances  
output.txt - created after ApplianceGenerator is called  
appReport.txt - created before the simulation starts  
affectedApps.txt - created at the end of the simulation  
allApps.txt - created at the end of the simulation  
