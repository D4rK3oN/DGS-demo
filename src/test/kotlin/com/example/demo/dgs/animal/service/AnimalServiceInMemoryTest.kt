package com.example.demo.dgs.animal.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AnimalServiceInMemoryTest {

    @InjectMocks
    lateinit var animalService: AnimalServiceInMemory

    @Test
    fun getById_whenIdNotExist_thenReturnEmpty() =
        assertTrue {
            animalService.getById("99")
                .blockOptional()
                .isEmpty
        }

    @Test
    fun getById_whenIdExist_thenReturnAnimal() {
        val response = animalService.getById("05")
            .blockOptional()

        assertAll(
            { assertFalse { response.isEmpty } },
            { assertEquals("05", response.get().id) },
            { assertEquals("Misi", response.get().name) },
            { assertEquals(3.0, response.get().size) },
            { assertNull(response.get().sizeText) }
        )
    }
}
