package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PendenciaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6454059914757475706L;

	//---------------------------------------------------------
	public PendenciaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PendenciaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PEND ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getPendencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "PEND " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPendencia());  

			stVirgula=",";
		}
		if ((dados.getDataVisto().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VISTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataVisto());  

			stVirgula=",";
		}			
		if ((dados.getId_PendenciaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PendenciaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Movimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Movimentacao());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoPrioridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PRIOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoPrioridade());  

			stVirgula=",";
		}		
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_PendenciaStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PendenciaStatus());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioCadastrador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_CADASTRADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioCadastrador());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioFinalizador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_FINALIZADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioFinalizador());  

			stVirgula=",";
		}	
		if ((dados.getDataInicio().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INICIO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataInicio());  

			stVirgula=",";
		}
		if ((dados.getDataFim().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_FIM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataFim());  

			stVirgula=",";
		}
		if ((dados.getDataLimite().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_LIMITE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataLimite());  

			stVirgula=",";
		}
		if ((dados.getDataDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getPrazo().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRAZO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPrazo());  

			stVirgula=",";
		}
		if ((dados.getDataTemp().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_TEMP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataTemp());  

			stVirgula=",";
		}
		if ((dados.getId_PendenciaPai().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND_PAI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PendenciaPai());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PEND",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PendenciaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PEND SET  ";
		
		stSql+= "DATA_VISTO = ?";		 ps.adicionarDateTime(dados.getDataVisto());  

		stSql+= ",ID_PEND_TIPO = ?";		 ps.adicionarLong(dados.getId_PendenciaTipo());  

		stSql+= ",ID_MOVI = ?";		 ps.adicionarLong(dados.getId_Movimentacao());  

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  	

		stSql+= ",ID_PROC_PRIOR = ?";		 ps.adicionarLong(dados.getId_ProcessoPrioridade());  	

		stSql+= ",ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_PEND_STATUS = ?";		 ps.adicionarLong(dados.getId_PendenciaStatus());  

		stSql+= ",ID_USU_CADASTRADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioCadastrador());  

		stSql+= ",ID_USU_FINALIZADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioFinalizador());  

		stSql+= ",DATA_INICIO = ?";		 ps.adicionarDateTime(dados.getDataInicio());  

		stSql+= ",DATA_FIM = ?";		 ps.adicionarDateTime(dados.getDataFim());  

		stSql+= ",DATA_LIMITE = ?";		 ps.adicionarDateTime(dados.getDataLimite());  

		stSql+= ",DATA_DIST = ?";		 ps.adicionarDateTime(dados.getDataDistribuicao());  

		stSql+= ",PRAZO = ?";		 ps.adicionarLong(dados.getPrazo());  

		stSql+= ",DATA_TEMP = ?";		 ps.adicionarDateTime(dados.getDataTemp());  

		stSql+= ",ID_PEND_PAI = ?";		 ps.adicionarLong(dados.getId_PendenciaPai());  

		stSql += " WHERE ID_PEND  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaexcluir()");

		stSql= "DELETE FROM PROJUDI.PEND";
		stSql += " WHERE ID_PEND = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PendenciaDt consultarId(String id_pendencia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Pendencia)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND WHERE ID_PEND = ?";		ps.adicionarLong(id_pendencia); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Pendencia  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaDt();
				associarDt(Dados, rs1);
				Dados.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				Dados.setClassificador(rs1.getString("CLASSIFICADOR"));
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PendenciaDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PEND"));
		Dados.setPendencia(rs.getString("PEND"));
		Dados.setDataVisto( Funcoes.FormatarDataHora(rs.getDateTime("DATA_VISTO")));
		Dados.setId_PendenciaTipo( rs.getString("ID_PEND_TIPO"));
		Dados.setPendenciaTipo( rs.getString("PEND_TIPO"));
		Dados.setId_Movimentacao( rs.getString("ID_MOVI"));
		Dados.setMovimentacao( rs.getString("MOVI"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setProcessoNumero( rs.getString("PROC_NUMERO_COMPLETO"));
		Dados.setId_ProcessoPrioridade( rs.getString("ID_PROC_PRIOR"));
		Dados.setProcessoPrioridade( rs.getString("PROC_PRIOR"));
		Dados.setProcessoPrioridadeCodigo( rs.getString("PROC_PRIOR_CODIGO"));
		Dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));		
		Dados.setNomeParte( rs.getString("NOME_PARTE"));
		Dados.setId_PendenciaStatus( rs.getString("ID_PEND_STATUS"));
		Dados.setPendenciaStatus( rs.getString("PEND_STATUS"));
		Dados.setId_UsuarioCadastrador( rs.getString("ID_USU_CADASTRADOR"));
		Dados.setUsuarioCadastrador( rs.getString("USU_CADASTRADOR"));
		Dados.setId_UsuarioFinalizador( rs.getString("ID_USU_FINALIZADOR"));
		Dados.setUsuarioFinalizador( rs.getString("USU_FINALIZADOR"));
		Dados.setDataInicio( Funcoes.FormatarDataHora(rs.getDateTime("DATA_INICIO")));
		Dados.setDataFim( Funcoes.FormatarDataHora(rs.getDateTime("DATA_FIM")));
		Dados.setDataLimite( Funcoes.FormatarDataHora(rs.getDateTime("DATA_LIMITE")));
		Dados.setDataDistribuicao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_DIST")));
		Dados.setPrazo( rs.getString("PRAZO"));
		Dados.setDataTemp( Funcoes.FormatarDataHora(rs.getDateTime("DATA_TEMP")));
		Dados.setId_PendenciaPai( rs.getString("ID_PEND_PAI"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setPendenciaTipoCodigo( rs.getString("PEND_TIPO_CODIGO"));
		Dados.setPendenciaStatusCodigo( rs.getString("PEND_STATUS_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPendencia()");

		stSql= "SELECT ID_PEND, PEND FROM PROJUDI.VIEW_PEND WHERE PEND LIKE ?";
		stSql+= " ORDER BY PEND ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPendencia  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PendenciaDt obTemp = new PendenciaDt();
				obTemp.setId(rs1.getString("ID_PEND"));
				obTemp.setPendencia(rs1.getString("PEND"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PEND WHERE PEND LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PendenciaPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
