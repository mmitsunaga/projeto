package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ArrecadacaoCustaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8451532438468849180L;

	//---------------------------------------------------------
	public ArrecadacaoCustaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ArrecadacaoCustaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArrecadacaoCustainserir()");
		stSqlCampos= "INSERT INTO projudi.ARRECADACAO_CUSTA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getArrecadacaoCustaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ARRECADACAO_CUSTA_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getArrecadacaoCustaCodigo());  

			stVirgula=",";
		}
		if ((dados.getCodigoArrecadacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_ARRECADACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoArrecadacao());  

			stVirgula=",";
		}
		if ((dados.getArrecadacaoCusta().length()>0)) {
			 stSqlCampos+=   stVirgula + "ARRECADACAO_CUSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getArrecadacaoCusta());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ARRECADACAO_CUSTA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ArrecadacaoCustaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psArrecadacaoCustaalterar()");

		stSql= "UPDATE projudi.ARRECADACAO_CUSTA SET  ";
		stSql+= "ARRECADACAO_CUSTA_CODIGO = ?";		 ps.adicionarLong(dados.getArrecadacaoCustaCodigo());  

		stSql+= ",CODIGO_ARRECADACAO = ?";		 ps.adicionarLong(dados.getCodigoArrecadacao());  

		stSql+= ",ARRECADACAO_CUSTA = ?";		 ps.adicionarString(dados.getArrecadacaoCusta());  

		stSql += " WHERE ID_ARRECADACAO_CUSTA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArrecadacaoCustaexcluir()");

		stSql= "DELETE FROM projudi.ARRECADACAO_CUSTA";
		stSql += " WHERE ID_ARRECADACAO_CUSTA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ArrecadacaoCustaDt consultarId(String id_arrecadacaocusta )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ArrecadacaoCustaDt Dados=null;
		////System.out.println("....ps-ConsultaId_ArrecadacaoCusta)");

		stSql= "SELECT * FROM projudi.VIEW_ARRECADACAO_CUSTA WHERE ID_ARRECADACAO_CUSTA = ?";		ps.adicionarLong(id_arrecadacaocusta); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ArrecadacaoCusta  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ArrecadacaoCustaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ArrecadacaoCustaDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_ARRECADACAO_CUSTA"));
		Dados.setArrecadacaoCusta(rs.getString("ARRECADACAO_CUSTA"));
		Dados.setArrecadacaoCustaCodigo( rs.getString("ARRECADACAO_CUSTA_CODIGO"));
		Dados.setCodigoArrecadacao( rs.getString("CODIGO_ARRECADACAO"));
		//Dados.setCusta( rs.getString("CUSTA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
				
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoArrecadacaoCusta()");

		stSql= "SELECT ID_ARRECADACAO_CUSTA, ARRECADACAO_CUSTA FROM projudi.VIEW_ARRECADACAO_CUSTA WHERE ARRECADACAO_CUSTA LIKE ?";
		stSql+= " ORDER BY ARRECADACAO_CUSTA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoArrecadacaoCusta  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ArrecadacaoCustaDt obTemp = new ArrecadacaoCustaDt();
				obTemp.setId(rs1.getString("ID_ARRECADACAO_CUSTA"));
				obTemp.setArrecadacaoCusta(rs1.getString("ARRECADACAO_CUSTA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE FROM projudi.VIEW_ARRECADACAO_CUSTA WHERE ARRECADACAO_CUSTA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ArrecadacaoCustaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
