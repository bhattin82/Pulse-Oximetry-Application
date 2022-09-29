# Pulse-Oximetry-Application

This project develops a wearable band that monitors the vitals of children so that parents can be at a peace of mind regarding their child's safety. The band uses a microcontroller (esp32), capacitive sensors, LED and a photodiode to measure the heart rate and blood oxygen level of the child. This vital information is communicated to a smartphone application via bluetooth. 

The application layout consists of a login page so that parents can protect their child's privacy. Once the correct credentials are entered, the mode selection page pops up. It provides the option to choose either the heart rate or blood oxygen mode. When the relevant mode is selected, it displays the heart rate/blood oxygen level data in real time along with a safety threshold. If the heart rate/blood oxygen level data is beyond the safety thresholds, a green background transitions to a red background indicating a health concern. Furthermore, the application has the feature where if the child is in distress, he/she can long press a panic button on the band to notify the parents that he/she is in pain and immediate action is required. In such a case, the application redirects to a red emergency page for the panic button.

Lastly, this project uses Android Studio as its IDE. Here is a link to the Activity Diagram for this subsystem: [ActivityDiagram.pdf](https://github.com/bhattin82/Pulse-Oximetry-Application/files/9667815/ActivityDiagram.pdf)

The following is the block diagram for this subsystem:

![BlockDiagram](https://user-images.githubusercontent.com/70234008/192476162-cf210c5d-4390-44b8-95a5-a060190501e1.jpeg)





