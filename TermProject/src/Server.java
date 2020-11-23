import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

	private static HashSet<String> names = new HashSet<String>();

	private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

	private static Hashtable<String, PrintWriter> n =new Hashtable<String, PrintWriter>();

 
	public static void main(String[] args) throws Exception{
		System.out.println("The chat server is running.");
		ExecutorService pool = Executors.newFixedThreadPool(50); // 50���� Ŭ���̾�Ʈ ���밡��
		ServerSocket listener = new ServerSocket(9997);
		try {
			while (true) {
				pool.execute(new Handler(listener.accept())); // �����带 ���� �������� Ŭ���̾�Ʈ ��������
			}
		} finally {
			listener.close();
		}
	}

	private static class Handler extends Thread{
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;


		public Handler(Socket socket) {
			this.socket = socket;
		}


		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();
					n.put(name,out); //�ؽ����̺� ���� �־���.
					writers.add(out);
					for (PrintWriter writer : writers) {
						writer.println("MESSAGE " + name + " has joined");
					} // Ŭ���̾�Ʈ�� ���ö����� ��ε�ĳ��Ʈ



					out.println("NAMEACCEPTED" + name);
					writers.add(out);

					if (name == null) {
						return;
					}
					synchronized (names) { // ����ȭ
						if (!names.contains(name)) {
							names.add(name);
							break;
						}
					}
				}

				String tempname="";
				PrintWriter whisper=null;


				while (true) {
					String input = in.readLine();
					if (input == null) {
						return;
					}

					if (input.toLowerCase().startsWith("/quit")) {
						return; // quit�ϸ� Ŭ���̾�Ʈ ��������.
					}
					else if(input.startsWith("/w")) // �ӼӸ���� /w ��� + �Ҹ�
					{
						int start = input.indexOf(" ") + 1;
						int end = input.indexOf(" ", start);
						tempname=input.substring(start,end);
						String msg = input.substring(end+1);


						if(n.containsKey(tempname)) // �޴»�� �ؽ�Ʈ����� ǥ��
							whisper=(PrintWriter) n.get(tempname);
						whisper.println("MESSAGE " + "[�ӼӸ�] " +  name + ": " + msg);

						if(n.containsKey(name)) //�����»�� �ؽ�Ʈ ����� ǥ��
							whisper=(PrintWriter) n.get(name);
						whisper.println("MESSAGE " + "[�ӼӸ�] " +  name + ": " + msg);
					}
					else
					{

						for (PrintWriter writer : writers) {
							writer.println("MESSAGE " + name + ": " + input);
						} // ��������� �ؽ�Ʈǥ��
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {

				writers.add(out);
				for (PrintWriter writer : writers) {
					writer.println("MESSAGE " + name + " has left");
				} // Ŭ���̾�Ʈ�� ���������� ��ε�ĳ��Ʈ ����
				if (name != null) {
					names.remove(name);
					n.remove(name,out);
				}
				if (out != null) {
					writers.remove(out);
				}

				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}



