/************************************************
*   UNIVERSIDADE FEDERAL RURAL DE PERNAMBUCO    *
*   BACHARELADO EM CIÊNCIAS DA COMPUTAÇÃO        *
*   CURSO DE ARDUINO SECOMP                     *
*   BRUNO HENRIQUE PEREIRA MARQUES              *
*   RECIFE, 24/08/2016                          *
************************************************/
 
#define rodaDirPWM 5
#define rodaDirFrente 4
 
#define rodaEsqPWM 6
#define rodaEsqFrente 7
 
#define sensorDir A15
#define sensorEsq A14

int leituraBitsDir;
int leituraBitsEsq;
 
// velocidade nas curvas
#define POTL 125
 
// raio nas curvas
#define POTS 10
 
// aceleracao
#define POTF 155
 
// Valor da LINHA
#define LINHA 700
 
char movimento = 'f';
 
void acelerar(int aceleracaoRodaDir, int aceleracaoRodaEsq, int sentido){
    analogWrite(rodaDirPWM, aceleracaoRodaDir);
    analogWrite(rodaEsqPWM, aceleracaoRodaEsq);
    if(sentido == 1){                             // frente
      digitalWrite(rodaDirFrente, HIGH);
      digitalWrite(rodaEsqFrente, HIGH);
    }else{                                        // tras
      digitalWrite(rodaDirFrente, LOW);
      digitalWrite(rodaEsqFrente, LOW);
    }
}
 
void parar(){
  acelerar(0, 0, 1);
}
 
char mover(char direcao){
    switch(direcao){
       //para frente  
       case 'f':
           acelerar(POTF, POTF, 1);
           break;
             
       //para tras  
       case 't':
           acelerar(POTF, POTF, 0);
           break;
             
       //para direita  
       case 'd':
           acelerar(POTL, POTS, 1);
           break;
             
       //para esquerda
       case 'e':
           acelerar(POTS, POTL, 1);
           break;        
      }
  return direcao;
}
 
void setup() {
    pinMode(rodaDirPWM, OUTPUT);
    pinMode(rodaDirFrente, OUTPUT);
    pinMode(rodaEsqPWM, OUTPUT);
    pinMode(rodaEsqFrente, OUTPUT);
 
   
}
 
void loop() {
    leituraBitsDir = analogRead(sensorDir);
    leituraBitsEsq = analogRead(sensorEsq);
 
    if(leituraBitsEsq < LINHA && leituraBitsDir < LINHA){
        movimento = mover('f');
    }else if(leituraBitsEsq > LINHA && leituraBitsDir < LINHA){
        movimento = mover('d');
    }else if(leituraBitsDir > LINHA && leituraBitsEsq < LINHA){
        movimento = mover('e');
    }else{
        parar();
    }
}