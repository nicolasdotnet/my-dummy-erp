package com.dummy.myerp.model.bean.comptabilite;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;

public class EcritureComptableTest {

    // un ensemble de ligne d'écriture constitut une écriture comptable
    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                vLibelle,
                vDebit, vCredit);
        return vRetour;
    }

    /**
     * Test of getTotalDebit method, of class EcritureComptable.
     */
    @Test
    public void testGetTotalDebit() {

        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, null));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40.01", null));

        Assert.assertEquals(new BigDecimal("341.01"), vEcriture.getTotalDebit());

    }

    /**
     * Test of getTotalCredit method, of class EcritureComptable.
     */
    @Test
    public void testGetTotalCredit() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.getListLigneEcriture().add(this.createLigne(1, null, null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, null, "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "7.01"));

        Assert.assertEquals(new BigDecimal("341.01"), vEcriture.getTotalCredit());

    }

    /**
     * Test of isEquilibree method, of class EcritureComptable.
     */
    @Test
    public void testIsEquilibree() {
        EcritureComptable vEcriture;
        vEcriture = new EcritureComptable();

        vEcriture.setLibelle("Equilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "40.01", "7.01"));
        Assert.assertTrue(vEcriture.toString(), vEcriture.isEquilibree());

        vEcriture.getListLigneEcriture().clear();
        vEcriture.setLibelle("Non équilibrée");
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcriture.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcriture.getListLigneEcriture().add(this.createLigne(2, "1.01", "2.01"));
        Assert.assertFalse(vEcriture.toString(), vEcriture.isEquilibree());
    }

}
