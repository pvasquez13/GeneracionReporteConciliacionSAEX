/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.dao;

import com.globokas.bean.transaccionOracle;
import com.globokas.utils.OraConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;

/**
 *
 * @author pvasquez
 */
public class oracleDao {

    private static final Logger logger = Logger.getLogger(oracleDao.class);

    public List<transaccionOracle> getTransaccionesOracle(String fechaHoy) {

//            OraConnection c = new OraConnection();
//            c.OracleConnection();
        Connection conn = null;
        PreparedStatement stmt = null;
        CallableStatement stmta = null;
        ResultSet rs = null;
        List<transaccionOracle> transaccionesOracleList = new ArrayList<transaccionOracle>();

        try {

            conn = OraConnection.OracleConnection();
//            stmt = conn.prepareStatement(sql);
//            rs = stmt.executeQuery();
            
            stmta = conn.prepareCall("{call SP_TRANSACCIONES_SAEX(?,?)}");
            stmta.setObject(1, fechaHoy);
            stmta.registerOutParameter(2, OracleTypes.CURSOR);
            stmta.executeUpdate();
            rs = (ResultSet) stmta.getObject(2);
            
            while (rs.next()) {
                transaccionOracle bean = new transaccionOracle();
                bean.setLaeFechaOpe(rs.getString("LAE_FECHAOPE"));
                bean.setLaeNumeroOpe(rs.getString("LAE_NUMEROOPE"));
                bean.setLaeCodAgreg(rs.getString("LAE_CODAGREG"));
                bean.setLaeCodTermi(rs.getString("LAE_CODTERMI"));
                bean.setLaeIdDistrib(rs.getString("LAE_IDDISTRIB"));
                bean.setLaeidTrazab(rs.getString("LAE_IDTRAZAB"));
                bean.setLaeSerEmpres(rs.getString("LAE_SER_EMPRES"));
                bean.setLaeSerTipSer(rs.getString("LAE_SER_TIPSER"));
                bean.setLaeSerNumSum(rs.getString("LAE_SER_NUMSUM"));
                bean.setLaeSerRefere(rs.getString("LAE_SER_REFERE"));
                bean.setLaeRecCodCov(rs.getString("LAE_REC_CODCOV"));
                bean.setLaeRecClsCov(rs.getString("LAE_REC_CLSCOV"));
                bean.setLaeRecDesCov(rs.getString("LAE_REC_DESCOV"));
                bean.setLaeRecRefere(rs.getString("LAE_REC_REFERE"));
                bean.setLaeNumMovCargo(rs.getString("LAE_NUMMOV_CARGO"));
                bean.setLaecDivCar(rs.getString("LAE_CDIVCAR"));
                bean.setLaeNumMovAbono(rs.getString("LAE_NUMMOV_ABONO"));
                bean.setLaecDivAbo(rs.getString("LAE_CDIVABO"));
                bean.setLaecDivTca(rs.getString("LAE_DIVTCA"));
                bean.setLaeImpConv(rs.getString("LAE_IMPCONV"));
                bean.setLaeImpTipCa(rs.getString("LAE_IMPTIPCA"));
                bean.setLaeCtaCargo(rs.getString("LAE_CTACARGO"));
                bean.setLaeImpSinComis(rs.getString("LAE_IMPSINCOMIS"));
                bean.setLaeCodigoOpe(rs.getString("LAE_CODIGOOPE"));
                transaccionesOracleList.add(bean);
            }
        } catch (Exception e) {
            logger.info("Error al traer Transacciones de Oracle", e);
            System.out.println("Error al traer Transacciones de Oracle");
            System.out.println(e);
        } finally {
            OraConnection.close(rs, stmta, conn);
        }

        return transaccionesOracleList;
    }

}
