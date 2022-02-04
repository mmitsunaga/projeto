package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoComplementoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MovimentacaoComplementoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8043968594117874941L;

	//---------------------------------------------------------
	public MovimentacaoComplementoPsGen() {

	}


//---------------------------------------------------------
	public void inserir(MovimentacaoComplementoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMovimentacaoComplementoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.MOVI_COMPLEMENTO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Complemento().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMPLEMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Complemento());  

			stVirgula=",";
		}
		if ((dados.getId_Movimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Movimentacao());  

			stVirgula=",";
		}
		if ((dados.getId_ComplementoTabelado().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMPLEMENTO_TABELADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ComplementoTabelado());  

			stVirgula=",";
		}
		if ((dados.getValorIdentificador().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_IDENTIFICADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getValorIdentificador());  

			stVirgula=",";
		}
		if ((dados.getValorTexto().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_TEXTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getValorTexto());  

			stVirgula=",";
		}
		if ((dados.getOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getOrigem());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_MOVI_COMPLEMENTO",ps));
			////System.out.println("....Execução OK"  );

	} 

//---------------------------------------------------------
	public MovimentacaoComplementoDt consultarId(String id_movimentacaoComplemento )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MovimentacaoComplementoDt Dados=null;
		////System.out.println("....ps-ConsultaId_MovimentacaoComplemento)");

		stSql= "SELECT * FROM PROJUDI.VIEW_MOVI_COMPLEMENTO WHERE ID_MOVI_COMPLEMENTO = ?";		ps.adicionarLong(id_movimentacaoComplemento); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_MovimentacaoComplemento  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MovimentacaoComplementoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( MovimentacaoComplementoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_MOVI_COMPLEMENTO"));
		Dados.setId_Complemento( rs.getString("ID_COMPLEMENTO"));
		Dados.setId_Movimentacao( rs.getString("ID_MOVI"));
		Dados.setId_ComplementoTabelado( rs.getString("ID_COMPLEMENTO_TABELADO"));
		Dados.setValorIdentificador( rs.getString("VALOR_IDENTIFICADOR"));
		Dados.setValorTexto( rs.getString("VALOR_TEXTO"));
		Dados.setOrigem( rs.getString("ORIGEM"));
	}


} 
