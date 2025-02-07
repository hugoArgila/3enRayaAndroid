package com.example.tresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Button botonJugar;//Boton que nos mandara a la activity Tres En Raya

    RadioButton botonCruz;//Boton de las fichas de cruz

    RadioButton botonCirculo;//Boton de las fichas de circulo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonJugar = findViewById(R.id.bjugar);//Llamamos al metodo jugar

        botonJugar.setOnClickListener(new View.OnClickListener() {//metodo
            //que hara al pulsar boton jugar , se nos mandara al tres en raya
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TresEnRaya.class);//Objeto intent para cambiar de clase
                startActivity(intent);//Start Activity pata lanzar el intent que habra el tres en raya
            }
        });

        botonCruz = findViewById(R.id.cruzRadioButton);
        botonCirculo = findViewById(R.id.CircleRadioButton);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        // Configura el listener del botón para iniciar la Activity 2
        Button buttonIrAActivity2 = findViewById(R.id.bjugar);
        buttonIrAActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del RadioButton seleccionado
                int radioButtonId = radioGroup.getCheckedRadioButtonId();

                // Pasa la información al tres en raya
                Intent intent = new Intent(MainActivity.this, TresEnRaya.class);
                intent.putExtra("radioButtonId", radioButtonId);

                // Inicia el tres en raya
                startActivity(intent);
            }
        });
    }
}