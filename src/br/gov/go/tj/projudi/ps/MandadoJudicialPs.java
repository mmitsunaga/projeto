package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.HistoricoRedistribuicaoMandadosDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MandadoJudicialPs extends MandadoJudicialPsGen{

	private static final long serialVersionUID = 2201502035858026568L;

	@SuppressWarnings("unused")
	private MandadoJudicialPs( ) {}
	
	public MandadoJudicialPs(Connection conexao){
		Conexao = conexao;
	}

	public void inserir(MandadoJudicialDt dados ) throws Exception {
		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO projudi.MAND_JUD ("; 
		SqlValores =  " Values (";
		
		// Obrigatório informar o número de mandado que foi reservado ao clicar no botão "Expedir". A reserva de números foi necessária para
		// a inclusão da variável "número do mandado" na elaboração do mesmo, ou seja, momento anterior à sua criação na tabela.
		if ((dados.getId().length()>0)){
			SqlCampos+= "ID_MAND_JUD " ;
			SqlValores += "?";
			ps.adicionarLong(dados.getId());
		}
		else {
			throw new MensagemException("Erro: Informe o número reservado para o novo mandado ao realizar a inserção do registro.");
		}
		
		if ((dados.getId_ServentiaCargoEscala().length()>0)){
			SqlCampos+= ",ID_SERV_CARGO_ESC " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ServentiaCargoEscala());
		}
		
		if ((dados.getId_Modelo().length()>0)){
			SqlCampos+= ",ID_MODELO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Modelo());
		}
		
		if ((dados.getId_MandadoTipo().length()>0)){
			SqlCampos+= ",ID_MAND_TIPO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_MandadoTipo());
		}
		if ((dados.getId_MandadoJudicialStatus().length()>0)){
			SqlCampos+= ",ID_MAND_JUD_STATUS " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_MandadoJudicialStatus());
		}
		if ((dados.getId_Area().length()>0)){
			SqlCampos+= ",ID_AREA " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Area());
		}
		if ((dados.getId_Zona().length()>0)){
			SqlCampos+= ",ID_ZONA " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Zona());
		}
		if ((dados.getId_Regiao().length()>0)){
			SqlCampos+= ",ID_REGIAO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Regiao());
		}
		if ((dados.getId_Bairro().length()>0)){
			SqlCampos+= ",ID_BAIRRO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Bairro());
		}
		if ((dados.getId_ProcessoParte().length()>0)){
			SqlCampos+= ",ID_PROC_PARTE " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoParte());
		}
		if ((dados.getId_EnderecoParte().length()>0)){
			SqlCampos+= ",ID_ENDERECO_PARTE " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_EnderecoParte());
		}
		if ((dados.getId_Pendencia().length()>0)){
			SqlCampos+= ",ID_PEND " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Pendencia());
		}
		if ((dados.getId_Escala().length()>0)){
			SqlCampos+= ",ID_ESC " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Escala());
		}
		if (dados.getValor() != null && dados.getValor().length()>0){
			SqlCampos+= ",VALOR " ;
			SqlValores += ",?";
			ps.adicionarDecimal(dados.getValor());
		}
		if ((dados.getAssistencia().length()>0)){
			SqlCampos+= ",ASSISTENCIA " ;
			SqlValores += ",?";
			ps.adicionarBoolean(dados.getAssistencia());
		}
		if ( dados.getDataDistribuicao().length() > 0 ){
			SqlCampos += ",DATA_DIST";
			SqlValores += ",?";
			ps.adicionarDateTime(new Date());
		}
		if ( dados.getDataLimite().length() > 0 ){
			SqlCampos += ",DATA_LIMITE";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataLimite());
		}
		if ( dados.getId_UsuarioServentia_1().length() > 0 ){
			SqlCampos += ",ID_USU_SERV_1";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioServentia_1());
		}
		if( dados.getId_UsuarioServentia_2().length() > 0 ){
			SqlCampos += ",ID_USU_SERV_2";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioServentia_2());
		}
		if( dados.getIdMandJudPagamentoStatus().length() > 0 ){
			SqlCampos += ",ID_MAND_JUD_PAGAMENTO_STATUS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getIdMandJudPagamentoStatus());
		}
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_MAND_JUD", ps));
	} 
	
	/**
	 * Método de Consulta do Mandado Judicial qua retorna o objeto Mandado Judicial com os objetos encapsulado.
	 * @param MandadoJudicialDt
	 * @return MandadoJudicialDt
	 * @throws Exception
	 */
	public MandadoJudicialDt consultarId(String idMandJud)  throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql= "SELECT * FROM PROJUDI.VIEW_MAND_JUD WHERE ID_MAND_JUD = ?";
		ps.adicionarLong(idMandJud);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {				
				dados = new MandadoJudicialDt();
				associarDt(dados, rs1); 
 				dados.setIdMandJudPagamentoStatus(rs1.getString("ID_MAND_JUD_PAGAMENTO_STATUS"));
 				dados.setMandJudPagamentoStatus(rs1.getString("MAND_JUD_PAGAMENTO_STATUS"));
 				dados.setIdComarca(rs1.getString("ID_COMARCA"));
 				dados.setComarca(rs1.getString("COMARCA"));
 				dados.setProcNumero(rs1.getString("PROC_NUMERO"));
 				dados.setIdUsuPagamentoStatus(rs1.getString("ID_USU_PAGAMENTO_STATUS"));
 				dados.setNomeUsuPagamentoStatus(rs1.getString("NOME_USU_PAGAMENTO_STATUS"));
 				dados.setDataPagamentoStatus(rs1.getString("DATA_PAGAMENTO_STATUS"));
 				dados.setIdUsuPagamentoEnvio(rs1.getString("ID_USU_PAGAMENTO_ENVIO"));
 				dados.setNomeUsuPagamentoEnvio(rs1.getString("NOME_USU_PAGAMENTO_ENVIO"));
 				dados.setDataPagamentoEnvio(rs1.getString("DATA_PAGAMENTO_ENVIO"));
 				dados.setId_Proc(rs1.getString("ID_PROC"));
 				dados.setId_Modelo(rs1.getString("ID_MODELO"));
 				dados.setResolutivo(rs1.getString("RESOLUTIVO"));
  				
 				UsuarioServentiaDt UsuarioServentiaDt = new UsuarioServentiaDt();
 		        if(rs1.getString("ID_USU_SERV_1") != null ) {
 		           UsuarioServentiaDt.setId(rs1.getString("ID_USU_SERV_1"));
 			       UsuarioServentiaDt.setNome(rs1.getString("NOME_USU_SERV_1"));
 		    	   dados.setUsuarioServentiaDt_1(UsuarioServentiaDt);
 		        }
 		        if(rs1.getString("ID_USU_SERV_2") != null ) {
 			       UsuarioServentiaDt UsuarioServentiaDt_2 = new UsuarioServentiaDt();
 		    	   UsuarioServentiaDt_2.setId(rs1.getString("ID_USU_SERV_2"));
 		    	   UsuarioServentiaDt_2.setNome(rs1.getString("NOME_USU_SERV_2"));
 			       dados.setUsuarioServentiaDt_2(UsuarioServentiaDt_2);
 		        }
 			    else
 			    dados.setUsuarioServentiaDt_2(null);				
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return dados; 
	}
	
	public List consultarMandadosAbertos(String numero, String idUsuarioServentia, String idServentia)  throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		List tempList = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_MAND_JUD WHERE ID_SERV = ? AND DATA_RETORNO IS NULL AND ID_MAND_JUD_STATUS IN (SELECT ID_MAND_JUD_STATUS FROM PROJUDI.MAND_JUD_STATUS WHERE MAND_JUD_STATUS_CODIGO IN (?,?)) ";
		ps.adicionarLong(idServentia);
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
		ps.adicionarLong(MandadoJudicialStatusDt.REDISTRIBUIDO);
		
		if (numero != null && numero.length()>0){
			Sql+= " AND ID_MAND_JUD = ?"; 
			ps.adicionarLong(numero);
		}
				
		if (idUsuarioServentia != null && idUsuarioServentia.length()>0){
			Sql+= " AND ID_USU_SERV_1 = ?"; 
			ps.adicionarLong(idUsuarioServentia);
		}
		
		Sql+= " ORDER BY ID_MAND_JUD DESC";
		
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				dados = new MandadoJudicialDt();
				dados.setId(rs1.getString("ID_MAND_JUD"));
				dados.setId_MandadoTipo(rs1.getString("ID_MAND_TIPO"));
				dados.setMandadoTipo(rs1.getString("MAND_TIPO"));
				dados.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				dados.setProcessoParte(rs1.getString("PROC_PARTE"));
				dados.setId_EnderecoParte(rs1.getString("ID_ENDERECO_PARTE"));
				dados.setEnderecoParte(rs1.getString("ENDERECO_PARTE"));
				dados.setId_Pendencia(rs1.getString("ID_PEND"));
				dados.setId_Area(rs1.getString("ID_AREA"));
				dados.setArea(rs1.getString("AREA"));
				dados.setId_Zona(rs1.getString("ID_ZONA"));
				dados.setZona(rs1.getString("ZONA"));
				dados.setId_Regiao(rs1.getString("ID_REGIAO"));
				dados.setRegiao(rs1.getString("REGIAO"));
				dados.setId_Bairro(rs1.getString("ID_BAIRRO"));
				dados.setBairro(rs1.getString("BAIRRO"));
				dados.setId_Escala(rs1.getString("ID_ESC"));
				dados.setEscala(rs1.getString("ESC"));
				dados.setValor(rs1.getString("VALOR"));
				dados.setAssistencia(rs1.getString("ASSISTENCIA"));
				dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				dados.setId_MandadoJudicialStatus(rs1.getString("ID_MAND_JUD_STATUS"));
				dados.setMandadoJudicialStatus(rs1.getString("MAND_JUD_STATUS"));
				dados.setId_UsuarioServentia_1(rs1.getString("ID_USU_SERV_1"));
				dados.setId_UsuarioServentia_2(rs1.getString("ID_USU_SERV_2"));
				dados.setDataDistribuicao(rs1.getString("DATA_DIST"));
				dados.setDataRetorno(rs1.getString("DATA_RETORNO"));
				dados.setDataLimite(rs1.getString("DATA_LIMITE"));
				dados.setId_UsuarioServentia_2(rs1.getString("ID_USU_SERV_2"));
				dados.setId_Serventia(rs1.getString("ID_SERV"));
				dados.setId_UsuarioServentia_2(rs1.getString("ID_SERV_CARGO_ESC"));
				dados.setLocomocoesFrutiferas(rs1.getString("LOCOMOCOES_FRUTIFERAS"));
				dados.setLocomocoesInfrutiferas(rs1.getString("LOCOMOCOES_FRUTIFERAS"));
				dados.setLocomocaoHoraMarcada(rs1.getString("LOCOMOCAO_HORA_MARCADA"));

			    UsuarioServentiaDt UsuarioServentiaDt = new UsuarioServentiaDt();
			    if( rs1.getString("ID_USU_SERV_1") != null ) {
			    	UsuarioServentiaDt.setId(rs1.getString("ID_USU_SERV_1"));
			    	UsuarioServentiaDt.setNome(rs1.getString("NOME_USU_SERV_1"));
			    	dados.setUsuarioServentiaDt_1(UsuarioServentiaDt);
			    }
			    if( rs1.getString("ID_USU_SERV_2") != null ) {
			    	UsuarioServentiaDt UsuarioServentiaDt_2 = new UsuarioServentiaDt();
			    	UsuarioServentiaDt_2.setId(rs1.getString("ID_USU_SERV_2"));
			    	UsuarioServentiaDt_2.setNome(rs1.getString("NOME_USU_SERV_2"));
			    	dados.setUsuarioServentiaDt_2(UsuarioServentiaDt_2);
			    } else dados.setUsuarioServentiaDt_2(null);
			    
				tempList.add(dados);
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return tempList; 
	}
	
	/**
	 * Consulta de mandados que tiveram o pagamento aprovado (status de Autorizado ou Enviado).
	 * @param numeroMandado
	 * @param nomeOficial
	 * @param idProcesso
	 * @param posicao
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarMandadosPagamentoAprovadoJson(String numeroMandado, String nomeOficial, String idProcesso, String posicao)  throws Exception {

		String sqlSelect;
		String sqlOrderBy;
		String sqlFrom;
		String sqlWhere;
		int qtdColunas = 1;
		String resultado = null;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sqlSelect = "SELECT MJ.ID_MAND_JUD AS ID,  MJ.ID_MAND_JUD || ' - ' || MJ.NOME_USU_SERV_1 AS DESCRICAO1 ";
		sqlFrom = " FROM PROJUDI.VIEW_MAND_JUD MJ ";
		sqlWhere = " WHERE MJ.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		
		sqlWhere += " AND MJ.ID_MAND_JUD_PAGAMENTO_STATUS IN (?,?)";
		ps.adicionarLong(MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO);
		ps.adicionarLong(MandadoJudicialDt.ID_PAGAMENTO_ENVIADO);
		
		if (numeroMandado != null && !numeroMandado.isEmpty()){
			sqlWhere += " AND MJ.ID_MAND_JUD = ?"; 
			ps.adicionarLong(numeroMandado);
		}
		
		if (nomeOficial != null && !nomeOficial.isEmpty()){
			sqlWhere += " AND MJ.NOME_USU_SERV_1 LIKE ? "; 
			ps.adicionarString("%"+nomeOficial+"%");
		}
		
		sqlOrderBy = " ORDER BY MJ.ID_MAND_JUD DESC";
		
		try{
			rs1 = consultarPaginacao(sqlSelect + sqlFrom + sqlWhere + sqlOrderBy, ps, posicao);
			rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlFrom + sqlWhere, ps);
			rs2.next();
			resultado = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdColunas);
		}finally{
             try{
            	 if (rs1 != null) rs1.close();
            	 if (rs2 != null) rs2.close();
             } catch(Exception e1) {}
        } 
		
		return resultado; 
	}

	
	/**
	 * Consultar Mandado Judicial por Id da Pendência.
	 * @param String idPendencia
	 * @return MandadoJudicialDt
	 * @throws Exception
	 */
	public MandadoJudicialDt consultarPorIdPendencia(String idPendencia) throws Exception {
		String Sql;
		MandadoJudicialDt dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM projudi.VIEW_MAND_JUD WHERE ID_PEND = ?";
		ps.adicionarLong(idPendencia);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				dados = new MandadoJudicialDt();
			    
				associarDt(dados, rs1);
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return dados; 
	}
	
	/**
	 * Método de alteração do status do Mandado Judicial
	 * @param MandadoJudicialDt mandadoJudicialDt
	 * @param Integer novoStatus
	 * @return boolean
	 * @throws Exception
	 */
	public boolean alterarStatusMandadoJudicial(MandadoJudicialDt mandadoJudicialDt, Integer novoStatus) throws Exception {
		boolean retorno = false;
		StringBuffer sql = new StringBuffer("UPDATE projudi.MAND_JUD SET ");
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" ID_MAND_JUD_STATUS = ?");
		ps.adicionarLong(novoStatus);
		sql.append(", LOCOMOCOES_FRUTIFERAS = ?");
		ps.adicionarLong(mandadoJudicialDt.getLocomocoesFrutiferas());
		sql.append(", LOCOMOCOES_INFRUTIFERAS = ?");
		ps.adicionarLong(mandadoJudicialDt.getLocomocoesInfrutiferas());
		sql.append(", LOCOMOCAO_HORA_MARCADA = ?");
		ps.adicionarLong(mandadoJudicialDt.getLocomocaoHoraMarcada());
		sql.append(", DATA_RETORNO = ?");
		ps.adicionarDateTime(new Date());	
		
		sql.append(" WHERE ID_MAND_JUD  = ?");
		ps.adicionarLong(mandadoJudicialDt.getId());
		
		this.executarUpdateDelete(sql.toString(), ps);
		
		retorno = true;
		
		return retorno;
	}
	
	/**
	 * Consulta lista de mandados que estão com os prazos vencidos.
	 * @return List<MandadoJudicialDt>
	 * @throws Exception
	 */
	public List consultarMandadosPrazosExpirados() throws Exception {
		List listaMandados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT ID_MAND_JUD,ID_USU_SERV_1,ID_USU_SERV_2 FROM projudi.MAND_JUD WHERE ? > DATA_LIMITE AND DATA_RETORNO IS NULL AND ID_MAND_JUD_STATUS = ?";
		ResultSetTJGO rs1 = null;
		
		ps.adicionarDateTime(new Date());
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
		
		try{
			rs1 = this.consultar(sql, ps);
			while(rs1.next()) {
				if( listaMandados == null )
					listaMandados = new ArrayList();
				
				MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
				mandadoJudicialDt.setId(rs1.getString("ID_MAND_JUD"));
				
				UsuarioServentiaDt UsuarioServentiaDt_1 = new UsuarioServentiaDt();
				UsuarioServentiaDt_1.setId(rs1.getString("ID_USU_SERV_1"));
				mandadoJudicialDt.setUsuarioServentiaDt_1(UsuarioServentiaDt_1);
				
				if( rs1.getString("ID_USU_SERV_2") != null ) {
					UsuarioServentiaDt UsuarioServentiaDt_2 = new UsuarioServentiaDt();
					UsuarioServentiaDt_2.setId(rs1.getString("ID_USU_SERV_2"));
					mandadoJudicialDt.setUsuarioServentiaDt_2(UsuarioServentiaDt_2);
				}
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return listaMandados;
	}
	
	/**
	 * Método que consulta mandados retornados que estão com prazos de data limite expirado.
	 * @return List<MandadoJudicialDt>
	 * @throws Exception
	 */
	public List consultarMandadosRetornadosPrazosExpirados() throws Exception {
		List listaMandados = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuffer sql = new StringBuffer("SELECT m.ID_MAND_JUD, m.ID_USU_SERV_1, m.ID_USU_SERV_2 FROM projudi.MAND_JUD m WHERE ");
		sql.append(" (SELECT COUNT(mj.ID_MAND_JUD) FROM projudi.MAND_JUD mj WHERE m.ID_MAND_JUD = mj.ID_MAND_JUD AND ? > mj.DATA_LIMITE AND mj.DATA_RETORNO IS NULL AND mj.ID_MAND_JUD_STATUS = ?) > 0 ");
		ps.adicionarDateTime(new Date());
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
		sql.append(" AND ? > m.DATA_LIMITE ");
		ps.adicionarDateTime(new Date());
		sql.append(" AND m.DATA_RETORNO IS NOT NULL ");
		sql.append(" AND m.ID_MAND_JUD_STATUS <> ?");
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO );
		
		ResultSetTJGO rs1 = null;
		
		try{
			rs1 = this.consultar(sql.toString(), ps);
			while(rs1.next()) {
				if( listaMandados == null )
					listaMandados = new ArrayList();
				
				MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
				mandadoJudicialDt.setId(rs1.getString("ID_MAND_JUD"));
				
				UsuarioServentiaDt UsuarioServentiaDt_1 = new UsuarioServentiaDt();
				UsuarioServentiaDt_1.setId(rs1.getString("ID_USU_SERV_1"));
				mandadoJudicialDt.setUsuarioServentiaDt_1(UsuarioServentiaDt_1);
				
				if( rs1.getString("ID_USU_SERV_2") != null ) {
					UsuarioServentiaDt UsuarioServentiaDt_2 = new UsuarioServentiaDt();
					UsuarioServentiaDt_2.setId(rs1.getString("ID_USU_SERV_2"));
					mandadoJudicialDt.setUsuarioServentiaDt_2(UsuarioServentiaDt_2);
				}
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return listaMandados;
	}
	
	/**
	 * Método para Alterar a Data limite do Mandado Judicial.
	 * @param MandadoJudicialDt
	 * @return boolean
	 * @throws Exception
	 */
	public boolean alterarDataLimite(MandadoJudicialDt mandadoJudicialDt) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE projudi.MAND_JUD SET DATA_LIMITE = ? WHERE ID_MAND_JUD  = ?";
		ps.adicionarDate(mandadoJudicialDt.getDataLimite());
		ps.adicionarLong(mandadoJudicialDt.getId());
		this.executarUpdateDelete(sql, ps);
		
		retorno = true;
		
		return retorno;
	}
	
	/**
	 * Método para Alterar Tipo Assitencia mandado.
	 * @param MandadoJudicialDt
	 * @return boolean
	 * @throws Exception
	 */
	public int alterarAssistencia(String idMandJud, int isAssistencia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE projudi.MAND_JUD SET ASSISTENCIA = ? WHERE ID_MAND_JUD  = ?";
		ps.adicionarLong(isAssistencia);
		ps.adicionarLong(idMandJud);
		return this.executarUpdateDelete(sql, ps);
	}

	public int salvarDataRetorno(String dataFim, String idPendencia) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql="";
		
		sql= "UPDATE projudi.MAND_JUD SET DATA_RETORNO = ? WHERE ID_PEND  = ? AND ID_MAND_JUD_STATUS <> (SELECT ID_MAND_JUD_STATUS FROM MAND_JUD_STATUS WHERE MAND_JUD_STATUS_CODIGO = ?) ";
		ps.adicionarDate(dataFim);
		ps.adicionarLong(idPendencia);
		ps.adicionarLong(MandadoJudicialStatusDt.REDISTRIBUIDO);
		
		 return this.executarUpdateDelete(sql, ps);
		
	}
	
	public MandadoJudicialDt consultarIdFinalizado(String id_mandadojudicial )  throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoJudicialDt Dados=null;

		stSql= "SELECT * FROM projudi.VIEW_MAND_JUD WHERE ID_MAND_JUD = ? AND DATA_RETORNO IS NOT NULL";		ps.adicionarLong(id_mandadojudicial); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoJudicialDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	/**
	 * Verifica se algum dos mandados da lista não pertencem ao oficial especificado.
	 * @param listaMandadoId
	 * @param usuId
	 * @return boolean
	 * @throws Exception
	 * @author hrrosa
	 */
	public boolean testarPropriedadeMandado(String[] listaMandadoId, String usuId )  throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		if(listaMandadoId == null || listaMandadoId.length < 1){
			return false;
		}
		
		stSql =  " SELECT 1 AS IMPROPRIO FROM PROJUDI.MAND_JUD MJ ";
		stSql += " WHERE MJ.ID_MAND_JUD IN (";
		
		for(String id: listaMandadoId){
			stSql += "?,"; ps.adicionarLong(id);
		}
		if( stSql.endsWith(",") ) { 
			stSql = stSql.substring(0, stSql.length() - 1);
		}
		
		stSql += ")";
		stSql += " AND MJ.ID_USU_SERV_1 <> ? ";
		ps.adicionarLong(usuId);
		
		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				return false;
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
		return true;
	}
	
	public void finalizaMandadoAbertoIdPend(String idPend, String idOficialCompanheiro, int codigoMandJudStatus) throws Exception {

		int qtdLinhasAlteradas = 0;
		String sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql =  " UPDATE MAND_JUD MJ ";
		sql += " SET MJ.ID_USU_SERV_2 = ?, ";					ps.adicionarLong(idOficialCompanheiro);	
		sql += " MJ.ID_MAND_JUD_STATUS = ( ";
		sql += "	SELECT MJS.ID_MAND_JUD_STATUS FROM MAND_JUD_STATUS MJS ";
		sql += " 	WHERE MJS.MAND_JUD_STATUS_CODIGO = ?) ";	ps.adicionarLong(codigoMandJudStatus);
		sql += " WHERE MJ.ID_MAND_JUD = ( ";
		sql += " 	SELECT MJ.ID_MAND_JUD FROM PROJUDI.MAND_JUD MJ ";
		sql += " 	INNER JOIN PROJUDI.MAND_JUD_STATUS MJS ON MJ.ID_MAND_JUD_STATUS = MJS.ID_MAND_JUD_STATUS "; 
		sql += " 	WHERE MJS.MAND_JUD_STATUS_CODIGO IN (?,?) ";		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO); ps.adicionarLong(MandadoJudicialStatusDt.REDISTRIBUIDO); 
		sql += " 	AND MJ.ID_PEND = ?) "; 						ps.adicionarLong(idPend);
		
		qtdLinhasAlteradas = this.executarUpdateDelete(sql, ps);
		
		//Para cada pendência de mandado finalizada deverá haver um mandado concluído na tabela mand_jud
		if(qtdLinhasAlteradas != 1) {
			throw new MensagemException("Erro ao atualizar o registro do mandado. Entre em contato com a equipe de suporte.");
		}
		
	}
	
	/**
	 * Retorna todos os mandados expirados de oficiais que ainda não constam na tabela de afastamento.
	 * Utilizado para o serviço da execução automática que suspende automaticamente estes oficiais.
	 * @return List<MandadoJudicialDt>
	 * @author hrrosa
	 * @throws Exception
	 */
	public List<MandadoJudicialDt> consultarMandadosAtrasadosExecucaoAutomatica() throws Exception {
		List<MandadoJudicialDt> listaMandadosAtrasados = null;
		MandadoJudicialDt mandadoJudicialDt = null;		
		
		String sql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = " SELECT MJ.ID_MAND_JUD, MJ.ID_USU_SERV_1, MJ.ID_USU_SERV_2 FROM PEND P ";
		sql += " JOIN MAND_JUD MJ ON MJ.ID_PEND = P.ID_PEND ";
		sql += " WHERE P.ID_PEND_TIPO = ";
		sql	+= "			(SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)"; 		ps.adicionarLong(PendenciaTipoDt.MANDADO);
		sql += " AND P.ID_PEND_STATUS = ";
		sql += "			(SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)"; 	ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_RETORNO);
		sql += " AND P.DATA_LIMITE < SYSDATE ";
		sql += " AND MJ.ID_USU_SERV_1 NOT IN ";
		sql += " ( SELECT USA.ID_USU_SERV FROM USU_SERV_AFASTAMENTO USA ";
		sql += "   INNER JOIN MAND_JUD MJ ON MJ.ID_USU_SERV_1 = USA.ID_USU_SERV ";
		sql += "   WHERE MJ.ID_USU_SERV_1 = USA.ID_USU_SERV ";
		sql += "   AND USA.ID_USU_SERV_FINALIZADOR IS NULL) ";
		
		sql += " ORDER BY MJ.ID_USU_SERV_1";

		try{
			rs1 = consultar(sql,ps);
			listaMandadosAtrasados = new ArrayList<MandadoJudicialDt>();
			while (rs1.next()) {
				mandadoJudicialDt = new MandadoJudicialDt();
				mandadoJudicialDt.setId(rs1.getString("ID_MAND_JUD"));
				mandadoJudicialDt.setId_UsuarioServentia_1(rs1.getString("ID_USU_SERV_1"));
				mandadoJudicialDt.setId_UsuarioServentia_2(rs1.getString("ID_USU_SERV_2"));
				listaMandadosAtrasados.add(mandadoJudicialDt);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
		
		if(listaMandadosAtrasados.isEmpty())
			return null;
		else
			return listaMandadosAtrasados;
	}
	public List<MandadoJudicialDt> consultaOficiaisParaRetornoDeSuspensao() throws Exception {
		List<MandadoJudicialDt> listaOficiais = new ArrayList<>();
		MandadoJudicialDt mandadoJudicialDt = null;		
		
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("SELECT usa.id_usu_serv AS idUsuServ FROM usu_serv_afastamento usa"
			+ " WHERE  usa.id_usu_serv_finalizador IS NULL"
			+ "	AND usa.id_usu_serv NOT IN ("
			+ "	(SELECT DISTINCT mj.id_usu_serv_1 FROM pend p"           // mandados atrazados
			+ "	INNER JOIN mand_jud mj ON mj.id_pend = p.id_pend"
			+ " WHERE p.id_pend_tipo = ? AND   p.id_pend_status = ?	AND  P.data_limite < SYSDATE))");
	 	
		ps.adicionarLong(PendenciaTipoDt.MANDADO);
    	ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_RETORNO);
		
    	try{
			rs1 = consultar(sql.toString(),ps);
			while (rs1.next()) {
				mandadoJudicialDt = new MandadoJudicialDt();
				mandadoJudicialDt.setId_UsuarioServentia_1(rs1.getString("idUsuServ"));		 
				listaOficiais.add(mandadoJudicialDt);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}		
		return listaOficiais;
	}
	
	
	public String consultarMeusMandadosOficialJSON(String idUsuServ, String dataInicial, String dataFinal, String posicao) throws Exception {
		String sqlSelect="";
		String sqlFrom="";
		String sqlOrderBy="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 7;
		
		sqlSelect = "SELECT ID_MAND_JUD AS ID,  MJ.REGIAO AS DESCRICAO1, MJ.AREA AS DESCRICAO2, MJ.MAND_TIPO AS DESCRICAO3, MJ.ZONA AS DESCRICAO4, "
			    +   " TO_CHAR(MJ.DATA_DIST, 'dd/mm/yyyy') AS DESCRICAO5, TO_CHAR(P.DATA_LIMITE, 'DD/MM/YYYY')  AS DESCRICAO6, MJ.MAND_JUD_STATUS AS DESCRICAO7"; 
		sqlFrom = " FROM VIEW_MAND_JUD MJ"
				+ " LEFT JOIN PROJUDI.PEND P ON P.ID_PEND = MJ.ID_PEND"
				+ " WHERE MJ.ID_USU_SERV_1 = ? ";
		ps.adicionarLong(idUsuServ);
		
		if(dataInicial != null && !dataInicial.isEmpty()){
			sqlFrom += " AND MJ.DATA_DIST > ? ";
			ps.adicionarDate(dataInicial);
		}
		if(dataFinal != null && !dataFinal.isEmpty()){
			sqlFrom += " AND MJ.DATA_DIST < ? ";
			ps.adicionarDate(dataFinal);
		}		
		
		sqlOrderBy = " ORDER BY  TO_CHAR(P.DATA_LIMITE, 'yyyymmdd')";
		
		try{
			rs1 = consultarPaginacao(sqlSelect + sqlFrom + sqlOrderBy, ps, posicao);
			rs2 = consultar("SELECT COUNT(*) AS QUANTIDADE" + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarSituacaoDistribuicaoCentral(String idServCentral, String idEsc, String posicao) throws Exception {
		String stTemp = "";
		String sqlSelect;
		String sqlFrom;
		String sqlOrderBy;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		sqlSelect = " SELECT VMJ.ID_USU_SERV_1 AS Id, VMJ.NOME_USU_SERV_1 AS DESCRICAO1, COUNT(*) AS DESCRICAO2 ";
		sqlFrom =  " FROM VIEW_MAND_JUD VMJ "; 
		sqlFrom += " INNER JOIN ESC E ON E.ID_ESC = VMJ.ID_ESC ";
		sqlFrom += " WHERE VMJ.ID_SERV = ? "; 	ps.adicionarLong(idServCentral);
		sqlFrom += " AND E.ID_ESC = ? "; 		ps.adicionarLong(idEsc);
		sqlFrom += " GROUP BY VMJ.ID_USU_SERV_1, E.ESC, VMJ.NOME_USU_SERV_1 ";
		
		sqlOrderBy = " ORDER BY COUNT(*) DESC, VMJ.NOME_USU_SERV_1 ";
		
		try{
			rs1 = consultarPaginacao(sqlSelect + sqlFrom + sqlOrderBy, ps, posicao);
			rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE FROM (SELECT 1 " + sqlFrom + ")", ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		
		return stTemp;
	} 
	
	public List consultaMandadosPorDataRetornoCustas(String idServentiaSessao, String dataReferencia, String idStatusOrdem) throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		List<MandadoJudicialDt> tempList = new ArrayList();
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps;
		try {
			ps = new PreparedStatementTJGO();
			Sql = "SELECT mj.id_mand_jud AS idMandado, mj.mand_tipo AS mandTipo, mj.proc_numero AS procNumero,"		
				+ " mj.mand_jud_pagamento_status AS mandJudPagamentoStatus, mj.nome_usu_serv_1 AS nomeUsuario,"
				+ " mj.nome_usu_serv_2 AS nomeOficialCompanheiro, mj.data_retorno AS dataRetorno,"
				+ " mj.id_mand_jud_pagamento_status as idMandJudPagamentoStatus"
				+ " FROM projudi.view_mand_jud mj"
				+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1" 
				+ " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv" 
				+ "	WHERE  mj.assistencia = ? AND mj.data_retorno BETWEEN ? AND ?";
			
			ps.adicionarLong(MandadoJudicialDt.NAO_ASSISTENCIA);	
			
			ps.adicionarDateTimePrimeiraHoraDia(Funcoes.somaData(dataReferencia, -60));  //  apenas ate 60 dias antes da data informada
			
			ps.adicionarDateTimeUltimaHoraDia(dataReferencia);
			 
			if(idStatusOrdem != null && !idStatusOrdem.isEmpty()) {
			    Sql += " AND mj.id_mand_jud_pagamento_status = ? ";
				ps.adicionarString(idStatusOrdem);
			}
			 
			if(idServentiaSessao != null && !idServentiaSessao.isEmpty()) {
			    Sql += " AND us.id_serv = ? ";
				ps.adicionarString(idServentiaSessao);
			}
			
			Sql += "ORDER BY nomeUsuario, TO_CHAR(dataRetorno, 'yyyymmdd') ";
 
			rs = consultar(Sql, ps);
			while (rs.next()) {
				dados = new MandadoJudicialDt();
				dados.setId(rs.getString("idMandado"));
				dados.setMandadoTipo(rs.getString("mandTipo"));
				dados.setProcNumero(rs.getString("procNumero"));
				dados.setNomeUsuarioServentia_1(rs.getString("nomeUsuario"));	
				dados.setNomeUsuarioServentia_2(rs.getString("nomeOficialCompanheiro"));	
				dados.setMandJudPagamentoStatus(rs.getString("mandJudPagamentoStatus"));
				dados.setIdMandJudPagamentoStatus(rs.getString("idMandJudPagamentoStatus"));
				dados.setDataRetorno(Funcoes.FormatarData(rs.getDate("dataRetorno")));
				tempList.add(dados);
			}			
			
		}
		finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }		
		return tempList; 
	}
	
	
	public List<MandadoJudicialDt> consultaMandadosPorDataRetornoGratuito(String idServentiaSessao, String dataReferencia, String idStatusOrdem) throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		List<MandadoJudicialDt> tempList = new ArrayList<MandadoJudicialDt>();
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps;
		try {
			ps = new PreparedStatementTJGO();
			
			String mes = dataReferencia.substring(3, 5);
			String ano = dataReferencia.substring(6, 10);
			Date primeiroDiaMes = Funcoes.getPrimeiroDiaMes(Funcoes.StringToInt(mes), Funcoes.StringToInt(ano));
			Date ultimoDiaMes = Funcoes.getUltimoDiaMes(mes, ano);
			
			
			Sql = "SELECT idMandado, mandTipo, procNumero, mandJudPagamentoStatus, nomeUsuario, nomeOficialCompanheiro, dataRetorno, idMandJudPagamentoStatus ";
			Sql += " from ( ";
			Sql += "SELECT mj.id_mand_jud AS idMandado, mj.mand_tipo AS mandTipo, mj.proc_numero AS procNumero,"		
					+ " mj.mand_jud_pagamento_status AS mandJudPagamentoStatus, mj.nome_usu_serv_1 AS nomeUsuario,"
					+ " mj.nome_usu_serv_2 AS nomeOficialCompanheiro, mj.data_retorno AS dataRetorno,"
					+ " mj.id_mand_jud_pagamento_status as idMandJudPagamentoStatus,";
			Sql += " ROW_NUMBER() OVER (PARTITION BY mj.id_usu_serv_1 ORDER BY mj.id_mand_jud) as idx ";
			Sql += " FROM PROJUDI.VIEW_MAND_JUD MJ ";
			Sql += " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1 "; 
			Sql += " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv "; 
			Sql += " where mj.data_dist between ? and ? ";
			ps.adicionarDateTimePrimeiraHoraDia(Funcoes.FormatarData(primeiroDiaMes));
			ps.adicionarDateTimeUltimaHoraDia(Funcoes.FormatarData(ultimoDiaMes));
			Sql += " and mj.assistencia = ? "; ps.adicionarLong(MandadoJudicialDt.SIM_ASSISTENCIA);
			Sql += " AND mj.id_mand_jud_pagamento_status = ? "; ps.adicionarLong(idStatusOrdem);
			Sql += " and MJ.DATA_RETORNO is not null ";
			Sql += " and MJ.id_usu_serv_1 in ( ";
			Sql += " SELECT distinct id_usu_serv_1 as oficial ";
			Sql += " FROM PROJUDI.MAND_JUD MJ ";
			Sql += " where mj.data_dist between ? and ? ";
			ps.adicionarDateTimePrimeiraHoraDia(Funcoes.FormatarData(primeiroDiaMes));
			ps.adicionarDateTimeUltimaHoraDia(Funcoes.FormatarData(ultimoDiaMes));
			Sql += " and mj.assistencia = ? "; ps.adicionarLong(MandadoJudicialDt.SIM_ASSISTENCIA);
			Sql += " group by mj.id_usu_serv_1 ";
			Sql += " having count(mj.id_mand_jud) > ? "; 			ps.adicionarLong(MandadoJudicialDt.ANALISAR_GRATUITO_DEPOIS_DE);
			Sql += " ) order by id_usu_serv_1, mj.id_mand_jud ";  
			Sql += " ) ";
			Sql += " where idx > ? ";								ps.adicionarLong(MandadoJudicialDt.ANALISAR_GRATUITO_DEPOIS_DE);
			
 
			rs = consultar(Sql, ps);
			while (rs.next()) {
				dados = new MandadoJudicialDt();
				dados.setId(rs.getString("idMandado"));
				dados.setMandadoTipo(rs.getString("mandTipo"));
				dados.setProcNumero(rs.getString("procNumero"));
				dados.setNomeUsuarioServentia_1(rs.getString("nomeUsuario"));	
				dados.setNomeUsuarioServentia_2(rs.getString("nomeOficialCompanheiro"));	
				dados.setMandJudPagamentoStatus(rs.getString("mandJudPagamentoStatus"));
				dados.setIdMandJudPagamentoStatus(rs.getString("idMandJudPagamentoStatus"));
				dados.setDataRetorno(Funcoes.FormatarData(rs.getDate("dataRetorno")));
				tempList.add(dados);
			}			
			
		}
		finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }		
		return tempList; 
	}
	
	public List<MandadoJudicialDt> consultaMandadosGratuitosAdicionalAnalisado(String idServentiaSessao, String dataReferencia) throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		List<MandadoJudicialDt> tempList = new ArrayList<MandadoJudicialDt>();
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps;
		try {
			ps = new PreparedStatementTJGO();
			
			String mes = dataReferencia.substring(3, 5);
			String ano = dataReferencia.substring(6, 10);
			Date primeiroDiaMes = Funcoes.getPrimeiroDiaMes(Funcoes.StringToInt(mes), Funcoes.StringToInt(ano));
			Date ultimoDiaMes = Funcoes.getUltimoDiaMes(mes, ano);
			
			
			Sql = "SELECT mj.id_mand_jud AS idMandado, mj.mand_tipo AS mandTipo, mj.proc_numero AS procNumero,"		
					+ " mj.mand_jud_pagamento_status AS mandJudPagamentoStatus, mj.nome_usu_serv_1 AS nomeUsuario,"
					+ " mj.nome_usu_serv_2 AS nomeOficialCompanheiro, mj.data_retorno AS dataRetorno,"
					+ " mj.id_mand_jud_pagamento_status as idMandJudPagamentoStatus";
			
			Sql += " FROM PROJUDI.VIEW_MAND_JUD MJ ";
			Sql += " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1 "; 
			Sql += " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv "; 
			Sql += " where mj.data_dist between ? and ? ";
			ps.adicionarDateTimePrimeiraHoraDia(Funcoes.FormatarData(primeiroDiaMes));
			ps.adicionarDateTimeUltimaHoraDia(Funcoes.FormatarData(ultimoDiaMes));
			Sql += " and mj.assistencia = ? "; ps.adicionarLong(MandadoJudicialDt.SIM_ASSISTENCIA);
			Sql += " AND mj.id_mand_jud_pagamento_status = ? "; ps.adicionarLong(MandadoJudicialDt.ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO);
			Sql += " and MJ.DATA_RETORNO is not null ";
			Sql += " order by MJ.data_pagamento_status, MJ.id_mand_jud desc ";
			
 
			rs = consultar(Sql, ps);
			while (rs.next()) {
				dados = new MandadoJudicialDt();
				dados.setId(rs.getString("idMandado"));
				dados.setMandadoTipo(rs.getString("mandTipo"));
				dados.setProcNumero(rs.getString("procNumero"));
				dados.setNomeUsuarioServentia_1(rs.getString("nomeUsuario"));	
				dados.setNomeUsuarioServentia_2(rs.getString("nomeOficialCompanheiro"));	
				dados.setMandJudPagamentoStatus(rs.getString("mandJudPagamentoStatus"));
				dados.setIdMandJudPagamentoStatus(rs.getString("idMandJudPagamentoStatus"));
				dados.setDataRetorno(Funcoes.FormatarData(rs.getDate("dataRetorno")));
				tempList.add(dados);
			}			
			
		}
		finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }		
		return tempList; 
	}
	
	/**
	 * Consulta que retorna os mandados que serão mostrados na tela de análise de pagamento do valor adicional para mandados gratuitos.
	 * Por regra, os primeiros 75 mandados gratuitos do mês, do oficial, não precisam passar por análise porque não receberão adicional.
	 * @param idServentiaSessao
	 * @param dataReferencia
	 * @param idStatusOrdem
	 * @return
	 * @throws Exception
	 */
	public List consultaMandadosAnaliseAdicionalGratuito(String idServentiaSessao, String dataReferencia, String idStatusOrdem) throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		List<MandadoJudicialDt> tempList = new ArrayList();
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps;
		try {
			ps = new PreparedStatementTJGO();
			Sql = "SELECT mj.id_mand_jud AS idMandado, mj.mand_tipo AS mandTipo, mj.proc_numero AS procNumero,"		
				+ " mj.mand_jud_pagamento_status AS mandJudPagamentoStatus, mj.nome_usu_serv_1 AS nomeUsuario,"
				+ " mj.nome_usu_serv_2 AS nomeOficialCompanheiro, mj.data_retorno AS dataRetorno,"
				+ " mj.id_mand_jud_pagamento_status as idMandJudPagamentoStatus"
				+ " FROM projudi.view_mand_jud mj"
				+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1" 
				+ " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv" 
				+ "	WHERE  mj.assistencia = ? AND mj.data_retorno BETWEEN ? AND ?";
			
			ps.adicionarLong(MandadoJudicialDt.SIM_ASSISTENCIA);	
			
			ps.adicionarDateTimePrimeiraHoraDia(Funcoes.somaData(dataReferencia, -60));  //  apenas ate 60 dias antes da data informada
			
			ps.adicionarDateTimeUltimaHoraDia(dataReferencia);
			 
			if(idStatusOrdem != null && !idStatusOrdem.isEmpty()) {
			    Sql += " AND mj.id_mand_jud_pagamento_status = ? ";
				ps.adicionarString(idStatusOrdem);
			}
			 
			if(idServentiaSessao != null && !idServentiaSessao.isEmpty()) {
			    Sql += " AND us.id_serv = ? ";
				ps.adicionarString(idServentiaSessao);
			}
			
			Sql += "ORDER BY nomeUsuario, TO_CHAR(dataRetorno, 'yyyymmdd') ";
 
			rs = consultar(Sql, ps);
			while (rs.next()) {
				dados = new MandadoJudicialDt();
				dados.setId(rs.getString("idMandado"));
				dados.setMandadoTipo(rs.getString("mandTipo"));
				dados.setProcNumero(rs.getString("procNumero"));
				dados.setNomeUsuarioServentia_1(rs.getString("nomeUsuario"));	
				dados.setNomeUsuarioServentia_2(rs.getString("nomeOficialCompanheiro"));	
				dados.setMandJudPagamentoStatus(rs.getString("mandJudPagamentoStatus"));
				dados.setIdMandJudPagamentoStatus(rs.getString("idMandJudPagamentoStatus"));
				dados.setDataRetorno(Funcoes.FormatarData(rs.getDate("dataRetorno")));
				tempList.add(dados);
			}			
			
		}
		finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }		
		return tempList; 
	}
	
	public List<MandadoJudicialDt> consultaMandJudPagamentoStatus() throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		List<MandadoJudicialDt> tempList = new ArrayList<MandadoJudicialDt>();
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps;
		try {
			ps = new PreparedStatementTJGO();
			Sql = "SELECT ps.mand_jud_pagamento_status as mandJudPagamentoStatus, ps.id_mand_jud_pagamento_status as idMandJudPagamentoStatus FROM projudi.mand_jud_pagamento_status ps";
			rs = consultar(Sql, ps);
			while (rs.next()) {
				dados = new MandadoJudicialDt();
				dados.setIdMandJudPagamentoStatus(rs.getString("idMandJudPagamentoStatus"));
				dados.setMandJudPagamentoStatus(rs.getString("mandJudPagamentoStatus"));
				tempList.add(dados);
			}			
			
		} finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }		
		return tempList; 
	}
	
	public List<MandadoJudicialDt> consultaMandJudPagamentoStatusTelaAnaliseGratuitos() throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		List<MandadoJudicialDt> tempList = new ArrayList<MandadoJudicialDt>();
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps;
		try {
			ps = new PreparedStatementTJGO();
			Sql = "SELECT ps.mand_jud_pagamento_status as mandJudPagamentoStatus, ps.id_mand_jud_pagamento_status as idMandJudPagamentoStatus FROM projudi.mand_jud_pagamento_status ps";
			Sql += " WHERE ID_MAND_JUD_PAGAMENTO_STATUS IN (?,?)";
			ps.adicionarLong(MandadoJudicialDt.ID_PAGAMENTO_PENDENTE);
			ps.adicionarLong(MandadoJudicialDt.ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO);
			rs = consultar(Sql, ps);
			while (rs.next()) {
				dados = new MandadoJudicialDt();
				dados.setIdMandJudPagamentoStatus(rs.getString("idMandJudPagamentoStatus"));
				dados.setMandJudPagamentoStatus(rs.getString("mandJudPagamentoStatus"));
				tempList.add(dados);
			}			
			
		} finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }		
		return tempList; 
	}
	
	public void alteraPagamentoStatus(MandadoJudicialDt mandadoJudicialDt, String dataAtual) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE projudi.mand_jud SET id_mand_jud_pagamento_status = ?,"
				+ " id_usu_pagamento_status = ?,"
		    	+ " data_pagamento_status = ?"
				+ " WHERE id_mand_jud  = ?";
	
		ps.adicionarString(mandadoJudicialDt.getIdMandJudPagamentoStatus());
		ps.adicionarString(mandadoJudicialDt.getIdUsuPagamentoStatus());
    	ps.adicionarDateTime(dataAtual);
		ps.adicionarString(mandadoJudicialDt.getId());
		
		this.executarUpdateDelete(sql, ps);		
	}
    
	public void alteraPagamentoStatus(String dataInicial, String dataFinal, String idUsuario) throws Exception {
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE projudi.mand_jud SET id_mand_jud_pagamento_status = ?,"
				+ " id_usu_pagamento_envio = ?,"
		    	+ " data_pagamento_envio = ?"
				+ " WHERE data_pagamento_status BETWEEN ? AND ? AND id_mand_jud_pagamento_status = ? AND assistencia = ?";	
		ps.adicionarLong (MandadoJudicialDt.ID_PAGAMENTO_ENVIADO);
		ps.adicionarLong(idUsuario);
    	ps.adicionarDateTime(Funcoes.FormatarData(new Date()));
    	ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
    	ps.adicionarDateTimeUltimaHoraDia(dataFinal);
    	ps.adicionarString(MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO);
    	ps.adicionarLong(MandadoJudicialDt.NAO_ASSISTENCIA);		
		this.executarUpdateDelete(sql, ps);			
	}
	
	public MandadoJudicialDt calculaValorLocomocao(String idMandJud) throws Exception {
		String Sql;
		MandadoJudicialDt dados = null;
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps;
		try {
			ps = new PreparedStatementTJGO();		
			
			Sql = "SELECT"
					+ " (SELECT COUNT(l.id_mand_jud) FROM projudi.locomocao l"
					+ " WHERE l.id_mand_jud = mj.id_mand_jud  AND l.id_guia_emis_comp IS NULL AND l.codigo_oficial_spg is null) AS quantidade,"
					+ " (SELECT SUM(gi.valor_calculado) FROM projudi.locomocao l"
					+ " INNER JOIN projudi.guia_item gi ON gi.id_guia_item = l.id_guia_item"
					+ " WHERE l.id_mand_jud = mj.id_mand_jud AND l.codigo_oficial_spg is null) AS VALOR"
					+ " FROM projudi.mand_jud mj where mj.id_mand_jud = ?";		
			
			ps.adicionarLong(idMandJud);			
			rs = consultar(Sql, ps);
			while (rs.next()) {		    
			  dados = new MandadoJudicialDt();
			  dados.setQuantidadeLocomocao(rs.getString("quantidade"));
			  dados.setValorLocomocao(rs.getString("valor"));	 
			}
						
		} finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }		
		return dados; 
	}
	
	/**
	 * Recebe o id de um mandado judicial e retorna a quantidade de itens de locomoção vinculados.
	 * @param idMandJud
	 * @return
	 * @throws Exception
	 */
	public int retornaQtdLocomocaoVinculada(String idMandJud) throws Exception {
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps;
		int qtd = 0;
		
		try {
			ps = new PreparedStatementTJGO();
			sql =  "SELECT COUNT(1) AS QTD FROM GUIA_ITEM GI ";
			sql += "INNER JOIN LOCOMOCAO LOC ON LOC.ID_GUIA_ITEM = GI.ID_GUIA_ITEM ";
			sql += "INNER JOIN GUIA_EMIS GE ON GE.ID_GUIA_EMIS =  GI.ID_GUIA_EMIS ";
			sql += "WHERE LOC.ID_MAND_JUD = ? ";
			ps.adicionarLong(idMandJud);
			
			rs = consultar(sql, ps);
			if (rs.next()) {		    
			  qtd = rs.getInt("QTD");
			}
			else {
				throw new MensagemException("Não foi possível identificar a quantidade de itens de locomoção vinculados a este mandado.");
			}
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {};
		}
		
		return qtd;
	}
	
	/**
	 * Consultar id do mandado judicial por Id da Pendência.
	 * @param String idPendencia
	 * @return String idMandado
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultaIdMandadoPorIdPendencia(String idPendencia) throws Exception {
		String Sql;
		MandadoJudicialDt dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String idMandado = null;

		Sql= "SELECT ID_MAND_JUD FROM projudi.MAND_JUD WHERE ID_PEND = ?";
		ps.adicionarLong(idPendencia);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				idMandado = rs1.getString("ID_MAND_JUD");
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return idMandado; 
	}
	
	public boolean alterarStatusMandadoJudicial(MandadoJudicialDt mandadoJudicialDt) throws Exception {
		boolean retorno = false;
		StringBuffer sql = new StringBuffer("UPDATE projudi.MAND_JUD SET ");
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		sql.append(" ID_MAND_JUD_STATUS = ?");
		ps.adicionarLong(mandadoJudicialDt.getId_MandadoJudicialStatus());
		
		if(Funcoes.StringToInt(mandadoJudicialDt.getResolutivo()) != 0){
			sql.append(", RESOLUTIVO = ?");
			ps.adicionarLong(mandadoJudicialDt.getResolutivo());
		}
		
		sql.append(" WHERE ID_MAND_JUD  = ?");
		ps.adicionarLong(mandadoJudicialDt.getId());
		
		this.executarUpdateDelete(sql.toString(), ps);
		
		retorno = true;
		
		return retorno;
	}
	
	/**
	 * Retorna o próximo número de mandado (id da tabela mand_jud) do sequence.
	 * @return String
	 * @throws Exception
	 */
	public String reservarNumeroProximoMandado() throws Exception {
		ResultSetTJGO rs = null;
		String proxNumeroMandado = null;
		String sql = "SELECT  MandadoJudicial_Id_MandadoJudi.NEXTVAL AS PROX_NUMERO FROM DUAL";
		
		try{
			rs = consultarSemParametros(sql);
			if (rs.next()) {
				proxNumeroMandado = rs.getString("PROX_NUMERO");
			}
		}finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }
		
		return proxNumeroMandado;
	}
	
	public String consultaIdCompanheiroPorIdMandado(String idMandJud) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String idCompanheiro = null;

		Sql= "SELECT ID_USU_SERV_2 FROM projudi.MAND_JUD WHERE ID_MAND_JUD = ?";
		ps.adicionarLong(idMandJud);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				idCompanheiro = rs1.getString("ID_USU_SERV_2");
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return idCompanheiro; 
	}
	
	/**
	 * Retorna a quantidade de mandados em aberto com oficial, reservados para ele.
	 * 
	 * @param idUsuServOficial
	 * @return
	 * @author hrrosa
	 */
	public long retornaQtdMandadosAbertosReservadosOficial(String idUsuServOficial) throws Exception {
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		long qtd = 0;

		sql =  " SELECT COUNT(1) QTD FROM MAND_JUD MJ ";
		sql += " INNER JOIN MAND_JUD_STATUS MJS ON MJ.ID_MAND_JUD_STATUS = MJS.ID_MAND_JUD_STATUS ";
		sql += " WHERE MJ.ID_USU_SERV_1 = ? "; ps.adicionarLong(idUsuServOficial); 
		sql += " AND MJS.MAND_JUD_STATUS_CODIGO IN (?,?) ";
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
		ps.adicionarLong(MandadoJudicialStatusDt.REDISTRIBUIDO);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				qtd = rs1.getLong("QTD");
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {throw e1;}
        } 
		
		return qtd; 
	}

	public MandadoJudicialDt consultaMandado(String idMandJud, int idServentia, int idUsuarioServentia)  throws Exception {

		StringBuffer sql = new StringBuffer();
		MandadoJudicialDt dados = new MandadoJudicialDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("SELECT mj.id_mand_jud AS idMandJud, p.proc_numero || '-' || p.digito_verificador AS numrProcesso, m.modelo AS modelo,"
				+ " mj.mand_tipo AS mandTipo, mj.nome_usu_serv_1 AS nomeUsuServ1, mj.mand_jud_status AS mandJudStatus,"
				+ " mj.nome_usu_serv_2 AS nomeUsuServ2, CASE WHEN mj.assistencia = 0 THEN 'COM CUSTAS' ELSE 'SEM CUSTAS' END AS assistencia,"
				+ " s.serv AS nomeServ, TO_CHAR(mj.data_dist,'dd/mm/yyyy') AS dataDist, TO_CHAR(mj.data_limite,'dd/mm/yyyy') AS dataLimite,"
				+ " TO_CHAR(mj.data_retorno,'dd/mm/yyyy') AS dataRetorno, mj.comarca AS comarca, mj.bairro AS bairro,"
				+ " mj.zona AS zona, mj.regiao AS regiao, mj.nome_usu_pagamento_status AS nomePagamentoStatus,"
				+ " TO_CHAR(mj.data_pagamento_status,'dd/mm/yyyy') AS dataPagamentoStatus, mj.id_pend AS idPend,"
				+ " mj.nome_usu_pagamento_envio AS nomePagamentoEnvio,  TO_CHAR(mj.data_pagamento_envio,'dd/mm/yyyy') AS dataPagamentoEnvio,"
				+ " e.id_esc AS idEscala, e.esc AS escala, mjps.mand_jud_pagamento_status AS mandJudPagamentoStatus"
				+ " FROM view_mand_jud  mj"
				+ " LEFT JOIN projudi.modelo m ON m.id_modelo = mj.id_modelo"
				+ " INNER JOIN projudi.proc p ON p.id_proc = mj.id_proc"
				+ " INNER JOIN projudi.serv s ON s.id_serv = p.id_serv"
				+ "	INNER JOIN projudi.esc e ON e.id_esc = mj.id_esc"
				+ " INNER JOIN projudi.mand_jud_pagamento_status mjps ON  mjps.id_mand_jud_pagamento_status = mj.id_mand_jud_pagamento_status"
				+ " WHERE mj.id_mand_jud = ?");			
		
		ps.adicionarLong(idMandJud);
		
		if (idUsuarioServentia != 0) {
			sql.append(" AND mj.id_usu_serv_1 = ?");
			ps.adicionarLong(idUsuarioServentia);
		}

		if (idServentia != 0) {
			sql.append(" AND mj.id_serv = ?");
			ps.adicionarLong(idServentia);
		}
		
		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				dados.setId(rs1.getString("idMandJud")); 
				dados.setProcNumero(rs1.getString("numrProcesso")); 
 				dados.setMandadoTipo(rs1.getString("mandTipo"));
 				dados.setNomeUsuarioServentia_1(rs1.getString("nomeUsuServ1"));
 				dados.setMandadoJudicialStatus(rs1.getString("mandJudStatus"));
 				dados.setNomeUsuarioServentia_2(rs1.getString("nomeUsuServ2"));
 				dados.setAssistencia(rs1.getString("assistencia"));
 				dados.setNomeServentia(rs1.getString("nomeServ"));
 				dados.setDataDistribuicao(rs1.getString("dataDist"));
 				dados.setDataLimite(rs1.getString("dataLimite"));
 				dados.setDataRetorno(rs1.getString("dataRetorno"));
 				dados.setComarca(rs1.getString("comarca"));
 				dados.setBairro(rs1.getString("bairro"));
 				dados.setZona(rs1.getString("zona"));
 				dados.setRegiao(rs1.getString("regiao"));
 				dados.setNomeUsuPagamentoStatus(rs1.getString("nomePagamentoStatus"));
 				dados.setDataPagamentoStatus(rs1.getString("dataPagamentoStatus"));
 				dados.setNomeUsuPagamentoEnvio(rs1.getString("nomePagamentoEnvio"));
 				dados.setDataPagamentoEnvio(rs1.getString("dataPagamentoEnvio"));
 				dados.setEscala(rs1.getString("idEscala") + " - " + rs1.getString("escala"));
 				dados.setMandJudPagamentoStatus(rs1.getString("mandJudPagamentoStatus"));	
 				dados.setId_Pendencia(rs1.getString("idPend"));
 				dados.setModelo(rs1.getString("modelo"));
			}
			
		} 
				
		finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return dados; 
	}
	    
}
