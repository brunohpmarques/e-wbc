/*
 * Interpretador de comandos BT-Arduino
 *
 */

// byte recebido
char data = 0;

// fila de comandos a serem executados
char commandQueue[50];

// numero de comandos (valor real e valor encontrado)
int numCommandsReal = 0;
int numCommandsFound = 0;

// estados do carrinho
typedef enum { NONE, READ_NUM_COMMANDS, READ_COMMANDS, READY } states;
typedef enum { NO_COLOR, RED, GREEN, BLUE } colors;

// estado atual
states currentState = NONE;

// cor atual (objeto)
colors currentColor = NO_COLOR;

void setup() {
    // TODO: setar pinos de entrada e saída

    Serial1.begin(9600);
}

void moveForward() {
    // TODO: andar para frente até detectar algo
    Serial1.println("Andar para frente");
}

void turnLeft() {
    // TODO
    Serial1.println("Virar para esquerda");
}

void turnRight() {
    // TODO
    Serial1.println("Virar para direita");
}

void kickObject() {
    // TODO: andar e chutar objeto
    Serial1.println("Chutar objeto");
}

void processCommandQueue() {
    for (int i = 0; i < numCommandsReal; i++) {
        if (commandQueue[i] == 'W') {
            moveForward();
        } else if (commandQueue[i] == 'A') {
            turnLeft();
        } else if (commandQueue[i] == 'D') {
            turnRight();
        } else if (commandQueue[i] == 'K') {
            kickObject();
        }
    }

    numCommandsReal = 0;
    currentState = NONE;
}

void processIncomingByte(const char b) {
    if (isdigit(b)) {
        if (currentState == READ_NUM_COMMANDS) {
            numCommandsReal *= 10;
            numCommandsReal += b - 48;
        }
        
//        else {
//            if (b == '1') {
//                Serial1.print(b);
//                
//                currentColor = RED;
//            } else if (b == '2') {
//                Serial1.print(b);
//                
//                currentColor = GREEN;
//            } else if (b == '3') {
//                Serial1.print(b);
//                
//                currentColor = BLUE;
//            }
//        }
    } else {
        if (currentState == READ_NUM_COMMANDS && numCommandsReal != 0) {
            currentState = READ_COMMANDS;
        }

        if (currentState == NONE) {
            if (b == '$') {
                Serial1.print(b);
            
                currentState = READ_NUM_COMMANDS;
            }
        } else if (currentState == READ_COMMANDS) {
            if (b == 'W' || b == 'A' || b == 'D' || b == 'K') {
                Serial1.print(b);

                commandQueue[numCommandsFound++] = b;
            } else if (b == '#') {
                if (numCommandsReal == numCommandsFound) {
                    Serial1.println(b);

                    currentState = READY;
                } else {
                    // erro de transmissão
                    // TODO: pedir reenvio?
                    Serial1.println("E");

                    numCommandsReal = 0;
                    currentState = NONE;
                }

                numCommandsFound = 0;
            }
        }
    }
}

void loop() {
    if (Serial1.available() > 0) {
        data = Serial1.read();
        
//         processIncomingByte(data);
        
        Serial.println(data);
    }

//     if (currentState == READY) {
//         // executar fila de comandos
//         processCommandQueue();
//     }
}
