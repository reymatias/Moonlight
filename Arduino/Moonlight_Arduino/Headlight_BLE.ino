/* 
===============================================================================
                                 _ _           
                        /\  /\___| (_) ___  ___ 
                       / /_/ / _ \ | |/ _ \/ __|
                      / __  /  __/ | | (_) \__ \
                      \/ /_/ \___|_|_|\___/|___/
          
                       HEADLIGHT CONTROL VIA BLE
                                V1.0
          
Description: Controls the headlight via BLE (Helios App or LightBlue).
Author: Seena Zandipour
Email: seena@ridehelios.com
Website: http://www.RideHelios.com
                          
Last updated: 3/9/2015

================================================================================
*/
#include <SoftwareSerial.h>
#include <BLEShield.h>

// PINS:
int led = A2;               // Assign pin for headlight
int led2 = 2;               // Assign pin for headlight (LOW mode)

// Setup BLE Shield
BLEShield bluetoothy;
// BLE Connected or not...
int conn = 0;
// Variable to store buffer
String message = "";


/******************************************************************************
 * Setup
 ******************************************************************************/
void setup() {
  //Initialize the light switch pin 
  pinMode(led, OUTPUT);
  
  //Initialize serial and wait for port to open:
  Serial.begin(9600);

  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
}

/******************************************************************************
 * Loop
 ******************************************************************************/
void loop() {
  //***********************************************
  //       BLE LISTENING SECTION (BLE READ)
  //***********************************************
  //Add incoming data to buffer
  if (bluetoothy.available()>0){
    // Create a buffer to hold the received data
    char buffer[64];
    // Read the data
    bluetoothy.readChars(buffer);
    message = buffer;
  }
  
  //Print out buffer when data transfer is complete
  else{
    if(message != "" && message != " " && message != 0)
    {
      Serial.print(message);
      message.toLowerCase();
    
        if((message == "on"))
        {
          // turn the LED on
           analogWrite(led, 255);
           analogWrite(led2, 255); 
        }
        // TURN HEADLIGHT OFF
        else if((message == "off"))
        {
          // turn the LED off
          analogWrite(led, 0);
          analogWrite(led2, 0);
        } 
    }
  } 
}
