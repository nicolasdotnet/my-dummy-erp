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
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplUTest {

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

    /**
     * Test of checkEcritureComptableUnit method, of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableUnit() throws Exception {

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(vJournalComptable);

        vEcritureComptable.setDate(new Date());

        System.out.println(new Date().toString());

        LocalDate date = LocalDate.now();
        String pReference = "AC-" + date.getYear() + "/00001";
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

    /**
     * Test of checkEcritureComptableUnit method (step : Vérification des
     * contraintes unitaires sur les attributs de l'écriture), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableUnitViolation() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable ne respecte pas les règles de gestion."));

        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    /**
     * Test of checkEcritureComptableUnit method (step : RG2), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableUnitRG2() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable n'est pas équilibrée."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

    /**
     * Test of checkEcritureComptableUnit method (step : RG3), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableUnitRG3WhenOneLigneWithTwoNull() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable doit avoir au moins deux lignes : une ligne avec un débit et une ligne avec un crédit."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

    /**
     * Test of checkEcritureComptableUnit method (step : RG3), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableUnitRG3WhenOneLigneWithOneCreditAndOneDebit() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable doit avoir au moins deux lignes avec une ligne avec un débit et une ligne avec un crédit."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

    /**
     * Test of checkEcritureComptableUnit method (step : RG3), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableUnitRG3WhenTwoLigneWithTwoDebit() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'écriture comptable doit avoir au moins deux lignes avec une ligne avec un débit et une ligne avec un crédit."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

    /**
     * Test of checkEcritureComptableUnit method (step : RG5), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkDateEcritureComptableUnitRG5() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("L'année de la date de l'écriture comptable ne correspond pas à l'année dans la référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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
    }

    /**
     * Test of checkEcritureComptableUnit method (step : RG5), of class
     * ComptabiliteManagerImpl.
     */
    @Test
    public void checkCodeEcritureComptableUnitRG5() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("Le code du journal de l'écriture comptable ne correspond pas au code dans la référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

    /**
     * Test of checkEcritureCompableContext() method (step : id != idECRef ), of
     * class ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureCompableContextWhenIdUnLike() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("Une autre écriture comptable existe déjà avec la même référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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
        doReturn(1).when(ecritureComptable).getId();

        managerImpl.checkEcritureComptableContext(vEcritureComptable);

    }

    /**
     * Test of checkEcritureCompableContext() method (step : id == null ), of
     * class ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableContextWhenIdNull() throws Exception {

        thrownException.expect(FunctionalException.class);
        thrownException.expectMessage(containsString("Une autre écriture comptable existe déjà avec la même référence."));

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

        doReturn(daoProxy).when(managerImpl).getDaoProxy();
        doReturn(comptabiliteDao).when(daoProxy).getComptabiliteDao();
        doReturn(ecritureComptable).when(comptabiliteDao).getEcritureComptableByRef(pReference);

        managerImpl.checkEcritureComptableContext(vEcritureComptable);

    }

    /**
     * Test of checkEcritureCompableContext() method (step : NotFoundException
     * ), of class ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableContextWhenAllGood() throws Exception {

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

    /**
     * Test of checkEcritureCompableContext() method (step : id = idECRef), of
     * class ComptabiliteManagerImpl.
     */
    @Test
    public void checkEcritureComptableContextWhenIdEqual() throws Exception {

        JournalComptable vJournalComptable;
        vJournalComptable = new JournalComptable();
        vJournalComptable.setCode("AC");
        vJournalComptable.setLibelle("Achat");

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

    /**
     * Test of checkEcritureComptable(), of class ComptabiliteManagerImpl.
     */
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

        LocalDate date = LocalDate.now();
        String pReference = "AC-" + date.getYear() + "/00006";

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
