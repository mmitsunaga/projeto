package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaGrupoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -4612328737407146011L;

	//---------------------------------------------------------
	public ServentiaGrupoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ServentiaGrupoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.SERV_GRUPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentiaGrupo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_GRUPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentiaGrupo());  

			stVirgula=",";
		}
		if ((dados.getAtividade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ATIVIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getAtividade());  

			stVirgula=",";
		}
		if ((dados.getId_Serventia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Serventia());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaGrupoProximo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_GRUPO_PROXIMO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaGrupoProximo());  

			stVirgula=",";
		}
		
		if ((dados.isEnviaMagistrado())) {
			 stSqlCampos+=   stVirgula + "ENVIA_DESEMBARGADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.isEnviaMagistrado());  

			stVirgula=",";
		}

		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_SERV_GRUPO",ps)); 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaGrupoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.SERV_GRUPO SET  ";
		stSql+= "SERV_GRUPO = ?";		 ps.adicionarString(dados.getServentiaGrupo());  

		stSql+= ",ATIVIDADE = ?";		 ps.adicionarString(dados.getAtividade());  

		stSql+= ",ID_SERV = ?";		 ps.adicionarLong(dados.getId_Serventia());  

		stSql+= ",ID_SERV_GRUPO_PROXIMO = ?";		 ps.adicionarLong(dados.getId_ServentiaGrupoProximo());  
		
		stSql+= ",ENVIA_DESEMBARGADOR  = ?";		 ps.adicionarBoolean(dados.isEnviaMagistrado());  

		stSql += " WHERE ID_SERV_GRUPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.SERV_GRUPO";
		stSql += " WHERE ID_SERV_GRUPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaGrupoDt consultarId(String id_serventiagrupo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaGrupoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_GRUPO WHERE ID_SERV_GRUPO = ?";		ps.adicionarLong(id_serventiagrupo); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaGrupoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( ServentiaGrupoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_SERV_GRUPO"));
		Dados.setServentiaGrupo(rs.getString("SERV_GRUPO"));
		Dados.setAtividade( rs.getString("ATIVIDADE"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setId_ServentiaGrupoProximo( rs.getString("ID_SERV_GRUPO_PROXIMO"));
		Dados.setServentiaGrupoProximo( rs.getString("SERV_GRUPO_PROXIMO"));
		Dados.setAtividadeProxima( rs.getString("ATIVIDADE_PROXIMA"));
		Dados.setEnviaMagistrado(rs.getBoolean("ENVIA_DESEMBARGADOR"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_GRUPO, SERV_GRUPO FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";
		stSql+= " ORDER BY SERV_GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				ServentiaGrupoDt obTemp = new ServentiaGrupoDt();
				obTemp.setId(rs1.getString("ID_SERV_GRUPO"));
				obTemp.setServentiaGrupo(rs1.getString("SERV_GRUPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_GRUPO as id, SERV_GRUPO as descricao1 FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";
		stSql+= " ORDER BY SERV_GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String id_serventiagrupo, String ordenacao, String quantidadeRegistros ) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) // ordenação do PROJUDI
			ordenacao = " SERV_GRUPO ";

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_GRUPO as id, SERV_GRUPO as descricao1 FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";
		ps.adicionarString("%"+descricao+"%"); 
		if (id_serventiagrupo != null && id_serventiagrupo.length()>0){
			stSql+=" AND ID_SERV_GRUPO <> ? ";
			ps.adicionarLong(id_serventiagrupo);
		}
		stSql+= " ORDER BY " + ordenacao;;
		

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao, quantidadeRegistros);
			stSql= "SELECT Count(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ? ";
			if (id_serventiagrupo != null && id_serventiagrupo.length()>0)
				stSql+=" AND ID_SERV_GRUPO <> ? ";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarDescricaoServentiaGrupoServentiaJSON(String descricao, String posicao, String id_serventia) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_GRUPO as id, SERV_GRUPO as descricao1 FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";ps.adicionarString("%"+descricao+"%"); 
		if (id_serventia != null && id_serventia.length()>0){
			stSql+=" AND ID_SERV = ? ";ps.adicionarLong(id_serventia);
		}
		
		stSql+=" AND ID_SERV_GRUPO NOT IN (  ";
		stSql+="		SELECT SG.ID_SERV_GRUPO ";
		stSql+="		FROM SERV_GRUPO SG "; 
		stSql+=" 		INNER JOIN SERV_CARGO_SERV_GRUPO SCSG ON SG.ID_SERV_GRUPO = SCSG.ID_SERV_GRUPO ";
		stSql+=" 		INNER JOIN SERV_CARGO SC ON SC.ID_SERV_CARGO = SCSG.ID_SERV_CARGO ";
		stSql+=" 		INNER JOIN CARGO_TIPO CT ON CT.ID_CARGO_TIPO = SC.ID_CARGO_TIPO ";
		stSql+=" 		WHERE CT.CARGO_TIPO_CODIGO = ? ";ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
		stSql+="    ) ";
		
		stSql+= " ORDER BY SERV_GRUPO ";
		

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ? ";
			if (id_serventia != null && id_serventia.length()>0){
				stSql+=" AND ID_SERV = ? ";
			}
			stSql+=" AND ID_SERV_GRUPO NOT IN (  ";
			stSql+="		SELECT SG.ID_SERV_GRUPO ";
			stSql+="		FROM SERV_GRUPO SG "; 
			stSql+=" 		INNER JOIN SERV_CARGO_SERV_GRUPO SCSG ON SG.ID_SERV_GRUPO = SCSG.ID_SERV_GRUPO ";
			stSql+=" 		INNER JOIN SERV_CARGO SC ON SC.ID_SERV_CARGO = SCSG.ID_SERV_CARGO ";
			stSql+=" 		INNER JOIN CARGO_TIPO CT ON CT.ID_CARGO_TIPO = SC.ID_CARGO_TIPO ";
			stSql+=" 		WHERE CT.CARGO_TIPO_CODIGO = ? ";
			stSql+="    ) ";
			
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	//lrcampos * 18/07/2019 * Montar o select2 filtrando pela descrição serv_cargo
	public String consultarDescricaoServentiaServCargoJSON(String descricao, String posicao, String id_serventia) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT VSG.ID_SERV_GRUPO as id, VSG.SERV_GRUPO as descricao1 FROM PROJUDI.VIEW_SERV_GRUPO  VSG "; //WHERE SERV_GRUPO LIKE ?";ps.adicionarString("%"+descricao+"%"); 
		stSql += " INNER JOIN SERV_CARGO_SERV_GRUPO SCSG ON SCSG.ID_SERV_GRUPO = VSG.ID_SERV_GRUPO ";
		stSql += " INNER JOIN SERV_CARGO SG ON SG.ID_SERV_CARGO = SCSG.ID_SERV_CARGO ";
		stSql += " WHERE SG.SERV_CARGO LIKE ?";ps.adicionarString("%"+descricao+"%"); 
		
		if (id_serventia != null && id_serventia.length()>0){
			stSql += " AND VSG.ID_SERV = ? "; ps.adicionarLong(id_serventia);
		}
		stSql+= " ORDER BY VSG.SERV_GRUPO ";
		

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_SERV_GRUPO VSG "
					+ " INNER JOIN SERV_CARGO_SERV_GRUPO SCSG ON SCSG.ID_SERV_GRUPO = VSG.ID_SERV_GRUPO "
					+ " INNER JOIN SERV_CARGO SG ON SG.ID_SERV_CARGO = SCSG.ID_SERV_CARGO WHERE SG.SERV_CARGO LIKE ? ";
			if (id_serventia != null && id_serventia.length()>0)
				stSql+=" AND VSG.ID_SERV = ? ";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarDescricaoServentiaGrupoJSON(String descricao, String posicao, String id_serventia, String id_serventiagrupo ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_SERV_GRUPO as id, SERV_GRUPO as descricao1 FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ?";ps.adicionarString("%"+descricao+"%"); 
		if (id_serventiagrupo != null && id_serventiagrupo.length()>0){
			stSql+=" AND ID_SERV_GRUPO <> ? ";ps.adicionarLong(id_serventiagrupo);
		}
		
		if (id_serventia != null && id_serventia.length()>0){
			stSql+=" AND ID_SERV = ? ";ps.adicionarLong(id_serventia);
		}
		
		stSql+= " ORDER BY SERV_GRUPO ";
		

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_SERV_GRUPO WHERE SERV_GRUPO LIKE ? ";
			if (id_serventiagrupo != null && id_serventiagrupo.length()>0)
				stSql+=" AND ID_SERV_GRUPO <> ? ";
			if (id_serventia != null && id_serventia.length()>0)
				stSql+=" AND ID_SERV = ? ";
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
