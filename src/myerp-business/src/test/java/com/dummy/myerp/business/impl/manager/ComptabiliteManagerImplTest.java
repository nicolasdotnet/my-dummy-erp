package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Spy
    ComptabiliteManagerImpl managerImpl;

    @Mock
    DaoProxy daoProxy;

    @Mock
    ComptabiliteDao comptabiliteDao;

    @Mock
    EcritureComptable ecritureComptable;

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Test
    public void checkEcritureComptableUnit() throws Exception {

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);

        vEcritureComptable.setDate(new Date());

        System.out.println(new Date().toString());

        String pReference = "AC-2020/00001";
        vEcritureComptable.setReference(pReference);

        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableUnitViolation() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable ne respecte pas les règles de gestion."));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableUnitRG2() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable n'est pas équilibrée."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableUnitRG3() throws Exception {

        // 2 null ds 1 ligne
        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2020/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);

    }

    @Test
    public void checkEcritureComptableUnitRG3Bis() throws Exception {

        // un credit & un debit meme ligne
        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2020/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                new BigDecimal(123)));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                null));
        manager.checkEcritureComptableUnit(vEcritureComptable);

    }

    @Test
    public void checkEcritureComptableUnitRG3TER() throws Exception {

        // 2 ligne avec 2 debits
        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2020/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, new BigDecimal(123),
                new BigDecimal(246)));
        manager.checkEcritureComptableUnit(vEcritureComptable);

    }
    
        @Test
    public void checkEcritureComptableUnitRG3QUAT() throws Exception {

        // 1 ligne Mais a déjà une validation !
        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2020/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, new BigDecimal(123),
                new BigDecimal(246)));
        manager.checkEcritureComptableUnit(vEcritureComptable);

    }

    @Test
    public void checkEcritureComptableUnitRG5() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'année de la date de l'écriture comptable ne correspond pas à l'année dans la référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2011/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("Le code du journal de l'écriture comptable ne correspond pas au code dans la référence."));

        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());

        LocalDate localDate = vEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        pReference = "AB-" + localDate.getYear() + "/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkCodeEcritureComptableUnitRG5() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("Le code du journal de l'écriture comptable ne correspond pas au code dans la référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());

        LocalDate localDate = vEcritureComptable.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String pReference = "AB-" + localDate.getYear() + "/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableContext() throws Exception {

        // AC-2016/00001
        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("Une autre écriture comptable existe déjà avec la même référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(2);
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2016/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

//        Mockito.doCallRealMethod().doReturn(daoProxy).when((AbstractBusinessManager) managerImpl).getDaoProxy();
        doReturn(daoProxy).when(managerImpl).getDaoProxy();
        doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        doReturn(ecritureComptable).when(comptabiliteDao).getEcritureComptableByRef(pReference);
        doReturn(1).when(ecritureComptable).getId();

        managerImpl.checkEcritureComptableContext(vEcritureComptable);

    }

    @Test
    public void checkEcritureComptableContext2() throws Exception {

        // AC-2016/00001
        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("Une autre écriture comptable existe déjà avec la même référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(null);
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2016/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

//        Mockito.doCallRealMethod().doReturn(daoProxy).when((AbstractBusinessManager) managerImpl).getDaoProxy();
        doReturn(daoProxy).when(managerImpl).getDaoProxy();
        doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        doReturn(ecritureComptable).when(comptabiliteDao).getEcritureComptableByRef(pReference);

        managerImpl.checkEcritureComptableContext(vEcritureComptable);

    }

    @Test
    public void checkEcritureComptableContext3() throws Exception {

        // AC-2016/00001
        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(2);
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2016/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        doReturn(daoProxy).when(managerImpl).getDaoProxy();
        doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        doThrow(NotFoundException.class).when(comptabiliteDao).getEcritureComptableByRef(pReference);

        managerImpl.checkEcritureComptableContext(vEcritureComptable);

    }

    @Test
    public void checkEcritureComptableContext4() throws Exception {

        // AC-2016/00001
        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(2);
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2016/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        doReturn(daoProxy).when(managerImpl).getDaoProxy();
        doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        doReturn(ecritureComptable).when(comptabiliteDao).getEcritureComptableByRef(pReference);
        doReturn(2).when(ecritureComptable).getId();

        managerImpl.checkEcritureComptableContext(vEcritureComptable);

    }

    @Test
    public void addReference() throws Exception {

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2020, 00001);

        JournalComptable SampleJournalComptable = new JournalComptable();
        SampleJournalComptable.setCode("AC");
        SampleJournalComptable.setLibelle("Achat");
        SampleJournalComptable.getSequenceEcritureComptable().add(sequenceEcritureComptable);

        List<JournalComptable> DataJournalComptable = new ArrayList<JournalComptable>();
        DataJournalComptable.add(SampleJournalComptable);

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
//        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2016/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        doReturn(DataJournalComptable).when(managerImpl).getListJournalComptable();
        doNothing().when(managerImpl).insertSequenceEcritureComptable(ArgumentMatchers.isA(SequenceEcritureComptable.class));

        managerImpl.addReference(vEcritureComptable);

        assertEquals("AC-2020/00002", vEcritureComptable.getReference());

    }

    @Test
    public void addReference2() throws Exception {

        JournalComptable SampleJournalComptable = new JournalComptable();
        SampleJournalComptable.setCode("AC");
        SampleJournalComptable.setLibelle("Achat");

        List<JournalComptable> DataJournalComptable = new ArrayList<JournalComptable>();
        DataJournalComptable.add(SampleJournalComptable);

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
//        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2016/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        doReturn(DataJournalComptable).when(managerImpl).getListJournalComptable();
        doNothing().when(managerImpl).insertSequenceEcritureComptable(ArgumentMatchers.isA(SequenceEcritureComptable.class));

        managerImpl.addReference(vEcritureComptable);

        assertEquals("AC-2020/00001", vEcritureComptable.getReference());

    }

    @Test
    public void addReference3() throws Exception {

        SequenceEcritureComptable sequenceEcritureComptable = new SequenceEcritureComptable(2016, 00002);

        JournalComptable SampleJournalComptable = new JournalComptable();
        SampleJournalComptable.setCode("AC");
        SampleJournalComptable.setLibelle("Achat");
        SampleJournalComptable.getSequenceEcritureComptable().add(sequenceEcritureComptable);

        List<JournalComptable> DataJournalComptable = new ArrayList<JournalComptable>();
        DataJournalComptable.add(SampleJournalComptable);

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");
//        vJournalComptable.getSequenceEcritureComptable().add(new SequenceEcritureComptable(2020, 00001));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());
        String pReference = "AC-2016/00001";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, null,
                new BigDecimal(123)));

        doReturn(DataJournalComptable).when(managerImpl).getListJournalComptable();
        doNothing().when(managerImpl).insertSequenceEcritureComptable(ArgumentMatchers.isA(SequenceEcritureComptable.class));

        managerImpl.addReference(vEcritureComptable);

        assertEquals("AC-2020/00001", vEcritureComptable.getReference());

    }

    @Test
    public void checkEcritureComptable() throws Exception {

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(1);
        vEcritureComptable.setJournal(vJournalComptable);
        vEcritureComptable.setDate(new Date());

        String pReference = "AC-2020/00006";
        vEcritureComptable.setReference(pReference);
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        doReturn(daoProxy).when(managerImpl).getDaoProxy();
        doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        doReturn(ecritureComptable).when(comptabiliteDao).getEcritureComptableByRef(pReference);
        doReturn(1).when(ecritureComptable).getId();
        managerImpl.checkEcritureComptable(vEcritureComptable);

    }

}
