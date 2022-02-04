package br.gov.go.tj.projudi.webservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IntegracaoSEEU {
	
	private static int CODIGO_SISTEMA = 0;
	
	public static String gerarSenha() {

        int INTERVALO_HORAS_PERMITE_VALIDAR_AUTENTICACAO = 2;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ddMM = "";
        String localMd5 = "";
        Integer codigoSistema = CODIGO_SISTEMA; //NNNNNN; //Codigo interno do TJMP
        Calendar dataAtualSuperior = Calendar.getInstance();
        Calendar dataBase = Calendar.getInstance();
        Calendar dataHoraAtual = Calendar.getInstance();
        ddMM = sdf.format(dataBase.getTime());
        dataBase.set(Calendar.HOUR_OF_DAY, 23);
        dataBase.set(Calendar.MINUTE, 59);
        dataBase.set(Calendar.SECOND, 59);
        dataAtualSuperior = dataBase;
        dataAtualSuperior.add(Calendar.HOUR_OF_DAY, INTERVALO_HORAS_PERMITE_VALIDAR_AUTENTICACAO);

        // compara horario atual é maior que horario base 23:59:59

        if ((dataHoraAtual.getTime().compareTo(dataBase.getTime()) > 0)) {
            ddMM = sdf.format(dataAtualSuperior.getTime());
        }

        String password = codigoSistema + ddMM;

        MessageDigest md = null;
                try {
                        md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                }
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
	}
}
