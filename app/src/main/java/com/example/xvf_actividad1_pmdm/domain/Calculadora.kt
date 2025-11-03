package com.example.xvf_actividad1_pmdm.domain
//Creamos un SINGLETON, solo existe en una sola instancia durante la ejecucion,es como una clase con metodos estaticos.
object Calculadora{
    //calcula salario neto segun porcentajes IRPF y porcentajes Seguridad Social
    fun calcular(bruto: Double, irpfPorcentaje: Double, ssPorcentaje: Double): ResultadoSalario {
        val irpf = bruto*irpfPorcentaje/100
        val seguridadSocial = bruto*ssPorcentaje/100
        val neto = bruto - irpf - seguridadSocial

        return ResultadoSalario(
            bruto = bruto,
            irpf = irpf,
            seguridadSocial = seguridadSocial,
            neto = neto
        )
    }
}