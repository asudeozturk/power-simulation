import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Vector;
import java.util.HashSet;
public class Report{ // prepares file reports

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
