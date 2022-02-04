package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PropriedadeDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PropriedadePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2961077489482365228L;

	//---------------------------------------------------------
	public PropriedadePsGen() {

	}



//---------------------------------------------------------
	public void inserir(PropriedadeDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPropriedadeinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROPRIEDADE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPropriedade().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROPRIEDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPropriedade());  

			stVirgula=",";
		}
		if ((dados.getPropriedadeCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROPRIEDADE_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPropriedadeCodigo());  

			stVirgula=",";
		}
		if ((dados.getValor().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValor());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROPRIEDADE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PropriedadeDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPropriedadealterar()");

		stSql= "UPDATE PROJUDI.PROPRIEDADE SET  ";
		stSql+= "PROPRIEDADE = ?";		 ps.adicionarString(dados.getPropriedade());  

		stSql+= ",PROPRIEDADE_CODIGO = ?";		 ps.adicionarLong(dados.getPropriedadeCodigo());  

		stSql+= ",VALOR = ?";		 ps.adicionarString(dados.getValor());  

		stSql += " WHERE ID_PROPRIEDADE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPropriedadeexcluir()");

		stSql= "DELETE FROM PROJUDI.PROPRIEDADE";
		stSql += " WHERE ID_PROPRIEDADE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PropriedadeDt consultarId(String id_propriedade )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PropriedadeDt Dados=null;
		////System.out.println("....ps-ConsultaId_Propriedade)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROPRIEDADE WHERE ID_PROPRIEDADE = ?";		ps.adicionarLong(id_propriedade); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Propriedade  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PropriedadeDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PropriedadeDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_PROPRIEDADE"));
		Dados.setPropriedade(rs.getString("PROPRIEDADE"));
		Dados.setPropriedadeCodigo( rs.getString("PROPRIEDADE_CODIGO"));
		Dados.setValor( rs.getString("VALOR"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));	
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPropriedade()");

		stSql= "SELECT ID_PROPRIEDADE, PROPRIEDADE FROM PROJUDI.VIEW_PROPRIEDADE WHERE PROPRIEDADE LIKE ?";
		stSql+= " ORDER BY PROPRIEDADE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPropriedade  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PropriedadeDt obTemp = new PropriedadeDt();
				obTemp.setId(rs1.getString("ID_PROPRIEDADE"));
				obTemp.setPropriedade(rs1.getString("PROPRIEDADE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROPRIEDADE WHERE PROPRIEDADE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PropriedadePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
