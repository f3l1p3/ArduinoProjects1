import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.TooManyListenersException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


enum Commands{red, green, blue, 
			  ALEFT, ACENTER, ARIGHT, 
			  BLEFT, BCENTER, BRIGHT, 
			  CLEFT, CCENTER, CRIGHT};

public class SerialControl extends HttpServlet {
	
	private static boolean connected = false;
	 
	/** serialVersionUID	 */
	private static final long serialVersionUID = 1358068382442684367L;
	
	/** Seial port */
	private static SerialPort serialPort;
	
	/** Streams */
	private static OutputStream   serialOut;	
	
	/** Servlet Init */
	public void init() throws ServletException{
		  try{
			  openPort("COM15", 300000);
		  }catch(Exception e){
			  e.printStackTrace();
		  }		  
	}
	
	/**
	 * GET not implemented
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
	      response.setContentType("text/html");
	      PrintWriter out = response.getWriter();
	      out.println("<h1> GET No implemented</h1>");
	}
	
	/**
	 * POST service. Just take request and send to serial port
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		  String cmdStr = request.getParameter("cmd");
		  System.out.println("  ########### RQS: "+cmdStr);
		  Commands cmd = Commands.valueOf(cmdStr);
		  sendCmd(cmd);

		  String nextJSP = "/index.jsp";
		  RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
		  dispatcher.forward(request,response);
	}	  
	
	
	/** Serlvet ends */
	public void destroy(){
	      if(connected){
	    	  closePort();
	      }
	}
	  
	private void sendCmd(Commands cmd) {
		if (connected) {
			synchronized (serialPort) {
				if (connected) {
					try {
						serialOut.write(cmd.toString().getBytes());
						serialOut.flush();
						System.out.println("  ########### SND: "+cmd.toString());
					} catch (IOException e) {
						e.printStackTrace();
						closePort();
					}
				}
			}
		}
	}
	  
	private void openPort(String portName, Integer speed) throws 
											  NoSuchPortException, 
											  PortInUseException, 
											  UnsupportedCommOperationException, 
											  IOException, 
											  TooManyListenersException{
			if(!connected){
				CommPortIdentifier port = CommPortIdentifier.getPortIdentifier(portName); 
		        CommPort commPort = port.open(this.getClass().getName(),2000);
		        serialPort = (SerialPort) commPort;
		        serialPort.setSerialPortParams(speed, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		        serialOut=serialPort.getOutputStream();
				connected = true;
				System.out.println("  ########### OPEN:");
			}
	}
	
	private void closePort(){
			if(connected){
				serialPort.close();
				connected = false;
				System.out.println("  ########### CLOSE:");
			}
	}
}
