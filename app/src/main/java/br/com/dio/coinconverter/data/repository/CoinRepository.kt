package br.com.dio.coinconverter.data.repository

import br.com.dio.coinconverter.data.model.ExchangeResponse
import br.com.dio.coinconverter.data.model.ExchangeResponseValue
import kotlinx.coroutines.flow.Flow
import okhttp3.internal.connection.Exchange

interface CoinRepository {
    suspend fun getExchangeValue(coins:String) : Flow<ExchangeResponseValue>
    suspend fun save(exchange: ExchangeResponseValue)

    fun list(): Flow<List<ExchangeResponseValue>>

}