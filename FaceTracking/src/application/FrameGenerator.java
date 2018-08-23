package application;
import facerecog.FrameProcess;
import sapphire.kernel.server.KernelServer;
import sapphire.kernel.server.KernelServerImpl;
import sapphire.oms.OMSServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FrameGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {

        String cmd = "/home/root1/.virtualenvs/cv/bin/python3";
        String path = "/home/root1/eclipse-workspace/edgeCV-consolidation-latest/java_wrapper/src/";

        ProcessBuilder ps1 = new ProcessBuilder(cmd, path + "frame_generator.py");
        ps1.redirectErrorStream(true);
        Process pr1 = ps1.start();

        BufferedReader in1 = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
        String line1="";

        Registry registry;

        try {

               // "192.168.42.140", "22346" - OMS
            registry = LocateRegistry.getRegistry("100.64.40.56", Integer.parseInt("22346"));
            OMSServer server = (OMSServer) registry.lookup("SapphireOMS");

//                "10.0.2.15", "22345" - Kernel server
            KernelServer nodeServer = new KernelServerImpl(new InetSocketAddress("10.0.2.15", Integer.parseInt("22345")), new InetSocketAddress("100.64.40.56", Integer.parseInt("22346")));
            FrameProcess frmProc = (FrameProcess)server.getAppEntryPoint();

                /* Generate frame */
            while ((line1 = in1.readLine()) != null) {
                frmProc.processFrame(line1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
        
//        out2.close();
//        in1.close();
//        pr1.waitFor();
}
