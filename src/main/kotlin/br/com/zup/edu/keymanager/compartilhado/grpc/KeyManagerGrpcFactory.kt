package br.com.zup.edu.keymanager.compartilhado.grpc

import br.com.zup.edu.CarregaChavePixServiceGrpc
import br.com.zup.edu.KeyManagerGrpcServiceGrpc
import br.com.zup.edu.ListaChavePixServiceGrpc
import br.com.zup.edu.RemoveChavePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("http://localhost:50051") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaChave() = RemoveChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = CarregaChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun carregaChave() = ListaChavePixServiceGrpc.newBlockingStub(channel)

}