package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MovimentacaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7623819112021935255L;

	//---------------------------------------------------------
	public MovimentacaoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(MovimentacaoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMovimentacaoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.MOVI ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMovimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getMovimentacao());  

			stVirgula=",";
		}
		if ((dados.getId_MovimentacaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MovimentacaoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioRealizador().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_REALIZADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioRealizador());  

			stVirgula=",";
		}
		if ((dados.getComplemento().length()>0)) {
			 stSqlCampos+=   stVirgula + "COMPLEMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getComplemento());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoPrioridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PRIOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoPrioridade());  

			stVirgula=",";
		}
		if ((dados.getDataRealizacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_REALIZACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataRealizacao());  

			stVirgula=",";
		}
		if ((dados.getPalavraChave().length()>0)) {
			 stSqlCampos+=   stVirgula + "PALAVRA_CHAVE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPalavraChave());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_MOVI",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(MovimentacaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psMovimentacaoalterar()");

		stSql= "UPDATE PROJUDI.MOVI SET  ";
		stSql+= "MOVI = ?";		 ps.adicionarString(dados.getMovimentacao());  

		stSql+= ",ID_MOVI_TIPO = ?";		 ps.adicionarLong(dados.getId_MovimentacaoTipo());  

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

		stSql+= ",ID_USU_REALIZADOR = ?";		 ps.adicionarLong(dados.getId_UsuarioRealizador());  

		stSql+= ",COMPLEMENTO = ?";		 ps.adicionarString(dados.getComplemento());  

		stSql+= ",ID_PROC_PRIOR = ?";		 ps.adicionarLong(dados.getId_ProcessoPrioridade());  

		stSql+= ",DATA_REALIZACAO = ?";		 ps.adicionarDateTime(dados.getDataRealizacao());  

		stSql+= ",PALAVRA_CHAVE = ?";		 ps.adicionarString(dados.getPalavraChave());  

		stSql += " WHERE ID_MOVI  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMovimentacaoexcluir()");

		stSql= "DELETE FROM PROJUDI.MOVI";
		stSql += " WHERE ID_MOVI = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public MovimentacaoDt consultarId(String id_movimentacao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MovimentacaoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Movimentacao)");

		stSql= "SELECT * FROM PROJUDI.VIEW_MOVI WHERE ID_MOVI = ?";		ps.adicionarLong(id_movimentacao); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Movimentacao  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MovimentacaoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( MovimentacaoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_MOVI"));
		Dados.setMovimentacao(rs.getString("MOVI"));
		Dados.setId_MovimentacaoTipo( rs.getString("ID_MOVI_TIPO"));
		Dados.setMovimentacaoTipo( rs.getString("MOVI_TIPO"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setProcessoNumero( rs.getString("PROC_NUMERO"));
		Dados.setId_UsuarioRealizador( rs.getString("ID_USU_REALIZADOR"));
		Dados.setUsuarioRealizador( rs.getString("USU_REALIZADOR"));
		Dados.setComplemento( rs.getString("COMPLEMENTO"));
		Dados.setId_ProcessoPrioridade( rs.getString("ID_PROC_PRIOR"));
		Dados.setProcessoPrioridade( rs.getString("PROC_PRIOR"));
		Dados.setDataRealizacao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_REALIZACAO")));
		Dados.setPalavraChave( rs.getString("PALAVRA_CHAVE"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setMovimentacaoTipoCodigo( rs.getString("MOVI_TIPO_CODIGO"));
		Dados.setProcessoPrioridadeCodigo( rs.getString("PROC_PRIOR_CODIGO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoMovimentacao()");

		stSql= "SELECT ID_MOVI, MOVI FROM PROJUDI.VIEW_MOVI WHERE MOVI LIKE ?";
		stSql+= " ORDER BY MOVI ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoMovimentacao  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				MovimentacaoDt obTemp = new MovimentacaoDt();
				obTemp.setId(rs1.getString("ID_MOVI"));
				obTemp.setMovimentacao(rs1.getString("MOVI"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_MOVI WHERE MOVI LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..MovimentacaoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
