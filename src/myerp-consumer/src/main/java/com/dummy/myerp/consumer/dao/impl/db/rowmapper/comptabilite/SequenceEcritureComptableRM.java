/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import com.dummy.myerp.model.bean.comptabilite.SequenceEcritureComptable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


/**
 * {@link RowMapper} de {@link SequenceEcritureComptable}
 */


/**
 *
 * @author nicolasdotnet
 */
public class SequenceEcritureComptableRM implements RowMapper<SequenceEcritureComptable> {

    @Override
    public SequenceEcritureComptable mapRow(ResultSet pRS, int i) throws SQLException {
        SequenceEcritureComptable vBean = new SequenceEcritureComptable();
        vBean.setJournalCode(pRS.getString("journal_code"));
        vBean.setAnnee(pRS.getInt("annee"));
        vBean.setDerniereValeur(pRS.getInt("derniere_valeur"));

        return vBean;
    }

}
