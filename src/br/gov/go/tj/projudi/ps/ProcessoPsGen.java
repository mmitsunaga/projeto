package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoPsGen extends Persistencia {

	private static final long serialVersionUID = -6903221165723176758L;

	// ---------------------------------------------------------
	public ProcessoPsGen() {

	}

	// ---------------------------------------------------------
	public void inserir(ProcessoDt dados) throws Exception {

		String stSqlCampos = "";
		String stSqlValores = "";
		String stSql = "";
		String stVirgula = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		// //System.out.println("....psProcessoinserir()");
		stSqlCampos = "INSERT INTO PROJUDI.PROC (";

		stSqlValores += " Values (";

		if ((dados.getProcessoNumero().length() > 0)) {
			stSqlCampos += stVirgula + "PROC_NUMERO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getProcessoNumero());

			stVirgula = ",";
		}
		if ((dados.getId_ProcessoPrincipal().length() > 0)) {
			stSqlCampos += stVirgula + "ID_PROC_DEPENDENTE ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ProcessoPrincipal());

			stVirgula = ",";
		}
		if ((dados.getId_ProcessoTipo().length() > 0)) {
			stSqlCampos += stVirgula + "ID_PROC_TIPO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ProcessoTipo());

			stVirgula = ",";
		}
		if ((dados.getId_ProcessoFase().length() > 0)) {
			stSqlCampos += stVirgula + "ID_PROC_FASE ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ProcessoFase());

			stVirgula = ",";
		}
		if ((dados.getId_ProcessoStatus().length() > 0)) {
			stSqlCampos += stVirgula + "ID_PROC_STATUS ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ProcessoStatus());

			stVirgula = ",";
		}
		if ((dados.getId_ProcessoPrioridade().length() > 0)) {
			stSqlCampos += stVirgula + "ID_PROC_PRIOR ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ProcessoPrioridade());

			stVirgula = ",";
		}
		if ((dados.getId_Serventia().length() > 0)) {
			stSqlCampos += stVirgula + "ID_SERV ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_Serventia());

			stVirgula = ",";
		}
		if ((dados.getId_ServentiaOrigem().length() > 0)) {
			stSqlCampos += stVirgula + "ID_SERV_ORIGEM ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ServentiaOrigem());

			stVirgula = ",";
		}
		if ((dados.getId_Area().length() > 0)) {
			stSqlCampos += stVirgula + "ID_AREA ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_Area());

			stVirgula = ",";
		}
		if ((dados.getId_ObjetoPedido().length() > 0)) {
			stSqlCampos += stVirgula + "ID_OBJETO_PEDIDO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_ObjetoPedido());

			stVirgula = ",";
		}
		if ((dados.getId_Classificador().length() > 0)) {
			stSqlCampos += stVirgula + "ID_CLASSIFICADOR ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getId_Classificador());

			stVirgula = ",";
		}
		if ((dados.getSegredoJustica().length() > 0)) {
			stSqlCampos += stVirgula + "SEGREDO_JUSTICA ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarBoolean(dados.getSegredoJustica());

			stVirgula = ",";
		}
		if ((dados.getProcessoDiretorio().length() > 0)) {
			stSqlCampos += stVirgula + "PROC_DIRETORIO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getProcessoDiretorio());

			stVirgula = ",";
		}
		if ((dados.getTcoNumero().length() > 0)) {
			stSqlCampos += stVirgula + "TCO_NUMERO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getTcoNumero());

			stVirgula = ",";
		}
		if ((dados.getValor().length() > 0)) {
			stSqlCampos += stVirgula + "VALOR ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getValor());

			stVirgula = ",";
		}
		if ((dados.getDataRecebimento().length() > 0)) {
			stSqlCampos += stVirgula + "DATA_RECEBIMENTO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarDateTime(dados.getDataRecebimento());

			stVirgula = ",";
		}
		if ((dados.getDataArquivamento().length() > 0)) {
			stSqlCampos += stVirgula + "DATA_ARQUIVAMENTO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarDateTime(dados.getDataArquivamento());

			stVirgula = ",";
		}
		if ((dados.getDataPrescricao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_PRESCRICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataPrescricao());  

			stVirgula=",";
		}		
		if ((dados.getApenso().length() > 0)) {
			stSqlCampos += stVirgula + "APENSO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarBoolean(dados.getApenso());

			stVirgula = ",";
		}
		if ((dados.getAno().length() > 0)) {
			stSqlCampos += stVirgula + "ANO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getAno());

			stVirgula = ",";
		}
		if ((dados.getForumCodigo().length() > 0)) {
			stSqlCampos += stVirgula + "FORUM_CODIGO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarLong(dados.getForumCodigo());

			stVirgula = ",";
		}
		if ((dados.getEfeitoSuspensivo().length() > 0)) {
			stSqlCampos += stVirgula + "EFEITO_SUSPENSIVO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarBoolean(dados.getEfeitoSuspensivo());

			stVirgula = ",";
		}
		if ((dados.getPenhora().length() > 0)) {
			stSqlCampos += stVirgula + "PENHORA ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarBoolean(dados.getPenhora());

			stVirgula = ",";
		}
		if ((dados.getDataTransitoJulgado().length() > 0)) {
			stSqlCampos += stVirgula + "DATA_TRANSITO_JULGADO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarDateTime(dados.getDataTransitoJulgado());

			stVirgula = ",";
		}
		if ((dados.getJulgado2Grau().length() > 0)) {
			stSqlCampos += stVirgula + "JULGADO_2_GRAU ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarBoolean(dados.getJulgado2Grau());

			stVirgula = ",";
		}
		if ((dados.getValorCondenacao().length() > 0)) {
			stSqlCampos += stVirgula + "VALOR_CONDENACAO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getValorCondenacao());

			stVirgula = ",";
		}		
		if ((dados.getId_Custa_Tipo().length() > 0)) {
			stSqlCampos += stVirgula + "ID_CUSTA_TIPO ";
			stSqlValores += stVirgula + "? ";
			ps.adicionarString(dados.getId_Custa_Tipo());

			stVirgula = ",";
		}
		
		stSqlCampos += ")";
		stSqlValores += ")";
		stSql += stSqlCampos + stSqlValores;
		
		dados.setId(executarInsert(stSql, "ID_PROC", ps));		
	}

	// ---------------------------------------------------------
	public void alterar(ProcessoDt dados) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		// //System.out.println("....psProcessoalterar()");

		stSql = "UPDATE PROJUDI.PROC SET  ";
		stSql += "PROC_NUMERO = ?";
		ps.adicionarLong(dados.getProcessoNumero());

		stSql += ",ID_PROC_DEPENDENTE = ?";
		ps.adicionarLong(dados.getId_ProcessoPrincipal());

		stSql += ",ID_PROC_TIPO = ?";
		ps.adicionarLong(dados.getId_ProcessoTipo());

		stSql += ",ID_PROC_FASE = ?";
		ps.adicionarLong(dados.getId_ProcessoFase());

		stSql += ",ID_PROC_STATUS = ?";
		ps.adicionarLong(dados.getId_ProcessoStatus());

		stSql += ",ID_PROC_PRIOR = ?";
		ps.adicionarLong(dados.getId_ProcessoPrioridade());

		stSql += ",ID_SERV = ?";
		ps.adicionarLong(dados.getId_Serventia());

		stSql += ",ID_SERV_ORIGEM = ?";
		ps.adicionarLong(dados.getId_ServentiaOrigem());

		stSql += ",ID_AREA = ?";
		ps.adicionarLong(dados.getId_Area());

		stSql += ",ID_OBJETO_PEDIDO = ?";
		ps.adicionarLong(dados.getId_ObjetoPedido());

		stSql += ",ID_CLASSIFICADOR = ?";
		ps.adicionarLong(dados.getId_Classificador());

		stSql += ",SEGREDO_JUSTICA = ?";
		ps.adicionarBoolean(dados.getSegredoJustica());

		stSql += ",PROC_DIRETORIO = ?";
		ps.adicionarString(dados.getProcessoDiretorio());

		stSql += ",TCO_NUMERO = ?";
		ps.adicionarString(dados.getTcoNumero());

		stSql += ",VALOR = ?";
		ps.adicionarString(dados.getValor());

		stSql += ",DATA_RECEBIMENTO = ?";
		ps.adicionarDateTime(dados.getDataRecebimento());

		stSql += ",DATA_ARQUIVAMENTO = ?";
		ps.adicionarDateTime(dados.getDataArquivamento());
		
		stSql+= ",DATA_PRESCRICAO = ?";		 
		ps.adicionarDate(dados.getDataPrescricao()); 

		stSql += ",APENSO = ?";
		ps.adicionarBoolean(dados.getApenso());

		stSql += ",ANO = ?";
		ps.adicionarLong(dados.getAno());

		stSql += ",FORUM_CODIGO = ?";
		ps.adicionarLong(dados.getForumCodigo());

		stSql += ",EFEITO_SUSPENSIVO = ?";
		ps.adicionarBoolean(dados.getEfeitoSuspensivo());

		stSql += ",PENHORA = ?";
		ps.adicionarBoolean(dados.getPenhora());

		stSql += ",DATA_TRANSITO_JULGADO = ?";
		ps.adicionarDate(dados.getDataTransitoJulgado());

		stSql += ",JULGADO_2_GRAU = ?";
		ps.adicionarDate(dados.getJulgado2Grau());

		stSql += ",VALOR_CONDENACAO = ?";
		ps.adicionarDate(dados.getValorCondenacao());
		
		stSql += ",ID_CUSTA_TIPO = ?";
		ps.adicionarLong(dados.getId_Custa_Tipo());

		stSql += " WHERE ID_PROC  = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(stSql,ps);

	}

	// ---------------------------------------------------------
	public void excluir(String chave) throws Exception {

		String stSql = "";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		// //System.out.println("....psProcessoexcluir()");

		stSql = "DELETE FROM PROJUDI.PROC";
		stSql += " WHERE ID_PROC = ?";
		ps.adicionarLong(chave);

		executarUpdateDelete(stSql,ps);

	}

//	// ---------------------------------------------------------
//	public ProcessoDt consultarId(String id_processo) throws Exception {
//
//		String stSql = "";
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		ProcessoDt Dados = null;
//		// //System.out.println("....ps-ConsultaId_Processo)");
//
//		stSql = "SELECT * FROM PROJUDI.VIEW_PROC WHERE ID_PROC = ?";
//		ps.adicionarLong(id_processo);
//
//		// //System.out.println("....Sql " + stSql );
//
//		try{
//			// //System.out.println("..ps-ConsultaId_Processo " + stSql);
//			rs1 = consultar(stSql, ps);
//			if (rs1.next()) {
//				Dados = new ProcessoDt();
//				associarDt(Dados, rs1);
//			}
//			// //System.out.println("..ps-ConsultaId");
//		} finally{
//			try{
//				if (rs1 != null)
//					rs1.close();
//			} catch(Exception e) {
//			}
//		}
//		return Dados;
//	}

	protected void associarDt(ProcessoDt Dados, ResultSetTJGO rs) throws Exception{
		
		Dados.setId(rs.getString("ID_PROC"));
		Dados.setProcessoNumero(rs.getString("PROC_NUMERO"));
		Dados.setId_ProcessoPrincipal(rs.getString("ID_PROC_DEPENDENTE"));
		Dados.setProcessoNumeroPrincipal(rs.getString("PROC_NUMERO_DEPENDENTE"));
		Dados.setId_ProcessoTipo(rs.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo(rs.getString("PROC_TIPO"));
		Dados.setId_ProcessoFase(rs.getString("ID_PROC_FASE"));
		Dados.setProcessoFase(rs.getString("PROC_FASE"));
		Dados.setId_ProcessoStatus(rs.getString("ID_PROC_STATUS"));
		Dados.setProcessoStatus(rs.getString("PROC_STATUS"));
		Dados.setProcessoStatusCodigo(rs.getString("PROC_STATUS_CODIGO"));
		Dados.setId_ProcessoPrioridade(rs.getString("ID_PROC_PRIOR"));
		Dados.setProcessoPrioridade(rs.getString("PROC_PRIOR"));
		Dados.setId_Serventia(rs.getString("ID_SERV"));
		Dados.setServentia(rs.getString("SERV"));
		Dados.setId_ServentiaOrigem(rs.getString("ID_SERV_ORIGEM"));
		Dados.setServentiaOrigem(rs.getString("SERV_ORIGEM"));
		Dados.setId_Area(rs.getString("ID_AREA"));
		Dados.setArea(rs.getString("AREA"));
		Dados.setId_ObjetoPedido(rs.getString("ID_OBJETO_PEDIDO"));
		Dados.setObjetoPedido(rs.getString("OBJETO_PEDIDO"));
		Dados.setId_Classificador(rs.getString("ID_CLASSIFICADOR"));
		Dados.setClassificador(rs.getString("CLASSIFICADOR"));
		Dados.setSegredoJustica(Funcoes.FormatarLogico(rs.getString("SEGREDO_JUSTICA")));
		Dados.setProcessoDiretorio(rs.getString("PROC_DIRETORIO"));
		Dados.setTcoNumero(rs.getString("TCO_NUMERO"));
		Dados.setValor(Funcoes.FormatarDecimal(rs.getString("VALOR")));
		Dados.setDataRecebimento(Funcoes.FormatarDataHora(rs.getDateTime("DATA_RECEBIMENTO")));
		Dados.setDataArquivamento(Funcoes.FormatarDataHora(rs.getDateTime("DATA_ARQUIVAMENTO")));
		Dados.setDataPrescricao(Funcoes.FormatarData(rs.getDateTime("DATA_PRESCRICAO")));
		Dados.setApenso(Funcoes.FormatarLogico(rs.getString("APENSO")));
		Dados.setAno(rs.getString("ANO"));
		Dados.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
		Dados.setForumCodigo(rs.getString("FORUM_CODIGO"));
		Dados.setCodigoTemp(rs.getString("CODIGO_TEMP"));
		Dados.setId_AreaDistribuicao(rs.getString("ID_AREA_DIST"));
		Dados.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
		Dados.setProcessoFaseCodigo(rs.getString("PROC_FASE_CODIGO"));
		Dados.setProcessoStatusCodigo(rs.getString("PROC_STATUS_CODIGO"));
		Dados.setProcessoPrioridadeCodigo(rs.getString("PROC_PRIOR_CODIGO"));
		Dados.setServentiaCodigo(rs.getString("SERV_CODIGO"));
		Dados.setServentiaOrigemCodigo(rs.getString("SERV_ORIGEM_CODIGO"));
		Dados.setAreaCodigo(rs.getString("AREA_CODIGO"));
		Dados.setObjetoPedidoCodigo(rs.getString("OBJETO_PEDIDO_CODIGO"));
		Dados.setEfeitoSuspensivo(Funcoes.FormatarLogico(rs.getString("EFEITO_SUSPENSIVO")));
		Dados.setPenhora(Funcoes.FormatarLogico(rs.getString("PENHORA")));
		Dados.setDataTransitoJulgado(Funcoes.FormatarData(rs.getDateTime("DATA_TRANSITO_JULGADO")));
		Dados.setJulgado2Grau(Funcoes.FormatarLogico(rs.getString("JULGADO_2_GRAU")));
		Dados.setValorCondenacao(Funcoes.FormatarDecimal(rs.getString("VALOR_CONDENACAO")));
		Dados.setId_Custa_Tipo(rs.getString("ID_CUSTA_TIPO"));
		Dados.setLocalizador(rs.getString("LOCALIZADOR"));
		if (rs.contains("ID_CNJ_CLASSE")) {
			Dados.setIdCNJClasse(rs.getString("ID_CNJ_CLASSE"));
		}
	}

	// ---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String stSql = "";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		// //System.out.println("..ps-ConsultaDescricaoProcesso()");

		stSql = "SELECT ID_PROC, PROC_NUMERO FROM PROJUDI.VIEW_PROC WHERE PROC_NUMERO LIKE ?";
		stSql += " ORDER BY PROC_NUMERO ";
		ps.adicionarString( descricao +"%");

		try{
			// //System.out.println("..ps-ConsultaDescricaoProcesso " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			// //System.out.println("....Execução Query OK" );

			while (rs1.next()) {
				ProcessoDt obTemp = new ProcessoDt();
				obTemp.setId(rs1.getString("ID_PROC"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql = "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC WHERE PROC_NUMERO LIKE ?";
			rs2 = consultar(stSql, ps);
			// //System.out.println("....2 - Consulta quantidade OK" );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			// //System.out.println("..ProcessoPsGen.consultarDescricao() Operação
			// realizada com sucesso");
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null)
					rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

}
