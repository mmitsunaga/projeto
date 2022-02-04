package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoGuiaEmissaoConsultaDt;
import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaProcessoTJGO;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoGuiaEmissaoConsultaPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4993693596482699162L;
	
	public ProcessoGuiaEmissaoConsultaPs(Connection conexao){
		Conexao = conexao;
	}

	public ProcessoGuiaEmissaoConsultaDt obtenhaProcesso(NumeroProcessoDt numeroProcessoDt) throws Exception {
		ResultSetTJGO rs = null;		
		ProcessoGuiaEmissaoConsultaDt processo = null;
		
		try{
			rs = consultar(obtenhaConsultaSQLProcesso(), obtenhaPreparedStatementTJGOProcesso(numeroProcessoDt));			
			while (rs.next()) {				
				if (processo == null)
				{
					processo = new ProcessoGuiaEmissaoConsultaDt();
					processo.setSistemaProcessoTJGO(EnumSistemaProcessoTJGO.projudi);
					processo.setNumeroProcessoCompletoDt(numeroProcessoDt);
					processo.setId(rs.getString("ID_PROC"));
					processo.setId_ProcessoTipo(rs.getString("ID_PROC_TIPO"));
					processo.setProcessoTipo(rs.getString("PROC_TIPO"));
					processo.setValor(Funcoes.FormatarDecimal(rs.getString("VALOR")));
					processo.setId_Serventia(rs.getString("ID_SERV"));
					processo.setServentia(rs.getString("SERV"));
					processo.setServentiaCodigo(rs.getString("SERV_CODIGO"));

//					processo.setNumeroTCOInquerito(rs.getString("TCO_NUMERO"));
//					processo.setClasse(rs.getString("PROC_TIPO"));
//					processo.setFase(rs.getString("PROC_FASE"));
//					processo.setClassificador(rs.getString("CLASSIFICADOR"));
//					if (rs.getDate("DATA_RECEBIMENTO") != null)
//						processo.setDataDistribuicao(new TJDataHora(rs.getDate("DATA_RECEBIMENTO")));
//					if (rs.getString("SEGREDO_JUSTICA") != null)
//						processo.setEhSegredoDeJustica(Funcoes.ObtenhaValorLogico(rs.getString("SEGREDO_JUSTICA")));
//					processo.setStatus(rs.getString("PROC_STATUS"));
				}
				
				//Adiciona parte a lista correspondente
//				if (rs.getString("PROC_PARTE_TIPO_CODIGO") != null) {
//					PoloDt poloDt = new PoloDt();
//					if (processo.isEhSegredoDeJustica())
//					{
//						poloDt.setNome(Funcoes.iniciaisNome(rs.getString("NOME")));
//						poloDt.setFiliacao(Funcoes.iniciaisNome(rs.getString("NOME_MAE")));						
//					}
//					else
//					{
//						poloDt.setNome(rs.getString("NOME"));
//						poloDt.setFiliacao(rs.getString("NOME_MAE"));
//						poloDt.setCpf(Funcoes.completaCPFZeros(rs.getString("CPF")));
//						if (poloDt.getCpf() == null || poloDt.getCpf().trim().length() == 0)
//							poloDt.setCpf(Funcoes.completaCNPJZeros(rs.getString("CNPJ")));
//						if (rs.getDate("DATA_NASCIMENTO") != null)
//							poloDt.setDataDeNascimento(new TJDataHora(rs.getDate("DATA_NASCIMENTO")));	
//					}					
//					
//					switch (Funcoes.StringToInt(rs.getString("PROC_PARTE_TIPO_CODIGO"))) {
//						case (AudienciaTJGOConstantes.PROMOVENTE_CODIGO_PROJUDI):
//							processo.adicionePoloAtivo(poloDt);
//							break;
//						case (AudienciaTJGOConstantes.PROMOVIDO_CODIGO_PROJUDI):
//							processo.adicionePoloPassivos(poloDt);
//							break;
//						default:
//							processo.adicioneOutrosPolos(poloDt);
//							break;
//					}
//				}
			}
			
//			if (processo != null)
//			{
//				libereResultset(rs);				
//				rs = consultar(obtenhaConsultaSQLAssunto(), obtenhaPreparedStatementTJGOAssunto(processo));
//				while (rs.next()) {
//					if (rs.getString("DISPOSITIVO_LEGAL") != null)
//						processo.adicioneAssunto(rs.getString("ASSUNTO") + " - " + rs.getString("DISPOSITIVO_LEGAL"));
//					else 
//						processo.adicioneAssunto(rs.getString("ASSUNTO"));
//				}
//				processo.setNomeMagistradoResponsavel(obtenhaNomeDoMagistradoResponsavelPeloProcesso(processo));
//			}
		}  finally{
			libereResultset(rs);
		}
		
		return processo;
	}
	
	private String obtenhaConsultaSQLProcesso()
	{
		StringBuffer consultaSQL = new StringBuffer();	
		
		consultaSQL.append(" SELECT * ");
		consultaSQL.append(" FROM PROJUDI.VIEW_PROC_COMPLETO ");
		consultaSQL.append(" WHERE PROC_NUMERO = ? ");
		consultaSQL.append(" AND DIGITO_VERIFICADOR = ? ");
		consultaSQL.append(" AND ANO = ? ");
		consultaSQL.append(" AND FORUM_CODIGO = ? ");		
		
		return consultaSQL.toString();
	}
	
	private PreparedStatementTJGO obtenhaPreparedStatementTJGOProcesso(NumeroProcessoDt numeroProcessoDt) throws Exception
	{
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ps.adicionarLong(numeroProcessoDt.getNumero());
		ps.adicionarLong(numeroProcessoDt.getDigito());
		ps.adicionarLong(numeroProcessoDt.getAno());
		ps.adicionarLong(numeroProcessoDt.getForum());
		return ps;
	}
}
