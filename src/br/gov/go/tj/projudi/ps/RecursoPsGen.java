package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RecursoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -357524509131592561L;

	//---------------------------------------------------------
	public RecursoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(RecursoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRecursoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.RECURSO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			 stVirgula=",";
		}				
		if ((dados.getId_ServentiaOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaOrigem());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaRecurso().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_RECURSO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaRecurso());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoTipo());  

			stVirgula=",";
		}
		if ((dados.getDataEnvio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_ENVIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEnvio());  

			stVirgula=",";
		}
		if ((dados.getDataRecebimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_RECEBIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataRecebimento());  

			stVirgula=",";
		}
		if ((dados.getDataRetorno().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_RETORNO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataRetorno());  

			stVirgula=",";
		}		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_RECURSO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(RecursoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psRecursoalterar()");

		stSql= "UPDATE PROJUDI.RECURSO SET  ";
		stSql+= "ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

		stSql+= ",ID_SERV_ORIGEM = ?";		 ps.adicionarLong(dados.getId_ServentiaOrigem());  

		stSql+= ",ID_SERV_RECURSO = ?";		 ps.adicionarLong(dados.getId_ServentiaRecurso());  

		stSql+= ",ID_PROC_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoTipo());  

		stSql+= ",DATA_ENVIO = ?";		 ps.adicionarDateTime(dados.getDataEnvio());  

		stSql+= ",DATA_RECEBIMENTO = ?";		 ps.adicionarDateTime(dados.getDataRecebimento());  

		stSql+= ",DATA_RETORNO = ?";		 ps.adicionarDateTime(dados.getDataRetorno());  

		stSql += " WHERE ID_RECURSO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psRecursoexcluir()");

		stSql= "DELETE FROM PROJUDI.RECURSO";
		stSql += " WHERE ID_RECURSO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public RecursoDt consultarId(String id_recurso )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RecursoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Recurso)");

		stSql= "SELECT * FROM PROJUDI.VIEW_RECURSO WHERE ID_RECURSO = ?";		ps.adicionarLong(id_recurso); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Recurso  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RecursoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( RecursoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_RECURSO"));
		Dados.setProcessoNumero(rs1.getString("PROC_NUMERO"));
		Dados.setId_Processo( rs1.getString("ID_PROC"));
		Dados.setId_ServentiaOrigem( rs1.getString("ID_SERV_ORIGEM"));
		Dados.setId_AreaDistribuicaoOrigem(rs1.getString("ID_AREA_DIST_ORIGEM"));
		Dados.setServentiaOrigem( rs1.getString("SERV_ORIGEM"));
		Dados.setId_ServentiaRecurso( rs1.getString("ID_SERV_RECURSO"));
		Dados.setServentiaRecurso( rs1.getString("SERV_RECURSO"));
		Dados.setId_ProcessoTipo( rs1.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo( rs1.getString("PROC_TIPO"));
		Dados.setDataEnvio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_ENVIO")));
		Dados.setDataRecebimento( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_RECEBIMENTO")));
		Dados.setDataRetorno( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_RETORNO")));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoRecurso()");

		stSql= "SELECT ID_RECURSO, PROC_NUMERO FROM PROJUDI.VIEW_RECURSO WHERE PROC_NUMERO LIKE ?";
		stSql+= " ORDER BY PROC_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoRecurso  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				RecursoDt obTemp = new RecursoDt();
				obTemp.setId(rs1.getString("ID_RECURSO"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_RECURSO WHERE PROC_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..RecursoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
