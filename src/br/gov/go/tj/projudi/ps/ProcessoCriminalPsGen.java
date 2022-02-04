package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoCriminalDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoCriminalPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6690265515678457988L;

	//---------------------------------------------------------
	public ProcessoCriminalPsGen() {

     
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

		//System.out.println("....psProcessoCriminalalterar()");

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
		
		stSql+= ",CODIGO_TEMP = ?";		 ps.adicionarLong(dados.getCodigoTemp());  

		stSql += " WHERE ID_PROC_CRIMINAL  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("....psProcessoCriminalexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_CRIMINAL";
		stSql += " WHERE ID_PROC_CRIMINAL = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public ProcessoCriminalDt consultarId(String id_processocriminal )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoCriminalDt Dados=null;
		//System.out.println("....ps-ConsultaId_ProcessoCriminal)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_CRIMINAL WHERE ID_PROC_CRIMINAL = ?";		ps.adicionarLong(id_processocriminal); 

		//System.out.println("....Sql  " + stSql  );

		try{
			//System.out.println("..ps-ConsultaId_ProcessoCriminal  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoCriminalDt();
				associarDt(Dados, rs1);
			}
			//System.out.println("..ps-ConsultaId");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
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
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//System.out.println("..ps-ConsultaDescricaoProcessoCriminal()");

		stSql= "SELECT ID_PROC_CRIMINAL, PROC_NUMERO FROM PROJUDI.VIEW_PROC_CRIMINAL WHERE PROC_NUMERO LIKE ?";
		stSql+= " ORDER BY PROC_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			//System.out.println("..ps-ConsultaDescricaoProcessoCriminal  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			//System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoCriminalDt obTemp = new ProcessoCriminalDt();
				obTemp.setId(rs1.getString("ID_PROC_CRIMINAL"));
				obTemp.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_PROC_CRIMINAL WHERE PROC_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);
			//System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}
			//System.out.println("..ProcessoCriminalPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
