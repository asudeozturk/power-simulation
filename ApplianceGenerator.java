import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class ApplianceGenerator {

	public ApplianceGenerator() {
		try {
			Appliance [] app = new Appliance[100];  // default 100 possible appliance types
			File inputFile = new File( "ApplianceDetail.txt" );
			Scanner scan = new Scanner( inputFile );
			int count=0;

				while ( scan.hasNext( ) ) {
					StringTokenizer stringToken = new StringTokenizer(scan.nextLine());
					app[count] = new Appliance(stringToken.nextToken(","),
								Integer.parseInt(stringToken.nextToken(",")),
								Integer.parseInt(stringToken.nextToken(",")),
								Double.parseDouble(stringToken.nextToken(",")),
								Boolean.parseBoolean(stringToken.nextToken(",")),
								Double.parseDouble(stringToken.nextToken()));
					count++;
				}


				FileWriter fw = new FileWriter( "output.txt", false);
				BufferedWriter bw = new BufferedWriter( fw );
				for (long location=1;location<=100 ;location++ ) {   // default 100 locations
					int applianceCount=(int)(Math.random()*6)+15;  //15-20 appliances per location
					for (int i=1;i<=applianceCount;i++ ){
						int index=(int)(Math.random()*count);  // pick an appliance randomly
						bw.write(String.valueOf(10000000+location));
						bw.write( "," );
						bw.write(app[index].getName());
						bw.write( "," );
						bw.write(String.valueOf(app[index].getOnW()));
						bw.write( "," );
						bw.write(String.valueOf(app[index].getProbOn()));
						bw.write( "," );
						bw.write(String.valueOf(app[index].getSmart()));
						bw.write( "," );
						bw.write(String.valueOf(app[index].getProbSmart()));
						bw.newLine( );
						bw.flush();

					}
				}

			}
			catch( IOException ioe )
			{
				ioe.printStackTrace( );
			}

	}
}
