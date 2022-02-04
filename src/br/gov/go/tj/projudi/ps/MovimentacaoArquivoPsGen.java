package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MovimentacaoArquivoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8043968594117874941L;

	//---------------------------------------------------------
	public MovimentacaoArquivoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(MovimentacaoArquivoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMovimentacaoArquivoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.MOVI_ARQ ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Arquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Arquivo());  

			stVirgula=",";
		}
		if ((dados.getId_Movimentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MOVI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Movimentacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_MOVI_ARQ",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(MovimentacaoArquivoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psMovimentacaoArquivoalterar()");

		stSql= "UPDATE PROJUDI.MOVI_ARQ SET  ";
		stSql+= "ID_ARQ = ?";		 ps.adicionarLong(dados.getId_Arquivo());  

		stSql+= ",ID_MOVI = ?";		 ps.adicionarLong(dados.getId_Movimentacao());  

		stSql += " WHERE ID_MOVI_ARQ  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psMovimentacaoArquivoexcluir()");

		stSql= "DELETE FROM PROJUDI.MOVI_ARQ";
		stSql += " WHERE ID_MOVI_ARQ = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public MovimentacaoArquivoDt consultarId(String id_movimentacaoarquivo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MovimentacaoArquivoDt Dados=null;
		////System.out.println("....ps-ConsultaId_MovimentacaoArquivo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_MOVI_ARQ WHERE ID_MOVI_ARQ = ?";		ps.adicionarLong(id_movimentacaoarquivo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_MovimentacaoArquivo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MovimentacaoArquivoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( MovimentacaoArquivoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_MOVI_ARQ"));
		Dados.setNomeArquivo(rs.getString("NOME_ARQ"));
		Dados.setId_Arquivo( rs.getString("ID_ARQ"));
		Dados.setId_Movimentacao( rs.getString("ID_MOVI"));
		Dados.setMovimentacaoTipo( rs.getString("MOVI_TIPO"));
		Dados.setId_MovimentacaoArquivoAcesso( rs.getString("ID_MOVI_ARQ_ACESSO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoMovimentacaoArquivo()");

		stSql= "SELECT ID_MOVI_ARQ, NOME_ARQ FROM PROJUDI.VIEW_MOVI_ARQ WHERE NOME_ARQ LIKE ?";
		stSql+= " ORDER BY NOME_ARQ ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoMovimentacaoArquivo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				MovimentacaoArquivoDt obTemp = new MovimentacaoArquivoDt();
				obTemp.setId(rs1.getString("ID_MOVI_ARQ"));
				obTemp.setNomeArquivo(rs1.getString("NOME_ARQ"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_MOVI_ARQ WHERE NOME_ARQ LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..MovimentacaoArquivoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
