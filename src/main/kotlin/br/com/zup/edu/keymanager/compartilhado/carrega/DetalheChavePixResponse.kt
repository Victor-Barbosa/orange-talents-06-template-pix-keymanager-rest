package br.com.zup.edu.keymanager.compartilhado.carrega

import br.com.zup.edu.CarregaChavePixResponse
import br.com.zup.edu.TipoConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class DetalheChavePixResponse(chaveResponse: CarregaChavePixResponse) {
    val pixId = chaveResponse.pixId
    val tipoChave = chaveResponse.chavePix.tipoChave.name
    val chave = chaveResponse.chavePix.chave

    val criadaEm = chaveResponse.chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when(chaveResponse.chavePix.contaInfo.tipoConta) {
        TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NAO_RECONHECIDA"
    }

    val conta = mapOf(
        Pair("tipoConta", tipoConta),
        Pair("instituicao", chaveResponse.chavePix.contaInfo.institucao),
        Pair("nomeDoTitular", chaveResponse.chavePix.contaInfo.nomeTitular),
        Pair("cpfDoTitular", chaveResponse.chavePix.contaInfo.cpfTitular),
        Pair("agencia", chaveResponse.chavePix.contaInfo.agencia),
        Pair("numero", chaveResponse.chavePix.contaInfo.numeroConta))
}

//data class DetalhesContaResponse(
//    var tipoConta: String,
//    var instituicao: String,
//    var nomeTitular: String,
//    var cpfTitular: String,
//    var agencia: String,
//    var numeroConta: String
//) {
//    companion object {
//        fun toDetalhesConta(chaveResponse: CarregaChavePixResponse): DetalhesContaResponse {
//            return DetalhesContaResponse(
//                tipoConta = chaveResponse.chavePix.contaInfo.tipoConta.name,
//                instituicao = chaveResponse.chavePix.contaInfo.institucao,
//                nomeTitular = chaveResponse.chavePix.contaInfo.nomeTitular,
//                cpfTitular = chaveResponse.chavePix.contaInfo.cpfTitular,
//                agencia = chaveResponse.chavePix.contaInfo.agencia,
//                numeroConta = chaveResponse.chavePix.contaInfo.numeroConta
//            )
//        }
//    }
//}