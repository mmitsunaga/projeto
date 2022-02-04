package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoParteAdvogadoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 3203659926943997814L;

	//---------------------------------------------------------
	public ProcessoParteAdvogadoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteAdvogadoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteAdvogadoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE_ADVOGADO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getNomeAdvogado().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_ADVOGADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeAdvogado());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentiaAdvogado().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_ADVOGADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentiaAdvogado());  

			stVirgula=",";
		}			
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getDataEntrada().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_ENTRADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEntrada());  

			stVirgula=",";
		}
		if ((dados.getDataSaida().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_SAIDA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataSaida());  

			stVirgula=",";
		}
		if ((dados.getPrincipal().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRINC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getPrincipal());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE_ADVOGADO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteAdvogadoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoParteAdvogadoalterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE_ADVOGADO SET  ";
		stSql+= "NOME_ADVOGADO = ?";		 ps.adicionarString(dados.getNomeAdvogado());  

		stSql+= ",ID_USU_SERV_ADVOGADO = ?";		 ps.adicionarLong(dados.getId_UsuarioServentiaAdvogado());  

		stSql+= ",ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",DATA_ENTRADA = ?";		 ps.adicionarDateTime(dados.getDataEntrada());  

		stSql+= ",DATA_SAIDA = ?";		 ps.adicionarDateTime(dados.getDataSaida());  

		stSql+= ",PRINC = ?";		 ps.adicionarBoolean(dados.getPrincipal());  

		stSql += " WHERE ID_PROC_PARTE_ADVOGADO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteAdvogadoexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PARTE_ADVOGADO";
		stSql += " WHERE ID_PROC_PARTE_ADVOGADO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteAdvogadoDt consultarId(String id_processoparteadvogado )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteAdvogadoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoParteAdvogado)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_ADVOGADO WHERE ID_PROC_PARTE_ADVOGADO = ?";		ps.adicionarLong(id_processoparteadvogado); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoParteAdvogado  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoParteAdvogadoDt();
				associarDt(Dados, rs1);
				Dados.setDativo(rs1.getString("Dativo"));
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoParteAdvogadoDt Dados, ResultSetTJGO rs1 )  throws Exception {

		Dados.setId(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
		Dados.setNomeParte(rs1.getString("NOME_PARTE"));
		Dados.setNomeAdvogado( rs1.getString("NOME_ADVOGADO"));
		Dados.setId_UsuarioServentiaAdvogado( rs1.getString("ID_USU_SERV_ADVOGADO"));
		Dados.setUsuarioAdvogado( rs1.getString("USU_ADVOGADO"));
		Dados.setId_ProcessoParte( rs1.getString("ID_PROC_PARTE"));
		Dados.setDataEntrada( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_ENTRADA")));
		Dados.setDataSaida( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_SAIDA")));
		Dados.setPrincipal( Funcoes.FormatarLogico(rs1.getString("PRINC")));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setOabNumero( rs1.getString("OAB_NUMERO"));
		Dados.setOabComplemento( rs1.getString("OAB_COMPLEMENTO"));
		Dados.setId_Processo( rs1.getString("ID_PROC"));
		Dados.setProcessoNumero( rs1.getString("PROC_NUMERO"));
		Dados.setId_ProcessoParteTipo( rs1.getString("ID_PROC_PARTE_TIPO"));
		Dados.setProcessoParteTipo( rs1.getString("PROC_PARTE_TIPO"));		
			
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoParteAdvogado()");

		stSql= "SELECT ID_PROC_PARTE_ADVOGADO, NOME_PARTE FROM PROJUDI.VIEW_PROC_PARTE_ADVOGADO WHERE NOME_PARTE LIKE ?";
		stSql+= " ORDER BY NOME_PARTE ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoParteAdvogado  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoParteAdvogadoDt obTemp = new ProcessoParteAdvogadoDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
				obTemp.setNomeParte(rs1.getString("NOME_PARTE"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE_ADVOGADO WHERE NOME_PARTE LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoParteAdvogadoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
