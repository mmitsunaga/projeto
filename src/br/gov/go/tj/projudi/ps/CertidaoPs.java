package br.gov.go.tj.projudi.ps;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BeneficioCertidaoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaPublicaDt;
import br.gov.go.tj.projudi.dt.CertidaoSegundoGrauNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.CertidaoTipoDt;
import br.gov.go.tj.projudi.dt.CertidaoValidacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoAntecedenteCriminalDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoSegundoGrauPositivaNegativaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CertidaoPs extends CertidaoPsGen {

	private static final long serialVersionUID = -1755609790036100525L;
	
	public CertidaoPs(Connection conexao){
		Conexao = conexao;
	}
	
	public void Inserir(CertidaoValidacaoDt dados) throws Exception {
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "INSERT INTO PROJUDI.CERT (CERT, DATA_VALIDADE, DATA_EMISSAO, CODIGO_TEMP, NUMERO_GUIA_COMPLETO, FINALIDADE, ID_MODELO, ID_COMARCA, DOCUMENTO) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps.adicionarByte(dados.getCertidao());
		ps.adicionarDateTime(Funcoes.BancoDataHora(dados.getDataValidade()).replaceAll("-", ""));
		ps.adicionarDateTime(Funcoes.BancoDataHora(dados.getDataEmissao()).replaceAll("-", ""));
		ps.adicionarLong(dados.getCodigoTemp());
		ps.adicionarLong(dados.getNumeroGuia());
		ps.adicionarLong(dados.getFinalidade());
		ps.adicionarLong(dados.getId_Modelo());
		ps.adicionarLong(dados.getId_Comarca());
		if (dados.getDocumento() != null && dados.getDocumento().length > 0) {
			ps.adicionarByte(dados.getDocumento());	
		} else {
			ps.adicionarByteNull();	
		}		
				
		dados.setId(executarInsert(sql, "ID_CERT", ps));
		
	}
	
	public void alterar(CertidaoValidacaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "UPDATE PROJUDI.CERT SET  ";
		sql += "CERT = ?";             ps.adicionarByte(dados.getCertidao());
		sql += ",DATA_VALIDADE = ?";   ps.adicionarDateTime(Funcoes.BancoDataHora(dados.getDataValidade()).replaceAll("-", ""));
		sql += ",DATA_EMISSAO = ?";	   ps.adicionarDateTime(Funcoes.BancoDataHora(dados.getDataEmissao()).replaceAll("-", ""));
		sql += ",NUMERO_GUIA_COMPLETO = ?"; ps.adicionarLong(dados.getNumeroGuia());
		sql += ",FINALIDADE = ?"; ps.adicionarLong(dados.getFinalidade());
		sql += ",ID_MODELO = ?"; ps.adicionarLong(dados.getId_Modelo());
		sql += ",ID_COMARCA = ?"; ps.adicionarLong(dados.getId_Comarca());
		if (dados.getDocumento() != null && dados.getDocumento().length > 0) {
			sql += ",DOCUMENTO = ?"; ps.adicionarByte(dados.getDocumento());	
		} 
		sql += " WHERE ID_CERT  = ? "; ps.adicionarLong(dados.getId());
		
		executarUpdateDelete(sql,ps);

	} 
	
	public List consultarProcessosCertidaoNP(CertidaoNegativaPositivaDt certidao) throws Exception {
		List listaProcesso = new ArrayList();		
		String sql;		
		ResultSetTJGO rs = null;	
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		boolean temFimJudicial = true;
		
		sql ="SELECT pa.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_PROC_TIPO, s.SERV, pa.SEXO, pa.ID_PROC_PARTE, pa.NOME, pa.CPF, pa.CNPJ, pa.NOME_MAE, TO_CHAR(pa.DATA_NASCIMENTO, 'DD/MM/RRRR') AS DATA_NASCIMENTO, c.COMARCA, a.AREA_CODIGO, pt.PROC_TIPO_CODIGO, ptps.ID_PROC_SUBTIPO, ";
		sql += " pt.PROC_TIPO, TO_CHAR(p.DATA_RECEBIMENTO, 'DD/MM/RRRR HH24:MI:SS') AS DATA_RECEBIMENTO, TO_CHAR(p.VALOR, '999G999G999G990D00') AS VALOR, pa1.NOME AS NOME_PROMOVENTE, ";
		sql += " (Select U.Nome From Proc_Parte_Advogado Ppa Inner Join Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv Inner Join Usu U On U.Id_Usu = Us.Id_Usu  Where Pa.Id_Proc_Parte = Ppa.Id_Proc_Parte And Rownum = 1  )  As ADVOGADO_PROMOVIDO, ";
		sql += " (Select U.Nome From Projudi.Proc_Parte_Advogado Ppa Inner Join Projudi.Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv inner JOIN USU u ON u.ID_USU = us.ID_USU  WHERE Pa1.Id_Proc_Parte = Ppa.Id_Proc_Parte "
				+ "	AND pa1.ID_PROC_PARTE IN (SELECT MIN(pp.ID_PROC_PARTE) FROM PROJUDI.PROC_PARTE pp WHERE pp.ID_PROC = p.ID_PROC AND pp.ID_PROC_PARTE_TIPO = ? AND pp.DATA_BAIXA IS NULL) and rownum = 1  )  AS ADVOGADO_PROMOVENTE,"; ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
		sql += " (Select Serv From Recurso R Inner Join Serv S On S.Id_Serv = R.Id_Serv_Origem Where R.Id_Proc = P.Id_Proc AND R.Data_Retorno IS NULL AND ROWNUM = 1) AS SERV_ORIGEM ";
		sql+="FROM PROJUDI.PROC_PARTE pa ";
		sql+="JOIN PROJUDI.PROC_PARTE_TIPO ppt ON(pa.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO) ";
		sql+="JOIN PROJUDI.PROC p ON (p.ID_PROC = pa.ID_PROC) ";
		sql+="JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
		sql+="JOIN PROJUDI.SERV s ON (p.ID_SERV = s.ID_SERV) ";
		sql+="JOIN PROJUDI.SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO) ";
		sql+="JOIN PROJUDI.COMARCA c ON (s.ID_COMARCA = c.ID_COMARCA)";
		sql+="JOIN PROJUDI.AREA a ON (a.ID_AREA = p.ID_AREA) ";			
		sql+="JOIN PROJUDI.PROC_TIPO pt ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
		sql+="LEFT JOIN PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps ON ptps.ID_PROC_TIPO = pt.ID_PROC_TIPO AND ptps.ID_PROC_SUBTIPO <> 1 "; //excluindo processos do tipo NÃO CONTENCIOSO = 1 
		sql+="LEFT JOIN PROJUDI.PROC_PARTE pa1 ON pa1.ID_PROC = p.ID_PROC AND pa1.ID_PROC_PARTE_TIPO = 2 "; //PROMOVENTE
  		sql+=" WHERE ppt.PROC_PARTE_TIPO_CODIGO = ? "; ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
        sql+= " AND pa.DATA_BAIXA IS NULL ";
        sql+= " AND ss.SERV_SUBTIPO_CODIGO <> ?"; ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
        sql+= " AND ps.PROC_STATUS_CODIGO IN(?,?,?) "; ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO); ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);
        sql+= " AND pa.ID_PROC_PARTE_TIPO = ? "; ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
        if (certidao.getAreaCodigo() != null && certidao.getAreaCodigo().trim().length() > 0) {
			sql+=" AND a.AREA_CODIGO = ?"; 
			ps.adicionarLong(Funcoes.StringToInt(certidao.getAreaCodigo()));
		}
		
        if (certidao.getId_Comarca() != null && certidao.getId_Comarca().trim().length() > 0) {
			sql+= " AND (c.ID_COMARCA = ? OR EXISTS (SELECT 1 ";
			sql+= "                                    FROM PROJUDI.RECURSO REC INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM ";
			sql+= "                                   WHERE REC.ID_PROC = P.ID_PROC ";
			sql+= "                                    AND SREC.ID_COMARCA = ?)) "; 
			ps.adicionarLong(certidao.getId_Comarca());
			ps.adicionarLong(certidao.getId_Comarca());			
		} else if (certidao.getComarcaCodigo() != null && certidao.getComarcaCodigo().trim().length() > 0) {
			sql+= " AND (c.COMARCA_CODIGO = ? OR EXISTS (SELECT 1 ";
			sql+= "                                        FROM PROJUDI.RECURSO REC INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM ";
			sql+= "                                        INNER JOIN PROJUDI.COMARCA CREC ON CREC.ID_COMARCA = SREC.ID_COMARCA ";
			sql+= "                                       WHERE REC.ID_PROC = P.ID_PROC ";
			sql+= "                                        AND CREC.COMARCA_CODIGO = ?)) "; 
			ps.adicionarLong(certidao.getComarcaCodigo());
			ps.adicionarLong(certidao.getComarcaCodigo());
		}
			
        if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
			sql+= " AND ";
			String cpfCnpj = "";
			if (certidao.getCpfCnpj().length() <= 14) {
				cpfCnpj = "CPF";
				sql += " (Pa." + cpfCnpj + " = ? "; ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
			} else {
				cpfCnpj = "CNPJ";
				sql += " (radicalcnpj(Pa."+cpfCnpj+ ") = ?  "; ps.adicionarString(certidao.getRadicalCnpj().trim());
			}
		}
		sql+= " OR (pa.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()).trim().toUpperCase());
		if (certidao.getNomeMae() != null && !certidao.getNomeMae().isEmpty() && certidao.getDataNascimento() != null && !certidao.getDataNascimento().isEmpty()) {
			sql+= " AND Pa.Nome_Mae = ? AND Pa.Data_Nascimento = ? "; 
			ps.adicionarString(certidao.getNomeMae().trim().toUpperCase());
			ps.adicionarDate(certidao.getDataNascimento());

			sql+= " ) OR (pa.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()).trim().toUpperCase());
		}
		sql+= " ) )";
		
		try{
			rs = consultar(sql, ps);
			while (rs.next()) {
				if ((rs.getString("ID_PROC_SUBTIPO") != null && 
					rs.getString("ID_PROC_SUBTIPO").equals("4") ||
					rs.getString("ID_PROC_TIPO").equals("95")) &&   // T.C.O.
					certidao.getAreaCodigo() != null && 
					certidao.getAreaCodigo().trim().length() > 0 &&
					Funcoes.StringToInt(certidao.getAreaCodigo()) == Funcoes.StringToInt(AreaDt.CRIMINAL)) {
					temFimJudicial = false;
					continue;
				}
				ProcessoCertidaoPositivaNegativaDt dt = new ProcessoCertidaoPositivaNegativaDt();
				
				dt.setId_Processo(rs.getString("ID_PROC"));
				dt.setProcessoNumero(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR"));
				if (rs.getString("SERV_ORIGEM") != null) {
					dt.setServentia(rs.getString("SERV_ORIGEM"));
				} else {
					dt.setServentia(rs.getString("SERV"));	
				}
				dt.setIdProcessoPartePromovido((rs.getString("ID_PROC_PARTE")));
				dt.setPromovidoNome(rs.getString("NOME"));
				dt.setPromovidodaSexo(rs.getString("SEXO"));
				dt.setPromovidoCpf(rs.getString("CPF"));
				dt.setPromovidoCnpj(rs.getString("CNPJ"));
				dt.setPromovidoNomeMae(rs.getString("NOME_MAE"));
				dt.setCertidaoPromoventeNome(rs.getString("NOME_PROMOVENTE"));
				dt.setNomeAdvogadoPromovente(rs.getString("ADVOGADO_PROMOVENTE"));
				dt.setNomeAdvogadoPromovido(rs.getString("ADVOGADO_PROMOVIDO"));
				dt.setPromovidoDataNascimento(rs.getString("DATA_NASCIMENTO"));
				dt.setTipo(rs.getString("AREA_CODIGO"));
				dt.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
				dt.setProcessoTipo(rs.getString("PROC_TIPO"));
				dt.setComarca(rs.getString("COMARCA"));
				dt.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
				dt.setValor(rs.getString("VALOR"));
				dt.setSistema("PROJUDI");
				
				listaProcesso.add(dt);
			}

		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}

		if (certidao.getAreaCodigo() != null && 
			certidao.getAreaCodigo().trim().length() > 0 &&
			Funcoes.StringToInt(certidao.getAreaCodigo()) == Funcoes.StringToInt(AreaDt.CIVEL)) {
			//Verifico se tem averbação de custas
			listaProcesso.addAll(getProcessoAverbacaoCusta(certidao));	
		}		
		
		//Verifico se tem fins judiciais
		//Se a lista for vazia usa o temFimJudicial, senão é true
		if(listaProcesso.isEmpty()) {
			certidao.setFimJudicial(temFimJudicial);
		} else {
			certidao.setFimJudicial(true);
		}
		
		return listaProcesso;
	}
	
	/**
	 * Consulta de processos de primeiro grau para a certidão negativa positiva.
	 * @param certidaoDt - dados da certidão a ser consultada
	 * @return lista de processos da certidão
	 * @throws Exception
	 * @author hmgodinho, jrcorrea
	 */
	public List consultarProcessosPrimeiroGrauCertidaoNP(CertidaoNegativaPositivaDt certidao) throws Exception {
			List listaProcesso = new ArrayList();	
			Set<String> listaProcessosAux = new HashSet<String>(); 
			List listaProcessoTipoTemFimJudicial = null;
			String sql;		
			ResultSetTJGO rs = null;	
			PreparedStatementTJGO ps = new PreparedStatementTJGO();		
			boolean temFimJudicial = true;

			//processos sem recursos			
			sql ="SELECT pa.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, s.SERV, pa.SEXO, pa.ID_PROC_PARTE, pa.NOME, pa.CPF, pa.CNPJ, pa.NOME_MAE, TO_CHAR(pa.DATA_NASCIMENTO, 'DD/MM/RRRR') AS DATA_NASCIMENTO, c.COMARCA, a.AREA_CODIGO, pt.PROC_TIPO_CODIGO, ";//ptps.ID_PROC_SUBTIPO, ";
			sql += " pt.PROC_TIPO, TO_CHAR(p.DATA_RECEBIMENTO, 'DD/MM/RRRR HH24:MI:SS') AS DATA_RECEBIMENTO, TO_CHAR(p.VALOR, '999G999G999G990D00') AS VALOR, pa1.NOME AS NOME_PROMOVENTE, ";
			sql += " (Select U.Nome From Proc_Parte_Advogado Ppa Inner Join Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv Inner Join Usu U On U.Id_Usu = Us.Id_Usu  Where Pa.Id_Proc_Parte = Ppa.Id_Proc_Parte And Rownum = 1  )  As ADVOGADO_PROMOVIDO, ";
			sql += " (Select U.Nome From Projudi.Proc_Parte_Advogado Ppa Inner Join Projudi.Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv inner JOIN USU u ON u.ID_USU = us.ID_USU  WHERE Pa1.Id_Proc_Parte = Ppa.Id_Proc_Parte and rownum = 1  )  AS ADVOGADO_PROMOVENTE, ";
			sql += " (Select Serv From Recurso R Inner Join Serv S On S.Id_Serv = R.Id_Serv_Origem Where R.Id_Proc = P.Id_Proc AND R.Data_Retorno IS NULL AND ROWNUM = 1) AS SERV_ORIGEM ";
			sql+="FROM PROJUDI.PROC_PARTE pa ";
			sql+="JOIN PROJUDI.PROC_PARTE_TIPO ppt ON(pa.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO) ";
			sql+="JOIN PROJUDI.PROC p ON (p.ID_PROC = pa.ID_PROC) ";
			sql+="JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
			sql+="JOIN PROJUDI.SERV s ON (p.ID_SERV = s.ID_SERV) ";
			sql+="JOIN PROJUDI.SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO) ";
	        sql+="JOIN PROJUDI.COMARCA c ON (s.ID_COMARCA = c.ID_COMARCA)";
			sql+="JOIN PROJUDI.AREA a ON (a.ID_AREA = p.ID_AREA) ";			
			sql+="JOIN PROJUDI.PROC_TIPO pt ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
			//localizar o primeiro polo ativo do processo, se houver
			sql+="LEFT JOIN projudi.proc_parte pa1 ON (pa1.id_proc = p.id_proc AND pa1.id_proc_parte_tipo = ? )"; 													ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
			sql+=" WHERE ppt.PROC_PARTE_TIPO_CODIGO = ? ";																											ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
			sql+= " AND pa.DATA_BAIXA IS NULL ";		
			sql+= "	AND ss.SERV_SUBTIPO_CODIGO NOT IN ( ";
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL); 	
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL); 
			sql+= " ? ";																																			ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
			sql+= " )";
					
	        sql+= " AND ps.PROC_STATUS_CODIGO IN( ";
	        sql+= " ?,"; 																																			ps.adicionarLong(ProcessoStatusDt.ATIVO); 
	        sql+= " ?,";																																			ps.adicionarLong(ProcessoStatusDt.SUSPENSO); 
	        sql+= " ? ";																																			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);
	        sql+= " ) ";
	        		
			sql+= " AND pa.ID_PROC_PARTE_TIPO = ? "; 																												ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
			
			//localizando o primeiro registro de polo ativo do processo, se houver. Impede duplicidade de registros em caso de processos com mais de um polo ativo.
			// Condição errada: impedia de encontrar qualquer processo (BO 9521/2020)
/*			sql+= " AND pa1.ID_PROC_PARTE = (SELECT MIN(ID_PROC_PARTE) FROM PROC_PARTE PP2 "
					+ "	WHERE p.ID_PROC = pp2.ID_PROC AND pa1.ID_PROC_PARTE_TIPO = ? AND pa1.DATA_BAIXA IS NULL) "; 												ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO); */
					
			if (certidao.isCivel()) {
				//valido para cada tipo de certidão os tipos de processos que não positivam.
				sql+= " AND not exists (SELECT 1 FROM cert_tipo_proc_tipo ctpt WHERE ctpt.id_cert_tipo = ? and  ctpt.id_proc_tipo = p.id_proc_tipo )";				ps.adicionarLong(CertidaoTipoDt.ID_CERTIDAO_CIVEL);
				sql+=" AND a.AREA_CODIGO = ?"; 																														ps.adicionarLong(AreaDt.CIVEL);			
				//o else foi removido daqui e colocado logo após iniciar o uso dos dados retornados pela consulta no resultset (while (rs.next()))
			} else {
				//área criminal
				sql+=" AND a.AREA_CODIGO = ?"; 																														ps.adicionarLong(AreaDt.CRIMINAL);
			}
			
			if (certidao.getId_Comarca() != null && certidao.getId_Comarca().trim().length() > 0) {
				sql+= " AND (c.ID_COMARCA = ? OR EXISTS (SELECT DISTINCT REC.ID_PROC ";																				ps.adicionarLong(certidao.getId_Comarca());
				sql+= "                                    	FROM PROJUDI.RECURSO REC INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM ";
				sql+= "                                   	WHERE DATA_RETORNO IS NULL AND REC.ID_PROC = P.ID_PROC ";
				sql+= "                                    		AND SREC.ID_COMARCA = ?)) ";																		ps.adicionarLong(certidao.getId_Comarca());					 					
			} else if (certidao.getComarcaCodigo() != null && certidao.getComarcaCodigo().trim().length() > 0) {
				sql+= " AND (c.COMARCA_CODIGO = ? OR EXISTS (SELECT DISTINCT REC.ID_PROC ";																			ps.adicionarLong(certidao.getComarcaCodigo());
				sql+= "                                        	FROM PROJUDI.RECURSO REC "
						+ "											INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM "
						+ "											INNER JOIN PROJUDI.COMARCA C ON C.ID_COMARCA = SREC.ID_COMARCA";
				sql+= "                                       	WHERE DATA_RETORNO IS NULL AND REC.ID_PROC = P.ID_PROC ";
				sql+= "                                        		AND C.COMARCA_CODIGO = ?)) ";																	ps.adicionarLong(certidao.getComarcaCodigo()); 					
			}
				
			//O CPF está de fora, pois ele é único.
			//Se colocar junto do if de nome abaixo no código, os processos que tiverem seu CPF e forem de outra pessoa
			//não aparecerão na busca.
			if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
				sql+= " AND ";
				String cpfCnpj = "";
				if (certidao.getCpfCnpj().length() < 14) {
					cpfCnpj = "CPF";
					sql += " (Pa." + cpfCnpj + " = ? "; 																											ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
				} else {
					cpfCnpj = "CNPJ";
					sql += " (radicalcnpj(Pa."+cpfCnpj+ ") = ? "; 																							ps.adicionarString(Funcoes.getRadicalCnpj(certidao.getCpfCnpj()));
				}
			}
			//Conforme Art. 96 da Consolidação de Atos normativos
			//https://www.tjgo.jus.br/index.php/91-corregedoria/publicacoes/346-cons-atos-normativos
			sql+= " OR (pa.NOME_SIMPLIFICADO = ? "; 																												ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()));
			if (certidao.getNomeMae() != null || certidao.getDataNascimento() != null || certidao.getCpfCnpj() != null) {
				if (certidao.getNomeMae() != null && !certidao.getNomeMae().isEmpty()){ 
					sql+= " AND ( Pa.Nome_Mae = ? or Pa.Nome_Mae is null ) "; 																						ps.adicionarString(certidao.getNomeMae().trim().toUpperCase());																																									
				}																											
				if (certidao.getDataNascimento() != null && !certidao.getDataNascimento().isEmpty()) {
					sql+= " AND  ( Pa.Data_Nascimento = ? or Pa.Data_Nascimento is null) ";																			ps.adicionarDate(certidao.getDataNascimento());
				}
				if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
					sql+= " AND ";
					String cpfCnpj = "";
					if (certidao.getCpfCnpj().length() < 14) {
						cpfCnpj = "CPF";
						sql += " (Pa." + cpfCnpj + " IS NULL OR Pa." + cpfCnpj  + "  = ? "; 																		ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
					} else {
						cpfCnpj = "CNPJ";
						sql += " (Pa." + cpfCnpj + " IS NULL OR radicalcnpj(Pa."+cpfCnpj+ ") = ? ";											ps.adicionarString(Funcoes.getRadicalCnpj(certidao.getCpfCnpj()));
					}
					sql+= " )";
				}
			}
			sql+= ") )";
			
			sql+= " UNION ALL ";
			
			//processos com recursos			
			sql +="SELECT pa.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, s.SERV, pa.SEXO, pa.ID_PROC_PARTE, pa.NOME, pa.CPF, pa.CNPJ, pa.NOME_MAE, TO_CHAR(pa.DATA_NASCIMENTO, 'DD/MM/RRRR') AS DATA_NASCIMENTO, c.COMARCA, a.AREA_CODIGO, pt.PROC_TIPO_CODIGO, ";//ptps.ID_PROC_SUBTIPO, ";
			sql += " pt.PROC_TIPO, TO_CHAR(p.DATA_RECEBIMENTO, 'DD/MM/RRRR HH24:MI:SS') AS DATA_RECEBIMENTO, TO_CHAR(p.VALOR, '999G999G999G990D00') AS VALOR, pa1.NOME AS NOME_PROMOVENTE, ";
			sql += " (Select U.Nome From Proc_Parte_Advogado Ppa Inner Join Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv Inner Join Usu U On U.Id_Usu = Us.Id_Usu  Where Pa.Id_Proc_Parte = Ppa.Id_Proc_Parte And Rownum = 1  )  As ADVOGADO_PROMOVIDO, ";
			sql += " (Select U.Nome From Projudi.Proc_Parte_Advogado Ppa Inner Join Projudi.Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv inner JOIN USU u ON u.ID_USU = us.ID_USU  WHERE Pa1.Id_Proc_Parte = Ppa.Id_Proc_Parte and rownum = 1  )  AS ADVOGADO_PROMOVENTE, ";
			sql += " (Select Serv From Recurso R Inner Join Serv S On S.Id_Serv = R.Id_Serv_Origem Where R.Id_Proc = P.Id_Proc AND R.Data_Retorno IS NULL AND ROWNUM = 1) AS SERV_ORIGEM ";
			sql+="FROM PROJUDI.PROC_PARTE pa ";
			sql+="JOIN PROJUDI.PROC_PARTE_TIPO ppt ON(pa.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO) ";
			sql+="JOIN PROJUDI.PROC p ON (p.ID_PROC = pa.ID_PROC) ";
			sql+="JOIN PROJUDI.RECURSO R ON (p.ID_PROC = r.ID_PROC    ) ";
			sql+="JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
			//serventia da origem do processo
			sql+="JOIN PROJUDI.SERV s ON (R.ID_SERV_ORIGEM = s.ID_SERV) ";
			sql+="JOIN PROJUDI.SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO) ";
	        sql+="JOIN PROJUDI.COMARCA c ON (s.ID_COMARCA = c.ID_COMARCA)";
			sql+="JOIN PROJUDI.AREA a ON (a.ID_AREA = p.ID_AREA) ";			
			sql+="JOIN PROJUDI.PROC_TIPO pt ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
			sql+="LEFT JOIN projudi.proc_parte pa1 ON (pa1.id_proc = p.id_proc AND pa1.id_proc_parte_tipo = ? )"; 													ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
			sql+=" WHERE ppt.PROC_PARTE_TIPO_CODIGO = ? ";																											ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
			sql+= " AND R.DATA_RETORNO is null ";
			sql+= " AND pa.DATA_BAIXA IS NULL ";		
			sql+= "	AND ss.SERV_SUBTIPO_CODIGO NOT IN ( ";
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL); 	
			sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL); 
			sql+= " ? ";																																			ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
			sql+= " )";
					
	        sql+= " AND ps.PROC_STATUS_CODIGO IN( ";
	        sql+= " ?,"; 																																			ps.adicionarLong(ProcessoStatusDt.ATIVO); 
	        sql+= " ?,";																																			ps.adicionarLong(ProcessoStatusDt.SUSPENSO); 
	        sql+= " ? ";																																			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);
	        sql+= " ) ";
	        		
			sql+= " AND pa.ID_PROC_PARTE_TIPO = ? "; 																												ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
			
			//localizando o primeiro registro de polo ativo do processo, se houver. Impede duplicidade de registros em caso de processos com mais de um polo ativo.
			// Condição errada: impedia de encontrar qualquer processo (BO 9521/2020)
/*			sql+= " AND pa1.ID_PROC_PARTE = (SELECT MIN(ID_PROC_PARTE) FROM PROC_PARTE PP2 "
					+ "	WHERE p.ID_PROC = pp2.ID_PROC AND pa1.ID_PROC_PARTE_TIPO = ? AND pa1.DATA_BAIXA IS NULL) "; 												ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO); */
			
			if (certidao.isCivel()) {
				//valido para cada tipo de certidão os tipos de processos que não positivam 
				sql+= " AND not exists (SELECT 1 FROM cert_tipo_proc_tipo ctpt WHERE ctpt.id_cert_tipo = ? and  ctpt.id_proc_tipo = p.id_proc_tipo )";				ps.adicionarLong(CertidaoTipoDt.ID_CERTIDAO_CIVEL);
				sql+=" AND a.AREA_CODIGO = ?"; 																														ps.adicionarLong(AreaDt.CIVEL);			
				//o else foi removido daqui e colocado logo após iniciar o uso dos dados retornados pela consulta no resultset (while (rs.next()))
			} else {
				//área criminal
				sql+=" AND a.AREA_CODIGO = ?"; 																														ps.adicionarLong(AreaDt.CRIMINAL);
			}
			
			if (certidao.getId_Comarca() != null && certidao.getId_Comarca().trim().length() > 0) {
				sql+= " AND (c.ID_COMARCA = ? OR EXISTS (SELECT DISTINCT REC.ID_PROC ";																				ps.adicionarLong(certidao.getId_Comarca());
				sql+= "                                    	FROM PROJUDI.RECURSO REC INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM ";
				sql+= "                                   	WHERE DATA_RETORNO IS NULL AND REC.ID_PROC = P.ID_PROC ";
				sql+= "                                    		AND SREC.ID_COMARCA = ?)) ";																		ps.adicionarLong(certidao.getId_Comarca());					 					
			} else if (certidao.getComarcaCodigo() != null && certidao.getComarcaCodigo().trim().length() > 0) {
				sql+= " AND (c.COMARCA_CODIGO = ? OR EXISTS (SELECT DISTINCT REC.ID_PROC ";																			ps.adicionarLong(certidao.getComarcaCodigo());
				sql+= "                                        	FROM PROJUDI.RECURSO REC "
						+ "											INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM "
						+ "											INNER JOIN PROJUDI.COMARCA C ON C.ID_COMARCA = SREC.ID_COMARCA";
				sql+= "                                       	WHERE DATA_RETORNO IS NULL AND REC.ID_PROC = P.ID_PROC ";
				sql+= "                                        		AND C.COMARCA_CODIGO = ?)) ";																	ps.adicionarLong(certidao.getComarcaCodigo()); 					
			}
				
			//O CPF está de fora, pois ele é único.
			//Se colocar junto do if de nome abaixo no código, os processos que tiverem seu CPF e forem de outra pessoa
			//não aparecerão na busca.
			if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
				sql+= " AND ";
				String cpfCnpj = "";
				if (certidao.getCpfCnpj().length() < 14) {
					cpfCnpj = "CPF";
					sql += " (Pa." + cpfCnpj + " = ? "; 																											ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
				} else {
					cpfCnpj = "CNPJ";
					sql += " (radicalcnpj(Pa."+cpfCnpj+ ") = ? "; 																									ps.adicionarString(Funcoes.getRadicalCnpj(certidao.getCpfCnpj()));
				}
			}
			//Conforme Art. 96 da Consolidação de Atos normativos
			//https://www.tjgo.jus.br/index.php/91-corregedoria/publicacoes/346-cons-atos-normativos
			sql+= " OR (pa.NOME_SIMPLIFICADO = ? "; 																												ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()));
			if (certidao.getNomeMae() != null || certidao.getDataNascimento() != null || certidao.getCpfCnpj() != null) {
				if (certidao.getNomeMae() != null && !certidao.getNomeMae().isEmpty()){ 
					sql+= " AND ( Pa.Nome_Mae = ? or Pa.Nome_Mae is null ) "; 		ps.adicionarString(certidao.getNomeMae().trim().toUpperCase());																																									
				}																											
				if (certidao.getDataNascimento() != null && !certidao.getDataNascimento().isEmpty()) {
					sql+= " AND  ( Pa.Data_Nascimento = ? or Pa.Data_Nascimento is null) ";				ps.adicionarDate(certidao.getDataNascimento());
				}
				if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
					sql+= " AND ";
					String cpfCnpj = "";
					if (certidao.getCpfCnpj().length() < 14) {
						cpfCnpj = "CPF";
						sql += " (Pa." + cpfCnpj + " IS NULL OR Pa." + cpfCnpj  + "  = ? "; 																		ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
					} else {
						cpfCnpj = "CNPJ";
						sql += " (Pa." + cpfCnpj + " IS NULL OR radicalcnpj(Pa."+cpfCnpj+ ") = ? "; 																ps.adicionarString(Funcoes.getRadicalCnpj(certidao.getCpfCnpj()));
					}
					sql+= " )";
				}
			}
			sql+= ") )";
			
			try{
				rs = consultar(sql, ps);
				
				while (rs.next()) {
					
					//Verificação de processos que não positivam certidão CRIMINAL. Estes processos, se forem localizados aqui, não devem ser
					//incluídos na certidão, mas devem incluir nela a informação de não ter fins judiciais.
					if (certidao.isCriminal()) {
						if(listaProcessoTipoTemFimJudicial == null) {
							listaProcessoTipoTemFimJudicial = this.getListaProcessoTemFimJudicial(String.valueOf(CertidaoTipoDt.ID_CERTIDAO_CRIMINAL));
						}
						if(listaProcessoTipoTemFimJudicial != null &&
							listaProcessoTipoTemFimJudicial.contains(rs.getString("PROC_TIPO_CODIGO"))) {
							temFimJudicial = false;

							continue;
						}
					}
					
					if(listaProcessosAux.contains(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR"))) {
						continue;
					}
					
					ProcessoCertidaoPositivaNegativaDt dt = new ProcessoCertidaoPositivaNegativaDt();
					
					dt.setId_Processo(rs.getString("ID_PROC"));
					dt.setProcessoNumero(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR"));
					if (rs.getString("SERV_ORIGEM") != null) {
						dt.setServentia(rs.getString("SERV_ORIGEM"));
					} else {
						dt.setServentia(rs.getString("SERV"));	
					}
					dt.setIdProcessoPartePromovido((rs.getString("ID_PROC_PARTE")));
					dt.setPromovidoNome(rs.getString("NOME"));
					dt.setPromovidodaSexo(rs.getString("SEXO"));
					dt.setPromovidoCpf(rs.getString("CPF"));
					dt.setPromovidoCnpj(rs.getString("CNPJ"));
					dt.setPromovidoNomeMae(rs.getString("NOME_MAE"));
					dt.setCertidaoPromoventeNome(rs.getString("NOME_PROMOVENTE"));
					dt.setNomeAdvogadoPromovente(rs.getString("ADVOGADO_PROMOVENTE"));
					dt.setNomeAdvogadoPromovido(rs.getString("ADVOGADO_PROMOVIDO"));
					dt.setPromovidoDataNascimento(rs.getString("DATA_NASCIMENTO"));
					dt.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
					dt.setProcessoTipo(rs.getString("PROC_TIPO"));
					dt.setComarca(rs.getString("COMARCA"));
					dt.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
					dt.setValor(rs.getString("VALOR"));
					dt.setSistema("PROJUDI");
					
					listaProcessosAux.add(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR"));
					listaProcesso.add(dt);
				}

			} finally{
				try{
					if (rs != null)
						rs.close();
				} catch(Exception e) { 
				}
			}

			if (certidao.isCivel()) {
				//Verifico se tem averbação de custas
				listaProcesso.addAll(getProcessoAverbacaoCusta(certidao));	
			}		
			
			//Verifico se tem fins judiciais
			//Se a lista for vazia usa o temFimJudicial, senão é true
			if(listaProcesso.isEmpty()) {
				certidao.setFimJudicial(temFimJudicial);
			} else {
				certidao.setFimJudicial(true);
			}
			
			return listaProcesso;
		}
		
	/**
	 * Método usado para trazer a lista de tipos de processos que não positivam certidões.
	 * @param idCertidaoTipo - id da certidão
	 * @return lista de tipos de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List getListaProcessoTemFimJudicial (String idCertidaoTipo) throws Exception {
		List listaProcessoTipo = new ArrayList();
		String sql;		
		ResultSetTJGO rs = null;	
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		
		sql = "SELECT pt.PROC_TIPO_CODIGO FROM cert_tipo_proc_tipo ctpt "
				+ " INNER JOIN proc_tipo pt ON pt.id_proc_tipo = ctpt.id_proc_tipo "
				+ " WHERE ctpt.id_cert_tipo = ? ";
		ps.adicionarLong(idCertidaoTipo);
		
		try{
			rs = consultar(sql, ps);
			while (rs.next()) {
				listaProcessoTipo.add(rs.getString("PROC_TIPO_CODIGO"));
			}
		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) { 
			}
		}
		
		return listaProcessoTipo;
	}
		
	
	// Válido para CertidaoNegativaPositivaPublicaDt
	public List consultarProcessosCertidaoNP(CertidaoNegativaPositivaPublicaDt certidao) throws Exception {
		List listaProcesso = new ArrayList();		
		String sql;		
		ResultSetTJGO rs = null;	
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		boolean temFimJudicial = true;
		
		sql ="SELECT pa.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, s.SERV, pa.SEXO, pa.ID_PROC_PARTE, pa.NOME, pa.CPF, pa.CNPJ, pa.NOME_MAE, TO_CHAR(pa.DATA_NASCIMENTO, 'DD/MM/RRRR') AS DATA_NASCIMENTO, c.COMARCA, a.AREA_CODIGO, pt.PROC_TIPO_CODIGO, ptps.ID_PROC_SUBTIPO, ";
		sql += " pt.PROC_TIPO, TO_CHAR(p.DATA_RECEBIMENTO, 'DD/MM/RRRR HH24:MI:SS') AS DATA_RECEBIMENTO, TO_CHAR(p.VALOR, '999G999G999G990D00') AS VALOR, pa1.NOME AS NOME_PROMOVENTE, ";
		sql += " (Select U.Nome From Proc_Parte_Advogado Ppa Inner Join Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv Inner Join Usu U On U.Id_Usu = Us.Id_Usu  Where Pa.Id_Proc_Parte = Ppa.Id_Proc_Parte And Rownum = 1  )  As ADVOGADO_PROMOVIDO, ";
		sql += " (Select U.Nome From Projudi.Proc_Parte_Advogado Ppa Inner Join Projudi.Usu_Serv Us On Us.Id_Usu_Serv = Ppa.Id_Usu_Serv inner JOIN USU u ON u.ID_USU = us.ID_USU  WHERE Pa1.Id_Proc_Parte = Ppa.Id_Proc_Parte and rownum = 1  )  AS ADVOGADO_PROMOVENTE, ";
		sql += " (Select Serv From Recurso R Inner Join Serv S On S.Id_Serv = R.Id_Serv_Origem Where R.Id_Proc = P.Id_Proc AND R.Data_Retorno IS NULL AND ROWNUM = 1) AS SERV_ORIGEM ";
		sql+="FROM PROJUDI.PROC_PARTE pa ";
		sql+="JOIN PROJUDI.PROC_PARTE_TIPO ppt ON(pa.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO) ";
		sql+="JOIN PROJUDI.PROC p ON (p.ID_PROC = pa.ID_PROC) ";
		sql+="JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
		sql+="JOIN PROJUDI.SERV s ON (p.ID_SERV = s.ID_SERV) ";
		sql+="JOIN PROJUDI.SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO) ";
        sql+="JOIN PROJUDI.COMARCA c ON (s.ID_COMARCA = c.ID_COMARCA)";
		sql+="JOIN PROJUDI.AREA a ON (a.ID_AREA = p.ID_AREA) ";			
		sql+="JOIN PROJUDI.PROC_TIPO pt ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
//		sql+="LEFT JOIN PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps ON ptps.ID_PROC_TIPO = pt.ID_PROC_TIPO AND ptps.ID_PROC_SUBTIPO <> 1 "; //excluindo processos do tipo NÃO CONTENCIOSO = 1 
		sql+="LEFT JOIN PROJUDI.PROC_PARTE pa1 ON pa1.ID_PROC = p.ID_PROC  "; 
		sql+=" WHERE ppt.PROC_PARTE_TIPO_CODIGO = ? ";																											ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		sql+= " AND pa.DATA_BAIXA IS NULL ";		
		sql+= "	AND ss.SERV_SUBTIPO_CODIGO NOT IN ( ";
		sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
		sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL);
		sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
		sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL);
		sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL); 	
		sql+= " ?,";																																			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL); 
		sql+= " ? ";																																			ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
		sql+= " )";
				
        sql+= " AND ps.PROC_STATUS_CODIGO IN( ";
        sql+= " ?,"; 																																			ps.adicionarLong(ProcessoStatusDt.ATIVO); 
        sql+= " ?,";																																			ps.adicionarLong(ProcessoStatusDt.SUSPENSO); 
        sql+= " ? ";																																			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);
        sql+= " ) ";
        		
		sql+= " AND pa.ID_PROC_PARTE_TIPO = ? "; 																												ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
		sql+= " AND pa1.ID_PROC_PARTE = (SELECT MIN(pp.ID_PROC_PARTE) FROM PROJUDI.PROC_PARTE pp WHERE pp.ID_PROC = p.ID_PROC AND pp.ID_PROC_PARTE_TIPO = ?) "; ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO); //ADVOGADO_PROMOVENTE
		
		if (certidao.isCivel()) {
			//valido para cada tipo de certidão os tipos de processos que não positivam 
			sql+= " AND not exists (SELECT 1 FROM cert_tipo_proc_tipo ctpt WHERE ctpt.id_cert_tipo = ? and  ctpt.id_proc_tipo = p.id_proc_tipo )";				ps.adicionarLong(CertidaoTipoDt.ID_CERTIDAO_CIVEL);
			sql+=" AND a.AREA_CODIGO = ?"; 																														ps.adicionarLong(AreaDt.CIVEL);			
		} else {
			//valido para cada tipo de certidão os tipos de processos que não positivam
			sql+= " AND not exists (SELECT 1 FROM cert_tipo_proc_tipo ctpt WHERE ctpt.id_cert_tipo = ? and  ctpt.id_proc_tipo = p.id_proc_tipo )";				ps.adicionarLong(CertidaoTipoDt.ID_CERTIDAO_CRIMINAL);
			sql+=" AND a.AREA_CODIGO = ?"; 																														ps.adicionarLong(AreaDt.CRIMINAL);
		}
		
		if (certidao.getId_Comarca() != null && certidao.getId_Comarca().trim().length() > 0) {
			sql+= " AND (c.ID_COMARCA = ? OR EXISTS (SELECT 1 ";																								ps.adicionarLong(certidao.getId_Comarca());
			sql+= "                                    FROM PROJUDI.RECURSO REC INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM ";
			sql+= "                                   WHERE REC.ID_PROC = P.ID_PROC ";
			sql+= "                                    AND SREC.ID_COMARCA = ?)) ";																				ps.adicionarLong(certidao.getId_Comarca());					 					
		} else if (certidao.getComarcaCodigo() != null && certidao.getComarcaCodigo().trim().length() > 0) {
			sql+= " AND (c.COMARCA_CODIGO = ? OR EXISTS (SELECT 1 ";																							ps.adicionarLong(certidao.getComarcaCodigo());
			sql+= "                                        FROM PROJUDI.RECURSO REC INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM ";
			sql+= "                                        INNER JOIN PROJUDI.COMARCA CREC ON CREC.ID_COMARCA = SREC.ID_COMARCA ";
			sql+= "                                       WHERE REC.ID_PROC = P.ID_PROC ";
			sql+= "                                        AND CREC.COMARCA_CODIGO = ?)) ";																		ps.adicionarLong(certidao.getComarcaCodigo()); 					
		}
			
		if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
			sql+= " AND ";
			String cpfCnpj = "";
			if (certidao.getCpfCnpj().length() <= 14) {
				cpfCnpj = "CPF";
				sql += " (Pa." + cpfCnpj + " = ? "; ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
			} else {
				cpfCnpj = "CNPJ";
				sql += " (radicalcnpj(Pa."+cpfCnpj+ ") = ?  "; ps.adicionarString(certidao.getRadicalCnpj().trim());
			}
		}
		sql+= " OR (pa.NOME_SIMPLIFICADO = ? "; 																												ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()));
		if (certidao.getNomeMae() != null && !certidao.getNomeMae().isEmpty() && certidao.getDataNascimento() != null && !certidao.getDataNascimento().isEmpty()) {
			sql+= " AND Pa.Nome_Mae = ? AND Pa.Data_Nascimento = ? "; 
			ps.adicionarString(certidao.getNomeMae().trim().toUpperCase());
			ps.adicionarDate(certidao.getDataNascimento());

			sql+= " ) OR (pa.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()));
		}
		sql+= " ) )";
		
		try{
			rs = consultar(sql, ps);
			while (rs.next()) {
				
				if (rs.getString("ID_PROC_SUBTIPO") != null && 
					rs.getString("ID_PROC_SUBTIPO").equals("4") &&
					certidao.getAreaCodigo() != null && 
					certidao.getAreaCodigo().trim().length() > 0 &&
					Funcoes.StringToInt(certidao.getAreaCodigo()) == Funcoes.StringToInt(AreaDt.CRIMINAL)) {
					temFimJudicial = false;
					continue;
				}
				ProcessoCertidaoPositivaNegativaDt dt = new ProcessoCertidaoPositivaNegativaDt();
				
				dt.setId_Processo(rs.getString("ID_PROC"));
				dt.setProcessoNumero(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR"));
				if (rs.getString("SERV_ORIGEM") != null) {
					dt.setServentia(rs.getString("SERV_ORIGEM"));
				} else {
					dt.setServentia(rs.getString("SERV"));	
				}
				dt.setIdProcessoPartePromovido((rs.getString("ID_PROC_PARTE")));
				dt.setPromovidoNome(rs.getString("NOME"));
				dt.setPromovidodaSexo(rs.getString("SEXO"));
				dt.setPromovidoCpf(rs.getString("CPF"));
				dt.setPromovidoCnpj(rs.getString("CNPJ"));
				dt.setPromovidoNomeMae(rs.getString("NOME_MAE"));
				dt.setCertidaoPromoventeNome(rs.getString("NOME_PROMOVENTE"));
				dt.setNomeAdvogadoPromovente(rs.getString("ADVOGADO_PROMOVENTE"));
				dt.setNomeAdvogadoPromovido(rs.getString("ADVOGADO_PROMOVIDO"));
				dt.setPromovidoDataNascimento(rs.getString("DATA_NASCIMENTO"));
				dt.setTipo(rs.getString("AREA_CODIGO"));
				dt.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
				dt.setProcessoTipo(rs.getString("PROC_TIPO"));
				dt.setComarca(rs.getString("COMARCA"));
				dt.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
				dt.setValor(rs.getString("VALOR"));
				dt.setSistema("PROJUDI");
				
				listaProcesso.add(dt);
			}

		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}

		if (certidao.getAreaCodigo() != null && 
			certidao.getAreaCodigo().trim().length() > 0 &&
			Funcoes.StringToInt(certidao.getAreaCodigo()) == Funcoes.StringToInt(AreaDt.CIVEL)) {
			//Verifico se tem averbação de custas
			listaProcesso.addAll(getProcessoAverbacaoCusta(certidao));	
		}		
		
		//Verifico se tem fins judiciais
		//Se a lista for vazia usa o temFimJudicial, senão é true
		if(listaProcesso.isEmpty()) {
			certidao.setFimJudicial(temFimJudicial);
		} else {
			certidao.setFimJudicial(true);
		}
		
		return listaProcesso;
	}
	
	/**
	 * Verifica se a certidão NP possui ou não fim judicial.
	 * @param idProcesso
	 * @return
	 * @throws Exception
	 */
	public boolean temFimJudicial(List idProcesso) throws Exception {
		boolean fimJudicial = false;
		if(idProcesso.isEmpty())
			return true;
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		//Arrumar:
		if (idProcesso.size() > 1000) {
			idProcesso = idProcesso.subList(0, 1000);
		}

		sql = "SELECT * FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO  ptps ";
		sql += "JOIN PROC_SUBTIPO  ps ON (ptps.ID_PROC_SUBTIPO = ps.ID_PROC_SUBTIPO) ";
		sql += "JOIN PROJUDI.PROC_TIPO pt on (pt.ID_PROC_TIPO = ptps.ID_PROC_TIPO) ";
		sql += "JOIN PROJUDI.PROC p ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
		sql += "JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
		sql += "WHERE p.ID_PROC IN ( ";
		for (int i =0; i < idProcesso.size() - 1; i++) {
			sql += "?, ";
			ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) idProcesso.get(i)).getId_Processo());
		}
		sql += " ?) ";
		ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) idProcesso.get(idProcesso.size() - 1)).getId_Processo());
		sql+="AND pt.ID_PROC_TIPO NOT IN (SELECT  pt2.ID_PROC_TIPO "; 
		sql+="                              FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps2 "; 
		sql+="                             JOIN PROJUDI.PROC_SUBTIPO ps2 ON(ptps2.ID_PROC_SUBTIPO = ps2.ID_PROC_SUBTIPO) "; 
		sql+="                             JOIN PROJUDI.PROC_TIPO pt2 ON (pt2.ID_PROC_TIPO = ptps2.id_proc_tipo) "; 
		sql+="                              WHERE ps2.PROC_SUBTIPO_CODIGO = ?) ";		
		ps.adicionarLong(ProcessoSubtipoDt.NAO_TEM_FIM_JUDICIAL);
		sql += " AND ps.PROC_STATUS_CODIGO IN(?,?) "; ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO);
		
		try{
			rs = consultar(sql, ps);
			if (rs.next()) {
				fimJudicial = true;				
			}

		} finally {
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
		return fimJudicial;
	}
	
	/**
	 * Consultar partes dos processos ProcessoCertidaoPositivaNegativaDt
	 * 
	 * @param listaProcesso: Lista de processos do Projudi
	 * @author jpcpresa
	 */
	public List getPartes(List listaProcesso) throws Exception {

		if (listaProcesso.isEmpty())
			return listaProcesso;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pp.NOME, pp.ID_PROC, pp.ID_PROC_PARTE, ppt.PROC_PARTE_TIPO_CODIGO, u.NOME as ADV , ppa.ID_PROC_PARTE_ADVOGADO, pp.DATA_BAIXA, ppa.DATA_SAIDA ";
		Sql += "FROM PROJUDI.PROC_PARTE pp "; 
		Sql += "JOIN PROC_PARTE_TIPO ppt ON pp.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO " ;
		Sql += "JOIN PROJUDI.PROC p ON (pp.ID_PROC = p.ID_PROC) ";
		Sql += "JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
		Sql += "LEFT JOIN PROC_PARTE_ADVOGADO ppa on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE ";
		Sql += "LEFT JOIN USU_SERV US ON PPA.ID_USU_SERV = US.ID_USU_SERV ";
		Sql += "LEFT JOIN SERV S ON S.ID_SERV = US.ID_SERV ";
		Sql += "LEFT JOIN USU U ON US.ID_USU = U.ID_USU ";
		Sql += "WHERE pp.ID_PROC in ( ";
		for (int i =0; i < listaProcesso.size() - 1; i++) {
			Sql += "?, ";
			ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(i)).getId_Processo());
		}
		Sql += " ?) ";
		ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(listaProcesso.size() - 1)).getId_Processo());
		Sql += " AND pp.DATA_BAIXA is null ";
		Sql += "ORDER BY pp.ID_PROC, pp.ID_PROC_PARTE";
		//System.out.println(Sql);
		try{
			rs1 = consultar(Sql, ps);
			String id_proc;
			if (rs1.next()) {
				id_proc = rs1.getString("ID_PROC");
			
			while (!rs1.isAfterLast()) {
				id_proc = rs1.getString("ID_PROC");
				ProcessoCertidaoPositivaNegativaDt dt = getProcCertNegPos(listaProcesso, id_proc);
				while (rs1.getString("ID_PROC").equals(id_proc)) {
					if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equals("1") && dt != null) {
						dt.addPromovente(rs1.getString("NOME"));
						if (rs1.getString("ADV") != null && rs1.getString("DATA_SAIDA") == null)
							dt.addPromoventeAdvogado(rs1.getString("ADV"));
					} else {
						if(dt.getPromovidoNome() == null || dt.getPromovidoNome().isEmpty())
							dt.setPromovidoNome(rs1.getString("NOME"));
						if (rs1.getString("ADV") != null && dt != null && rs1.getString("DATA_SAIDA") == null)
							if (dt.getPromovidoNome() != null && !dt.getPromovidoNome().isEmpty())
							dt.addPromovidoAdvogado(rs1.getString("ADV"));
					} 
					
					rs1.next();
					if (rs1.isAfterLast()) {
						break;
					}
					
				}

			}
		}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaProcesso;
	}
	
	/**
	 * Consultar partes dos processos ProcessoCertidaoPositivaNegativaDt
	 * 
	 * @param listaProcesso: Lista de processos do Projudi
	 * @author jpcpresa
	 */
	public List getPartesCompleto(List listaProcesso) throws Exception {
		
		if (listaProcesso.isEmpty())
			return listaProcesso;

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT pp.NOME, pp.ID_PROC, pp.ID_PROC_PARTE, ppt.PROC_PARTE_TIPO_CODIGO, u.NOME as ADV , ppa.ID_PROC_PARTE_ADVOGADO, pp.DATA_BAIXA ";
		Sql += "FROM PROJUDI.PROC_PARTE pp "; 
		Sql += "JOIN PROC_PARTE_TIPO ppt ON pp.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO " ;
		Sql += "JOIN PROJUDI.PROC p ON (pp.ID_PROC = p.ID_PROC) ";
		Sql += "JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
		Sql += "LEFT JOIN PROC_PARTE_ADVOGADO ppa on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE ";
		Sql += "LEFT JOIN USU_SERV US ON PPA.ID_USU_SERV = US.ID_USU_SERV ";
		Sql += "LEFT JOIN SERV S ON S.ID_SERV = US.ID_SERV ";
		Sql += "LEFT JOIN USU U ON US.ID_USU = U.ID_USU ";		
		Sql += "WHERE pp.ID_PROC in ( ";
		for (int i =0; i < listaProcesso.size() - 1; i++) {
			Sql += "?, ";
			ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(i)).getId_Processo());
		}
		Sql += " ?) ";
		ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(listaProcesso.size() - 1)).getId_Processo());
		Sql += " AND pp.DATA_BAIXA is null AND ppa.DATA_SAIDA is null ";
		Sql += " AND ps.PROC_STATUS_CODIGO IN(?,?) "; ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO);
		Sql += " ORDER BY pp.ID_PROC, pp.ID_PROC_PARTE";		
		try{
			rs1 = consultar(Sql, ps);
			rs1.next();
			String id_proc = rs1.getString("ID_PROC");
			
			while (!rs1.isAfterLast()) {
				id_proc = rs1.getString("ID_PROC");
				ProcessoCertidaoPositivaNegativaDt dt = getProcCertNegPos(listaProcesso, id_proc);
				while (rs1.getString("ID_PROC").equals(id_proc)) {
					if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equals("1") && dt != null) {
						dt.addPromovente(rs1.getString("NOME"));
						if (rs1.getString("ADV") != null)
							dt.addPromoventeAdvogado(rs1.getString("ADV"));
					} else {
						if(dt.getPromovidoNome() == null || dt.getPromovidoNome().isEmpty())
						dt.setPromovidoNome(rs1.getString("NOME"));
						if (rs1.getString("ADV") != null && dt != null)
							dt.addPromovidoAdvogado(rs1.getString("ADV"));
					}
					rs1.next();
					if (rs1.isAfterLast()) {
						break;
					}
				}
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaProcesso;
	}
	
	public ProcessoCertidaoPositivaNegativaDt getProcCertNegPos(List listaProcesso,String id_proc) {
		ProcessoCertidaoPositivaNegativaDt dt = null;
		for (int i = 0; i < listaProcesso.size(); i++) {
			if (((ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(i)).getId_Processo().equals(id_proc)) {
				dt = (ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(i);
			}
		}
		return dt;
	}
	
	public List getProcessoAverbacaoCusta(CertidaoNegativaPositivaDt certidao) throws Exception {
		
		List listaProcesso = new ArrayList();		
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", "");

		sql ="SELECT pa.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, pa.SEXO, pa.ID_PROC_PARTE, pa.NOME, pa.CPF, pa.CNPJ, pa.NOME_MAE, pa.DATA_NASCIMENTO, ppt.PROC_PARTE_TIPO_CODIGO, pt.PROC_TIPO_CODIGO, s.SERV, "
				+ "TO_CHAR(p.DATA_RECEBIMENTO, 'DD/MM/RRRR HH24:MI:SS') AS DATA_RECEBIMENTO, TO_CHAR(p.VALOR, '999G999G999G990D00') AS VALOR, pt.PROC_TIPO ";
		sql+="FROM PROJUDI.PROC_PARTE pa ";
		sql+="JOIN PROJUDI.PROC_PARTE_TIPO ppt ON(pa.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO) ";
		sql+="JOIN PROJUDI.PROC p ON (pa.ID_PROC = p.ID_PROC) ";
		sql+="JOIN PROJUDI.PROC_PARTE_DEBITO pd ON (pd.ID_PROC_PARTE = pa.ID_PROC_PARTE) ";
		sql+="JOIN PROJUDI.AREA a ON (a.ID_AREA = p.ID_AREA) ";
		sql+="JOIN PROJUDI.SERV s ON (s.ID_SERV = p.ID_SERV) ";
		sql+="JOIN PROJUDI.COMARCA c ON (c.ID_COMARCA = s.ID_COMARCA) ";
		sql+="JOIN PROJUDI.PROC_TIPO pt ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
		sql+=" WHERE a.AREA_CODIGO = ? 	"; ps.adicionarLong(Funcoes.StringToInt(certidao.getAreaCodigo()));
		sql+=" AND pd.ID_PROC_DEBITO_STATUS = 1 ";
		if (certidao.getId_Comarca() != null && certidao.getId_Comarca().trim().length() > 0)
		{
			sql+= " AND c.ID_COMARCA = ?"; 
			ps.adicionarLong(certidao.getId_Comarca());
		} else if (certidao.getComarcaCodigo() != null && certidao.getComarcaCodigo().trim().length() > 0) {
			sql+= " AND c.COMARCA_CODIGO = ?"; 
			ps.adicionarLong(certidao.getComarcaCodigo());
		}
		sql+= " AND (";
		if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) 
		{
			String cpfCnpj = certidao.getCpfCnpj().length() <= 14 ? "CPF" : "CNPJ";
			sql += " (NOT Pa." + cpfCnpj + " IS NULL AND pa." + cpfCnpj + " = ?) "; ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
			sql += " OR ";
			sql+= " (";
			sql += " (Pa." + cpfCnpj + " IS NULL OR Pa." + cpfCnpj + " = ? ) AND "; ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));			
		} else {
			sql+= " (";
		}
		sql+= " pa.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()).trim().toUpperCase());
		sql+= "  )";
		sql+= " )";
		
		
		try{
			rs = consultar(sql, ps);
			while (rs.next()) {
				ProcessoCertidaoPositivaNegativaDebitoDt dt = new ProcessoCertidaoPositivaNegativaDebitoDt();
				dt.setId_Processo(rs.getString("ID_PROC"));
				dt.setParteAverbacaoNome(rs.getString("NOME"));
				dt.setParteAverbacaoSexo(rs.getString("SEXO"));
				dt.setParteAverbacaoCPF(rs.getString("CPF"));
				dt.setParteAverbacaoCNPJ(rs.getString("CNPJ"));
				dt.setParteAverbcaoNomeMae(rs.getString("NOME_MAE"));
				dt.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
				dt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				dt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				dt.setAno(rs.getString("ANO"));
				dt.setServentia(rs.getString("SERV"));
				dt.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
				dt.setValor(rs.getString("VALOR"));
				dt.setProcessoTipo(rs.getString("PROC_TIPO"));
				dt.setSistema("Projudi");
				
				listaProcesso.add(dt);
			}

		} finally {
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {}
		}
		return listaProcesso;
	}
	
	// Válido para CertidaoNegativaPositivaPublicaDt
	public List getProcessoAverbacaoCusta(CertidaoNegativaPositivaPublicaDt certidao) throws Exception {
		
		List listaProcesso = new ArrayList();		
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", "");

		sql ="SELECT pa.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, pa.SEXO, pa.ID_PROC_PARTE, pa.NOME, pa.CPF, pa.CNPJ, pa.NOME_MAE, pa.DATA_NASCIMENTO, ppt.PROC_PARTE_TIPO_CODIGO, pt.PROC_TIPO_CODIGO, s.SERV, "
				+ "TO_CHAR(p.DATA_RECEBIMENTO, 'DD/MM/RRRR HH24:MI:SS') AS DATA_RECEBIMENTO, TO_CHAR(p.VALOR, '999G999G999G990D00') AS VALOR, pt.PROC_TIPO ";
		sql+="FROM PROJUDI.PROC_PARTE pa ";
		sql+="JOIN PROJUDI.PROC_PARTE_TIPO ppt ON(pa.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO) ";
		sql+="JOIN PROJUDI.PROC p ON (pa.ID_PROC = p.ID_PROC) ";
		sql+="JOIN PROJUDI.PROC_PARTE_DEBITO pd ON (pd.ID_PROC_PARTE = pa.ID_PROC_PARTE) ";
		sql+="JOIN PROJUDI.AREA a ON (a.ID_AREA = p.ID_AREA) ";
		sql+="JOIN PROJUDI.SERV s ON (s.ID_SERV = p.ID_SERV) ";
		sql+="JOIN PROJUDI.COMARCA c ON (c.ID_COMARCA = s.ID_COMARCA) ";
		sql+="JOIN PROJUDI.PROC_TIPO pt ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
		sql+=" WHERE a.AREA_CODIGO = ? 	"; ps.adicionarLong(Funcoes.StringToInt(certidao.getAreaCodigo()));
		sql+=" AND pd.ID_PROC_DEBITO_STATUS = 1 ";
		if (certidao.getId_Comarca() != null && certidao.getId_Comarca().trim().length() > 0)
		{
			sql+= " AND c.ID_COMARCA = ?"; 
			ps.adicionarLong(certidao.getId_Comarca());
		} else if (certidao.getComarcaCodigo() != null && certidao.getComarcaCodigo().trim().length() > 0) {
			sql+= " AND c.COMARCA_CODIGO = ?"; 
			ps.adicionarLong(certidao.getComarcaCodigo());
		}
		sql+= " AND (";
		if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) 
		{
			String cpfCnpj = certidao.getCpfCnpj().length() <= 14 ? "CPF" : "CNPJ";
			sql += " (NOT Pa." + cpfCnpj + " IS NULL AND pa." + cpfCnpj + " = ?) "; ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
			sql += " OR ";
			sql+= " (";
			sql += " (Pa." + cpfCnpj + " IS NULL OR Pa." + cpfCnpj + " = ? ) AND "; ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));			
		} else {
			sql+= " (";
		}
		sql+= " pa.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()).trim().toUpperCase());
		sql+= "  )";
		sql+= " )";
		
		
		try{
			rs = consultar(sql, ps);
			while (rs.next()) {
				ProcessoCertidaoPositivaNegativaDebitoDt dt = new ProcessoCertidaoPositivaNegativaDebitoDt();
				dt.setId_Processo(rs.getString("ID_PROC"));
				dt.setParteAverbacaoNome(rs.getString("NOME"));
				dt.setParteAverbacaoSexo(rs.getString("SEXO"));
				dt.setParteAverbacaoCPF(rs.getString("CPF"));
				dt.setParteAverbacaoCNPJ(rs.getString("CNPJ"));
				dt.setParteAverbcaoNomeMae(rs.getString("NOME_MAE"));
				dt.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
				dt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				dt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				dt.setAno(rs.getString("ANO"));
				dt.setServentia(rs.getString("SERV"));
				dt.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
				dt.setValor(rs.getString("VALOR"));
				dt.setProcessoTipo(rs.getString("PROC_TIPO"));
				dt.setSistema("Projudi");
				
				listaProcesso.add(dt);
			}

		} finally {
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {}
		}
		return listaProcesso;
	}

	public List consultarProcessosCertidaoNPSegundoGrau(CertidaoSegundoGrauNegativaPositivaDt certidao) throws Exception {
		List listaProcesso = new ArrayList();		
		StringBuffer sql = new StringBuffer();
		String cpf = certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", "");
		ResultSetTJGO rs = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String cpfCnpj = "CPF";
		if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 11)) 
			cpfCnpj = "CNPJ";			  
				
		sql.append("SELECT " +
				//esse distinct deve ser retirado se for obrigatório informar o relator do processo na certidão
				" distinct " + 
				"p.id_proc AS ID_PROC, promovido.id_proc_parte AS ID_PROC_PARTE, promovido.nome AS APELADO, promovente.nome AS APELANTE, promovido.sexo AS SEXO, promovido.cpf AS CPF, " +
				"promovido.cnpj AS CNPJ, promovido.nome_mae AS NOME_MAE, promovido.DATA_NASCIMENTO, p.proc_numero AS PROC_NUMERO, p.digito_verificador AS DIGITO_VERIFICADOR, " +
				"pt.proc_tipo AS PROC_TIPO, '' AS RELATOR, p.forum_codigo AS FORUM_CODIGO, p.ano AS ANO, s.serv AS SERV, pf.proc_fase AS PROC_FASE " +
				"FROM ( " +
				"	SELECT p.id_proc, p.id_serv,(SELECT MIN(pp.id_proc_parte) " +
				"									FROM projudi.proc_parte pp " +
				"									INNER JOIN projudi.proc_parte_tipo ppt ON ppt.id_proc_parte_tipo = pp.id_proc_parte_tipo AND ppt.proc_parte_tipo_codigo = ? " ); ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
		sql.append(" 								WHERE p.id_proc = pp.id_proc " +
				"										AND pp.data_baixa IS NULL ) AS id_promovente," +
				" pp2.id_proc_parte AS id_promovido " +
				"	FROM projudi.proc p " +
				"	INNER JOIN projudi.proc_parte pp2 ON pp2.id_proc = p.id_proc " +
				"	INNER JOIN projudi.proc_parte_tipo ppt2 ON ppt2.id_proc_parte_tipo = pp2.id_proc_parte_tipo  AND ppt2.proc_parte_tipo_codigo = ? "); ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		sql.append("INNER JOIN projudi.serv s ON p.id_serv = s.id_serv " +
				"	INNER JOIN projudi.serv_subtipo sst ON sst.id_serv_subtipo = s.id_serv_subtipo " +
				"	INNER JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status " +
				" 	WHERE (pp2.NOME_SIMPLIFICADO like ? "); ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()));
		sql.append("	OR pp2."+ cpfCnpj +" like ?) "); ps.adicionarLong(cpf);
		sql.append("	AND sst.SERV_SUBTIPO_CODIGO IN (?,?,?,?,?,?)");  ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL); ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL); ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL); ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
		sql.append("    AND pp2.DATA_BAIXA IS NULL " +
				"		AND ps.PROC_STATUS_CODIGO IN(?,?) "); ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO);	
		sql.append(" 	AND p.data_arquivamento IS NULL ");

		if (certidao.isCivel(certidao.getArea())) {
			sql.append(" AND p.id_area = ? " );
			ps.adicionarLong(AreaDt.CIVEL);
		} else if (certidao.isCriminal(certidao.getArea())) {
			sql.append(" AND p.id_area = ? " );
			ps.adicionarLong(AreaDt.CRIMINAL);
		}
		
		sql.append("	AND NOT EXISTS (SELECT 1 FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL AND r.id_serv_recurso = p.id_serv) " +
				" 		AND p.ID_PROC_TIPO NOT IN (SELECT ptps2.ID_PROC_TIPO FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps2 JOIN PROJUDI.PROC_SUBTIPO ps2 ON (ptps2.ID_PROC_SUBTIPO = ps2.ID_PROC_SUBTIPO) " +
				"										WHERE ps2.PROC_SUBTIPO_CODIGO = ?) "); ps.adicionarLong(ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO);
		sql.append("UNION " +
				"SELECT r.id_proc, r.id_serv_recurso as id_serv, " +
				"	(SELECT MIN(pp.id_proc_parte) " +
				"		FROM projudi.recurso_parte rp " +
				"		INNER JOIN projudi.proc_parte_tipo ppt ON ppt.id_proc_parte_tipo = rp.id_proc_parte_tipo AND ppt.proc_parte_tipo_codigo = ? "); ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO); 
		sql.append("	INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = rp.id_proc_parte WHERE r.id_recurso = rp.id_recurso AND rp.data_baixa IS NULL ) AS id_promovente, " +
				"	pp2.id_proc_parte AS id_promovido " +
				"	FROM projudi.recurso r " +
				"	INNER JOIN projudi.recurso_parte rp2 ON rp2.id_recurso = r.id_recurso " +
				"	INNER JOIN projudi.proc_parte_tipo ppt2 ON ppt2.id_proc_parte_tipo = rp2.id_proc_parte_tipo AND ppt2.proc_parte_tipo_codigo = ? "); ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		sql.append("INNER JOIN projudi.proc_parte pp2 ON pp2.id_proc_parte = rp2.id_proc_parte " +
				"	INNER JOIN projudi.proc p ON p.id_proc = r.id_proc and p.id_serv = r.id_serv_recurso " +
				"	INNER JOIN projudi.serv s ON r.id_serv_recurso = s.id_serv " +
				"	INNER JOIN projudi.serv_subtipo sst ON sst.id_serv_subtipo = s.id_serv_subtipo " +
				"	INNER JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status " +
				" 	WHERE (pp2.NOME_SIMPLIFICADO like ? "); ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()));
		sql.append("	OR pp2."+ cpfCnpj +" like ?) "); ps.adicionarLong(cpf);
		sql.append("	AND sst.SERV_SUBTIPO_CODIGO IN (?,?,?,?,?,?)");  ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL); ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL); ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
		sql.append("    AND rp2.DATA_BAIXA IS NULL ");

		if (certidao.isCivel(certidao.getArea())) {
			sql.append(" AND p.id_area = ? " );
			ps.adicionarLong(AreaDt.CIVEL);
		} else if (certidao.isCriminal(certidao.getArea())) {
			sql.append(" AND p.id_area = ? " );
			ps.adicionarLong(AreaDt.CRIMINAL);
		}
		
		sql.append("	AND ps.PROC_STATUS_CODIGO IN(?,?) "); ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO);	
		sql.append(" 	AND r.data_retorno IS NULL " +
				"		AND p.ID_PROC_TIPO NOT IN (SELECT ptps2.ID_PROC_TIPO FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps2 JOIN PROJUDI.PROC_SUBTIPO ps2 ON (ptps2.ID_PROC_SUBTIPO = ps2.ID_PROC_SUBTIPO) " +
				"									WHERE ps2.PROC_SUBTIPO_CODIGO = ?)"); ps.adicionarLong(ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO);
		sql.append(") tab " +
				"INNER JOIN projudi.proc p ON p.id_proc = tab.id_proc " +
				"INNER JOIN projudi.proc_parte promovente ON promovente.id_proc_parte = tab.id_promovente " +
				"INNER JOIN projudi.proc_parte promovido ON promovido.id_proc_parte = tab.id_promovido " +
				//o trecho comentado abaixo só será descomentado SE for obrigatório informar o nome do relator do processo
				//mesmo assim, vai acontecer duplicidade de relator quando o processo tiver dois recursos ativos.
				//"LEFT JOIN PROJUDI.PROC_RESP pr ON p.ID_PROC = pr.ID_PROC AND pr.redator = 1 AND pr.codigo_temp <> -1 " +
				//"INNER JOIN PROJUDI.SERV_CARGO sc ON pr.ID_SERV_CARGO = sc.ID_SERV_CARGO --and tab.id_serv = sc.id_serv " +
				//"INNER JOIN PROJUDI.CARGO_TIPO ct ON pr.ID_CARGO_TIPO = ct.ID_CARGO_TIPO AND ct.CARGO_TIPO_CODIGO = 8 -- ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU); " +
				//"INNER JOIN PROJUDI.USU_SERV_GRUPO usg ON usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO " +
				//"INNER JOIN PROJUDI.USU_SERV us ON usg.ID_USU_SERV = us.ID_USU_SERV " +
				//"INNER JOIN PROJUDI.USU u ON u.ID_USU = us.ID_USU " +
				"INNER JOIN projudi.serv s ON s.id_serv = tab.id_serv " +
				"INNER JOIN projudi.proc_fase pf ON pf.id_proc_fase = p.id_proc_fase " +
				"INNER JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = p.id_proc_tipo");

		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				ProcessoCertidaoSegundoGrauPositivaNegativaDt dt = new ProcessoCertidaoSegundoGrauPositivaNegativaDt();
				dt.setId_Processo(rs.getString("ID_PROC"));
				dt.setIdProcessoPartePromovido((rs.getString("ID_PROC_PARTE")));
				dt.setPromovidoNome(rs.getString("APELADO"));
				dt.setPromoventeNome(rs.getString("APELANTE"));
				dt.setRequerente(rs.getString("APELANTE"));
				dt.setPromovidodaSexo(rs.getString("SEXO"));
				dt.setPromovidoCpf(rs.getString("CPF"));
				dt.setPromovidoCnpj(rs.getString("CNPJ"));
				dt.setPromovidoNomeMae(rs.getString("NOME_MAE"));
				dt.setPromovidoDataNascimento(rs.getString("DATA_NASCIMENTO"));
				dt.setProcessoNumero(rs.getString("PROC_NUMERO"));
		        dt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
		        dt.setProcessoTipo(rs.getString("PROC_TIPO"));
		        dt.setRelator(rs.getString("RELATOR"));
		        dt.setForumCodigo(rs.getString("FORUM_CODIGO"));
		        dt.setAno(rs.getString("ANO"));
		        dt.setCamara(rs.getString("SERV"));
		        dt.setFaseAtual(rs.getString("PROC_FASE"));
		        dt.setSistema("Projudi");
		        
				listaProcesso.add(dt);
			}
			
		} finally {
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {}
		}
		
		return listaProcesso;
	}
	
	public boolean negativarPFisicaPJuricaSegundoGrau(CertidaoNegativaPositivaPublicaDt certidao) throws Exception {
		boolean retorno;
		String sql;	
		ResultSetTJGO rs = null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String cpfCnpj = "CPF";
		if (certidao.isEhPessoaJuridica()) 	cpfCnpj = "CNPJ";
		
		sql ="SELECT ";  
		sql +="  p.id_proc AS ID_PROC, promovido.id_proc_parte AS ID_PROC_PARTE, promovido.nome AS APELADO, promovente.nome AS APELANTE, promovido.sexo AS SEXO, ";
		sql +="  promovido.cpf AS CPF, promovido.cnpj AS CNPJ, promovido.nome_mae AS NOME_MAE, promovido.DATA_NASCIMENTO, p.proc_numero AS PROC_NUMERO, ";
		sql +="  p.digito_verificador AS DIGITO_VERIFICADOR, pt.proc_tipo AS PROC_TIPO, '' AS RELATOR, p.forum_codigo AS FORUM_CODIGO, p.ano AS ANO, s.serv AS SERV, ";
		sql +="  pf.proc_fase AS PROC_FASE ";
		sql +=" FROM ( ";
		sql +="         SELECT p.id_proc, p.id_serv,(SELECT MIN(pp.id_proc_parte) ";
		sql +="                                      FROM projudi.proc_parte pp ";
		sql +="                                      INNER JOIN projudi.proc_parte_tipo ppt ON ppt.id_proc_parte_tipo = pp.id_proc_parte_tipo AND ppt.proc_parte_tipo_codigo = ? "; ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
		sql +="                                      WHERE p.id_proc = pp.id_proc 	AND pp.data_baixa IS NULL ) AS id_promovente, "; 
		sql +="                pp2.id_proc_parte AS id_promovido "; 	
		sql +="         FROM projudi.proc p ";
		sql +="         INNER JOIN projudi.proc_parte pp2 ON pp2.id_proc = p.id_proc ";
		sql +="         INNER JOIN projudi.proc_parte_tipo ppt2 ON ppt2.id_proc_parte_tipo = pp2.id_proc_parte_tipo  AND ppt2.proc_parte_tipo_codigo = ? "; ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		sql +="         INNER JOIN projudi.serv s ON p.id_serv = s.id_serv 	INNER JOIN projudi.serv_subtipo sst ON sst.id_serv_subtipo = s.id_serv_subtipo ";
		sql +="         INNER JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status ";
		
		sql +="         WHERE ( ";
		if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
			sql +="				(NOT pp2." + cpfCnpj + " IS NULL AND pp2." + cpfCnpj + " = ?) ";  ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
			sql += "            OR ( ";
			sql += "                 (pp2." + cpfCnpj + " IS NULL OR pp2." + cpfCnpj + " = ? ) AND "; ps.adicionarLong(0);
		} else {
			sql+= "             ( ";
		}
		sql+= "                 pp2.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()).trim().toUpperCase());
		if (certidao.getNomeMae() != null && !certidao.getNomeMae().isEmpty() && certidao.getDataNascimento() != null && !certidao.getDataNascimento().isEmpty()){
			sql+= " 					  AND ((pp2.Nome_Mae = ? OR pp2.Nome_Mae IS NULL) AND (pp2.Data_Nascimento = ? OR pp2.Data_Nascimento IS NULL))"; 
										ps.adicionarString(certidao.getNomeMae().trim().toUpperCase());
										ps.adicionarDate(certidao.getDataNascimento());
		}
		sql +="                    ) ";
		sql +="				  )";
		
		sql +="               AND sst.SERV_SUBTIPO_CODIGO IN (?,?,?,?,?,?) "; 
							  ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL); ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);  ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);    ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
	    if (certidao.isCivel()) {
	    	sql +="           AND p.ID_AREA = ? ";
			ps.adicionarLong(AreaDt.CIVEL);
		} else if (certidao.isCriminal()) {
			sql +="           AND p.ID_AREA = ? ";
			ps.adicionarLong(AreaDt.CRIMINAL);
		}
		sql +="               AND pp2.DATA_BAIXA IS NULL AND p.data_arquivamento IS NULL ";
		sql +="				  AND ps.PROC_STATUS_CODIGO IN(?,?,?)  	 ";  
							  ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO); ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);
		sql +="               AND NOT EXISTS (SELECT 1 ";
		sql +="                               FROM projudi.recurso r ";
		sql +="                               WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL AND r.id_serv_recurso = p.id_serv) ";
		sql +="               AND p.ID_PROC_TIPO NOT IN (SELECT ptps2.ID_PROC_TIPO ";
		sql +="                                          FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps2 ";
		sql +="                                          JOIN PROJUDI.PROC_SUBTIPO ps2 ON (ptps2.ID_PROC_SUBTIPO = ps2.ID_PROC_SUBTIPO) ";
		sql +="                                           WHERE ps2.PROC_SUBTIPO_CODIGO = ?) ";  ps.adicionarLong(ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO);
	                                                                                                  
		sql +="         UNION ";
				
		sql +="         SELECT r.id_proc, r.id_serv_recurso as id_serv, (SELECT MIN(pp.id_proc_parte) "; 		
		sql +="                                           				 FROM projudi.recurso_parte rp ";
		sql +="                                           				 INNER JOIN projudi.proc_parte_tipo ppt ON ppt.id_proc_parte_tipo = rp.id_proc_parte_tipo AND ppt.proc_parte_tipo_codigo = ? "; ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
		sql +="                                           				 INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = rp.id_proc_parte ";
		sql +="                                           				WHERE r.id_recurso = rp.id_recurso AND rp.data_baixa IS NULL ) AS id_promovente, ";	
		sql +="                pp2.id_proc_parte AS id_promovido 	";
		sql +="         FROM projudi.recurso r ";
		sql +="         INNER JOIN projudi.recurso_parte rp2 ON rp2.id_recurso = r.id_recurso ";
		sql +="         INNER JOIN projudi.proc_parte_tipo ppt2 ON ppt2.id_proc_parte_tipo = rp2.id_proc_parte_tipo AND ppt2.proc_parte_tipo_codigo = ? "; ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		sql +="         INNER JOIN projudi.proc_parte pp2 ON pp2.id_proc_parte = rp2.id_proc_parte ";	
		sql +="         INNER JOIN projudi.proc p ON p.id_proc = r.id_proc and p.id_serv = r.id_serv_recurso ";
		sql +="         INNER JOIN projudi.serv s ON r.id_serv_recurso = s.id_serv ";
		sql +="         INNER JOIN projudi.serv_subtipo sst ON sst.id_serv_subtipo = s.id_serv_subtipo ";
		sql +="         INNER JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status ";
		sql +="         WHERE ( ";
		if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) {
			sql +="				(NOT pp2." + cpfCnpj + " IS NULL AND pp2." + cpfCnpj + " = ?) ";  ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
			sql += "            OR ( ";
			sql += "                 (pp2." + cpfCnpj + " IS NULL OR pp2." + cpfCnpj + " = ? ) AND "; ps.adicionarLong(0);
		} else {
			sql+= "             ( ";
		}
		sql+= "                 pp2.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()).trim().toUpperCase());
		if (certidao.getNomeMae() != null && !certidao.getNomeMae().isEmpty() && certidao.getDataNascimento() != null && !certidao.getDataNascimento().isEmpty()){
			sql+= " 					  AND ((pp2.Nome_Mae = ? OR pp2.Nome_Mae IS NULL) AND (pp2.Data_Nascimento = ? OR pp2.Data_Nascimento IS NULL))"; 
										ps.adicionarString(certidao.getNomeMae().trim().toUpperCase());
										ps.adicionarDate(certidao.getDataNascimento());
		}
		sql +="                    ) ";
		sql +="				  )";
		sql +="               AND sst.SERV_SUBTIPO_CODIGO IN (?,?,?,?,?,?) "; 
		  					  ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL); ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL); ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL); ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA);
		if (certidao.isCivel()) {
	    	sql +="           AND p.ID_AREA = ? ";
			ps.adicionarLong(AreaDt.CIVEL);
		} else if (certidao.isCriminal()) {
			sql +="           AND p.ID_AREA = ? ";
			ps.adicionarLong(AreaDt.CRIMINAL);
		}
		sql +=" 			  AND rp2.DATA_BAIXA IS NULL AND r.data_retorno IS NULL ";
		sql +="				  AND ps.PROC_STATUS_CODIGO IN(?,?,?) ";  
		                      ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO); ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);
		sql +="               AND p.ID_PROC_TIPO NOT IN (SELECT ptps2.ID_PROC_TIPO ";
		sql +="                                           FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps2 ";
		sql +="                                           JOIN PROJUDI.PROC_SUBTIPO ps2 ON (ptps2.ID_PROC_SUBTIPO = ps2.ID_PROC_SUBTIPO) ";
		sql +="                                            WHERE ps2.PROC_SUBTIPO_CODIGO = ?) ";  ps.adicionarLong(ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO);
	                                      
		sql +="       ) tab ";
		sql +="        INNER JOIN projudi.proc p ON p.id_proc = tab.id_proc ";
		sql +="        INNER JOIN projudi.proc_parte promovente ON promovente.id_proc_parte = tab.id_promovente ";
		sql +="        INNER JOIN projudi.proc_parte promovido ON promovido.id_proc_parte = tab.id_promovido ";
		sql +="        INNER JOIN projudi.serv s ON s.id_serv = tab.id_serv ";
		sql +="        INNER JOIN projudi.proc_fase pf ON pf.id_proc_fase = p.id_proc_fase ";
		sql +="        INNER JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = p.id_proc_tipo ";
		
		

		try {
			rs = consultar(sql.toString(), ps);
			if (rs.next()) {
				retorno = false;
			} else {
				retorno = true;
			}
			
		} catch (Exception e) {
			throw new Exception(
					"<{Erro ao gerar certidão.}> Local Exception: "
							+ this.getClass().getName() + ".negativarPFisicaPJuricaSegundoGrau(): "
							+ e.getMessage(), e);
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e) {}
		}
		
		return retorno;
	}
	
	
	public ProcessoCertidaoSegundoGrauPositivaNegativaDt getProcSegundoGrauCertNegPos(List listaProcesso,String id_proc) {
		ProcessoCertidaoSegundoGrauPositivaNegativaDt dt = null;
		for (int i = 0; i < listaProcesso.size(); i++) {
			if (((ProcessoCertidaoSegundoGrauPositivaNegativaDt) listaProcesso.get(i)).getId_Processo().equals(id_proc)) {
				dt = (ProcessoCertidaoSegundoGrauPositivaNegativaDt) listaProcesso.get(i);
			}
		}
		return dt;
	}
	
		
	public List consultarProcessoCertidaoAntecedenteCriminal(String nome, String cpfCnpj, String dataNascimento, String nomeMae, String nomePai, String idNaturalidade, String rg, boolean menorInfrator) throws Exception {
		List processosCertidao =  new ArrayList();;
		ResultSetTJGO rs = null;
		String sql = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{
			sql = "SELECT ID_PROC, NOME, CNPJ, CPF,  RG, NOME_MAE, NOME_PAI, DATA_NASCIMENTO, SEXO, PROC_BENEFICIO, DATA_INICIAL_BENEFICIO, DATA_FINAL_BENEFICIO, PROC_NUMERO, "
					+ " DIGITO_VERIFICADOR, SEGREDO_JUSTICA, PROC_TIPO, ASSUNTO, VALOR, SERV, ANO, FORUM_CODIGO, DATA_TRANS_JULGADO, DATA_FATO, FASE, PROC_ARQUIVAMENTO_TIPO, "
					+ " SENTENCA , COMARCA, DATA_RECEBIMENTO, PROMOVENTE, STATUS, VITIMA, INFRACAO " +
					"FROM view_certidao_folha_corrida " +
					"WHERE " ;

			sql += " NOME_SIMPLIFICADO = ? "; 																					ps.adicionarString(Funcoes.converteNomeSimplificado(nome));
						
			sql += " AND SERV_SUBTIPO_CODIGO " + (!menorInfrator?"NOT ":"") + "IN (?) ";										ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
			
			//alteração solicitada no dia 03/11/2020 no Grupo Trabalho PJD-crime, autorizado pelo dr. Cláudio e Jesus.
			sql += " AND PROC_TIPO_CODIGO <> ? ";																				ps.adicionarLong(ProcessoTipoDt.CARTA_PRECATORIA_CPP);
			
//			//Processos arquivados há mais de 5 anos não entrarão nos antecedentes criminais
//			Calendar dataArquivamentoLimite = Calendar.getInstance();
//			dataArquivamentoLimite.add(Calendar.YEAR, -5);
//			dataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
//			dataArquivamentoLimite.add(Calendar.MINUTE, 0);
//			dataArquivamentoLimite.add(Calendar.SECOND, 0);
//			dataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
//			sql += " AND (DATA_ARQUIVAMENTO IS NULL OR DATA_ARQUIVAMENTO >= ? )";				ps.adicionarDate(dataArquivamentoLimite.getTime());
			
			//reunião com dr. Claudio e Juizes criminais, Anderson, Keila e Jesus 24/09/2020
			if((nomeMae != null && !nomeMae.isEmpty()) 
					|| (nomePai != null && !nomePai.isEmpty()) 
					|| (dataNascimento != null && !dataNascimento.isEmpty()) 
					|| (idNaturalidade != null && !idNaturalidade.isEmpty()) 
					|| (cpfCnpj != null && !cpfCnpj.isEmpty()) 
					|| (rg != null && !rg.isEmpty())) {
				sql += "AND ( ";
				String sqlOR = "";
				if(nomeMae != null && !nomeMae.isEmpty()) {
					sqlOR += " NOME_MAE = ? "; 																					ps.adicionarString(nomeMae.trim().toUpperCase());
				}
				
				if(nomePai != null && !nomePai.isEmpty()) { 
					if(!sqlOR.isEmpty()) {
						sqlOR += " OR ";
					}
					sqlOR += " NOME_PAI = ? "; 																					ps.adicionarString(nomePai.trim().toUpperCase());
				}
				
				if(dataNascimento != null && !dataNascimento.isEmpty()) {
					if(!sqlOR.isEmpty()) {
						sqlOR += " OR ";
					}
					sqlOR += " DATA_NASCIMENTO = ? "; 																			ps.adicionarDate(dataNascimento);
				}
				
				if(idNaturalidade != null && !idNaturalidade.isEmpty()) {
					if(!sqlOR.isEmpty()) {
						sqlOR += " OR ";
					}
					sqlOR += " ID_NATURALIDADE = ? "; 																			ps.adicionarLong(idNaturalidade);
				}
				
				if (cpfCnpj != null && !cpfCnpj.isEmpty()) {
					String cpfOuCnpj = "CPF";
					cpfOuCnpj = cpfCnpj.length() <= 14 ? "CPF" : "CNPJ";
					if(!sqlOR.isEmpty()) {
						sqlOR += " OR ";
					}
					sqlOR += cpfOuCnpj + " = ? "; 																				ps.adicionarLong(cpfCnpj.replaceAll("[\\-\\.\\/]", ""));
				}
				
				if(rg != null && !rg.isEmpty()) { 
					if(!sqlOR.isEmpty()) {
						sqlOR += " OR ";
					}
					sqlOR += " RG = ? "; 																						ps.adicionarString(rg);
				}
				
				sql += sqlOR + ") ";
			}
			
			sql += " ORDER BY DATA_RECEBIMENTO, ID_PROC ";
			
			rs = this.consultar(sql, ps);
			ProcessoAntecedenteCriminalDt processoAux = null;
			if(rs.next())
				while(!rs.isAfterLast()) {
					processoAux = new ProcessoAntecedenteCriminalDt();
					processoAux.setSistema("Projudi");
					processoAux.setId_Processo(rs.getString("ID_PROC"));
					processoAux.setPromovidoNome(rs.getString("NOME"));
					processoAux.setPromovidoCnpj(rs.getString("CNPJ"));
					processoAux.setPromovidoCpf(rs.getString("CPF"));
					processoAux.setPromovidoRg(rs.getString("RG"));
					processoAux.setPromovidoNomeMae(rs.getString("NOME_MAE"));
					processoAux.setPromovidoNomePai(rs.getString("NOME_PAI"));
					processoAux.setPromovidoDataNascimento(rs.getString("DATA_NASCIMENTO"));
					processoAux.setPromovidodaSexo(rs.getString("SEXO"));
					processoAux.setProcessoNumero(rs.getString("PROC_NUMERO"));
					processoAux.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
					processoAux.setAno(rs.getString("ANO"));
					processoAux.setForumCodigo(rs.getString("FORUM_CODIGO"));
					processoAux.setComarca(rs.getString("COMARCA"));
					processoAux.setServentia(rs.getString("SERV"));
					processoAux.setProcessoTipo(rs.getString("PROC_TIPO"));
					processoAux.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
					processoAux.setDataFato(Funcoes.FormatarData(rs.getDate("DATA_FATO")));
					processoAux.setPromovidoDataNascimento(Funcoes.FormatarData(rs.getDate("DATA_NASCIMENTO")));
					processoAux.setDataRecebimento(Funcoes.FormatarDataHora(rs.getString("DATA_RECEBIMENTO")));
					processoAux.setDataTransitoJulgado(Funcoes.FormatarData(rs.getDate("DATA_TRANS_JULGADO")));
					processoAux.setProcessoArquivamentoTipo(rs.getString("PROC_ARQUIVAMENTO_TIPO"));
					processoAux.setFase(rs.getString("FASE"));
					processoAux.setSegredoJustica(rs.getString("SEGREDO_JUSTICA").equals("1") ? true : false);
					processoAux.setNomeVitima(rs.getString("VITIMA"));
					processoAux.setSentenca(rs.getString("SENTENCA"));
					processoAux.setStatus(rs.getString("STATUS"));

					String  data_beneficio_inicio = null;
					String beneficio;
					String data_beneficio_fim;
					String data_beneficio_inicio_aux = null;
					String id_Processo = rs.getString("ID_PROC");
					if (rs.getString("PROC_BENEFICIO") != null		|| rs.getString("DATA_INICIAL_BENEFICIO") != null) {
						data_beneficio_inicio = Funcoes.FormatarData(rs.getDateTime("DATA_INICIAL_BENEFICIO"));
						data_beneficio_inicio_aux = rs.getString("DATA_INICIAL_BENEFICIO");
						beneficio = rs.getString("PROC_BENEFICIO");
						data_beneficio_fim = Funcoes.FormatarData(rs.getDateTime("DATA_FINAL_BENEFICIO"));
						processoAux.addBeneficio(new BeneficioCertidaoAntecedenteCriminalDt(data_beneficio_inicio, beneficio, data_beneficio_fim));
					}
					processoAux.addAssunto(rs.getString("ASSUNTO"));
					processoAux.addPromovente(rs.getString("PROMOVENTE"));
					processoAux.addInfracao(rs.getString("INFRACAO"));
					
					while (rs.next()&& rs.getString("ID_PROC").equals(id_Processo)) {
						processoAux.addAssunto(rs.getString("ASSUNTO"));
						processoAux.addPromovente(rs.getString("PROMOVENTE"));
						processoAux.addInfracao(rs.getString("INFRACAO"));
						//arrumar questão do beneficio iteração com a data inicial			
						if(rs.getString("DATA_INICIAL_BENEFICIO") != null && !rs.getString("DATA_INICIAL_BENEFICIO").equals(data_beneficio_inicio_aux)) {
							data_beneficio_inicio = Funcoes.FormatarData(rs.getDateTime("DATA_INICIAL_BENEFICIO"));
							beneficio = rs.getString("PROC_BENEFICIO");
							data_beneficio_fim = Funcoes.FormatarData(rs.getDateTime("DATA_FINAL_BENEFICIO"));
							processoAux.addBeneficio(new BeneficioCertidaoAntecedenteCriminalDt(data_beneficio_inicio, beneficio, data_beneficio_fim));
							data_beneficio_inicio_aux = rs.getString("DATA_INICIAL_BENEFICIO");
						}
					}
					
				processosCertidao.add(processoAux);
			}
			
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return processosCertidao;
	}
	
//	public List<String> consultarNomesComarcasComProcessosCertidaoPublicaNP(CertidaoNegativaPositivaPublicaDt certidao) throws Exception {
//		List<String> listaNomesComarcasComProcessos = new ArrayList<String>();
//		
//		if (!(certidao.getCpfCnpj().length() <= 14)) return listaNomesComarcasComProcessos;
//		
//		String sql;		
//		ResultSetTJGO rs = null;	
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		
//		sql ="SELECT pa.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, s.SERV, pa.SEXO, pa.ID_PROC_PARTE, pa.NOME, pa.CPF, pa.CNPJ, pa.NOME_MAE, pa.DATA_NASCIMENTO, c.COMARCA, a.AREA_CODIGO, pt.PROC_TIPO_CODIGO, pa.NOME_SIMPLIFICADO, ";
//		sql += "(Select Serv From Recurso R Inner Join Serv S On S.Id_Serv = R.Id_Serv_Origem Where R.Id_Proc = P.Id_Proc AND R.Data_Retorno IS NULL AND ROWNUM = 1) AS SERV_ORIGEM ";
//		sql+="FROM PROJUDI.PROC_PARTE pa ";
//		sql+="JOIN PROJUDI.PROC_PARTE_TIPO ppt ON(pa.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO) ";
//		sql+="JOIN PROJUDI.PROC p ON (p.ID_PROC = pa.ID_PROC) ";
//		sql+="JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
//		sql+="JOIN PROJUDI.SERV s ON (p.ID_SERV = s.ID_SERV) ";
//		sql+="JOIN PROJUDI.COMARCA c ON (s.ID_COMARCA = c.ID_COMARCA)";
//		sql+="JOIN PROJUDI.AREA a ON (a.ID_AREA = p.ID_AREA) ";			
//		sql+="JOIN PROJUDI.PROC_TIPO pt ON (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) ";
//		sql+="JOIN PROJUDI.SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO) ";
//		sql+=" WHERE ppt.PROC_PARTE_TIPO_CODIGO = ? "; ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
//		sql+= " AND pa.DATA_BAIXA IS NULL ";
//		sql+= " AND ss.SERV_SUBTIPO_CODIGO <> ?"; ps.adicionarLong(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL);
//		sql+= " AND ps.PROC_STATUS_CODIGO IN (?,?,?) "; ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.SUSPENSO); ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);
//		
//		if (certidao.getAreaCodigo() != null && certidao.getAreaCodigo().trim().length() > 0){
//			sql+=" AND a.AREA_CODIGO = ?"; 
//			ps.adicionarLong(Funcoes.StringToInt(certidao.getAreaCodigo()));
//			//Comentando o trecho do código abaixo e retornando o sistema para a normalidade (consultar a comarca de Goiânia)
//			//Processo administrativo 5360528/2015 e despachos 738/2015 da DI e 3509/2015 da Diretoria Geral.
////			if (certidao.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL))) {
////				sql+= " AND s.ID_COMARCA <> 12 "; //Retirando a consulta dos processos na comarca de Goiânia, conforme solicitação no processo número administrativo número 5360528...	
////			}			
//		}
//		
//		if (certidao.getTerritorio() != null && certidao.getTerritorio().trim().equalsIgnoreCase("C") && certidao.getId_Comarca() != null && certidao.getId_Comarca().trim().length() > 0)
//		{
//			sql+= " AND (c.ID_COMARCA = ? OR EXISTS (SELECT 1 ";
//			sql+= "                                    FROM PROJUDI.RECURSO REC INNER JOIN PROJUDI.SERV SREC ON SREC.ID_SERV = REC.ID_SERV_ORIGEM ";
//			sql+= "                                   WHERE REC.ID_PROC = P.ID_PROC ";
//			sql+= "                                    AND SREC.ID_COMARCA = ?)) "; 
//			ps.adicionarLong(certidao.getId_Comarca());
//			ps.adicionarLong(certidao.getId_Comarca());	
//		}
//		
//		if (certidao.isEhPessoaJuridica()) {
//			sql += " AND pa.CPF IS NULL "; 
//		} else {
//			sql += " AND pa.CNPJ IS NULL ";
//		}
//			
//        sql+= " AND (";
//		if ((certidao.getCpfCnpj() != null) && (certidao.getCpfCnpj().length() > 0)) 
//		{
//			String cpfCnpj = "CPF";			
//			if (certidao.isEhPessoaJuridica()) cpfCnpj = "CNPJ"; 
//			
//			sql += " (NOT Pa." + cpfCnpj + " IS NULL AND pa." + cpfCnpj + " = ?) "; ps.adicionarLong(certidao.getCpfCnpj().replaceAll("[\\-\\.\\/]", ""));
//			sql += " OR ";
//			sql+= " (";
//			sql += " (Pa." + cpfCnpj + " IS NULL OR Pa." + cpfCnpj + " = ? ) AND "; ps.adicionarLong(0);
//		
//		} else {
//			sql+= " (";
//		}
//		sql+= " pa.NOME_SIMPLIFICADO = ? "; ps.adicionarString(Funcoes.converteNomeSimplificado(certidao.getNome()).trim().toUpperCase());
//		if (certidao.getNomeMae() != null && !certidao.getNomeMae().isEmpty() && certidao.getDataNascimento() != null && !certidao.getDataNascimento().isEmpty())
//		{
//			sql+= " AND ((Pa.Nome_Mae = ? OR Pa.Nome_Mae IS NULL) AND (Pa.Data_Nascimento = ? OR Pa.Data_Nascimento IS NULL))"; 
//			ps.adicionarString(certidao.getNomeMae().trim().toUpperCase());
//			ps.adicionarDate(certidao.getDataNascimento());
//		}
//		sql+= "  )";
//		sql+= " )";
//		
//		try{
//			rs = consultar(sql, ps);
//			while (rs.next()) {
//				if (rs.getString("SERV_ORIGEM") != null) {
//					if (!listaNomesComarcasComProcessos.contains(rs.getString("SERV_ORIGEM")))
//						listaNomesComarcasComProcessos.add(rs.getString("SERV_ORIGEM"));
//				} else {
//					if (!listaNomesComarcasComProcessos.contains(rs.getString("SERV")))
//						listaNomesComarcasComProcessos.add(rs.getString("SERV"));					
//				}
//			}
//
//		} finally{
//			try{
//				if (rs != null)
//					rs.close();
//			} catch(Exception e) {
//			}
//		}
//		
//		return listaNomesComarcasComProcessos;
//	}
	
	public CertidaoValidacaoDt consultarNumeroGuia(String numeroGuiaCompleto)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CertidaoValidacaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_CERT WHERE NUMERO_GUIA_COMPLETO = ?";		ps.adicionarLong(numeroGuiaCompleto); 

		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CertidaoValidacaoDt();
				associarDt(Dados, rs1);
			}

		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}
	
	/**
	 * Método que verifica se guia já foi utilizada para outra certidão.
	 * @param String numeroGuiaCompleto
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isGuiaJaUtilizada(String numeroGuiaCompleto) throws Exception {
		
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT NUMERO_GUIA_COMPLETO FROM PROJUDI.CERT WHERE NUMERO_GUIA_COMPLETO = ?";
		
		ps.adicionarLong(numeroGuiaCompleto);
		
		ResultSetTJGO rs1 = null;
		
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					if( Funcoes.StringToInt(rs1.getString("NUMERO_GUIA_COMPLETO")) == Funcoes.StringToInt(numeroGuiaCompleto) ) {
						retorno = true;
					}
				}
			}
		} finally {
			 try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
}