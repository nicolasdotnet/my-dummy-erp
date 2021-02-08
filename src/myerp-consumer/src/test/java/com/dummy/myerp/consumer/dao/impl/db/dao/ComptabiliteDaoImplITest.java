/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.consumer.dao.impl.db.dao;

import static com.dummy.myerp.consumer.ConsumerHelper.getDaoProxy;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author nicolasdotnet
 */
public class ComptabiliteDaoImplITest {

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Before
    public void init() {

        try (ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("com/dummy/myerp/consumer/applicationContext.xml")) {

            DataSourcesEnum compteComponant = context.getBean(DataSourcesEnum.class);

            System.out.println(compteComponant);

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

    }

    /**
     * Test of getListCompteComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testGetListCompteComptable() {

        System.out.println("getListCompteComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();

        int expResult = 7;
        List<CompteComptable> result = instance.getListCompteComptable();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of getListJournalComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testGetListJournalComptable() {
        System.out.println("getListJournalComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        int expResult = 4;
        List<JournalComptable> result = instance.getListJournalComptable();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getListEcritureComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testGetListEcritureComptable() {
        System.out.println("getListEcritureComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        int expResult = 5;
        List<EcritureComptable> result = instance.getListEcritureComptable();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getEcritureComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testGetEcritureComptable() throws Exception {
        System.out.println("getEcritureComptable");

        Integer pId = -1;
        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        EcritureComptable expResult = new EcritureComptable();
        expResult.setId(pId);
        EcritureComptable result = instance.getEcritureComptable(pId);
        assertEquals(expResult.getId(), result.getId());

    }

    /**
     * Test of getEcritureComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testThrowsGetEcritureComptable() throws Exception {
        System.out.println("getEcritureComptable");

        Integer pId = -0;
        thrownException.expect(NotFoundException.class);
        thrownException.expectMessage(containsString("EcritureComptable non trouvée : id=" + pId));

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        EcritureComptable expResult = new EcritureComptable();
        expResult.setId(pId);
        EcritureComptable result = instance.getEcritureComptable(pId);

    }

    /**
     * Test of getEcritureComptableByRef method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testGetEcritureComptableByRef() throws Exception {
        System.out.println("getEcritureComptableByRef");

        String pReference = "VE-2016/00004";
        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        EcritureComptable expResult = new EcritureComptable();
        expResult.setReference(pReference);
        EcritureComptable result = instance.getEcritureComptableByRef(pReference);
        assertEquals(expResult.getReference(), result.getReference());
    }

    /**
     * Test of getEcritureComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testThrowsGetEcritureComptableByRef() throws Exception {
        System.out.println("getEcritureComptable");

        String pReference = "AC-2011/00001";
        thrownException.expect(NotFoundException.class);
        thrownException.expectMessage(containsString("EcritureComptable non trouvée : reference=" + pReference));

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        EcritureComptable expResult = new EcritureComptable();
        expResult.setReference(pReference);
        EcritureComptable result = instance.getEcritureComptableByRef(pReference);

    }

    /**
     * Test of loadListLigneEcriture method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testLoadListLigneEcriture() {
        System.out.println("loadListLigneEcriture");

        EcritureComptable pEcritureComptable = new EcritureComptable();
        pEcritureComptable.setId(-1);
        int expResult = 3;

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        instance.loadListLigneEcriture(pEcritureComptable);
        assertEquals(expResult, pEcritureComptable.getListLigneEcriture().size());
    }

    /**
     * Test of insertEcritureComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testInsertEcritureComptable() {
        System.out.println("insertEcritureComptable");

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("BQ");
        vJournalComptable.setLibelle("Achat");

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());

        LocalDate date = LocalDate.now();
        String pReference = "BQ-" + date.getYear() + "/00001";
        vEcritureComptable.setReference(pReference);

        vEcritureComptable.setLibelle("Fake achat");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                "Fake Fournisseur A", new BigDecimal(10),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4456),
                "TVA 20%", new BigDecimal(2),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                "Fake Paiement facture 1", null,
                new BigDecimal(12)));

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        instance.insertEcritureComptable(vEcritureComptable);

    }

    /**
     * Test of deleteListLigneEcritureComptable method, of class
     * ComptabiliteDaoImpl.
     */
    @Test
    public void testDeleteListLigneEcritureComptable() {
        System.out.println("deleteListLigneEcritureComptable");

        Integer pEcritureId = -2;
        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        instance.deleteListLigneEcritureComptable(pEcritureId);
    }

    /**
     * Test of updateEcritureComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testUpdateEcritureComptable() throws NotFoundException {
        System.out.println("updateEcritureComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        EcritureComptable vEcritureComptable;

        LocalDate date = LocalDate.now();
        String pReference = "BQ-" + date.getYear() + "/00001";

        vEcritureComptable = instance.getEcritureComptableByRef(pReference);
        vEcritureComptable.setLibelle("Paiement Facture C110002 -> update");

        instance.updateEcritureComptable(vEcritureComptable);

        EcritureComptable vEcritureComptableFind = instance.getEcritureComptableByRef(pReference);
        assertEquals("Paiement Facture C110002 -> update", vEcritureComptableFind.getLibelle());
    }

    /**
     * Test of deleteEcritureComptable method, of class ComptabiliteDaoImpl.
     */
    @Test
    public void testDeleteEcritureComptable() throws NotFoundException {
        System.out.println("deleteEcritureComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        EcritureComptable vEcritureComptable;
        LocalDate date = LocalDate.now();
        String pReference = "BQ-" + date.getYear() + "/00001";
        vEcritureComptable = instance.getEcritureComptableByRef(pReference);

        instance.deleteEcritureComptable(vEcritureComptable.getId());

    }

    /**
     * Test of getSequenceByYearandJournalCode method, of class
     * ComptabiliteDaoImpl.
     */
    @Test
    public void testGetSequenceByYearandJournalCode() throws Exception {
        System.out.println("getSequenceByYearandJournalCode");

        Integer pAnnee = 2016;
        String pJournalCode = "BQ";
        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();

        SequenceEcritureComptable expResult = new SequenceEcritureComptable();
        expResult.setAnnee(2016);
        expResult.setDerniereValeur(51);
        expResult.setJournalCode("BQ");

        SequenceEcritureComptable result = instance.getSequenceByYearandJournalCode(pAnnee, pJournalCode);
        assertEquals(expResult, result);

    }

    /**
     * Test of insertSequenceEcritureComptable method, of class
     * ComptabiliteDaoImpl.
     */
    @Test
    public void testInsertSequenceEcritureComptable() throws Exception {
        System.out.println("insertSequenceEcritureComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setJournalCode("OD");
        sequenceEcritureComptable.setDerniereValeur(42);

        LocalDate date = LocalDate.now();
        sequenceEcritureComptable.setAnnee(date.getYear());

        instance.insertSequenceEcritureComptable(sequenceEcritureComptable);

        SequenceEcritureComptable sec = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(date.getYear(), "OD");
        assertEquals(42, (int) sec.getDerniereValeur());

    }

    /**
     * Test of updateSequenceEcritureComptable method, of class
     * ComptabiliteDaoImpl.
     */
    @Test
    public void testUpdateSequenceEcritureComptable() throws Exception {
        System.out.println("updateSequenceEcritureComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();

        LocalDate date = LocalDate.now();
        int pAnnee = date.getYear();
        String pJournalCode = "OD";

        SequenceEcritureComptable pNouvSequenceEcriture = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(pAnnee, pJournalCode);
        pNouvSequenceEcriture.setDerniereValeur(43);

        instance.updateSequenceEcritureComptable(pNouvSequenceEcriture);

        SequenceEcritureComptable sec = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(pAnnee, pJournalCode);
        assertEquals(43, (int) sec.getDerniereValeur());
    }

    /**
     * Test of deleteSequenceEcritureComptable method, of class
     * ComptabiliteDaoImpl.
     */
    @Test
    public void testDeleteSequenceEcrituree() {
        System.out.println("deleteSequenceEcriture");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();

        SequenceEcritureComptable sec = new SequenceEcritureComptable();
        sec.setJournalCode("OD");
        LocalDate date = LocalDate.now();
        sec.setAnnee(date.getYear());

        instance.deleteSequenceEcritureComptable(sec);

    }

}
