package com.example.demo.dgs.animal.service

import com.netflix.dgs.codegen.generated.types.Animal
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AnimalServiceInMemory : AnimalService {

    val list: Array<Animal> = arrayOf(
        Animal("01", "Calcetines"),
        Animal("02"),
        Animal("03", "Luna", 3.5),
        Animal("04", null, 5.0),
        Animal("05", "Misi", 3.0)
    )

    override fun getById(id: String): Mono<Animal> =
        Mono.justOrEmpty(list.firstOrNull { animal: Animal -> animal.id == id })

    override fun findAnimals(name: String, size: Double?): Flux<Animal> =
        Flux.fromIterable(
            list.filter { animal: Animal -> (animal.name != null && animal.name.contains(name)) || animal.name == null }
                .filter { animal: Animal -> if (size != null) animal.size == size else true }
        )

    override fun findAllAnimals(): Flux<Animal> =
        Flux.fromArray(list)

    override fun addAnimal(name: String, size: Double?): Mono<Animal> =
        Mono.just(Animal("00011562", name, size))
}
