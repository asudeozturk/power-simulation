import java.util.Scanner;
import java.util.Vector;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.lang.Exception;

public class PowerSimClient {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		PowerSim sim = new PowerSim();												//Creates a new power simulation
		ApplianceGenerator appGen = new ApplianceGenerator(); //Generates appliances, writes them to output.txt

		// creating appliances
		try {

			File fileName = new File("output.txt");
			Scanner scan = new Scanner(fileName);
			String line = "";
			while(scan.hasNextLine()){
				line = scan.nextLine();
				String [] tok = line.split(",");

				if(Boolean.parseBoolean(tok[4])){
					Apps smartApp = new Smart(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
											   Float.parseFloat(tok[3]),Float.parseFloat(tok[5]));
					sim.addAppToList(smartApp);
				}
				else{
					Apps regularApp = new Regular(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
											   Float.parseFloat(tok[3]));
					sim.addAppToList(regularApp);
				}
			}
		} catch(IOException ex) { System.out.println("File couldn't found");}

		// User - interactive part
		while(true){
				System.out.println("\nSelect an option:");
				System.out.println("Type \"A\" to add an appliance or \"AI\" to add appliances with a file.");
				System.out.println("Type \"D\" to delete appliance.");
				System.out.println("Type \"F\" to find appliance.");
				System.out.println("Type \"V\" to view all appliances.");
				System.out.println("Type \"VL\" to view all appliances in a location.");
				System.out.println("Type \"VA\" to view an appliance across all locations.");
				System.out.println("Type \"R\" to prepare report (appReport.txt) and run the simulation.");
				Scanner userInput = new Scanner(System.in);
				String option = userInput.nextLine();

				switch (option) {
					case "A":
						System.out.println("\nEnter in format: location,name,onWatt,probability,smart(true/false), smartProb ");
						String newApp = userInput.nextLine();
						if(sim.addAppliance(newApp)){
							System.out.println("Appliance is added.");
						}
						else { System.out.println("Couldn't add");}
						break;
					case "AI":
						System.out.println("\nEnter the name of the file: \nformat: location,name,onWatt,probability,smart(true/false), smartProb ");
						String newApps = userInput.nextLine();
						if(sim.addApplianceFile(newApps)){
							System.out.println("Appliances are added.");
						}
						break;

					case "D": System.out.print("\nEnter an ID to delete:");
						while (!userInput.hasNextInt() ) {
							userInput.next();
							System.out.print("Please enter an integer: ");
						}
						int id  = userInput.nextInt();
						if(sim.deleteAppliance(id)){ System.out.println("Deleted.");}
						else { System.out.println("Couldn't find ID");}
						break;
					case "F": System.out.print("\nEnter an ID to view the appliance: ");

						while (!userInput.hasNextInt() ) {
							userInput.next();
							System.out.print("Please enter an integer: ");
						}
						id = userInput.nextInt();

						System.out.println(sim.findAppliance(id));
						break;
					case "V" :
						for(int i =0; i < sim.getAllApps().size();i++) {
							System.out.println(sim.getAllApps().elementAt(i).toString());
						}
						break;
					case "VL" : System.out.print("\nEnter a location: ");
						while (!userInput.hasNextInt() ) {
							userInput.next();
							System.out.print("Please enter an integer: ");
						}
						int loc = userInput.nextInt();
						System.out.println(sim.viewLocation(loc));
						break;
					case "VA" : System.out.print("\nEnter the name of appliance: ");
						String name = userInput.nextLine();
						System.out.println(sim.viewAppliance(name));
						break;
					case "R": System.out.println("Running Simulation.");
						sim.appReport();
						sim.run();
						System.exit(0);
					default: System.out.println("Wrong option! Try again");
						break;
				}
			}
	}
}
