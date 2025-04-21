package crosemont.dti.tictactoe.vue


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import crosemont.dti.tictactoe.R
import crosemont.dti.tictactoe.databinding.ActivityMainBinding
import crosemont.dti.tictactoe.vuemodele.JeuViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var boutons: List<android.widget.Button>
    private val jeuViewModel: JeuViewModel by viewModels()
    //singleton, pour accéder à un activity instance globalement
    companion object {
        lateinit var instance: MainActivity
            private set
    }

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boutons = listOf(binding.case0, binding.case1, binding.case2 ,binding.case3,
            binding.case4, binding.case5, binding.case6, binding.case7, binding.case8)

        for ((index, btn) in boutons.withIndex()) {
            btn.setOnClickListener {
                Log.i(TAG, "Case cliquée: $index")
                jeuViewModel.jouerCoup(index)
            }
        }

        binding.btnReinitialiser.setOnClickListener {
            Log.i(TAG, "Bouton réinitialiser cliqué")
            jeuViewModel.reinitialiserJeu()
        }

        jeuViewModel.grille.observe(this, Observer { grille ->
            Log.i(TAG, "Changement d'état - grille: $grille")

            for ((index, btn) in boutons.withIndex()) {
                btn.text = grille[index]?.toString() ?: ""
            }
        })

        jeuViewModel.joueurActuel.observe(this, Observer { joueur ->
            Log.i(TAG, "Changement d'état - joueur actuel: ${joueur}")

            binding.tvEtatJeu.text = getString(R.string.tourJoueurs, joueur.nom, joueur.symbole)
            basculerÉtatBoutons(actif = joueur.estHumain)

            if(!joueur.estHumain) {
                lifecycleScope.launch {
                    delay(500) // Délai pour donner l'impression que le bot réfléchit!
                    jeuViewModel.jouerCoup(joueur.demanderCoup(jeuViewModel.grille.value!!))
                }
            }
        })

        jeuViewModel.gagnant.observe(this, Observer { gagnant ->
            Log.i(TAG, "Changement d'état - gagnant: ${gagnant}")

            gagnant?.let {
                binding.tvEtatJeu.text = getString(R.string.gagnant, gagnant.nom)
            }
        })

        jeuViewModel.partieTerminée.observe(this, Observer { partieTerminée ->
            Log.i(TAG, "Changement d'état - partie terminée: ${partieTerminée}")

            if (partieTerminée) {
                if (jeuViewModel.gagnant.value == null) {
                    binding.tvEtatJeu.text = getString(R.string.match_nul)
                }
            }
            basculerÉtatBoutons(actif = !partieTerminée)
        })

        //changement de couleur des cases gagnantes
        jeuViewModel.combinaisonGagnante.observe(this, Observer { combinaisonGagnante ->
            if (combinaisonGagnante != null) {
                for (i in combinaisonGagnante) {
                    boutons[i].setTextAppearance(R.style.caseTicTacToe_Winning)
                }
            } else {
                //reinitialiser les couleurs
                for (button in boutons) {
                    for (button in boutons) {
                        button.setTextAppearance(R.style.caseTicTacToe)
                    }
                }
            }
        })

        //afficher message d'erreur
        jeuViewModel.erreurMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun basculerÉtatBoutons(actif: Boolean) {
        for (btn in boutons) {
            btn.isEnabled = actif
        }
    }
}