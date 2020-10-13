
import java.io.*;
import java.net.*;
class input extends Thread
{
	Socket s;
	input(Socket st)
	{
		s=st;
	}
	public void run()
	{
		try
		{
			DataOutputStream dis=new DataOutputStream(s.getOutputStream());
			String str="";
			do
			{
				BufferedReader sc=new BufferedReader(new InputStreamReader(System.in));
				str=sc.readLine();
				dis.writeUTF(str);
				dis.flush();
			}while(!(str.equals("stop")));
			dis.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
class output extends Thread
{
	Socket s;
	output(Socket st)
	{
		s=st;
	}
	public void run()
	{
		try
		{
			DataInputStream dis=new DataInputStream(s.getInputStream());
			String str="";
			do
			{
				str=(String)dis.readUTF();
				System.out.println(str);

			}while(!(str.equals("stop")));
			dis.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
class myclient
{
	public static void main(String args[])throws IOException
	{
		try
		{
			String host=args[0];
			int port=Integer.parseInt(args[1]);
			Socket st=new Socket(host,port);
			new input(st).start();
			new output(st).start();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}