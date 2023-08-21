

#include "HX711.h"
#include <FirebaseESP8266.h>



HX711 scale;

#define FIREBASE_HOST "fir-weight-94364-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "6Dj54We7jGKKkPuQrOGVhLBZWLUvyMpRGHU6j9B7"
#define WIFI_SSID "SAYAN"
#define WIFI_PASSWORD "sankha41"

const int LOADCELL_DOUT_PIN = D4;
const int LOADCELL_SCK_PIN = D5;


// Create an instance of the Firebase library
FirebaseData firebaseData;

FirebaseJson json;



void setup() {
  Serial.begin(57600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  Serial.println("Load Cell Interfacing with ESP8266");
  
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);

  scale.set_scale(7599.59);    // this value is obtained by calibrating the scale with known weights as in previous step
  scale.tare();				         // reset the scale to 0
}
void sensorUpdate(){
 
  float w = scale.get_units(10);
  w=w*100;

  // Check if any reads failed and exit early (to try again).
  if (isnan(w)) {
    Serial.println(F("Failed to read from hx711 sensor!"));
    return;
  }
Serial.print(F("Weight: "));
  Serial.print(w);

if (Firebase.setFloat(firebaseData, "/WeightCapacity/Weight", w))
  {
    Serial.println("PASSED");
    Serial.println("PATH: " + firebaseData.dataPath());
    Serial.println("TYPE: " + firebaseData.dataType());
    Serial.println("ETag: " + firebaseData.ETag());
    Serial.println("------------------------------------");
    Serial.println();
  }
  else
  {
    Serial.println("FAILED");
    Serial.println("REASON: " + firebaseData.errorReason());
    Serial.println("------------------------------------");
    Serial.println();
  }
}

void loop() {
  sensorUpdate();
   delay(100);
}


