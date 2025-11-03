package com.example.xvf_actividad1_pmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.xvf_actividad1_pmdm.domain.Calculadora
import com.example.xvf_actividad1_pmdm.domain.ResultadoSalario
import com.example.xvf_actividad1_pmdm.ui.theme.XVFActividad1PMDMTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                NavegacionApp()

            }
        }
    }
}
//mutableStateOf crea un objeto MutableState para que Compose lo observe, cuando su valos cambia el repinta los composables que lo usan
//remember permite que el estado se mantenga durante recomposiciones, si no se reiniciaria la variable
//by es sintaxis de Kotlin, lo trata como variable normal en vez de usar .value
@Composable
fun NavegacionApp(){
    val navController = rememberNavController()
    var resultado by remember {mutableStateOf<ResultadoSalario?>(null)}

    NavHost(navController = navController, startDestination = "pantalla1"){
        //formulario
        composable("pantalla1"){
            PantallaFormulario {bruto, irpf, ss ->
                    resultado = Calculadora.calcular(bruto, irpf, ss)
                    navController.navigate("pantalla2")
                }
            }
        //resultados
        composable("pantalla2"){
            PantallaResultado(resultado) {
                navController.popBackStack()
            }

        }
    }
}
@Composable
fun PantallaFormulario(onCalcular: (Double, Double, Double)-> Unit) {
    var bruto by remember { mutableStateOf("") }
    var irpf by remember { mutableStateOf("15") }
    var ss by remember { mutableStateOf("6.35") }

    //Column organiza elementos de forma vertical
    Column(
        modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Calculadora Salario", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = bruto, onValueChange = { bruto = it},
            label = { Text("Salario bruto (ej. 2000)")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = irpf, onValueChange = { irpf = it},
            label = { Text("IRPF (%)")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = ss, onValueChange = { ss = it},
            label = { Text("Seguridad Social (%)")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick= {
                //toDoubleOrNull convierte un string a double o null si no es numeros, si usamos toDouble() debemos lanzar una excepcion de numerFormat.
                val bruto = bruto.toDoubleOrNull() ?: 0.0
                val irpf = irpf.toDoubleOrNull() ?: 0.0
                val ss = ss.toDoubleOrNull() ?: 0.0
                onCalcular(bruto, irpf, ss)
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Vamos a Calcular")
        }
    }
}
@Composable
fun PantallaResultado(resultado: ResultadoSalario?, onVolver: () -> Unit){
//lanzo una opción para regresar en caso de que algo falle y la operación de null.
    if(resultado == null){
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text("No hay datos disponibles actualmente")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onVolver){
                    Text("Volver")
                }
            }
        }
        return
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text("Resultado del cálculo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Salario bruto: ${resultado.bruto} €")
        Text("IRPF ${resultado.irpf} €")
        Text("Seguridad Social ${resultado.seguridadSocial} €")

        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Text("Salario neto: ${resultado.neto} €", style =MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()){
            Text("Volver a Calcular")
        }
    }
}