package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ProcessoPartePsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8893593125459970782L;

	//---------------------------------------------------------
	public ProcessoPartePsGen() {

	}



//---------------------------------------------------------
	public void inserir(ProcessoParteDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.PROC_PARTE ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getNome().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNome());  

			stVirgula=",";
		}
		if ((dados.getSexo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SEXO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSexo());  

			stVirgula=",";
		}
		if ((dados.getNomeMae().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_MAE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomeMae());  

			stVirgula=",";
		}
		if ((dados.getNomePai().length()>0)) {
			 stSqlCampos+=   stVirgula + "NOME_PAI " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNomePai());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoParteTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParteTipo());  

			stVirgula=",";
		}
		if ((dados.getId_Processo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Processo());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoParteAusencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE_AUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParteAusencia());  

			stVirgula=",";
		}
		if ((dados.getId_Naturalidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_NATURALIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Naturalidade());  

			stVirgula=",";
		}
		if ((dados.getDataNascimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_NASCIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataNascimento());  

			stVirgula=",";
		}
		if ((dados.getId_EstadoCivil().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESTADO_CIVIL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EstadoCivil());  

			stVirgula=",";
		}
		if ((dados.getId_Profissao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROFISSAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Profissao());  

			stVirgula=",";
		}
		if ((dados.getId_Endereco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Endereco());  

			stVirgula=",";
		}
		if ((dados.getRg().length()>0)) {
			 stSqlCampos+=   stVirgula + "RG " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRg());  

			stVirgula=",";
		}
		if ((dados.getId_RgOrgaoExpedidor().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_RG_ORGAO_EXP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_RgOrgaoExpedidor());  

			stVirgula=",";
		}
		if ((dados.getRgDataExpedicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "RG_DATA_EXPEDICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getRgDataExpedicao());  

			stVirgula=",";
		}
		if ((dados.getId_Escolaridade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESCOLARIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Escolaridade());  

			stVirgula=",";
		}
		if ((dados.getCpf().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCpf());  

			stVirgula=",";
		}
		if ((dados.getCnpj().length()>0)) {
			 stSqlCampos+=   stVirgula + "CNPJ " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCnpj());  

			stVirgula=",";
		}
		if ((dados.getTituloEleitor().length()>0)) {
			 stSqlCampos+=   stVirgula + "TITULO_ELEITOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTituloEleitor());  

			stVirgula=",";
		}
		if ((dados.getTituloEleitorZona().length()>0)) {
			 stSqlCampos+=   stVirgula + "TITULO_ELEITOR_ZONA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTituloEleitorZona());  

			stVirgula=",";
		}
		if ((dados.getTituloEleitorSecao().length()>0)) {
			 stSqlCampos+=   stVirgula + "TITULO_ELEITOR_SECAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getTituloEleitorSecao());  

			stVirgula=",";
		}
		if ((dados.getCtps().length()>0)) {
			 stSqlCampos+=   stVirgula + "CTPS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCtps());  

			stVirgula=",";
		}
		if ((dados.getCtpsSerie().length()>0)) {
			 stSqlCampos+=   stVirgula + "CTPS_SERIE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getCtpsSerie());  

			stVirgula=",";
		}
		if ((dados.getId_CtpsUf().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CTPS_UF " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CtpsUf());  

			stVirgula=",";
		}
		if ((dados.getPis().length()>0)) {
			 stSqlCampos+=   stVirgula + "PIS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getPis());  

			stVirgula=",";
		}
		if ((dados.getDataCadastro().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_CADASTRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataCadastro());  

			stVirgula=",";
		}
		if ((dados.getEMail().length()>0)) {
			 stSqlCampos+=   stVirgula + "EMAIL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEMail());  

			stVirgula=",";
		}
		if ((dados.getTelefone().length()>0)) {
			 stSqlCampos+=   stVirgula + "TELEFONE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTelefone());  

			stVirgula=",";
		}
		if ((dados.getCelular().length()>0)) {
			 stSqlCampos+=   stVirgula + "CELULAR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCelular());  

			stVirgula=",";
		}
		if ((dados.getCitacaoOnline().length()>0)) {
			 stSqlCampos+=   stVirgula + "CITACAO_ONLINE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getCitacaoOnline());  

			stVirgula=",";
		}
		if ((dados.getIntimacaoOnline().length()>0)) {
			 stSqlCampos+=   stVirgula + "INTIMACAO_ONLINE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getIntimacaoOnline());  

			stVirgula=",";
		}
		if ((dados.getRecebeEMail().length()>0)) {
			 stSqlCampos+=   stVirgula + "RECEBE_EMAIL " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getRecebeEMail());  

			stVirgula=",";
		}
		if ((dados.getDataBaixa().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_BAIXA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataBaixa());  

			stVirgula=",";
		}
		if ((dados.getCitada().length()>0)) {
			 stSqlCampos+=   stVirgula + "CITADA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getCitada());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia());  

			stVirgula=",";
		}
		if ((dados.getId_GovernoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GOVERNO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GovernoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_EmpresaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_EMPRESA_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EmpresaTipo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_PROC_PARTE",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ProcessoParteDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psProcessoPartealterar()");

		stSql= "UPDATE PROJUDI.PROC_PARTE SET  ";
		stSql+= "NOME = ?";		 ps.adicionarString(dados.getNome());  

		stSql+= ",SEXO = ?";		 ps.adicionarString(dados.getSexo());  

		stSql+= ",NOME_MAE = ?";		 ps.adicionarString(dados.getNomeMae());  

		stSql+= ",NOME_PAI = ?";		 ps.adicionarString(dados.getNomePai());  

		stSql+= ",ID_PROC_PARTE_TIPO = ?";		 ps.adicionarLong(dados.getId_ProcessoParteTipo());  

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());  

		stSql+= ",ID_PROC_PARTE_AUS = ?";		 ps.adicionarLong(dados.getId_ProcessoParteAusencia());  

		stSql+= ",ID_NATURALIDADE = ?";		 ps.adicionarLong(dados.getId_Naturalidade());  

		stSql+= ",DATA_NASCIMENTO = ?";		 ps.adicionarDate(dados.getDataNascimento());  

		stSql+= ",ID_ESTADO_CIVIL = ?";		 ps.adicionarLong(dados.getId_EstadoCivil());  

		stSql+= ",ID_PROFISSAO = ?";		 ps.adicionarLong(dados.getId_Profissao());  

		stSql+= ",ID_ENDERECO = ?";		 ps.adicionarLong(dados.getId_Endereco());  

		stSql+= ",RG = ?";		 ps.adicionarString(dados.getRg());  

		stSql+= ",ID_RG_ORGAO_EXP = ?";		 ps.adicionarLong(dados.getId_RgOrgaoExpedidor());  

		stSql+= ",RG_DATA_EXPEDICAO = ?";		 ps.adicionarDate(dados.getRgDataExpedicao());  

		stSql+= ",ID_ESCOLARIDADE = ?";		 ps.adicionarLong(dados.getId_Escolaridade());  

		stSql+= ",CPF = ?";		 ps.adicionarLong(dados.getCpf());  

		stSql+= ",CNPJ = ?";		 ps.adicionarLong(dados.getCnpj());  

		stSql+= ",TITULO_ELEITOR = ?";		 ps.adicionarString(dados.getTituloEleitor());  

		stSql+= ",TITULO_ELEITOR_ZONA = ?";		 ps.adicionarLong(dados.getTituloEleitorZona());  

		stSql+= ",TITULO_ELEITOR_SECAO = ?";		 ps.adicionarLong(dados.getTituloEleitorSecao());  

		stSql+= ",CTPS = ?";		 ps.adicionarLong(dados.getCtps());  

		stSql+= ",CTPS_SERIE = ?";		 ps.adicionarLong(dados.getCtpsSerie());  

		stSql+= ",ID_CTPS_UF = ?";		 ps.adicionarLong(dados.getId_CtpsUf());  

		stSql+= ",PIS = ?";		 ps.adicionarString(dados.getPis());  

		stSql+= ",DATA_CADASTRO = ?";		 ps.adicionarDateTime(dados.getDataCadastro());  

		stSql+= ",EMAIL = ?";		 ps.adicionarString(dados.getEMail());  

		stSql+= ",TELEFONE = ?";		 ps.adicionarString(dados.getTelefone());  

		stSql+= ",CELULAR = ?";		 ps.adicionarString(dados.getCelular());  

		stSql+= ",CITACAO_ONLINE = ?";		 ps.adicionarBoolean(dados.getCitacaoOnline());  

		stSql+= ",INTIMACAO_ONLINE = ?";		 ps.adicionarBoolean(dados.getIntimacaoOnline());  

		stSql+= ",RECEBE_EMAIL = ?";		 ps.adicionarBoolean(dados.getRecebeEMail());  

		stSql+= ",DATA_BAIXA = ?";		 ps.adicionarDateTime(dados.getDataBaixa());  

		stSql+= ",CITADA = ?";		 ps.adicionarBoolean(dados.getCitada());  

		stSql+= ",ID_USU_SERV = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia());
		
		stSql+= ",ID_GOVERNO_TIPO = ?";		 ps.adicionarLong(dados.getId_GovernoTipo());  
		
		stSql+= ",ID_EMPRESA_TIPO = ?";		 ps.adicionarLong(dados.getId_EmpresaTipo());  

		stSql += " WHERE ID_PROC_PARTE  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psProcessoParteexcluir()");

		stSql= "DELETE FROM PROJUDI.PROC_PARTE";
		stSql += " WHERE ID_PROC_PARTE = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ProcessoParteDt consultarId(String id_processoparte )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoParteDt Dados=null;
		////System.out.println("....ps-ConsultaId_ProcessoParte)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE WHERE ID_PROC_PARTE = ?";		ps.adicionarLong(id_processoparte); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ProcessoParte  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ProcessoParteDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ProcessoParteDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_PROC_PARTE"));
		Dados.setNome(rs.getString("NOME"));
		Dados.setSexo( rs.getString("SEXO"));
		Dados.setNomeMae( rs.getString("NOME_MAE"));
		Dados.setNomePai( rs.getString("NOME_PAI"));
		Dados.setId_ProcessoParteTipo( rs.getString("ID_PROC_PARTE_TIPO"));
		Dados.setProcessoParteTipo( rs.getString("PROC_PARTE_TIPO"));
		Dados.setId_Processo( rs.getString("ID_PROC"));
		Dados.setProcessoNumero( rs.getString("PROC_NUMERO"));
		Dados.setId_ProcessoParteAusencia( rs.getString("ID_PROC_PARTE_AUS"));
		Dados.setProcessoParteAusencia( rs.getString("PROC_PARTE_AUS"));
		Dados.setId_Naturalidade( rs.getString("ID_NATURALIDADE"));
		Dados.setCidadeNaturalidade( rs.getString("CIDADE_NATURALIDADE"));
		Dados.setDataNascimento( Funcoes.FormatarData(rs.getDateTime("DATA_NASCIMENTO")));
		Dados.setId_EstadoCivil( rs.getString("ID_ESTADO_CIVIL"));
		Dados.setEstadoCivil( rs.getString("ESTADO_CIVIL"));
		Dados.setId_Profissao( rs.getString("ID_PROFISSAO"));
		Dados.setProfissao( rs.getString("PROFISSAO"));
		Dados.setId_Endereco( rs.getString("ID_ENDERECO"));
		Dados.setEndereco( rs.getString("ENDERECO"));
		Dados.setRg( rs.getString("RG"));
		Dados.setId_RgOrgaoExpedidor( rs.getString("ID_RG_ORGAO_EXP"));
		Dados.setRgOrgaoExpedidor( rs.getString("RG_ORGAO_EXP"));
		Dados.setRgDataExpedicao( Funcoes.FormatarData(rs.getDateTime("RG_DATA_EXPEDICAO")));
		Dados.setId_Escolaridade( rs.getString("ID_ESCOLARIDADE"));
		Dados.setEscolaridade( rs.getString("ESCOLARIDADE"));
		Dados.setCpf( rs.getString("CPF"));
		Dados.setCnpj( rs.getString("CNPJ"));
		Dados.setTituloEleitor( rs.getString("TITULO_ELEITOR"));
		Dados.setTituloEleitorZona( rs.getString("TITULO_ELEITOR_ZONA"));
		Dados.setTituloEleitorSecao( rs.getString("TITULO_ELEITOR_SECAO"));
		Dados.setCtps( rs.getString("CTPS"));
		Dados.setCtpsSerie( rs.getString("CTPS_SERIE"));
		Dados.setId_CtpsUf( rs.getString("ID_CTPS_UF"));
		Dados.setEstadoCtpsUf( rs.getString("ESTADO_CTPS_UF"));
		Dados.setPis( rs.getString("PIS"));
		Dados.setDataCadastro( Funcoes.FormatarDataHora(rs.getDateTime("DATA_CADASTRO")));
		Dados.setEMail( rs.getString("EMAIL"));
		Dados.setTelefone( rs.getString("TELEFONE"));
		Dados.setCelular( rs.getString("CELULAR"));
		Dados.setCitacaoOnline( Funcoes.FormatarLogico(rs.getString("CITACAO_ONLINE")));
		Dados.setIntimacaoOnline( Funcoes.FormatarLogico(rs.getString("INTIMACAO_ONLINE")));
		Dados.setRecebeEMail( Funcoes.FormatarLogico(rs.getString("RECEBE_EMAIL")));
		Dados.setDataBaixa( Funcoes.FormatarDataHora(rs.getDateTime("DATA_BAIXA")));
		Dados.setCitada( Funcoes.FormatarLogico(rs.getString("CITADA")));
		Dados.setId_UsuarioServentia( rs.getString("ID_USU_SERV"));
		Dados.setUsuario( rs.getString("USU"));
		Dados.setId_GovernoTipo( rs.getString("ID_GOVERNO_TIPO"));
		Dados.setGovernoTipo( rs.getString("GOVERNO_TIPO"));
		Dados.setId_EmpresaTipo( rs.getString("ID_EMPRESA_TIPO"));
		Dados.setEmpresaTipo( rs.getString("EMPRESA_TIPO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setProcessoParteTipoCodigo( rs.getString("PROC_PARTE_TIPO_CODIGO"));
		Dados.setProcessoParteAusenciaCodigo( rs.getString("PROC_PARTE_AUS_CODIGO"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoProcessoParte()");

		stSql= "SELECT ID_PROC_PARTE, NOME FROM PROJUDI.VIEW_PROC_PARTE WHERE NOME LIKE ?"; ps.adicionarString(descricao+"%"); 
		stSql+= " ORDER BY NOME ";
		

		try{
			////System.out.println("..ps-ConsultaDescricaoProcessoParte  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ProcessoParteDt obTemp = new ProcessoParteDt();
				obTemp.setId(rs1.getString("ID_PROC_PARTE"));
				obTemp.setNome(rs1.getString("NOME"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_PROC_PARTE WHERE NOME LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ProcessoPartePsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
