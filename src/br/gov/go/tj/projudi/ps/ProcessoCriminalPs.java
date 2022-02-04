package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.ProcessoCriminalDt;
import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoCriminalPs extends ProcessoCriminalPsGen {

	private static final long serialVersionUID = -6796493482815348094L;

	public ProcessoCriminalPs(Connection conexao){
		Conexao = conexao;
	}

	public ProcessoCriminalDt consultarIdProcesso(String id_processo) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoCriminalDt Dados = null;
		//System.out.println("....ps-ConsultaId_ProcessoCriminal)");

		stSql = "SELECT * FROM PROJUDI.VIEW_PROC_CRIMINAL WHERE ID_PROC = ?";
		ps.adicionarLong(id_processo);

		//System.out.println("....Sql  " + stSql);

		try{
			//System.out.println("..ps-ConsultaId_ProcessoCriminal  " + stSql);
			rs1 = consultar(stSql, ps);
			Dados = new ProcessoCriminalDt();
			if (rs1.next()) {
				Dados = new ProcessoCriminalDt();
				associarDt(Dados, rs1);
			}
			//System.out.println("..ps-ConsultaIdProcesso");
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	//---------------------------------------------------------
		public void inserir(ProcessoCriminalDt dados ) throws Exception {

			String stSqlCampos="";
			String stSqlValores="";
			String stSql="";
			String stVirgula="";
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			//System.out.println("....psProcessoCriminalinserir()");
			stSqlCampos= "INSERT INTO PROJUDI.PROC_CRIMINAL ("; 

			stSqlValores +=  " Values (";
	 
			if ((dados.getId_Processo().length()>0)) {
				 stSqlCampos+=   stVirgula + "ID_PROC " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getId_Processo());  

				stVirgula=",";
			}
			if ((dados.getReuPreso().length()>0)) {
				 stSqlCampos+=   stVirgula + "REU_PRESO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarBoolean(dados.getReuPreso());  

				stVirgula=",";
			}
			if ((dados.getInquerito().length()>0)) {
				 stSqlCampos+=   stVirgula + "INQUERITO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getInquerito());  

				stVirgula=",";
			}
			if ((dados.getDataPrisao().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_PRISAO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataPrisao());  

				stVirgula=",";
			}
			if ((dados.getDataOferecimentoDenuncia().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_OFERECIMENTO_DENUNCIA " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataOferecimentoDenuncia());  

				stVirgula=",";
			}
			if ((dados.getDataRecebimentoDenuncia().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_RECEBIMENTO_DENUNCIA " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataRecebimentoDenuncia());  

				stVirgula=",";
			}
			if ((dados.getDataTransacaoPenal().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_TRANSACAO_PENAL " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataTransacaoPenal());  

				stVirgula=",";
			}
			if ((dados.getDataSuspensaoPenal().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_SUSPENSAO_PENAL " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataSuspensaoPenal());  

				stVirgula=",";
			}
			if ((dados.getDataFato().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_FATO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataFato());  

				stVirgula=",";
			}
			if ((dados.getDataPrescricao().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_PRESCRICAO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataPrescricao());  

				stVirgula=",";
			}
			if ((dados.getDataBaixa().length()>0)) {
				 stSqlCampos+=   stVirgula + "DATA_BAIXA " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarDate(dados.getDataBaixa());  

				stVirgula=",";
			}
			if ((dados.getId_ProcessoArquivamentoTipo().length()>0)) {
				 stSqlCampos+=   stVirgula + "ID_PROC_ARQUIVAMENTO_TIPO " ;
				 stSqlValores+=   stVirgula + "? " ;
				 ps.adicionarLong(dados.getId_ProcessoArquivamentoTipo());  

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
			
			dados.setId(executarInsert(stSql,"ID_PROC_CRIMINAL",ps)); 
		} 

	//---------------------------------------------------------
		public void alterar(ProcessoCriminalDt dados) throws Exception{

			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			String stSql="";

			stSql= "UPDATE PROJUDI.PROC_CRIMINAL SET  ";
			stSql+= "ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

			stSql+= ",REU_PRESO = ?";		 ps.adicionarBoolean(dados.getReuPreso());  

			stSql+= ",INQUERITO = ?";		 ps.adicionarLong(dados.getInquerito());  

			stSql+= ",DATA_PRISAO = ?";		 ps.adicionarDate(dados.getDataPrisao());  

			stSql+= ",DATA_OFERECIMENTO_DENUNCIA = ?";		 ps.adicionarDate(dados.getDataOferecimentoDenuncia());  

			stSql+= ",DATA_RECEBIMENTO_DENUNCIA = ?";		 ps.adicionarDate(dados.getDataRecebimentoDenuncia());  

			stSql+= ",DATA_TRANSACAO_PENAL = ?";		 ps.adicionarDate(dados.getDataTransacaoPenal());  

			stSql+= ",DATA_SUSPENSAO_PENAL = ?";		 ps.adicionarDate(dados.getDataSuspensaoPenal());  

			stSql+= ",DATA_FATO = ?";		 ps.adicionarDate(dados.getDataFato());
			
			stSql+= ",DATA_PRESCRICAO = ?";		 ps.adicionarDate(dados.getDataPrescricao());
			
			stSql+= ",DATA_BAIXA = ?";		 ps.adicionarDate(dados.getDataBaixa());
			
			stSql+= ",ID_PROC_ARQUIVAMENTO_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoArquivamentoTipo());
			
			stSql+= ",ID_USU_CERT_INCONSISTENCIAS = ?";		 ps.adicionarLong(dados.getIdUsuCertificaInconsistencias());
			
			stSql+= ",CODIGO_TEMP = ?";		 ps.adicionarLong(dados.getCodigoTemp());  

			stSql += " WHERE ID_PROC_CRIMINAL  = ? "; 		ps.adicionarLong(dados.getId()); 

			executarUpdateDelete(stSql,ps);	
		} 
		
		protected void associarDt( ProcessoCriminalDt Dados, ResultSetTJGO rs )  throws Exception {
			Dados.setId(rs.getString("ID_PROC_CRIMINAL"));
			Dados.setProcessoNumero(rs.getString("PROC_NUMERO"));
			Dados.setId_Processo( rs.getString("ID_PROC"));
			Dados.setReuPreso( Funcoes.FormatarLogico(rs.getString("REU_PRESO")));
			Dados.setInquerito( rs.getString("INQUERITO"));
			Dados.setDataPrisao( Funcoes.FormatarData(rs.getDateTime("DATA_PRISAO")));
			Dados.setDataOferecimentoDenuncia( Funcoes.FormatarData(rs.getDateTime("DATA_OFERECIMENTO_DENUNCIA")));
			Dados.setDataRecebimentoDenuncia( Funcoes.FormatarData(rs.getDateTime("DATA_RECEBIMENTO_DENUNCIA")));
			Dados.setDataTransacaoPenal( Funcoes.FormatarData(rs.getDateTime("DATA_TRANSACAO_PENAL")));
			Dados.setDataSuspensaoPenal( Funcoes.FormatarData(rs.getDateTime("DATA_SUSPENSAO_PENAL")));
			Dados.setDataFato( Funcoes.FormatarData(rs.getDateTime("DATA_FATO")));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			Dados.setId_ProcessoArquivamentoTipo( rs.getString("ID_PROC_ARQUIVAMENTO_TIPO"));
			Dados.setProcessoArquivamentoTipo( rs.getString("PROC_ARQUIVAMENTO_TIPO"));			
			Dados.setIdUsuCertificaInconsistencias(rs.getString("ID_USU_CERT_INCONSISTENCIAS"));	
			Dados.setNomeUsuCertificaInconsistencias(rs.getString("NOME_USU_CERT_INCONSISTENCIAS"));
			Dados.setDataPrescricao(Funcoes.FormatarData(rs.getDateTime("DATA_PRESCRICAO")));
			if (rs.contains("DATA_BAIXA")) {
				Dados.setDataBaixa(Funcoes.FormatarData(rs.getDateTime("DATA_BAIXA")));
			}			
		}
}
