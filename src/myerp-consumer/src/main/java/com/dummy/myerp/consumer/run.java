/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.consumer;

import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.consumer.db.DataSourcesEnum;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import java.util.List;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author nicolasdotnet
 */
public class run {

    public static void main(String[] args) throws NotFoundException {

        try (ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("com/dummy/myerp/consumer/applicationContext.xml")) {

            DataSourcesEnum compteComponant = context.getBean(DataSourcesEnum.class);

            System.out.println(compteComponant);

        } catch (Exception e) {

            System.out.println(e.getMessage());

        }

        System.out.println("MAIN getListCompteComptable");

        ComptabiliteDaoImpl instance = ComptabiliteDaoImpl.getInstance();

        System.out.println("MAIN ?????????????????????????? : " + instance);
        int expResult = 7;
        List<CompteComptable> result = instance.getListCompteComptable();
        System.out.println("RESULTAT 7 : " + result.size());

        expResult = 4;
        List<JournalComptable> result2 = instance.getListJournalComptable();
        System.out.println("RESULTAT 4 : " + result2.size());

        expResult = 5;
        List<EcritureComptable> result3 = instance.getListEcritureComptable();
        System.out.println("RESULTAT 5 : " + result3.size());

        Integer pId = -1;
        EcritureComptable result4 = instance.getEcritureComptable(pId);
        System.out.println("RESULTAT -1 : " + result4.getId());

        String pReference = "AC-2016/00001";
        EcritureComptable result5 = instance.getEcritureComptableByRef(pReference);
        System.out.println("RESULTAT AC-2016/00001 : " + result5.getReference());

        EcritureComptable pEcritureComptable = new EcritureComptable();
        pEcritureComptable.setId(-1);
        expResult = 3;
        instance.loadListLigneEcriture(pEcritureComptable);
        System.out.println("RESULTAT 3 : " + pEcritureComptable.getListLigneEcriture().size());

    }

}
