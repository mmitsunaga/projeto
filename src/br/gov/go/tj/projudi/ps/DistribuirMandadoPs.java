package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class DistribuirMandadoPs extends Persistencia {
	
	private static final long serialVersionUID = 6600156986078036383L;

	public void inserir(MandadoJudicialDt dados ) throws Exception {
		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.MAND_JUD (";		 
		SqlValores = " Values (";
		if ((dados.getId_MandadoTipo().length()>0)){
			SqlCampos += ",ID_MAND_TIPO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_MandadoTipo());
		}
		if ((dados.getId_MandadoJudicialStatus().length()>0)){
			SqlCampos += ",ID_MAND_JUD_STATUS ";
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
			SqlCampos += ",ID_REGIAO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Regiao());
		}
		if ((dados.getId_Bairro().length()>0)){
			SqlCampos += ",ID_BAIRRO " ;
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
		if ((dados.getValor().length()>0)){
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
		if ( dados.getUsuarioServentiaDt_1().getId().length() > 0 ){
			SqlCampos += ",ID_USU_SERV_1";
			SqlValores += ",?";
			ps.adicionarLong(dados.getUsuarioServentiaDt_1().getId());
		}
		if( dados.getUsuarioServentiaDt_2() != null ) {
			if ( dados.getUsuarioServentiaDt_2().getId().length() > 0 ){
				SqlCampos += ",ID_USU_SERV_2";
				SqlValores += ",?";
				ps.adicionarLong(dados.getUsuarioServentiaDt_2().getId());
			}
		}
		if ( dados.getLocomocoesFrutiferas() != null ){
			SqlCampos += ",LOCOMOCOES_FRUTIFERAS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getLocomocoesFrutiferas());
		}
		if ( dados.getLocomocoesInfrutiferas() != null ){
			SqlCampos += ",LOCOMOCOES_INFRUTIFERAS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getLocomocoesInfrutiferas());
		}
		if ( dados.getLocomocaoHoraMarcada() != null ){
			SqlCampos += ",LOCOMOCAO_HORA_MARCADA";	
			SqlValores += ",?";
			ps.adicionarLong(dados.getLocomocaoHoraMarcada());
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
	public MandadoJudicialDt consultarId(MandadoJudicialDt mandadoJudicialDt)  throws Exception {

		String Sql;
		MandadoJudicialDt dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_MAND_JUD WHERE ID_MAND_JUD = ?";
		ps.adicionarLong(mandadoJudicialDt.getId());

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
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
			    }
			    else
			    	dados.setUsuarioServentiaDt_2(null);
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return dados; 
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

		Sql= "SELECT * FROM PROJUDI.VIEW_MAND_JUD WHERE ID_PEND = ?";
		ps.adicionarLong(idPendencia);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
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
			    }
			    else
			    	dados.setUsuarioServentiaDt_2(null);
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
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuffer sql = new StringBuffer("UPDATE PROJUDI.MAND_JUD SET ");
		
		sql.append(" ID_MAND_JUD_STATUS = ?");
		ps.adicionarLong(novoStatus );
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
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List listaMandados = null;
		String sql = "SELECT ID_MAND_JUD,ID_USU_SERV_1,ID_USU_SERV_2 FROM PROJUDI.MAND_JUD WHERE ? > DATA_LIMITE AND DATA_RETORNO IS NULL AND ID_MAND_JUD_STATUS = ?";
		
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
		StringBuffer sql = new StringBuffer("SELECT m.ID_MAND_JUD, m.ID_USU_SERV_1, m.ID_USU_SERV_2 FROM PROJUDI.MAND_JUD m WHERE ");
		sql.append(" (SELECT COUNT(mj.ID_MAND_JUD) FROM PROJUDI.MAND_JUD mj WHERE m.ID_MAND_JUD = mj.ID_MAND_JUD AND ? > mj.DATA_LIMITE AND mj.DATA_RETORNO IS NULL AND mj.ID_MAND_JUD_STATUS = ?) > ? ");
		sql.append(" AND ? > m.DATA_LIMITE ");
		sql.append(" AND m.DATA_RETORNO IS NOT NULL ");
		sql.append(" AND m.ID_MAND_JUD_STATUS <> ?");
		
		ps.adicionarDateTime(new Date());
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
		ps.adicionarLong(0);
		ps.adicionarDateTime(new Date());
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
		
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
		String sql = "UPDATE PROJUDI.MAND_JUD SET DATA_LIMITE = ? WHERE ID_MAND_JUD  = ?";
		ps.adicionarDateTime(mandadoJudicialDt.getDataLimite());
		ps.adicionarLong(mandadoJudicialDt.getId());
		
		this.executarUpdateDelete(sql, ps);
		
		retorno = true;
		
		return retorno;
	}
	
	public void alterar(MandadoJudicialDt dados) throws Exception{
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String Sql;

		Sql= "UPDATE PROJUDI.MAND_JUD SET  ";
		//Sql+= "MAND_JUD  = ?";
		//ps.adicionarString(dados.getMandadoJudicial()); 
		Sql+= "ID_MAND_TIPO  = ?";
		ps.adicionarLong(dados.getId_MandadoTipo()); 
		Sql+= ",ID_MAND_JUD_STATUS  = ?";
		ps.adicionarLong(dados.getId_MandadoJudicialStatus()); 
		Sql+= ",ID_AREA  = ?";
		ps.adicionarLong(dados.getId_Area()); 
		Sql+= ",ID_ZONA  = ?";
		ps.adicionarLong(dados.getId_Zona()); 
		Sql+= ",ID_REGIAO  = ?";
		ps.adicionarLong(dados.getId_Regiao()); 
		Sql+= ",ID_BAIRRO  = ?";
		ps.adicionarLong(dados.getId_Bairro()); 
		Sql+= ",ID_PROC_PARTE  = ?";
		ps.adicionarLong(dados.getId_ProcessoParte()); 
		Sql+= ",ID_ENDERECO_PARTE  = ?";
		ps.adicionarLong(dados.getId_EnderecoParte()); 
		Sql+= ",ID_PEND  = ?";
		ps.adicionarLong(dados.getId_Pendencia()); 
		Sql+= ",ID_ESC  = ?";
		ps.adicionarLong(dados.getId_Escala()); 
		Sql+= ",VALOR  = ?";
		ps.adicionarDecimal(dados.getValor()); 
		Sql+= ",ASSISTENCIA  = ?";
		ps.adicionarBoolean(dados.getAssistencia());	
		Sql = Sql + " WHERE ID_MAND_JUD  = ?";
		ps.adicionarLong(dados.getId());
		
		executarUpdateDelete(Sql, ps);
	} 
}
