package br.com.zup.edu.keymanager.compartilhado.carrega

import br.com.zup.edu.ListaChavePixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChavePixResponse(chavePix: ListaChavePixResponse.ChavePixLista) {
    val id = chavePix.pixId
    val chave = chavePix.chave
    val tipo = chavePix.tipoChave
    val tipoDeConta = chavePix.tipoConta
    val criadaEm = chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
