package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class EventoExecucaoTipoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 4278590526681324606L;

	//---------------------------------------------------------
	public EventoExecucaoTipoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(EventoExecucaoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoExecucaoTipoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.EVENTO_EXE_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getEventoExecucaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "EVENTO_EXE_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getEventoExecucaoTipo());  

			stVirgula=",";
		}
		if ((dados.getEventoExecucaoTipoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "EVENTO_EXE_TIPO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getEventoExecucaoTipoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_EVENTO_EXE_TIPO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(EventoExecucaoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psEventoExecucaoTipoalterar()");

		stSql= "UPDATE PROJUDI.EVENTO_EXE_TIPO SET  ";
		stSql+= "EVENTO_EXE_TIPO = ?";		 ps.adicionarString(dados.getEventoExecucaoTipo());  

		stSql+= ",EVENTO_EXE_TIPO_CODIGO = ?";		 ps.adicionarLong(dados.getEventoExecucaoTipoCodigo());  

		stSql += " WHERE ID_EVENTO_EXE_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psEventoExecucaoTipoexcluir()");

		stSql= "DELETE FROM PROJUDI.EVENTO_EXE_TIPO";
		stSql += " WHERE ID_EVENTO_EXE_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public EventoExecucaoTipoDt consultarId(String id_eventoexecucaotipo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EventoExecucaoTipoDt Dados=null;
		////System.out.println("....ps-ConsultaId_EventoExecucaoTipo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_EVENTO_EXE_TIPO WHERE ID_EVENTO_EXE_TIPO = ?";		ps.adicionarLong(id_eventoexecucaotipo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_EventoExecucaoTipo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EventoExecucaoTipoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( EventoExecucaoTipoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_EVENTO_EXE_TIPO"));
		Dados.setEventoExecucaoTipo(rs.getString("EVENTO_EXE_TIPO"));
		Dados.setEventoExecucaoTipoCodigo( rs.getString("EVENTO_EXE_TIPO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoEventoExecucaoTipo()");

		stSql= "SELECT ID_EVENTO_EXE_TIPO, EVENTO_EXE_TIPO FROM PROJUDI.VIEW_EVENTO_EXE_TIPO WHERE EVENTO_EXE_TIPO LIKE ?";
		stSql+= " ORDER BY EVENTO_EXE_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoEventoExecucaoTipo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				EventoExecucaoTipoDt obTemp = new EventoExecucaoTipoDt();
				obTemp.setId(rs1.getString("ID_EVENTO_EXE_TIPO"));
				obTemp.setEventoExecucaoTipo(rs1.getString("EVENTO_EXE_TIPO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_EVENTO_EXE_TIPO WHERE EVENTO_EXE_TIPO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..EventoExecucaoTipoPsGen.consultarDescricao() Operação realizada com sucesso");
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception{

		String stSql="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_EVENTO_EXE_TIPO as id, EVENTO_EXE_TIPO as descricao1 FROM PROJUDI.VIEW_EVENTO_EXE_TIPO WHERE EVENTO_EXE_TIPO LIKE ?";
		stSql+= " ORDER BY EVENTO_EXE_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{


			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade  FROM PROJUDI.VIEW_EVENTO_EXE_TIPO WHERE EVENTO_EXE_TIPO LIKE ?";
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
