package br.gov.go.tj.projudi.temp;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.ps.Persistencia;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

/**
 * Classe de persistencia contendo as rotinas de negócio 
 * referentea importação dos arquivos BLOB para o sistemas
 * de arquivos.
 * 
 * @author cjamacedo
 *
 */
public class ExportacaoPs extends Persistencia {


	/**
     * 
     */
    private static final long serialVersionUID = 7830445134875354823L;

    public ExportacaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Recupera o ID do menor arquivo existente na 
	 * base de dados que ainda não foi exportado para
	 * o disco.
	 * 
	 * @return
	 * @throws Exception
	 */
	public long buscarIdInicial() throws Exception {
		long loTemp =0;
		ResultSetTJGO rs1 = null;
		String Sql = "SELECT MIN(Id_Arquivo) as Id_Menor  FROM Arquivo WHERE Caminho IS NULL";
		try{
			rs1 = consultarSemParametros(Sql);
			while (rs1.next()) {
				loTemp = rs1.getLong("Id_Menor");
			}
		
		} finally{
			rs1.close();
		}
		return loTemp;
	}

	/**
	 * Recupera o ID do menor arquivo existente na 
	 * base de dados que ainda não foi exportado para
	 * o disco.
	 * 
	 * @param idInicio - Long - Identificador de arquivo inicio na importação.
	 * @param limite - int - Quantidade de arquivos importador a cada rodada.
	 * 
	 * @return List
	 * @throws Exception
	 */
	public List<String> buscarIdsExportacao(Long idInicio, long limite) throws Exception {
		List<String> rt = new ArrayList<String>();
		ResultSetTJGO rs1 = null;
		StringBuffer Sql = new StringBuffer();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql.append("SELECT Id_Arquivo as Id_Arquivo  ");
		Sql.append(" FROM Arquivo WHERE Caminho IS NULL AND ID_ARQ >= ? AND Arquivo IS NOT NULL AND ROWNUM <= ? ");
		ps.adicionarLong(idInicio);
		ps.adicionarLong(limite);
		
		try{
			rs1 = consultar(Sql.toString(), ps);
			while (rs1.next()) {
				rt.add(rs1.getString("ID_ARQ"));
			}
		
		} finally{
			rs1.close();
		}
		return rt;
	}

	/**
	 * Retorna a quantidade de registros 
	 * que ainda serão exportados.
	 * 
	 * @return Long
	 * @throws Exception
	 */
	public Long contarIdsRegistroExportacao() throws Exception {
		ResultSetTJGO rs1 = null;
		StringBuffer Sql = new StringBuffer();
		Sql.append("SELECT COUNT(Id_Arquivo) as quantidade  ");
		Sql.append(" FROM Arquivo WHERE Caminho IS NULL AND Arquivo IS NOT NULL");
		try{
			rs1 = consultarSemParametros(Sql.toString());
			while (rs1.next()) {
				return rs1.getLong("quantidade");
			}
		
		} finally{
			rs1.close();
		}
		return null;
	}

	/**
	 * Recupera a quantidade total de arquivos existentes
	 * na base de dados que ainda não foram exportados para
	 * a base de dados.
	 * 
	 * @return long
	 * @throws Exception
	 */
	public long buscarQtdTotalArquivos() throws Exception{
		long loTemp=0;
		String Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM Arquivo WHERE Caminho IS NULL AND Arquivo IS NOT NULL";
		
		ResultSetTJGO rs1 = consultarSemParametros(Sql);
		while (rs1.next()) {
			loTemp = rs1.getLong("QUANTIDADE");
		}
		rs1.close();
		
		return loTemp;
	}
	
	/**
	 * Atualiza o registro de arquivo indicando a exportação do mesmo.
	 * 
	 * @param idArquivo
	 * @param path
	 * @throws Exception
	 */
	public void atualizarCaminhoArquivo(String idArquivo, String path) throws Exception{
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Sql= "UPDATE Arquivo SET  ";
		Sql+= "Caminho  = ? ";
		ps.adicionarString(path);
		Sql = Sql + " WHERE Id_Arquivo = ? "; 
		ps.adicionarLong(idArquivo);
		executarUpdateDelete(Sql, ps);
	} 
	
	/**
	 * Recupera as informações de um arquivo com base no ID.
	 * 
	 * @param idArquivo
	 * @return ArquivoDt
	 * @throws Exception
	 */
	public ArquivoDt getArquivoById(String idArquivo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		ArquivoDt Dados = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Sql = "SELECT * FROM Arquivo WHERE Id_Arquivo = ? AND Caminho IS NULL AND Arquivo IS NOT NULL";
		ps.adicionarLong(idArquivo);
		try{
			 rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados = new ArquivoDt();
				associarDt(Dados, rs1);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return Dados;
	}
	
	/**
	 * Popula o DT de Arquivo com os dados do banco.
	 * 
	 * @param Dados -ArquivoDt
	 * @param rs1 -ResultSetTJGO
	 * @throws Exception 
	 * @throws Exception
	 */
	private void associarDt(ArquivoDt Dados, ResultSetTJGO rs1) throws Exception{
		
		Dados.setId(rs1.getString("ID_ARQ"));
		Dados.setDataInsercao( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
		Dados.setNomeArquivo(rs1.getString("NOME_ARQ"));
		Dados.setContentType(rs1.getString("CONTENT_TYPE"));
		Dados.setArquivo(rs1.getBytes("ARQ"));
		Dados.setCaminho(rs1.getString("CAMINHO"));
		
	}
	
	
}
