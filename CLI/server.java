 import java.io.*;
import java.net.*;
class lion extends Thread
{
	Socket s[]=new Socket[5];
	String name[]=new String[100];
	int i;
	int x;
	public lion(Socket st[],int b,int t,String name[])
	{
		s=st;
		this.name=name;
		i=b;
		x=t;
	}
	public void run()
	{
			try
			{
				DataInputStream dis=new DataInputStream(s[x].getInputStream());
				String str="";
				String sts="";
				DataOutputStream dip=null;
				do
				{
					sts=(String)dis.readUTF();
					str=name[x]+" : "+sts;
					for(int t=0;t<i;t++)
					{
							dip=new DataOutputStream(s[t].getOutputStream());
							dip.writeUTF(str);
							dip.flush();
				
					}

				}while(!(str.equals("stop")));
				dip.close();
				dis.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}

		}
	}
	
	class user extends Thread
	{
		String name;
		Socket st;
		user(Socket s)
		{
			st=s;
		}
		public void run()
		{
			try
			{
				DataInputStream dip=new DataInputStream(st.getInputStream());
				name=(String)dip.readUTF();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
class clients extends Thread
{
	ServerSocket st;
	Socket sr[]=new Socket[100];
	int i;
	int x=0;
	clients(ServerSocket s,int b)
	{
		st=s;
		i=b;
	}
	public void run()
	{
		lion stc[]=new lion[100];
		user sts[]=new user[100];
		String name[]=new String[100];
		try
		{
			System.out.println("Listening on port 8888....");
			while(x<i)
			{	
				sr[x]=st.accept();
				System.out.println((x+1)+" Device Connected!!!");
				x++;
			}
			System.out.println("All Device Connected!!!");
			System.out.println("Connection Established!!!");
			String str="Enter Your Name : ";
			DataOutputStream dis=null;
			DataInputStream dip=null;
			for(int q=0;q<i;q++)
			{
				dis=new DataOutputStream(sr[q].getOutputStream());
				dis.writeUTF(str);
				dis.flush();
			}
			for(int q=0;q<i;q++)
			{
				sts[q]=new user(sr[q]);
				sts[q].setDaemon(true);	
				sts[q].start();
			}
			boolean check=true;
			do
			{
				check=true;
				for(int q=0;q<i;q++)
				{
					if(sts[q].name==null)
					{
						check=false;
					}
					else
					{
						name[q]=sts[q].name;
					}	
				}	
			}while(check==false);
			for(int q=0;q<i;q++)
			{
				dis=new DataOutputStream(sr[q].getOutputStream());
				dis.writeUTF("Enjoy!!!");
				dis.flush();
			}
			for(int q=0;q<i;q++)
			{
				stc[q]=new lion(sr,i,q,name);
				stc[q].start();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

class client_back extends Thread
{
	int i;
	client_back(int s)
	{
		i=s;
	}
	public void run()
	{
		try
		{
			ServerSocket st=new ServerSocket(8888);
			new clients(st,i).start();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

class myserver
{
	public static void main(String args[])throws IOException
	{
		try
		{
			System.out.println("Welcome To Our Messeger");
			System.out.println("1 - To Create a chatbox");
			System.out.println("2 - To Create a Group Chatbox");
			BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
			int s=Integer.parseInt(sc.readLine());
			if(s==1)
			{
				new client_back(2).start();
			}
			else if(s==2)
			{
				System.out.println("Enter Number of people : ");
				int t=Integer.parseInt(sc.readLine());
				new client_back(t).start();
			}
			else
			{
				System.out.println("invalid Input");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
