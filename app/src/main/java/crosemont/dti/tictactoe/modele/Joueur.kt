package crosemont.dti.tictactoe.modele

abstract class Joueur(val nom: String, val symbole: Char) {
    abstract val estHumain: Boolean

    abstract fun demanderCoup(grille: List<Char?>): Int

    override fun toString(): String {
        return "Joueur(nom: $nom, symbole: $symbole)"
    }
}

class JoueurHumain(nom: String, symbole: Char) : Joueur(nom, symbole) {
    override val estHumain = true

    override fun demanderCoup(grille: List<Char?>): Int {
        throw Exception("Le joueur  humain joue à partir de l'interface graphique.")
    }
}

//Bot aléatoire
class JoueurBot(nom: String, symbole: Char) : Joueur(nom, symbole) {
    override val estHumain = false

    override fun demanderCoup(grille: List<Char?>): Int {
        val indexValides = grille.mapIndexedNotNull() { i, symbole ->
            if (symbole == null) i else null
        }
        return indexValides.random()
    }
}

//Bot intelligent
class intelligentBot(nom: String, symbole: Char, val intelligence: strategieBot) : Joueur(nom, symbole) {
    override val estHumain = false

    override fun demanderCoup(grille: List<Char?>) : Int {
        return intelligence.attaquer(grille, symbole)
    }
}

class StrategieSimple : strategieBot {
    //appele de l'interface & plateau.combinaisonsGagnantes
    override fun attaquer(grille: List<Char?>, symbole: Char): Int {
        val combinaisonsGagnantes = Plateau.COMBINAISONS_GAGNANTES
        val adversaire = if (symbole == 'X') 'O' else 'X'

        //essayer de gagner
        for (combo in combinaisonsGagnantes) {
            val (a, b, c) = combo
            val cases = listOf(grille[a], grille[b], grille[c])
            if (cases.count { it == symbole } == 2 && cases.count { it == null } == 1) {
                return combo[cases.indexOf(null)]
            }
        }

        //bloquer l’adversaire
        for (combo in combinaisonsGagnantes) {
            val (a, b, c) = combo
            val cases = listOf(grille[a], grille[b], grille[c])
            if (cases.count { it == adversaire } == 2 && cases.count { it == null } == 1) {
                return combo[cases.indexOf(null)]
            }
        }

        //jouer normalement
        val coupsPossibles = grille.mapIndexedNotNull { i, case -> if (case == null) i else null }
        return coupsPossibles.random()
    }
}