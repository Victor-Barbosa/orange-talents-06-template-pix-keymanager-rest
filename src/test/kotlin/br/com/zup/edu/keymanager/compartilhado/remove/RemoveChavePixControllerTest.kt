package br.com.zup.edu.keymanager.compartilhado.remove

import br.com.zup.edu.RemoveChavePixResponse
import br.com.zup.edu.RemoveChavePixServiceGrpc
import br.com.zup.edu.RemoveChavePixServiceGrpc.*
import br.com.zup.edu.keymanager.compartilhado.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest {

    @field:Inject
    lateinit var removeStub: RemoveChavePixServiceBlockingStub


    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `DEVE remover uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RemoveChavePixResponse.newBuilder()
                                                 .setMensagem("erro")
                                                 .build()


        given(removeStub.remove(any())).willReturn(respostaGrpc)


        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)

    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class RemoveStubFactory {

        @Singleton
        fun deletaChave() = mock(RemoveChavePixServiceBlockingStub::class.java)
    }
}

