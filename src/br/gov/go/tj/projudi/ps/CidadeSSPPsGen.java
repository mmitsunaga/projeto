package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CidadeSSPDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CidadeSSPPsGen extends Persistencia {


/**
     * 
     */
    private static final long serialVersionUID = -6954128556018382897L;

    //---------------------------------------------------------
	public CidadeSSPPsGen() {

	}



//---------------------------------------------------------
	public void inserir(CidadeSSPDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		//////System.out.println("....psCidadeSSPinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.CIDADE_SSP (";
		
		stSqlValores +=  " Values (";
		
		if (!(dados.getCidade().length()==0)){
			stSqlCampos+=   stVirgula + "CIDADE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getCidade());
			
			stVirgula=",";
		}
		if (!(dados.getId_CidadeTJ().length()==0)){
			stSqlCampos+=   stVirgula + "ID_CIDADE_TJ " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_CidadeTJ());
			
			stVirgula=",";
		}
		if (!(dados.getEstado().length()==0)){
			stSqlCampos+=   stVirgula + "ESTADO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarString(dados.getEstado());
			
			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		dados.setId(executarInsert(stSql,"ID_CIDADE_SSP",ps));
	} 

//---------------------------------------------------------
	public void alterar(CidadeSSPDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psCidadeSSPalterar()");

		stSql= "UPDATE PROJUDI.CIDADE_SSP SET  ";
		stSql+= "CIDADE  = ? "; ps.adicionarString(dados.getCidade()); 
		stSql+= ",ID_CIDADE_TJ  = ? "; ps.adicionarLong(dados.getId_CidadeTJ()); 
		stSql+= ",ESTADO  = ? "; ps.adicionarString(dados.getEstado());		
		stSql+= " WHERE ID_CIDADE_SSP  = ? "; ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psCidadeSSPexcluir()");

		stSql= "DELETE FROM PROJUDI.CIDADE_SSP";
		stSql+= "  WHERE ID_CIDADE_SSP = ? "; ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public CidadeSSPDt consultarId(String id_cidadessp )  throws Exception {

		String stSql;
		ResultSetTJGO rs1 = null;
		CidadeSSPDt Dados=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//////System.out.println("....ps-ConsultaId_CidadeSSP)");

		stSql= "SELECT * FROM PROJUDI.VIEW_CIDADE_SSP WHERE ID_CIDADE_SSP = ? ";
		ps.adicionarLong(id_cidadessp);
		//////System.out.println("....Sql  " + Sql  );

		try{
			//////System.out.println("..ps-ConsultaId_CidadeSSP  " + Sql);
			 rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados= new CidadeSSPDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
			//////System.out.println("..ps-ConsultaId");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
        return Dados; 
	}

	protected void associarDt( CidadeSSPDt Dados, ResultSetTJGO rs1 )  throws Exception {

		Dados.setId(rs1.getString("ID_CIDADE_SSP"));
		Dados.setCidade(rs1.getString("CIDADE"));
		Dados.setId_CidadeTJ( rs1.getString("ID_CIDADE_TJ"));
		Dados.setCidadeTJ( rs1.getString("CIDADE_TJ"));
		Dados.setEstado( rs1.getString("ESTADO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//////System.out.println("..ps-ConsultaDescricaoCidadeSSP()");

		stSql= "SELECT ID_CIDADE_SSP, CIDADE FROM PROJUDI.VIEW_CIDADE_SSP WHERE CIDADE LIKE ? ";
		ps.adicionarString( descricao +"%");
		stSql+= " ORDER BY CIDADE ";		
		try{
			//////System.out.println("..ps-ConsultaDescricaoCidadeSSP  " + Sql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				CidadeSSPDt obTemp = new CidadeSSPDt();
				obTemp.setId(rs1.getString("ID_CIDADE_SSP"));
				obTemp.setCidade(rs1.getString("CIDADE"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_CIDADE_SSP WHERE CIDADE LIKE ? ";
			rs2 = consultar(stSql, ps);
			//////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
			//////System.out.println("..CidadeSSPPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

} 
