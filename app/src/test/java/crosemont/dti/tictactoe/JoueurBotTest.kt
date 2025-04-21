import crosemont.dti.tictactoe.modele.JoueurBot
import crosemont.dti.tictactoe.modele.Plateau
import crosemont.dti.tictactoe.modele.StrategieSimple
import crosemont.dti.tictactoe.modele.intelligentBot
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test

class JoueurBotTest {

    private lateinit var joueur: JoueurBot
    private val strategie = StrategieSimple()

    @BeforeTest
    fun setup() {
        joueur = JoueurBot("Bot", 'O', strategie)
    }

    @Test
    fun `test demander un coup dans une grille vide`() {
        val grille = Plateau().grille
        val index = joueur.demanderCoup(grille)
        assertTrue(index in grille.indices)
    }

    @Test
    fun `test demander un coup quand il reste une case libre`() {
        val indexCaseLibre = 3
        val grille = List(9) { if (it == indexCaseLibre) null else 'X' }
        val index = joueur.demanderCoup(grille)
        assertEquals(index, indexCaseLibre)
    }


    @Test
    fun `test gagner quand il reste une case libre`() {
        val grille = listOf(
            'O', 'O', null,
            null, null, null,
            null, null, null
        )
        val bot = intelligentBot("Bot", 'O', strategie)
        val result = bot.demanderCoup(grille)
        assertEquals(2, result)
    }

    @Test
    fun `test pour que le bot bloque l'adversaire quand il reste une case libre`() {
        val grille = listOf(
            'X', 'X', null,
            null, null, null,
            null, null, null
        )
        val bot = intelligentBot("Bot", 'O', strategie)
        val result = bot.demanderCoup(grille)
        assertEquals(2, result)
    }

    @Test
    fun `test pour jouer normale`() {
        val grille = listOf(
            'X', 'O', 'X',
            'X', 'O', null,
            'O', 'X', null
        )
        val bot = intelligentBot("Bot", 'O', strategie)
        val result = bot.demanderCoup(grille)
        assertTrue(result == 5 || result == 8)
    }

    @Test
    fun `test pour que le bot gagne diagonalement`() {
        val grille = listOf(
            'X', null, 'O',
            null, null, null,
            'O', null, 'X'
        )
        val bot = intelligentBot("Bot", 'O', strategie)
        val result = bot.demanderCoup(grille)
        assertEquals(4, result)
    }

    @Test
    fun `test pour que le bot bloque l'adversaire diagonalement`() {
        val grille = listOf(
            'X', null, 'O',
            'O', 'X', null,
            'X', null, null
        )
        val bot = intelligentBot("Bot", 'O', strategie)
        val result = bot.demanderCoup(grille)
        assertEquals(8, result)
    }
}