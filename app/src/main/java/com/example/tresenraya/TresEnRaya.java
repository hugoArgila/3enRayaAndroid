package com.example.tresenraya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Random;

public class TresEnRaya extends AppCompatActivity {

    TextView textoVictoria; //Objeto TextView que
    // mostrara si ganamos , perdemos o empatamos.
    Integer[] botones; //Array de Integer que contendra los botones clicables.
    int[] tablero = new int[]{
            0,0,0,
            0,0,0,
            0,0,0
    }; //array que contendra cada posicion en nuestro tablero.

    int estado =0; //Estado de la partida (Ganado(1)/Perdido(-1)/Empate(2)/Seguir jugando(0))

    int casillasMarcadas =0; //Variable contador de las casillas marcadas

    int turno =1; //variable de los turnos de la partida (Jugador(1)/Rival (-1))

    int[] posGanadora =  new int[]{-1 , -1 , -1};//Array que contenga las posiciones variables


    Button botonRegresar;//Boton que servira para volver a main activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tres_en_raya);

        textoVictoria = (TextView) findViewById(R.id.textoVictoria);//Le asignamos el
        // textoBox de victoria o derrota en Main activity.xml al Objeto

        textoVictoria.setVisibility(View.INVISIBLE);//Establecemos en oculto el
        //textbox

        botones = new Integer[]{
                R.id.b1, R.id.b2 , R.id.b3 ,
                R.id.b4 , R.id.b5 , R.id.b6,
                R.id.b7 , R.id.b8 , R.id.b9,
        };//rellenamos el array botones poniendo en cada posicion uno de los botones
        //en Main Activity.xml

        botonRegresar = findViewById(R.id.bRegresar);

        botonRegresar.setVisibility(View.INVISIBLE);
            botonRegresar.setOnClickListener(new View.OnClickListener() {//metodo
                //que hara al pulsar boton regresar , se nos mandara al main activity
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TresEnRaya.this, MainActivity.class);//Objeto intent para cambiar de clase
                    startActivity(intent);//Start Activity pata lanzar el intent que habra el main activity
                }
            });

        Button botonCerrarApp = findViewById(R.id.bCerrarApp);
        botonCerrarApp.setOnClickListener(new View.OnClickListener() {//metodo
            //que hara al pulsar boton cerrar , se cerrara la apk
            @Override
            public void onClick(View v) {
                cerrarApp(v);
            }
        });
    }

    public void cerrarApp(View view) {//metodo para cerrar la apk
        finishAffinity();
    }

    @SuppressLint("NonConstantResourceId")
    public void marcarCasilla(View v) {//metodo para marcar casillas del tres en raya
        if (estado == 0) {

            turno = 1;

            int numBot = Arrays.asList(botones).indexOf(v.getId());//creamos una variable
            //de int que le asignamos el boton que sea clicado mediante un getID

            if (tablero[numBot] == 0) {//impide seleccionar un boton ya presionado

                Intent intent = getIntent();
                int radioButtonId = intent.getIntExtra("radioButtonId", -1);

                if (radioButtonId == R.id.cruzRadioButton) {//metodo para decidir si seras circulos
                    //o cruces
                    v.setBackgroundResource(R.drawable.tresenrayax);//Estableces la imagen del boton
                    //clicado en una x
                } else  {
                    v.setBackgroundResource(R.drawable.tresenrayao);//le aplicamos al boton la imagen
                    //del circulo
                }

                tablero[numBot] = 1;//cambiamos el numero de 0 a 1 en el array tableros para
                //que no pueda volver a ser seleccionado.

                casillasMarcadas += 1;//aumentamos en 1 el contador

                estado = comprobarEstado();//comprobamos el estado de la partida

                terminarPartida();//llamamos a terminar partida

                Button botonNuevaPartida = findViewById(R.id.bNuevaPartida); //objeto que contendra el boton
                // nueva partida

                botonNuevaPartida.setOnClickListener(new View.OnClickListener() {//metodo
                    //que hara que si el boton se clicka, se crea una nueva partida

                    @Override
                    public void onClick(View v) {
                        nuevaPartida();//llamamos a nueva partida
                    }
                });

                if (estado == 0) {
                    turno = -1;
                    iaRival();//Tras seleccionar un boton , la IA  marcara otra casilla
                    casillasMarcadas += 1;//aumentamos en 1 el contador
                    estado = comprobarEstado();//comprobamos el estado de la partida
                    terminarPartida();//llamamos a terminar partida
                }
            }
        }
    }

    public void iaRival(){//metodo para crear el comportamiento de la IA/Rival
        Random ranRival = new Random();//Creamos un objeto random para que se randomice
        //que boton marcara la ia

        int pos = ranRival.nextInt(tablero.length);//Creamos una variable int para asignarle
        //mediante el objeto random y el array tablero la pos que se marcara

        while(tablero[pos] !=0 ){// while para que solo pueda marcar los que son 0, es decir
            //los no pulsados por el usuario

            pos = ranRival.nextInt(tablero.length);//le asignamos a pos una posicion del
            //tablero aleatoria
        }

        Button bRival = (Button) findViewById(botones[pos]);//le aplicamos al boton elegido
        //aleatoriamente pos

        Intent intent = getIntent();
        int radioButtonId = intent.getIntExtra("radioButtonId", -1);

        if (radioButtonId == R.id.cruzRadioButton) {//metodo para decidir si el rival sera circulos
            //o cruces

            bRival.setBackgroundResource(R.drawable.tresenrayao);//le aplicamos al boton la imagen
            //del circulo
        } else  {
            bRival.setBackgroundResource(R.drawable.tresenrayax);//Estableces la imagen del boton
            //clicado en una x
        }

        tablero[pos] = -1 ;//le asignamos -1 a la posicion del tablero para que el usuario
        //no pueda clicarla
    }

    public int comprobarEstado(){//Metodo para controlar el estado
        int nuevoEstado = 0;
        if (Math.abs(tablero[0]+ tablero[1] + tablero[2]) ==3){//si la suma de las
            // posiciones es = 3 es que la partida acaba
            posGanadora = new int[]{0,1,2};
            nuevoEstado = 1*turno; //Calcular x1(ganador) o x-1 (perdedor)
        } else if ((Math.abs(tablero[3]+ tablero[4] + tablero[5]) ==3)) {
            posGanadora = new int[]{3,4,5};
            nuevoEstado = 1*turno;
        } else if ((Math.abs(tablero[6]+ tablero[7] + tablero[8]) ==3)) {
            posGanadora = new int[]{6,7,8};
            nuevoEstado = 1*turno;
        }else if ((Math.abs(tablero[0]+ tablero[3] + tablero[6]) ==3)) {
            posGanadora = new int[]{0,3,6};
            nuevoEstado = 1*turno;
        }else if ((Math.abs(tablero[1]+ tablero[4] + tablero[7]) ==3)) {
            posGanadora = new int[]{1,4,7};
            nuevoEstado = 1*turno;
        }else if ((Math.abs(tablero[2]+ tablero[5] + tablero[8]) ==3)) {
            posGanadora = new int[]{2,5,8};
            nuevoEstado = 1*turno;
        }else if ((Math.abs(tablero[0]+ tablero[4] + tablero[8]) ==3)) {
            posGanadora = new int[]{0,4,8};
            nuevoEstado = 1*turno;
        }else if ((Math.abs(tablero[2]+ tablero[4] + tablero[6]) ==3)) {
            posGanadora = new int[]{2,4,6};
            nuevoEstado = 1*turno;
        } else if  (casillasMarcadas == 9){
            nuevoEstado =2; //Empate
        }


        return nuevoEstado;
    }

    public void terminarPartida(){//metodo que manda si se debe terminar la partida
        if (estado==1 || estado ==-1){// si el estado es ganador o perdedor
            if(estado ==1){
                textoVictoria.setVisibility(View.VISIBLE);//hacemos visible el text
                textoVictoria.setTextColor(Color.GREEN);//cambiamos el text color a verde
                botonRegresar.setVisibility(View.VISIBLE);
            }else if(estado ==-1){
                textoVictoria.setVisibility(View.VISIBLE);//hacemos visible el text
                textoVictoria.setText("Has perdido :(");//cambiamos contenido de textbox
                textoVictoria.setTextColor(Color.RED);//cambiamos el text color a rojo
                botonRegresar.setVisibility(View.VISIBLE);
            }
        } else if (estado ==2) {//Si el estado es empate
            textoVictoria.setVisibility(View.VISIBLE);//hacemos visible el text
            textoVictoria.setText("Has Empatado ");//cambiamos contenido de textbox
            botonRegresar.setVisibility(View.VISIBLE);

        }
    }

    public void nuevaPartida(){//Metodo para empezar una partida nueva
        textoVictoria.setVisibility(View.INVISIBLE);
        estado = 0;
        int nuevaFicha = R.drawable.tresenrayanp;
        for (int i =0 ; i<botones.length;i++){//for para cambiar las imagenes
            Button b = findViewById(botones[i]);
            //b.setBackground((Drawable)R.drawable.tresenrayanp);
            b.setBackgroundResource(nuevaFicha);

        }

        tablero = new int[]{
                0,0,0,
                0,0,0,
                0,0,0
        };

        turno = 1;
    }
}

