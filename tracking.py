import cv2
import numpy as np
import face_recognition
from pathlib import Path
import pickle
import os

import base64

from imutils.video import FPS
import imutils
import psutil

video_capture = cv2.VideoCapture(0)


font = cv2.FONT_HERSHEY_DUPLEX


def load_weights(encoding_file):
    if Path(encoding_file).is_file():
        f = open(encoding_file, "rb")
        known_face_names = pickle.load(f)
        known_face_encodings = pickle.load(f)
        f.close()
        return os.stat(encoding_file).st_mtime, known_face_names, known_face_encodings
    else:
        print("No encoding file!")


def face_tracking():

    refresh = 0
    fps = FPS().start()
    # capture frames from the camera
    while True:
        # Grab a single frame of video
        ret, frame = video_capture.read()
        (H, W) = frame.shape[:2]

        # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
        rgb_frame = frame[:, :, ::-1]

        encoding_file = "/home/root1/Desktop/EdgeCV/edgeCV/known_face_encodings.p"
        cached_stamp, known_face_names, known_face_encodings = load_weights(encoding_file)

        face_locations = face_recognition.face_locations(rgb_frame)
        face_encodings = face_recognition.face_encodings(rgb_frame, face_locations)

        # Loop through each face found in the frame of video
        for (top, right, bottom, left), face_encoding in zip(face_locations, face_encodings):
            # See if the face is a match for the known face(s)
            matches = face_recognition.compare_faces(known_face_encodings, face_encoding, 0.5)
            name = "Unknown"
            if True in matches:
                if matches.count(True) > 1:
                    first_match_index = np.argmin(face_recognition.face_distance(known_face_encodings, face_encoding))
                else:
                    first_match_index = matches.index(True)
                name = known_face_names[first_match_index]

            # Draw a box around the face
            cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)

            # Draw a label with a name below the face
            cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
            font = cv2.FONT_HERSHEY_DUPLEX
            cv2.putText(frame, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

        ## info to be displayed in the frame
        fps.update()
        fps.stop()
        info = [
            ("FPS", "{:.2f}".format(fps.fps()))
        ]

        ## loop over the info tuples and draw them on our frame
        for (i, (k, v)) in enumerate(info):
            text = "{}: {}".format(k, v)
            cv2.putText(frame, text, (10, H - ((i*20) + 20)), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 0, 255), 2)

        frame = cv2.imencode('.jpg', frame)[1].tobytes()

        # refresh += 1
        # if refresh == 160:
        #     video_capture.release()
        #     break

        ## get CPU percentage (average of all cores, but only one being used right now)
        print(psutil.cpu_percent(None,True))

	## get network stats 
        print(psutil.net_io_counters(True))


        # yield (b'--frame\r\n'
        #         b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')
        frame_serialize = base64.b64encode(frame).decode("utf-8")
        print (frame_serialize)

face_tracking()
