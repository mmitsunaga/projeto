package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PrazoSuspensoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PrazoSuspensoPsGen extends Persistencia {

	private static final long serialVersionUID = 7447506729056418383L;

	public PrazoSuspensoPsGen() {

	}

	public void inserir(PrazoSuspensoDt dados ) throws Exception{
		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.PRAZO_SUSPENSO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMotivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOTIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMotivo());  

			stVirgula=",";
		}
		if ((dados.getId_PrazoSuspensoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PRAZO_SUSPENSO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PrazoSuspensoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getId_Cidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Cidade());  

			stVirgula=",";
		}
		if ((dados.getData().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getData());  

			stVirgula=",";
		}
		if ((dados.getPlantaoLiberado().length()>0)) {
			 stSqlCampos+=   stVirgula + "PLANTAO_LIBERADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPlantaoLiberado());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_PRAZO_SUSPENSO",ps));
	} 

	
	public void alterar(PrazoSuspensoDt dados) throws Exception{
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PRAZO_SUSPENSO SET  ";
		stSql+= "MOTIVO = ?";		 ps.adicionarString(dados.getMotivo());  
		stSql+= ",ID_PRAZO_SUSPENSO_TIPO = ?";		 ps.adicionarLong(dados.getId_PrazoSuspensoTipo());  
		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  
		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  
		stSql+= ",ID_CIDADE = ?";		 ps.adicionarLong(dados.getId_Cidade());  
		stSql+= ",DATA = ?";		 ps.adicionarDate(dados.getData());  
		stSql+= ",PLANTAO_LIBERADO = ?";		 ps.adicionarLong(dados.getPlantaoLiberado()); 
		stSql += " WHERE ID_PRAZO_SUSPENSO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

	
	public void excluir( String chave) throws Exception{
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSql= "DELETE FROM PROJUDI.PRAZO_SUSPENSO";
		stSql += " WHERE ID_PRAZO_SUSPENSO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

	public PrazoSuspensoDt consultarId(String id_prazosuspenso )  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PrazoSuspensoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PRAZO_SUSPENSO WHERE ID_PRAZO_SUSPENSO = ?";		ps.adicionarLong(id_prazosuspenso); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PrazoSuspensoDt();
				associarDt(Dados, rs1);
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( PrazoSuspensoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PRAZO_SUSPENSO"));
		Dados.setPrazoSuspensoTipo(rs.getString("PRAZO_SUSPENSO_TIPO"));
		Dados.setMotivo( rs.getString("MOTIVO"));
		Dados.setId_PrazoSuspensoTipo( rs.getString("ID_PRAZO_SUSPENSO_TIPO"));
		Dados.setId_Comarca( rs.getString("ID_COMARCA"));
		Dados.setComarca( rs.getString("COMARCA"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setId_Cidade( rs.getString("ID_CIDADE"));
		Dados.setCidade( rs.getString("CIDADE"));
		Dados.setData( Funcoes.FormatarData(rs.getDateTime("DATA")));
		Dados.setDataLancamento( Funcoes.FormatarData(rs.getDateTime("DATA_LANCAMENTO")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setPrazoSuspensoTipoCodigo( rs.getString("PRAZO_SUSPENSO_TIPO_CODIGO"));
		Dados.setComarcaCodigo( rs.getString("COMARCA_CODIGO"));
		Dados.setServentiaCodigo( rs.getString("SERV_CODIGO"));
		Dados.setCidadeCodigo( rs.getString("CIDADE_CODIGO"));
		Dados.setPlantaoLiberado( rs.getString("PLANTAO_LIBERADO"));
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception{
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_PRAZO_SUSPENSO, PRAZO_SUSPENSO_TIPO FROM PROJUDI.VIEW_PRAZO_SUSPENSO WHERE PRAZO_SUSPENSO_TIPO LIKE ?";
		stSql+= " ORDER BY PRAZO_SUSPENSO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			while (rs1.next()) {
				PrazoSuspensoDt obTemp = new PrazoSuspensoDt();
				obTemp.setId(rs1.getString("ID_PRAZO_SUSPENSO"));
				obTemp.setPrazoSuspensoTipo(rs1.getString("PRAZO_SUSPENSO_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PRAZO_SUSPENSO WHERE PRAZO_SUSPENSO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
