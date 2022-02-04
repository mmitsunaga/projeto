package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class GuiaCustaModeloPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = 6898728309767102025L;

	//---------------------------------------------------------
	public GuiaCustaModeloPsGen() {

	}



//---------------------------------------------------------
	public void inserir(GuiaCustaModeloDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaCustaModeloinserir()");
		stSqlCampos= "INSERT INTO projudi.GUIA_CUSTA_MODELO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getGuiaCustaModelo().length()>0)) {
			 stSqlCampos+=   stVirgula + "GUIA_CUSTA_MODELO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getGuiaCustaModelo());  

			stVirgula=",";
		}
		if ((dados.getId_GuiaModelo().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_GUIA_MODELO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_GuiaModelo());  

			stVirgula=",";
		}
		if ((dados.getId_Custa().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_CUSTA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Custa());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_GUIA_CUSTA_MODELO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(GuiaCustaModeloDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psGuiaCustaModeloalterar()");

		stSql= "UPDATE projudi.GUIA_CUSTA_MODELO SET  ";
		stSql+= "GUIA_CUSTA_MODELO = ?";		 ps.adicionarString(dados.getGuiaCustaModelo());  

		stSql+= ",ID_GUIA_MODELO = ?";		 ps.adicionarLong(dados.getId_GuiaModelo());  

		stSql+= ",ID_CUSTA = ?";		 ps.adicionarLong(dados.getId_Custa());  

		stSql += " WHERE ID_GUIA_CUSTA_MODELO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psGuiaCustaModeloexcluir()");

		stSql= "DELETE FROM projudi.GUIA_CUSTA_MODELO";
		stSql += " WHERE ID_GUIA_CUSTA_MODELO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public GuiaCustaModeloDt consultarId(String id_guiacustamodelo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		GuiaCustaModeloDt Dados=null;
		////System.out.println("....ps-ConsultaId_GuiaCustaModelo)");

		stSql= "SELECT * FROM projudi.VIEW_GUIA_CUSTA_MODELO WHERE ID_GUIA_CUSTA_MODELO = ?";		ps.adicionarLong(id_guiacustamodelo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_GuiaCustaModelo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new GuiaCustaModeloDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( GuiaCustaModeloDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_GUIA_CUSTA_MODELO"));
		Dados.setGuiaCustaModelo(rs.getString("GUIA_CUSTA_MODELO"));
		Dados.setId_GuiaModelo( rs.getString("ID_GUIA_MODELO"));
		Dados.setGuiaModelo( rs.getString("GUIA_MODELO"));
		Dados.setId_Custa( rs.getString("ID_CUSTA"));
		Dados.setCodigoRegimento( rs.getString("CODIGO_REGIMENTO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoGuiaCustaModelo()");

		stSql= "SELECT ID_GUIA_CUSTA_MODELO, GUIA_CUSTA_MODELO FROM projudi.VIEW_GUIA_CUSTA_MODELO WHERE GUIA_CUSTA_MODELO LIKE ?";
		stSql+= " ORDER BY GUIA_CUSTA_MODELO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoGuiaCustaModelo  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				GuiaCustaModeloDt obTemp = new GuiaCustaModeloDt();
				obTemp.setId(rs1.getString("ID_GUIA_CUSTA_MODELO"));
				obTemp.setGuiaCustaModelo(rs1.getString("GUIA_CUSTA_MODELO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM projudi.VIEW_GUIA_CUSTA_MODELO WHERE GUIA_CUSTA_MODELO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..GuiaCustaModeloPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
