
import com.globokas.bean.transaccionOracle;
import com.globokas.bean.transaccionSQL;
import com.globokas.dao.oracleDao;
import com.globokas.dao.sqlDao;
import com.globokas.utils.ClientFTP;
import com.globokas.utils.ConfigApp;
import com.globokas.utils.Mail;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
/**
 *
 * @author pvasquez
 */
public class GeneracionReporteConciliacionSAEX {

    /**
     * @param args the command line arguments
     */
    private static final Logger logger = Logger.getLogger(GeneracionReporteConciliacionSAEX.class);
    private static List<transaccionOracle> transaccionesOracleList;

    public static void main(String[] args) {
        // TODO code application logic here
        String tipoReporte = args[0];
        if (tipoReporte.equals("Conciliacion")) {
            generacionReporteConciliacion();
        } else if (tipoReporte.equals("Establecimientos")) {
            generacionReporteEstablecimiento();
        } else if (tipoReporte.equals("Terminales")) {
            generacionReporteTerminales();
        } else {
            logger.info("No se encontro parametro: Conciliacion,Establecimientos,Terminales");
        }

//        generacionReporteConciliacion();
//        generacionReporteEstablecimiento();
//        generacionReporteTerminales();
    }

    public static void generacionReporteEstablecimiento() {

        String fecha = obtenerFechaReporte();
        String filename = ConfigApp.getValue("NOMBRE_ARCHIVO_ESTABLECIMIENTO_SAEX") + fecha + ".txt";
        String ruta = ConfigApp.getValue("RUTA_ARCHIVO_SAEX");
        String hostFTPBBVA = ConfigApp.getValue("FTP_HOST_BBVA");
        String userFTPBBVA = ConfigApp.getValue("FTP_USER_BBVA");
        String passFTPBBVA = "miftpbbva";
//        String passFTPBBVA = ConfigApp.getValue("FTP_PASS_BBVA");

        sqlDao sqlDao = new sqlDao();

        try {
            logger.info("Inicio de generacion del Reporte de Establecimientos - Fecha: " + fecha);
            String archivoStr = sqlDao.generarReporteEstablecimiento(fecha);
            BufferedWriter out;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta + filename), "latin1"));
            out.write(archivoStr);
            out.close();
            logger.info("La generacion del Reporte de Establecimientos culmino satisfactoriamente");

//            envioFTP(hostFTPBBVA, userFTPBBVA, passFTPBBVA, ruta, filename);
            String asuntoCorreo = "VALIDACION CORRECTA - " + filename;
            String bodyCorreo = "El archivo " + filename + " se genero satisfactoriamente";
            envioCorreo(filename, asuntoCorreo, bodyCorreo);

        } catch (Exception ex) {
            String asuntoCorreo = "VALIDACION INCORRECTA - " + filename;
            String bodyCorreo = "El archivo " + filename + " no pudo generarse o se genero con error.";
            envioCorreo(filename, asuntoCorreo, bodyCorreo);
            java.util.logging.Logger.getLogger(GeneracionReporteConciliacionSAEX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void generacionReporteTerminales() {

        String fecha = obtenerFechaReporte();
        String filename = ConfigApp.getValue("NOMBRE_ARCHIVO_TERMINALES_SAEX") + fecha + ".txt";
        String ruta = ConfigApp.getValue("RUTA_ARCHIVO_SAEX");
        String hostFTPBBVA = ConfigApp.getValue("FTP_HOST_BBVA");
        String userFTPBBVA = ConfigApp.getValue("FTP_USER_BBVA");
        String passFTPBBVA = "miftpbbva";
//        String passFTPBBVA = ConfigApp.getValue("FTP_PASS_BBVA");

        sqlDao sqlDao = new sqlDao();

        try {
            logger.info("Inicio de generacion del Reporte de Terminales - Fecha: " + fecha);
            String archivoStr = sqlDao.generarReporteTerminal(fecha);
            BufferedWriter out;
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta + filename), "latin1"));
            out.write(archivoStr);
            out.close();
            logger.info("La generacion del Reporte de Terminales culmino satisfactoriamente");

//            envioFTP(hostFTPBBVA, userFTPBBVA, passFTPBBVA, ruta, filename);
            String asuntoCorreo = "VALIDACION CORRECTA - " + filename;
            String bodyCorreo = "El archivo " + filename + " se genero satisfactoriamente";
            envioCorreo(filename, asuntoCorreo, bodyCorreo);

        } catch (Exception ex) {
            String asuntoCorreo = "VALIDACION INCORRECTA - " + filename;
            String bodyCorreo = "El archivo " + filename + " no pudo generarse o se genero con error.";
            envioCorreo(filename, asuntoCorreo, bodyCorreo);
            java.util.logging.Logger.getLogger(GeneracionReporteConciliacionSAEX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void generacionReporteConciliacion() {

        String fechaHoy = obtenerFechaReporte();
        String filename = ConfigApp.getValue("NOMBRE_ARCHIVO_SAEX") + fechaHoy + ".txt";
        String ruta = ConfigApp.getValue("RUTA_ARCHIVO_SAEX");

        try {

            logger.info("FECHA=" + fechaHoy);
            oracleDao oracleDao = new oracleDao();
            sqlDao sqlDao = new sqlDao();

            transaccionesOracleList = oracleDao.getTransaccionesOracle(fechaHoy);
            List<transaccionSQL> transaccionesSqlList = sqlDao.getTransaccionesSQL(fechaHoy);

            logger.info("SQL=" + transaccionesSqlList.size());
            logger.info("ORA=" + transaccionesOracleList.size());
            System.out.println("ORA=" + transaccionesOracleList.size());
            StringBuilder archivoStr = new StringBuilder();
            for (transaccionSQL sql : transaccionesSqlList) {

                String key = sql.getFecha() + sql.getIdTerminal() + sql.getNumeroOpe() + sql.getPrcode();
                transaccionOracle ora = buscarTrxOracle(key);

                if (ora != null) {
                    String destra = sql.getCodTransDesTrans();

                    String cuentaCargo = destra.equalsIgnoreCase("KTMCCOBRO EFECTIVO MOVIL") ? ora.getLaeCtaCargo() : sql.getCtaCargo();
                    String impOper = destra.equalsIgnoreCase("RE93PAGO TELEFONIA      ") ? ora.getLaeImpSinComis() : sql.getImpOper();

                    String cuentaAbono = sql.getCtaAbono();
                    if (destra.equalsIgnoreCase("RC57PAGO RECAUDOS       ") && cuentaCargo.equals("                    ")) {
                        cuentaCargo = sql.getCtaAbono();
                        cuentaAbono = "                    ";
                    }

                    if (destra.equals("MCS4PAGO TARJETA DE CRED") || destra.equals("RE93PAGO TELEFONIA      ")) {
                        cuentaCargo = sql.getCtaAbono();
                        cuentaAbono = sql.getCtaCargo();
                    }

                    archivoStr.append(ora.getLaeCodAgreg());
                    archivoStr.append(sql.getCodTerminal());
                    archivoStr.append(ora.getLaeIdDistrib());
                    archivoStr.append(ora.getLaeidTrazab());
                    archivoStr.append(sql.getFechaOpe());
                    archivoStr.append(sql.getHoraOpe());
                    archivoStr.append(sql.getCodUsuar());
                    archivoStr.append(sql.getCodTransDesTrans());
                    archivoStr.append(sql.getNtarjeta());
                    archivoStr.append("          ");//SERNUMMOV
                    archivoStr.append(ora.getLaeSerEmpres());
                    archivoStr.append(ora.getLaeSerTipSer());
                    archivoStr.append(ora.getLaeSerNumSum());
                    archivoStr.append(ora.getLaeSerRefere());
                    archivoStr.append("          ");//RECNUMMOV
                    archivoStr.append(ora.getLaeRecCodCov());
                    archivoStr.append(ora.getLaeRecClsCov());
                    archivoStr.append(ora.getLaeRecDesCov());
                    archivoStr.append(ora.getLaeRecRefere());
                    archivoStr.append(ora.getLaeNumMovCargo());
                    archivoStr.append(cuentaCargo);
                    archivoStr.append(ora.getLaecDivCar());
                    archivoStr.append(ora.getLaeNumMovAbono());
                    archivoStr.append(cuentaAbono);
                    archivoStr.append(ora.getLaecDivAbo());
                    archivoStr.append(ora.getLaecDivTca());
                    archivoStr.append(impOper);
                    archivoStr.append(ora.getLaeImpConv());
                    archivoStr.append(ora.getLaeImpTipCa());
                    archivoStr.append(sql.getIndTipIm());
                    archivoStr.append(sql.getTipPag());
                    archivoStr.append(sql.getTipOpe());
                    archivoStr.append(sql.getIndExtor());

                    archivoStr.append("\r\n");

                }
            }

            logger.info(archivoStr);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta + filename), "latin1"));
            out.write(archivoStr.toString());
            out.close();

            String asuntoCorreo = "VALIDACION CORRECTA - " + filename;
            String bodyCorreo = "El archivo " + filename + " se genero satisfactoriamente";
            envioCorreo(filename, asuntoCorreo, bodyCorreo);

        } catch (IOException e) {
            String asuntoCorreo = "VALIDACION INCORRECTA - " + filename;
            String bodyCorreo = "El archivo " + filename + " no pudo generarse o se genero con error.";
            envioCorreo(filename, asuntoCorreo, bodyCorreo);
            logger.error("Error al generar el archivo", e);
        }
    }

    public static transaccionOracle buscarTrxOracle(String keySql) {

        for (transaccionOracle trxOracle : transaccionesOracleList) {
            String keyOracle = trxOracle.getLaeFechaOpe() + trxOracle.getLaeCodTermi() + trxOracle.getLaeNumeroOpe()
                    + trxOracle.getLaeCodigoOpe();
            if (keyOracle.equals(keySql)) {
                return trxOracle;
            }
        }
        return null;
    }

    public static String obtenerFechaReporte() {
        String fechaHoy;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String modo_automatico = ConfigApp.getValue("MODO_AUTOMATICO");
        logger.info("modo_automatico=" + modo_automatico);

        if (modo_automatico.equals("FALSE")) {
            fechaHoy = ConfigApp.getValue("FECHA_OPERACION");
        } else {//FECHA DE HOY
//            fechaHoy = df.format(new Date());
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            fechaHoy = df.format(c.getTime());
        }

        return fechaHoy;
    }

    public static void envioFTP(String hostFtp, String userFtp, String passFtp, String rutaOrigen, String filename) {
        try {
            logger.info("Inicia el envío del archivo por FTP");
            ClientFTP.upLoadFileToServer2(hostFtp, userFtp, passFtp, rutaOrigen, filename);
            logger.info("Finalizó el envío del archivo por FTP");
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(GeneracionReporteConciliacionSAEX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void envioCorreo(String filename, String asuntoCorreo, String bodyCorreo) {
        try {
            Mail m = new Mail();
            m.enviaCorreoPorGrupo(asuntoCorreo, bodyCorreo, 11);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(GeneracionReporteConciliacionSAEX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
