package br.com.zup.edu.keymanager.compartilhado.carrega

import br.com.zup.edu.CarregaChavePixRequest
import br.com.zup.edu.CarregaChavePixServiceGrpc
import br.com.zup.edu.ListaChavePixRequest
import br.com.zup.edu.ListaChavePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class CarregaChavePixController(val carregaChavePixClient: CarregaChavePixServiceGrpc.CarregaChavePixServiceBlockingStub,
                                val listaChavePixClient: ListaChavePixServiceGrpc.ListaChavePixServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carrega(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        LOGGER.info("[$clienteId] carregando chave pix de ID [$pixId]")

        val carregaChaveResponse = carregaChavePixClient.carrega(
            CarregaChavePixRequest.newBuilder()
                                  .setPixId(
                                      CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                                                            .setClienteId(clienteId.toString())
                                                            .setPixId(pixId.toString())
                                                            .build())
                                  .build()
        )

        return HttpResponse.ok(DetalheChavePixResponse(carregaChaveResponse))
    }

    @Get("/pix/")
    fun lista(clienteId: UUID): HttpResponse<Any> {

        LOGGER.info("[$clienteId] listando chaves pix")

        val pix = listaChavePixClient.lista(ListaChavePixRequest.newBuilder()
                                                                .setClienteId(clienteId.toString())
                                                                .build()
        )

        val chaves = pix.chavesList.map { ChavePixResponse(it) }

        return HttpResponse.ok(chaves)
    }
}
