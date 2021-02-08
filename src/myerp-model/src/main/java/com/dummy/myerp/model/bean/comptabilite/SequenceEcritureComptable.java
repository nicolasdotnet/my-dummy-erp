package com.dummy.myerp.model.bean.comptabilite;

import java.util.Objects;

/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    /**
     * Le code journal
     */
    private String journalCode;

    /**
     * L'année
     */
    private Integer annee;
    /**
     * La dernière valeur utilisée
     */
    private Integer derniereValeur;

    // ==================== Constructeurs ====================
    /**
     * Constructeur
     */
    public SequenceEcritureComptable() {
    }

    /**
     * Constructeur
     *
     * @param pAnnee -
     * @param pDerniereValeur -
     */
    public SequenceEcritureComptable(Integer pAnnee, Integer pDerniereValeur) {
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
    }

    // ==================== Getters/Setters ====================

    public String getJournalCode() {
        return journalCode;
    }

    public void setJournalCode(String journalCode) {
        this.journalCode = journalCode;
    }
    
    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer pAnnee) {
        annee = pAnnee;
    }

    public Integer getDerniereValeur() {
        return derniereValeur;
    }

    public void setDerniereValeur(Integer pDerniereValeur) {
        derniereValeur = pDerniereValeur;
    }

    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final StringBuilder vStB = new StringBuilder(this.getClass().getSimpleName());
        final String vSEP = ", ";
        vStB.append("{")
                .append("journalCode=").append(journalCode)
                .append("annee=").append(annee)
                .append(vSEP).append("derniereValeur=").append(derniereValeur)
                .append("}");
        return vStB.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.journalCode);
        hash = 29 * hash + Objects.hashCode(this.annee);
        hash = 29 * hash + Objects.hashCode(this.derniereValeur);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SequenceEcritureComptable other = (SequenceEcritureComptable) obj;
        if (!Objects.equals(this.journalCode, other.journalCode)) {
            return false;
        }
        if (!Objects.equals(this.annee, other.annee)) {
            return false;
        }
        if (!Objects.equals(this.derniereValeur, other.derniereValeur)) {
            return false;
        }
        return true;
    }
    
    
}
