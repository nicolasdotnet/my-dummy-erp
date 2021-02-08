/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.business.impl.manager;

import static com.dummy.myerp.consumer.ConsumerHelper.getDaoProxy;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author nicolasdotnet
 */
public class ComptabiliteManagerImplITest {

    @Before
    public void init() {

        try (ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("com/dummy/myerp/business/applicationContext.xml")) {

            DataSourcesEnum compteComponant = context.getBean(DataSourcesEnum.class);

            System.out.println(compteComponant);

        } catch (Exception e) {

            System.out.println("HELLLOOOOO" + e.getMessage());

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
        int expResult = 5;
        List<EcritureComptable> result = instance.getListEcritureComptable();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of insertEcritureComptable method, of class ComptabiliteManagerImpl.
     */
    @Test
    public void testInsertEcritureComptable() throws Exception {
        System.out.println("insertEcritureComptable");

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("VE");
        vJournalComptable.setLibelle("Vente");

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);

        vEcritureComptable.setDate(new Date());

        System.out.println(new Date().toString());

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
    public void testUpdateEcritureComptable() throws Exception {
        System.out.println("updateEcritureComptable");

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
    public void testDeleteEcritureComptable() throws Exception {
        System.out.println("deleteEcritureComptable");

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
    public void testAddReferenceInitial() throws Exception {
        System.out.println("addReference");

        LocalDate date = LocalDate.now();
        Date d = new Date();
        d.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

        EcritureComptable pEcritureComptable = new EcritureComptable();
        pEcritureComptable.setId(-1);
        pEcritureComptable.setJournal(vJournalComptable);
        pEcritureComptable.setDate(d);

        pEcritureComptable.setLibelle("ReferenceInitial");
        pEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        pEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();
        instance.addReference(pEcritureComptable);

        EcritureComptable ec = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef("AC-" + date.getYear() + "/00001");
        assertEquals("AC-" + date.getYear() + "/00001", ec.getReference());

        SequenceEcritureComptable sec = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(date.getYear(), "AC");
        assertEquals(1, (int) sec.getDerniereValeur());

    }

    /**
     * Test of addReference method (step : i++), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void testAddReferenceIncrement() throws Exception {
        System.out.println("addReference");

        LocalDate localDate = LocalDate.of(2016, Month.MARCH, 23);
        
        Date d = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        
        System.out.println("d : "+d.toString());

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

        EcritureComptable pEcritureComptable = new EcritureComptable();
        pEcritureComptable.setId(-1);
        pEcritureComptable.setJournal(vJournalComptable);
        pEcritureComptable.setDate(d);

        pEcritureComptable.setLibelle("ReferenceIncrement");
        pEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                null, new BigDecimal(123),
                null));
        pEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(411),
                null, null,
                new BigDecimal(123)));

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();
        instance.addReference(pEcritureComptable);

        EcritureComptable ec = getDaoProxy().getComptabiliteDao().getEcritureComptableByRef("AC-2016/00041");
        assertEquals("AC-2016/00041", ec.getReference());

        SequenceEcritureComptable sec = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(localDate.getYear(), "AC");
        assertEquals(41, (int) sec.getDerniereValeur());

    }

    /**
     * Test of insertSequenceEcritureComptable method, of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void testInsertSequenceEcritureComptable() throws Exception {
        System.out.println("insertSequenceEcritureComptable");

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
    public void testUpdateSequenceEcritureComptable() throws Exception {
        System.out.println("updateSequenceEcritureComptable");

        ComptabiliteManagerImpl instance = new ComptabiliteManagerImpl();

        SequenceEcritureComptable seqFind = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(1994, "AC");
        seqFind.setDerniereValeur(65);

        instance.updateSequenceEcritureComptable(seqFind);
        
        SequenceEcritureComptable seqBis = getDaoProxy().getComptabiliteDao().getSequenceByYearandJournalCode(1994, "AC");

        assertEquals(65, (int) seqBis.getDerniereValeur());
    }

}
