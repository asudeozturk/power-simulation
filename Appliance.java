public class Appliance {
  private String name;
  private int onW, offW;
  private double probOn;
  private boolean smart;
  private double probSmart;

  public Appliance (String n, int on, int off, double pOn, boolean s, double pSmart){
    name = n;
    onW = on;
    offW = off;
    probOn = pOn;
    smart = s;
    probSmart = pSmart;
  }

  public String getName() { return name;}
  public int getOnW() { return onW;}
  public int getOffW() { return offW;}
  public double getProbOn() { return probOn;}
  public boolean getSmart() { return smart;}
  public double getProbSmart() { return probSmart;}

  public String toString () {
    return name + "," + onW + "," + offW + "," + probOn + "," + smart + "," + probSmart;
  }
}
