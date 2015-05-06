#include <SoftwareSerial.h>
#include <BLEShield.h>

// Pins:
int led = A2;  // Assign pin for headlight
int led2 = 2;  // Assign pin for headlight (LOW mode)

BLEShield bluetoothy;  // Setup BLE Shield
int conn = 0;          // BLE connection
String message = "";   // Variable to store buffer
boolean flag = true;   // Stolen light pattern on by default

void setup() {
  pinMode(led, OUTPUT);  // Initialize the light switch pin 
  Serial.begin(9600);    // Initialize serial and wait for port to open
}

void loop() {
  //Add incoming data to buffer
  if (bluetoothy.available()>0){
    char buffer[64];                // Create a buffer to hold the received data
    bluetoothy.readChars(buffer);   // Read the data
    message = buffer;
  } 
  //Print out buffer when data transfer is complete
  else{
    if(message != "" && message != " " && message != 0)
    {
      Serial.println(message);
      message.toLowerCase();
      
      if(message == "on") {
        flag = false;
      } else if(message == "off") {
        flag = true;
      }  
    }
    
    if(flag == true) {
      stolenPattern();
    } else if(flag == false) {
      defaultPattern();
    }
  } 
}

void defaultPattern() {
  analogWrite(led, 255);
  analogWrite(led2, 255); 
  delay(100);
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(100);
}

void stolenPattern() {
  analogWrite(led, 255);
  analogWrite(led2, 255); 
  delay(100);
  analogWrite(led, 0);
  analogWrite(led2, 0); 
  delay(100);
  analogWrite(led, 255);
  analogWrite(led2, 255);
  delay(100); 
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(100);
  analogWrite(led, 255);
  analogWrite(led2, 255);
  delay(100); 
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(100);
  
  analogWrite(led, 255);
  analogWrite(led2, 255);  
  delay(1000);
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(100);
  analogWrite(led, 255);
  analogWrite(led2, 255);  
  delay(1000);
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(100);
  analogWrite(led, 255);
  analogWrite(led2, 255);  
  delay(1000);
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(100);
  
  analogWrite(led, 255);
  analogWrite(led2, 255); 
  delay(100);
  analogWrite(led, 0);
  analogWrite(led2, 0); 
  delay(100);
  analogWrite(led, 255);
  analogWrite(led2, 255);
  delay(100); 
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(100);
  analogWrite(led, 255);
  analogWrite(led2, 255);
  delay(100); 
  analogWrite(led, 0);
  analogWrite(led2, 0);
  delay(500);
}
