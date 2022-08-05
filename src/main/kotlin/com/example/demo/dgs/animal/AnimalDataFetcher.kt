package com.example.demo.dgs.animal

import com.example.demo.dgs.animal.service.AnimalServiceInMemory
import com.netflix.dgs.codegen.generated.DgsConstants
import com.netflix.dgs.codegen.generated.types.Animal
import com.netflix.dgs.codegen.generated.types.FindAnimalsInput
import com.netflix.dgs.codegen.generated.types.GetAnimalByIdInput
import com.netflix.dgs.codegen.generated.types.NewAnimalInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import graphql.schema.DataFetchingEnvironment
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.Optional

@DgsComponent
class AnimalDataFetcher(private val animalService: AnimalServiceInMemory) {

    @DgsQuery
    fun getAnimalById(getAnimalByIdInput: GetAnimalByIdInput): Mono<Animal> =
        animalService.getById(getAnimalByIdInput.id)

    @DgsQuery
    fun findAnimals(findAnimalsInput: FindAnimalsInput): Flux<Animal> =
        animalService.findAnimals(findAnimalsInput.name, findAnimalsInput.size)

    @DgsQuery
    fun findAllAnimals(): Flux<Animal> =
        animalService.findAllAnimals()

    @DgsMutation
    fun addAnimal(newAnimalInput: NewAnimalInput): Mono<Animal> =
        animalService.addAnimal(newAnimalInput.name, newAnimalInput.size)

    @DgsData(parentType = DgsConstants.ANIMAL.TYPE_NAME, field = DgsConstants.ANIMAL.SizeText)
    fun sizeText(dfe: DataFetchingEnvironment): Mono<String> {
        val animal = dfe.getSource<Animal>()

        return if (Optional.ofNullable(animal.size).isPresent) {
            Mono.just("Mi peso es ${animal.size}")
        } else {
            Mono.just("Peso desconocido")
        }
    }
}
