package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class PendenciaArquivoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 7509676355215453000L;

	//---------------------------------------------------------
	public PendenciaArquivoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(PendenciaArquivoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaArquivoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PEND_ARQ ("; 

		stSqlValores +=  " Values (";
		
		if ((dados.getId_Arquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Arquivo());  

			stVirgula=",";
		} 
		if ((dados.getId_Pendencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Pendencia());  

			stVirgula=",";
		}
		if ((dados.getResposta().length()>0)) {
			 stSqlCampos+=   stVirgula + "RESPOSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getResposta());  

			stVirgula=",";
		}
		if ((dados.getCodigoTemp().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_TEMP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoTemp());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PEND_ARQ",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(PendenciaArquivoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psPendenciaArquivoalterar()");

		stSql= "UPDATE PROJUDI.PEND_ARQ SET  ";
		stSql+= "ID_ARQ = ?";		 ps.adicionarLong(dados.getId_Arquivo());
		
		stSql+= ",ID_PEND = ?";		 ps.adicionarLong(dados.getId_Pendencia()); 

		stSql+= ",RESPOSTA = ?";		 ps.adicionarBoolean(dados.getResposta());  
		
		stSql+= ",CODIGO_TEMP = ?";		 ps.adicionarLong(dados.getCodigoTemp());

		stSql += " WHERE ID_PEND_ARQ  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psPendenciaArquivoexcluir()");

		stSql= "DELETE FROM PROJUDI.PEND_ARQ";
		stSql += " WHERE ID_PEND_ARQ = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public PendenciaArquivoDt consultarId(String id_pendenciaarquivo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaArquivoDt Dados=null;
		////System.out.println("....ps-ConsultaId_PendenciaArquivo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_ARQ WHERE ID_PEND_ARQ = ?";		ps.adicionarLong(id_pendenciaarquivo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaArquivo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaArquivoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( PendenciaArquivoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PEND_ARQ"));
		Dados.setNomeArquivo(rs.getString("NOME_ARQ"));
		Dados.setId_Pendencia( rs.getString("ID_PEND"));
		Dados.setId_Arquivo( rs.getString("ID_ARQ"));
		Dados.setResposta( Funcoes.FormatarLogico(rs.getString("RESPOSTA")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));		
		Dados.setPendenciaTipo( rs.getString("PEND_TIPO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoPendenciaArquivo()");

		stSql= "SELECT ID_PEND_ARQ, NOME_ARQ FROM PROJUDI.VIEW_PEND_ARQ WHERE NOME_ARQ LIKE ?";
		stSql+= " ORDER BY NOME_ARQ ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoPendenciaArquivo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				PendenciaArquivoDt obTemp = new PendenciaArquivoDt();
				obTemp.setId(rs1.getString("ID_PEND_ARQ"));
				obTemp.setNomeArquivo(rs1.getString("NOME_ARQ"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PEND_ARQ WHERE NOME_ARQ LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..PendenciaArquivoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
