package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EnderecoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2027602517466298902L;

	//---------------------------------------------------------
	public EnderecoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EnderecoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEnderecoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.ENDERECO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getLogradouro().length()>0)) {
			 stSqlCampos+=   stVirgula + "LOGRADOURO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLogradouro());  

			stVirgula=",";
		}
		if ((dados.getNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNumero());  

			stVirgula=",";
		}
		if ((dados.getComplemento().length()>0)) {
			 stSqlCampos+=   stVirgula + "COMPLEMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getComplemento());  

			stVirgula=",";
		}
		if ((dados.getCep().length()>0)) {
			 stSqlCampos+=   stVirgula + "CEP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCep());  

			stVirgula=",";
		}
		if ((dados.getId_Bairro().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_BAIRRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Bairro());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ENDERECO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EnderecoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEnderecoalterar()");

		stSql= "UPDATE PROJUDI.ENDERECO SET  ";
		stSql+= "LOGRADOURO = ?";		 ps.adicionarString(dados.getLogradouro());  

		stSql+= ",NUMERO = ?";		 ps.adicionarString(dados.getNumero());  

		stSql+= ",COMPLEMENTO = ?";		 ps.adicionarString(dados.getComplemento());  

		stSql+= ",CEP = ?";		 ps.adicionarLong(dados.getCep());  

		stSql+= ",ID_BAIRRO = ?";		 ps.adicionarLong(dados.getId_Bairro());  

		stSql += " WHERE ID_ENDERECO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEnderecoexcluir()");

		stSql= "DELETE FROM PROJUDI.ENDERECO";
		stSql += " WHERE ID_ENDERECO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EnderecoDt consultarId(String id_endereco )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EnderecoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Endereco)");

		stSql= "SELECT * FROM PROJUDI.VIEW_ENDERECO WHERE ID_ENDERECO = ?";		ps.adicionarLong(id_endereco); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Endereco  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EnderecoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EnderecoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_ENDERECO"));
		Dados.setLogradouro(rs.getString("LOGRADOURO"));
		Dados.setNumero( rs.getString("NUMERO"));
		Dados.setComplemento( rs.getString("COMPLEMENTO"));
		Dados.setCep( rs.getString("CEP"));
		Dados.setId_Bairro( rs.getString("ID_BAIRRO"));
		Dados.setBairro( rs.getString("BAIRRO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setBairroCodigo( rs.getString("BAIRRO_CODIGO"));
		Dados.setId_Cidade( rs.getString("ID_CIDADE"));
		Dados.setCidade( rs.getString("CIDADE"));
		Dados.setCidadeCodigo( rs.getString("CIDADE_CODIGO"));
		Dados.setEstadoCodigo( rs.getString("ESTADO_CODIGO"));
		Dados.setUf( rs.getString("UF"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEndereco()");

		stSql= "SELECT ID_ENDERECO, LOGRADOURO FROM PROJUDI.VIEW_ENDERECO WHERE LOGRADOURO LIKE ?";
		stSql+= " ORDER BY LOGRADOURO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEndereco  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EnderecoDt obTemp = new EnderecoDt();
				obTemp.setId(rs1.getString("ID_ENDERECO"));
				obTemp.setLogradouro(rs1.getString("LOGRADOURO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ENDERECO WHERE LOGRADOURO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EnderecoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
