import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Math;
import java.util.Arrays;
import java.util.Random;

public class BAModelSimulator {
	//using 2D-array to represent the BA scale-free network
	public static int[][] node;
	//linkx is the first parameter in array node(the row of 2D array),
	//linky is the second parameter in node(the column of 2D array), eg. node[linkx][linky]
	public static int linkx;
	public static int linky;
	public static int[] degree;
	public static int numberOfDegree;
	
	public static void main(String[] argv) throws FileNotFoundException{	
	long startTime = System.currentTimeMillis();
	//start with 3 node 
	int m0=3;
	//connected to 2 nodes when new node born 
	int m=2;
	//number of time step t 
	int t=497;
	int nodecount=m0+t;
	node = new int[nodecount][nodecount];
	node[0][1]=1;
	node[0][2]=1;
	node[1][0]=1;
	node[1][2]=1;
	node[2][0]=1;
	node[2][1]=1;
	degree = new int[nodecount];
		degree[0]=2;
		degree[1]=2;
		degree[2]=2;
		numberOfDegree = 6;
		//the first node which is added in the network that is the fourth node in the network  
		linkx=4;
		//linky=3;
		while(linkx<=nodecount)
		{
	    checkDegree(nodecount,m);
		linkx++;
		}
		PrintWriter writer = new PrintWriter("degree.txt");
		for(int z=0;z<nodecount;z++)
		{
			//System.out.println(z+"-th degree = "+degree[z]+"\n");
			writer.println(degree[z]+"\n");
			
		}
		writer.close();
		
		PrintWriter network = new PrintWriter("network.txt");
        for(int x=0;x<nodecount;x++)
        {
        	for(int y=0;y<nodecount;y++)
        	{
        		network.print(node[x][y]);
        	}
        network.println(" ");
        }
        network.close();
        spreading(nodecount,m);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);
        System.out.println(numberOfDegree);
	}
	
	public static void checkDegree(int nodecount,int m){
    float num1 = 0;
    int num2 = 0;
	int bigger;
	float biggerx;
	int firstNode;
	int secondNode;
	float[] degreecal=new float[nodecount];
	for(int i=0;i < linkx;i++)
	{
	degreecal[i]= (float) ((degree[i]/numberOfDegree) + Math.random());
	}
	for(int j=0;j<m;j++)
	{
		num1=degreecal[0];
		num2=0;
		for(int k=1;k < linkx-1;k++)
		{
			if(num1<degreecal[k])
			{
				num1=degreecal[k];
				num2=k;
			}		
		}
		if(degreecal[num2]!=0)
		addNode(num2,num1);
		degreecal[num2]=0;
	}
	
	}
	
	private static void addNode(int x,float y)
	{
	degree[x]++;
	//updata node
	node[x][linkx-1]=1;		
	//-------------------------------------------------
	numberOfDegree = numberOfDegree+2;
	degree[linkx-1]++;
	node[linkx-1][x]=1;
	}

	public static void spreading(int nodecount,int m) throws FileNotFoundException
	{
		int infected[]= new int[nodecount];
		int stageinfect[];
		int num=0;
		int firstInfect[] = new int[4];
		int numOFImmunity;
		int immunity;
		int immunityRate;
		int infectedCounter;
		int skip=0;
		int n=0;
		int timestep=100;
		 firstInfect=Arrays.copyOf(findI(nodecount), 4);
		 infected[0]=firstInfect[0];
		 infected[1]=firstInfect[1];
		 infected[2]=firstInfect[2];
		 infected[3]=firstInfect[3];
		 
		 infectedCounter = 4;
		 

		
	for(int a=0;a<9;a++){
		num=0;
		infectedCounter = 4;
		immunity=a*10;
		immunityRate=(nodecount/100);
		numOFImmunity=immunity*immunityRate;
		int immutable1[] = new int[numOFImmunity];
		immutable1=Arrays.copyOf(findmax(numOFImmunity), numOFImmunity);
		 PrintWriter spreading1 = new PrintWriter("spreading"+immunity+"%_"+m+".txt");
		while(num<timestep)
		 {
			 spreading1.println(infectedCounter);
			 for(int x = 0; x<nodecount;x++)
			 {
				 for(int checkinfected = 0; checkinfected<infectedCounter;checkinfected++)
				 {
					 if (x==infected[checkinfected]) 
					 {
						 skip=1;
					 }
					 for (int z=0;z<numOFImmunity;z++)
					 {
						 if(x==immutable1[z])
						 skip=1;
					 }
				 }
				 if(skip==0)
				 {
					 for(int y = 0 ; y<infectedCounter;y++ )
					 {
						 if(node[x][infected[y]]==1)
							 n++;
					 }
				 }
				int check = infectConfirm(n);
				if(infectedCounter>=nodecount)
					break;
				if(check==1)
				{
					infected[infectedCounter]=x;
					infectedCounter++;	
				}
				skip=0;
				n=0;
			 }
			 num++;
		 }
		spreading1.println();
		spreading1.close();
		}
		
	}
	
	public static int[] findI(int nodecount)
	{
		//random choose 4 node to be infected in T0
		int firstInfect[] = new int[4];
		Arrays.fill(firstInfect, nodecount+1);
		int x;
		Random r = new Random();
		for(int i=0; i<4;)
		{
			x=r.nextInt(nodecount); //random 0-nodecount
			if(x!=firstInfect[0]&&x!=firstInfect[1]&&x!=firstInfect[2]&&x!=firstInfect[3])
			{
				firstInfect[i]=x;
				i++;
			}
		}
		return firstInfect;
		
	}
	
	public static int infectConfirm(int n)
	{
		double spreadingRate = 0.1;
		double Pi;
		Pi = 1-Math.pow((1-spreadingRate),n);
		Random random = new Random();
		double ran= ((double)random.nextInt(10000)/10000);
		//System.out.println(Pi + "&&" +ran);
		if(ran<=Pi)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public static int[] findmax(int n){
		int findmax[]= new int[degree.length];
		int fourmax[]= new int[n];
		int x = 0;
		int y;
		findmax=Arrays.copyOf(degree, degree.length);
		for (int i = 0;i<n;i++)
		{
			y=findmax[0];
			for (int j =1;j<findmax.length-1;j++)
			{
				if(y<findmax[j])
				{
					y=findmax[j];
					x=j;
				}
			}
			fourmax[i]=x;
			findmax[x]=0;
			x=0;
		}
		return fourmax;
		
	}
}
	
