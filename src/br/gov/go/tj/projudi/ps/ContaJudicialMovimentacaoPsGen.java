package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.projudi.dt.ContaJudicialMovimentacaoDt;


public class ContaJudicialMovimentacaoPsGen extends Persistencia {


//---------------------------------------------------------
	public ContaJudicialMovimentacaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ContaJudicialMovimentacaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.CONTA_JUDICIAL_MOVI ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_ContaJudicial().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CONTA_JUDICIAL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ContaJudicial());  

			stVirgula=",";
		}
		if ((dados.getNumeroParcela().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_PARCELA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNumeroParcela());  

			stVirgula=",";
		}
		if ((dados.getValor().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getValor());  

			stVirgula=",";
		}
		if ((dados.getData().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getData());  

			stVirgula=",";
		}
		if ((dados.getTipoPagamento().length()>0)) {
			 stSqlCampos+=   stVirgula + "TIPO_PAGAMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTipoPagamento());  

			stVirgula=",";
		}
		if ((dados.getSituacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "SITUACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getSituacao());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_CONTA_JUDICIAL_MOVI",ps));
	} 

//---------------------------------------------------------
	public void alterar(ContaJudicialMovimentacaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.CONTA_JUDICIAL_MOVI SET  ";
		stSql+= "ID_CONTA_JUDICIAL = ?";		 ps.adicionarLong(dados.getId_ContaJudicial());  

		stSql+= ",NUMERO_PARCELA = ?";		 ps.adicionarLong(dados.getNumeroParcela());  

		stSql+= ",VALOR = ?";		 ps.adicionarLong(dados.getValor());  

		stSql+= ",DATA = ?";		 ps.adicionarDateTime(dados.getData());  

		stSql+= ",TIPO_PAGAMENTO = ?";		 ps.adicionarLong(dados.getTipoPagamento());  

		stSql+= ",SITUACAO = ?";		 ps.adicionarLong(dados.getSituacao());  

		stSql += " WHERE ID_CONTA_JUDICIAL_MOVI  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.CONTA_JUDICIAL_MOVI";
		stSql += " WHERE ID_CONTA_JUDICIAL_MOVI = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public ContaJudicialMovimentacaoDt consultarId(String id_contajudicialmovi )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ContaJudicialMovimentacaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_CONTA_JUDICIAL_MOVI WHERE ID_CONTA_JUDICIAL_MOVI = ?";		ps.adicionarLong(id_contajudicialmovi); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ContaJudicialMovimentacaoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return Dados; 
	}

	protected void associarDt( ContaJudicialMovimentacaoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_CONTA_JUDICIAL_MOVI"));
			Dados.setNumeroParcela(rs.getString("NUMERO_PARCELA"));
			Dados.setId_ContaJudicial( rs.getString("ID_CONTA_JUDICIAL"));
			Dados.setValor( rs.getString("VALOR"));
			Dados.setData( Funcoes.FormatarDataHora(rs.getDateTime("DATA")));
			Dados.setTipoPagamento( rs.getString("TIPO_PAGAMENTO"));
			Dados.setSituacao( rs.getString("SITUACAO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			Dados.setId_Deposito( rs.getString("ID_DEPOSITO"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CONTA_JUDICIAL_MOVI, NUMERO_PARCELA FROM PROJUDI.VIEW_CONTA_JUDICIAL_MOVI WHERE NUMERO_PARCELA LIKE ?";
		stSql+= " ORDER BY NUMERO_PARCELA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				ContaJudicialMovimentacaoDt obTemp = new ContaJudicialMovimentacaoDt();
				obTemp.setId(rs1.getString("ID_CONTA_JUDICIAL_MOVI"));
				obTemp.setNumeroParcela(rs1.getString("NUMERO_PARCELA"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CONTA_JUDICIAL_MOVI WHERE NUMERO_PARCELA LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
			return liTemp; 
	}

//---------------------------------------------------------
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CONTA_JUDICIAL_MOVI as id, NUMERO_PARCELA as descricao1 FROM PROJUDI.VIEW_CONTA_JUDICIAL_MOVI WHERE NUMERO_PARCELA LIKE ?";
		stSql+= " ORDER BY NUMERO_PARCELA ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_CONTA_JUDICIAL_MOVI WHERE NUMERO_PARCELA LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
			return stTemp; 
	}

} 
