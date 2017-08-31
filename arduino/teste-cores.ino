//Pinos de conexao do modulo
const int s0 = 9;
const int s1 = 8;
const int s2 = 11;
const int s3 = 10;
const int out = 12;

//Variaveis cores
int red = 0;
int green = 0;
int blue = 0;

void setup()
{
    pinMode(s0, OUTPUT);
    pinMode(s1, OUTPUT);
    pinMode(s2, OUTPUT);
    pinMode(s3, OUTPUT);
    pinMode(out, INPUT);
    Serial.begin(9600);
    digitalWrite(s0, HIGH);
    digitalWrite(s1, LOW);
}

void loop()
{
    //Detecta a cor
    color();
    //Mostra valores no serial monitor
    Serial.print("Vermelho: ");
    Serial.print(red, DEC);
    Serial.print(" Verde : ");
    Serial.print(green, DEC);
    Serial.print(" Azul : ");
    Serial.print(blue, DEC);
    Serial.println();
  
    if (red > 400 && green > 400 && blue > 400) {
        Serial.println("Preto");
    }

    else if (red < 200 && green < 150 && blue < 100) {
        Serial.println("Azul claro");
    }

    else if (red < 200 && green < 200 && blue < 200) {
        Serial.println("Branco");
    }
    
    else if (red < blue && red < green && red < 200 && green > 250 && blue > 150)
    {
      Serial.println("Vermelho");
    }
  
    //Verifica se a cor azul foi detectada
    else if (blue < red && blue < green && red > 400)
    {
      Serial.println("Azul");
    }
  
    Serial.println();
    delay(500);
}

void color()
{
    //Rotina que le o valor das cores
    digitalWrite(s2, LOW);
    digitalWrite(s3, LOW);
    //count OUT, pRed, RED
    red = pulseIn(out, digitalRead(out) == HIGH ? LOW : HIGH);
    digitalWrite(s3, HIGH);
    //count OUT, pBLUE, BLUE
    blue = pulseIn(out, digitalRead(out) == HIGH ? LOW : HIGH);
    digitalWrite(s2, HIGH);
    //count OUT, pGreen, GREEN
    green = pulseIn(out, digitalRead(out) == HIGH ? LOW : HIGH);
}

