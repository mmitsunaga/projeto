package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoObjetoArquivoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoObjetoArquivoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 5264266308289514673L;

	//---------------------------------------------------------
	public ProcessoObjetoArquivoPsGen() {


	}



//---------------------------------------------------------
	public void inserir(ProcessoObjetoArquivoDt dados ) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO projudi.PROC_OBJETO_ARQ ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getProcObjetoArq().length()>0)) {
			 stSqlCampos+=   stVirgula + "PROC_OBJETO_ARQ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getProcObjetoArq());  

			stVirgula=",";
		}
		if ((dados.getId_ObjetoSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_OBJETO_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ObjetoSubtipo());  

			stVirgula=",";
		}
		if ((dados.getNomeDepositante().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_DEPOSITANTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNomeDepositante());  

			stVirgula=",";
		}
		if ((dados.getId_Delegacia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_DELEGACIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Delegacia());  

			stVirgula=",";
		}
		if ((dados.getInquerito().length()>0)) {
			 stSqlCampos+=   stVirgula + "INQUERITO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getInquerito());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getCodigoLote().length()>0)) {
			 stSqlCampos+=   stVirgula + "CODIGO_LOTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCodigoLote());  

			stVirgula=",";
		}
		if ((dados.getPlaca().length()>0)) {
			 stSqlCampos+=   stVirgula + "PLACA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPlaca());  

			stVirgula=",";
		}
		if ((dados.getChassi().length()>0)) {
			 stSqlCampos+=   stVirgula + "CHASSI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getChassi());  

			stVirgula=",";
		}
		if ((dados.getId_ServArquivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_ARQUIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServArquivo());  

			stVirgula=",";
		}
		if ((dados.getModulo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MODULO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getModulo());  

			stVirgula=",";
		}
		if ((dados.getPerfil().length()>0)) {
			 stSqlCampos+=   stVirgula + "PERFIL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPerfil());  

			stVirgula=",";
		}
		if ((dados.getNivel().length()>0)) {
			 stSqlCampos+=   stVirgula + "NIVEL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNivel());  

			stVirgula=",";
		}
		if ((dados.getUnidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "UNIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getUnidade());  

			stVirgula=",";
		}
		if ((dados.getLeilao().length()>0)) {
			 stSqlCampos+=   stVirgula + "LEILAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getLeilao());  

			stVirgula=",";
		}
		if ((dados.getStatusLeilao().length()>0)) {
			 stSqlCampos+=   stVirgula + "STATUS_LEILAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getStatusLeilao());  

			stVirgula=",";
		}
		if ((dados.getNumeroRegistro().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_REGISTRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNumeroRegistro());  

			stVirgula=",";
		}
		if ((dados.getDataEntrada().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_ENTRADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataEntrada());  

			stVirgula=",";
		}
		if ((dados.getDataBaixa().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_BAIXA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataBaixa());  

			stVirgula=",";
		}
		if ((dados.getStatusBaixa().length()>0)) {
			 stSqlCampos+=   stVirgula + "STATUS_BAIXA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getStatusBaixa());  

			stVirgula=",";
		}
		if ((dados.getNomeRecebedor().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_RECEBEDOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeRecebedor());  

			stVirgula=",";
		}
		if ((dados.getCpfRecebedor().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF_RECEBEDOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCpfRecebedor());  

			stVirgula=",";
		}
		if ((dados.getRgRecebedor().length()>0)) {
			 stSqlCampos+=   stVirgula + "RG_RECEBEDOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRgRecebedor());  

			stVirgula=",";
		}
		if ((dados.getId_RgOrgaoExpRecebedor().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_RG_ORGAO_EXP_RECEBEDOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_RgOrgaoExpRecebedor());  

			stVirgula=",";
		}
		if ((dados.getId_EnderecoRecebedor().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO_RECEBEDOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EnderecoRecebedor());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 


			dados.setId(executarInsert(stSql,"ID_PROC_OBJETO_ARQ",ps));
			
			if (dados.getCodigoLote().length()==0) {
				dados.setCodigoLote(consultarId(dados.getId()).getCodigoLote());
			}


	} 

//---------------------------------------------------------
	public void alterar(ProcessoObjetoArquivoDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";



		stSql= "UPDATE projudi.PROC_OBJETO_ARQ SET  ";
		stSql+= "PROC_OBJETO_ARQ = ?";		 ps.adicionarString(dados.getProcObjetoArq());  

		stSql+= ",ID_OBJETO_SUBTIPO = ?";		 ps.adicionarLong(dados.getId_ObjetoSubtipo());  
		
		stSql+= ",NOME_DEPOSITANTE = ?";		 ps.adicionarLong(dados.getNomeDepositante());  

		stSql+= ",ID_DELEGACIA = ?";		 ps.adicionarLong(dados.getId_Delegacia());  
		
		stSql+= ",INQUERITO = ?";		 ps.adicionarLong(dados.getInquerito());  

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

		stSql+= ",CODIGO_LOTE = ?";		 ps.adicionarLong(dados.getCodigoLote());  

		stSql+= ",PLACA = ?";		 ps.adicionarString(dados.getPlaca());  

		stSql+= ",CHASSI = ?";		 ps.adicionarString(dados.getChassi());  

		stSql+= ",ID_SERV_ARQUIVO = ?";		 ps.adicionarLong(dados.getId_ServArquivo());  

		stSql+= ",MODULO = ?";		 ps.adicionarLong(dados.getModulo());  

		stSql+= ",PERFIL = ?";		 ps.adicionarLong(dados.getPerfil());  

		stSql+= ",NIVEL = ?";		 ps.adicionarLong(dados.getNivel());  

		stSql+= ",UNIDADE = ?";		 ps.adicionarLong(dados.getUnidade());  

		stSql+= ",LEILAO = ?";		 ps.adicionarLong(dados.getLeilao());  

		stSql+= ",STATUS_LEILAO = ?";		 ps.adicionarString(dados.getStatusLeilao());  

		stSql+= ",NUMERO_REGISTRO = ?";		 ps.adicionarString(dados.getNumeroRegistro());  

		stSql+= ",DATA_ENTRADA = ?";		 ps.adicionarDateTime(dados.getDataEntrada());  
		
		stSql+= ",DATA_BAIXA = ?";		 ps.adicionarDateTime(dados.getDataBaixa());

		stSql+= ",STATUS_BAIXA = ?";		 ps.adicionarLong(dados.getStatusBaixa());  

		stSql+= ",NOME_RECEBEDOR = ?";		 ps.adicionarString(dados.getNomeRecebedor());  

		stSql+= ",CPF_RECEBEDOR = ?";		 ps.adicionarLong(dados.getCpfRecebedor());  

		stSql+= ",RG_RECEBEDOR = ?";		 ps.adicionarString(dados.getRgRecebedor());  

		stSql+= ",ID_RG_ORGAO_EXP_RECEBEDOR = ?";		 ps.adicionarLong(dados.getId_RgOrgaoExpRecebedor());  

		stSql+= ",ID_ENDERECO_RECEBEDOR = ?";		 ps.adicionarLong(dados.getId_EnderecoRecebedor());  

		stSql += " WHERE ID_PROC_OBJETO_ARQ  = ? "; 		ps.adicionarLong(dados.getId()); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "DELETE FROM projudi.PROC_OBJETO_ARQ";
		stSql += " WHERE ID_PROC_OBJETO_ARQ = ?";		ps.adicionarLong(chave); 



			executarUpdateDelete(stSql,ps);


	} 

//---------------------------------------------------------
	public ProcessoObjetoArquivoDt consultarId(String id_procobjetoarq )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoObjetoArquivoDt Dados=null;


		stSql= "SELECT * FROM projudi.VIEW_PROC_OBJETO_ARQ WHERE ID_PROC_OBJETO_ARQ = ?";		ps.adicionarLong(id_procobjetoarq); 



		try{

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoObjetoArquivoDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
			return Dados; 
	}

	protected void associarDt( ProcessoObjetoArquivoDt Dados, ResultSetTJGO rs )  throws Exception {

			Dados.setId(rs.getString("ID_PROC_OBJETO_ARQ"));
			Dados.setProcObjetoArq(rs.getString("PROC_OBJETO_ARQ"));
			Dados.setId_ObjetoSubtipo( rs.getString("ID_OBJETO_SUBTIPO"));
			Dados.setObjetoSubtipo( rs.getString("OBJETO_SUBTIPO"));
			Dados.setNomeDepositante( rs.getString("NOME_DEPOSITANTE"));
			Dados.setId_Delegacia( rs.getString("ID_DELEGACIA"));			
			Dados.setDelegacia( rs.getString("DELEGACIA"));
			Dados.setId_Processo( rs.getString("ID_PROC"));
			Dados.setProcNumero( rs.getString("PROC_NUMERO"));
			Dados.setInquerito( rs.getString("INQUERITO"));
			Dados.setCodigoLote( rs.getString("CODIGO_LOTE"));
			Dados.setPlaca( rs.getString("PLACA"));
			Dados.setChassi( rs.getString("CHASSI"));
			Dados.setId_ServArquivo( rs.getString("ID_SERV_ARQUIVO"));
			Dados.setServentiaarquivo( rs.getString("SERVENTIAARQUIVO"));
			Dados.setModulo( rs.getString("MODULO"));
			Dados.setPerfil( rs.getString("PERFIL"));
			Dados.setNivel( rs.getString("NIVEL"));
			Dados.setUnidade( rs.getString("UNIDADE"));
			Dados.setLeilao( rs.getString("LEILAO"));
			Dados.setStatusLeilao( rs.getString("STATUS_LEILAO"));
			Dados.setNumeroRegistro( rs.getString("NUMERO_REGISTRO"));
			Dados.setDataEntrada( Funcoes.FormatarDataHora(rs.getString("DATA_ENTRADA")));
			Dados.setDataBaixa( Funcoes.FormatarDataHora(rs.getString("DATA_BAIXA")));
			Dados.setStatusBaixa( rs.getString("STATUS_BAIXA"));
			Dados.setNomeRecebedor( rs.getString("NOME_RECEBEDOR"));
			Dados.setCpfRecebedor( rs.getString("CPF_RECEBEDOR"));
			Dados.setRgRecebedor( rs.getString("RG_RECEBEDOR"));
			Dados.setId_RgOrgaoExpRecebedor( rs.getString("ID_RG_ORGAO_EXP_RECEBEDOR"));
			Dados.setRgOrgaoExp( rs.getString("RG_ORGAO_EXP"));
			Dados.setId_EnderecoRecebedor( rs.getString("ID_ENDERECO_RECEBEDOR"));
			Dados.setLogradouro( rs.getString("LOGRADOURO"));
			Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
			Dados.setNumero( rs.getString("NUMERO"));
			Dados.setComplemento( rs.getString("COMPLEMENTO"));
			Dados.setCep( rs.getString("CEP"));
			Dados.setId_Bairro( rs.getString("Id_BAIRRO"));
			Dados.setBairro( rs.getString("BAIRRO"));
			Dados.setCidade( rs.getString("CIDADE"));
			Dados.setUf( rs.getString("UF"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql,stSqlOrder, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_PROC_OBJETO_ARQ, PROC_OBJETO_ARQ ";
		stSqlFrom= " FROM projudi.VIEW_PROC_OBJETO_ARQ WHERE PROC_OBJETO_ARQ LIKE ?";
		stSqlOrder = " ORDER BY PROC_OBJETO_ARQ ";
		ps.adicionarString(descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);


			while (rs1.next()) {
				ProcessoObjetoArquivoDt obTemp = new ProcessoObjetoArquivoDt();
				obTemp.setId(rs1.getString("ID_PROC_OBJETO_ARQ"));
				obTemp.setProcObjetoArq(rs1.getString("PROC_OBJETO_ARQ"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom,ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
			return liTemp; 
	}

} 
