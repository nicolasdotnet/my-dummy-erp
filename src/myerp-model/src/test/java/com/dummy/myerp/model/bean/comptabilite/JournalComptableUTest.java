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
 * @author nicolasdotnet
 */
public class JournalComptableUTest {

    /**
     * Test of getByCode method, of class JournalComptable.
     */
    @Test
    public void testGetByCode() {

        List<JournalComptable> pList = new ArrayList<JournalComptable>();
        JournalComptable j = new JournalComptable();

        j.setCode("AA");
        j.setLibelle("est");

        pList.add(j);

        String pCode = "AA";

        JournalComptable expResult = j;
        JournalComptable result = JournalComptable.getByCode(pList, pCode);
        assertEquals(expResult, result);

    }

    /**
     * Test of getByCode method (No found/null return), of class
     * JournalComptable.
     */
    @Test
    public void testGetByCodeNoFound() {

        List<JournalComptable> pList = new ArrayList<JournalComptable>();
        JournalComptable j = new JournalComptable();

        j.setCode("AA");
        j.setLibelle("est");
        pList.add(j);

        String pCode = "BB";
        JournalComptable expResult = null;
        JournalComptable result = JournalComptable.getByCode(pList, pCode);
        assertEquals(expResult, result);

    }
}
