 /*******************************************************************************
* Proyecto - Programación De Sistema                                   ELO 330
* Diego Almonacid                                            01 Diciembre 2023
* Sebastián Madariaga                                                        *
* Annette Ramirez                                                            *
*******************************************************************************/
//Archivo .ino para microcontrolador ESP32

#include <WiFi.h>
#include <ArduinoJson.h>
#include <WiFiClient.h>
#include <PubSubClient.h>
#include <WiFiUdp.h>
WiFiUDP udp;

#define ADC_VREF_mV    3300.0   // in millivolt
#define ADC_RESOLUTION 4096.0
#define PIN_LM35       34       // ESP32 pin GPIO36 (ADC0) connected to LM35
#define PIN_Pote       35

char packetBuffer[255];
const char* ssid = "HUAWEIP30";
const char* password = "elo330Proyecto";
unsigned int localPort = 9999;
bool bol = false;

const char* serverIP = "200.1.17.195"; // Dirección IP de Aragorn
const int serverPort = 47204 ;         // Puerto del servidor

WiFiClient client;

void setup() {
    // Iniciando velocidad del Puerto Serial
  Serial.begin(115200);
  delay(10);
  pinMode(2, OUTPUT);
  pinMode(PIN_LM35, INPUT); //Sensor de Temperatura
  pinMode(PIN_Pote, INPUT); //Boton
  
  // Conectando a la red WiFi
  Serial.println();
  Serial.print("Conectando a ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi conectado");
  udp.begin(localPort);
}

void loop() {
 int temp = analogRead(PIN_LM35);
 float milliVolt = temp * (ADC_VREF_mV / ADC_RESOLUTION);
 float tempC = milliVolt / 10;

 int adcVal = analogRead(PIN_Pote);
 adcVal = (adcVal/4095.0)*100;

 int packetSize = udp.parsePacket();
 if (packetSize){
   bol =!bol;
   Serial.print(" Received packet from : "); Serial.println(udp.remoteIP());
   int len = udp.read(packetBuffer, 255);
   Serial.print("Data :");
   Serial.println(packetBuffer);
  }
  if(bol){
    digitalWrite(2, HIGH);
  }
  else{
    digitalWrite(2, LOW);
  }
  
  delay(1000);
  //Serial.print("[Client Connected] "); Serial.println(WiFi.localIP());
  udp.beginPacket(serverIP, serverPort);
  char buf[30];
  sprintf(buf, "%d", adcVal);
  udp.print(buf);
  Serial.print("Eviando el dato:");
  Serial.println(buf);
  udp.endPacket();
 
}
