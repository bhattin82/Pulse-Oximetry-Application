# Pulse-Oximetry-Application

This project develops a wearable band that monitors the vitals of children so that parents can be at a peace of mind regarding their child's safety. The band uses a microcontroller (esp32) and the MAX30102 sensor to measure the heart rate and blood oxygen level of the child. This vital information is communicated to a smartphone application via bluetooth in real time. This repository consists of the Android application code.

The application layout consists of a login page so that parents can protect their child's privacy. Once the correct credentials (Username: Stacy123, Password: health) are entered, the mode selection page pops up. It displays the band battery, and it provides the option to choose either the heart rate or blood oxygen mode. When the relevant mode is selected, it displays the heart rate/blood oxygen level data in real time along with a safety threshold. If the heart rate/blood oxygen level data is beyond the safety thresholds, a green background transitions to a red background indicating a health concern. Furthermore, the application has the feature where if the child is in distress, he/she can long press a panic button on the band to notify the parents that he/she is in pain and immediate action is required. In such a case, the application redirects to a red emergency page for the panic button.

The data sent by the server (esp32 microcontroller) to the client (Android Application) is in the following format: HeartRate=67,BloodOxygen=95,BatteryPercent=92,Panic=0

This can be achieved by the following lines of code on the Arduino IDE: 
SerialBT.printf("HeartRate=%d,BloodOxygen=%d,BatteryPercent=%d,Panic=%d", heartRate, bloodOxygen, battery, panicButton);
SerialBT.printf("\n");

How was the bluetooth connection established and the data read in real time?

There are three threads in the application: main (UI) thread, Connect Thread (establishes bluetooth connection), Connected Thread (bluetooth data transfer). The main thread is responsible for handling activites related to the User Interface. The application scans for all bluetooth devices available for pairing, and then selects one among them. In my case, I knew the device I wanted to connect to; therefore, I simply hardcoded its unique address. Next, I started the Connect Thread that takes care of initiating a bluetooth socket for communication. Once the bluetooth connection was established, the Connnected Thread began its execution. This thread reads an input stream that is a sequence of characters sent by the server (esp32), and then invokes the handler each time the data is updated. The handler runs on the main thread. The data is filtered from the list to extract the vital information, and it is passed to a broadcast receiver through an intent. The broadcast receiver listens for the data related to the intent, and then further performs actions related to the application scope. 

Lastly, this project uses Android Studio as its IDE. The icons used in the app were taken from icons8. Here is a link to the Activity Diagram for this subsystem: [ActivityDiagram.pdf](https://github.com/bhattin82/Pulse-Oximetry-Application/files/9667815/ActivityDiagram.pdf)

Note: This is not a responsive application. I only tested it on a Galaxy S10e.

The following is the block diagram for this subsystem:

![BlockDiagram](https://user-images.githubusercontent.com/70234008/192476162-cf210c5d-4390-44b8-95a5-a060190501e1.jpeg)

The following is the application layout:

<img src="https://user-images.githubusercontent.com/70234008/204683487-5f05083f-40a4-4bbd-829c-01c1ca61031d.png" width="700" height="690">




