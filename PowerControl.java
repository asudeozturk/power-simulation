import java.util.Vector;
import java.util.HashSet;

public class PowerControl {
	private Vector<Apps> apps;
	private int wattLevel;
	private int appNum;

	private Vector<Integer> locationList; //all locations
	private Vector<Integer> smartWattList ; // watts of apps in a grid
	private Vector<Integer> regularWattList ;

	private Vector<Integer> changedLocation; // affected locations
	private Vector<Smart> changedSmart;
	private Vector<Regular> changedRegular;
	private Vector<Smart> smartOff;
	private int smartChange=0,regularChange=0,smartO=0; //# of changed apps in a grid

	private static Vector<String> appReport;
	private float appCondition; //determines if an app is on/off

	public PowerControl(Vector<Apps> a, int w, int n){
		setApps(a);
		setWattLevel(w);
		setAppNum(n);
		setLists();
		setWattLists();

		setLocationList();
		sortApps();
		appCondition = (float)(Math.random());

	}

	public void setApps(Vector<Apps> a) {
		apps = new Vector<Apps>();
		for(int i = 0; i < a.size(); i++) {
				apps.add(a.elementAt(i));
		}
	}
	public void setAppNum(int n){
		this.appNum = n;
	}
	public void setWattLevel(int w) {
		this.wattLevel = w;
	}
	public void setLists() {
		changedSmart = new Vector<Smart>();
		changedRegular = new Vector<Regular>();
		changedLocation = new Vector<Integer>();
		smartOff = new Vector<Smart>();
		locationList = new Vector<Integer>();
		appReport = new Vector<String>();
	}
	public void setWattLists(){
		smartWattList = new Vector<Integer>();
		regularWattList = new Vector<Integer>();
	}
	public void setLocationList() { // locations used for simulation
		for(int i =0; i < apps.size(); i++) {
			locationList.add(apps.elementAt(i).getLocation());
		}
		HashSet<Integer> unique = new HashSet<Integer>(locationList); //removes duplicate locations
		locationList = new Vector<Integer>(unique);

		for(int i =0; i < locationList.size(); i++) {   //sort location nums
			for(int j=i+1; j < locationList.size();j++){
				if(locationList.elementAt(i) > locationList.elementAt(j)){
					int temp = locationList.elementAt(j);
					locationList.set(j,locationList.elementAt(i));
					locationList.set(i,temp);
				}
			}
		}

	}

	public Vector<String> getAppReport() { return appReport; }
	public Vector<Apps> getApps() { return apps;}

	public String control() { //MAIN METHOD FOR POWER CONTROL CLASS
		int index =0,loc=0;
		int tBegin =0,tFinal=0;

		for(int i = 0; i < locationList.size(); i++) {
			setWattLists();
			loc = locationList.elementAt(i);
			int total =0;
			boolean flag = true;

			for(int j =index; j < apps.size()&&flag;j++){

				if( loc == apps.elementAt(j).getLocation()){
					if(apps.elementAt(j).getType().equals("SMART"))
						smartWattList.add(this.findWatt(j));
					else
						regularWattList.add(this.findWatt(j));
				}

				else{
					total = this.calculateSum();
					tBegin = total;

					index =j;
					flag = false;

					if(total > wattLevel) {          //Smart Low
						total = applyLow(total,loc);
					} else {this.smartChange=0;}

					if(total > wattLevel) {
						total = applyBrownOutR(total,loc);  //Regular Off
					}else { this.regularChange=0;}

					if(total > wattLevel) {
						total = applyBrownOutS(total,loc);  //Smart OfF
					}else {this.smartO=0;}
					tFinal = total;

					appReport.add(allAppReport(loc,tBegin,tFinal));
				}
			}
		}
		appReport.add(allAppReport(loc,tBegin,tFinal));

		return screenReport();
	}
	public int applyLow(int total, int loc) {
		this.smartChange=0;
		boolean flag = true;
		Vector<Smart> smart = sortGridSmart(loc);

		for(int i =0 ; i < smart.size() && flag;i++) {
			if(total > wattLevel) {
				if(smartWattList.elementAt(i) != 0) {
					total = total - smartWattList.elementAt(i);
					total += smart.elementAt(i).getLow();
					smartWattList.set(i,smart.elementAt(i).getLow());
					changedSmart.add(smart.elementAt(i));
					smartChange++;
					changedLocation.add(loc);
				}
			}
			else {
				flag = false;
			}
		}
		return total;
	}
	public int applyBrownOutR(int total, int loc) {
		boolean flag = true;
		this.regularChange=0;
		Vector<Regular> regular = sortGridRegular(loc);

		for(int i =0 ; i < regular.size() && flag;i++) {
			if(total > wattLevel) {
				if(regularWattList.elementAt(i) != 0) {
					total = total - regularWattList.elementAt(i);
					regularWattList.set(i,regular.elementAt(i).getOffW());
					changedRegular.add(regular.elementAt(i));
					regularChange++;
					changedLocation.add(loc);
				}
			}
			else {
				flag = false;
			}
		}
		return total;
	}
	public int applyBrownOutS(int total, int loc) {
		boolean flag = true;
		this.smartO=0;
		Vector<Smart> smart = sortGridSmart(loc);

		for(int i =0 ; i < smart.size() && flag;i++) {
			if(total > wattLevel) {
				if(smartWattList.elementAt(i) != 0) {
					total = total - smartWattList.elementAt(i);
					smartWattList.set(i,smart.elementAt(i).getOffW());
					smartOff.add(smart.elementAt(i));
					smartO++;
				}
			}
			else {
				flag = false;
			}
		}
		return total;
	}

	public void sortApps() { //sorts apps based on location, type

		Vector<Apps> sortedApps = new Vector<Apps>();

		for(int i =0; i < locationList.size();i++) { // sort based on location num
			int loc = locationList.elementAt(i);
			for(int j =0; j < apps.size(); j++) {
				if(apps.elementAt(j).getLocation() == loc) {
					sortedApps.add(apps.elementAt(j));
				}
			}
		}
		for(int i =0;i <locationList.size(); i++) {  // sort :first smart,then regular
			int loc = locationList.elementAt(i);
			boolean flag1 = true, flag2 = true;
			int start = 0, end =0;

			for(int j =0; j < sortedApps.size() &&flag1;j++) {
				if(loc == sortedApps.elementAt(j).getLocation()){
					start = j;
					flag1 = false;
				}
			}
			for(int j =start; j < sortedApps.size()&&flag2;j++) {
				if(loc != sortedApps.elementAt(j).getLocation()){
					end = j;
					flag2 =false;
				}
			}

			for(int j =start; j < end;j++) {
				for(int k =start; k<end;k++) {
					if(sortedApps.elementAt(k).getType().equals("SMART")) {
						Apps temp = sortedApps.elementAt(k);
						sortedApps.remove(k);
						sortedApps.add(start,temp);
					}
				}
			}
		}

		this.apps = sortedApps;

	}
	public Vector<Integer> sortChangedLocation() { // sorts affected location list
		HashSet<Integer> unique = new HashSet<Integer>(changedLocation); //removes duplicate locations
		Vector<Integer> afftectedLocations = new Vector<Integer>(unique);
		for(int i =0; i < afftectedLocations.size(); i++) {   //sort location num
			for(int j=i+1; j < afftectedLocations.size();j++){
				if(afftectedLocations.elementAt(i) > afftectedLocations.elementAt(j)){
					int temp = afftectedLocations.elementAt(j);
					afftectedLocations.set(j,afftectedLocations.elementAt(i));
					afftectedLocations.set(i,temp);
				}
			}
		}
		return afftectedLocations;
	}
	public Vector<Smart> sortGridSmart(int loc) {  // sorts smart apps in decreasing order
		Vector<Smart> smart = new Vector<Smart>();
		for(int i =0; i < apps.size(); i++) {
			if(apps.elementAt(i).getLocation() == loc &&  apps.elementAt(i).getType().equals("SMART"))
				smart.add((Smart)apps.elementAt(i));
		}

		for(int i =0; i < smart.size();i++) {
			for(int j =0; j < smart.size() - 1;j++) {
				if(smart.elementAt(j).getOnW() < smart.elementAt(j+1).getOnW()){
					Smart temp = smart.elementAt(j+1);
					smart.set(j+1, smart.elementAt(j));
					smart.set(j,temp);
				}
			}
		}
		return smart;
	}
	public Vector<Regular> sortGridRegular(int loc) { // sorts regular apps in increasing order
		Vector<Regular> regular = new Vector<Regular>();
		for(int i =0; i < apps.size(); i++) {
			if(apps.elementAt(i).getLocation() == loc &&  apps.elementAt(i).getType().equals("REGULAR"))
				regular.add((Regular)apps.elementAt(i));
		}

		for(int i =0; i < regular.size();i++) {
			for(int j =0; j < regular.size() - 1;j++) {
				if(regular.elementAt(j).getOnW() < regular.elementAt(j+1).getOnW()){
					Regular temp = regular.elementAt(j+1);
					regular.set(j+1, regular.elementAt(j));
					regular.set(j,temp);
				}
			}
		}
		return regular;
	}

	public int findWatt(int i) { //determines if an app is on/off
		int watt =0; //off
		if(apps.elementAt(i).getProbOn() >= appCondition)
			watt = apps.elementAt(i).getOnW(); //on
		return watt;
	}
	public int calculateSum() {  // total watt of a grid
		int sum =0;
		for(int i =0; i < smartWattList.size(); i++) {
			sum += smartWattList.elementAt(i);
		}
		for(int i =0; i < regularWattList.size(); i++) {
			sum += regularWattList.elementAt(i);
		}
		return sum;
	}

	public String screenReport() { //screen report


		Vector<Integer> afftectedLocations = sortChangedLocation();

		String output="\nAffected locations:";
		for(int i =0;i < afftectedLocations.size();i++) {
			output+= "\n" + afftectedLocations.elementAt(i);
			int countS =0, countR=0,countBS=0;

			for(int j =0;j < changedSmart.size();j++) {
				if( changedSmart.elementAt(j).getLocation() == afftectedLocations.elementAt(i))
					countS++;
			}
			for(int j =0;j < changedRegular.size();j++) {
				if( changedRegular.elementAt(j).getLocation() == afftectedLocations.elementAt(i))
					countR++;
			}
			for(int j =0;j < smartOff.size();j++) {
				if( smartOff.elementAt(j).getLocation() == afftectedLocations.elementAt(i))
					countBS++;
			}


			output+=" Turn low " + (countS-countBS) + " smart. Turn off " + countR +  " regular."+ "TurnOff: " + countBS + " smart.";
		}
		return output;
	}
	public String allAppReport(int loc,int tBegin, int tFinal) { //all appliances report
		String report = " ";
		int smartNum=0, regularNum=0;
		for(int i =0; i < apps.size();i++) {
			if(loc == apps.elementAt(i).getLocation()){
				if(apps.elementAt(i).getType().equals("SMART"))
					smartNum++;
				else
					regularNum++;
			}
		}

		report+= "\n" + loc;
		report+=" #Smart: " + smartNum + " #Regular: " + regularNum;
		report+= " Initial Watt: " + tBegin;
		report+= " Turn Low#: " + (smartChange-smartO) +" Brown out R#: " +regularChange + " Brown out S#: " + smartO;
		report += " Final Watt: " + tFinal;

		return report;
	}
	public Vector<String> detailReport() { // file report
		Vector<String> report = new Vector<String>();
		report.add("Probability : " + appCondition);
		report.add("\nTurn to Low: " + (changedSmart.size()-smartOff.size()) + " appliances.\n");
		for(int i =0 ; i < changedSmart.size(); i++) {
			boolean flag = true;
			for(int j =0; j < smartOff.size()&& flag;j++) {
				if(smartOff.elementAt(j).getUniqueID() == changedSmart.elementAt(i).getUniqueID())
					flag = false;
			}
			if(flag) {report.add("\n" + changedSmart.elementAt(i).toString());}

		}
		report.add("\nTurn to Off: \n"+ (changedRegular.size()+smartOff.size()) + " appliances.\n");
		for(int i =0 ; i < changedRegular.size(); i++) {
			report.add( "\n" + changedRegular.elementAt(i).toString());
		}
		for(int i =0 ; i < smartOff.size(); i++) {
			report.add( "\n" + smartOff.elementAt(i).toString());
		}

		return report ;

	}
}
