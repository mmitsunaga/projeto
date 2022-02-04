package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaCargoPsGen extends Persistencia {


/**
     * 
     */
    private static final long serialVersionUID = 8640676634226212634L;

    //---------------------------------------------------------
	public ServentiaCargoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ServentiaCargoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//////System.out.println("....psServentiaCargoinserir()");
		
		stSqlCampos= "INSERT INTO PROJUDI.SERV_CARGO ("; 

		stSqlValores +=  " Values (";		
		if (!(dados.getServentiaCargo().length()==0)){
			stSqlCampos+=   stVirgula + "SERV_CARGO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getServentiaCargo());
			
			stVirgula=",";
		}
		if (!(dados.getId_Serventia().length()==0)){
			stSqlCampos+=   stVirgula + "ID_SERV " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_Serventia());
			
			stVirgula=",";
		}
		if (!(dados.getId_CargoTipo().length()==0)){
			stSqlCampos+=   stVirgula + "ID_CARGO_TIPO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_CargoTipo());
			
			stVirgula=",";
		}
		if (!(dados.getId_UsuarioServentiaGrupo().length()==0)){
			stSqlCampos+=   stVirgula + "ID_USU_SERV_GRUPO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_UsuarioServentiaGrupo());
			
			stVirgula=",";
		}
		if (!(dados.getQuantidadeDistribuicao().length()==0)){
			stSqlCampos+=   stVirgula + "QUANTIDADE_DIST " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getQuantidadeDistribuicao());
			
			stVirgula=",";
		}
		if (!(dados.getPrazoAgenda().length()==0)){
			stSqlCampos+=   stVirgula + "PRAZO_AGENDA " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getPrazoAgenda());
			
			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores;		
 			
		dados.setId(executarInsert(stSql,"ID_SERV_CARGO",ps));		 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaCargoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psServentiaCargoalterar()");

		stSql= "UPDATE PROJUDI.SERV_CARGO SET  ";
		stSql+= "SERV_CARGO  = ? ";  ps.adicionarString(dados.getServentiaCargo()); 
		stSql+= ",ID_SERV  = ? "; ps.adicionarLong(dados.getId_Serventia()); 
		stSql+= ",ID_CARGO_TIPO  = ? "; ps.adicionarLong(dados.getId_CargoTipo()); 
		stSql+= ",ID_USU_SERV_GRUPO  = ? "; ps.adicionarLong(dados.getId_UsuarioServentiaGrupo()); 
		stSql+= ",QUANTIDADE_DIST  = ? "; ps.adicionarLong(dados.getQuantidadeDistribuicao()); 
		stSql+= ",PRAZO_AGENDA  = ? "; ps.adicionarLong(dados.getPrazoAgenda());		
		stSql+=" WHERE ID_SERV_CARGO  = ? "; ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		//////System.out.println("....psServentiaCargoexcluir()");

		stSql= "DELETE FROM PROJUDI.SERV_CARGO";
		stSql+="  WHERE ID_SERV_CARGO = ? "; ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaCargoDt consultarId(String id_serventiacargo )  throws Exception {

		String stSql="";
		ServentiaCargoDt Dados=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV_CARGO WHERE ID_SERV_CARGO = ? "; ps.adicionarLong(id_serventiacargo);

		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados= new ServentiaCargoDt();
				associarDt(Dados, rs1);
				Dados.setGrupoTipoUsuario( rs1.getString("GRUPO_TIPO_USU"));
				Dados.setGrupoTipoUsuarioCodigo( rs1.getString("GRUPO_TIPO_USU_CODIGO"));
				Dados.setNomeUsuario(rs1.getString("NOME_USU"));
				Dados.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				Dados.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
				Dados.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
				Dados.setDataInicialSubstituicao(Funcoes.FormatarData(rs1.getDateTime("DATA_INICIAL_SUBSTITUICAO")));
				Dados.setDataFinalSubstituicao(Funcoes.FormatarData(rs1.getDateTime("DATA_FINAL_SUBSTITUICAO")));
				Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}

	protected void associarDt( ServentiaCargoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo(rs1.getString("SERV_CARGO"));
		Dados.setId_Serventia( rs1.getString("ID_SERV"));
		Dados.setServentia( rs1.getString("SERV"));
		Dados.setId_CargoTipo( rs1.getString("ID_CARGO_TIPO"));
		Dados.setCargoTipo( rs1.getString("CARGO_TIPO"));
		Dados.setId_UsuarioServentiaGrupo( rs1.getString("ID_USU_SERV_GRUPO"));
		Dados.setUsuarioServentiaGrupo( rs1.getString("USU_SERV_GRUPO"));
		Dados.setQuantidadeDistribuicao( rs1.getString("QUANTIDADE_DIST"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setServentiaCodigo( rs1.getString("SERV_CODIGO"));
		Dados.setCargoTipoCodigo( rs1.getString("CARGO_TIPO_CODIGO"));
		Dados.setServentiaUsuario( rs1.getString("SERV_USU"));
		Dados.setNomeUsuario( rs1.getString("NOME_USU"));
		Dados.setGrupoUsuario( rs1.getString("GRUPO_USU"));
		Dados.setGrupoUsuarioCodigo( rs1.getString("GRUPO_USU_CODIGO"));
		Dados.setPrazoAgenda( rs1.getString("PRAZO_AGENDA"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		//////System.out.println("..ps-ConsultaDescricaoServentiaCargo()");

		stSql= "SELECT ID_SERV_CARGO, SERV_CARGO FROM PROJUDI.VIEW_SERV_CARGO WHERE SERV_CARGO LIKE ? ";
		ps.adicionarString( descricao +"%");
		stSql+= " ORDER BY SERV_CARGO ";		
		try{
			//////System.out.println("..ps-ConsultaDescricaoServentiaCargo  " + Sql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaCargoDt obTemp = new ServentiaCargoDt();
				obTemp.setId(rs1.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV_CARGO WHERE SERV_CARGO LIKE ?";
			rs2 = consultar(stSql, ps);
			//////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
			//////System.out.println("..ServentiaCargoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

} 
