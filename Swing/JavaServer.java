import java.awt.Color;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.border.*;
public class clients extends Thread
    {
        Socket sr[]=new Socket[100];
        lion stc[]=new lion[100];
        JTextArea rec;
        int num;
        clients(JTextArea rec,int num)
        {
            this.rec=rec;
            this.num=num;
        }
        public void run()
        {
            int x=0;
            try {
                    ServerSocket st=new ServerSocket(8888);
                    rec.append("\nListening on port 8888....\n");
                      while(x<num)
                    {   
                         sr[x]=st.accept();
                         rec.append("\n"+(x+1)+" Device Connected!!!");
                           x++;
                  }
                   rec.append("\nAll Device Connected!!!");
                   rec.append("\nConnection Established!!!");
                for(int q=0;q<num;q++)
                {
                    stc[q]=new lion(sr,num,q,rec);
                    stc[q].start();
                }
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
    class lion extends Thread
    {
    Socket s[]=new Socket[100];
    int i;
    int x;
    JTextArea rec;
    public lion(Socket st[],int b,int t,JTextArea rec)
    {
        s=st;
        i=b;
        x=t;
        this.rec=rec;
    }
    public void run()
    {
        try
        {   
                DataInputStream dis=new DataInputStream(s[x].getInputStream());
                String str="";
                DataOutputStream dip=null;
                do
                {
                    str=(String)dis.readUTF();
                    for(int t=0;t<i;t++)
                    {
                        if(t!=x)
                        {
                            dip=new DataOutputStream(s[t].getOutputStream());
                            dip.writeUTF("\n"+str);
                            dip.flush();
                        }
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
public class JavaServer extends JFrame implements ActionListener{
    static String message = "";
    static int i;
    static String members = "";
    static ServerSocket server = null;
    static Socket socket = null;
    static PrintWriter writer = null;
    static JTextArea msgRec = new JTextArea(200, 100);
    JScrollPane pane2;
    JMenuBar bar = new JMenuBar();
 
    JMenu messanger = new JMenu("Messenger");
    JMenuItem logOut = new JMenuItem("Log Out");
 
    JMenu help = new JMenu("Help");
    JMenuItem s_keys = new JMenuItem("Shortcut Keys");
    JMenuItem about = new JMenuItem("about");
 
    public JavaServer() {
        super("Java Server");
        JFrame.setDefaultLookAndFeelDecorated(true);  
        Image icon =Toolkit.getDefaultToolkit().getImage("C:\\Users\\admin\\Downloads\\send.png");
        setIconImage(icon);
        setBounds(0, 0, 407, 495);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
 
        msgRec.setEditable(false);
        msgRec.setBackground(Color.BLACK);
        msgRec.setForeground(Color.WHITE);

        msgRec.setText("");
 
        msgRec.setWrapStyleWord(true);
        msgRec.setLineWrap(true);
 
        pane2 = new JScrollPane(msgRec);
        pane2.setBounds(0, 0, 390, 460);
        pane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane2);
        bar.add(messanger);
        messanger.add(logOut);
        logOut.addActionListener(this);
 
        bar.add(help);
        help.add(s_keys);
        s_keys.addActionListener(this);
        help.add(about);
        about.addActionListener(this);
 
        setJMenuBar(bar);
 
            setVisible(true);
    }
    
 
    public static void main(String[] args) throws IOException 
    {
    
        members=JOptionPane.showInputDialog("Number Of Memebers : ");
        i=Integer.parseInt(members);
        (new Thread(new Runnable() {
            public void run() {
                new JavaServer();
            }
 
        })).start();
        new clients(msgRec,i).start();
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
 
        } catch (IOException e) {
            try {
                server.close();
                socket.close();
            } catch (IOException eee) {
            }
        }
    }
    public void actionPerformed(ActionEvent e) {
        Object scr = e.getSource();
 
        
         if (scr == logOut) {
 
            System.exit(0);
 
        } else if (scr == s_keys) {
 
            JOptionPane.showMessageDialog(this,
                    "(shift+Enter) for new line while writing message"
                            + "\n(ctrl+x) for quit");
 
        } else if (scr == about) {
            JOptionPane.showMessageDialog(this,
                    "Messanger 1.0\ndeveloped Shashank Kesarwani");
        }
    }
}