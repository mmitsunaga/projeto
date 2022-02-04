package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CidadePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6112606393289578859L;

	//---------------------------------------------------------
	public CidadePsGen() {

	}



//---------------------------------------------------------
	public void inserir(CidadeDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCidadeinserir()");
		stSqlCampos= "INSERT INTO projudi.CIDADE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "CIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCidade());  

			stVirgula=",";
		}
		if ((dados.getCidadeCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CIDADE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCidadeCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_Estado().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESTADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Estado());  

			stVirgula=",";
		}
		if ((dados.getCodigoSPG().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_SPG " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoSPG());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CIDADE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(CidadeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psCidadealterar()");

		stSql= "UPDATE projudi.CIDADE SET  ";
		stSql+= "CIDADE = ?";		 ps.adicionarString(dados.getCidade());  

		stSql+= ",CIDADE_CODIGO = ?";		 ps.adicionarLong(dados.getCidadeCodigo());  

		stSql+= ",ID_ESTADO = ?";		 ps.adicionarLong(dados.getId_Estado());
		
		stSql+= ",CODIGO_SPG = ?";		 ps.adicionarLong(dados.getCodigoSPG());

		stSql += " WHERE ID_CIDADE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCidadeexcluir()");

		stSql= "DELETE FROM projudi.CIDADE";
		stSql += " WHERE ID_CIDADE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CidadeDt consultarId(String id_cidade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CidadeDt Dados=null;
		////System.out.println("....ps-ConsultaId_Cidade)");

		stSql= "SELECT * FROM projudi.VIEW_CIDADE WHERE ID_CIDADE = ?";		ps.adicionarLong(id_cidade); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Cidade  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CidadeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CidadeDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_CIDADE"));
		Dados.setCidade(rs.getString("CIDADE"));
		Dados.setCidadeCodigo( rs.getString("CIDADE_CODIGO"));
		Dados.setId_Estado( rs.getString("ID_ESTADO"));
		Dados.setEstado( rs.getString("ESTADO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setEstadoCodigo( rs.getString("ESTADO_CODIGO"));
		Dados.setUf( rs.getString("UF"));
		Dados.setCodigoSPG(rs.getString("CODIGO_SPG"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoCidade()");

		stSql= "SELECT ID_CIDADE, CIDADE FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
		stSql+= " ORDER BY CIDADE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoCidade  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CidadeDt obTemp = new CidadeDt();
				obTemp.setId(rs1.getString("ID_CIDADE"));
				obTemp.setCidade(rs1.getString("CIDADE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_CIDADE WHERE CIDADE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..CidadePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
