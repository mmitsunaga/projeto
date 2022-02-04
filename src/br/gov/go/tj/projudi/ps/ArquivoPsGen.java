package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ArquivoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -2166908823196085670L;

	//---------------------------------------------------------
	public ArquivoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ArquivoDt dados ) throws Exception{

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArquivoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.ARQ ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getNomeArquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeArquivo());  

			stVirgula=",";
		}
		if ((dados.getId_ArquivoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ARQ_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ArquivoTipo());  

			stVirgula=",";
		}
		if ((dados.getContentType().length()>0)) {
			 stSqlCampos+=   stVirgula + "CONTENT_TYPE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getContentType());  

			stVirgula=",";
		}
		if ((dados.getArquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getArquivo());  

			stVirgula=",";
		}
		if ((dados.getCaminho().length()>0)) {
			 stSqlCampos+=   stVirgula + "CAMINHO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCaminho());  

			stVirgula=",";
		}
		if ((dados.getDataInsercao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_INSERCAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataInsercao());  

			stVirgula=",";
		}
		if ((dados.getUsuarioAssinador().length()>0)) {
			 stSqlCampos+=   stVirgula + "USU_ASSINADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getUsuarioAssinador());  

			stVirgula=",";
		}
		if ((dados.getRecibo().length()>0)) {
			 stSqlCampos+=   stVirgula + "RECIBO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getRecibo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_ARQ",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ArquivoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psArquivoalterar()");

		stSql= "UPDATE PROJUDI.ARQ SET  ";
		stSql+= "NOME_ARQ = ?";		 ps.adicionarString(dados.getNomeArquivo());  

		stSql+= ",ID_ARQ_TIPO = ?";		 ps.adicionarLong(dados.getId_ArquivoTipo());  

		stSql+= ",CONTENT_TYPE = ?";		 ps.adicionarString(dados.getContentType());  

		stSql+= ",ARQ = ?";		 ps.adicionarString(dados.getArquivo());  

		stSql+= ",CAMINHO = ?";		 ps.adicionarString(dados.getCaminho());  

		stSql+= ",DATA_INSERCAO = ?";		 ps.adicionarDateTime(dados.getDataInsercao());  

		stSql+= ",USU_ASSINADOR = ?";		 ps.adicionarString(dados.getUsuarioAssinador());  

		stSql+= ",RECIBO = ?";		 ps.adicionarBoolean(dados.getRecibo());  

		stSql += " WHERE ID_ARQ  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psArquivoexcluir()");

		stSql= "DELETE FROM PROJUDI.ARQ";
		stSql += " WHERE ID_ARQ = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ArquivoDt consultarId(String id_arquivo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ArquivoDt Dados=null;
		////System.out.println("....ps-ConsultaId_Arquivo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_ARQ WHERE ID_ARQ = ?";		ps.adicionarLong(id_arquivo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Arquivo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ArquivoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ArquivoDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
			Dados.setId(rs1.getString("ID_ARQ"));
			Dados.setNomeArquivo(rs1.getString("NOME_ARQ"));
			Dados.setId_ArquivoTipo( rs1.getString("ID_ARQ_TIPO"));
			Dados.setArquivoTipo( rs1.getString("ARQ_TIPO"));
			Dados.setContentType( rs1.getString("CONTENT_TYPE"));
			Dados.setArquivo( rs1.getString("ARQ"));
			Dados.setCaminho( rs1.getString("CAMINHO"));
			Dados.setDataInsercao( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
			Dados.setUsuarioAssinador( rs1.getString("USU_ASSINADOR"));
			Dados.setRecibo( Funcoes.FormatarLogico(rs1.getString("RECIBO")));
			Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
			Dados.setArquivoTipoCodigo( rs1.getString("ARQ_TIPO_CODIGO"));
				
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoArquivo()");

		stSql= "SELECT ID_ARQ, NOME_ARQ FROM PROJUDI.VIEW_ARQ WHERE NOME_ARQ LIKE ?";
		stSql+= " ORDER BY NOME_ARQ ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoArquivo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ArquivoDt obTemp = new ArquivoDt();
				obTemp.setId(rs1.getString("ID_ARQ"));
				obTemp.setNomeArquivo(rs1.getString("NOME_ARQ"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_ARQ WHERE NOME_ARQ LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ArquivoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
