int led = 13;
int buzz = 11;
int recv = 0;

void setup() {                
  pinMode(led, OUTPUT);  
  pinMode(buzz, OUTPUT);  
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) {  
    recv = Serial.read();
    if (recv == 121) {
      digitalWrite(led, HIGH);
      digitalWrite(buzz, HIGH);
    } else {
      digitalWrite(led, LOW);
      digitalWrite(buzz, LOW);
    }
  }
}
