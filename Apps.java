public class Apps{
	
		private int location;
		private String name;
		private int onW, offW; 
		private float probOn; 
		private String type;
		private int uniqueID;
		private static int staticID=1;
	    

		public Apps(int l, String n, int on, float p, String t ){
			setLocation(l);
			setName(n);
			setOnW(on);
			setOffW(0);
			setProbOn(p);
			setType(t);
			setUniqueID();
		}
		
		private void setLocation(int l) { // private or public
			this.location = l;
		}
		private void setName(String n) {
			this.name = n;
		}
		private void setOnW(int on) {
			this.onW = on;
		}
		private void setOffW(int off) {
			this.offW =off;
		}
		private void setProbOn(float p) {
			this.probOn = p;
		}
		private void setType(String t) {
			this.type = t;
		}
		private void setUniqueID(){
			this.uniqueID = staticID;
			staticID++;
		}
		
		public int  getLocation() { return this.location; }
		public String getName() { return this.name; }
		public int getOnW() { return this.onW;}
		public int getOffW() { return this.offW;}
		public float getProbOn() { return this.probOn;}
		public String getType() { return this.type; }
		public int getUniqueID() { return this.uniqueID;}
		
		
		public String toString() {
			String output = type + " " + location + " ID: " + uniqueID + " N: " + name ;
			output += " On: " + onW + " Prob: " + probOn;
			return output;
		}
		
}