package br.com.zup.edu.keymanager.compartilhado.carrega

import br.com.zup.edu.*
import br.com.zup.edu.keymanager.compartilhado.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CarregaChavePixControllerTest {

    @field:Inject
    lateinit var carregaChaveStub: CarregaChavePixServiceGrpc.CarregaChavePixServiceBlockingStub

    @field:Inject
    lateinit var listaChaveStub: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class CarregaStubFactory {
        @Singleton
        fun stubDetalhesMock() =
            Mockito.mock(CarregaChavePixServiceGrpc.CarregaChavePixServiceBlockingStub::class.java)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class ListaStubFactory {
        @Singleton
        fun stubDetalhesMock() =
            Mockito.mock(ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub::class.java)
    }

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient



    private fun carregaChavePixResponse(clienteId: String, pixId: String): CarregaChavePixResponse {
        return CarregaChavePixResponse.newBuilder()
                                        .setClienteId(clienteId)
                                        .setPixId(pixId)
                                        .setChavePix(
                                            ChavePix.newBuilder()
                                                .setChave("teste@teste.com")
                                                .setContaInfo(
                                                    ContaInfo.newBuilder()
                                                        .setTipoConta(TipoConta.CONTA_CORRENTE)
                                                        .setInstitucao("ITAU UNIBANCO")
                                                        .setNomeTitular("VictorBarbosa")
                                                        .setCpfTitular("35710118052")
                                                        .setAgencia("0001")
                                                        .setNumeroConta("123456")
                                                        .build()
                                                )
                                                .setCriadaEm(now().let {
                                                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                                                    Timestamp.newBuilder()
                                                        .setSeconds(createdAt.epochSecond)
                                                        .setNanos(createdAt.nano)
                                                        .build()
                                                })
                                        ).build()
    }

    private fun listaChavePixResponse(clienteId: String): ListaChavePixResponse {

        val chaveEmail = ListaChavePixResponse.ChavePixLista.newBuilder()
                                              .setPixId(UUID.randomUUID().toString())
                                              .setTipoChave(TipoChave.EMAIL)
                                              .setChave("teste@teste.com")
                                              .setTipoConta(TipoConta.CONTA_CORRENTE)
                                              .setCriadaEm(LocalDateTime.now().let {
                                                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                                                    Timestamp.newBuilder()
                                                        .setSeconds(createdAt.epochSecond)
                                                        .setNanos(createdAt.nano)
                                                        .build()
                                              })
                                              .build()


        val chaveCelular = ListaChavePixResponse.ChavePixLista.newBuilder()
                                                .setPixId(UUID.randomUUID().toString())
                                                .setTipoChave(TipoChave.CELULAR)
                                                .setChave("+5532979369851")
                                                .setTipoConta(TipoConta.CONTA_CORRENTE)
                                                .setCriadaEm(LocalDateTime.now().let {
                                                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                                                    Timestamp.newBuilder()
                                                        .setSeconds(createdAt.epochSecond)
                                                        .setNanos(createdAt.nano)
                                                        .build()
                                                })
                                                .build()


        return ListaChavePixResponse.newBuilder()
                                    .setClienteId(clienteId)
                                    .addAllChaves(listOf(chaveEmail, chaveCelular))
                                    .build()

    }


    @Test
    fun `deve carregar uma chave pix existente`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(carregaChaveStub.carrega(Mockito.any())).willReturn(carregaChavePixResponse(clienteId, pixId))


        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Test
    fun `deve listar todas as chaves pix existente`() {

        val clienteId = UUID.randomUUID().toString()

        val respostaGrpc = listaChavePixResponse(clienteId)

        given(listaChaveStub.lista(Mockito.any())).willReturn(respostaGrpc)


        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(response.body()!!.size, 2)
    }
}
