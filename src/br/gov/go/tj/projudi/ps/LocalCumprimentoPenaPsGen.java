package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class LocalCumprimentoPenaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6404733084843289022L;

	//---------------------------------------------------------
	public LocalCumprimentoPenaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(LocalCumprimentoPenaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psLocalCumprimentoPenainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.LOCAL_CUMP_PENA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getLocalCumprimentoPena().length()>0)) {
			 stSqlCampos+=   stVirgula + "LOCAL_CUMP_PENA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getLocalCumprimentoPena());  

			stVirgula=",";
		}
		if ((dados.getId_Endereco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Endereco());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_LOCAL_CUMP_PENA",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(LocalCumprimentoPenaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psLocalCumprimentoPenaalterar()");

		stSql= "UPDATE PROJUDI.LOCAL_CUMP_PENA SET  ";
		stSql+= "LOCAL_CUMP_PENA = ?";		 ps.adicionarString(dados.getLocalCumprimentoPena());  

		stSql+= ",ID_ENDERECO = ?";		 ps.adicionarLong(dados.getId_Endereco());  

		stSql += " WHERE ID_LOCAL_CUMP_PENA  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psLocalCumprimentoPenaexcluir()");

		stSql= "DELETE FROM PROJUDI.LOCAL_CUMP_PENA";
		stSql += " WHERE ID_LOCAL_CUMP_PENA = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public LocalCumprimentoPenaDt consultarId(String id_localcumprimentopena )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LocalCumprimentoPenaDt Dados=null;
		////System.out.println("....ps-ConsultaId_LocalCumprimentoPena)");

		stSql= "SELECT * FROM PROJUDI.VIEW_LOCAL_CUMP_PENA WHERE ID_LOCAL_CUMP_PENA = ?";		ps.adicionarLong(id_localcumprimentopena); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_LocalCumprimentoPena  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LocalCumprimentoPenaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( LocalCumprimentoPenaDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_LOCAL_CUMP_PENA"));
		Dados.setLocalCumprimentoPena(rs.getString("LOCAL_CUMP_PENA"));
		Dados.setId_Endereco( rs.getString("ID_ENDERECO"));
		Dados.setEndereco( rs.getString("ENDERECO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));			

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoLocalCumprimentoPena()");

		stSql= "SELECT ID_LOCAL_CUMP_PENA, LOCAL_CUMP_PENA FROM PROJUDI.VIEW_LOCAL_CUMP_PENA WHERE LOCAL_CUMP_PENA LIKE ?";
		stSql+= " ORDER BY LOCAL_CUMP_PENA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoLocalCumprimentoPena  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				LocalCumprimentoPenaDt obTemp = new LocalCumprimentoPenaDt();
				obTemp.setId(rs1.getString("ID_LOCAL_CUMP_PENA"));
				obTemp.setLocalCumprimentoPena(rs1.getString("LOCAL_CUMP_PENA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_LOCAL_CUMP_PENA WHERE LOCAL_CUMP_PENA LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..LocalCumprimentoPenaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
