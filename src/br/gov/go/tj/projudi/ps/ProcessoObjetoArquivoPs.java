package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class ProcessoObjetoArquivoPs extends ProcessoObjetoArquivoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7351662945262195859L;

	private ProcessoObjetoArquivoPs( ) {}
	
	public ProcessoObjetoArquivoPs(Connection conexao) {
		Conexao = conexao;
	}
	
	public String consultarDescricaoJSON(String numeroProcesso, String codigoLote, String inquerito, String id_Serventia_Usuario, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 5;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql = "SELECT ID_PROC_OBJETO_ARQ as id, PROC_OBJETO_ARQ as descricao1, DATA_ENTRADA as descricao2,  PROC_NUMERO as descricao3, CODIGO_LOTE as descricao4, INQUERITO as descricao5";
		stSqlFrom = " FROM projudi.VIEW_PROC_OBJETO_ARQ "
				+ "WHERE ID_SERV_ARQUIVO = ? ";
		ps.adicionarLong(id_Serventia_Usuario); 
		
		if(numeroProcesso.length() > 0) {
			stSqlFrom += " AND PROC_NUMERO LIKE ?";
			ps.adicionarString(numeroProcesso+"%"); 
		}
		
		if(codigoLote.length() > 0) {
			stSqlFrom += " AND CODIGO_LOTE LIKE ?";
			ps.adicionarString(codigoLote+"%"); 
		}
		
		if(inquerito.length() > 0) {
			stSqlFrom += " AND INQUERITO LIKE ?";
			ps.adicionarString(inquerito+"%"); 
		}


		stSqlOrder= " ORDER BY PROC_OBJETO_ARQ ";
		try{

			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return stTemp; 
	}
//

	public String consultarListaObjetosJSON(String stId_Proc) throws Exception {
		String stSql, stSqlFrom;
		String stTemp="";
		
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * ";
		stSqlFrom= " FROM VIEW_PROC_OBJETO_ARQ WHERE ID_PROC = ? ";		ps.adicionarLong(stId_Proc); 
		
		try{

			rs1 = consultar(stSql+stSqlFrom, ps );
			stTemp="[";
			if (rs1.next()){	
				stTemp+=atribuirGerarJson( rs1);											
			}
			//if e while para não precisar testar toda hora se posso ou nao colocar a virgula
			while (rs1.next()){											
				stTemp+="," + atribuirGerarJson( rs1);					
			}
			stTemp+="]";							

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return stTemp; 
	}

	private String atribuirGerarJson(ResultSetTJGO rs1) throws Exception {
		ProcessoObjetoArquivoDt dt = new ProcessoObjetoArquivoDt();
		associarDt(dt,rs1);
		
//		dt.setId(rs1.getString("ID_PROC_OBJETO_ARQ")); 
//		dt.setProcObjetoArq(rs1.getString("PROC_OBJETO_ARQ"));
//		dt.setId_ObjetoSubtipo(rs1.getString("ID_OBJETO_SUBTIPO"));
//		dt.setObjetoSubtipo(rs1.getString("OBJETO_SUBTIPO"));
//		dt.setId_Delegacia(rs1.getString("ID_DELEGACIA"));
//		dt.setDelegacia(rs1.getString("DELEGACIA"));
//		dt.setId_Processo(rs1.getString("ID_PROC"));
//		dt.setProcNumero(rs1.getString("PROC_NUMERO"));
//		dt.setCodigoLote(rs1.getString("CODIGO_LOTE"));
//		dt.setId(rs1.getString("PLACA"));
//		dt.setId(rs1.getString("CHASSI"));
//		dt.setId(rs1.getString("ID_SERV_ARQUIVO"));
//		dt.setId(rs1.getString("SERVENTIAARQUIVO"));
//		dt.setId(rs1.getString("MODULO"));
//		dt.setId(rs1.getString("PERFIL"));
//		dt.setId(rs1.getString("NIVEL"));
//		dt.setId(rs1.getString("UNIDADE"));
//		dt.setId(rs1.getString("LEILAO"));
//		dt.setId(rs1.getString("STATUS_LEILAO")); 
//		dt.setId(rs1.getString("NUMERO_REGISTRO"));                          
//		dt.setId(rs1.getString("DATA_ENTRADA"));        
//		dt.setId(rs1.getString("STATUS_BAIXA NOME_RECEBEDOR"));                                               
//		dt.setId(rs1.getString("CPF_RECEBEDOR RG_RECEBEDOR"));                                                
//		dt.setId(rs1.getString("ID_RG_ORGAO_EXP_RECEBEDOR"));
//		dt.setId(rs1.getString("RG_ORGAO_EXP"));                                                 
//		dt.setId(rs1.getString("ID_ENDERECO_RECEBEDOR")); 
//		dt.setId(rs1.getString("LOGRADOURO"));
//		dt.setId(rs1.getString("CODIGO_TEMP"));
//		dt.setId(rs1.getString("NUMERO"));
//		dt.setId(rs1.getString("COMPLEMENTO"));
//		dt.setId(rs1.getString("CEP"));
//		dt.setId(rs1.getString("ID_BAIRRO BAIRRO"));                                                       
//		dt.setId(rs1.getString("CIDADE"));                                                       
//		dt.setId(rs1.getString("UF"));
		
		return dt.toJson();
	}

}
