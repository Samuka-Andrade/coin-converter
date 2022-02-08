package br.com.dio.coinconverter.data.model

import java.util.*

enum class Coin(val locale: Locale) {
    USD(Locale.US),
    EUR(Locale.FRANCE),
    BRL(Locale("pt", "BR")),

    ;

    companion object {
        fun getByName(name: String) = values().find { it.name == name } ?: BRL
    }
}