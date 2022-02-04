package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AlcunhaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class AlcunhaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3729320201287661783L;

	//---------------------------------------------------------
	public AlcunhaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(AlcunhaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAlcunhainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.ALCUNHA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getAlcunha().length()>0)) {
			 stSqlCampos+=   stVirgula + "ALCUNHA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAlcunha());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ALCUNHA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(AlcunhaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAlcunhaalterar()");

		stSql= "UPDATE PROJUDI.ALCUNHA SET  ";
		stSql+= "ALCUNHA = ?";		 ps.adicionarString(dados.getAlcunha());  

		stSql += " WHERE ID_ALCUNHA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psAlcunhaexcluir()");

		stSql= "DELETE FROM PROJUDI.ALCUNHA";
		stSql += " WHERE ID_ALCUNHA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public AlcunhaDt consultarId(String id_alcunha )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		AlcunhaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Alcunha)");

		stSql= "SELECT * FROM PROJUDI.VIEW_ALCUNHA WHERE ID_ALCUNHA = ?";		ps.adicionarLong(id_alcunha); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Alcunha  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new AlcunhaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( AlcunhaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_ALCUNHA"));
		Dados.setAlcunha(rs.getString("ALCUNHA"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoAlcunha()");

		stSql= "SELECT ID_ALCUNHA, ALCUNHA FROM PROJUDI.VIEW_ALCUNHA WHERE ALCUNHA LIKE ?";
		stSql+= " ORDER BY ALCUNHA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoAlcunha  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				AlcunhaDt obTemp = new AlcunhaDt();
				obTemp.setId(rs1.getString("ID_ALCUNHA"));
				obTemp.setAlcunha(rs1.getString("ALCUNHA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ALCUNHA WHERE ALCUNHA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..AlcunhaPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
