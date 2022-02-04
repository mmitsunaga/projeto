package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RecursoPartePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 1979230830082254582L;

	//---------------------------------------------------------
	public RecursoPartePsGen() {

	}



//---------------------------------------------------------
	public void inserir(RecursoParteDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRecursoParteinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.RECURSO_PARTE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Recurso().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_RECURSO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Recurso());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoParteTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParteTipo());  

			stVirgula=",";
		}
		if ((dados.getDataBaixa().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_BAIXA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataBaixa());  

			stVirgula=",";
		}		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_RECURSO_PARTE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(RecursoParteDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psRecursoPartealterar()");

		stSql= "UPDATE PROJUDI.RECURSO_PARTE SET  ";
		stSql+= "ID_RECURSO = ?";		 ps.adicionarLong(dados.getId_Recurso());  

		stSql+= ",ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_PROC_PARTE_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoParteTipo());  

		stSql+= ",DATA_BAIXA = ?";		 ps.adicionarDateTime(dados.getDataBaixa());  

		stSql += " WHERE ID_RECURSO_PARTE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRecursoParteexcluir()");

		stSql= "DELETE FROM PROJUDI.RECURSO_PARTE";
		stSql += " WHERE ID_RECURSO_PARTE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public RecursoParteDt consultarId(String id_recursoparte )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RecursoParteDt Dados=null;
		////System.out.println("....ps-ConsultaId_RecursoParte)");

		stSql= "SELECT * FROM PROJUDI.VIEW_RECURSO_PARTE WHERE ID_RECURSO_PARTE = ?";		ps.adicionarLong(id_recursoparte); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_RecursoParte  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RecursoParteDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( RecursoParteDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_RECURSO_PARTE"));
		Dados.setProcessoParteTipo(rs.getString("PROC_PARTE_TIPO"));
		Dados.setId_Recurso( rs.getString("ID_RECURSO"));
		Dados.setDataEnvio( Funcoes.FormatarDataHora(rs.getDateTime("DATA_ENVIO")));
		Dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));
		Dados.setNome( rs.getString("NOME"));
		Dados.setId_ProcessoParteTipo( rs.getString("ID_PROC_PARTE_TIPO"));
		Dados.setDataBaixa( Funcoes.FormatarDataHora(rs.getDateTime("DATA_BAIXA")));
		Dados.setId_ProcessoTipo(rs.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo(rs.getString("PROC_TIPO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoRecursoParte()");

		stSql= "SELECT ID_RECURSO_PARTE, PROC_PARTE_TIPO, ID_PROC_TIPO, PROC_TIPO FROM PROJUDI.VIEW_RECURSO_PARTE WHERE PROC_PARTE_TIPO LIKE ?";
		stSql+= " ORDER BY PROC_PARTE_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoRecursoParte  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				RecursoParteDt obTemp = new RecursoParteDt();
				obTemp.setId(rs1.getString("ID_RECURSO_PARTE"));
				obTemp.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				obTemp.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				obTemp.setProcessoTipo(rs1.getString("PROC_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_RECURSO_PARTE WHERE PROC_PARTE_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..RecursoPartePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
