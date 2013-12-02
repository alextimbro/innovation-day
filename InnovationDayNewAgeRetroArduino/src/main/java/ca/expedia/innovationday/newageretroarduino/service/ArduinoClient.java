package ca.expedia.innovationday.newageretroarduino.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Enumeration;

public class ArduinoClient implements NastySound, SerialPortEventListener {
    SerialPort serialPort = null; 
    
    private static final String PORT_NAMES[] = { 
        "/dev/tty.usbmodem", // Mac OS X
        "/dev/usbdev", // Linux
        "/dev/tty", // Linux
        "/dev/serial", // Linux
        "COM4", // Windows
    };
    
    private String appName;
    private BufferedReader input;
    private OutputStream output;
    
    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port

    public boolean initialize() {
        try {
            CommPortIdentifier portId = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            System.out.println( "Trying to connect to:");
            while (portId == null && portEnum.hasMoreElements()) {
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                System.out.println( "   port" + currPortId.getName() );
                for (String portName : PORT_NAMES) {
                    if ( currPortId.getName().equals(portName) 
                      || currPortId.getName().startsWith(portName)) {

                        serialPort = (SerialPort)currPortId.open(appName, TIME_OUT);
                        portId = currPortId;
                        System.out.println( "Connected on port " + currPortId.getName() );
                        break;
                    }
                }
            }
        
            if (portId == null || serialPort == null) {
                System.out.println("Could not connect to Arduino!");
                return false;
            }
        
            serialPort.setSerialPortParams(DATA_RATE,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            try { Thread.sleep(2000); } catch (InterruptedException ie) {}
            
            return true;
        }
        catch ( Exception e ) { 
            e.printStackTrace();
        }
        return false;
    }
    
    private void sendData(String data) {
        try {
            System.out.println("Sending data: '" + data +"'");
            output = serialPort.getOutputStream();
            output.write( data.getBytes() );
        } 
        catch (Exception e) {
            System.err.println(e.toString());
            System.exit(0);
        }
    }

    public synchronized void close() {
        if ( serialPort != null ) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    public synchronized void serialEvent(SerialPortEvent oEvent) {
        try {
            switch (oEvent.getEventType() ) {
                case SerialPortEvent.DATA_AVAILABLE: 
                    if ( input == null ) {
                        input = new BufferedReader(
                            new InputStreamReader(
                                    serialPort.getInputStream()));
                    }
                    String inputLine = input.readLine();
                    System.out.println(inputLine);
                    break;

                default:
                    break;
            }
        } 
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public ArduinoClient() {
        appName = getClass().getName();
    }

	@Override
	public void on(long miliseconds, ArduinoSound sound) {
		on(sound);
        try { Thread.sleep(miliseconds); } catch (InterruptedException ie) {}
        off();
	}	
	
	@Override
	public void on(ArduinoSound sound) {
		if(initialize()) {
			switch (sound) {
				case SOUND_A: sendData("a"); break;
				case SOUND_B: sendData("r"); break;
				case SOUND_C: sendData("m"); break;
				case SOUND_D: sendData("s"); break;
				case SOUND_E: sendData("p"); break;
			}
		}
	}	
	
	@Override
	public void off() {
		sendData("n");
		close();
		try { Thread.sleep(2000); } catch (InterruptedException ie) {}   
	}
}

