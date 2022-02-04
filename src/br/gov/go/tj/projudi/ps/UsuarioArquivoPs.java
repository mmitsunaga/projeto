package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioArquivoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UsuarioArquivoPs extends UsuarioArquivoPsGen {

	private static final long serialVersionUID = -2486531666916598296L;

	public UsuarioArquivoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Consultar dados detalhados de um arquivo de usuário
	 * 
	 * @param id_UsuarioArquivo, identificação do arquivo de usuário
	 * 
	 * @author msapaula
	 */
	public UsuarioArquivoDt consultarIdCompleto(String id_UsuarioArquivo) throws Exception {
		UsuarioArquivoDt usuarioArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String Sql = " SELECT * FROM PROJUDI.VIEW_USU_ARQ_COMPLETA u";
		Sql += " WHERE u.ID_USU_ARQ = ?";
		ps.adicionarLong(id_UsuarioArquivo);
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				usuarioArquivoDt = new UsuarioArquivoDt();
				this.associarDt(usuarioArquivoDt, rs1);
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivoDt.setDataInsercao(Funcoes.FormatarData(rs1.getDateTime("DATA_INSERCAO")));
				arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				usuarioArquivoDt.setArquivoDt(arquivoDt);
			}

		
		} finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e1) {}
		}
		return usuarioArquivoDt;
	}

	/**
	 * Consultar arquivos de um determinado usuário
	 * 
	 * @param id_Usuario,
	 *            identificação do usuário
	 * 
	 * @author msapaula
	 */
	public List consultarArquivosUsuario(String id_Usuario) throws Exception {
		List arquivos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String Sql = " SELECT * FROM PROJUDI.VIEW_USU_ARQ_COMPLETA u";
		Sql += " WHERE u.ID_USU = ?";
		ps.adicionarLong(id_Usuario);
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();
				UsuarioArquivoDt usuarioArquivoDt = new UsuarioArquivoDt();
				this.associarDt(usuarioArquivoDt, rs1);
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivoDt.setDataInsercao(Funcoes.FormatarData(rs1.getDateTime("DATA_INSERCAO")));
				arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
				usuarioArquivoDt.setArquivoDt(arquivoDt);
				arquivos.add(usuarioArquivoDt);
			}

		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return arquivos;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql = "SELECT ID_USU_ARQ AS ID, USU AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_USU_ARQ";
		stSqlFrom += " WHERE USU LIKE ?";
		stSqlOrder = " ORDER BY USU ";
		ps.adicionarString("%"+ descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
}
