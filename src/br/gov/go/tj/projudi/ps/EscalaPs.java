package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EscalaTipoDt;
import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;

import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class EscalaPs extends EscalaPsGen{

	private static final long serialVersionUID = -204922782361666724L;

	public EscalaPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método responsável por consultar as escalas cadastradas
	 */
	public List consultarEscalas(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT ID_ESC, ESC, MAND_TIPO, ESCALA_TIPO, REGIAO";//, ZONA, BAIRRO ";
		SqlFrom = " FROM PROJUDI.VIEW_ESCALA ";
		SqlFrom += " WHERE ESC LIKE ? ";
		SqlOrder = " ORDER BY ESC ";
		ps.adicionarString("%" +  descricao +"%");
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				EscalaDt obTemp = new EscalaDt();
				obTemp.setId(rs1.getString("ID_ESC"));
				obTemp.setEscala(rs1.getString("ESC"));
				obTemp.setMandadoTipo(rs1.getString("MAND_TIPO"));
				obTemp.setEscalaTipo(rs1.getString("ESCALA_TIPO"));
				//obTemp.setZona(rs1.getString("ZONA"));
				obTemp.setRegiao(rs1.getString("REGIAO"));
				//obTemp.setBairro(rs1.getString("BAIRRO"));
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

	/**
	 * Método de consulta das escalas ativas.
	 * @return List<EscalaDt>
	 * @throws Exception
	 */
    public List consultarEscalasAtivas() throws Exception {
        String Sql;
        List liTemp = new ArrayList();
        EscalaDt Dados =null; 
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Sql= "SELECT * FROM PROJUDI.VIEW_ESCALA WHERE ATIVO = ?";
        ps.adicionarBoolean(String.valueOf(EscalaDt.ATIVO));        
        
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {
                Dados= new EscalaDt();
                associarDt(Dados, rs1);
                liTemp.add(Dados);
            }
        } finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return liTemp; 
    }
    
    /**
     * Método para Consultar Escala.
     * @param mandadoTipoDt
     * @param EscalaTipoDt
     * @param regiaoDt
     * @return EscalaDt
     * @throws Exception
     */
    public EscalaDt consultarEscala(MandadoTipoDt mandadoTipoDt, EscalaTipoDt EscalaTipoDt, RegiaoDt regiaoDt) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	EscalaDt escalaDt = null;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String clausulaWhereAnd = " WHERE ";
    	
    	sql.append("SELECT * FROM PROJUDI.VIEW_ESCALA ");
    	if( mandadoTipoDt != null ) {
    		sql.append(clausulaWhereAnd + " MAND_TIPO_CODIGO = ?");
    		ps.adicionarLong(mandadoTipoDt.getMandadoTipoCodigo());
    		clausulaWhereAnd = " AND ";
    	}
    	if( EscalaTipoDt != null ) {
    		sql.append(clausulaWhereAnd +" ESCALA_TIPO_CODIGO = ?");
    		ps.adicionarLong(EscalaTipoDt.getEscalaTipoCodigo());
    		clausulaWhereAnd = " AND ";
    	}
    	if( regiaoDt != null ) {
    		sql.append(clausulaWhereAnd + " REGIAO_CODIGO = ?");
    		ps.adicionarLong(regiaoDt.getRegiaoCodigo());
    		clausulaWhereAnd = " AND ";
    	}
    	sql.append(" ORDER BY ESC ");
    	
    	ResultSetTJGO rs1 = null;
    	try{
    		rs1 = consultar(sql.toString(), ps);
    		while( rs1.next() ) {
    			escalaDt = new EscalaDt();
    			associarDt(escalaDt, rs1);
    		}
    	
    	} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
    	
    	return escalaDt;
    }
    
    /**
     * Método para consultar escala.
     * @param String idBairro
     * @param String idServentia
     * @param String idMandadoTipo
     * @return EscalaDt
     * @throws Exception
     */
    public EscalaDt consultarEscala(String idBairro, String idServentia, String idMandadoTipo) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	EscalaDt escalaDt = null;
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String clausulaWhereAnd = " WHERE ";
    	
    	sql.append("SELECT * FROM PROJUDI.VIEW_ESCALA ");
    	if( idBairro != null && idBairro.length() > 0 ) {
    		sql.append(clausulaWhereAnd + " ID_BAIRRO = ?");
    		ps.adicionarLong(idBairro);
    		clausulaWhereAnd = " AND ";
    	}
    	if( idServentia != null && idServentia.length() > 0 ) {
    		sql.append(clausulaWhereAnd + " ID_SERV = ?");
    		ps.adicionarLong(idServentia);
    		clausulaWhereAnd = " AND ";
    	}
    	if( idMandadoTipo != null && idMandadoTipo.length() > 0 ) {
    		sql.append(clausulaWhereAnd + " ID_MAND_TIPO = ?");
    		ps.adicionarLong(idMandadoTipo);
    		clausulaWhereAnd = " AND ";
    	}
    	sql.append(" ORDER BY ESC ");
    	
    	ResultSetTJGO rs1 = null;
    	try{
    		rs1 = consultar(sql.toString(), ps);
    		while( rs1.next() ) {
    			escalaDt = new EscalaDt();
    			associarDt(escalaDt, rs1);
    		}
    	
    	} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
    	
    	return escalaDt;
    }
    
    public String consultarIdEscala(String idRegiao, String idServentia, String idMandadoTipo) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	String idEscala = "";
    	PreparedStatementTJGO ps =  new PreparedStatementTJGO();
    	String clausulaWhereAnd = " WHERE ";
    	
    	sql.append("SELECT * FROM PROJUDI.VIEW_ESCALA ");
    	if( idRegiao != null && idRegiao.length() > 0 ) {
    		sql.append(clausulaWhereAnd + " ID_REGIAO = ?");
    		ps.adicionarLong(idRegiao);
    		clausulaWhereAnd = " AND ";
    	}
    	if( idServentia != null && idServentia.length() > 0 ) {
    		sql.append(clausulaWhereAnd + " ID_SERV = ?");
    		ps.adicionarLong(idServentia);
    		clausulaWhereAnd = " AND ";
    	}
    	if( idMandadoTipo != null && idMandadoTipo.length() > 0 ) {
    		sql.append(clausulaWhereAnd + " ID_MAND_TIPO = ?");
    		ps.adicionarLong(idMandadoTipo);
    		clausulaWhereAnd = " AND ";
    	}
    	sql.append(" ORDER BY ESC ");
    	
    	ResultSetTJGO rs1 = null;
    	try {
    		rs1 = consultar(sql.toString(), ps);
    		while( rs1.next() ) {
    			idEscala = rs1.getString("ID_ESC");
    		}
    	} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
        }
    	
    	return idEscala;
    }
    
    /**
     * Método para salvar Escala.
     * @param EscalaDt dados
     * @throws Exception
     */
    public void inserirEscala(EscalaDt dados ) throws Exception {

    	String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	
		SqlCampos= "INSERT INTO PROJUDI.ESC (";
		SqlValores =  " Values (";		
		if (!(dados.getEscala().length()==0)){
			SqlCampos += "ESC " ;
			SqlValores += "?";
			ps.adicionarString(dados.getEscala());
		}
		if (!(dados.getId_Serventia().length()==0)){
			SqlCampos+= ",ID_SERV " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Serventia());
		}
		if (!(dados.getId_EscalaTipo().length()==0)){
			SqlCampos+= ",ID_EscalaTipo ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_EscalaTipo());
		}
		if (!(dados.getId_MandadoTipo().length()==0)){
			SqlCampos+= ",ID_MAND_TIPO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_MandadoTipo());
		}
		if (!(dados.getId_Zona().length()==0)){
			SqlCampos+= ",ID_ZONA " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Zona());
		}
		if (!(dados.getId_Regiao().length()==0)){
			SqlCampos+= ",ID_REGIAO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Regiao());
		}
		if (!(dados.getId_Bairro().length()==0)){
			SqlCampos+= ",ID_BAIRRO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Bairro());
		}
		if (!(dados.getQuantidadeMandado().length()==0)){
			SqlCampos+= ",QUANTIDADE_MAND " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getQuantidadeMandado());
		}
		if (!(dados.getAtivo().length()==0)){
			SqlCampos+= ",ATIVO " ;
			SqlValores += ",?";
			ps.adicionarLong(dados.getAtivo());
		}
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

 		dados.setId(executarInsert(Sql, "ID_ESC", ps)); 
	} 

    /**
     * Método para alterar Escala.
     * @param EscalaDt dados
     * @throws Exception
     */
	public void alterarEscala(EscalaDt dados) throws Exception{

		StringBuffer Sql = new StringBuffer();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql.append("UPDATE PROJUDI.ESC SET ");
		Sql.append("ESC  = ?");
		ps.adicionarString(dados.getEscala()); 
		Sql.append(",ID_SERV  = ?");
		ps.adicionarLong(dados.getId_Serventia()); 
		Sql.append(",ID_EscalaTipo  = ?");
		ps.adicionarLong(dados.getId_EscalaTipo()); 
		Sql.append(",ID_MAND_TIPO  = ?");
		ps.adicionarLong(dados.getId_MandadoTipo()); 
		Sql.append(",ID_ZONA  = ?");
		ps.adicionarLong(dados.getId_Zona()); 
		Sql.append(",ID_REGIAO  = ?");
		ps.adicionarLong(dados.getId_Regiao()); 
		Sql.append(",ID_BAIRRO  = ?");
		ps.adicionarLong(dados.getId_Bairro()); 
		Sql.append(",QUANTIDADE_MAND  = ?");
		ps.adicionarLong(dados.getQuantidadeMandado());
		Sql.append(",ATIVO = ?");
		ps.adicionarLong(dados.getAtivo());
		Sql.append(",TIPO_ESPECIAL = ?");
		ps.adicionarLong(dados.getTipoEspecial());
		Sql.append(" WHERE ID_ESC  = ?");
		ps.adicionarLong(dados.getId());
		
		executarUpdateDelete(Sql.toString(), ps); 
	
	}
	
	public String consultarEscalasJSON(String descricao, String idServ, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql= "SELECT ID_ESC AS ID, ESC AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_ESCALA";
		SqlFrom += " WHERE ESC LIKE ? ";
		ps.adicionarString("%" +  descricao +"%");
		SqlFrom += " AND ID_SERV = ? ";
		ps.adicionarString(idServ);
		SqlOrder = " ORDER BY ESC";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql= "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp; 
	}
	
	/*
	 * A distribuição para um escala deve seguir uma fila, usando a ordem do cadastro na tabela serv_cargo_esc
	 * 1 - pegue no ultimo mandado emitido o id do serv_cargo_esc, retorne o proximo oficial da escala, se esse for o ultimo retorne o primeiro da escala.
	 * mas para a redistribuição exclua o oficial atual.
	 * @jrcorrea 02/08/2016 15:55
	 * 
	 * @hrrosa - A redistribuição, diferente da distribuição, não precisa verificar se a escala para a qual
	 * está redistribuindo é de asssistência ou não. Isto acontece porque já irá redistribuir dentro da
	 * mesma escala para a qual o mandado foi distribuído inicialmente, momento em que esta verificação
	 * já é feita.
	 */
	
	public String[] redistribuicaoEscala(String id_escala, String id_serv_cargo_esc) throws Exception {
		String Sql;
		String[] stProximoOficial = {"","",""};
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		try{			
												
			Sql=  " Select id_usu_serv , id_serv_cargo, id_esc, id_serv_cargo_esc FROM (";
			Sql+= " Select Us.id_usu_serv , sc.id_serv_cargo, e.id_esc, e.id_serv_cargo_esc";
			Sql+= "  From  serv_Cargo_Esc E ";          
			Sql+= "  Inner Join Serv_Cargo Sc On Sc.Id_Serv_Cargo = E.Id_Serv_Cargo ";          
			Sql+= "  Inner Join Usu_Serv_Grupo Usg On Usg.Id_Usu_Serv_Grupo = Sc.Id_Usu_Serv_Grupo And Usg.Ativo = ? "; ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
			Sql+= "  Inner Join Usu_Serv Us On Usg.Id_Usu_Serv = Us.Id_Usu_Serv And Us.Ativo = ? "; ps.adicionarLong(UsuarioServentiaDt.ATIVO);  
			Sql+= "  Inner Join Serv_Cargo_Esc_Status Sces On E.Id_Serv_Cargo_Esc_Status = Sces.Id_Serv_Cargo_Esc_Status And Sces.serv_cargo_esc_status_codigo = ? "; ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
			Sql+= " Where E.Id_Serv_Cargo_Esc > (Select Id_Serv_Cargo_Esc From Mand_Jud Where Id_Mand_Jud = (Select Max(Id_Mand_Jud) From Mand_Jud Md Where Md.Id_Esc = ?)) "; ps.adicionarLong(id_escala);
			Sql+= " And E.id_esc = ? "; ps.adicionarLong(id_escala); 
			Sql+= " And E.Id_Serv_Cargo_Esc <> ? "; ps.adicionarLong(id_serv_cargo_esc);
			Sql+= " order by  E.Id_Serv_Cargo_Esc asc ) CONSULTA1 where rownum = 1 ";
			
			Sql+= "union all ";
			
			Sql+= " Select Id_Usu_Serv , id_serv_cargo , id_esc, id_serv_cargo_esc FROM ( ";
			Sql+= " Select Us.Id_Usu_Serv , sc.id_serv_cargo , e.id_esc, e.id_serv_cargo_esc ";
			Sql+= " From  serv_Cargo_Esc E ";
			Sql+= "  Inner Join Serv_Cargo Sc On Sc.Id_Serv_Cargo = E.Id_Serv_Cargo ";          
			Sql+= "  Inner Join Usu_Serv_Grupo Usg On Usg.Id_Usu_Serv_Grupo = Sc.Id_Usu_Serv_Grupo And Usg.Ativo = ? "; ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
			Sql+= "  Inner Join Usu_Serv Us On Usg.Id_Usu_Serv = Us.Id_Usu_Serv And Us.Ativo = ? ";	ps.adicionarLong(UsuarioServentiaDt.ATIVO);
			Sql+= " Where E.Id_Serv_Cargo_Esc >= (Select Min(Id_Serv_Cargo_Esc) From  Serv_Cargo_Esc E     Where    E.Id_Esc = ? ) "; ps.adicionarLong(id_escala);
			Sql+= " And E.id_esc = ? "; ps.adicionarLong(id_escala); 
			Sql+= " And E.Id_Serv_Cargo_Esc <> ? ";	ps.adicionarLong(id_serv_cargo_esc);
			Sql+= " order by  E.Id_Serv_Cargo_Esc asc) CONSULTA2 WHERE rownum = 1";
			
			rs1 = consultar(Sql, ps);
			
			if (rs1.next()){
				stProximoOficial[0] = rs1.getString("id_usu_serv");
				stProximoOficial[1] = rs1.getString("id_serv_cargo");
				stProximoOficial[2] = rs1.getString("id_serv_cargo_esc");
			}else{
				throw new MensagemException("Não foi possivel localizar uma escala ou um oficial. Verifique o cadastro e tente novamente");
			}
			
						
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return stProximoOficial; 
	}
	
	/*---
	 * A distribuição para um escala deve seguir uma fila, usando a ordem do cadastro na tabela serv_cargo_esc
	 * 1 - pegue no ultimo mandado emitido o id do serv_cargo_esc, retorne o proximo oficial da escala, se esse for o ultimo retorne o primeiro da escala.
	 * @jrcorrea 02/08/2016 15:55
	 */
	
	public String[] distribuicaoEscala(String id_regiao, String id_ServentiaCentralMandado, String id_MandadoTipo, int escalaTipoCodigo) throws Exception {
		
		String Sql;		
		String[] stProximoOficial = {"","","",""};
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		try{
			
			// FUNCIONAMENTO GERAL: O sql abaixo retorna os dados do oficial que foi sorteado para o mandado. A distribuição é feita dentro da escala
			// obedecendo uma ordem de fila, ordenando os oficiais por serv_Cargo_Esc.Id_Serv_Cargo_Esc e pegando sempre o próximo. O sql é dividido em duas partes
			// que são unidas por um UNION ALL. A primeira parte do sql atende às situações em que o próximo oficial estará no início ou no meio da fila enquanto a
			// segunda parte do sql atende às situações em que o próximo oficial estará no início da fila (imediatamente após a fila chegar no final ou quando não
			// existirem mandados ainda para a escala em questão). Ao final do sql, após o UNION ALL, será usado sempre a primeira linha retornada. Isto significa
			// que o resultado da primeira parte do sql será sempre utilizado à menos em situações nas quais ele não for retornado (não houver próximo da fila,
			// precisando assim retornar ao início).
			
			Sql= " Select id_usu_serv , id_serv_cargo, id_esc, id_serv_cargo_esc FROM (";
			Sql+= " Select Us.id_usu_serv , sc.id_serv_cargo, e.id_esc, e.id_serv_cargo_esc";
			Sql+= "  From  serv_Cargo_Esc E ";          
			Sql+= "  Inner Join Serv_Cargo Sc On Sc.Id_Serv_Cargo = E.Id_Serv_Cargo ";          
			Sql+= "  Inner Join Usu_Serv_Grupo Usg On Usg.Id_Usu_Serv_Grupo = Sc.Id_Usu_Serv_Grupo And Usg.Ativo = ? "; ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
			Sql+= "  Inner Join Usu_Serv Us On Usg.Id_Usu_Serv = Us.Id_Usu_Serv And Us.Ativo = ? ";	ps.adicionarLong(UsuarioServentiaDt.ATIVO);      
			Sql+= "  Inner Join Serv_Cargo_Esc_Status Sces On E.Id_Serv_Cargo_Esc_Status = Sces.Id_Serv_Cargo_Esc_Status And Sces.serv_cargo_esc_status_codigo = ? "; ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
			Sql+= "  Inner Join ESC on E.ID_ESC = ESC.ID_ESC AND ESC.TIPO_ESPECIAL = ? "; ps.adicionarLong(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO ? EscalaDt.TIPO_ESPECIAL_PLANTAO : EscalaDt.TIPO_ESPECIAL_NORMAL);
			Sql+= "  INNER JOIN ESCALA_TIPO ET ON ET.ID_ESCALA_TIPO = ESC.ID_ESCALA_TIPO AND ET.ESCALA_TIPO_CODIGO = ? "; ps.adicionarLong(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO ? EscalaTipoDt.CIVEL: escalaTipoCodigo);
			Sql+= " Where E.Id_Serv_Cargo_Esc > (Select Id_Serv_Cargo_Esc From Mand_Jud Where Id_Mand_Jud = ( select Id_Mand_Jud from ( Select Id_Mand_Jud From Mand_Jud Md Where Md.Id_Esc In "; 
			
			if(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO){
				//Se for um mandado to tipo Plantão, utiliza diretamente a escala de plantão da Central
				Sql+= "(SELECT ID_ESC FROM ESC ES WHERE ES.ID_SERV = ? AND ES.ID_MAND_TIPO = ?)";
				ps.adicionarLong(id_ServentiaCentralMandado);
				ps.adicionarLong(MandadoTipoDt.PLANTAO);
			}
			else {
				
				if(escalaTipoCodigo == EscalaTipoDt.ASSISTENCIA){
					//Se for um mandado sem custas, pesquisa a escala de acordo com a região do bairro do endereço da parte.
					Sql+= " (Select Id_Esc From Esc Es Where Es.Id_Regiao = ? And Es.Id_Serv= ? And Es.ID_MAND_TIPO = ? And Es.TIPO_ESPECIAL = ?) ";       ps.adicionarLong(id_regiao); 	ps.adicionarLong(id_ServentiaCentralMandado);	ps.adicionarLong(id_MandadoTipo);	ps.adicionarLong(0);
				}
				else {
					//Se for um mandado com custas, usa a escala com região "genérica".
					Sql+= "( SELECT E.ID_ESC FROM ESC E WHERE E.ID_REGIAO = ";
								//A subquery abaixo retorna a região genérica da comarca da central de mandados em questão.
					Sql+= " 	(SELECT R.ID_REGIAO FROM REGIAO R ";
					Sql+= "  	INNER JOIN COMARCA C ON R.ID_COMARCA = C.ID_COMARCA ";
					Sql+= "  	INNER JOIN SERV S ON S.ID_COMARCA = C.ID_COMARCA AND S.ID_SERV = ? "; 	ps.adicionarLong(id_ServentiaCentralMandado);
					Sql+= "  	WHERE UPPER(R.REGIAO) LIKE '%GENÉRICA%') "; 							//Por enquanto o único parâmetro para identificar a região genérica é o nome.
						  //Mesmo a região sendo genérica, obedece ao tipo de mandado da escala
					Sql+= "AND E.ID_MAND_TIPO = ? AND E.TIPO_ESPECIAL = ? ) ";								ps.adicionarLong(id_MandadoTipo);	ps.adicionarLong(0);
				}
			
			}
			
			Sql+= " order by data_dist desc) where rownum = 1 ";
			Sql+= ") and e.id_esc = ";
			
			if(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO){
				//Se for um mandado to tipo Plantão, utiliza diretamente a escala de plantão da Central
				Sql+= "(SELECT ID_ESC FROM ESC ES WHERE ES.ID_SERV = ? AND ES.ID_MAND_TIPO = ?)";
				ps.adicionarLong(id_ServentiaCentralMandado);
				ps.adicionarLong(MandadoTipoDt.PLANTAO);
			}
			else {
				
				if(escalaTipoCodigo == EscalaTipoDt.ASSISTENCIA){
					//Se for um mandado sem custas, pesquisa a escala de acordo com a região do bairro do endereço da parte.
					Sql+= " (Select Id_Esc From Esc Es Where Es.Id_Regiao = ? And Es.Id_Serv= ? And Es.ID_MAND_TIPO = ? And Es.TIPO_ESPECIAL = ? AND Es.ID_ESCALA_TIPO = ? ) ";
					ps.adicionarLong(id_regiao);
					ps.adicionarLong(id_ServentiaCentralMandado);
					ps.adicionarLong(id_MandadoTipo);
					ps.adicionarLong(0);
					ps.adicionarLong(escalaTipoCodigo);
				}
				else {
					Sql+= "		  (SELECT E.ID_ESC ";
					Sql+= "		      FROM ESC E ";
					Sql+= "		      WHERE E.ID_REGIAO = ";
					Sql+= " 			(SELECT R.ID_REGIAO ";
					Sql+= "				FROM REGIAO R ";
					Sql+= "				INNER JOIN COMARCA C ";
					Sql+= "				ON R.ID_COMARCA = C.ID_COMARCA ";
					Sql+= "				INNER JOIN SERV S ";
					Sql+= "				ON S.ID_COMARCA = C.ID_COMARCA ";
					Sql+= "				AND S.ID_SERV   = ? ";						ps.adicionarLong(id_ServentiaCentralMandado);
					Sql+= "				WHERE UPPER(R.REGIAO) LIKE '%GENÉRICA%' ";
					Sql+= "				) ";
					Sql+= "			AND E.ID_MAND_TIPO  = ? ";						ps.adicionarLong(id_MandadoTipo);
					Sql+= "			AND E.TIPO_ESPECIAL = ? ";						ps.adicionarLong(0);
					Sql+= ") ";
				}
				
			}

			Sql+= ") order by  E.Id_Serv_Cargo_Esc asc ) CONSULTA1 where rownum = 1 ";
			
			
			//Fará a junção do resultado das duas partes do sql
			Sql+= " UNION ALL ";
			
			
			Sql+= " Select Id_Usu_Serv , id_serv_cargo , id_esc, id_serv_cargo_esc FROM ( ";
			Sql+= " Select Us.Id_Usu_Serv , sc.id_serv_cargo , e.id_esc, e.id_serv_cargo_esc ";
			Sql+= " From  serv_Cargo_Esc E ";
			Sql+= "  Inner Join Serv_Cargo Sc On Sc.Id_Serv_Cargo = E.Id_Serv_Cargo ";          
			Sql+= "  Inner Join Usu_Serv_Grupo Usg On Usg.Id_Usu_Serv_Grupo = Sc.Id_Usu_Serv_Grupo And Usg.Ativo = ? "; ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
			Sql+= "  Inner Join Usu_Serv Us On Usg.Id_Usu_Serv = Us.Id_Usu_Serv And Us.Ativo = ? "; ps.adicionarLong(UsuarioServentiaDt.ATIVO);
			Sql+= "  Inner Join Serv_Cargo_Esc_Status Sces On E.Id_Serv_Cargo_Esc_Status = Sces.Id_Serv_Cargo_Esc_Status And Sces.serv_cargo_esc_status_codigo = ? "; ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
			Sql+= "  Inner Join ESC on E.ID_ESC = ESC.ID_ESC AND ESC.TIPO_ESPECIAL = ? "; ps.adicionarLong(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO ? EscalaDt.TIPO_ESPECIAL_PLANTAO : EscalaDt.TIPO_ESPECIAL_NORMAL);
			Sql+= "  INNER JOIN ESCALA_TIPO ET ON ET.ID_ESCALA_TIPO = ESC.ID_ESCALA_TIPO AND ET.ESCALA_TIPO_CODIGO = ? "; ps.adicionarLong(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO ? EscalaTipoDt.CIVEL: escalaTipoCodigo);
			Sql+= " Where E.Id_Serv_Cargo_Esc >= (Select Min(Id_Serv_Cargo_Esc) From  Serv_Cargo_Esc E     Where    E.Id_Esc In ";
			
			if(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO){
				//Se for um mandado to tipo Plantão, utiliza diretamente a escala de plantão da Central
				Sql+= "(SELECT ID_ESC FROM ESC ES WHERE ES.ID_SERV = ? AND ES.ID_MAND_TIPO = ?)";
				ps.adicionarLong(id_ServentiaCentralMandado);
				ps.adicionarLong(MandadoTipoDt.PLANTAO);
			}
			else {
				
				if(escalaTipoCodigo == EscalaTipoDt.ASSISTENCIA){
					Sql+= " (Select Id_Esc From Esc Es Where Es.Id_Regiao = ? And Es.Id_Serv= ? And Es.ID_MAND_TIPO = ? And Es.TIPO_ESPECIAL = ?) "; ps.adicionarLong(id_regiao); 	ps.adicionarLong(id_ServentiaCentralMandado);	ps.adicionarLong(id_MandadoTipo);	ps.adicionarLong(0);
				}
				else {
					//Se for um mandado com custas, usa a escala com região "genérica".
					Sql+= "( SELECT E.ID_ESC FROM ESC E WHERE E.ID_REGIAO = ";
								//A subquery abaixo retorna a região genérica da comarca da central de mandados em questão.
					Sql+= " 	(SELECT R.ID_REGIAO FROM REGIAO R ";
					Sql+= "  	INNER JOIN COMARCA C ON R.ID_COMARCA = C.ID_COMARCA ";
					Sql+= "  	INNER JOIN SERV S ON S.ID_COMARCA = C.ID_COMARCA AND S.ID_SERV = ? "; 	ps.adicionarLong(id_ServentiaCentralMandado);
					Sql+= "  	WHERE UPPER(R.REGIAO) LIKE '%GENÉRICA%') "; 							//Por enquanto o único parâmetro para identificar a região genérica é o nome.
						  //Mesmo a região sendo genérica, obedece ao tipo de mandado da escala
					Sql+= "AND E.ID_MAND_TIPO = ? AND E.TIPO_ESPECIAL = ? ) ";								ps.adicionarLong(id_MandadoTipo);	ps.adicionarLong(0);
				}
			
			}
			
			Sql+= ") and e.id_esc = ";
			
			if(Funcoes.StringToInt(id_MandadoTipo) == MandadoTipoDt.PLANTAO){
				//Se for um mandado to tipo Plantão, utiliza diretamente a escala de plantão da Central
				Sql+= "(SELECT ID_ESC FROM ESC ES WHERE ES.ID_SERV = ? AND ES.ID_MAND_TIPO = ?)";
				ps.adicionarLong(id_ServentiaCentralMandado);
				ps.adicionarLong(MandadoTipoDt.PLANTAO);
			}
			else {
				
				if(escalaTipoCodigo == EscalaTipoDt.ASSISTENCIA){
					//Se for um mandado sem custas, pesquisa a escala de acordo com a região do bairro do endereço da parte.
					Sql+= " (Select Id_Esc From Esc Es Where Es.Id_Regiao = ? And Es.Id_Serv= ? And Es.ID_MAND_TIPO = ? And Es.TIPO_ESPECIAL = ? AND Es.ID_ESCALA_TIPO = ?) ";       
					ps.adicionarLong(id_regiao);
					ps.adicionarLong(id_ServentiaCentralMandado);
					ps.adicionarLong(id_MandadoTipo);
					ps.adicionarLong(0);
					ps.adicionarLong(escalaTipoCodigo);
				}
				else {
					Sql+= "		  (SELECT E.ID_ESC ";
					Sql+= "		      FROM ESC E ";
					Sql+= "		      WHERE E.ID_REGIAO = ";
					Sql+= " 			(SELECT R.ID_REGIAO ";
					Sql+= "				FROM REGIAO R ";
					Sql+= "				INNER JOIN COMARCA C ";
					Sql+= "				ON R.ID_COMARCA = C.ID_COMARCA ";
					Sql+= "				INNER JOIN SERV S ";
					Sql+= "				ON S.ID_COMARCA = C.ID_COMARCA ";
					Sql+= "				AND S.ID_SERV   = ? ";						ps.adicionarLong(id_ServentiaCentralMandado);
					Sql+= "				WHERE UPPER(R.REGIAO) LIKE '%GENÉRICA%' ";
					Sql+= "				) ";
					Sql+= "			AND E.ID_MAND_TIPO  = ? ";						ps.adicionarLong(id_MandadoTipo);
					Sql+= "			AND E.TIPO_ESPECIAL = ? ";						ps.adicionarLong(0);
					Sql+= ") ";
				}
			
			}
			
			Sql+= " order by  E.Id_Serv_Cargo_Esc asc) CONSULTA2 WHERE rownum = 1";
																							   
			rs1 = consultar(Sql, ps);
			
			if (rs1.next()){
				stProximoOficial[0] = rs1.getString("id_usu_serv");
				stProximoOficial[1] = rs1.getString("id_serv_cargo");
				stProximoOficial[2] = rs1.getString("id_serv_cargo_esc");
				stProximoOficial[3] = rs1.getString("id_esc");
			}else{
				throw new MensagemException("Não foi possivel localizar uma escala ou um oficial. Verifique o cadastro e tente novamente");
			}			
			
						
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return stProximoOficial; 
	}
	/*
	 * A distribuição para um escala deve seguir uma fila, usando a ordem do
	 *
	 * cadastro na tabela serv_cargo_esc pegue no ultimo mandado emitido o id do
	 * 
	 * serv_cargo_esc, retorne o proximo oficial da escala, se esse for o ultimo
	 *
	 * retorne o primeiro da escala.
	 * 
	 * 
	 * @fvmeireles 24/04/2020
	 */

//	public String[] distribuicaoEscala(String idRegiao, String idServ, String idMandTipo, int idEscTipo)
//			throws Exception {
// 
//		String idUsuServ = "";
//		String idEsc = "";
//    	String dadosUsuReceb = "";
//		
//		String[]  array = {"",""};		
//		String[] stProximoOficial = { "", "", "", "" };
//
//		StringBuffer sql = new StringBuffer();
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();	
//
//		try {
//
//			// BUSCA ULTIMO MANDADO DISTRIBUIDO COM OS PARAMETROS RECEBIDOS
//
//			sql.append(" SELECT mj.id_esc AS idEsc, mj.id_usu_serv_1 AS idUsuServ1"
//					+ " FROM projudi.mand_jud mj WHERE mj.id_mand_jud IN "
//					//
//					+ " (SELECT  MAX(mj.id_mand_jud)"
//					+ "	FROM projudi.view_mand_jud mj INNER JOIN projudi.esc e ON e.id_esc = mj.id_esc"
//					+ "	WHERE e.id_mand_tipo = ? AND e.id_escala_tipo = ? AND e.id_serv = ?"
//					+ "	AND	e.id_regiao = ?)");   		
//
//			ps.adicionarLong(idMandTipo);
//			ps.adicionarLong(idEscTipo);
//			ps.adicionarLong(idServ);
//			ps.adicionarLong(idRegiao);
//		 
//			rs1 = consultar(sql.toString(), ps);
//			
//			if (rs1.next()) {
//				idEsc = rs1.getString("idEsc");
//			 	idUsuServ = rs1.getString("idUsuServ1");			
//			}			
//					
//			if (idUsuServ != null && !idUsuServ.equals("")) {
//
//				// ACHOU MANDADO - BUSCA ID_SERV_CARGO_ESC DO OFICIAL E
//				// DEPOIS BUSCA O PROXIMO A RECEBER
//				
//				sql = new StringBuffer();
//				rs1 = null;
//				ps = new PreparedStatementTJGO();
//			
//				sql.append("SELECT tab.*,"
//					+ "( SELECT sce.id_usu_serv || '#' || sc.id_serv_cargo"   
//					+ " FROM projudi.view_serv_cargo_esc sce"
//					+ " INNER JOIN projudi.serv_cargo sc ON sc.id_serv_cargo = sce.id_serv_cargo"
//					+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = sce.id_usu_serv"
//					+ " WHERE sce.id_serv_cargo_esc = tab.idServCargoEscReceb) AS dadosUsuReceb FROM"
//					//
//					+ "	(SELECT MIN(sce.ID_SERV_CARGO_ESC)  AS idServCargoEscReceb"   
//					+ "	FROM projudi.view_serv_cargo_esc sce"
//					+ "	INNER JOIN projudi.esc e ON e.id_esc = sce.id_esc"
//					+ "	INNER JOIN projudi.SERV_CARGO_ESC_STATUS SCES  ON"
//					+ "	sces.id_serv_cargo_esc_status  = sce.id_serv_cargo_esc_status"
//					+ "	WHERE e.ativo = ? AND sces.serv_cargo_esc_status_codigo = ?  AND e.plantao <> ?"
//					+ "	AND e.id_esc= ? AND sce.id_serv_cargo_esc >"
//					//
//					+ " (SELECT  sce.id_serv_cargo_esc As idServCargoEsc"    
//					+ "	FROM projudi.view_serv_cargo_esc sce "
//					+ "	INNER JOIN projudi.esc e ON e.id_esc = sce.id_esc"
//					+ "	WHERE sce.id_usu_serv = ?"
//					+ "	AND sce.id_esc = ? AND e.plantao <> ?)) tab"); 			
//				
//				ps.adicionarLong(EscalaDt.ATIVO);
//				ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
//				ps.adicionarLong(EscalaDt.PLANTAO);
//				ps.adicionarLong(idEsc);
//				ps.adicionarLong(idUsuServ);
//				ps.adicionarLong(idEsc);
//				ps.adicionarLong(EscalaDt.PLANTAO);
//
//				rs1 = consultar(sql.toString(), ps);
//				
//				if (rs1.next()) {
//					if (rs1.getString("dadosUsuReceb") != null) {
//				       dadosUsuReceb = rs1.getString("dadosUsuReceb");		
//				       array = dadosUsuReceb.split("#");						
//					   stProximoOficial[0] = array[0];
//					   stProximoOficial[1] = array[1];
//					   stProximoOficial[2] = rs1.getString("idServCargoEscReceb");
//					   stProximoOficial[3] = idEsc;				
//				   }
//				}	
//			}
//			
//			if (stProximoOficial[0] != null && stProximoOficial[0].equalsIgnoreCase("")) {   
//
//				// NAO ACHOU - ULTIMO MANDADO OU O ID_SERV_CARGO_ESC ERA O ULTIMO DA FILA.
//				// PEGA O PRIMEIRO DA FILA COM OS PARAMETROS RECEBIDOS
//
//				sql = new StringBuffer();
//				rs1 = null;
//				ps = new PreparedStatementTJGO();
//
//				sql.append("SELECT tab.*,"
//						//
//						+ " (SELECT sce.id_usu_serv || '#' || sce.id_serv_cargo"
//						+ "	FROM projudi.view_serv_cargo_esc sce"
//						+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = sce.id_usu_serv"
//						+ "	WHERE sce.id_serv_cargo_esc =  tab.idServCargoEscReceb"
//						+ "	AND us.ativo = ?)  AS dadosUsuReceb,"
//						//
//						+ " (SELECT sce.id_esc FROM projudi.view_serv_cargo_esc sce "
//						+ "  WHERE sce.id_serv_cargo_esc = tab. idServCargoEscReceb) as idEsc FROM"						
//						//
//						+ "	(SELECT MIN(sce.ID_SERV_CARGO_ESC) as idServCargoEscReceb"
//						+ "	FROM projudi.view_serv_cargo_esc sce"
//						+ "	INNER JOIN projudi.esc e on e.id_esc = sce.id_esc"
//						+ " INNER JOIN projudi.serv_cargo_esc_status sces  ON" 
//						+ " sces.id_serv_cargo_esc_status  = sce.id_serv_cargo_esc_status"
//						+ " WHERE sce.id_serv_cargo_esc >  0"
//						+ "	AND e.ativo = ? AND sces.serv_cargo_esc_status_codigo = ? AND e.plantao <> ?"
//						+ "	AND e.id_mand_tipo = ? AND e.id_escala_tipo = ? AND e.id_serv = ? AND e.id_regiao = ?) tab");
//
//				ps.adicionarLong(UsuarioServentiaDt.ATIVO);
//				ps.adicionarLong(EscalaDt.ATIVO);
//				ps.adicionarLong(ServentiaCargoEscalaStatusDt.ATIVO);
//				ps.adicionarLong(EscalaDt.PLANTAO);
//				ps.adicionarLong(idMandTipo);
//				ps.adicionarLong(idEscTipo);
//				ps.adicionarLong(idServ);
//				ps.adicionarLong(idRegiao);
//
//				rs1 = consultar(sql.toString(), ps);
//
//				if (rs1.next()) {
//					if (rs1.getString("dadosUsuReceb") != null) {
//				    	dadosUsuReceb = rs1.getString("dadosUsuReceb");
//				    	array = dadosUsuReceb.split("#");
//					    stProximoOficial[0] = array[0];
//				    	stProximoOficial[1] = array[1];
//				    	stProximoOficial[2] = rs1.getString("idServCargoEscReceb");
//				    	stProximoOficial[3] = rs1.getString("idEsc");
//				    }
//				}
//			}
//
//			if (stProximoOficial[0].equalsIgnoreCase("")) {
//				throw new MensagemException(
//						"Não foi possivel localizar uma escala ou um oficial. Verifique o cadastro e tente novamente");
//			}
//		}		
//				
//		finally
//
//		{
//			try {
//				if (rs1 != null)
//					rs1.close();
//			} catch (Exception e1) {
//			}
//		}
//		return stProximoOficial;
//	}

	
	public String consultarIdEscalaPlantao(String idCentralMandado) throws Exception {
		String idEscalaPlantao = null;
		String Sql;		
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		try{											
				
			Sql= " SELECT ID_ESC FROM PROJUDI.VIEW_ESCALA WHERE ID_SERV = ? AND MAND_TIPO_CODIGO = ?";
			ps.adicionarLong(idCentralMandado);
			ps.adicionarLong(MandadoTipoDt.PLANTAO);
			
			rs1 = consultar(Sql, ps);
			
			if (rs1.next()){
				idEscalaPlantao = rs1.getString("ID_ESC");
			}		
			
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		
		return idEscalaPlantao;
	}
	
	public String consultarIdEscalaAdhoc(String idCentralMandado) throws Exception {
		String idEscalaAdHoc = null;
		String Sql;		
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		try{											
				
			Sql= " SELECT ID_ESC FROM PROJUDI.ESC WHERE ID_SERV = ? AND TIPO_ESPECIAL = ?";
			ps.adicionarLong(idCentralMandado);
			ps.adicionarLong(EscalaDt.TIPO_ESPECIAL_ADHOC);
			
			rs1 = consultar(Sql, ps);
			
			if (rs1.next()){
				idEscalaAdHoc = rs1.getString("ID_ESC");
			}		
			
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		
		return idEscalaAdHoc;
	}
	
	/**
	 * Caso exista uma escala do tipo_especial normal que já contenha os mesmo dados dos parâmetros, retorna seu nome.
	 * caso não encontre, retorna null.
	 * @param id_regiao
	 * @param id_ServentiaCentralMandado
	 * @param id_mandadoTipo
	 * @param id_escala_tipo
	 * @return
	 * @throws Exception
	 */
	public String consultarNomeEscalaNormal(String id_regiao, String id_ServentiaCentralMandado, String id_mandadoTipo, String id_escala_tipo) throws Exception {
		String Sql;
		String nomeEscalaNormal = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try {

			Sql = " Select esc From Esc Es Where Es.Id_Regiao = ? And Es.Id_Serv= ? And Es.Id_Mand_Tipo = ? AND Es.id_escala_tipo = ? AND Es.tipo_especial = ?";
			ps.adicionarLong(id_regiao);
			ps.adicionarLong(id_ServentiaCentralMandado);
			ps.adicionarLong(id_mandadoTipo);
			ps.adicionarLong(id_escala_tipo);
			ps.adicionarLong(EscalaDt.TIPO_ESPECIAL_NORMAL);
			rs1 = consultar(Sql, ps);

			if (rs1.next()) {
				nomeEscalaNormal = rs1.getString("esc");
			}

		} finally {
            try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
		}
		return nomeEscalaNormal;
	}
	
}
