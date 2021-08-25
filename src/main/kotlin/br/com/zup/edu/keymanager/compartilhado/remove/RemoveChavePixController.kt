package br.com.zup.edu.keymanager.compartilhado.remove

import br.com.zup.edu.RemoveChavePixRequest
import br.com.zup.edu.RemoveChavePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes")
class RemoveChavePixController(private val removeChavePixClient: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/{clienteId}/pix/{pixId}")
    fun removeChavePix(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        LOGGER.info("[$clienteId] removendo chave pix de ID [$pixId]")

        removeChavePixClient.remove(RemoveChavePixRequest.newBuilder()
                                    .setClienteId(clienteId.toString())
                                    .setPixId(pixId.toString())
                                    .build()
        )


        return HttpResponse.ok()
    }
}