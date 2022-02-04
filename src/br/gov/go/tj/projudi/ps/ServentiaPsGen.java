package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ServentiaPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -3183348843481495741L;

	//---------------------------------------------------------
	public ServentiaPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ServentiaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiainserir()");
		stSqlCampos= "INSERT INTO PROJUDI.SERV ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getServentia().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getServentia());  

			stVirgula=",";
		}
		if ((dados.getServentiaCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaCodigo());  

			stVirgula=",";
		}
		if ((dados.getServentiaCodigoExterno().length()>0)) {
			 stSqlCampos+=   stVirgula + "SERV_CODIGO_EXTERNO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getServentiaCodigoExterno());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaTipo());  

			stVirgula=",";
		}
		if ((dados.getId_ServentiaSubtipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_SERV_SUBTIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

			stVirgula=",";
		}
		if ((dados.getId_Area().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Area());  

			stVirgula=",";
		}
		if ((dados.getId_Comarca().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_COMARCA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Comarca());  

			stVirgula=",";
		}
		if ((dados.getId_AreaDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AreaDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getId_EstadoRepresentacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESTADO_REPRESENTACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EstadoRepresentacao());  

			stVirgula=",";
		}
		if ((dados.getId_AudienciaTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AUDI_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_AudienciaTipo());  

			stVirgula=",";
		}
		if ((dados.getQuantidadeDistribuicao().length()>0)) {
			 stSqlCampos+=   stVirgula + "QUANTIDADE_DIST " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getQuantidadeDistribuicao());  

			stVirgula=",";
		}
		if ((dados.getId_Endereco().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Endereco());  

			stVirgula=",";
		}
		if ((dados.getTelefone().length()>0)) {
			 stSqlCampos+=   stVirgula + "TELEFONE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getTelefone());  

			stVirgula=",";
		}
		if ((dados.getConclusoDireto().length()>0)) {
			 stSqlCampos+=   stVirgula + "CONCLUSO_DIRETO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getConclusoDireto());  

			stVirgula=",";
		}
		if ((dados.getOnline().length()>0)) {
			 stSqlCampos+=   stVirgula + "ON_LINE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getOnline());  

			stVirgula=",";
		}
		stSqlCampos  +=  stVirgula + "DATA_CADASTRO";
		stSqlValores += stVirgula + "? ";
		ps.adicionarDateTime(new Date());  

		stVirgula=",";
		if ((dados.getDataImplantacao().length()>0)) {
			 stSqlCampos+=   stVirgula + "DATA_IMPLANTACAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarDate(dados.getDataImplantacao());  

			stVirgula=",";
		}		
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_SERV",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ServentiaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psServentiaalterar()");

		stSql= "UPDATE PROJUDI.SERV SET  ";
		stSql+= "SERV = ?";		 ps.adicionarString(dados.getServentia());  

		stSql+= ",SERV_CODIGO = ?";		 ps.adicionarLong(dados.getServentiaCodigo());  

		stSql+= ",SERV_CODIGO_EXTERNO = ?";		 ps.adicionarLong(dados.getServentiaCodigoExterno());  
		
		stSql+= ",ID_CNJ_SERV = ?";		 ps.adicionarString(dados.getId_CNJServentia());  

		stSql+= ",ID_SERV_TIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaTipo());  

		stSql+= ",ID_SERV_SUBTIPO = ?";		 ps.adicionarLong(dados.getId_ServentiaSubtipo());  

		stSql+= ",ID_AREA = ?";		 ps.adicionarLong(dados.getId_Area());  

		stSql+= ",ID_COMARCA = ?";		 ps.adicionarLong(dados.getId_Comarca());  

		stSql+= ",ID_AREA_DIST = ?";		 ps.adicionarLong(dados.getId_AreaDistribuicao());  

		stSql+= ",ID_ESTADO_REPRESENTACAO = ?";		 ps.adicionarLong(dados.getId_EstadoRepresentacao());  

		stSql+= ",ID_AUDI_TIPO = ?";		 ps.adicionarLong(dados.getId_AudienciaTipo());  

		stSql+= ",QUANTIDADE_DIST = ?";		 ps.adicionarBoolean(dados.getQuantidadeDistribuicao());

		stSql+= ",ID_ENDERECO = ?";		 ps.adicionarLong(dados.getId_Endereco());  

		stSql+= ",TELEFONE = ?";		 ps.adicionarString(dados.getTelefone());  

		stSql+= ",ON_LINE = ?";		 ps.adicionarBoolean(dados.getOnline());
		
		stSql+= ",DATA_IMPLANTACAO  = ? ";      ps.adicionarDate(dados.getDataImplantacao());

		stSql+= ",CONCLUSO_DIRETO  = ? ";      ps.adicionarBoolean(dados.getConclusoDireto());
		
		stSql += " WHERE ID_SERV  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psServentiaexcluir()");

		stSql= "DELETE FROM PROJUDI.SERV";
		stSql += " WHERE ID_SERV = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ServentiaDt consultarId(String id_serventia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ServentiaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Serventia)");

		stSql= "SELECT * FROM PROJUDI.VIEW_SERV WHERE ID_SERV = ?";		ps.adicionarLong(id_serventia); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Serventia  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ServentiaDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ServentiaDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		Dados.setId(rs1.getString("ID_SERV"));
		Dados.setServentia(rs1.getString("SERV"));
		Dados.setServentiaCodigo( rs1.getString("SERV_CODIGO"));
		Dados.setServentiaCodigoExterno( rs1.getString("SERV_CODIGO_EXTERNO"));
		Dados.setId_ServentiaTipo( rs1.getString("ID_SERV_TIPO"));
		Dados.setServentiaTipo( rs1.getString("SERV_TIPO"));
		Dados.setId_ServentiaSubtipo( rs1.getString("ID_SERV_SUBTIPO"));
		Dados.setServentiaSubtipo( rs1.getString("SERV_SUBTIPO"));
		Dados.setId_Area( rs1.getString("ID_AREA"));
		Dados.setArea( rs1.getString("AREA"));
		Dados.setId_Comarca( rs1.getString("ID_COMARCA"));
		Dados.setComarca( rs1.getString("COMARCA"));
		Dados.setId_AreaDistribuicao( rs1.getString("ID_AREA_DIST"));
		Dados.setAreaDistribuicao( rs1.getString("AREA_DIST"));
		Dados.setId_EstadoRepresentacao( rs1.getString("ID_ESTADO_REPRESENTACAO"));
		Dados.setEstadoRepresentacao( rs1.getString("ESTADO_REPRESENTACAO"));
		Dados.setId_AudienciaTipo( rs1.getString("ID_AUDI_TIPO"));
		Dados.setAudienciaTipo( rs1.getString("AUDI_TIPO"));		
		Dados.setId_Endereco( rs1.getString("ID_ENDERECO"));
		Dados.setLogradouro( rs1.getString("LOGRADOURO"));
		Dados.setTelefone( rs1.getString("TELEFONE"));
		Dados.setOnline( Funcoes.FormatarLogico(rs1.getString("ON_LINE")));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setNumero( rs1.getString("NUMERO"));
		Dados.setComplemento( rs1.getString("COMPLEMENTO"));
		Dados.setId_Bairro( rs1.getString("ID_BAIRRO"));
		Dados.setBairro( rs1.getString("BAIRRO"));
		Dados.setCidade( rs1.getString("CIDADE"));
		Dados.setEstado( rs1.getString("ESTADO"));
		Dados.setCep( rs1.getString("CEP"));
		Dados.setServentiaTipoCodigo( rs1.getString("SERV_TIPO_CODIGO"));
		Dados.setServentiaSubtipoCodigo( rs1.getString("SERV_SUBTIPO_CODIGO"));
		Dados.setAreaCodigo( rs1.getString("AREA_CODIGO"));
		Dados.setComarcaCodigo( rs1.getString("COMARCA_CODIGO"));
		Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
		Dados.setDataImplantacao(Funcoes.FormatarData(rs1.getDateTime("DATA_IMPLANTACAO")));
		Dados.setConclusoDireto(Funcoes.FormatarLogico(rs1.getString("CONCLUSO_DIRETO")));
		Dados.setQuantidadeDistribuicao( Funcoes.FormatarLogico(rs1.getString("QUANTIDADE_DIST")));
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoServentia()");

		stSql= "SELECT ID_SERV, SERV FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
		stSql+= " ORDER BY SERV ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoServentia  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ServentiaDt obTemp = new ServentiaDt();
				obTemp.setId(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_SERV WHERE SERV LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ServentiaPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
