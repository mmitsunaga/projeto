package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.UsuarioFoneDt;


public class UsuarioFonePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 722639724334505063L;

	//---------------------------------------------------------
	public UsuarioFonePsGen() {


	}


//---------------------------------------------------------
	public void alterar(UsuarioFoneDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE projudi.USU_FONE SET  ";
		stSql+= "ID_USU = ?";		 ps.adicionarLong(dados.getId_Usuario());  

		stSql+= ",IMEI = ?";		 ps.adicionarString(dados.getImei());  

		stSql+= ",FONE = ?";		 ps.adicionarString(dados.getFone());  

		stSql+= ",CODIGO = ?";		 ps.adicionarString(dados.getCodigo());  

		stSql+= ",CODIGO_VALIDADE = ?";		 ps.adicionarLong(dados.getCodigoValidade());  

		stSql+= ",DATA_PEDIDO = ?";		 ps.adicionarDateTime(dados.getDataPedido());  

		stSql+= ",DATA_LIBERACAO = ?";		 ps.adicionarDateTime(dados.getDataLiberacao());  

		stSql += " WHERE ID_USU_FONE  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM projudi.USU_FONE";
		stSql += " WHERE ID_USU_FONE = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public UsuarioFoneDt consultarId(String id_usufone )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioFoneDt Dados=null;


		stSql= "SELECT * FROM projudi.VIEW_USU_FONE WHERE ID_USU_FONE = ?";		ps.adicionarLong(id_usufone); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioFoneDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( UsuarioFoneDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_USU_FONE"));
			Dados.setUsuario(rs.getString("USU"));
			Dados.setId_Usuario( rs.getString("ID_USU"));
			Dados.setImei( rs.getString("IMEI"));
			Dados.setFone( rs.getString("FONE"));
			Dados.setCodigo( rs.getString("CODIGO"));
			Dados.setCodigoValidade( rs.getString("CODIGO_VALIDADE"));
			Dados.setDataPedido( Funcoes.FormatarDataHora(rs.getString("DATA_PEDIDO")));
			Dados.setDataLiberacao( Funcoes.FormatarDataHora(rs.getString("DATA_LIBERACAO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_USU_FONE, USU ";
		stSqlFrom= " FROM projudi.VIEW_USU_FONE WHERE USU LIKE ?";
		stSqlOrder = " ORDER BY USU ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				UsuarioFoneDt obTemp = new UsuarioFoneDt();
				obTemp.setId(rs1.getString("ID_USU_FONE"));
				obTemp.setUsuario(rs1.getString("USU"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_USU_FONE as id, USU as descricao1 ";
		stSqlFrom= " FROM projudi.VIEW_USU_FONE WHERE USU LIKE ?";
		ps.adicionarString(descricao+"%"); 

		stSqlOrder= " ORDER BY USU ";
		try{


			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}

} 
