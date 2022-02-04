package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaSubtipoAssuntoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 4185553287975134378L;

	//---------------------------------------------------------
	public ServentiaSubtipoAssuntoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ServentiaSubtipoAssuntoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaSubtipoAssuntoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.SERV_SUBTIPO_ASSUNTO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ServentiaSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

			stVirgula=",";
		}
		if ((dados.getId_Assunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Assunto());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_SERV_SUBTIPO_ASSUNTO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaSubtipoAssuntoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psServentiaSubtipoAssuntoalterar()");

		stSql= "UPDATE PROJUDI.SERV_SUBTIPO_ASSUNTO SET  ";
		stSql+= "ID_SERV_SUBTIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

		stSql+= ",ID_ASSUNTO = ?";		 ps.adicionarLong(dados.getId_Assunto());  

		stSql += " WHERE ID_SERV_SUBTIPO_ASSUNTO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaSubtipoAssuntoexcluir()");

		stSql= "DELETE FROM PROJUDI.SERV_SUBTIPO_ASSUNTO";
		stSql += " WHERE ID_SERV_SUBTIPO_ASSUNTO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaSubtipoAssuntoDt consultarId(String id_serventiasubtipoassunto )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaSubtipoAssuntoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ServentiaSubtipoAssunto)");

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_SUBTIPO_ASSUNTO WHERE ID_SERV_SUBTIPO_ASSUNTO = ?";		ps.adicionarLong(id_serventiasubtipoassunto); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ServentiaSubtipoAssunto  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaSubtipoAssuntoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaSubtipoAssuntoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_SERV_SUBTIPO_ASSUNTO"));
		Dados.setServentiaSubtipo(rs.getString("SERV_SUBTIPO"));
		Dados.setId_ServentiaSubtipo( rs.getString("ID_SERV_SUBTIPO"));
		Dados.setId_Assunto( rs.getString("ID_ASSUNTO"));
		Dados.setAssunto( rs.getString("ASSUNTO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setServentiaSubtipoCodigo( rs.getString("SERV_SUBTIPO_CODIGO"));
		Dados.setAssuntoCodigo( rs.getString("ASSUNTO_CODIGO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServentiaSubtipoAssunto()");

		stSql= "SELECT ID_SERV_SUBTIPO_ASSUNTO, SERV_SUBTIPO FROM PROJUDI.VIEW_SERV_SUBTIPO_ASSUNTO WHERE SERV_SUBTIPO LIKE ?";
		stSql+= " ORDER BY SERV_SUBTIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoServentiaSubtipoAssunto  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaSubtipoAssuntoDt obTemp = new ServentiaSubtipoAssuntoDt();
				obTemp.setId(rs1.getString("ID_SERV_SUBTIPO_ASSUNTO"));
				obTemp.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_SUBTIPO_ASSUNTO WHERE SERV_SUBTIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ServentiaSubtipoAssuntoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
