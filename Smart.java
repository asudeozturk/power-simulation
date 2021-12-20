public class Smart extends Apps{
	private int low;
	private float smartProb;
	
	public Smart(int l, String n, int on, float p, float sProb ) {
		super(l,n,on,p,"SMART");
		setLow(sProb);
	}
	
	public void setLow(float sProb){ // reduced watt
		this.low = (int)(super.getOnW()-super.getOnW()*sProb);
	}
	
	public void setSmartProb(float sProb){ 
		this.smartProb =sProb;
	}
	
	public int getLow() { return this.low;}
	public float getSmartProb() { return this.smartProb;} 
	
}