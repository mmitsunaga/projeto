package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoPrisaoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoOrigemDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.projudi.dt.PrisaoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MandadoPrisaoPs extends MandadoPrisaoPsGen{

	private static final long serialVersionUID = -7213116004555271012L;
	@SuppressWarnings("unused")
	private MandadoPrisaoPs( ) {}
	public MandadoPrisaoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Lista os Mandados de Prisão referente ao processo
	 * @param idPorcesso: identificação do processo
	 * @return: Lista com mandados de prisão
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarMandadoPrisaoProcesso(String idProcesso, boolean listarTodos) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = null;

		stSql = "SELECT * FROM PROJUDI.VIEW_MANDADO_PRISAO " +
				" WHERE ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		if (!listarTodos) {
			stSql += " AND SIGILO = ?";
			ps.adicionarBoolean(false);
		}
		stSql += " ORDER BY MANDADO_PRISAO_NUMERO";
		
		try{
			rs1 = consultar(stSql,ps);
			lista = new ArrayList();
			while (rs1.next()) {
				MandadoPrisaoDt dados= new MandadoPrisaoDt();
				associarDt(dados, rs1);
				lista.add(dados);
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}

	/**
	 * Verifica se existe Mandado de Prisão Não Cumprido no processo
	 * @param idPorcesso: identificação do processo
	 * @return: boolean
	 * @throws Exception
	 * @author wcsilva
	 */
	public boolean existeMandadoPrisaoProcesso_NaoCumprido(String idProcesso) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		boolean existe = false;
		
		stSql = "SELECT * FROM PROJUDI.VIEW_MANDADO_PRISAO " +
				" WHERE ID_PROC = ? " +
				" AND DATA_CUMPRIMENTO IS NULL ";
		ps.adicionarLong(idProcesso);
		
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				existe = true;
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return existe; 
	}
	
	/**
	 * Lista os Mandados de Prisão referente ao processo
	 * @param idPorcesso: identificação do processo
	 * @return: Lista com mandados de prisão
	 * @throws Exception
	 * @author wcsilva
	 */
	public String listarMandadoPrisaoServentiaJSON(String numeroProcesso, String dataInicial, String dataFinal, String mandadoPrisaoStatusCodigo, String mandadoPrisaoTipoCodigo, boolean listarTodos, String idServentia, String posicao) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stRetorno = null;
		int qtdeColunas = 10;

		stSql = "SELECT mp.ID_MANDADO_PRISAO as ID,  p.PROC_NUMERO, p.DIGITO_VERIFICADOR," +
				" mp.MANDADO_PRISAO_NUMERO, pp.NOME, mp.SIGILO, mp.DATA_VALIDADE, mps.MANDADO_PRISAO_STATUS," +
				" mp.DATA_EXPEDICAO, mp.DATA_CUMPRIMENTO, pt.PRISAO_TIPO ";
		
		String stSqlFrom = " FROM PROJUDI.MANDADO_PRISAO mp" +
	       					" left join PROJUDI.mandado_prisao_status mps on mps.id_mandado_prisao_status = mp.id_mandado_prisao_status" +
	       					" inner join PROJUDI.prisao_tipo pt on mp.id_prisao_tipo = pt.id_prisao_tipo" +
	       					" inner join PROJUDI.proc_parte pp on mp.id_proc_parte = pp.id_proc_parte" +
	       					" inner join PROJUDI.proc p on pp.id_proc = p.id_proc" +
	       					" WHERE p.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		
		if (!listarTodos){
			stSqlFrom += " AND SIGILO = ?";
			ps.adicionarBoolean(false);
		}
		
		if (numeroProcesso.length() > 0){
			stSqlFrom += " AND p.proc_numero = ?";
			String[] procNumero = numeroProcesso.split("\\.");
			ps.adicionarLong(procNumero[0]);
			
			if (procNumero.length > 1){
				stSqlFrom += " AND p.digito_verificador = ?";
				ps.adicionarLong(procNumero[1]);	
			}			
		}
		
		if (mandadoPrisaoTipoCodigo != null && mandadoPrisaoTipoCodigo.length() > 0){
			stSqlFrom += " AND pt.PRISAO_TIPO_CODIGO = ?";
			ps.adicionarLong(mandadoPrisaoTipoCodigo);
		}
		
		if (mandadoPrisaoStatusCodigo.length() > 0){
			stSqlFrom += " AND mps.mandado_prisao_status_codigo = ?";
			ps.adicionarLong(mandadoPrisaoStatusCodigo);
			
			String descData = "";
			switch (Funcoes.StringToInt(mandadoPrisaoStatusCodigo)) {
				case MandadoPrisaoStatusDt.CUMPRIDO:
				case MandadoPrisaoStatusDt.REVOGADO:
					descData = "mp.data_cumprimento";
					break;
				case MandadoPrisaoStatusDt.EMITIDO:
					descData = "mp.data_emissao";
					break;
				case MandadoPrisaoStatusDt.IMPRESSO:
					descData = "mp.data_impressao";
					break;
				case MandadoPrisaoStatusDt.EXPEDIDO:
					descData = "mp.data_expedicao";
					break;
			}
			
			if (dataInicial.length() > 0){
				stSqlFrom += " AND " + descData + " >= ?";
				ps.adicionarDate(dataInicial);
			}
			if (dataFinal.length() > 0){
				stSqlFrom += " AND " + descData + " <= ?";
				ps.adicionarDate(dataFinal);		
			}
		}
		stSql += stSqlFrom;
		stSql += " order by p.proc_numero";
		
		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE " + stSqlFrom;
			rs2 = consultar(stSql,ps);
			rs2.next();
			
//			stRetorno = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
			//************************* GERAR JSON ************************************//
			StringBuilder stTemp = new StringBuilder();
			stTemp.append("[");
			stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append(rs2.getLong("QUANTIDADE"));
			for (int i = 2; i <= qtdeColunas; i++) {
				stTemp.append("\",\"desc" + i + "\":\"");
			}
			stTemp.append("\"}");
			stTemp.append(",{\"id\":\"-60000\",\"desc1\":\"").append(posicao);
			for (int i = 2; i <= qtdeColunas; i++) {
				stTemp.append("\",\"desc" + i + "\":\"");
			}
			stTemp.append("\"}");
			while (rs1.next()){		
				stTemp.append(",{\"id\":\"").append(rs1.getString("ID"));
				stTemp.append("\",\"desc1\":\"").append(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				if (rs1.getString("MANDADO_PRISAO_NUMERO") == null)
					stTemp.append("\",\"desc2\":\"").append("-");
				else
					stTemp.append("\",\"desc2\":\"").append(rs1.getString("MANDADO_PRISAO_NUMERO"));
				stTemp.append("\",\"desc3\":\"").append(rs1.getString("NOME"));
				if (Funcoes.FormatarLogico(rs1.getString("SIGILO")).equals("true"))
					stTemp.append("\",\"desc4\":\"").append("Sim");
				else stTemp.append("\",\"desc4\":\"").append("Não");
				stTemp.append("\",\"desc5\":\"").append(rs1.getString("PRISAO_TIPO"));
				stTemp.append("\",\"desc6\":\"").append(Funcoes.FormatarData(rs1.getDateTime("DATA_VALIDADE")));
				if (rs1.getString("MANDADO_PRISAO_STATUS") == null)
					stTemp.append("\",\"desc7\":\"").append("-");
				else stTemp.append("\",\"desc7\":\"").append(rs1.getString("MANDADO_PRISAO_STATUS"));
				stTemp.append("\",\"desc8\":\"").append(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPEDICAO")));
				stTemp.append("\",\"desc9\":\"").append(Funcoes.FormatarData(rs1.getDateTime("DATA_CUMPRIMENTO")));
				stTemp.append("\"}");
			}
			stTemp.append("]");
			stRetorno = stTemp.toString();
			//*************************************************************************//
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stRetorno; 
	}
	
	
	/**
	 * Lista todos as Origens do Mandado de Prisão
	 * @return: Lista com MandadoPrisaoStatusDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarMandadoPrisaoOrigem() throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_MANDADO_PRISAO_ORIGEM";		

		try{
			rs1 = consultar(stSql,ps);
			lista = new ArrayList();
			while (rs1.next()) {
				MandadoPrisaoOrigemDt dados= new MandadoPrisaoOrigemDt();
//				associarMandadoPrisaoOrigemDt(dados, rs1);
				dados.setId(rs1.getString("ID_MANDADO_PRISAO_ORIGEM"));
				dados.setMandadoPrisaoOrigem(rs1.getString("MANDADO_PRISAO_ORIGEM"));
				dados.setMandadoPrisaoOrigemCodigo(rs1.getString("MANDADO_PRISAO_ORIGEM_CODIGO"));
				dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				lista.add(dados);
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
	
	
	/**
	 * Lista todos os Tipos de Prisão
	 * @return: Lista com PrisaoTipoDt
	 * @throws Exception
	 * @author wcsilva
	 */
	public List listarPrisaoTipo() throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PRISAO_TIPO";		

		try{
			rs1 = consultar(stSql,ps);
			lista = new ArrayList();
			while (rs1.next()) {
				PrisaoTipoDt dados= new PrisaoTipoDt();
//				associarPrisaoTipoDt(dados, rs1);
				dados.setId(rs1.getString("ID_PRISAO_TIPO"));
				dados.setPrisaoTipo(rs1.getString("PRISAO_TIPO"));
				dados.setPrisaoTipoCodigo(rs1.getString("PRISAO_TIPO_CODIGO"));
				dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
				lista.add(dados);
			}
			
		} finally { 
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
	
	
//	/**
//	 * Lista todos os Regimes de Pena
//	 * @return: Lista com RegimeExecucaoDt
//	 * @throws Exception
//	 * @author wcsilva
//	 */
//	public List listarRegime() throws Exception{
//		String stSql="";
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		List lista = null;
//
//		stSql= "SELECT * FROM PROJUDI.VIEW_REGIME_EXE";		
//
//		try{
//			rs1 = consultar(stSql,ps);
//			lista = new ArrayList();
//			while (rs1.next()) {
//				RegimeExecucaoDt dados= new RegimeExecucaoDt();
//				dados.setId(rs1.getString("ID_REGIME_EXE"));
//				dados.setRegimeExecucao(rs1.getString("REGIME_EXE"));
//				dados.setId_PenaExecucaoTipo(rs1.getString("ID_PENA_EXE_TIPO"));
//				dados.setId_ProximoRegimeExecucao(rs1.getString("ID_PROXIMO_REGIME_EXE"));
//				dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
//				dados.setPenaExecucaoTipo(rs1.getString("PENA_EXE_TIPO"));
//				lista.add(dados);
//			}
//			
//		} finally {
//			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
//		}
//		return lista; 
//	}
	
	
	/**
	 * Consulta a quantidade de mandados de prisão para o processo
	 * @param idProcesso: identificador do processo
	 * @return quantidade de mandados de prisao do processo
	 * @throws Exception
	 */
	public int consultarMaiorNumeroMandadoProcesso(String idProcesso) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int quantidade = 0;

		stSql= "SELECT max(MANDADO_PRISAO_NUMERO) AS ULTIMO_NUMERO FROM PROJUDI.VIEW_MANDADO_PRISAO WHERE ID_PROC = ?";		
		ps.adicionarLong(idProcesso);
		
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				quantidade = rs1.getInt("ULTIMO_NUMERO");
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return quantidade; 
	}
	
	public void inserir(MandadoPrisaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.MANDADO_PRISAO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMandadoPrisaoNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_NUMERO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMandadoPrisaoNumero());  
			stVirgula=",";
		}
//		if ((dados.getDataDelito().length()>0)) {
//			 stSqlCampos+=   stVirgula + "DATA_DELITO " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarDate(dados.getDataDelito());  
//				stVirgula=",";
//		}
		if ((dados.getDataExpedicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EXPEDICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataExpedicao());  
			stVirgula=",";
		}
//		if ((dados.getMandadoPrisaoNumeroAnterior().length()>0)) {
//			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_NUMERO_ANTERIOR " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarLong(dados.getMandadoPrisaoNumeroAnterior());  
//			stVirgula=",";
//		}
		if ((dados.getPenaImposta().length()>0)) {
			 stSqlCampos+=   stVirgula + "PENA_IMPOSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPenaImposta());  
			stVirgula=",";
		}
		if ((dados.getPrazoPrisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRAZO_PRISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPrazoPrisao());  
			stVirgula=",";
		}
//		if ((dados.getRecaptura().length()>0)) {
//			 stSqlCampos+=   stVirgula + "RECAPTURA " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarBoolean(dados.getRecaptura());  
//			stVirgula=",";
//		}
		if ((dados.getSigilo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SIGILO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getSigilo());  
			stVirgula=",";
		}
		if ((dados.getSinteseDecisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "SINTESE_DECISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSinteseDecisao());  
			stVirgula=",";
		}
		if ((dados.getValorFianca().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_FIANCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDecimal(dados.getValorFianca());  
			stVirgula=",";
		}
		if ((dados.getId_RegimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_REGIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_RegimeExecucao());  
			stVirgula=",";
		}
		if ((dados.getPrisaoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PRISAO_TIPO " ;
			 stSqlValores+=   stVirgula + "(SELECT ID_PRISAO_TIPO FROM PRISAO_TIPO WHERE PRISAO_TIPO_CODIGO = ?) " ;
			 ps.adicionarLong(dados.getPrisaoTipoCodigo());  
			stVirgula=",";
		}
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  
			stVirgula=",";
		}
		if ((dados.getId_Assunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Assunto());  
			stVirgula=",";
		}
		if ((dados.getDataValidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VALIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataValidade());  
			stVirgula=",";
		}
		if ((dados.getOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getOrigem());  
			stVirgula=",";
		}
		if ((dados.getMandadoPrisaoOrigemCodigo().length()>0)) {
			stSqlCampos+=   stVirgula + "ID_MANDADO_PRISAO_ORIGEM " ;
			stSqlValores+=   stVirgula + "(SELECT ID_MANDADO_PRISAO_ORIGEM FROM MANDADO_PRISAO_ORIGEM WHERE MANDADO_PRISAO_ORIGEM_CODIGO = ?) " ;
			ps.adicionarLong(dados.getMandadoPrisaoOrigemCodigo());    
			stVirgula=",";
		}
//		if ((dados.getAssuntoDelitoPrincipal().length()>0)) {
//			 stSqlCampos+=   stVirgula + "ASSUNTO_DELITO_PRINCIPAL " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarString(dados.getAssuntoDelitoPrincipal());  
//			stVirgula=",";
//		}
		if ((dados.getLocalRecolhimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "LOCAL_RECOLHIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLocalRecolhimento());  
			stVirgula=",";
		}
		if ((dados.getNumeroOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNumeroOrigem());  
			stVirgula=",";
		}
		if ((dados.getDataCumprimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_CUMPRIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataCumprimento());  
			stVirgula=",";
		}
		if ((dados.getDataEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EMISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataEmissao());  
			stVirgula=",";
		}
		if ((dados.getDataImpressao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_IMPRESSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataImpressao());  
			stVirgula=",";
		}
		if ((dados.getIdUsuarioServentiaImpressao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_IMPRESSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdUsuarioServentiaImpressao());  
			stVirgula=",";
		}
		if ((dados.getIdUsuarioServentiaEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_EMISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdUsuarioServentiaEmissao());  
			stVirgula=",";
		}
		if ((dados.getIdUsuarioServentiaExpedicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_EXPEDICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getIdUsuarioServentiaExpedicao());  
			stVirgula=",";
		}
		if (dados.getMandadoPrisaoStatusCodigo().length() > 0){
			stSqlCampos+=   stVirgula + "ID_MANDADO_PRISAO_STATUS " ;
			stSqlValores+=   stVirgula + "(SELECT ID_MANDADO_PRISAO_STATUS FROM MANDADO_PRISAO_STATUS WHERE MANDADO_PRISAO_STATUS_CODIGO = ?) " ;
			ps.adicionarLong(dados.getMandadoPrisaoStatusCodigo());    
			stVirgula=",";
		}
		
		if (dados.getDataAtualizacao().length() > 0){
			stSqlCampos+=   stVirgula + "DATA_ATUALIZACAO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDate(new Date());
		}

		stVirgula=",";
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_MANDADO_PRISAO",ps));
	} 

	public void alterar(MandadoPrisaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.MANDADO_PRISAO SET  ";
		
		stSql+= "MANDADO_PRISAO_NUMERO = ?";		 ps.adicionarLong(dados.getMandadoPrisaoNumero());  

//		stSql+= ",DATA_DELITO = ?";		 ps.adicionarDate(dados.getDataDelito());  

		stSql+= ",DATA_EXPEDICAO = ?";		 ps.adicionarDate(dados.getDataExpedicao());  

//		stSql+= ",MANDADO_PRISAO_NUMERO_ANTERIOR = ?";		 ps.adicionarLong(dados.getMandadoPrisaoNumeroAnterior());  

		stSql+= ",PENA_IMPOSTA = ?";		 ps.adicionarLong(dados.getPenaImposta());  

		stSql+= ",PRAZO_PRISAO = ?";		 ps.adicionarString(dados.getPrazoPrisao());  

//		stSql+= ",RECAPTURA = ?";		 ps.adicionarBoolean(dados.getRecaptura());  
		
		stSql+= ",SIGILO = ?";		 ps.adicionarBoolean(dados.getSigilo());

		stSql+= ",SINTESE_DECISAO = ?";		 ps.adicionarString(dados.getSinteseDecisao());  

		stSql+= ",ID_MANDADO_PRISAO_STATUS = (SELECT ID_MANDADO_PRISAO_STATUS FROM MANDADO_PRISAO_STATUS WHERE MANDADO_PRISAO_STATUS_CODIGO = ?)";
		ps.adicionarLong(dados.getMandadoPrisaoStatusCodigo());  

		stSql+= ",VALOR_FIANCA = ?";		 ps.adicionarDecimal(dados.getValorFianca());  

		stSql+= ",ID_REGIME_EXE = ?";		 ps.adicionarLong(dados.getId_RegimeExecucao());  

		stSql+= ",ID_PRISAO_TIPO = (SELECT ID_PRISAO_TIPO FROM PRISAO_TIPO WHERE PRISAO_TIPO_CODIGO = ?)";		 
		ps.adicionarLong(dados.getPrisaoTipoCodigo());  

		stSql+= ",ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_ASSUNTO = ?";		 ps.adicionarLong(dados.getId_Assunto());  

		stSql+= ",DATA_VALIDADE = ?";		 ps.adicionarDate(dados.getDataValidade());  

		stSql+= ",ORIGEM = ?";		 ps.adicionarString(dados.getOrigem());
		
//		stSql+= ",ASSUNTO_DELITO_PRINCIPAL = ?";		 ps.adicionarString(dados.getAssuntoDelitoPrincipal());
		
		stSql+= ",LOCAL_RECOLHIMENTO = ?";		 ps.adicionarString(dados.getLocalRecolhimento());
		
		stSql+= ",NUMERO_ORIGEM = ?";		 ps.adicionarString(dados.getNumeroOrigem());

		stSql+= ",ID_MANDADO_PRISAO_ORIGEM = (SELECT ID_MANDADO_PRISAO_ORIGEM FROM MANDADO_PRISAO_ORIGEM WHERE MANDADO_PRISAO_ORIGEM_CODIGO = ?)";	
		ps.adicionarLong(dados.getMandadoPrisaoOrigemCodigo());  
		
//		if (dados.getDataAtualizacao().length() > 0) {
			stSql+= ",DATA_ATUALIZACAO = ?";		 
			ps.adicionarDate(new Date());
//		}
		
		stSql+= ",DATA_CUMPRIMENTO = ?";		 ps.adicionarDate(dados.getDataCumprimento());
		
		stSql+= ",DATA_EMISSAO = ?";		 ps.adicionarDate(dados.getDataEmissao());
		
		stSql+= ",ID_USU_SERV_EMISSAO = ?";		 ps.adicionarLong(dados.getIdUsuarioServentiaEmissao());
		
		stSql+= ",ID_USU_SERV_EXPEDICAO= ?";		 ps.adicionarLong(dados.getIdUsuarioServentiaExpedicao());
		
		stSql+= ",DATA_IMPRESSAO = ?";		 ps.adicionarDate(dados.getDataImpressao());
		
		stSql+= ",ID_USU_SERV_IMPRESSAO = ?";		 ps.adicionarLong(dados.getIdUsuarioServentiaImpressao());

		stSql += " WHERE ID_MANDADO_PRISAO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	} 
	
	protected void associarDt( MandadoPrisaoDt dados, ResultSetTJGO rs )  throws Exception {

		dados.setId(rs.getString("ID_MANDADO_PRISAO"));
		if (rs.getString("MANDADO_PRISAO_NUMERO") != null)
			dados.setMandadoPrisaoNumero(Funcoes.completarZeros(rs.getString("MANDADO_PRISAO_NUMERO"), 4));
//			dados.setDataDelito( Funcoes.FormatarData(rs.getDate("DATA_DELITO")));
		dados.setDataExpedicao( Funcoes.FormatarData(rs.getDateTime("DATA_EXPEDICAO")));
//			dados.setMandadoPrisaoNumeroAnterior( rs.getString("MANDADO_PRISAO_NUMERO_ANTERIOR"));
		String tempoPena = rs.getString("PENA_IMPOSTA");
		if (tempoPena != null && Funcoes.StringToInt(tempoPena) != 0){
			dados.setTempoPenaTotalDias(tempoPena);
			String tempo = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(dados.getPenaImposta()));
			String[] tempo1 = tempo.split(" - ");
			dados.setTempoPenaAno(tempo1[0]);
			dados.setTempoPenaMes(tempo1[1]);
			dados.setTempoPenaDia(tempo1[2]);
		}
		dados.setPrazoPrisao( rs.getString("PRAZO_PRISAO"));
//			dados.setRecaptura( rs.getString("RECAPTURA"));
		dados.setSigilo( rs.getString("SIGILO"));
		dados.setSinteseDecisao( rs.getString("SINTESE_DECISAO"));
		dados.setId_MandadoPrisaoStatus( rs.getString("ID_MANDADO_PRISAO_STATUS"));
		dados.setMandadoPrisaoStatus( rs.getString("MANDADO_PRISAO_STATUS"));
		dados.setMandadoPrisaoStatusCodigo( rs.getString("MANDADO_PRISAO_STATUS_CODIGO"));
		dados.setValorFianca( Funcoes.FormatarDecimal(rs.getString("VALOR_FIANCA")));
		dados.setId_RegimeExecucao( rs.getString("ID_REGIME_EXE"));
		dados.setRegimeExecucao( rs.getString("REGIME_EXE"));
		dados.setId_PrisaoTipo( rs.getString("ID_PRISAO_TIPO"));
		dados.setPrisaoTipo( rs.getString("PRISAO_TIPO"));
		dados.setPrisaoTipoCodigo( rs.getString("PRISAO_TIPO_CODIGO"));
		dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));
		dados.setProcessoParte( rs.getString("NOME"));
		dados.setId_Assunto( rs.getString("ID_ASSUNTO"));
		dados.setAssunto( rs.getString("ASSUNTO"));
		dados.setDataValidade( Funcoes.FormatarData(rs.getDateTime("DATA_VALIDADE")));
		dados.setOrigem( rs.getString("ORIGEM"));
		dados.setId_MandadoPrisaoOrigem( rs.getString("ID_MANDADO_PRISAO_ORIGEM"));
		dados.setMandadoPrisaoOrigem( rs.getString("MANDADO_PRISAO_ORIGEM"));
		dados.setMandadoPrisaoOrigemCodigo( rs.getString("MANDADO_PRISAO_ORIGEM_CODIGO"));
		dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		dados.setId_Processo( rs.getString("ID_PROC"));
		dados.setProcessoTipo( rs.getString("PROC_TIPO"));
		dados.setProcessoNumero( rs.getString("PROC_NUMERO"));
		dados.setDigitoVerificador( rs.getString("DIGITO_VERIFICADOR"));
		dados.setAno( rs.getString("ANO"));
		dados.setDataNascimento( Funcoes.FormatarData(rs.getDateTime("DATA_NASCIMENTO")));
		dados.setNomeMae( rs.getString("NOME_MAE"));
		dados.setNomePai( rs.getString("NOME_PAI"));
		dados.setUfNaturalidade( rs.getString("NATURALIDADE_UF"));
		dados.setSexo( rs.getString("SEXO"));
		dados.setCpf( rs.getString("CPF"));
		dados.setNaturalidade( rs.getString("NATURALIDADE"));
		dados.setDataAtualizacao( Funcoes.FormatarData(rs.getDateTime("DATA_ATUALIZACAO")));
		dados.setLocalRecolhimento( rs.getString("LOCAL_RECOLHIMENTO"));
		dados.setNumeroOrigem( rs.getString("NUMERO_ORIGEM"));
//			dados.setAssuntoDelitoPrincipal( rs.getString("ASSUNTO_DELITO_PRINCIPAL"));
		dados.setDataCumprimento( Funcoes.FormatarData(rs.getDateTime("DATA_CUMPRIMENTO")));
		dados.setDataEmissao( Funcoes.FormatarData(rs.getDateTime("DATA_EMISSAO")));
		dados.setIdUsuarioServentiaEmissao( rs.getString("ID_USU_SERV_EMISSAO"));
		dados.setIdUsuarioServentiaExpedicao( rs.getString("ID_USU_SERV_EXPEDICAO"));
		dados.setForumCodigo( rs.getString("FORUM_CODIGO"));
		dados.setIdEndereco(rs.getString("ID_ENDERECO"));
		dados.setDataImpressao(Funcoes.FormatarData(rs.getDateTime("DATA_IMPRESSAO")));
		dados.setIdUsuarioServentiaImpressao(rs.getString("ID_USU_SERV_IMPRESSAO"));
		
	}
	
	/**
	 * Listar os mandados de prisão emitidos para o cargo.
	 * @param idServentiaCargo: identificação do cargo
	 * @return
	 */
	public List listarMandadoPrisaoEmitidoServentiaCargo(String idServentiaCargo) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = null;

		stSql= "SELECT mp.* FROM PROJUDI.VIEW_MANDADO_PRISAO mp" +
				" INNER JOIN PROJUDI.PROC p on mp.ID_PROC = p.ID_PROC" +
				" INNER JOIN PROJUDI.PROC_RESP pr on pr.ID_PROC = p.ID_PROC" +
				" WHERE pr.CODIGO_TEMP = 0" + //PROCESSO RESPONSÁVEL ATIVO
				" AND pr.ID_SERV_CARGO = ?" +
				" AND (mp.MANDADO_PRISAO_STATUS_CODIGO = ?" +
				" OR mp.MANDADO_PRISAO_STATUS_CODIGO IS NULL) " +
				" ORDER BY p.PROC_NUMERO, mp.MANDADO_PRISAO_NUMERO, mp.DATA_EMISSAO";		
		ps.adicionarLong(idServentiaCargo);
		ps.adicionarLong(MandadoPrisaoStatusDt.EMITIDO); 
		
		try{
			rs1 = consultar(stSql,ps);
			lista = new ArrayList();
			while (rs1.next()) {
				MandadoPrisaoDt dados= new MandadoPrisaoDt();
				associarDt(dados, rs1);
				lista.add(dados);
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
	
	/**
	 * Listar mandados de prisão para a serventia cargo, conforme o status 
	 * @param idServentiaCargo: identificação do usuário
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @return
	 */
	public List listarMandadoPrisaoServentiaCargo(String idServentiaCargo, List mandadoPrisaoStatusCodigo) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = null;

		stSql= "SELECT mp.* FROM PROJUDI.VIEW_MANDADO_PRISAO mp" +
				" INNER JOIN PROJUDI.PROC p on mp.ID_PROC = p.ID_PROC" +
				" INNER JOIN PROJUDI.PROC_RESP pr on pr.ID_PROC = p.ID_PROC" +
				" WHERE pr.CODIGO_TEMP = 0" + //PROCESSO RESPONSÁVEL ATIVO
				" AND pr.ID_SERV_CARGO = ?" +
				" AND mp.MANDADO_PRISAO_STATUS_CODIGO in (";
		ps.adicionarLong(idServentiaCargo);
		
		for (int i=0; i<mandadoPrisaoStatusCodigo.size(); i++){
			stSql += "?,";
			ps.adicionarLong((String)mandadoPrisaoStatusCodigo.get(i));
		}
		stSql = stSql.substring (0, stSql.length() - 1);
		
		stSql += ") ORDER BY p.PROC_NUMERO, mp.MANDADO_PRISAO_NUMERO, mp.DATA_EMISSAO";		
		
		try{
			rs1 = consultar(stSql,ps);
			lista = new ArrayList();
			while (rs1.next()) {
				MandadoPrisaoDt dados= new MandadoPrisaoDt();
				associarDt(dados, rs1);
				lista.add(dados);
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
	
	/**
	 * Consultar quantidade de mandados de prisão emitidos para o Juiz.
	 * @param idServentiaCargo: identificação do cargo
	 * @return
	 */
	public int consultarQuantidadeMandadoPrisaoEmitidoServentiaCargo(String idServentiaCargo) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int retorno = 0;

		stSql= "SELECT count(1) as quantidade FROM PROJUDI.VIEW_MANDADO_PRISAO mp" +
				" INNER JOIN PROJUDI.PROC p on mp.ID_PROC = p.ID_PROC" +
				" INNER JOIN PROJUDI.PROC_RESP pr on pr.ID_PROC = p.ID_PROC" +
				" WHERE pr.CODIGO_TEMP = 0" + //PROCESSO RESPONSÁVEL ATIVO
				" AND pr.ID_SERV_CARGO = ?" +
				" AND (mp.MANDADO_PRISAO_STATUS_CODIGO = ?" +
				" OR mp.MANDADO_PRISAO_STATUS_CODIGO IS NULL) "; //pode ser null, pois se o juiz salvar o mandado sem expedir, o satus será null, e não foi emitido pela serventia
				
		ps.adicionarLong(idServentiaCargo);
		ps.adicionarLong(MandadoPrisaoStatusDt.EMITIDO); 
		
		try{
			rs1 = consultar(stSql,ps);
			while (rs1.next()) {
				retorno = Funcoes.StringToInt(rs1.getString("quantidade"));
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return retorno; 
	}
	
	/**
	 * Consultar quantidade de mandados de prisão para a serventia cargo, conforme o status 
	 * @param idServentiaCargo: identificação do Juiz
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @return
	 */
	public int consultarQuantidadeMandadoPrisaoServentiaCargo(String idServentiaCargo, List mandadoPrisaoStatusCodigo) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int retorno = 0;

		stSql= "SELECT count(1) as quantidade FROM PROJUDI.VIEW_MANDADO_PRISAO mp" +
				" INNER JOIN PROJUDI.PROC p on mp.ID_PROC = p.ID_PROC" +
				" INNER JOIN PROJUDI.PROC_RESP pr on pr.ID_PROC = p.ID_PROC" +
				" WHERE pr.CODIGO_TEMP = 0" + //PROCESSO RESPONSÁVEL ATIVO
				" AND pr.ID_SERV_CARGO = ?" +
				" AND mp.MANDADO_PRISAO_STATUS_CODIGO in (";
		ps.adicionarLong(idServentiaCargo);
		
		for (int i=0; i<mandadoPrisaoStatusCodigo.size(); i++){
			stSql += "?,";
			ps.adicionarLong((String)mandadoPrisaoStatusCodigo.get(i));
		}
		stSql = stSql.substring (0, stSql.length() - 1);
		
		stSql += ")";		
		
		try{
			rs1 = consultar(stSql,ps);
			while (rs1.next()) {
				retorno = Funcoes.StringToInt(rs1.getString("quantidade"));
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return retorno; 
	}
	
	/**
	 * Consultar quantidade de mandados de prisão para a serventia 
	 * @param idServentia: identificação da serventia
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @param consultaMandadoSigiloso: possibilita a consulta de mandado sigiloso
	 * @return
	 */
	public int consultarQuantidadeMandadoPrisaoServentia(String idServentia, List mandadoPrisaoStatusCodigo, boolean consultaMandadoSigiloso) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int retorno = 0;

		stSql= "SELECT count(1) as quantidade FROM PROJUDI.VIEW_MANDADO_PRISAO mp" +
				" INNER JOIN PROJUDI.PROC p on mp.ID_PROC = p.ID_PROC" +
				" WHERE p.ID_SERV = ?" + 
				" AND mp.MANDADO_PRISAO_STATUS_CODIGO in (";
		ps.adicionarLong(idServentia);
		
		for (int i=0; i<mandadoPrisaoStatusCodigo.size(); i++){
			stSql += "?,";
			ps.adicionarLong((String)mandadoPrisaoStatusCodigo.get(i));
		}
		stSql = stSql.substring (0, stSql.length() - 1);		
		stSql += ")";
		
		if (!consultaMandadoSigiloso){
			stSql += " AND mp.SIGILO = ?";
			ps.adicionarBoolean(false);
		}
		
				
		try{
			rs1 = consultar(stSql,ps);
			while (rs1.next()) {
				retorno = Funcoes.StringToInt(rs1.getString("quantidade"));
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return retorno; 
	}
	
	/**
	 * Listar os mandados de prisão para a serventia 
	 * @param idServentia: identificação da serventia
	 * @param mandadoPrisaoStatusCodigo: código do status do mandado de prisão
	 * @param consultaMandadoSigiloso: possibilita a consulta de mandado sigiloso
	 * @return
	 */
	public List listarMandadoPrisaoServentia(String idServentia, List mandadoPrisaoStatusCodigo, boolean consultaMandadoSigiloso) throws Exception{
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_MANDADO_PRISAO mp" +
				" INNER JOIN PROJUDI.PROC p on mp.ID_PROC = p.ID_PROC" +
				" WHERE p.ID_SERV = ?" + 
				" AND mp.MANDADO_PRISAO_STATUS_CODIGO in (";
		ps.adicionarLong(idServentia);
		
		for (int i=0; i<mandadoPrisaoStatusCodigo.size(); i++){
			stSql += "?,";
			ps.adicionarLong((String)mandadoPrisaoStatusCodigo.get(i));
		}
		stSql = stSql.substring (0, stSql.length() - 1);		
		stSql += ")";
		
		if (!consultaMandadoSigiloso){
			stSql += " AND mp.SIGILO = ?";
			ps.adicionarBoolean(false);
		}
		
		stSql += " ORDER BY mp.DATA_EXPEDICAO";		
		
		try{
			rs1 = consultar(stSql,ps);
			lista = new ArrayList();
			while (rs1.next()) {
				MandadoPrisaoDt dados= new MandadoPrisaoDt();
				associarDt(dados, rs1);
				lista.add(dados);
			}
		} finally { 
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return lista; 
	}
}
