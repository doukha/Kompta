package fr.kompta

import org.junit.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals


class KomptaTest {

    val TJM = 560
    val nombreDeMois = 12
    val nombreMoyenDeJourTravailleParMois = 18

    val fraisBouffeParMoisEnMoyenne = 96
    val fraisTransportParMois = 66
    val fraisFreeElectriciteParMois = 144 / 3
    val deductionLoyerParMois = 334
    val fraisMutuelleSante = 120
    val fraisExpertComptableParMois = 96
    val fraisMaltMoyenne = (TJM * nombreMoyenDeJourTravailleParMois) * 0.05
    val fraisAcccessoirAnnuelle = 1000

    // TVA
    val tvaFreeElectriciteParMois = 25
    val tvaExpertComptable = 16
    val tvaMaltParMoisEnMoyenne = 100

    @Test
    fun `compute net should success`() {
        val fraisBouffe = fraisBouffeParMoisEnMoyenne * nombreDeMois
        val fraisTransport = fraisTransportParMois * nombreDeMois
        val fraisLogement = (deductionLoyerParMois + fraisFreeElectriciteParMois) * nombreDeMois
//        val fraisMutuelleSante = fraisMutuelleSante * nombreDeMois
        val fraisExpertComptable = fraisExpertComptableParMois * nombreDeMois
        val fraisMalt = fraisMaltMoyenne * nombreDeMois
        val deductionDeCharge = fraisBouffe + fraisTransport + fraisLogement +
//            fraisMutuelleSante +
                fraisExpertComptable + fraisMalt + fraisAcccessoirAnnuelle

        println(" nombre de mois $nombreDeMois")
        println(" charge annuelle bouffe $fraisBouffe")
        println(" charge annuelle transport $fraisTransport")
        println(" charge annuelle logement electricite $fraisLogement")
        println(" charge annuelle mutuelle sante $fraisMutuelleSante")
        println(" charge annuelle expertComptable $fraisExpertComptable")
        println(" charge annuelle malt $fraisMalt")
        println(" charge annuelle a deduire ==> $deductionDeCharge")


        val chiffreAffaireAnnuelleBrut = TJM * (nombreMoyenDeJourTravailleParMois * nombreDeMois)
        println("\n chiffre d'affaire annuelle $chiffreAffaireAnnuelleBrut")

        val revenusProfessionels = chiffreAffaireAnnuelleBrut - deductionDeCharge
        println("\n revenus professionnels ( chiffre d'affaire annuelle apres deduction de charge  ) $revenusProfessionels")

        val renumeration = revenusProfessionels / 1.47
        val chargesSociales = 0.47 * renumeration
        val revenusImposables = renumeration * 0.9
        val impot = appliquerLesTranchesSur(revenusImposables)
        println(" chargesSociales ${chargesSociales.roundToInt()}")
        println("\n rénumeration ${renumeration.roundToInt()}")
        println(" revenus imposables ( chiffre d'affaire apres abattement ) ${revenusImposables.roundToInt()}")
        println(" impot ==> $impot")


        val revenuesNet = (renumeration - impot) //+ (revenusImposables * 0.1)
        println(" revenue net annuel ${revenuesNet.roundToInt()}")
        println(" revenue net relatif par mois ${(revenuesNet / nombreDeMois).roundToInt()}")

        val depenseMensuelle = 1500 // loyer, bouffe, voyages
        val epargneAnnuelle = revenuesNet - (nombreDeMois * depenseMensuelle)
        println(" avec une depense mensuelle de $depenseMensuelle tu peux epargner " +
                "${epargneAnnuelle.roundToInt()}")

    }

    private fun appliquerLesTranchesSur(revenusImposables: Double): Int {
        // 2016
//        val debutTranche1 = 9700
//        val debutTranche2 = 26791
//        val debutTranche3 = 71826
//        val debutTranche3 = 152108
        // 2019
        val debutTranche1 = 9964
        val debutTranche2 = 27519
        val debutTranche3 = 73779
        val debutTranche4 = 152108

        val tranche1 = (debutTranche2 - debutTranche1) * 0.14
        val tranche2 = calculerTranche2(revenusImposables, debutTranche2, debutTranche3)
        val tranche3 = calculerTranche3(revenusImposables, debutTranche3, debutTranche4)
        val tranche4 = calculerTranche4(revenusImposables, debutTranche4)
        return (tranche1 + tranche2 + tranche3 + tranche4).roundToInt()
    }

    private fun calculerTranche2(revenusImposables: Double, debutTranche2: Int, debutTranche3: Int): Double {
        return when {
            (revenusImposables > debutTranche2.toDouble() && revenusImposables < debutTranche3.toDouble()) -> (revenusImposables - debutTranche2.toDouble()) * 0.3
            (revenusImposables > debutTranche3) -> (debutTranche3 - debutTranche2) * 0.3
            else -> 0.0
        }
    }

    private fun calculerTranche3(revenusImposables: Double, debutTranche3: Int, debutTranche4: Int): Double {
        return when {
            (revenusImposables > debutTranche3.toDouble() && revenusImposables < debutTranche4.toDouble()) -> (revenusImposables - debutTranche3.toDouble()) * 0.3
            (revenusImposables > debutTranche4) -> (debutTranche4 - debutTranche3) * 0.45
            else -> 0.0
        }
    }

    private fun calculerTranche4(revenusImposables: Double, debutTranche4: Int): Double {
        return if (revenusImposables > debutTranche4.toDouble()) {
            ((revenusImposables - debutTranche4) * 0.45)
        } else {
            0.0
        }
    }

    @Test
    fun `compute gain tva par mois`() {
        val tvaTotal = (tvaExpertComptable + tvaFreeElectriciteParMois + tvaMaltParMoisEnMoyenne) * nombreDeMois

        println(" gain tva au total $tvaTotal")
    }

}
