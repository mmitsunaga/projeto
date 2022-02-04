package br.gov.go.tj.projudi.ps;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import br.gov.go.tj.projudi.dt.OficialCertidaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class OficialCertidaoPs extends OficialCertidaoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1977747304272979465L;
	public OficialCertidaoPs(Connection conexao){
		Conexao = conexao;
	}
//
	
	public void inserir(OficialCertidaoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.OFICIAL_CERT ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getModelo().length()>0)) {
			 stSqlCampos+=   stVirgula + "CERT_NOME " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getModelo());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getStatus());  

			stVirgula=",";
		}
		if ((dados.getNumeroMandado().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUM_MANDADO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNumeroMandado());  

			stVirgula=",";
		}
		if ((dados.getDataEmissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EMISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEmissao());  

			stVirgula=",";
		}
		
		if ((dados.getSegredoJustica().length()>0)) {
			 stSqlCampos+=   stVirgula + "SEGREDO_JUSTICA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSegredoJustica());  

			stVirgula=",";
		}
		
		if ((dados.getTexto().length()>0)) {
			 stSqlCampos+=   stVirgula + "TEXTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTexto());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_OFICIAL_CERT",ps));
		dados.setTexto(dados.getTexto().replaceAll("[$][{][\\s]*"+"oficialCertidao.numero"+"[\\s]*[}]", dados.getId()));
//		texto = texto.replaceAll("[$][{][\\s]*"+OFICIAL_CERTIDAO_NUMERO +"[\\s]*[}]", format(oficialCertidaoDt.getId()));
		this.alterar(dados);
	} 

//---------------------------------------------------------
	public void alterar(OficialCertidaoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE PROJUDI.OFICIAL_CERT SET  ";
		

		stSql+= "ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());  
		
		if(dados.getModelo()!= null && !dados.getModelo().equals("")){
			stSql+= ",CERT_NOME = ?";		 ps.adicionarString(dados.getModelo());	
		}
		
		stSql+= ",STATUS = ?";		 ps.adicionarString(dados.getStatus());  

		stSql+= ",NUM_MANDADO = ?";		 ps.adicionarString(dados.getNumeroMandado());  

		stSql+= ",DATA_EMISSAO = ?";		 ps.adicionarDateTime(dados.getDataEmissao());  

		stSql+= ",TEXTO = ?";		 ps.adicionarString(dados.getTexto());  

		stSql += " WHERE ID_OFICIAL_CERT  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM PROJUDI.OFICIAL_CERT";
		stSql += " WHERE ID_OFICIAL_CERT = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

//---------------------------------------------------------
	public OficialCertidaoDt consultarId(String id_oficialcertidao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		OficialCertidaoDt Dados=null;


		stSql= "SELECT * FROM PROJUDI.VIEW_OFICIAL_CERT WHERE ID_OFICIAL_CERT = ?";		
		ps.adicionarLong(id_oficialcertidao); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new OficialCertidaoDt();
				associarDt(Dados, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt( OficialCertidaoDt Dados, ResultSetTJGO rs )  throws Exception {
		
		Dados.setId(rs.getString("ID_OFICIAL_CERT"));
		Dados.setModelo(rs.getString("CERT_NOME"));
		Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
		Dados.setNome( rs.getString("NOME"));
		Dados.setSegredoJustica( rs.getString("SEGREDO_JUSTICA"));
		Dados.setStatus( rs.getString("STATUS"));
		Dados.setNumeroMandado( rs.getString("NUM_MANDADO"));
		Dados.setDataEmissao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_EMISSAO")));
	//	Dados.setDataEmissao( Funcoes.FormatarData(rs.getDate("DATA_EMISSAO")));
		Dados.setTexto( rs.getString("TEXTO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OFICIAL_CERT, CERT_NOME";
		stSqlFrom = " FROM PROJUDI.VIEW_OFICIAL_CERT";
		stSqlFrom += " WHERE CERT_NOME LIKE ?";				ps.adicionarString(descricao+"%");
		stSqlOrder = " ORDER BY CERT_NOME ";
		 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			while (rs1.next()) {
				OficialCertidaoDt obTemp = new OficialCertidaoDt();
				obTemp.setId(rs1.getString("ID_OFICIAL_CERT"));
				obTemp.setModelo(rs1.getString("CERT_NOME"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);

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
	public String consultarDescricaoJSON(String descricao, String id_Usuario, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		//stSql= "SELECT ID_OFICIAL_CERT as id, NUM_MANDADO as descricao1, DATA_EMISSAO as descricao2, CERT_NOME as descricao3 FROM PROJUDI.VIEW_OFICIAL_CERT WHERE NUM_MANDADO LIKE ?";
		stSql= "SELECT ID_OFICIAL_CERT as id, NUM_MANDADO as descricao1, CERT_NOME as descricao2, DATA_EMISSAO as descricao3, STATUS as descricao4, TEXTO as descricao5";
		stSqlFrom = " FROM PROJUDI.VIEW_OFICIAL_CERT";
		stSqlFrom += " WHERE NUM_MANDADO LIKE ?";
		ps.adicionarString("%"+descricao+"%"); 
		
		if(!id_Usuario.equals("") && id_Usuario!=null){
			stSqlFrom += " and ID_USU=?";
			ps.adicionarString(""+id_Usuario+""); 
		}
		
		stSqlOrder = " ORDER BY DATA_EMISSAO ";
		
		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			stSql= "SELECT Count(*) as Quantidade";			
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSONOficialCertidao(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

	public String gerarJSONOficialCertidao( long qtdPaginas, String posicaoAtual, ResultSetTJGO rs, int qtdeColunas) throws Exception{
		StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");
		stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append(qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		stTemp.append(",{\"id\":\"-60000\",\"desc1\":\"").append(posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		while (rs.next()){			
			stTemp.append(",{\"id\":\"").append(rs.getString("Id")).append("\",\"desc1\":\"").append(rs.getString("descricao1"));
				stTemp.append("\",\"desc1" + "\":\"").append(rs.getString("descricao1"));
				//stTemp.append("\",\"desc2\":\"").append(Funcoes.FormatarData(rs.getDate("descricao2")));
				stTemp.append("\",\"desc2\":\"").append(rs.getString("descricao2"));
				stTemp.append("\",\"desc3\":\"").append(Funcoes.FormatarData(rs.getDateTime("descricao3")));
				
				if(rs.getString("descricao4").equals("0")){
//					stTemp.append("\",\"desc4\":\"").append(rs.getString("descricao4"));
					stTemp.append("\",\"desc4\":\"").append("Editar");
				}else stTemp.append("\",\"desc4\":\"").append("Finalizado");
				
			stTemp.append("\"}");
		}
		stTemp.append("]");
		return stTemp.toString();
	}
	
	
	
	
	
	/**
	 * Recupera os dados do mandado no SPG através do número do mandado
	 * @param getNumeroMandado: número do mandado
	 * @return mensagem, quando retorna diferente de vazio é porque ocorreu erro na consulta.
	 * @throws Exception
	 * @author kbsriccioppo/João Paulo / Jelves
	 */
	public String consultarMandado(OficialCertidaoDt OficialCertidaodt) throws Exception{
		String mensagem = "";

		//Criamos uma classe SAXBuilder que vai processar o XML4
		SAXBuilder builder = new SAXBuilder();

		//Faz a consulta no SPG e retorna o xml da consulta
		String xml = this.getXml("http://sv-natweb-p00.tjgo.jus.br/cgi-bin/tjg-guia/FORPSPGI/SPG0010T?mandado=" + OficialCertidaodt.getNumeroMandado());
		Document d = builder.build(new StringReader(xml));
		
		//Recuperamos o elemento root  
		Element mandado = d.getRootElement();

		if(mandado.getAttributeValue("numeroMandado")==null){
			mensagem = "O Número do mandado informado não foi encontrado no SPG.";	
			return mensagem;
		}
		
		//se o numero da guia informado para pesquisa for igual ao retorno xml
		if (OficialCertidaodt.getNumeroMandado().equals(mandado.getAttributeValue("numeroMandado").trim())){
			//processoExecucaoDt.setNumeroGuiaRecolhimento(guiaRecolhimento.getAttributeValue("numeroGuiaRecolhimento").trim());
			List dados = mandado.getChildren();  
			Iterator i = dados.iterator();  
			
			//Limpa o objeto
			OficialCertidaodt.setTexto(""); //Limpa a textarea caso tenha algo
			OficialCertidaodt.setId("");// Limpa o id caso tenha
			String DiligenciaEndereco="";
			//Iteramos com os elementos filhos, e filhos do dos filhos  
			while (i.hasNext()) { 
				Element itensDados = (Element) i.next();
				String nomeNo = itensDados.getName();
				String valorNo = itensDados.getValue().trim();
				if (valorNo.length()>0){
					if (nomeNo.equals("msg")){
						mensagem = valorNo;
						return mensagem;
					}
					else if (nomeNo.equals("numeroMandado")) 				OficialCertidaodt.setNumeroMandado(valorNo);
					else if (nomeNo.equals("numeroProcesso")) 				OficialCertidaodt.setProcessoNumero(valorNo);
					else if (nomeNo.equals("serventia"))				OficialCertidaodt.setServentia(valorNo);
					else if (nomeNo.equals("promovente"))				OficialCertidaodt.setProcessoPromoventeNome(valorNo);
					else if (nomeNo.equals("promovido"))				OficialCertidaodt.setProcessoPromovidoNome(valorNo);
					else if (nomeNo.equals("ruaPromovente"))				OficialCertidaodt.setPromoventeEnderecoLogradouro(valorNo);
					else if (nomeNo.equals("numeroPromovente"))			OficialCertidaodt.setPromoventeEnderecoNumero(valorNo);
					else if (nomeNo.equals("complementoPromovente"))		OficialCertidaodt.setPromoventeEnderecoComplemento(valorNo);
					else if (nomeNo.equals("quadraPromovente"))			OficialCertidaodt.setPromoventeEnderecoQuadra(valorNo);
					else if (nomeNo.equals("lotePromovente"))			OficialCertidaodt.setPromoventeEnderecoLote(valorNo);
					else if (nomeNo.equals("bairroPromovente"))			OficialCertidaodt.setPromoventeEnderecoBairro(valorNo);
					else if (nomeNo.equals("cepPromovente"))				OficialCertidaodt.setPromoventeEnderecoCEP(valorNo);
					else if (nomeNo.equals("cidadePromovente"))			OficialCertidaodt.setPromoventeEnderecoCidade(valorNo);
					else if (nomeNo.equals("estadoPromovente"))				OficialCertidaodt.setPromoventeEnderecoUf(valorNo);
					else if (nomeNo.equals("sigiloso"))				OficialCertidaodt.setSegredoJustica(valorNo);
//					else if (nomeNo.equals("ruaPromovido")&& !valorNo.equals(""))		DiligenciaEndereco +=valorNo;
//					else if (nomeNo.equals("quadraPromovido") && !valorNo.equals("")) 	DiligenciaEndereco +=" Qd:"+valorNo;
//					else if (nomeNo.equals("lotePromovido") && !valorNo.equals(""))		DiligenciaEndereco +=" Lt:"+valorNo;
//					else if (nomeNo.equals("complementoPromovido") && !valorNo.equals(""))		DiligenciaEndereco +=" "+valorNo;
//					else if (nomeNo.equals("bairroPromovido") && !valorNo.equals(""))		DiligenciaEndereco +=" Bairro:"+valorNo;
//					else if (nomeNo.equals("cepPromovido") && !valorNo.equals(""))		DiligenciaEndereco +=" CEP:"+valorNo;
//					else if (nomeNo.equals("cidadePromovido") && !valorNo.equals(""))		DiligenciaEndereco +=" Cidade:"+valorNo;
//					else if (nomeNo.equals("estadoPromovido") && !valorNo.equals(""))		DiligenciaEndereco +=" - "+valorNo;
					else if (nomeNo.equals("notificado"))				OficialCertidaodt.setDiligenciaNomeIntimado(valorNo);
					else if (nomeNo.equals("ruaNotificado")&& !valorNo.equals(""))		DiligenciaEndereco +=valorNo;
					else if (nomeNo.equals("quadraNotificado") && !valorNo.equals("")) 	DiligenciaEndereco +=" Qd:"+valorNo;
					else if (nomeNo.equals("loteNotificado") && !valorNo.equals(""))		DiligenciaEndereco +=" Lt:"+valorNo;
					else if (nomeNo.equals("numeroNotificado") && !valorNo.equals("")) 	DiligenciaEndereco +=" N.:"+valorNo;
					else if (nomeNo.equals("complementoNotificado") && !valorNo.equals(""))		DiligenciaEndereco +=" "+valorNo;
					else if (nomeNo.equals("bairroNotificado") && !valorNo.equals(""))		DiligenciaEndereco +=" Bairro:"+valorNo;
					else if (nomeNo.equals("cepNotificado") && !valorNo.equals(""))		DiligenciaEndereco +=" CEP:"+valorNo;
					else if (nomeNo.equals("cidadeNotificado") && !valorNo.equals(""))		DiligenciaEndereco +=" Cidade:"+valorNo;
					else if (nomeNo.equals("estadoNotificado") && !valorNo.equals(""))		DiligenciaEndereco +=" - "+valorNo;
					}
			}
					OficialCertidaodt.setDiligenciaEndereco(DiligenciaEndereco);
		}
		else {
			mensagem = "O Número do mandado informado não foi encontrado no SPG.";
		}
		return mensagem;
	}
	
	
	
	public List consultarMandados(String numeroMandado, String DataInicial, String DataFinal, String id_Usuario, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OFICIAL_CERT, NUM_MANDADO, CERT_NOME, TEXTO, SEGREDO_JUSTICA, STATUS, DATA_EMISSAO, ID_USU";
		stSqlFrom = " FROM PROJUDI.VIEW_OFICIAL_CERT";
		stSqlFrom += " WHERE";
		
		if ((numeroMandado.length()>0)) {
			stSqlFrom += " NUM_MANDADO LIKE ?";
			ps.adicionarString(numeroMandado+"%");		
		}else{
			stSqlFrom += " DATA_EMISSAO BETWEEN ? and ?";
			ps.adicionarDateTimePrimeiraHoraDia(DataInicial);
			ps.adicionarDateTimeUltimaHoraDia(DataFinal);
		}
		
		//Caso o usuário seja diferente de Coordenador de Mandados, trás somente as certidões dele
		if(!id_Usuario.equals("") && id_Usuario!=null){
			stSqlFrom += " and ID_USU=?";
			ps.adicionarString(""+id_Usuario+""); 
		}
		
		stSqlOrder = " ORDER BY CERT_NOME ";

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				OficialCertidaoDt obTemp = new OficialCertidaoDt();
				obTemp.setId(rs1.getString("ID_OFICIAL_CERT"));
				obTemp.setNumeroMandado(rs1.getString("NUM_MANDADO"));
				obTemp.setModelo(rs1.getString("CERT_NOME"));
				obTemp.setTexto(rs1.getString("TEXTO"));
				obTemp.setSegredoJustica(rs1.getString("SEGREDO_JUSTICA"));
				if(rs1.getString("STATUS").equals("0")){
					obTemp.setStatus("Editar");	
				}else{
					obTemp.setStatus("Finalizado");
				}
				
				obTemp.setDataEmissao(Funcoes.FormatarData(rs1.getDateTime("DATA_EMISSAO")));
				liTemp.add(obTemp);
			}
			
			stSql= "SELECT Count(*) as Quantidade";			
			rs2 = consultar(stSql + stSqlFrom, ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	

	//Usado para fazer a consulta das certidões finalizadas, usando como parâmetros Numero do Mandado, Data Inicial e Data Final - Jelves 15/02/2013
	public String consultarMandadosJSON(String numeroMandado, String DataInicial, String DataFinal, String id_Usuario, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		//stSql= "SELECT ID_OFICIAL_CERT as id, NUM_MANDADO as descricao1, DATA_EMISSAO as descricao2, CERT_NOME as descricao3 FROM PROJUDI.VIEW_OFICIAL_CERT WHERE NUM_MANDADO LIKE ?";
		stSql= "SELECT ID_OFICIAL_CERT as id, NUM_MANDADO as descricao1, CERT_NOME as descricao2, DATA_EMISSAO as descricao3, STATUS as descricao4, TEXTO as descricao5";
		stSqlFrom = " FROM PROJUDI.VIEW_OFICIAL_CERT";
		stSqlFrom += " WHERE";
		
		if ((numeroMandado.length()>0)) {
			stSqlFrom += " NUM_MANDADO LIKE ?";
			ps.adicionarString(numeroMandado+"%");		
		}else{
			stSqlFrom += " DATA_EMISSAO BETWEEN ? and ?";
			ps.adicionarDateTimePrimeiraHoraDia(DataInicial);
			ps.adicionarDateTimeUltimaHoraDia(DataFinal);
		}
		
		if(!id_Usuario.equals("") && id_Usuario!=null){
			stSqlFrom += " and ID_USU=?";
			ps.adicionarString(""+id_Usuario+""); 
		}
		
		stSqlOrder = " ORDER BY DATA_EMISSAO ";
		
		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			stSql= "SELECT Count(*) as Quantidade";			
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSONOficialCertidao(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	
	
	
	public List consultarMandadosSelecionados(String[] idsDados, String id_Usuario, String posicao )throws Exception{

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_OFICIAL_CERT, NUM_MANDADO, CERT_NOME, TEXTO, SEGREDO_JUSTICA, STATUS, DATA_EMISSAO, ID_USU";
		stSqlFrom = " FROM PROJUDI.VIEW_OFICIAL_CERT";
		stSqlFrom += " WHERE ID_OFICIAL_CERT IN( ";

	    for (int i=0; i <idsDados.length - 1; i++) {
		    	stSqlFrom += "?, ";
				ps.adicionarLong(idsDados[i]);
		}
	    stSqlFrom += " ?) ";
	    
		ps.adicionarLong(idsDados[idsDados.length - 1]);
		
	    //ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(listaProcesso.size() - 1)).getId_Processo());
		//stSqlOrder = " ORDER BY CERT_NOME ";

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom, ps, posicao);    //caso o order passe a ser usado, é preciso concatená-lo na chamada: rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao); 

			while (rs1.next()) {
				OficialCertidaoDt obTemp = new OficialCertidaoDt();
				obTemp.setId(rs1.getString("ID_OFICIAL_CERT"));
				obTemp.setNumeroMandado(rs1.getString("NUM_MANDADO"));
				obTemp.setModelo(rs1.getString("CERT_NOME"));
				obTemp.setTexto(rs1.getString("TEXTO"));
				obTemp.setSegredoJustica(rs1.getString("SEGREDO_JUSTICA"));
				if(rs1.getString("STATUS").equals("0")){
					obTemp.setStatus("Editar");	
				}else{
					obTemp.setStatus("Finalizado");
				}
				
				obTemp.setDataEmissao(Funcoes.FormatarData(rs1.getDateTime("DATA_EMISSAO")));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";		    
			rs2 = consultar(stSql + stSqlFrom, ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	
	
	
	public String getXml(String HttpGetUrl)throws Exception{
		try(DefaultHttpClient httpclient = new DefaultHttpClient()){
			HttpGet httpget = new HttpGet(HttpGetUrl);
			//httpget.getParams().setParameter("http.socket.timeout",new Integer(120000));
		
			HttpResponse response;
			String xml = null;
			
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				xml = EntityUtils.toString(entity);
			}
	
			return xml;
		}
	}
}
