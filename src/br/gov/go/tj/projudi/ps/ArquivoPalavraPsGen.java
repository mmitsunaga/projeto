package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoPalavraDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ArquivoPalavraPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 37042713151243928L;

	//---------------------------------------------------------
	public ArquivoPalavraPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ArquivoPalavraDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArquivoPalavrainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.ARQ_PALAVRA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Arquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Arquivo());  

			stVirgula=",";
		}
		if ((dados.getId_Palavra1().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PALAVRA_1 " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Palavra1());  

			stVirgula=",";
		}
		if ((dados.getId_Palavra2().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PALAVRA_2 " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Palavra2());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ARQ_PALAVRA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ArquivoPalavraDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psArquivoPalavraalterar()");

		stSql= "UPDATE PROJUDI.ARQ_PALAVRA SET  ";
		stSql+= "ID_ARQ = ?";		 ps.adicionarLong(dados.getId_Arquivo());  

		stSql+= ",ID_PALAVRA_1 = ?";		 ps.adicionarLong(dados.getId_Palavra1());  

		stSql+= ",ID_PALAVRA_2 = ?";		 ps.adicionarLong(dados.getId_Palavra2());  

		stSql += " WHERE ID_ARQ_PALAVRA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArquivoPalavraexcluir()");

		stSql= "DELETE FROM PROJUDI.ARQ_PALAVRA";
		stSql += " WHERE ID_ARQ_PALAVRA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ArquivoPalavraDt consultarId(String ID_ARQ_PALAVRA )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ArquivoPalavraDt Dados=null;
		////System.out.println("....ps-ConsultaId_ArquivoPalavra)");

		stSql= "SELECT * FROM PROJUDI.VIEW_ARQ_PALAVRA WHERE ID_ARQ_PALAVRA = ?";		ps.adicionarLong(ID_ARQ_PALAVRA); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ArquivoPalavra  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ArquivoPalavraDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ArquivoPalavraDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_ARQ_PALAVRA"));
		Dados.setNomeArquivo(rs.getString("NOME_ARQ"));
		Dados.setId_Arquivo( rs.getString("ID_ARQ"));
		Dados.setId_Palavra1( rs.getString("ID_PALAVRA_1"));
		Dados.setPalavra1( rs.getString("PALAVRA_1"));
		Dados.setId_Palavra2( rs.getString("ID_PALAVRA_2"));
		Dados.setPalavra2( rs.getString("PALAVRA_2"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoArquivoPalavra()");

		stSql= "SELECT ID_ARQ_PALAVRA, NOME_ARQ FROM PROJUDI.VIEW_ARQ_PALAVRA WHERE NOME_ARQ LIKE ?";
		stSql+= " ORDER BY NOME_ARQ ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoArquivoPalavra  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ArquivoPalavraDt obTemp = new ArquivoPalavraDt();
				obTemp.setId(rs1.getString("ID_ARQ_PALAVRA"));
				obTemp.setNomeArquivo(rs1.getString("NOME_ARQ"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ARQ_PALAVRA WHERE NOME_ARQ LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta QUANTIDADE OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ArquivoPalavraPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
