package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ObjetoPedidoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class ObjetoPedidoPsGen extends Persistencia {


/**
	 * 
	 */
	private static final long serialVersionUID = -6087117986803138238L;

	//---------------------------------------------------------
	public ObjetoPedidoPsGen() {

	}



//---------------------------------------------------------
	public void inserir(ObjetoPedidoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psObjetoPedidoinserir()");
		stSqlCampos= "INSERT INTO PROJUDI.OBJETO_PEDIDO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getObjetoPedido().length()>0)) {
			 stSqlCampos+=   stVirgula + "OBJETO_PEDIDO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getObjetoPedido());  

			stVirgula=",";
		}
		if ((dados.getObjetoPedidoCodigo().length()>0)) {
			 stSqlCampos+=   stVirgula + "OBJETO_PEDIDO_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getObjetoPedidoCodigo());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		////System.out.println("....Sql " + stSql);

		
			dados.setId(executarInsert(stSql,"ID_OBJETO_PEDIDO",ps));
			////System.out.println("....Execução OK"  );

		 
	} 

//---------------------------------------------------------
	public void alterar(ObjetoPedidoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psObjetoPedidoalterar()");

		stSql= "UPDATE PROJUDI.OBJETO_PEDIDO SET  ";
		stSql+= "OBJETO_PEDIDO = ?";		 ps.adicionarString(dados.getObjetoPedido());  

		stSql+= ",OBJETO_PEDIDO_CODIGO = ?";		 ps.adicionarLong(dados.getObjetoPedidoCodigo());  

		stSql += " WHERE ID_OBJETO_PEDIDO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
	} 

//---------------------------------------------------------
	public void excluir( String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("....psObjetoPedidoexcluir()");

		stSql= "DELETE FROM PROJUDI.OBJETO_PEDIDO";
		stSql += " WHERE ID_OBJETO_PEDIDO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);

	} 

//---------------------------------------------------------
	public ObjetoPedidoDt consultarId(String id_objetopedido )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ObjetoPedidoDt Dados=null;
		////System.out.println("....ps-ConsultaId_ObjetoPedido)");

		stSql= "SELECT * FROM PROJUDI.VIEW_OBJETO_PEDIDO WHERE ID_OBJETO_PEDIDO = ?";		ps.adicionarLong(id_objetopedido); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_ObjetoPedido  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ObjetoPedidoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}

	protected void associarDt( ObjetoPedidoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_OBJETO_PEDIDO"));
		Dados.setObjetoPedido(rs.getString("OBJETO_PEDIDO"));
		Dados.setObjetoPedidoCodigo( rs.getString("OBJETO_PEDIDO_CODIGO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}

//---------------------------------------------------------
	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-ConsultaDescricaoObjetoPedido()");

		stSql= "SELECT ID_OBJETO_PEDIDO, OBJETO_PEDIDO FROM PROJUDI.VIEW_OBJETO_PEDIDO WHERE OBJETO_PEDIDO LIKE ?";
		stSql+= " ORDER BY OBJETO_PEDIDO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			////System.out.println("..ps-ConsultaDescricaoObjetoPedido  " + stSql);

			rs1 = consultarPaginacao(stSql, ps, posicao);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				ObjetoPedidoDt obTemp = new ObjetoPedidoDt();
				obTemp.setId(rs1.getString("ID_OBJETO_PEDIDO"));
				obTemp.setObjetoPedido(rs1.getString("OBJETO_PEDIDO"));
				liTemp.add(obTemp);
			}
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_OBJETO_PEDIDO WHERE OBJETO_PEDIDO LIKE ?";
			rs2 = consultar(stSql,ps);
			////System.out.println("....2 - Consulta quantidade OK"  );
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			////System.out.println("..ObjetoPedidoPsGen.consultarDescricao() Operação realizada com sucesso");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return liTemp; 
	}

} 
