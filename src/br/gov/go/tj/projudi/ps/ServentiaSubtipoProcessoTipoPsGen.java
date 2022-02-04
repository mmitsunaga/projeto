package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoProcessoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaSubtipoProcessoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3448527393668915302L;

	//---------------------------------------------------------
	public ServentiaSubtipoProcessoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ServentiaSubtipoProcessoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaSubtipoProcessoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.SERV_SUBTIPO_PROC_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ServentiaSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_SERV_SUBTIPO_PROC_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaSubtipoProcessoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psServentiaSubtipoProcessoTipoalterar()");

		stSql= "UPDATE PROJUDI.SERV_SUBTIPO_PROC_TIPO SET  ";
		stSql+= "ID_SERV_SUBTIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

		stSql+= ",ID_PROC_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoTipo());  

		stSql += " WHERE ID_SERV_SUBTIPO_PROC_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaSubtipoProcessoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.SERV_SUBTIPO_PROC_TIPO";
		stSql += " WHERE ID_SERV_SUBTIPO_PROC_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaSubtipoProcessoTipoDt consultarId(String id_serventiasubtipoprocessotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaSubtipoProcessoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ServentiaSubtipoProcessoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO WHERE ID_SERV_SUBTIPO_PROC_TIPO = ?";		ps.adicionarLong(id_serventiasubtipoprocessotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ServentiaSubtipoProcessoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaSubtipoProcessoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaSubtipoProcessoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_SERV_SUBTIPO_PROC_TIPO"));
		Dados.setServentiaSubtipo(rs.getString("SERV_SUBTIPO"));
		Dados.setId_ServentiaSubtipo( rs.getString("ID_SERV_SUBTIPO"));
		Dados.setId_ProcessoTipo( rs.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo( rs.getString("PROC_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setServentiaSubtipoCodigo( rs.getString("SERV_SUBTIPO_CODIGO"));
		Dados.setProcessoTipoCodigo( rs.getString("PROC_TIPO_CODIGO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServentiaSubtipoProcessoTipo()");

		stSql= "SELECT ID_SERV_SUBTIPO_PROC_TIPO, SERV_SUBTIPO FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO WHERE SERV_SUBTIPO LIKE ?";
		stSql+= " ORDER BY SERV_SUBTIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoServentiaSubtipoProcessoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaSubtipoProcessoTipoDt obTemp = new ServentiaSubtipoProcessoTipoDt();
				obTemp.setId(rs1.getString("ID_SERV_SUBTIPO_PROC_TIPO"));
				obTemp.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_SUBTIPO_PROC_TIPO WHERE SERV_SUBTIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ServentiaSubtipoProcessoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
