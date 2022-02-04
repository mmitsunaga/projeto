package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GuiaEmissaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3043178239451867061L;

	//---------------------------------------------------------
	public GuiaEmissaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GuiaEmissaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaEmissaoinserir()");
		stSqlCampos= "INSERT INTO projudi.GUIA_EMIS ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getGuiaEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "GUIA_EMIS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGuiaEmissao());  

			stVirgula=",";
		}
		if ((dados.getId_GuiaModelo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA_MODELO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaModelo());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getNumeroProcessoDependente().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_PROC_DEPENDENTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNumeroProcessoDependente());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_GuiaEmissaoPrincipal().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA_EMIS_PRINC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaEmissaoPrincipal());  

			stVirgula=",";
		}
		if ((dados.getServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentia());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_GuiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_GuiaStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaStatus());  

			stVirgula=",";
		}
		if ((dados.getId_AreaDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getId_Usuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Usuario());  

			stVirgula=",";
		}
		if ((dados.getDataRecebimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_RECEBIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataRecebimento());  

			stVirgula=",";
		}
		if ((dados.getDataVencimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VENCIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataVencimento());  

			stVirgula=",";
		}
		if ((dados.getRequerente().length()>0)) {
			 stSqlCampos+=   stVirgula + "REQUERENTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRequerente());  

			stVirgula=",";
		}
		if ((dados.getRequerido().length()>0)) {
			 stSqlCampos+=   stVirgula + "REQUERIDO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRequerido());  

			stVirgula=",";
		}
		if ((dados.getValorAcao().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_ACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorAcao());  

			stVirgula=",";
		}
		if ((dados.getNumeroGuiaCompleto().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_GUIA_COMPLETO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNumeroGuiaCompleto());  

			stVirgula=",";
		}
		if ((dados.getNumeroDUAM().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_DUAM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNumeroDUAM());  

			stVirgula=",";
		}
		if ((dados.getQuantidadeParcelasDUAM().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUANTIDADE_PARCELAS_DUAM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getQuantidadeParcelasDUAM());  

			stVirgula=",";
		}
		if ((dados.getDataVencimentoDUAM().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VENCIMENTO_DUAM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataVencimentoDUAM());  

			stVirgula=",";
		}
		if ((dados.getValorImpostoMunicipalDUAM().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_IMPOSTO_MUNICIPAL_DUAM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValorImpostoMunicipalDUAM());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GUIA_EMIS",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public boolean alterar(GuiaEmissaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";


		stSql= "UPDATE projudi.GUIA_EMIS SET  ";
		stSql+= "GUIA_EMIS = ?";		 ps.adicionarString(dados.getGuiaEmissao());

		stSql+= ",ID_GUIA_MODELO = ?";		 ps.adicionarLong(dados.getId_GuiaModelo());

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());

		stSql+= ",NUMERO_PROC_DEPENDENTE = ?";		 ps.adicionarLong(dados.getNumeroProcessoDependente());

		stSql+= ",ID_PROC_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoTipo());

		stSql+= ",ID_GUIA_EMIS_PRINC = ?";		 ps.adicionarLong(dados.getId_GuiaEmissaoPrincipal());

		stSql+= ",SERV = ?";		 ps.adicionarString(dados.getServentia());

		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());

		stSql+= ",ID_GUIA_TIPO = ?";		 ps.adicionarLong(dados.getId_GuiaTipo());

		stSql+= ",ID_GUIA_STATUS = ?";		 ps.adicionarLong(dados.getId_GuiaStatus());  

		stSql+= ",ID_AREA_DIST = ?";		 ps.adicionarLong(dados.getId_AreaDistribuicao());  

		stSql+= ",ID_USU = ?";		 ps.adicionarLong(dados.getId_Usuario());  

		stSql+= ",DATA_RECEBIMENTO = ?";		 ps.adicionarDateTime(dados.getDataRecebimento());  

		stSql+= ",DATA_VENCIMENTO = ?";		 ps.adicionarDateTime(dados.getDataVencimento());  

		stSql+= ",REQUERENTE = ?";		 ps.adicionarString(dados.getRequerente());  

		stSql+= ",REQUERIDO = ?";		 ps.adicionarString(dados.getRequerido());  

		stSql+= ",VALOR_ACAO = ?";		 ps.adicionarString(dados.getValorAcao());  

		stSql+= ",NUMERO_GUIA_COMPLETO = ?";		 ps.adicionarLong(dados.getNumeroGuiaCompleto());  

		stSql+= ",NUMERO_DUAM = ?";		 ps.adicionarLong(dados.getNumeroDUAM());  

		stSql+= ",QUANTIDADE_PARCELAS_DUAM = ?";		 ps.adicionarLong(dados.getQuantidadeParcelasDUAM());  

		stSql+= ",DATA_VENCIMENTO_DUAM = ?";		 ps.adicionarDate(dados.getDataVencimentoDUAM());  

		stSql+= ",VALOR_IMPOSTO_MUNICIPAL_DUAM = ?";		 ps.adicionarString(dados.getValorImpostoMunicipalDUAM());  

		stSql += " WHERE ID_GUIA_EMIS  = ? "; 		ps.adicionarLong(dados.getId()); 

		return executarUpdateDelete(stSql,ps) > 0;
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaEmissaoexcluir()");

		stSql= "DELETE FROM projudi.GUIA_EMIS";
		stSql += " WHERE ID_GUIA_EMIS = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GuiaEmissaoDt consultarId(String id_guiaemissao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GuiaEmissaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_GuiaEmissao)");

		stSql= "SELECT * FROM projudi.VIEW_GUIA_EMIS WHERE ID_GUIA_EMIS = ?";		ps.adicionarLong(id_guiaemissao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GuiaEmissao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GuiaEmissaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GuiaEmissaoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GUIA_EMIS"));
		Dados.setGuiaEmissao(rs.getString("GUIA_EMIS"));
		Dados.setId_GuiaModelo( rs.getString("ID_GUIA_MODELO"));
		Dados.setGuiaModelo( rs.getString("GUIA_MODELO"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setId_Serventia( rs.getString("ID_SERV"));
		Dados.setServentia( rs.getString("SERV"));
		Dados.setId_ProcessoTipo( rs.getString("ID_PROC_TIPO"));
		Dados.setProcessoTipo( rs.getString("PROC_TIPO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_GUIA_EMIS, GUIA_EMIS FROM projudi.VIEW_GUIA_EMIS WHERE GUIA_EMIS LIKE ?";
		stSql+= " ORDER BY GUIA_EMIS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql, ps, posicao);

			while (rs1.next()) {
				GuiaEmissaoDt obTemp = new GuiaEmissaoDt();
				obTemp.setId(rs1.getString("ID_GUIA_EMIS"));
				obTemp.setGuiaEmissao(rs1.getString("GUIA_EMIS"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE FROM projudi.VIEW_GUIA_EMIS WHERE GUIA_EMIS LIKE ?";
			rs2 = consultar(stSql,ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
