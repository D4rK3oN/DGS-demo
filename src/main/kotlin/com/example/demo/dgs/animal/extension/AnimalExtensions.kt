package com.example.demo.dgs.animal.extension

import com.netflix.dgs.codegen.generated.types.Animal

fun Animal.Companion.sizeText(param: Double?): String =
    param?.let { "Mi peso es $it" } ?: "Peso desconocido"
