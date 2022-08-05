package com.example.demo.dgs.animal

import com.example.demo.dgs.animal.service.AnimalServiceInMemory
import com.jayway.jsonpath.TypeRef
import com.netflix.dgs.codegen.generated.client.FindAllAnimalsGraphQLQuery
import com.netflix.dgs.codegen.generated.client.FindAllAnimalsProjectionRoot
import com.netflix.dgs.codegen.generated.client.GetAnimalByIdGraphQLQuery
import com.netflix.dgs.codegen.generated.client.GetAnimalByIdProjectionRoot
import com.netflix.dgs.codegen.generated.types.Animal
import com.netflix.dgs.codegen.generated.types.GetAnimalByIdInput
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import com.netflix.graphql.dgs.reactive.DgsReactiveQueryExecutor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AnimalDataFetcherTest {

    val animals: Array<Animal> = arrayOf(
        Animal("01", "Calcetines"),
        Animal("02", "Luna", 3.5),
        Animal("03", "Misi", 3.0)
    )

    @Autowired
    lateinit var dgsQueryExecutor: DgsReactiveQueryExecutor

    @MockBean
    private lateinit var service: AnimalServiceInMemory

    @Test
    @Disabled
    fun getById_whenIdNotExist_thenReturnEmpty() {
        Mockito.`when`(service.getById(anyString()))
            .thenReturn(Mono.empty())

        val query: GetAnimalByIdGraphQLQuery = GetAnimalByIdGraphQLQuery.Builder()
            .getAnimalByIdInput(GetAnimalByIdInput(anyString()))
            .build()

        val projection: GetAnimalByIdProjectionRoot = GetAnimalByIdProjectionRoot()
            .id()
            .name()
            .size()

        val request = GraphQLQueryRequest(query, projection)

        val animal = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            request.serialize(),
            "data.${query.getOperationName()}",
            object : TypeRef<Animal?>() {}
        )

        TODO("DefaultDgsReactiveQueryExecutor returned a null value.")

        /*
         * { "data": { "getAnimalById": null } }
         * */

        // assertTrue(animal.blockOptional().isEmpty)
    }

    @Test
    fun getById_whenIdExist_thenReturnAnimal() {
        Mockito.`when`(service.getById(anyString()))
            .thenReturn(Mono.just(animals[1]))

        val query: GetAnimalByIdGraphQLQuery = GetAnimalByIdGraphQLQuery.Builder()
            .getAnimalByIdInput(GetAnimalByIdInput(anyString()))
            .build()

        val projection: GetAnimalByIdProjectionRoot = GetAnimalByIdProjectionRoot()
            .id()
            .name()
            .sizeText()

        val request = GraphQLQueryRequest(query, projection)

        val animal = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
            request.serialize(),
            "data.${query.getOperationName()}",
            object : TypeRef<Animal>() {}
        )
            .blockOptional()

        assertAll(
            { assertTrue(animal.isPresent) },
            { assertEquals("02", animal.get().id) },
            { assertEquals("Luna", animal.get().name) },
            { assertEquals("Mi peso es 3.5", animal.get().sizeText) }
        )
    }

    @Test
    fun findAllAnimals_thenReturnList() {
        Mockito.`when`(service.findAllAnimals())
            .thenReturn(Flux.fromArray(animals))

        val query: FindAllAnimalsGraphQLQuery = FindAllAnimalsGraphQLQuery.Builder().build()

        val projection: FindAllAnimalsProjectionRoot = FindAllAnimalsProjectionRoot()
            .name()
            .sizeText()

        val request = GraphQLQueryRequest(query, projection)

        val animals = dgsQueryExecutor.executeAndExtractJsonPath<List<Animal>>(
            request.serialize(),
            "data.${query.getOperationName()}.[*]"
        )
            .blockOptional()

        assertAll(
            { assertTrue(animals.isPresent) },
            { assertFalse(animals.get().isEmpty()) },
            { assertEquals(3, animals.get().size) }
        )
    }
}
