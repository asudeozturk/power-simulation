import java.util.Scanner;
import java.util.Vector;
import java.util.HashSet;
import java.io.File;           
import java.io.IOException;
import java.util.Collections;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.lang.Exception;

public class EventSimulator {
	private Vector<Apps> allApp;
	
	public Vector<Apps> shuffleAndSelect(Vector<Apps> a, int num){  //selects apps for simulation
		Vector<Apps> simApps = new Vector<Apps>();
		Vector<Apps> temp = new Vector<Apps>();
		
		for(int i = 0; i < a.size(); i++) {
			temp.add(a.elementAt(i));
		}
		Collections.shuffle(temp);
		
		for(int i = 0; i < num ; i++) {
			simApps.add(temp.elementAt(i));
		}
		return simApps;
	}
	
	public void run(){ // starts the simulation
		final int ARRIVAL_MEAN=5;
		int nextArrivalTime, callCount=0;
		int simulationLength=0, minute=0,  appsNum=0, wattLevel =0; 
		Scanner input = new Scanner(System.in);
		nextArrivalTime = minute + getRandInt('E', ARRIVAL_MEAN, 0);
		Report r = new Report();
		
		while (simulationLength<5 || simulationLength>1440 )	{
			System.out.print("How many minutes long is the simulation? (min: 5, max:1440): "); 

			while (!input.hasNextInt()) {
				input.next();
				System.out.print("Please enter an integer: ");
			}
			simulationLength = input.nextInt();
		}

		while (appsNum<=0 || appsNum >1000)	{ 
			System.out.print("How many appliances are used for simulation? (max:1000): "); 

			while (!input.hasNextInt()) {
				input.next();
				System.out.print("Please enter an integer: ");
			}
			appsNum = input.nextInt();
		}
		while (wattLevel<=0)	{             
			System.out.print("What is the warning level for the grid: "); 

			while (!input.hasNextInt()) {
				input.next();
				System.out.print("Please enter an integer: ");
			}
			wattLevel = input.nextInt();
		}
		
		
		
		Vector<Apps> selected = new Vector<Apps>();
		 selected = this.shuffleAndSelect(allApp,appsNum);
		 
		while(minute<=simulationLength) {
			while ((minute == nextArrivalTime) && (minute<=simulationLength)) {
				callCount++;
				System.out.println("\nMinute:"+minute+" Event#"+callCount);
				nextArrivalTime=minute+getRandInt('E', ARRIVAL_MEAN, 0);
				
				
				PowerControl p = new PowerControl(selected,wattLevel,appsNum);
				System.out.println(p.control()); //screen report
				
				r.allAppsReport(minute,callCount,p.getAppReport()); //file report
				r.affectedAppsReport(minute,callCount,p.detailReport()); //file report
				
			}
			minute++;
		} 
		
		
		System.out.println("\nNumber of events = " + callCount);
		System.out.println("\nSEE AFFECTEDAPPS.TXT FOR DETAIL.");
	}
	
	public boolean addAppliance(String s) {  
		boolean flag = false;
		String [] tok = s.split(",");
				
		try{
			if(Boolean.parseBoolean(tok[4])){
				Smart smartApp = new Smart(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
										   Float.parseFloat(tok[3]),Float.parseFloat(tok[5]));
				allApp.add(smartApp);
				System.out.println(smartApp.toString());
				flag =  true;
			}
			else  {
				Regular regularApp = new Regular(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
										   Float.parseFloat(tok[3]));
				allApp.add(regularApp);
				System.out.println(regularApp.toString());
				flag = true;
			}
		}
		catch(Exception e) { 
			System.out.println("Wrong Format."); 
			return false;
		}
		
		return flag;
	}
	public boolean addApplianceFile(String s) {  
		boolean flag = false;
		try{
			
			File fileName = new File(s);
			Scanner input = new Scanner(fileName);
			
			while(input.hasNextLine()){
				String line = input.nextLine();
				String [] tok = line.split(",");
			
				if(Boolean.parseBoolean(tok[4])){
					Smart smartApp = new Smart(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
											   Float.parseFloat(tok[3]),Float.parseFloat(tok[5]));
					allApp.add(smartApp);
					flag = true;
				}
				else{
					Regular regularApp = new Regular(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
											   Float.parseFloat(tok[3]));
					allApp.add(regularApp);
					flag = true;
				}
				
			}
		}catch(Exception e) { 
			System.out.println("Wrong Format."); 
			return false;
		}
		return flag;
	}
	public boolean deleteAppliance(int id) { 
		for(int i =0; i < allApp.size(); i++) {
			if(allApp.elementAt(i).getUniqueID() == id) {
				System.out.println(allApp.elementAt(i).toString());
				allApp.removeElementAt(i);
				return true;
			}
		}
		
		return false;
	}
	public String findAppliance(int id) {
		String app="";
		boolean flag= true;
		for(int i =0; i < allApp.size()&& flag; i++) {
			if(allApp.elementAt(i).getUniqueID() == id) {
				app = allApp.elementAt(i).toString();
				flag = false;
			}
			else
				app = "ID couldn't found";
		}
		return app;
	}
	public String viewLocation(int loc) {
		
		String output ="";
		for(int i =0; i < allApp.size(); i++) {
			if(allApp.elementAt(i).getLocation() == loc){
				output+= "\n" + allApp.elementAt(i).toString();
			}
		}
		if(output.equals("")) 
			output = "No Location Found.";
		return output;
	}
	public String viewAppliance(String n) {
		
		String output ="";
		for(int i =0; i < allApp.size(); i++) {
			if(allApp.elementAt(i).getName().equals(n)){
				output+= "\n" + allApp.elementAt(i).toString();
			}
		}
		if(output.equals("")) 
			output = "No Appliance Found.";
		return output;
	}
	public void appReport(){
		try {
			FileWriter f = new FileWriter( "appReport.txt", false);
			BufferedWriter b = new BufferedWriter( f );
			
			int totalLocNum=0,totalSmart = 0, totalRegular =0;
			Vector<Integer> locNum = new Vector<Integer>();
			for(int i =0; i <allApp.size(); i++) {
				locNum.add(allApp.elementAt(i).getLocation());
				if(allApp.elementAt(i).getType().equals("SMART"))
					totalSmart++;
				else
					totalRegular++;
			}
			HashSet<Integer> l = new HashSet<Integer>(locNum); 
			locNum = new Vector<Integer>(l);
			totalLocNum = locNum.size();
			
			b.write("Total number of locations: " + totalLocNum);
			b.newLine();
			b.write("# of Smart Appliances: " + totalSmart);
			b.newLine();
			b.write("# of Regular Appliances: " + totalRegular);
			b.newLine();
			b.newLine();
			b.flush();
			
			Vector<String> appName = new Vector<String>();
			for(int i =0; i <allApp.size(); i++) {
				appName.add(allApp.elementAt(i).getName());}
			HashSet<String> n = new HashSet<String>(appName); 
			appName = new Vector<String>(n);
			
			
			Vector<Integer> appNum = new Vector<Integer>();
			for(int i =0; i <appName.size(); i++) {
				int count =0;
				String name = appName.elementAt(i);
				for(int j =0; j <allApp.size(); j++) {
					if(allApp.elementAt(j).getName().equals(name)){
						count++;
						
					}
					
				}
				appNum.add(count);
			}
			
			
			for(int i =0; i <appName.size(); i++) {
				 b.write(appName.elementAt(i) + " : " + appNum.elementAt(i));
				 b.newLine();
				 b.flush();
			}
		}catch( IOException ioe ){
			ioe.printStackTrace( );
		}
	
	}
	
	public static class Report{ // prepares file reports 
		private static FileWriter fw1,fw2;
		private static BufferedWriter bw1,bw2;
		
		public Report() {
			try{ 
			fw1 = new FileWriter( "affectedApps.txt", false);
			bw1 = new BufferedWriter( fw1 );
			fw2 = new FileWriter( "allApps.txt", false);
			bw2 = new BufferedWriter( fw2 );
			
			} catch( IOException ioe ){
			ioe.printStackTrace( );}
		}
		
		public void affectedAppsReport(int m, int c, Vector<String> o){
			try{ 
				bw1.write("Minute: ");
				bw1.write(String.valueOf(m));
				bw1.write(" Event #: ");
				bw1.write(String.valueOf(c));
				bw1.newLine();
				for(int i =0; i < o.size();i++) {
					bw1.write(o.elementAt(i));
					bw1.newLine();
				}
				bw1.newLine();
				bw1.flush();
				
			}catch( IOException ioe ){
			ioe.printStackTrace( );}
		}
		
		public void allAppsReport(int m, int c, Vector<String> o){
			try{ 
				bw2.write("Minute: ");
				bw2.write(String.valueOf(m));
				bw2.write(" Event #: ");
				bw2.write(String.valueOf(c));
				bw2.newLine();
				for(int i =0; i < o.size();i++) {
					bw2.write(o.elementAt(i));
					bw2.newLine();
				}
				bw2.newLine();
				bw2.flush();
				
			}catch( IOException ioe ){
			ioe.printStackTrace( );}
		}
		
	}
	
	public static void main(String[] args) {
	
		Scanner input = new Scanner(System.in);
		EventSimulator sim = new EventSimulator();
		sim.allApp = new Vector<Apps>();
		
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
					sim.allApp.add(smartApp);
				}
				else{
					Apps regularApp = new Regular(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
											   Float.parseFloat(tok[3]));
					sim.allApp.add(regularApp);
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
						for(int i =0; i < sim.allApp.size();i++) {
							System.out.println(sim.allApp.elementAt(i).toString());
						}
						break;
					case "VL" : 
						System.out.print("\nEnter a location: ");
						while (!userInput.hasNextInt() ) {
							userInput.next();
							System.out.print("Please enter an integer: ");
						}
						int loc = userInput.nextInt();
						System.out.println(sim.viewLocation(loc));
						break;
					case "VA" : 
						System.out.print("\nEnter the name of appliance: ");
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
	
	public static int getRandInt(char type, int x, int y) {
		int num;
		switch (type) { 
		case 'U': case 'u':		// Uniform Distribution
			num = (int)(x + (Math.random()*(y+1-x))); 
			break;
		case 'E': case 'e':		// Exponential Distribution
			num = (int)(-1*x*Math.log(Math.random()));  
			break;	
		case 'N': case 'n':		// Normal Distribution
			num = (int)( x +
                (y * Math.cos(2 * Math.PI * Math.random()) *
                Math.sqrt(-2 * Math.log(Math.random()))));
			break;			
		default:				// Uniform Distribution
			num = (int)(x + (Math.random()*(y+1-x))); 
		}
		return num;
	}
}