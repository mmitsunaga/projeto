package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoParteAlcunhaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6418862244853906602L;

	//---------------------------------------------------------
	public ProcessoParteAlcunhaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteAlcunhaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteAlcunhainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_ALCUNHA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_Alcunha().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ALCUNHA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Alcunha());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE_ALCUNHA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteAlcunhaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoParteAlcunhaalterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE_ALCUNHA SET  ";
		stSql+= "ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_ALCUNHA = ?";		 ps.adicionarLong(dados.getId_Alcunha());  

		stSql += " WHERE ID_PROC_PARTE_ALCUNHA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteAlcunhaexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_ALCUNHA";
		stSql += " WHERE ID_PROC_PARTE_ALCUNHA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteAlcunhaDt consultarId(String id_processopartealcunha )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteAlcunhaDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoParteAlcunha)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_ALCUNHA WHERE ID_PROC_PARTE_ALCUNHA = ?";		ps.adicionarLong(id_processopartealcunha); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoParteAlcunha  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoParteAlcunhaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoParteAlcunhaDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_PARTE_ALCUNHA"));
		Dados.setAlcunha(rs.getString("ALCUNHA"));
		Dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));
		Dados.setNome( rs.getString("NOME"));
		Dados.setId_Alcunha( rs.getString("ID_ALCUNHA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoParteAlcunha()");

		stSql= "SELECT ID_PROC_PARTE_ALCUNHA, ALCUNHA FROM PROJUDI.VIEW_PROC_PARTE_ALCUNHA WHERE ALCUNHA LIKE ?";
		stSql+= " ORDER BY ALCUNHA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoParteAlcunha  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoParteAlcunhaDt obTemp = new ProcessoParteAlcunhaDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_ALCUNHA"));
				obTemp.setAlcunha(rs1.getString("ALCUNHA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_ALCUNHA WHERE ALCUNHA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoParteAlcunhaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
