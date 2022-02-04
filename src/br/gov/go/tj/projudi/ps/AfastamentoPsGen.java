package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AfastamentoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AfastamentoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3756526146153511000L;

	//---------------------------------------------------------
	public AfastamentoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AfastamentoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAfastamentoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.AFASTAMENTO ("; 

		stSqlValores +=  " VALUES (";
 
		if ((dados.getAfastamento().length()>0)) {
			 stSqlCampos+=   stVirgula + "AFASTAMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAfastamento());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);
		
		dados.setId(executarInsert(stSql,"ID_AFASTAMENTO",ps));
			////System.out.println("....Execução OK"  );


	} 

//---------------------------------------------------------
	public void alterar(AfastamentoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAfastamentoalterar()");

		stSql= "UPDATE PROJUDI.AFASTAMENTO SET  ";
		stSql+= "AFASTAMENTO = ?";		 ps.adicionarString(dados.getAfastamento());  

		stSql += " WHERE ID_AFASTAMENTO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception{

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAfastamentoexcluir()");

		stSql= "DELETE FROM PROJUDI.AFASTAMENTO";
		stSql += " WHERE ID_AFASTAMENTO = ?";		ps.adicionarLong(chave); 

		////System.out.println("....Sql  " + stSql  );

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public AfastamentoDt consultarId(String id_afastamento )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AfastamentoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Afastamento)");

		stSql= "SELECT * FROM PROJUDI.VIEW_AFASTAMENTO WHERE ID_AFASTAMENTO = ?";		ps.adicionarLong(id_afastamento); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Afastamento  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AfastamentoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( AfastamentoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_AFASTAMENTO"));
		Dados.setAfastamento(rs.getString("AFASTAMENTO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception{

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAfastamento()");

		stSql= "SELECT ID_AFASTAMENTO, AFASTAMENTO FROM PROJUDI.VIEW_AFASTAMENTO WHERE AFASTAMENTO LIKE ?";
		stSql+= " ORDER BY AFASTAMENTO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAfastamento  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AfastamentoDt obTemp = new AfastamentoDt();
				obTemp.setId(rs1.getString("ID_AFASTAMENTO"));
				obTemp.setAfastamento(rs1.getString("AFASTAMENTO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AFASTAMENTO WHERE AFASTAMENTO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AfastamentoPsGen.consultarDescricao() Operação realizada com sucesso");

		}	finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
