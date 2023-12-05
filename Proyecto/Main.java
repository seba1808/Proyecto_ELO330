import java.io.*;
import java.net.*;
import java.util.Random;

public class Main{

    public static void main(String[] args) throws IOException {

        System.out.println("Servidor iniciado en el puerto 47203");
        Thread threadUDP = new Thread(() -> {
            try {
            DatagramSocket datagramSocket = new DatagramSocket(47204);
            while(true) {

                    byte[] buf = new byte[1000];

                    DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                    //System.out.println("Antes de recivir");
                    if(LastTemp.isButton_pulse() && LastTemp.ArduinoDataIsCollected){
                        String outdata = "LedOn";
                        byte[] outBuff = outdata.getBytes();
                        System.out.println("Boton pulsado");
                        datagramSocket.send(new DatagramPacket(outBuff,outBuff.length,LastTemp.ArduinoAddr, LastTemp.ArduinoPort));
                        LastTemp.setButton_pulse(false);
                    }else{
                        datagramSocket.receive(datagramPacket);

                        if(!LastTemp.ArduinoDataIsCollected){
                            LastTemp.ArduinoAddr = datagramPacket.getAddress();
                            LastTemp.ArduinoPort = datagramPacket.getPort();
                            LastTemp.ArduinoDataIsCollected = true;
                        }
                        String data = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                        LastTemp.setLast_temp(Integer.parseInt(data));
                    }


                }
            }catch (IOException e) {

            }

        });

        threadUDP.start();
        try {
            ServerSocket serverSocket = new ServerSocket(47203);
            while (true) {
                Socket socket = serverSocket.accept();
                Runnable runnable = new SocketTcpThread(socket);
                Thread thread2 = new Thread(runnable);
                thread2.start();
            }
        }catch (IOException e){
            System.out.println("atao tcp");
        }



    }
    public static class LastTemp{
        public static float getLast_temp() {
            return last_temp;
        }

        public static void setLast_temp(float last_temp) {
            LastTemp.last_temp = last_temp;
        }

        static float last_temp = 0;
        public static InetAddress ArduinoAddr;
        public static int ArduinoPort;
        public static boolean ArduinoDataIsCollected;

        public static boolean isButton_pulse() {
            return button_pulse;
        }

        public static void setButton_pulse(boolean button_pulse) {
            LastTemp.button_pulse = button_pulse;
        }

        static boolean button_pulse = false;
    }
    public static class SocketTcpThread implements Runnable{
        Socket socket;

        public SocketTcpThread(Socket socket) {
            this.socket = socket;
        }
        private static String getContentType(String filename) {
            if (filename.endsWith(".html")) {
                return "text/html";
            } else if (filename.endsWith(".js")) {
                return "application/javascript";
            } else {
                return "text/plain";
            }
        }
        @Override
        public void run() {
            try(
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                String requestLine = br.readLine(); // Lee la primera línea para identificar la solicitud
                if (requestLine != null && !requestLine.isEmpty()) {

                    String[] requestParts = requestLine.split(" ");
                    if (requestParts.length >= 2) {
                        String path = requestParts[1]; // Obtiene la ruta de la solicitud
                        if(path.equals("/Dinamico")){
                            out.println("HTTP/1.1 200 OK");
                            out.println("Content-Type: ");
                            out.println();
                            out.println(LastTemp.getLast_temp());
                            out.flush();
                        }else if(path.equals("/Boton")){
                            LastTemp.setButton_pulse(true);

                        }else {
                            // Decide qué archivo servir basado en la ruta
                            String filename = path.equals("/") ? "./index.html" : "./" + path.substring(1);

                            // Verifica si el archivo existe
                            File file = new File(filename);
                            if (file.exists() && !file.isDirectory()) {
                                System.out.println("FileSended");
                                sendResponse(out, file);
                            } else {
                                // Envía una respuesta 404 si el archivo no existe
                                out.println("HTTP/1.1 404 Not Found");
                                out.println();
                                out.flush();
                            }
                        }
                    }
                }
            }catch (IOException e){

            }
        }

        private static void sendResponse(PrintWriter out, File file) throws IOException {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + getContentType(file.getName()));
            out.println();

            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
            }

            out.flush();
            fileReader.close();
        }
    }
}

