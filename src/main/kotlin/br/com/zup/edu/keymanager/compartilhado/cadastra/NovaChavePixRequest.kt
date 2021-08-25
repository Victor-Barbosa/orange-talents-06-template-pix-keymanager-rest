package br.com.zup.edu.keymanager.compartilhado.cadastra

import br.com.zup.edu.RegistraChavePixRequest
import br.com.zup.edu.TipoChave
import br.com.zup.edu.TipoConta
import br.com.zup.edu.keymanager.compartilhado.valid.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class NovaChavePixRequest(@field:NotNull val tipoConta: TipoContaRequest?,
                          @field:Size(max = 77) val chave: String?,
                          @field:NotNull val tipoChave: TipoChaveRequest?) {

    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoConta(tipoConta?.atributoGrpc ?: TipoConta.UNKNOWN_CONTA)
            .setTipoChave(tipoChave?.atributoGrpc ?: TipoChave.UNKNOWN_CHAVE)
            .setChave(chave ?: "")
            .build()
    }
}

enum class TipoChaveRequest(val atributoGrpc: TipoChave) {

    CPF(TipoChave.CPF) {
        override fun valida(chave: String?): Boolean {
            return chave.isNullOrBlank()
        }
    },

    CELULAR(TipoChave.CELULAR) {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoChave.EMAIL) {
        override fun valida(chave: String?): Boolean {
            if (!chave.isNullOrBlank()) {
                val regex =
                    Regex("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
                            "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$")
                if (chave.matches(regex)) return true
            }
            return false
        }

    },

    ALEATORIA(TipoChave.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank()
    };

    abstract fun valida(chave: String?): Boolean
}

enum class TipoContaRequest(val atributoGrpc: TipoConta) {

    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)

}
