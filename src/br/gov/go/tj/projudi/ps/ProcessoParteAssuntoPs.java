package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ProcessoParteAssuntoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class ProcessoParteAssuntoPs extends ProcessoParteAssuntoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6644894638043532423L;
	private ProcessoParteAssuntoPs( ) {}
	public ProcessoParteAssuntoPs(Connection conexao) {
		Conexao = conexao;
	}
//
	//---------------------------------------------------------
		public String consultarAssuntosJSON(String id_processo_parte ) throws Exception {

			String stSql, stSqlFrom;
			String stTemp="";
			ResultSetTJGO rs1=null;
			PreparedStatementTJGO ps = new PreparedStatementTJGO();

			stSql= "SELECT ID_PROC_PARTE_ASSUNTO, ASSUNTO, DISPOSITIVO_LEGAL, ARTIGO, DATA_INCLUSAO, ID_ASSUNTO, ID_PROC, ID_PROC_PARTE ";
			stSqlFrom= " FROM VIEW_PROC_PARTE_ASSUNTO WHERE id_proc_parte =  ?";			ps.adicionarLong(id_processo_parte); 
			
			try{

				rs1 = consultar(stSql+stSqlFrom, ps);
								
				stTemp="[";
				if (rs1.next()){	
					stTemp+=atribuirProcessoParteAssuntoJson( rs1);											
				}
				//if e while para não precisar testar toda hora se posso ou nao colocar a virgula
				while (rs1.next()){											
					stTemp+="," + atribuirProcessoParteAssuntoJson( rs1);					
				}
				stTemp+="]";							

			}finally{
				try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			}
				return stTemp; 
		}
		
		private String atribuirProcessoParteAssuntoJson( ResultSetTJGO rs1) throws Exception {
			ProcessoParteAssuntoDt dt = new ProcessoParteAssuntoDt();
			dt.setId(rs1.getString("ID_PROC_PARTE_ASSUNTO"));
			dt.setAssunto(Funcoes.limparStringToJSON(rs1.getString("ASSUNTO")));
			dt.setDispositivoLegal( Funcoes.limparStringToJSON(rs1.getString("DISPOSITIVO_LEGAL")));
			dt.setArtigo(Funcoes.limparStringToJSON(rs1.getString("ARTIGO")));
			dt.setId_Assunto(rs1.getString("ID_ASSUNTO"));
			dt.setId_Processo(rs1.getString("ID_PROC"));
			dt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
			dt.setDataInclusao(Funcoes.FormatarData(rs1.getDate("DATA_INCLUSAO")));
			return dt.toJson();
		}

}
