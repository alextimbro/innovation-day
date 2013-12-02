int recv = 0;
int ledPin = 13;
int speakerOut = 11;              
byte names[] = {'c', 'd', 'e', 'f', 'g', 'a', 'b', 'C'};  
int tones[] = {1915, 1700, 1519, 1432, 1275, 1136, 1014, 956};
byte melody[] = "2d2a1f2c2d2a2d2c2f2d2a2c2d2a1f2c2d2a2a2g2p8p8p8p";
int count = 0;
int count2 = 0;
int count3 = 0;
int MAX_COUNT = 24;
int statePin = LOW;

void setup() {                
  pinMode(ledPin, OUTPUT); 
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) {  
    recv = Serial.read();
    if (recv == 121) {    
      digitalWrite(ledPin, HIGH);
      analogWrite(speakerOut, 0);    
      for (count = 0; count < MAX_COUNT; count++) {
        statePin = !statePin;
        digitalWrite(ledPin, statePin);
        for (count3 = 0; count3 <= (melody[count*2] - 48) * 30; count3++) {
          for (count2=0;count2<8;count2++) {
            if (names[count2] == melody[count*2 + 1]) {      
              analogWrite(speakerOut,500);
              delayMicroseconds(tones[count2]);
              analogWrite(speakerOut, 0);
              delayMicroseconds(tones[count2]);
            }
            if (melody[count*2 + 1] == 'p') {
              // make a pause of a certain size
              analogWrite(speakerOut, 0);
              delayMicroseconds(500);
            }
          }
        }
      }
    } else {
      digitalWrite(ledPin, LOW);
    }
  }
}
