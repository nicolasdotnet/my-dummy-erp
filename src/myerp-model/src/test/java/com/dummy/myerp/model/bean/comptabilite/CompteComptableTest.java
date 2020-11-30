/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.model.bean.comptabilite;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pi
 */
public class CompteComptableTest {

    /**
     * Test of getByNumero method, of class CompteComptable.
     */
    @Test
    public void testGetByNumero() {

        List<CompteComptable> pList = new ArrayList<CompteComptable>();
        CompteComptable r = new CompteComptable(Integer.valueOf("1"));
        pList.add(r);

        Integer pNumero = Integer.valueOf("1");

        CompteComptable expResult = r;
        CompteComptable result = CompteComptable.getByNumero(pList, pNumero);
        assertEquals(expResult, result);
    }

    /**
     * Test of getByNumero method (No found/null return), of class
     * CompteComptable.
     */
    @Test
    public void testGetByNumeroNoFound() {

        List<CompteComptable> pList = new ArrayList<CompteComptable>();
        CompteComptable r = new CompteComptable(Integer.valueOf("1"));
        pList.add(r);

        Integer pNumero = Integer.valueOf("0");

        CompteComptable expResult = null;
        CompteComptable result = CompteComptable.getByNumero(pList, pNumero);
        assertEquals(expResult, result);
    }

}
