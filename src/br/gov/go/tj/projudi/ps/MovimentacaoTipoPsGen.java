package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MovimentacaoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 198880081888668449L;

	//---------------------------------------------------------
	public MovimentacaoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(MovimentacaoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMovimentacaoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.MOVI_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMovimentacaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOVI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMovimentacaoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_CNJ().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CNJ_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CNJ());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_MOVI_TIPO",ps));
			dados.setMovimentacaoTipoCodigo(dados.getId());
			////System.out.println("....Execução OK"  );

	} 

//---------------------------------------------------------
	public void alterar(MovimentacaoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psMovimentacaoTipoalterar()");

		stSql= "UPDATE PROJUDI.MOVI_TIPO SET  ";
		stSql+= "MOVI_TIPO = ?";		 ps.adicionarString(dados.getMovimentacaoTipo());  

		stSql+= ",ID_CNJ_MOVI = ?";		 ps.adicionarLong(dados.getId_CNJ());  

		stSql += " WHERE ID_MOVI_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMovimentacaoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.MOVI_TIPO";
		stSql += " WHERE ID_MOVI_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public MovimentacaoTipoDt consultarId(String id_movimentacaotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MovimentacaoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_MovimentacaoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_MOVI_TIPO WHERE ID_MOVI_TIPO = ?";		ps.adicionarLong(id_movimentacaotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_MovimentacaoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MovimentacaoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( MovimentacaoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_MOVI_TIPO"));
		Dados.setMovimentacaoTipo(rs.getString("MOVI_TIPO"));
		Dados.setMovimentacaoTipoCodigo( rs.getString("MOVI_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setId_CNJ(rs.getString("ID_CNJ_MOVI"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoMovimentacaoTipo()");

		stSql= "SELECT ID_MOVI_TIPO, MOVI_TIPO FROM PROJUDI.VIEW_MOVI_TIPO WHERE MOVI_TIPO LIKE ?";
		stSql+= " ORDER BY MOVI_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoMovimentacaoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				MovimentacaoTipoDt obTemp = new MovimentacaoTipoDt();
				obTemp.setId(rs1.getString("ID_MOVI_TIPO"));
				obTemp.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_MOVI_TIPO WHERE MOVI_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..MovimentacaoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
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


		stSql= "SELECT ID_MOVI_TIPO as id, MOVI_TIPO as descricao1 FROM PROJUDI.VIEW_MOVI_TIPO WHERE MOVI_TIPO LIKE ?";
		stSql+= " ORDER BY MOVI_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MOVI_TIPO WHERE MOVI_TIPO LIKE ?";
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
