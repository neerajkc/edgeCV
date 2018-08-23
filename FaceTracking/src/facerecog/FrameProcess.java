package facerecog;
import java.io.*;

import sapphire.app.SapphireObject;
import sapphire.policy.mobility.explicitmigration.ExplicitMigrationPolicy;

public class FrameProcess implements SapphireObject<ExplicitMigrationPolicy> {
    transient OutputStream out2;
    public FrameProcess(){

        String cmd = "/home/root1/.virtualenvs/cv/bin/python3";
        String path = "/home/root1/eclipse-workspace/edgeCV-consolidation-latest/java_wrapper/src/";

        ProcessBuilder ps2 = new ProcessBuilder(cmd, path + "recognition.py");
        ps2.redirectErrorStream(true);
        Process pr2 = null;
        try {
            pr2 = ps2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        out2 = pr2.getOutputStream();
        // BufferedReader in2 = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
    }

    public void processFrame(String frame) throws IOException, InterruptedException {

        String line2="";
        //while ((line1 = in1.readLine()) != null) {

        out2.write((frame+"\n").getBytes()); //write to file done and ok to proceed
        out2.flush();
            
           // line2 = in2.readLine();
           // System.out.println("Receiving the output from the other process pr2 => " + line2);

        //out2.close();
        //in2.close();
        // pr2.waitFor();
    }
}