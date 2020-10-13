import java.awt.Color;
import java.awt.event.*;
import java.awt.*;
import java.net.Socket;
import java.io.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
class output extends Thread
{
    Socket s;
    JTextArea rec;
    output(Socket st,JTextArea rec)
    {
        s=st;
        this.rec=rec;
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
                rec.append(str);

            }while(!(str.equals("stop")));
            dis.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
 
public class JavaClient extends JFrame implements ActionListener, KeyListener, MouseListener {
 
    static String message="";
    static String userName="";
    static String port="";
    static String iPAddress = null;
    static Socket socket = null;
    static PrintWriter writer = null;
    static JTextArea msgRec = new JTextArea(100, 50);
    static JTextArea msgSend = new JTextArea(100, 50);
    JButton send = new JButton("Send");
    JScrollPane pane2, pane1;
 
    JMenuBar bar = new JMenuBar();
 
    JMenu messanger = new JMenu("Messenger");
    JMenuItem logOut = new JMenuItem("Log Out");
 
    JMenu help = new JMenu("Help");
    JMenuItem s_keys = new JMenuItem("Shortcut Keys");
    JMenuItem about = new JMenuItem("about");
 
    public JavaClient() {
        super(userName);
        setBounds(0, 0, 407, 495);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        JFrame.setDefaultLookAndFeelDecorated(true);  
        Image icon =Toolkit.getDefaultToolkit().getImage("C:\\Users\\admin\\Downloads\\send.png");
        setIconImage(icon);
        msgRec.setEditable(false);
        msgRec.setBackground(Color.BLACK);
        msgRec.setForeground(Color.WHITE);
        msgRec.setText("");
        msgRec.setWrapStyleWord(true);
        msgRec.setLineWrap(true);
        pane2 = new JScrollPane(msgRec);
        pane2.setBounds(0, 0, 385, 300);
        pane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane2);
        msgSend.setBackground(Color.LIGHT_GRAY);
        msgSend.setForeground(Color.BLACK);
        msgSend.setLineWrap(true);
        msgSend.setWrapStyleWord(true);
        msgSend.setText("Write Message here");
        msgSend.addKeyListener(this);
		msgSend.addMouseListener(this);
		addMouseListener(this);
        pane1 = new JScrollPane(msgSend);
        pane1.setBounds(0, 300, 385, 100);
        pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane1);
        ImageIcon img=new ImageIcon("D:\\send.png");  
        Image img2=img.getImage();
        Image newimg = img2.getScaledInstance( 30,20,  java.awt.Image.SCALE_SMOOTH ) ;  
        send=new JButton(new ImageIcon(newimg));
		send.setText("Send");
        send.setBounds(0, 400,385, 30);
        add(send);
        send.addActionListener(this);
        bar.add(messanger);
        messanger.add(logOut);
        logOut.addActionListener(this);
        bar.add(help);
        help.add(s_keys);
        s_keys.addActionListener(this);
        help.add(about);
        about.addActionListener(this);
        setJMenuBar(bar);
        if ((userName) != null) {
            setVisible(true);
        } else {
            System.exit(0);
        }
    }
 
    public static void main(String[] args) throws Exception {
        userName = JOptionPane.showInputDialog("User Name (Client)");
        iPAddress = JOptionPane.showInputDialog("Enter Server IpAddress");
         port = JOptionPane.showInputDialog("Enter Port Number");
        (new Thread(new Runnable() {
            public void run() {
                new JavaClient();
 
            }
        })).start();
 
        socket = new Socket(iPAddress,Integer.parseInt(port));
        msgRec.setText("Connected!"); 
        new output(socket,msgRec).start();

    }
 
    public void actionPerformed(ActionEvent e) {
        Object scr = e.getSource();
 
        if (scr == send) {
            sendMessage();
			msgSend.setText("");
        } else if (scr == logOut) {
 
            System.exit(0);
 
        } else if (scr == s_keys) {
 
            JOptionPane.showMessageDialog(this,
                    "(shift+Enter) for new line while writing message"
                            + "\n(ctrl+x) for quit");
 
        } else if (scr == about) {
            JOptionPane.showMessageDialog(this,
                    "Messanger 1.0\ndeveloped By Shashank Kesarwani");
        }
    }
	 public void mousePressed(MouseEvent e) {
		 msgSend.setText("");
    
  }

  public void mouseReleased(MouseEvent e) {
    
  }

  public void mouseEntered(MouseEvent e) {
   
  }

  public void mouseExited(MouseEvent e) {
    
  }

  public void mouseClicked(MouseEvent e) {
 
  }
    public void keyTyped(KeyEvent e) {
    }
 
    public void keyReleased(KeyEvent e) {
    }
    private void sendMessage() 
    {
        try
        {
			String s[]=msgSend.getText().split("\n");
        String str=userName + " :" + s[s.length-1];
        msgRec.append("\nYou: " + s[s.length-1]);
        DataOutputStream dis=new DataOutputStream(socket.getOutputStream());
        dis.writeUTF(str);
        dis.flush();
        cursorUpdate();
        msgSend.setText("");
        msgSend.setCaretPosition(0);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public void keyPressed(KeyEvent e) {
 
        if ((e.getKeyCode() == KeyEvent.VK_ENTER) && e.isShiftDown()) {
            msgSend.append("\n");
 
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
			msgSend.setText("");
        }
 
        else if ((e.getKeyCode() == KeyEvent.VK_X) && e.isControlDown()) {
            System.exit(0);
        }
    }
    private static void cursorUpdate() {
        DefaultCaret caret = (DefaultCaret) msgRec.getCaret();
        caret.setDot(msgRec.getDocument().getLength());
        DefaultCaret caret2 = (DefaultCaret) msgSend.getCaret();
        caret2.setDot(msgSend.getDocument().getLength());
    }
}