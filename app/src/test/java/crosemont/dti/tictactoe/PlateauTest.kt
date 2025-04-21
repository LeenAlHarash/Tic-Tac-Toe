import crosemont.dti.tictactoe.modele.Plateau
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class PlateauTest {
    private lateinit var plateau: Plateau

    @BeforeTest
    fun setup() {
        plateau = Plateau()
    }

    @Test
    fun `test la classe plateau existe`() {
        assertNotNull(plateau)
    }

    @Test
    fun `test affichage initial de la grille`() {
        assertEquals(
            "0 | 1 | 2\n" +
                    "---------\n" +
                    "3 | 4 | 5\n" +
                    "---------\n" +
                    "6 | 7 | 8", plateau.toString()
        )
    }

    @Test
    fun `test jouer un coup valide`() {
        plateau.jouerCoup(4, 'X')
        assertEquals(
            "0 | 1 | 2\n" +
                    "---------\n" +
                    "3 | X | 5\n" +
                    "---------\n" +
                    "6 | 7 | 8", plateau.toString()
        )
    }

    @Test
    fun `test jouer un coup dans une case déjà occupée lance une exception`() {
        plateau.jouerCoup(4, 'X')
        assertThrows(Plateau.CaseOccupéeException::class.java) {
            plateau.jouerCoup(4, 'O')
        }
    }
    @Test
    fun `test jouer un coup dans une case inexistante lance une exception`() {
        assertThrows(Plateau.CaseInexistanteException::class.java) {
            plateau.jouerCoup(9, 'X')
        }
    }

    @Test
    fun `test victoire par ligne`() {
        plateau.jouerCoup(0, 'X')
        plateau.jouerCoup(1, 'X')
        plateau.jouerCoup(2, 'X')
        assertTrue(plateau.vérifierVictoire('X'))
    }

    @Test
    fun `test victoire par colonne`() {
        plateau.jouerCoup(0, 'O')
        plateau.jouerCoup(3, 'O')
        plateau.jouerCoup(6, 'O')
        assertTrue(plateau.vérifierVictoire('O'))
    }

    @Test
    fun `test victoire par diagonale`() {
        plateau.jouerCoup(0, 'X')
        plateau.jouerCoup(4, 'X')
        plateau.jouerCoup(8, 'X')
        assertTrue(plateau.vérifierVictoire('X'))
    }

    @Test
    fun `test match nul`() {
        val coups = charArrayOf('X', 'O', 'X', 'X', 'O', 'X', 'O', 'X', 'O' )
        for ((index, coup) in coups.withIndex()) {
            plateau.jouerCoup(index, coup)
        }
        assertEquals(
            "X | O | X\n" +
                    "---------\n" +
                    "X | O | X\n" +
                    "---------\n" +
                    "O | X | O", plateau.toString()
        )
        assertTrue(plateau.estPlein())
        assertFalse(plateau.vérifierVictoire('X'))
        assertFalse(plateau.vérifierVictoire('O'))
    }


}
