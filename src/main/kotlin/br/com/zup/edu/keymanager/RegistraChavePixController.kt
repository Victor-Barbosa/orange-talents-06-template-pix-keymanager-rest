package br.com.zup.edu.keymanager

import br.com.zup.edu.KeyManagerGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes")
class RegistraChavePixController(private val registraChavePixClient: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/{clienteId}/pix")
    fun create(clienteId: UUID, @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {

        LOGGER.info("[$clienteId] criando nova chave pix com $request")

        val  grpcResponse = registraChavePixClient.registra(request.paraModeloGrpc(clienteId))

        return HttpResponse.created(location(clienteId, grpcResponse.pixId))
    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${pixId}")
}