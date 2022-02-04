package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class CargoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 8232076907333181511L;

	//---------------------------------------------------------
	public CargoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CargoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCargoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.CARGO_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getCargoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CARGO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCargoTipo());  

			stVirgula=",";
		}
		if ((dados.getCargoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CARGO_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCargoTipoCodigo());  

			stVirgula=",";
		}
		if ((dados.getId_Grupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Grupo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_CARGO_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(CargoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psCargoTipoalterar()");

		stSql= "UPDATE PROJUDI.CARGO_TIPO SET  ";
		stSql+= "CARGO_TIPO = ?";		 ps.adicionarString(dados.getCargoTipo());  

		stSql+= ",CARGO_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getCargoTipoCodigo());  

		stSql+= ",ID_GRUPO = ?";		 ps.adicionarLong(dados.getId_Grupo());  

		stSql += " WHERE ID_CARGO_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psCargoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.CARGO_TIPO";
		stSql += " WHERE ID_CARGO_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CargoTipoDt consultarId(String id_cargotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CargoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_CargoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CARGO_TIPO WHERE ID_CARGO_TIPO = ?";		ps.adicionarLong(id_cargotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_CargoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CargoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( CargoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_CARGO_TIPO"));
		Dados.setCargoTipo(rs.getString("CARGO_TIPO"));
		Dados.setCargoTipoCodigo( rs.getString("CARGO_TIPO_CODIGO"));
		Dados.setId_Grupo( rs.getString("ID_GRUPO"));
		Dados.setGrupo( rs.getString("GRUPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setGrupoCodigo( rs.getString("GRUPO_CODIGO"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoCargoTipo()");

		stSql= "SELECT ID_CARGO_TIPO, CARGO_TIPO FROM PROJUDI.VIEW_CARGO_TIPO WHERE CARGO_TIPO LIKE ?";
		stSql+= " ORDER BY CARGO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoCargoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CargoTipoDt obTemp = new CargoTipoDt();
				obTemp.setId(rs1.getString("ID_CARGO_TIPO"));
				obTemp.setCargoTipo(rs1.getString("CARGO_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CARGO_TIPO WHERE CARGO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..CargoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CARGO_TIPO as id, CARGO_TIPO as descricao1 FROM PROJUDI.VIEW_CARGO_TIPO WHERE CARGO_TIPO LIKE ?";
		stSql+= " ORDER BY CARGO_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CARGO_TIPO WHERE CARGO_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
