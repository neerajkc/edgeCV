import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.*;

public class VideoFile {

	private static int absoluteFaceSize;
	private static int frameTimes = 0;  
	private static short frames = 0; 

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//Create new MAT object
		Mat frame = new Mat();
		CascadeClassifier faceDetector = new CascadeClassifier();
		faceDetector.load("/home/root1/Desktop/edgeCV/haarcascade_frontalface_default.xml");
		//Create new VideoCapture object
		VideoCapture videoFile = new VideoCapture("/home/root1/Desktop/Test/test_video.webm");
		//Create new JFrame object
		JFrame jframe = new JFrame("Face Detection");
		//Inform jframe what to do in the event that you close the program
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Create a new JLabel object vidpanel
		JLabel vidPanel = new JLabel();
		//assign vidPanel to jframe
		jframe.setContentPane(vidPanel);
		//set frame size
		jframe.setSize(2000, 4000);
		//make jframe visible
		jframe.setVisible(true);
		while (true) {
			//If next video frame is available
			int startTime = (int) System.currentTimeMillis();
			if (videoFile.read(frame)) {

				MatOfRect faces = new MatOfRect();
				Mat grayFrame = new Mat();
				// convert the frame in gray scale
				Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
				// equalize the frame histogram to improve the result
				Imgproc.equalizeHist(grayFrame, grayFrame);
				// compute minimum face size (20% of the frame height, in our case)
				if (absoluteFaceSize == 0){
					int height = grayFrame.rows();
					if (Math.round(height * 0.2f) > 0){
						absoluteFaceSize = Math.round(height * 0.2f);
					}
				}
				// detect faces
				faceDetector.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
						new Size(absoluteFaceSize, absoluteFaceSize), new Size());
				// each rectangle in faces is a face: draw them
				Rect[] facesArray = faces.toArray();
				for (int i = 0; i < facesArray.length; i++){
					Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);
				}
				//Create new image icon object and convert Mat to Buffered Image
				ImageIcon image = new ImageIcon(Mat2BufferedImage(frame));
				//Update the image in the vidPanel
				vidPanel.setIcon(image);
				//Update the vidPanel in the JFrame
				vidPanel.repaint();

				int endTime = (int) System.currentTimeMillis();
				frameTimes = frameTimes + endTime - startTime; 
				++frames;
				//if the difference is greater than 1 second (or 1000ms) post the results  
				if(frameTimes >= 1000)  
				{  
					//post results 
					System.out.println("FPS :"+ frames);    
					frames = 0;  
					frameTimes = 0;  

				}

			}
		}

	}

	public static BufferedImage Mat2BufferedImage(Mat m) {
		//Method converts a Mat to a Buffered Image
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( m.channels() > 1 ) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels()*m.cols()*m.rows();
		byte [] b = new byte[bufferSize];
		m.get(0,0,b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);  
		return image;
	}

}
