package com.example.demo.dgs.animal

import com.example.demo.dgs.animal.service.AnimalServiceInMemory
import com.netflix.dgs.codegen.generated.types.Animal
import com.netflix.dgs.codegen.generated.types.FindAnimalsInput
import com.netflix.dgs.codegen.generated.types.GetAnimalByIdInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

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
}
