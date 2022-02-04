package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class MandadoJudicialPsGen extends Persistencia {

	private static final long serialVersionUID = -1199383277208840303L;

	public void inserir(MandadoJudicialDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO projudi.MAND_JUD ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_MandadoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MAND_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MandadoTipo());  

			stVirgula=",";
		}
		if ((dados.getId_MandadoJudicialStatus().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_MAND_JUD_STATUS " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_MandadoJudicialStatus());  

			stVirgula=",";
		}
		if ((dados.getId_Area().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_AREA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Area());  

			stVirgula=",";
		}
		if ((dados.getId_Zona().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ZONA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Zona());  

			stVirgula=",";
		}
		if ((dados.getId_Regiao().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_REGIAO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Regiao());  

			stVirgula=",";
		}
		if ((dados.getId_Bairro().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_BAIRRO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Bairro());  

			stVirgula=",";
		}
		if ((dados.getId_ProcessoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_ProcessoParte());  

			stVirgula=",";
		}
		if ((dados.getId_EnderecoParte().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ENDERECO_PARTE " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_EnderecoParte());  

			stVirgula=",";
		}
		if ((dados.getId_Pendencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PEND " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Pendencia());  

			stVirgula=",";
		}
		if ((dados.getId_Escala().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_ESC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Escala());  

			stVirgula=",";
		}
		if ((dados.getId_UsuarioServentia_2().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_USU_SERV_2 " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_UsuarioServentia_2());  

			stVirgula=",";
		}
		if ((dados.getValor().length()>0)) {
			 stSqlCampos+=   stVirgula + "VALOR " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getValor());  

			stVirgula=",";
		}
		if ((dados.getAssistencia().length()>0)) {
			 stSqlCampos+=   stVirgula + "ASSISTENCIA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getAssistencia());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql,"ID_MAND_JUD",ps));

		 
	} 

	public void alterar(MandadoJudicialDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE projudi.MAND_JUD SET  ";

		stSql+= "ID_MAND_TIPO = ?";		 ps.adicionarLong(dados.getId_MandadoTipo());  

		stSql+= ",ID_MAND_JUD_STATUS = ?";		 ps.adicionarLong(dados.getId_MandadoJudicialStatus());  

		stSql+= ",ID_AREA = ?";		 ps.adicionarLong(dados.getId_Area());  

		stSql+= ",ID_ZONA = ?";		 ps.adicionarLong(dados.getId_Zona());  

		stSql+= ",ID_REGIAO = ?";		 ps.adicionarLong(dados.getId_Regiao());  

		stSql+= ",ID_BAIRRO = ?";		 ps.adicionarLong(dados.getId_Bairro());  

		stSql+= ",ID_PROC_PARTE = ?";		 ps.adicionarLong(dados.getId_ProcessoParte());  

		stSql+= ",ID_ENDERECO_PARTE = ?";		 ps.adicionarLong(dados.getId_EnderecoParte());  

		stSql+= ",ID_PEND = ?";		 ps.adicionarLong(dados.getId_Pendencia());  

		stSql+= ",ID_ESC = ?";		 ps.adicionarLong(dados.getId_Escala());  
		
		stSql+= ",ID_USU_SERV_1 = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia_1());  

		stSql+= ",ID_USU_SERV_2 = ?";		 ps.adicionarLong(dados.getId_UsuarioServentia_2());  

		stSql+= ",VALOR = ?";		 ps.adicionarString(dados.getValor());  

		stSql+= ",ASSISTENCIA = ?";		 ps.adicionarBoolean(dados.getAssistencia());  
		
		stSql+= ",ID_SERV_CARGO_ESC = ?";		 ps.adicionarLong(dados.getId_ServentiaCargoEscala());  

		stSql += " WHERE ID_MAND_JUD  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM projudi.MAND_JUD";
		stSql += " WHERE ID_MAND_JUD = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

	public MandadoJudicialDt consultarId(String id_mandadojudicial )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		MandadoJudicialDt Dados=null;

		stSql= "SELECT * FROM projudi.VIEW_MAND_JUD WHERE ID_MAND_JUD = ?";		ps.adicionarLong(id_mandadojudicial); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new MandadoJudicialDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( MandadoJudicialDt dados, ResultSetTJGO rs1 )  throws Exception {
		dados.setId(rs1.getString("ID_MAND_JUD"));
		dados.setId_MandadoTipo(rs1.getString("ID_MAND_TIPO"));
		dados.setMandadoTipo(rs1.getString("MAND_TIPO"));
		dados.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
		dados.setProcessoParte(rs1.getString("PROC_PARTE"));
		dados.setId_EnderecoParte(rs1.getString("ID_ENDERECO_PARTE"));
		dados.setEnderecoParte(rs1.getString("ENDERECO_PARTE"));
		dados.setId_Pendencia(rs1.getString("ID_PEND"));
		dados.setId_Area(rs1.getString("ID_AREA"));
		dados.setArea(rs1.getString("AREA"));
		dados.setId_Zona(rs1.getString("ID_ZONA"));
		dados.setZona(rs1.getString("ZONA"));
		dados.setId_Regiao(rs1.getString("ID_REGIAO"));
		dados.setRegiao(rs1.getString("REGIAO"));
		dados.setId_Bairro(rs1.getString("ID_BAIRRO"));
		dados.setBairro(rs1.getString("BAIRRO"));
		dados.setId_Escala(rs1.getString("ID_ESC"));
		dados.setEscala(rs1.getString("ESC"));
		dados.setValor(rs1.getString("VALOR"));
		dados.setAssistencia(rs1.getString("ASSISTENCIA"));
		dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		dados.setId_MandadoJudicialStatus(rs1.getString("ID_MAND_JUD_STATUS"));
		dados.setMandadoJudicialStatus(rs1.getString("MAND_JUD_STATUS"));
		dados.setId_UsuarioServentia_1(rs1.getString("ID_USU_SERV_1"));
		dados.setNomeUsuarioServentia_1(rs1.getString("NOME_USU_SERV_1"));
		dados.setId_UsuarioServentia_2(rs1.getString("ID_USU_SERV_2"));
		dados.setNomeUsuarioServentia_2(rs1.getString("NOME_USU_SERV_2"));
		dados.setDataDistribuicao(rs1.getString("DATA_DIST"));
		dados.setDataRetorno(rs1.getString("DATA_RETORNO"));
		dados.setDataLimite(rs1.getString("DATA_LIMITE"));
		dados.setId_Serventia(rs1.getString("ID_SERV"));
		dados.setId_ServentiaCargoEscala(rs1.getString("ID_SERV_CARGO_ESC"));
		dados.setLocomocoesFrutiferas(rs1.getString("LOCOMOCOES_FRUTIFERAS"));
		dados.setLocomocoesInfrutiferas(rs1.getString("LOCOMOCOES_FRUTIFERAS"));
		dados.setLocomocaoHoraMarcada(rs1.getString("LOCOMOCAO_HORA_MARCADA"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoMandadoJudicial()");

		stSql= "SELECT ID_MAND_JUD, MAND_JUD FROM projudi.VIEW_MAND_JUDICIAL WHERE MAND_JUD LIKE ?";
		stSql+= " ORDER BY MAND_JUD ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoMandadoJudicial  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				MandadoJudicialDt obTemp = new MandadoJudicialDt();
				obTemp.setId(rs1.getString("ID_MAND_JUD"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_MAND_JUDICIAL WHERE MAND_JUD LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..MandadoJudicialPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
