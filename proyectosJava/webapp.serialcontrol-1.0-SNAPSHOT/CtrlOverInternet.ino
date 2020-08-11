#include <Servo.h>

#define LED_RED        2
#define LED_GREEN      3
#define LED_BLUE       4
#define SERVO_A_PIN    9
#define SERVO_B_PIN   10
#define SERVO_C_PIN   11

unsigned char LR=0;
unsigned char LG=0;
unsigned char LB=0;
Servo servoA;
Servo servoB;
Servo servoC;

void setup() {
    Serial.begin(300000);
    
    pinMode(LED_RED,   OUTPUT);
    pinMode(LED_GREEN, OUTPUT);
    pinMode(LED_BLUE,  OUTPUT);
    
    digitalWrite(LED_RED,   LOW);
    digitalWrite(LED_GREEN, LOW);
    digitalWrite(LED_BLUE,  LOW);

    servoA.attach(SERVO_A_PIN);
    servoA.write(90);
    servoB.attach(SERVO_B_PIN);
    servoB.write(90);
    servoC.attach(SERVO_C_PIN);
    servoC.write(90);

}


void loop(){


}

void serialEvent() {
    int ind=0;
    char buff[11];  
    if(Serial.available()){
        delay(10);
        while(Serial.available()){
            char c = (char)Serial.read();
            if(ind<10){
              buff[ind++] = c;
            }
        }
        buff[ind]=0;
        
        if      (strcmp(buff, "red")==0)  { LR = 1-LR; digitalWrite(LED_RED, LR);
        }else if(strcmp(buff, "green")==0){ LG = 1-LG; digitalWrite(LED_GREEN, LG);
        }else if(strcmp(buff, "blue")==0) { LB = 1-LB; digitalWrite(LED_BLUE, LB);
        }else if(strcmp(buff, "ALEFT")==0)  { servoA.write(0);
        }else if(strcmp(buff, "ACENTER")==0){ servoA.write(90);
        }else if(strcmp(buff, "ARIGHT")==0) { servoA.write(180);
        }else if(strcmp(buff, "BLEFT")==0)  { servoB.write(0);
        }else if(strcmp(buff, "BCENTER")==0){ servoB.write(90);
        }else if(strcmp(buff, "BRIGHT")==0) { servoB.write(180);
        }else if(strcmp(buff, "CLEFT")==0)  { servoC.write(0);
        }else if(strcmp(buff, "CCENTER")==0){ servoC.write(90);
        }else if(strcmp(buff, "CRIGHT")==0) { servoC.write(180);
        }
    }  
}


