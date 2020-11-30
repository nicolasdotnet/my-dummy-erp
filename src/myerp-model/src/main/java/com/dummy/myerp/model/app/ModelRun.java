/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.model.app;

import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** @author pi */
public class ModelRun {

  public static void main(String[] args) {

    System.out.println("com.dummy.myerp.technical.app.modelRun.main()");

    try (ClassPathXmlApplicationContext context =
        new ClassPathXmlApplicationContext("com/dummy/myerp/model/applicationContext-model.xml")) {

      CompteComptable compteComponant = context.getBean(CompteComptable.class);

      FunctionalException feComponant = context.getBean(FunctionalException.class);

      System.out.println(compteComponant);

      System.out.println(feComponant);

    } catch (Exception e) {

      System.out.println(e.getMessage());
      
    }
  }
}
