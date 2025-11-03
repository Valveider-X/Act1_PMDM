# Actividad 1 para PMDM - Programación multimedia y dispositivos móviles - 2ndo DAM

Esta actividad para PMDM ha sido creada en **Kotlin** con **Jetpack Compose**, permite calcular el salario neto a partir del salario bruto, el IRPF (%) y la Seguridad Social (%).

Está dividida en **dos pantallas**:
1. Una pantalla con un **formulario** para introducir los datos.
2. Una pantalla con el **resultado del cálculo**.

Su propósito es **aprender y entender los fundamentos reales de Compose, Kotlin y la navegación entre pantallas**.
Como extra he creado un readme detallado para poder repasar los conceptos de cara al exámen.

---

## Objetivos de aprendizaje

- Comprender cómo se estructura un proyecto de Android con Compose.
- Aprender a usar `@Composable`, `remember` y `mutableStateOf`.
- Entender cómo funciona la navegación entre pantallas (`NavHost`, `NavController`).
- Familiarizarse con palabras clave del lenguaje Kotlin y conceptos de Jetpack Compose.

---

## Estructura del proyecto

```
app/
└── kotlin+java/
    └── com.example.xvf_actividad1_pmdm/
        ├── domain/
        │   ├── Calculadora.kt
        │   └── ResultadoSalario.kt
        └── MainActivity.kt
```

### Archivos

| Archivo | Descripción                                                                                                                |
|----------|----------------------------------------------------------------------------------------------------------------------------|
| **MainActivity.kt** | Contiene la actividad principal, las dos pantallas (`PantallaFormulario`, `PantallaResultado`) y el sistema de navegación. |
| **dominio/Calculadora.kt** | La lógica que realiza el cálculo del salario neto.                                                                         |
| **dominio/ResultadoSalario.kt** | Clase de datos (`data class`) que almacena los resultados calculados.                                                      |

---

## Cómo Funciona

1. **Pantalla 1 - Formulario:**  
   El usuario introduce el salario bruto y los porcentajes de IRPF y Seguridad Social.  
   Al pulsar **Vamos a Calcular**, la app realiza el cálculo y navega a la segunda pantalla.

2. **Pantalla 2 - Resultado:**  
   Se muestra el detalle del cálculo y un botón **Volver** que retorna al formulario.

```
[PantallaFormulario]
↓ (Botón Calcular)
[PantallaResultado]
↑ (Botón Volver)
```

---

## Lógica del cálculo (`domain/Calculadora.kt`)

```kotlin
object Calculadora {
    fun calcular(bruto: Double, irpfPorcentaje: Double, ssPorcentaje: Double): ResultadoSalario {
        val irpf = bruto * irpfPorcentaje / 100
        val seguridadSocial = bruto * ssPorcentaje / 100
        val neto = bruto - irpf - seguridadSocial

        return ResultadoSalario(bruto, irpf, seguridadSocial, neto)
    }
}
```

**Explicación**

- `object`: crea una instancia única global (Así no necesito usar `new` ni instanciar).
- `fun`: define una función.
- `val`: crea una variable inmutable.
- Los cálculos son operaciones matemáticas simples.
- La función devuelve un `ResultadoSalario`, que es una data class con los resultados.

```kotlin
data class ResultadoSalario(
    val bruto: Double,
    val irpf: Double,
    val seguridadSocial: Double,
    val neto: Double
)
```

**Explicación**

- `data class`: clase especial para almacenar datos sin tener que escribir código extra.
- Cada propiedad almacena parte del resultado del cálculo.

### MainActivity

```kotlin
val navController = rememberNavController()
var resultado by remember { mutableStateOf<ResultadoSalario?>(null) }

NavHost(navController, startDestination = "formulario") {
    composable("formulario") {
        PantallaFormulario { bruto, irpf, ss ->
            resultado = Calculadora.calcular(bruto, irpf, ss)
            navController.navigate("resultado")
        }
    }
    composable("resultado") {
        PantallaResultado(resultado) {
            navController.popBackStack()
        }
    }
}
```

**Qué hace cada método:**

| Código | Explicación                                                  |
|--------|--------------------------------------------------------------|
| `rememberNavController()` | Crea el navegador de la app, controla a qué pantalla vas.    |
| `NavHost` | Define las pantallas disponibles.                            |
| `composable("formulario")` | Define la pantalla del formulario y lo que se dibuja en ella. |
| `navController.navigate("resultado")` | Cambia a la pantalla del resultado.                          |
| `navController.popBackStack()` | Vuelve a la pantalla anterior.                               |

### Pantalla 1: Formulario

```kotlin
var bruto by remember { mutableStateOf("") }

OutlinedTextField(
    value = bruto,
    onValueChange = { bruto = it },
    label = { Text("Salario bruto") }
)
```

**Qué hace `remember` y `mutableStateOf`**

- `remember` guarda el valor incluso cuando la interfaz se redibuja.
- `mutableStateOf` crea una variable reactiva que, al cambiar, actualiza automáticamente la UI.

```kotlin
var bruto by remember { mutableStateOf("") }
```

Crea una variable `bruto` que empieza vacía y que Compose recordará mientras esta pantalla esté visible.

 **Qué hace `value` y `onValueChange`**

- `value` muestra el valor actual del campo.
- `onValueChange` dice qué hacer cuando el usuario escribe algo.

```kotlin
onValueChange = { bruto = it }
```

Guarda el nuevo texto que el usuario escribe dentro de la variable `bruto`.

 **Qué es `it`**

En Kotlin, `it` es el parámetro de la función lambda actual.  
Representa el texto que acaba de escribir el usuario.

```kotlin
onValueChange = { nuevoTexto -> bruto = nuevoTexto }
```

 **En resumen:**

Cada vez que el usuario escribe algo:

- `onValueChange` recibe el nuevo texto (en `it`).
- Se guarda en `bruto`.
- Compose detecta el cambio y redibuja automáticamente el campo.

### Pantalla 2: Resultado

```kotlin
Text("Salario neto: ${resultado.neto}")
Button(onClick = onVolver) { Text("Volver") }
```

- Muestra los resultados guardados en la variable `resultado`.
- Usa `HorizontalDivider()` para separar visualmente las secciones.
- El botón ejecuta `onVolver`, que llama a `navController.popBackStack()`.

---

## Palabras clave y conceptos de Kotlin.

| Palabra clave | Significado                                                           | Ejemplo                                  |
|---------------|-----------------------------------------------------------------------|------------------------------------------|
| `package` | Define el nombre del paquete del archivo.                             | `package com.tuempresa.miapp`            |
| `import` | Permite usar clases o funciones de otros paquetes.                    | `import androidx.compose.material3.*`    |
| `class` | Define una clase (estructura de código).                              | `class MainActivity`                     |
| `object` | Define una clase de la que solo hay una instancia.                    | `object Calculadora`                     |
| `fun` | Declara una función.                                                  | `fun calcular(...)`                      |
| `val` | Variable inmutable.                                                   | `val neto = bruto - irpf - ss`           |
| `var` | Variable mutable.                                                     | `var bruto = ""`                         |
| `data class` | Clase usada solo para guardar datos.                                  | `data class ResultadoSalario(...)`       |
| `by` | Delegación de propiedades, usado con `remember { mutableStateOf() }`. | `var ss by remember { ... }`             |
| `@Composable` | Indica que una función puede dibujar UI con Compose.                  | `@Composable fun PantallaFormulario()`   |
| `remember` | Guarda un valor en memoria entre redibujos.                           | `remember { mutableStateOf("") }`        |
| `mutableStateOf` | Crea una variable observable.                                         | `var texto by mutableStateOf("")`        |
| `it` | Parámetro implícito en lambdas de un solo argumento.                  | `{ ss = it }`                            |
| `navigate` | Mueve a otra pantalla en el NavController.                            | `navController.navigate("resultado")`    |
| `popBackStack` | Vuelve atrás en la navegación.                                        | `navController.popBackStack()`           |

---
## Enlaces oficiales con la documentación de la que trata el ejercicio.
[Tutorial general de Jetpack Compose](https://developer.android.com/develop/ui/compose/tutorial?hl=es-419)
[El estado y Compose](https://developer.android.com/develop/ui/compose/state?hl=es-419)
[Navegación en Compose](https://developer.android.com/develop/ui/compose/navigation?hl=es-419)
[API Runtime de Compose](https://developer.android.com/reference/kotlin/androidx/compose/runtime/package-summary)
## Autor

Desarrollado por [Xavier Valverde](https://github.com/Valveider-X)

