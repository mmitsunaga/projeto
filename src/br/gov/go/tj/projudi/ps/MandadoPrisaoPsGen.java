package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoPrisaoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class MandadoPrisaoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -7188555682872572488L;

	//---------------------------------------------------------
	public MandadoPrisaoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(MandadoPrisaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.MANDADO_PRISAO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getMandadoPrisaoNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_NUMERO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMandadoPrisaoNumero());  

			stVirgula=",";
		}
//		if ((dados.getDataDelito().length()>0)) {
//			 stSqlCampos+=   stVirgula + "DATA_DELITO " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarDateTime(dados.getDataDelito());  
//
//			stVirgula=",";
//		}
		if ((dados.getDataExpedicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EXPEDICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataExpedicao());  

			stVirgula=",";
		}
//		if ((dados.getMandadoPrisaoNumeroAnterior().length()>0)) {
//			 stSqlCampos+=   stVirgula + "MANDADO_PRISAO_NUMERO_ANTERIOR " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarLong(dados.getMandadoPrisaoNumeroAnterior());  
//
//			stVirgula=",";
//		}
		if ((dados.getPenaImposta().length()>0)) {
			 stSqlCampos+=   stVirgula + "PENA_IMPOSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPenaImposta());  

			stVirgula=",";
		}
		if ((dados.getPrazoPrisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "PRAZO_PRISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPrazoPrisao());  

			stVirgula=",";
		}
//		if ((dados.getRecaptura().length()>0)) {
//			 stSqlCampos+=   stVirgula + "RECAPTURA " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarLong(dados.getRecaptura());  
//
//			stVirgula=",";
//		}
		if ((dados.getSinteseDecisao().length()>0)) {
			 stSqlCampos+=   stVirgula + "SINTESE_DECISAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSinteseDecisao());  

			stVirgula=",";
		}
		if ((dados.getId_MandadoPrisaoStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MANDADO_PRISAO_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MandadoPrisaoStatus());  

			stVirgula=",";
		}
		if ((dados.getValorFianca().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR_FIANCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getValorFianca());  

			stVirgula=",";
		}
		if ((dados.getId_RegimeExecucao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_REGIME_EXE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_RegimeExecucao());  

			stVirgula=",";
		}
		if ((dados.getId_PrisaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PRISAO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_PrisaoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_Assunto().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ASSUNTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Assunto());  

			stVirgula=",";
		}
		if ((dados.getDataValidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_VALIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataValidade());  

			stVirgula=",";
		}
		if ((dados.getOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getOrigem());  

			stVirgula=",";
		}
		if ((dados.getId_MandadoPrisaoOrigem().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MANDADO_PRISAO_ORIGEM " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MandadoPrisaoOrigem());  

			stVirgula=",";
		}
//		if ((dados.getAssuntoDelitoPrincipal().length()>0)) {
//			 stSqlCampos+=   stVirgula + "ASSUNTO_DELITO_PRINCIPAL " ;
//			 stSqlValores+=   stVirgula + "? " ;
//			 ps.adicionarString(dados.getAssuntoDelitoPrincipal());  
//
//			stVirgula=",";
//		}
		
		stSqlCampos+=   stVirgula + "DATA_ATUALIZACAO " ;
		stSqlValores+=   stVirgula + "? " ;
		ps.adicionarDateTime(new Date());  

		stVirgula=",";

		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_MANDADO_PRISAO",ps));
	} 

//---------------------------------------------------------
	public void alterar(MandadoPrisaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.MANDADO_PRISAO SET  ";
		stSql+= "MANDADO_PRISAO_NUMERO = ?";		 ps.adicionarLong(dados.getMandadoPrisaoNumero());  

//		stSql+= ",DATA_DELITO = ?";		 ps.adicionarDateTime(dados.getDataDelito());  

		stSql+= ",DATA_EXPEDICAO = ?";		 ps.adicionarDateTime(dados.getDataExpedicao());  

//		stSql+= ",MANDADO_PRISAO_NUMERO_ANTERIOR = ?";		 ps.adicionarLong(dados.getMandadoPrisaoNumeroAnterior());  

		stSql+= ",PENA_IMPOSTA = ?";		 ps.adicionarLong(dados.getPenaImposta());  

		stSql+= ",PRAZO_PRISAO = ?";		 ps.adicionarString(dados.getPrazoPrisao());  

//		stSql+= ",RECAPTURA = ?";		 ps.adicionarLong(dados.getRecaptura());  

		stSql+= ",SINTESE_DECISAO = ?";		 ps.adicionarString(dados.getSinteseDecisao());  

		stSql+= ",ID_MANDADO_PRISAO_STATUS = ?";		 ps.adicionarLong(dados.getId_MandadoPrisaoStatus());  

		stSql+= ",VALOR_FIANCA = ?";		 ps.adicionarLong(dados.getValorFianca());  

		stSql+= ",ID_REGIME_EXE = ?";		 ps.adicionarLong(dados.getId_RegimeExecucao());  

		stSql+= ",ID_PRISAO_TIPO = ?";		 ps.adicionarLong(dados.getId_PrisaoTipo());  

		stSql+= ",ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_ASSUNTO = ?";		 ps.adicionarLong(dados.getId_Assunto());  

		stSql+= ",DATA_VALIDADE = ?";		 ps.adicionarDateTime(dados.getDataValidade());  

		stSql+= ",ORIGEM = ?";		 ps.adicionarString(dados.getOrigem());
		
//		stSql+= ",ASSUNTO_DELITO_PRINCIPAL = ?";		 ps.adicionarString(dados.getAssuntoDelitoPrincipal());

		stSql+= ",ID_MANDADO_PRISAO_ORIGEM = ?";		 ps.adicionarLong(dados.getId_MandadoPrisaoOrigem());  

		stSql += " WHERE ID_MANDADO_PRISAO  = ? "; 		ps.adicionarLong(dados.getId()); 


		executarUpdateDelete(stSql,ps);	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.MANDADO_PRISAO";
		stSql += " WHERE ID_MANDADO_PRISAO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public MandadoPrisaoDt consultarId(String id_mandadoprisao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoPrisaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_MANDADO_PRISAO WHERE ID_MANDADO_PRISAO = ?";		ps.adicionarLong(id_mandadoprisao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoPrisaoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( MandadoPrisaoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_MANDADO_PRISAO"));
		Dados.setMandadoPrisaoNumero(rs.getString("MANDADO_PRISAO_NUMERO"));
//		Dados.setDataDelito( Funcoes.FormatarDataHora(rs.getDate("DATA_DELITO")));
		Dados.setDataExpedicao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_EXPEDICAO")));
//		Dados.setMandadoPrisaoNumeroAnterior( rs.getString("MANDADO_PRISAO_NUMERO_ANTERIOR"));
		Dados.setPenaImposta( rs.getString("PENA_IMPOSTA"));
		Dados.setPrazoPrisao( rs.getString("PRAZO_PRISAO"));
//		Dados.setRecaptura( rs.getString("RECAPTURA"));
		Dados.setSinteseDecisao( rs.getString("SINTESE_DECISAO"));
		Dados.setId_MandadoPrisaoStatus( rs.getString("ID_MANDADO_PRISAO_STATUS"));
		Dados.setMandadoPrisaoStatus( rs.getString("MANDADO_PRISAO_STATUS"));
		Dados.setValorFianca( rs.getString("VALOR_FIANCA"));
		Dados.setId_RegimeExecucao( rs.getString("ID_REGIME_EXE"));
		Dados.setRegimeExecucao( rs.getString("REGIME_EXE"));
		Dados.setId_PrisaoTipo( rs.getString("ID_PRISAO_TIPO"));
		Dados.setPrisaoTipo( rs.getString("PRISAO_TIPO"));
		Dados.setId_ProcessoParte( rs.getString("ID_PROC_PARTE"));
		Dados.setProcessoParte( rs.getString("NOME"));
		Dados.setId_Assunto( rs.getString("ID_ASSUNTO"));
		Dados.setAssunto( rs.getString("ASSUNTO"));
		Dados.setDataValidade( Funcoes.FormatarDataHora(rs.getDateTime("DATA_VALIDADE")));
		Dados.setOrigem( rs.getString("ORIGEM"));
		Dados.setId_MandadoPrisaoOrigem( rs.getString("ID_MANDADO_PRISAO_ORIGEM"));
		Dados.setMandadoPrisaoOrigem( rs.getString("MANDADO_PRISAO_ORIGEM"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setProcessoTipo( rs.getString("PROC_TIPO"));
		Dados.setProcessoNumero( rs.getString("PROC_NUMERO"));
		Dados.setDigitoVerificador( rs.getString("DIGITO_VERIFICADOR"));
		Dados.setAno( rs.getString("ANO"));
		Dados.setDataNascimento( Funcoes.FormatarDataHora(rs.getDateTime("DATA_NASCIMENTO")));
		Dados.setNomeMae( rs.getString("NOME_MAE"));
		Dados.setNomePai( rs.getString("NOME_PAI"));
		Dados.setUfNaturalidade( rs.getString("NATURALIDADE_UF"));
		Dados.setSexo( rs.getString("SEXO"));
		Dados.setCpf( rs.getString("CPF"));
		Dados.setNaturalidade( rs.getString("NATURALIDADE"));
		Dados.setDataAtualizacao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_ATUALIZACAO")));
		Dados.setLocalRecolhimento( rs.getString("LOCAL_RECOLHIMENTO"));
		Dados.setNumeroOrigem( rs.getString("NUMERO_ORIGEM"));
//		Dados.setAssuntoDelitoPrincipal( rs.getString("ASSUNTO_DELITO_PRINCIPAL"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_MANDADO_PRISAO, MANDADO_PRISAO_NUMERO FROM PROJUDI.VIEW_MANDADO_PRISAO WHERE MANDADO_PRISAO_NUMERO LIKE ?";
		stSql+= " ORDER BY MANDADO_PRISAO_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);


			while (rs1.next()) {
				MandadoPrisaoDt obTemp = new MandadoPrisaoDt();
				obTemp.setId(rs1.getString("ID_MANDADO_PRISAO"));
				obTemp.setMandadoPrisaoNumero(rs1.getString("MANDADO_PRISAO_NUMERO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MANDADO_PRISAO WHERE MANDADO_PRISAO_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		} finally {
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


		stSql= "SELECT ID_MANDADO_PRISAO as id, MANDADO_PRISAO_NUMERO as descricao1 FROM PROJUDI.VIEW_MANDADO_PRISAO WHERE MANDADO_PRISAO_NUMERO LIKE ?";
		stSql+= " ORDER BY MANDADO_PRISAO_NUMERO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_MANDADO_PRISAO WHERE MANDADO_PRISAO_NUMERO LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

} 
