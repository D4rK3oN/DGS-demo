package com.example.demo.dgs.animal.service

import com.netflix.dgs.codegen.generated.types.Animal
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AnimalService {
    fun getById(id: String): Mono<Animal>
    fun findAnimals(name: String, size: Double?): Flux<Animal>
    fun findAllAnimals(): Flux<Animal>
}
