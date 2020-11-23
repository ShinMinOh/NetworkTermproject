import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame implements ActionListener {
	
	// ä�ù� GUI ����
	private JPanel contentPane = new JPanel(); // �������� ������ ������ ����
	private JButton Invite = new JButton("ģ���ʴ�"); // ģ���ʴ� ��ư
	private JButton Exit = new JButton("������");		//������ ��ư
	private JTextArea ChatView = new JTextArea(); // ä�ù� �κ�
	private JTextArea Chatting = new JTextArea(); // ä�� �Էºκ�
	private JLabel ChatName = new JLabel("ä�ù� �̸�"); // ä�ù� �̸�
	private JFrame frame = new JFrame(); //������
	
//	private Socket socket;
//	private String ip="";
//	private int port;
//	private InputStream is;
//	private OutputStream os;
//	private DataInputStream dis;
//	private DataOutputStream dos;
//	
	
// 	  �׿ܿ� ����
	String serverAddress;
    Scanner in;
    PrintWriter out;
    String names[] = new String[100];
    int i=0;

	// ������ �޼ҵ�
	Client(){
		init();		// ä�ù� ȭ�� ���� �޼ҵ�
		start();	// ������ ��ư  
	}
	
	private void start() {
		Invite.addActionListener(this); // ģ���ʴ��ư ������
		Exit.addActionListener(this);   // ä�ù� ������ ��ư ������
	}
	
	// ä�ù� ȭ�� ���� �޼ҵ�
	private void init() {

				setTitle("ä�ù� �̸�");

				
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setBounds(100, 100, 452, 532);
				
				contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
				setContentPane(contentPane);
				contentPane.setLayout(null);
				
				
				//ģ���ʴ� GUI
				Invite.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					}
				});
				Invite.setBounds(217, 0, 109, 53);
				contentPane.add(Invite);
				
				
				//ä�ù� ������ GUI
				Exit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				Exit.setBounds(325, 0, 109, 53);
				contentPane.add(Exit);
				
				
				//ä��â GUI
				ChatView.setBounds(0, 53, 434, 374);
				contentPane.add(ChatView);
				
				
				//ä�� �Է� GUI
				Chatting.setBounds(0, 432, 434, 53);
				contentPane.add(Chatting);
				
				
				//ä�ù� �̸� GUI
				ChatName.setHorizontalAlignment(SwingConstants.CENTER);
				ChatName.setBounds(0, 0, 221, 53);
				contentPane.add(ChatName);
				
				this.setVisible(true); // ȭ�鿡 ���̰��ϸ� true
	}

//	private void Network() {
//		try {
//			socket = new Socket(ip,port);
//			if(socket!=null) { // ������ ���������� ����� ���
//				Connection();
//			}
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
//	private void Connection() { // ������ �����ϴ� �κ�
//		try {
//		is = socket.getInputStream();
//		dis = new DataInputStream(is);
//		
//		os = socket.getOutputStream();
//		dos = new DataOutputStream(os);
//		}
//		catch(IOException e) {
//			
//		}
//	}
	
	// ������ ��ư Ȯ��
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Invite) {
			System.out.println("ģ���ʴ��ư Ŭ��");
		}
		else if(e.getSource()==Exit) {
			System.out.println("ä�ù� ������ ��ư Ŭ��");
		}
	}
	
	// ������ �޼����� ������ �κ�
//	private void send_message(String str) {
//		try {
//			dos.writeUTF(str);	//���ڿ��� �޾Ƽ� ����
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	
	
	

	
	private void run(int port) throws IOException {
        try {
           Socket socket = new Socket(serverAddress, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            
           
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    out.println(getName());
                } else if (line.startsWith("NAMEACCEPTED")) {
                    names[i] = line.substring(12);
                    frame.setTitle("Chatter - " + line.substring(12));
                    ChatView.setEditable(true);
                    i++;
  
                    
                } else if (line.startsWith("MESSAGE")) {
                    Chatting.append(line.substring(8) + "\n");
                }
            }  
        }
       finally {
    	   frame.setVisible(false);
    	   frame.dispose();
       }
       
    }
	
	public static void main(String[] args) {
		new Client();
		String st=null; // �������ּ�
	    int port=0; // ��Ʈ��ȣ
	    
	    try {
	    	File file = new File("C:\\work");
	        //�� ��ǻ�Ϳ� ������ִ� serverinfo.dat �б�  

	        Scanner input = new Scanner(file);
	    	 while(input.hasNext())
	         {
	            st=input.nextLine();
	            port=input.nextInt();
	         }
	    }
	    catch(FileNotFoundException e) // ���� ������ ã������������ �⺻������ ���� 
	    {
	       st = "127.0.0.1";
	       port = 9999;
	       System.out.println(e.getMessage());
	    }
		
	}
}
