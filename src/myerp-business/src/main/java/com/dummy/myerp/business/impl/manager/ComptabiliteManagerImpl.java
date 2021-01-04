package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.TransactionStatus;
import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comptabilite manager implementation.
 */
public class ComptabiliteManagerImpl extends AbstractBusinessManager implements ComptabiliteManager {

    // ==================== Attributs ====================
    // ==================== Constructeurs ====================
    /**
     * Instantiates a new Comptabilite manager.
     */
    public ComptabiliteManagerImpl() {
    }

    // ==================== Getters/Setters ====================
    @Override
    public List<CompteComptable> getListCompteComptable() {
        return getDaoProxy().getComptabiliteDao().getListCompteComptable();
    }

    @Override
    public List<JournalComptable> getListJournalComptable() {
        return getDaoProxy().getComptabiliteDao().getListJournalComptable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EcritureComptable> getListEcritureComptable() {
        return getDaoProxy().getComptabiliteDao().getListEcritureComptable();
    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public synchronized void addReference(EcritureComptable pEcritureComptable) {
        // TODO à implémenter
        // Bien se réferer à la JavaDoc de cette méthode !
        /* Le principe :
        1.  Remonter depuis la persitance la dernière valeur de la séquence du journal pour l'année de l'écriture
        (table sequence_ecriture_comptable)
        2.  * S'il n'y a aucun enregistrement pour le journal pour l'année concernée :
        1. Utiliser le numéro 1.
         * Sinon :
        1. Utiliser la dernière valeur + 1
        3.  Mettre à jour la référence de l'écriture avec la référence calculée (RG_Compta_5)
        4.  Enregistrer (insert/update) la valeur de la séquence en persitance
        (table sequence_ecriture_comptable)
         */

        // 1
        JournalComptable journal = pEcritureComptable.getJournal();

        LocalDate localDate = pEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String vReference = null;

        SequenceEcritureComptable vNouvSequenceEcriture = new SequenceEcritureComptable();

        List<JournalComptable> listJournalComptable = getListJournalComptable();

        for (JournalComptable journalComptable : listJournalComptable) {

            if (journalComptable.getCode().equals(journal.getCode())) {

                List<SequenceEcritureComptable> vSequenceEcritureComptable = journalComptable.getSequenceEcritureComptable();

                int lastIndexOf = vSequenceEcritureComptable.size();

                // 2
                if (lastIndexOf == 0) {

                    // 3
                    vReference = journal.getCode() + "-" + localDate.getYear() + "/" + "00001";
                    pEcritureComptable.setReference(vReference);

                    vNouvSequenceEcriture.setAnnee(localDate.getYear());
                    vNouvSequenceEcriture.setDerniereValeur(00001);

                } else {

                    SequenceEcritureComptable vAncSequenceEcriture = vSequenceEcritureComptable.get(lastIndexOf - 1);

                    Integer annee = vAncSequenceEcriture.getAnnee();

                    if (annee == localDate.getYear()) {

                        Integer vDerniereValeur = vAncSequenceEcriture.getDerniereValeur();

                        vReference = journal.getCode() + "-" + localDate.getYear() + "/" + String.format("%05d", ++vDerniereValeur);

                        pEcritureComptable.setReference(vReference);

                        vNouvSequenceEcriture.setAnnee(localDate.getYear());
                        vNouvSequenceEcriture.setDerniereValeur(vDerniereValeur + 1);
                    } else {

                        pEcritureComptable.getDate();

                        vReference = journal.getCode() + "-" + localDate.getYear() + "/" + "00001";

                        pEcritureComptable.setReference(vReference);

                        vNouvSequenceEcriture.setAnnee(localDate.getYear());
                        vNouvSequenceEcriture.setDerniereValeur(1);

                    }
                }

                //TODO persitance SéquenceEcriture
                insertSequenceEcritureComptable(vNouvSequenceEcriture);

            }
        }

    }

    /**
     * {@inheritDoc}
     */
    // TODO à tester
    @Override
    public void checkEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptableUnit(pEcritureComptable);
        this.checkEcritureComptableContext(pEcritureComptable);
    }

    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion
     * unitaires, c'est à dire indépendemment du contexte (unicité de la
     * référence, exercie comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
     * règles de gestion
     */
    // TODO tests à compléter
    protected void checkEcritureComptableUnit(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== Vérification des contraintes unitaires sur les attributs de l'écriture
        Set<ConstraintViolation<EcritureComptable>> vViolations = getConstraintValidator().validate(pEcritureComptable);
        if (!vViolations.isEmpty()) {
            throw new FunctionalException("L'écriture comptable ne respecte pas les règles de gestion.",
                    new ConstraintViolationException(
                            "L'écriture comptable ne respecte pas les contraintes de validation",
                            vViolations));
        }

        // ===== RG_Compta_2 : Pour qu'une écriture comptable soit valide, elle doit être équilibrée
        if (!pEcritureComptable.isEquilibree()) {
            throw new FunctionalException("L'écriture comptable n'est pas équilibrée.");
        }

        // ===== RG_Compta_3 : une écriture comptable doit avoir au moins 2 lignes d'écriture (1 au débit, 1 au crédit)
        if (pEcritureComptable.getListLigneEcriture().size() < 2) {

            // l'écriture comptable doit contenir 2 lignes
            throw new FunctionalException(
                    "L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }

        int vNbrCreditEcritureComptable = 0;
        int vNbrDebitEcritureComptable = 0;

        for (LigneEcritureComptable vLigneEcritureComptable : pEcritureComptable.getListLigneEcriture()) {

            int vNbrCredit = 0;
            int vNbrDebit = 0;

            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getCredit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrCredit++;

            }

            if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(vLigneEcritureComptable.getDebit(),
                    BigDecimal.ZERO)) != 0) {
                vNbrDebit++;

            }

            if (vNbrCredit == 0 && vNbrDebit == 0) {

                // une ligne avec que des null
                throw new FunctionalException(
                        "A L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
            }

            if (vNbrCredit == 1 && vNbrDebit == 1) {

                // une ligne avec un credit et un debit
                throw new FunctionalException(
                        " B L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
            }

            vNbrCreditEcritureComptable = +vNbrCredit;
            vNbrDebitEcritureComptable = +vNbrDebit;

        }
        // On test le nombre de lignes car si l'écriture à une seule ligne
        //      avec un montant au débit et un montant au crédit ce n'est pas valable

        if (vNbrCreditEcritureComptable >= 2 || vNbrDebitEcritureComptable >= 2) {

            // 2 lignes avec soit 2 credits ou soit 2 debits
            throw new FunctionalException(
                    "C L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
        }
        // RG_Compta_5 : Format et contenu de la référence
        // vérifier que l'année dans la référence correspond bien à la date de l'écriture, idem pour le code journal...

        LocalDate localDate = pEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // vérif année ref avec année écriture
        Pattern pattern = Pattern.compile("-" + localDate.getYear());
        Matcher matcher = pattern.matcher(pEcritureComptable.getReference());

        if (!matcher.find()) {

            throw new FunctionalException("L'année de la date de l'écriture comptable ne correspond pas à l'année dans la référence.");

        }

        // vérif code ref avec code journal
        pattern = Pattern.compile(pEcritureComptable.getJournal().getCode() + "-");
        matcher = pattern.matcher(pEcritureComptable.getReference());

        if (!matcher.find()) {

            throw new FunctionalException("Le code du journal de l'écriture comptable ne correspond pas au code dans la référence.");

        }

    }

    /**
     * Vérifie que l'Ecriture comptable respecte les règles de gestion liées au
     * contexte (unicité de la référence, année comptable non cloturé...)
     *
     * @param pEcritureComptable -
     * @throws FunctionalException Si l'Ecriture comptable ne respecte pas les
     * règles de gestion
     */
    protected void checkEcritureComptableContext(EcritureComptable pEcritureComptable) throws FunctionalException {
        // ===== RG_Compta_6 : La référence d'une écriture comptable doit être unique
        if (StringUtils.isNoneEmpty(pEcritureComptable.getReference())) {
            try {
                // Recherche d'une écriture ayant la même référence  
                EcritureComptable vECRef = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef(
                        pEcritureComptable.getReference());

                // Si l'écriture à vérifier est une nouvelle écriture (id == null),
                // ou si elle ne correspond pas à l'écriture trouvée (id != idECRef),
                // c'est qu'il y a déjà une autre écriture avec la même référence
                if (pEcritureComptable.getId() == null
                        || !pEcritureComptable.getId().equals(vECRef.getId())) {
                    throw new FunctionalException("Une autre écriture comptable existe déjà avec la même référence.");
                }
            } catch (NotFoundException vEx) {
                // Dans ce cas, c'est bon, ça veut dire qu'on n'a aucune autre écriture avec la même référence.
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        this.checkEcritureComptable(pEcritureComptable);
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEcritureComptable(EcritureComptable pEcritureComptable) throws FunctionalException {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().updateEcritureComptable(pEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEcritureComptable(Integer pId) {
        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().deleteEcritureComptable(pId);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Override
    public void insertSequenceEcritureComptable(SequenceEcritureComptable sequenceEcritureComptable) {

        TransactionStatus vTS = getTransactionManager().beginTransactionMyERP();
        try {
            getDaoProxy().getComptabiliteDao().insertSequenceEcritureComptable(sequenceEcritureComptable);
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }
}
