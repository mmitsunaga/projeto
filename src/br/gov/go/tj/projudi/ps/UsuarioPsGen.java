package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class UsuarioPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -8508457949990316166L;

	//---------------------------------------------------------
	public UsuarioPsGen() {

	}



//---------------------------------------------------------
	public void inserir(UsuarioDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.USU ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getUsuario().length()>0)) {
			 stSqlCampos+=   stVirgula + "USU " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getUsuario());  

			stVirgula=",";
		}
		if ((dados.getId_CtpsUf().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CTPS_UF " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_CtpsUf());  

			stVirgula=",";
		}
		if ((dados.getId_Naturalidade().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_NATURALIDADE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Naturalidade());  

			stVirgula=",";
		}
		if ((dados.getId_Endereco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Endereco());  

			stVirgula=",";
		}
		if ((dados.getId_RgOrgaoExpedidor().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_RG_ORGAO_EXP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_RgOrgaoExpedidor());  

			stVirgula=",";
		}		
		if ((dados.getSenha().length()>0)) {
			 stSqlCampos+=   stVirgula + "SENHA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getSenha());  

			stVirgula=",";
		}
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
		if ((dados.getDataNascimento().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_NASCIMENTO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataNascimento());  

			stVirgula=",";
		}
		if ((dados.getRg().length()>0)) {
			 stSqlCampos+=   stVirgula + "RG " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getRg());  

			stVirgula=",";
		}
		if ((dados.getRgDataExpedicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "RG_DATA_EXPEDICAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getRgDataExpedicao());  

			stVirgula=",";
		}
		if ((dados.getCpf().length()>0)) {
			 stSqlCampos+=   stVirgula + "CPF " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getCpf());  

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
		if ((dados.getPis().length()>0)) {
			 stSqlCampos+=   stVirgula + "PIS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getPis());  

			stVirgula=",";
		}
		if ((dados.getMatriculaTjGo().length()>0)) {
			 stSqlCampos+=   stVirgula + "MATRICULA_TJGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getMatriculaTjGo());  

			stVirgula=",";
		}
		if ((dados.getNumeroConciliador().length()>0)) {
			 stSqlCampos+=   stVirgula + "NUMERO_CONCILIADOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNumeroConciliador());  

			stVirgula=",";
		}
		if ((dados.getDataCadastro().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_CADASTRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataCadastro());  

			stVirgula=",";
		}
		if ((dados.getAtivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAtivo());  

			stVirgula=",";
		}
		if ((dados.getDataAtivo().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_ATIVO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataAtivo());  

			stVirgula=",";
		}
		if ((dados.getDataExpiracao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_EXPIRACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDateTime(dados.getDataExpiracao());  

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
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_USU",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(UsuarioDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psUsuarioalterar()");

		stSql= "UPDATE PROJUDI.USU SET  ";
		stSql+= "USU = ?";		 ps.adicionarString(dados.getUsuario());  

		stSql+= ",ID_CTPS_UF = ?";		 ps.adicionarLong(dados.getId_CtpsUf());  

		stSql+= ",ID_NATURALIDADE = ?";		 ps.adicionarLong(dados.getId_Naturalidade());  

		stSql+= ",ID_ENDERECO = ?";		 ps.adicionarLong(dados.getId_Endereco());  

		stSql+= ",ID_RG_ORGAO_EXP = ?";		 ps.adicionarLong(dados.getId_RgOrgaoExpedidor());

		stSql+= ",SENHA = ?";		 ps.adicionarString(dados.getSenha());  

		stSql+= ",NOME = ?";		 ps.adicionarString(dados.getNome());  

		stSql+= ",SEXO = ?";		 ps.adicionarString(dados.getSexo());  

		stSql+= ",DATA_NASCIMENTO = ?";		 ps.adicionarDate(dados.getDataNascimento());  

		stSql+= ",RG = ?";		 ps.adicionarString(dados.getRg());  

		stSql+= ",RG_DATA_EXPEDICAO = ?";		 ps.adicionarDate(dados.getRgDataExpedicao());  

		stSql+= ",CPF = ?";		 ps.adicionarString(dados.getCpf());  

		stSql+= ",TITULO_ELEITOR = ?";		 ps.adicionarString(dados.getTituloEleitor());  

		stSql+= ",TITULO_ELEITOR_ZONA = ?";		 ps.adicionarLong(dados.getTituloEleitorZona());  

		stSql+= ",TITULO_ELEITOR_SECAO = ?";		 ps.adicionarLong(dados.getTituloEleitorSecao());  

		stSql+= ",CTPS = ?";		 ps.adicionarLong(dados.getCtps());  

		stSql+= ",CTPS_SERIE = ?";		 ps.adicionarLong(dados.getCtpsSerie());  

		stSql+= ",PIS = ?";		 ps.adicionarLong(dados.getPis());  

		stSql+= ",MATRICULA_TJGO = ?";		 ps.adicionarLong(dados.getMatriculaTjGo());  

		stSql+= ",NUMERO_CONCILIADOR = ?";		 ps.adicionarLong(dados.getNumeroConciliador());  

		stSql+= ",DATA_CADASTRO = ?";		 ps.adicionarDateTime(dados.getDataCadastro());  

		stSql+= ",ATIVO = ?";		 ps.adicionarBoolean(dados.getAtivo());  

		stSql+= ",DATA_ATIVO = ?";		 ps.adicionarDateTime(dados.getDataAtivo());  

		stSql+= ",DATA_EXPIRACAO = ?";		 ps.adicionarDateTime(dados.getDataExpiracao());  

		stSql+= ",EMAIL = ?";		 ps.adicionarString(dados.getEMail());  

		stSql+= ",TELEFONE = ?";		 ps.adicionarString(dados.getTelefone());  

		stSql+= ",CELULAR = ?";		 ps.adicionarString(dados.getCelular());  

		stSql += " WHERE ID_USU  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psUsuarioexcluir()");

		stSql= "DELETE FROM PROJUDI.USU";
		stSql += " WHERE ID_USU = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public UsuarioDt consultarId(String id_usuario )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UsuarioDt Dados=null;
		////System.out.println("....ps-ConsultaId_Usuario)");

		stSql= "SELECT * FROM PROJUDI.VIEW_USU WHERE ID_USU = ?";		ps.adicionarLong(id_usuario); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Usuario  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UsuarioDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( UsuarioDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_USU"));
		Dados.setUsuario(rs.getString("USU"));
		Dados.setId_CtpsUf( rs.getString("ID_CTPS_UF"));
		Dados.setCtpsEstado( rs.getString("CTPS_ESTADO"));
		Dados.setId_Naturalidade( rs.getString("ID_NATURALIDADE"));
		Dados.setNaturalidade( rs.getString("NATURALIDADE"));
		Dados.setId_Endereco( rs.getString("ID_ENDERECO"));
		Dados.setEndereco( rs.getString("ENDERECO"));
		Dados.setId_RgOrgaoExpedidor( rs.getString("ID_RG_ORGAO_EXP"));
		Dados.setRgOrgaoExpedidor( rs.getString("RG_ORGAO_EXP"));
		Dados.setRgOrgaoExpedidorSigla( rs.getString("RG_ORGAO_EXP_SIGLA"));
		Dados.setSenha( rs.getString("SENHA"));
		Dados.setNome( rs.getString("NOME"));
		Dados.setSexo( rs.getString("SEXO"));
		Dados.setDataNascimento( Funcoes.FormatarData(rs.getDateTime("DATA_NASCIMENTO")));
		Dados.setRg( rs.getString("RG"));
		Dados.setRgDataExpedicao( Funcoes.FormatarData(rs.getDateTime("RG_DATA_EXPEDICAO")));
		Dados.setCpf( rs.getString("CPF"));
		Dados.setTituloEleitor( rs.getString("TITULO_ELEITOR"));
		Dados.setTituloEleitorZona( rs.getString("TITULO_ELEITOR_ZONA"));
		Dados.setTituloEleitorSecao( rs.getString("TITULO_ELEITOR_SECAO"));
		Dados.setCtps( rs.getString("CTPS"));
		Dados.setCtpsSerie( rs.getString("CTPS_SERIE"));
		Dados.setPis( rs.getString("PIS"));
		Dados.setMatriculaTjGo( rs.getString("MATRICULA_TJGO"));
		Dados.setNumeroConciliador( rs.getString("NUMERO_CONCILIADOR"));
		Dados.setDataCadastro( Funcoes.FormatarDataHora(rs.getDateTime("DATA_CADASTRO")));
		Dados.setAtivo( Funcoes.FormatarLogico(rs.getString("ATIVO")));
		Dados.setDataAtivo( Funcoes.FormatarDataHora(rs.getDateTime("DATA_ATIVO")));
		Dados.setDataExpiracao( Funcoes.FormatarDataHora(rs.getDateTime("DATA_EXPIRACAO")));
		Dados.setEMail( rs.getString("EMAIL"));
		Dados.setTelefone( rs.getString("TELEFONE"));
		Dados.setCelular( rs.getString("CELULAR"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoUsuario()");

		stSql= "SELECT ID_USU, USU FROM PROJUDI.VIEW_USU WHERE USU LIKE ?";
		stSql+= " ORDER BY USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoUsuario  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_USU WHERE USU LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..UsuarioPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

} 
