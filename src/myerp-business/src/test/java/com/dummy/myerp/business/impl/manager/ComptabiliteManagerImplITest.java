/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.business.impl.manager;

import static com.dummy.myerp.consumer.ConsumerHelper.getDaoProxy;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author nicolasdotnet
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComptabiliteManagerImplITest {

    @Before
    public void init() {

        try (ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("com/dummy/myerp/business/applicationContext.xml")) {

        } catch (Exception e) {
            
             System.out.println(e.getMessage());

        }

    }

    /**
     * Test of getListCompteComptable method, of class ComptabiliteManagerImpl.
     */
    @Test
    public void testGetListCompteComptable() {
        System.out.println("getListCompteComptable");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();
        int expResult = 7;
        List<CompteComptable> result = instance.getListCompteComptable();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getListJournalComptable method, of class ComptabiliteManagerImpl.
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
     * Test of getListEcritureComptable method, of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void testGetListEcritureComptable() {
        System.out.println("getListEcritureComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();
        int expResult = 6;
        List<EcritureComptable> result = instance.getListEcritureComptable();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of insertEcritureComptable method, of class ComptabiliteManagerImpl.
     */
    @Test
    public void testAddEcritureComptable() throws Exception {
        System.out.println("addEcritureComptable");

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("VE");
        vJournalComptable.setLibelle("Vente");

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);

        vEcritureComptable.setDate(new Date());

        LocalDate date = LocalDate.now();
        String pReference = "VE-" + date.getYear() + "/00001";
        vEcritureComptable.setReference(pReference);

        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();
        instance.insertEcritureComptable(vEcritureComptable);

        EcritureComptable eb = getDaoProxy().getComptabiliteDao()
                .getEcritureComptableByRef("VE-" + date.getYear() + "/00001");

        assertEquals("VE-" + date.getYear() + "/00001", eb.getReference());

    }

    /**
     * Test of updateEcritureComptable method, of class ComptabiliteManagerImpl.
     */
    @Test
    public void testModifyEcritureComptable() throws Exception {
        System.out.println("modifyEcritureComptable");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();

        LocalDate localDate = LocalDate.now();
        EcritureComptable eb = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef("VE-" + localDate.getYear() + "/00001");
        assertEquals("VE-" + localDate.getYear() + "/00001", eb.getReference());

        String libelleUpdate = "LibelleUpdate";
        eb.setLibelle(libelleUpdate);
        instance.updateEcritureComptable(eb);

        assertEquals(libelleUpdate, eb.getLibelle());

    }

    /**
     * Test of deleteEcritureComptable method, of class ComptabiliteManagerImpl.
     */
    @Test
    public void testRemoveEcritureComptable() throws Exception {
        System.out.println("removeEcritureComptable");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();

        LocalDate localDate = LocalDate.now();
        EcritureComptable eb = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef("VE-" + localDate.getYear() + "/00001");
        Integer pId = eb.getId();
        
        instance.deleteEcritureComptable(pId);

    }

    /**
     * Test of addReference method (step : i=1), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void testAddReferenceFirst() throws Exception {
        System.out.println("addReference");

        LocalDate date = LocalDate.now();
        Date d = new Date();
        d.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

        EcritureComptable pEcritureComptable = new EcritureComptable();
        pEcritureComptable.setJournal(vJournalComptable);
        pEcritureComptable.setDate(d);

        pEcritureComptable.setLibelle("ReferenceInitial");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();
        instance.addReference(pEcritureComptable);

        SequenceEcritureComptable sec = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(date.getYear(), "AC");
        assertEquals(1, (int) sec.getDerniereValeur());

    }

    /**
     * Test of addReference method (step : i++), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void testAddReferenceSecondAndMore() throws Exception {
        System.out.println("addReference");

        LocalDate date = LocalDate.now();
        Date d = new Date();
        d.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        
        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

        EcritureComptable pEcritureComptable = new EcritureComptable();
        pEcritureComptable.setJournal(vJournalComptable);
        pEcritureComptable.setDate(d);

        pEcritureComptable.setLibelle("ReferenceIncrement");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();
        instance.addReference(pEcritureComptable);

        SequenceEcritureComptable sec = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode( date.getYear(), "AC");
        assertEquals(2, (int) sec.getDerniereValeur());
        
        getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(sec);

    }

    /**
     * Test of insertSequenceEcritureComptable method, of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void testAddSequenceEcritureComptable() throws Exception {
        System.out.println("addSequenceEcritureComptable");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable();
        sequenceEcritureComptable.setJournalCode("AC");
        sequenceEcritureComptable.setDerniereValeur(32);
        sequenceEcritureComptable.setAnnee(1994);

        instance.insertSequenceEcritureComptable(sequenceEcritureComptable);

        SequenceEcritureComptable sec = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(1994, "AC");

        assertEquals(1994, (int) sec.getAnnee());
    }

    /**
     * Test of updateSequenceEcritureComptable method, of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void testModifySequenceEcritureComptable() throws Exception {
        System.out.println("ModifySequenceEcritureComptable");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();

        SequenceEcritureComptable seqFind = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(1994, "AC");
        seqFind.setDerniereValeur(65);

        instance.updateSequenceEcritureComptable(seqFind);

        SequenceEcritureComptable seqBis = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(1994, "AC");

        assertEquals(65, (int) seqBis.getDerniereValeur());
        
        getDaoProxy().getComptabiliteDao().deleteSequenceEcritureComptable(seqBis);
    }

}
