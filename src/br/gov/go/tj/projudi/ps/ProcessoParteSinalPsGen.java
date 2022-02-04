package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoParteSinalPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -1768149152402957746L;

	//---------------------------------------------------------
	public ProcessoParteSinalPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteSinalDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteSinalinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_SINAL ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_Sinal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SINAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Sinal());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE_SINAL",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteSinalDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoParteSinalalterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE_SINAL SET  ";
		stSql+= "ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_SINAL = ?";		 ps.adicionarLong(dados.getId_Sinal());  

		stSql += " WHERE ID_PROC_PARTE_SINAL  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteSinalexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_SINAL";
		stSql += " WHERE ID_PROC_PARTE_SINAL = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteSinalDt consultarId(String id_processopartesinal )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteSinalDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoParteSinal)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_SINAL WHERE ID_PROC_PARTE_SINAL = ?";		ps.adicionarLong(id_processopartesinal); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoParteSinal  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoParteSinalDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoParteSinalDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_PARTE_SINAL"));
		Dados.setSinal(rs.getString("SINAL"));
		Dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));
		Dados.setNome( rs.getString("NOME"));
		Dados.setId_Sinal( rs.getString("ID_SINAL"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoParteSinal()");

		stSql= "SELECT ID_PROC_PARTE_SINAL, SINAL FROM PROJUDI.VIEW_PROC_PARTE_SINAL WHERE SINAL LIKE ?";
		stSql+= " ORDER BY SINAL ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoParteSinal  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoParteSinalDt obTemp = new ProcessoParteSinalDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_SINAL"));
				obTemp.setSinal(rs1.getString("SINAL"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_SINAL WHERE SINAL LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoParteSinalPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
