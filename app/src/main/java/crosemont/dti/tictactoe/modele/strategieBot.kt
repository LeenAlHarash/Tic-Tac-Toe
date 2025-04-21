package crosemont.dti.tictactoe.modele

interface strategieBot {
    fun attaquer(grille: List<Char?>, symbole: Char) : Int
}