import java.util.Scanner;
import java.util.Vector;
import java.util.HashSet;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.lang.Exception;



public class PowerSim {
  private Vector<Apps> allApps;

  public PowerSim() {
    allApps = new Vector<Apps>();
  }
  public Vector<Apps> getAllApps() { return allApps; }

  public void addAppToList(Apps appliance) {
    	allApps.add(appliance);
  }


 ///////

  public boolean addAppliance(String s) {
    boolean flag = false;
    String [] tok = s.split(",");

    try{
      if(Boolean.parseBoolean(tok[4])){
        Smart smartApp = new Smart(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
                       Float.parseFloat(tok[3]),Float.parseFloat(tok[5]));
        addAppToList(smartApp);
        System.out.println(smartApp.toString());
        flag =  true;
      }
      else  {
        Regular regularApp = new Regular(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
                       Float.parseFloat(tok[3]));
        addAppToList(regularApp);
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
          addAppToList(smartApp);
          flag = true;
        }
        else{
          Regular regularApp = new Regular(Integer.parseInt(tok[0]), tok[1], Integer.parseInt(tok[2]),
                         Float.parseFloat(tok[3]));
          addAppToList(regularApp);

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
    for(int i =0; i < allApps.size(); i++) {
      if(allApps.elementAt(i).getUniqueID() == id) {
        System.out.println(allApps.elementAt(i).toString());
        allApps.removeElementAt(i);
        return true;
      }
    }

    return false;
  }

  public String findAppliance(int id) {
    String app="";
    boolean flag= true;
    for(int i =0; i < allApps.size() && flag; i++) {
      if(allApps.elementAt(i).getUniqueID() == id) {
        app = allApps.elementAt(i).toString();
        flag = false;
      }
      else
        app = "ID couldn't found";
    }
    return app;
  }

  public String viewLocation(int loc) {

    String output ="";
    for(int i =0; i < allApps.size(); i++) {
      if(allApps.elementAt(i).getLocation() == loc){
        output+= "\n" + allApps.elementAt(i).toString();
      }
    }
    if(output.equals(""))
      output = "No Location Found.";
    return output;
  }

  public String viewAppliance(String n) {

    String output ="";
    for(int i =0; i < allApps.size(); i++) {
      if(allApps.elementAt(i).getName().equals(n)){
        output+= "\n" + allApps.elementAt(i).toString();
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
      for(int i =0; i <allApps.size(); i++) {
        locNum.add(allApps.elementAt(i).getLocation());
        if(allApps.elementAt(i).getType().equals("SMART"))
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
      for(int i =0; i <allApps.size(); i++) {
        appName.add(allApps.elementAt(i).getName());}
      HashSet<String> n = new HashSet<String>(appName);
      appName = new Vector<String>(n);


      Vector<Integer> appNum = new Vector<Integer>();
      for(int i =0; i <appName.size(); i++) {
        int count =0;
        String name = appName.elementAt(i);
        for(int j =0; j <allApps.size(); j++) {
          if(allApps.elementAt(j).getName().equals(name)){
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

/////////
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
    selected = shuffleAndSelect(allApps,appsNum);

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

  public  int getRandInt(char type, int x, int y) {
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
}
