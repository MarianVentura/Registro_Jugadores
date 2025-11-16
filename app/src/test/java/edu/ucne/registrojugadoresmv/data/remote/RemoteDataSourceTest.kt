package edu.ucne.registrojugadoresmv.data.remote

import edu.ucne.registrojugadoresmv.data.remote.dto.MovimientoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class RemoteDataSourceTest {

    private lateinit var TicTacToeApi: TicTacToeApi
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        TicTacToeApi = mockk()
        remoteDataSource = RemoteDataSource(TicTacToeApi)
    }

    @Test
    fun `getMovimientos should return list of movimientos when api call is successful`() = runTest {
        val partidaId = 1
        val expectedMovimientos = listOf(
            MovimientoDto(
                partidaId = 1,
                jugador = "X",
                posicionFila = 0,
                posicionColumna = 0
            ),
            MovimientoDto(
                partidaId = 1,
                jugador = "O",
                posicionFila = 1,
                posicionColumna = 1
            )
        )

        coEvery { TicTacToeApi.getMovimientos(partidaId) } returns expectedMovimientos

        val result = remoteDataSource.getMovimientos(partidaId)

        assertEquals(expectedMovimientos, result)
        assertEquals(2, result.size)
        assertEquals("X", result[0].jugador)
        assertEquals("O", result[1].jugador)

        coVerify(exactly = 1) { TicTacToeApi.getMovimientos(partidaId) }
    }

    @Test
    fun `getMovimientos should return empty list when no movimientos exist`() = runTest {
        val partidaId = 999
        val emptyList = emptyList<MovimientoDto>()

        coEvery { TicTacToeApi.getMovimientos(partidaId) } returns emptyList

        val result = remoteDataSource.getMovimientos(partidaId)

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { TicTacToeApi.getMovimientos(partidaId) }
    }

    @Test
    fun `getMovimientos should throw exception when api call fails`() = runTest {
        val partidaId = 1
        val exception = Exception("Network error")

        coEvery { TicTacToeApi.getMovimientos(partidaId) } throws exception

        try {
            remoteDataSource.getMovimientos(partidaId)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            assertEquals("Network error", e.message)
        }

        coVerify(exactly = 1) { TicTacToeApi.getMovimientos(partidaId) }
    }

    @Test
    fun `saveMovimiento should call api successfully`() = runTest {
        val movimiento = MovimientoDto(
            partidaId = 1,
            jugador = "X",
            posicionFila = 2,
            posicionColumna = 2
        )

        coEvery { TicTacToeApi.saveMovimiento(movimiento) } returns Unit

        remoteDataSource.saveMovimiento(movimiento)

        coVerify(exactly = 1) { TicTacToeApi.saveMovimiento(movimiento) }
    }

    @Test
    fun `saveMovimiento should throw exception when api call fails`() = runTest {
        val movimiento = MovimientoDto(
            partidaId = 1,
            jugador = "X",
            posicionFila = 0,
            posicionColumna = 0
        )
        val exception = Exception("Failed to save")

        coEvery { TicTacToeApi.saveMovimiento(movimiento) } throws exception

        try {
            remoteDataSource.saveMovimiento(movimiento)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            assertEquals("Failed to save", e.message)
        }

        coVerify(exactly = 1) { TicTacToeApi.saveMovimiento(movimiento) }
    }

    @Test
    fun `getMovimientos should handle multiple partidaIds correctly`() = runTest {
        val partidaId1 = 1
        val partidaId2 = 2

        val movimientos1 = listOf(
            MovimientoDto(partidaId = 1, jugador = "X", posicionFila = 0, posicionColumna = 0)
        )
        val movimientos2 = listOf(
            MovimientoDto(partidaId = 2, jugador = "O", posicionFila = 1, posicionColumna = 1)
        )

        coEvery { TicTacToeApi.getMovimientos(partidaId1) } returns movimientos1
        coEvery { TicTacToeApi.getMovimientos(partidaId2) } returns movimientos2

        val result1 = remoteDataSource.getMovimientos(partidaId1)
        val result2 = remoteDataSource.getMovimientos(partidaId2)

        assertEquals(1, result1.size)
        assertEquals(1, result2.size)
        assertEquals(partidaId1, result1[0].partidaId)
        assertEquals(partidaId2, result2[0].partidaId)

        coVerify(exactly = 1) { TicTacToeApi.getMovimientos(partidaId1) }
        coVerify(exactly = 1) { TicTacToeApi.getMovimientos(partidaId2) }
    }
}