package com.example.xvf_actividad1_pmdm.domain
//Data class es una construcciñon para clases que SOLO contienen datos,
//el compilador genera automaticamente: equals(), hashCode(), toString(), copy() y componentN()
//Hay que usarla cuando tu clase NO tenga una lógica compleja y represente un DTO/Modelo de datos puros
data class ResultadoSalario (
    val bruto: Double,
    val irpf: Double,
    val seguridadSocial: Double,
    val neto: Double,
)

