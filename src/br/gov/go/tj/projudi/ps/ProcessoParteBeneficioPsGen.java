package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteBeneficioDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoParteBeneficioPsGen extends Persistencia {


/**
     * 
     */
    private static final long serialVersionUID = -5671233302287325071L;

    //---------------------------------------------------------
	public ProcessoParteBeneficioPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteBeneficioDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		//////System.out.println("....psProcessoParteBeneficioinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_BENEFICIO ("; 
		
		stSqlValores +=  " Values (";
		
		if ((dados.getId_ProcessoBeneficio().length()>0)){
			stSqlCampos+=   stVirgula + "ID_PROC_BENEFICIO " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoBeneficio());
			
			stVirgula=",";
		}
		if ((dados.getId_ProcessoParte().length()>0)){
			stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarLong(dados.getId_ProcessoParte());
			
			stVirgula=",";
		}
		if ((dados.getDataInicial().length()>0)){
			stSqlCampos+=   stVirgula + "DATA_INICIAL " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDate(dados.getDataInicial());
			
			stVirgula=",";
		}
		if ((dados.getDataFinal().length()>0)){
			stSqlCampos+=   stVirgula + "DATA_FINAL " ;
			stSqlValores+=   stVirgula + "? " ;
			ps.adicionarDate(dados.getDataFinal());
			
			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql, "ID_PROC_PARTE_BENEFICIO", ps));		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteBeneficioDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psProcessoParteBeneficioalterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE_BENEFICIO SET  ";
		stSql+= "ID_PROC_BENEFICIO  = ? "; ps.adicionarLong(dados.getId_ProcessoBeneficio()); 
		stSql+= ",ID_PROC_PARTE  = ? "; ps.adicionarLong(dados.getId_ProcessoParte()); 
		stSql+= ",DATA_INICIAL = ? "; ps.adicionarDate(dados.getDataInicial()); 
		stSql+= ",DATA_FINAL = ? "; ps.adicionarDate(dados.getDataFinal());		
		stSql+= " WHERE ID_PROC_PARTE_BENEFICIO  = ? "; ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		//////System.out.println("....psProcessoParteBeneficioexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_BENEFICIO";
		stSql+= "  WHERE ID_PROC_PARTE_BENEFICIO = ? "; ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteBeneficioDt consultarId(String id_processopartebeneficio )  throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		ProcessoParteBeneficioDt Dados=null;
		ResultSetTJGO rs1 = null;
		//////System.out.println("....ps-ConsultaId_ProcessoParteBeneficio)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO WHERE ID_PROC_PARTE_BENEFICIO = ? "; ps.adicionarLong(id_processopartebeneficio);
		//////System.out.println("....Sql  " + Sql  );

		try{
			//////System.out.println("..ps-ConsultaId_ProcessoParteBeneficio  " + Sql);
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados= new ProcessoParteBeneficioDt();
				associarDt(Dados, rs1);
			}
			//rs1.close();
			//////System.out.println("..ps-ConsultaId");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}

	protected void associarDt( ProcessoParteBeneficioDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_PROC_PARTE_BENEFICIO"));
		Dados.setProcessoBeneficio(rs1.getString("PROC_BENEFICIO"));
		Dados.setId_ProcessoBeneficio( rs1.getString("ID_PROC_BENEFICIO"));
		Dados.setId_ProcessoParte( rs1.getString("ID_PROC_PARTE"));
		Dados.setNome( rs1.getString("NOME"));
		Dados.setDataInicial( Funcoes.FormatarData(rs1.getDateTime("DATA_INICIAL")));
		Dados.setDataFinal( Funcoes.FormatarData(rs1.getDateTime("DATA_FINAL")));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		//////System.out.println("..ps-ConsultaDescricaoProcessoParteBeneficio()");

		stSql= "SELECT ID_PROC_PARTE_BENEFICIO, PROC_BENEFICIO FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO WHERE PROC_BENEFICIO LIKE ? ";
		ps.adicionarString( descricao +"%");
		stSql+= " ORDER BY PROC_BENEFICIO ";		
		try{
			//////System.out.println("..ps-ConsultaDescricaoProcessoParteBeneficio  " + Sql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoParteBeneficioDt obTemp = new ProcessoParteBeneficioDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_BENEFICIO"));
				obTemp.setProcessoBeneficio(rs1.getString("PROC_BENEFICIO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_BENEFICIO WHERE PROC_BENEFICIO LIKE ?";
			rs2 = consultar(stSql, ps);
			//////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
			//////System.out.println("..ProcessoParteBeneficioPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

} 
