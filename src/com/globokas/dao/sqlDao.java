/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.dao;

import com.globokas.bean.BeanReporteSeguimiento;
import com.globokas.bean.transaccionSQL;
import com.globokas.utils.SqlConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pvasquez
 */
public class sqlDao {

    public List<transaccionSQL> getTransaccionesSQL(String fecha) {

        List<transaccionSQL> transaccionesSqlList = new ArrayList<transaccionSQL>();

        ResultSet rs = null;
        Connection conn = null;

        try {

            SqlConection c = new SqlConection();
            conn = c.SQLServerConnection(2);
            PreparedStatement ps;
            ps = conn.prepareStatement("{call uspCamposConciliacion_saex(?)}");
            ps.setObject(1, fecha);
            rs = ps.executeQuery();
            while (rs.next()) {
                transaccionSQL trxSQL = new transaccionSQL();
                trxSQL.setFecha(rs.getString("FECHA"));
                trxSQL.setNumeroOpe(rs.getString("NUMEROOPE"));
                trxSQL.setIdTerminal(rs.getString("ID_TERMINAL"));
                trxSQL.setAgregador(rs.getString("AGREGADOR"));
                trxSQL.setCodTerminal(rs.getString("CODTERMINAL"));
                trxSQL.setFechaOpe(rs.getString("FECHAOPE"));
                trxSQL.setHoraOpe(rs.getString("HORAOPE"));
                trxSQL.setCodUsuar(rs.getString("CODUSUAR"));
                trxSQL.setCodTransDesTrans(rs.getString("CODTRANS_DESTRANS"));
                trxSQL.setNtarjeta(rs.getString("NTARJETA"));
                trxSQL.setCtaCargo(rs.getString("CTACARGO"));
                trxSQL.setCtaAbono(rs.getString("CTAABONO"));
                trxSQL.setImpOper(rs.getString("IMPOPER"));
                trxSQL.setImpConv(rs.getString("IMPCONV"));
                trxSQL.setImpTipCa(rs.getString("IMPTIPCA"));
                trxSQL.setIndTipIm(rs.getString("INDTIPIM"));
                trxSQL.setTipPag(rs.getString("TIPPAG"));
                trxSQL.setTipOpe(rs.getString("TIPOPE"));
                trxSQL.setIndExtor(rs.getString("INDEXTOR"));
                trxSQL.setPrcode(rs.getString("PRCODE"));
                transaccionesSqlList.add(trxSQL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return transaccionesSqlList;

    }

    public String generarReporteEstablecimiento(String fecha) {

        StringBuilder reporte = new StringBuilder();

        ResultSet rs = null;
        Connection conn = null;

        try {

            SqlConection c = new SqlConection();
            conn = c.SQLServerConnection(1);
            PreparedStatement ps;
            ps = conn.prepareStatement("{call GES_SP_ESTABLECIMIENTOS_SAEX(?,?)}");
            ps.setObject(1, fecha);
            ps.setObject(2, 3);
            rs = ps.executeQuery();
            while (rs.next()) {
                reporte.append(rs.getString("ESTABLECIMIENTO"));
                reporte.append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return reporte.toString();
    }
    
    public String generarReporteTerminal(String fecha) {

        StringBuilder reporte = new StringBuilder();

        ResultSet rs = null;
        Connection conn = null;

        try {

            SqlConection c = new SqlConection();
            conn = c.SQLServerConnection(1);
            PreparedStatement ps;
            ps = conn.prepareStatement("{call GES_SP_TERMINALES_SAEX(?,?)}");
            ps.setObject(1, fecha);
            ps.setObject(2, 3);
            rs = ps.executeQuery();
            while (rs.next()) {
                reporte.append(rs.getString("TERMINAL"));
                reporte.append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return reporte.toString();
    }
    
    public List ListarDestinatariosBD(int CodigoReporte) throws SQLException {
        
        Connection con = null;

        SqlConection c = new SqlConection();
        con = c.SQLServerConnection(2);
        PreparedStatement stmt = null;
        stmt = con.prepareStatement("{call dbo.uspDestinatarios(?)}");
        stmt.setInt(1, CodigoReporte);

        ResultSet rs_mail = stmt.executeQuery();
        ArrayList<BeanReporteSeguimiento> listado_mail = new ArrayList<BeanReporteSeguimiento>();
        while (rs_mail.next()) {

            BeanReporteSeguimiento bean_mail = new BeanReporteSeguimiento();
            bean_mail.setNom_mail(rs_mail.getString("vchNombre"));
            bean_mail.setMail(rs_mail.getString("vchCorreo"));
            bean_mail.setVer_mail(rs_mail.getString("chVersion"));
            bean_mail.setMial_ToCcBcc(rs_mail.getString("vchCopia"));
            listado_mail.add(bean_mail);
        }

        rs_mail.close();
        stmt.close();
        con.close();

        return listado_mail;
    }

}
