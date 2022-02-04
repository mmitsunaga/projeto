package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.ServentiaPs;
import br.gov.go.tj.projudi.ps.ServentiaRelacionadaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ServentiaRelacionadaNe extends ServentiaRelacionadaNeGen {

    //

    /**
     * 
     */
    private static final long serialVersionUID = 366414015564240171L;

    // ---------------------------------------------------------
    public String Verificar(ServentiaRelacionadaDt dados) {

        String stRetorno = "";

        if (dados.getServentiaRelacionada().length() == 0) stRetorno += "O Campo ServentiaRelacionada é obrigatório.";
        if (dados.getServentiaPrincipal().length() == 0) stRetorno += "O Campo ServentiaPrincipal é obrigatório.";
        return stRetorno;

    }

    public void salvar(ServentiaRelacionadaDt dados, FabricaConexao obFabricaConexao) throws Exception {
        LogDt obLogDt;
        
        ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
        if (dados.getId().equalsIgnoreCase("")) {               
            obPersistencia.inserir(dados);
            obLogDt = new LogDt("ServentiaRelacionada", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
        } else {               
            obPersistencia.alterar(dados);
            obLogDt = new LogDt("ServentiaRelacionada", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
        }

        obDados.copiar(dados);
        obLog.salvar(obLogDt, obFabricaConexao);
        
    }

    /**
     * Retorna a serventia relacionada do tipo Turma Recursal
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author msapaula
     * @throws Exception 
     */
    public ServentiaDt consultarTurmaRecursalRelacionada(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.SEGUNDO_GRAU);
    }

    /**
     * Retorna a serventia relacionada do tipo Promotoria
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author msapaula
     * @throws Exception 
     */
    public ServentiaDt consultarPromotoriaRelacionada(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.PROMOTORIA);
    }
    
    /**
     * Retorna a serventia relacionada do tipo Promotoria
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param ServentiaSubtipoCodigo,
     *            identificação da serventia subtipo da serventia principal
     * 
     * @author lsbernardes
     * @throws Exception 
     */
    public ServentiaDt consultarPromotoriaRelacionada(String id_Serventia, String ServentiaSubtipoCodigo) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaSubtipoCodigo, ServentiaTipoDt.PROMOTORIA);
    }

    /**
     * Retorna a serventia relacionada do tipo Contadoria
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author msapaula
     * @throws Exception 
     */
    public ServentiaDt consultarContadoriaRelacionada(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.CONTADORIA);
    }
    
    /**
     * Retorna a serventia relacionada do tipo Undidade Administrativa
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author lsbernardes
     * @throws Exception 
     */
    public ServentiaDt consultarUnidadeAdministrativaRelacionadaCENOPES(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.UNIDADES_ADMINISTRATIVAS);
    }
    
    /**
     * Retorna a serventia relacionada do tipo Câmara de Saúde
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author lsbernardes
     * @throws Exception 
     */
    public ServentiaDt consultarCamaraSaudeRelacionada(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.CAMARA_SAUDE);
    }
    
    /**
     * Retorna a serventia relacionada do tipo Contadoria
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author asrocha
     */
    public ServentiaDt consultarCentralMandadosRelacionada(String id_Serventia) throws Exception {
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.CENTRAL_MANDADOS);
    }

    /**
     * Consulta uma lista de serventias relaciondadas do tipo especificado
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author hrrosa
     */
    public List<ServentiaDt> consultarListaCentralMandadosRelacionada(String id_Serventia) throws Exception {
        return this.consultarListaServentiaRelacionada(id_Serventia, ServentiaTipoDt.CENTRAL_MANDADOS);
    }

    
    /**
     * Consulta quais são as serventias relacionadas a serventia que foi passada
     * como parâmetro
     * 
     * @param id_Serventia:
     *            identificação da serventia
     * 
     * @author msapaula
     */
    public List consultarServentiasRel(String id_Serventia) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao =null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarServentiasRel(id_Serventia);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }

    /**
     * Consulta a serventia relacionada de um determinado tipo
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return dt da serventia relacionada
     * @author msapaula
     */
    public ServentiaDt consultarServentiaRelacionada(String id_Serventia, int tipoServentia) throws Exception {
        ServentiaDt serventiaDt = null;
        FabricaConexao obFabricaConexao =null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            serventiaDt = obPersistencia.consultarServentiaRelacionada(id_Serventia, tipoServentia);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return serventiaDt;
    }
    
    public String consultarListaServentiaRelacionadaJSON(String id_Serventia, int tipoServentia, String posicao, int qtdeColunas) throws Exception {
        String retorno = null;
        FabricaConexao obFabricaConexao =null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            retorno = obPersistencia.consultarListaServentiaRelacionadaJSON(id_Serventia, tipoServentia, posicao, qtdeColunas);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return retorno;
    }
    
    /**
     * Consulta uma lista de serventias relaciondadas do tipo especificado
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return List<dt> da serventia relacionada
     * @author hrrosa
     */
    public List<ServentiaDt> consultarListaServentiaRelacionada(String id_Serventia, int tipoServentia) throws Exception {
        List<ServentiaDt> listaServentiasRelacionadasDt = new ArrayList<ServentiaDt>();
        FabricaConexao obFabricaConexao =null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            listaServentiasRelacionadasDt = obPersistencia.consultarListaServentiaRelacionada(id_Serventia, tipoServentia);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return listaServentiasRelacionadasDt;
    }
    
    /**
     * Consulta a serventia relacionada de um determinado tipo
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param ServentiaSubtipoCodigo,
     *            identificação da serventia subtipo da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return dt da serventia relacionada
     * @author lsbernardes
     */
    public ServentiaDt consultarServentiaRelacionada(String id_Serventia, String ServentiaSubtipoCodigo, int tipoServentia) throws Exception {
        ServentiaDt serventiaDt = null;
        FabricaConexao obFabricaConexao =null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            serventiaDt = obPersistencia.consultarServentiaRelacionada(id_Serventia, ServentiaSubtipoCodigo, tipoServentia);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return serventiaDt;
    }
    
    /**
     * Consulta a serventia relacionada de um determinado subtipo
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * @param tipoServentia,
     *            tipo da serventia relacionada que se deseja pesquisar
     * 
     * @return dt da serventia relacionada
     */
    public ServentiaDt consultarServentiaRelacionadaSubTipo(String id_Serventia, String serventiaSubtipoProcesso) throws Exception {
        ServentiaDt serventiaDt = null;
        FabricaConexao obFabricaConexao =null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            serventiaDt = obPersistencia.consultarServentiaRelacionadaSubTipo(id_Serventia, serventiaSubtipoProcesso);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return serventiaDt;
    }
    
    /**
     * Retorna a serventia relacionada do tipo Psico-Social Forense
     * 
     * @param id_Serventia,
     *        identificação da serventia principal
     * 
     * @author Márcio Gomes
     * @throws Exception 
     */
    public ServentiaDt consultarServentiaEquipeInterProfissionalRelacionada(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.EQUIPE_INTERPROFISSIONAL);
    }
    
    /**
     * Retorna a serventia relacionada do tipo Justiça Restaurativa
     * 
     * @param id_Serventia, identificação da serventia principal
     * 
     * @author lsbernardes
     * @throws Exception 
     */
    public ServentiaDt consultarServentiaJusticaRestaurativaRelacionada(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionada(id_Serventia, ServentiaTipoDt.JUSTICA_RESTAURATIVA);
    }
    
    /**
     * Método que realiza a consulta das Serventias Principais relacionadas à Serventia
     * informada.
     * @param id_Serventia - ID da Serventia Relação
     * @return lista de Serventias Principais
     * @throws Exception
     * @author hmgodinho
     */
    public List consultarServentiasPrincipaisRelacionadas(String id_Serventia) throws Exception {
        List serventiasRelacionadas = null;
        FabricaConexao obFabricaConexao =null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            serventiasRelacionadas = obPersistencia.consultarServentiasPrincipaisRelacionadas(id_Serventia);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return serventiasRelacionadas;
    }
    
    /**
     * Recebe o id de uma serventia do tipo CENTRAL_MANDADOS e retorna uma lista que contém a serventia que foi
     * recebida como parâmetro e também todas as serventias do tipo CENTRAL_MANDADOS relacionadas com ela. Método
     * útil para consultar quais são as centrais de mandado contíguas à que foi especificada. Feito para implementar
     * a regra que permitirá expedir mandados apenas para comarcas contíguas. A relação de comarca contígua será
     * cadastrada relacionando a central de mandados com as centrais de mandados das comarcas que deverão ser
     * consideradas contíguas à ela. 
     * @param descricao
     * @param usuarioDt
     * @param posicao
     * @return
     * @throws Exception
     * 
     * @author hrrosa
     */
	public String consultarDescricaoCentralMandadoRelacionadasJSON(String idCentralMandadosRelacionada, String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaRelacionadaPs obPersistencia = new  ServentiaRelacionadaPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoCentralMandadoRelacionadasJSON(idCentralMandadosRelacionada, descricao, usuarioDt, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
	
    /**
     * Atualiza uma serventia do Tipo Presidencia relacionada como presidência
     * 
     * @param id_SeventiaPrincipal
     * @param id_ServentiaRelacionadaPresidencia
     * @throws Exception
     * @author mmgomes
     */
    public void atualizeServentiaRelacionadaPresidencia(String id_SeventiaPrincipal, String id_ServentiaRelacionadaPresidencia) throws Exception{
    	 FabricaConexao obFabricaConexao =null;
         try{
             obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
             obFabricaConexao.iniciarTransacao();
             
             ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
             obPersistencia.atualizeServentiaRelacionadaPresidencia(id_SeventiaPrincipal, id_ServentiaRelacionadaPresidencia);
             
             obFabricaConexao.finalizarTransacao();
         } catch(Exception e) {
        	 if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
             throw e;
         } finally{
             obFabricaConexao.fecharConexao();
         }
    }
    
    /**
     * Consultar a Serventia do Tipo Presidencia Relacionada à Câmara
     * 
     * @param id_ServentiaCamara
     * @return
     * @throws Exception
     */
    public ServentiaDt consultarServentiaRelacionadaPresidencia(String id_ServentiaCamara) throws Exception {
        FabricaConexao obFabricaConexao =null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);            
            return consultarServentiaRelacionadaPresidencia(id_ServentiaCamara, obFabricaConexao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }        
    }
    
    /**
     * Consultar a Serventia do Tipo Presidencia Relacionada à Câmara
     * 
     * @param id_ServentiaCamara
     * @param obFabricaConexao
     * @return
     * @throws Exception
     */
    public ServentiaDt consultarServentiaRelacionadaPresidencia(String id_ServentiaCamara, FabricaConexao obFabricaConexao) throws Exception {
        ServentiaDt serventiaDt = null;
        FabricaConexao obFabricaConexaoLocal = null;
        
        try{
        	if (obFabricaConexao != null) obFabricaConexaoLocal = obFabricaConexao;
        	else obFabricaConexaoLocal = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        	
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexaoLocal.getConexao());
            serventiaDt = obPersistencia.consultarServentiaRelacionadaPresidencia(id_ServentiaCamara);
        
        } finally{
            if (obFabricaConexao == null) obFabricaConexaoLocal.fecharConexao();
        }
        return serventiaDt;
    }
    
    /**
     * Verifica se o id_ServentiaRelacionada é uma serventia relacionada do id_serventiaPrincipal
     * 
     * @param id_serventiaPrincipal
     * @param id_ServentiaRelacionada
     * @return
     * @throws Exception
     * @author mmgomes
     */
    public boolean isServentiaRelacionada(String id_serventiaPrincipal, String id_ServentiaRelacionada) throws Exception
	{
    	if (id_serventiaPrincipal == null || id_ServentiaRelacionada == null) return false;
    	
    	List listaServentiasRelacionadas = consultarServentiasRel(id_serventiaPrincipal);
    	for(Object serventiaRelacionada : listaServentiasRelacionadas)
    	{
    		if (((ServentiaDt)serventiaRelacionada).getId().trim().equalsIgnoreCase(id_ServentiaRelacionada.trim())) return true;
    	}
    	return false;
	}
    
    /**
     * Consulta o gabinete de segundo grau que é substituto atual do id serventia do gabinete passado como parâmetro
     * 
     * @param id_ServentiaCamaraPrincipal
     * @param id_ServentiaGabinetePrincipal
     * @return
     * @author mmgomes
     * @throws Exception
     */
    public ServentiaDt consultarGabineteSubstitutoAtualSegundoGrau(String id_ServentiaCamaraPrincipal, String id_ServentiaGabinetePrincipal) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);            
            return consultarGabineteSubstitutoAtualSegundoGrau(id_ServentiaCamaraPrincipal, id_ServentiaGabinetePrincipal, obFabricaConexao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }   
    }
    
    /**
     * Consulta o gabinete de segundo grau que é substituto atual do id serventia do gabinete passado como parâmetro
     *
     * @param id_ServentiaCamaraPrincipal
     * @param id_ServentiaGabinetePrincipal
     * @param obFabricaConexao
     * @return
     * @author mmgomes
     * @throws Exception
     */
    public ServentiaDt consultarGabineteSubstitutoAtualSegundoGrau(String id_ServentiaCamaraPrincipal, String id_ServentiaGabinetePrincipal, FabricaConexao obFabricaConexao) throws Exception {
    	ServentiaDt serventiaDt = null;
    	FabricaConexao obFabricaConexaoLocal = null;
    	
        try{
        	if (obFabricaConexao != null) obFabricaConexaoLocal = obFabricaConexao;
        	else obFabricaConexaoLocal = new FabricaConexao(FabricaConexao.PERSISTENCIA);       	
            
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexaoLocal.getConexao());
            serventiaDt = obPersistencia.consultarGabineteSubstitutoAtualSegundoGrau(id_ServentiaCamaraPrincipal, id_ServentiaGabinetePrincipal);
        
        } finally{
        	if (obFabricaConexao == null) obFabricaConexaoLocal.fecharConexao();
        }
        return serventiaDt;
    }
    
    /**
     * Consulta a lista de gabinetes de segundo grau que são substitutos atuais do id serventia do gabinete passado como parâmetro, dentro do período informado
     * 
     * @param id_ServentiaCamaraPrincipal
     * @param id_ServentiaGabinetePrincipal
     * @param DataInicial
     * @param DataFinal
     * @return
     * @author mmgomes
     * @throws Exception
     */
    public List consultarGabinetesSubstitutosSegundoGrau(String id_ServentiaCamaraPrincipal, String id_ServentiaGabinetePrincipal, String DataInicial, String DataFinal) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);            
            return consultarGabinetesSubstitutosSegundoGrau(id_ServentiaCamaraPrincipal, id_ServentiaGabinetePrincipal, DataInicial, DataFinal, obFabricaConexao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }   
    }
    
    /**
     * Consulta a lista de gabinetes de segundo grau que são substitutos atuais do id serventia do gabinete passado como parâmetro, dentro do período informado
     * 
     * @param id_ServentiaCamaraPrincipal
     * @param id_ServentiaGabinetePrincipal
     * @param DataInicial
     * @param DataFinal
     * @param obFabricaConexao
     * @return
     * @author mmgomes
     * @throws Exception
     */
    public List consultarGabinetesSubstitutosSegundoGrau(String id_ServentiaCamaraPrincipal, String id_ServentiaGabinetePrincipal, String DataInicial, String DataFinal, FabricaConexao obFabricaConexao) throws Exception {
    	List listaTemp = null;
    	FabricaConexao obFabricaConexaoLocal = null;
    	
        try{
        	if (obFabricaConexao != null) obFabricaConexaoLocal = obFabricaConexao;
        	else obFabricaConexaoLocal = new FabricaConexao(FabricaConexao.PERSISTENCIA);       	
            
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexaoLocal.getConexao());
            listaTemp = obPersistencia.consultarGabinetesSubstitutosSegundoGrau(id_ServentiaCamaraPrincipal, id_ServentiaGabinetePrincipal, DataInicial, DataFinal);
        
        } finally{
        	if (obFabricaConexao == null) obFabricaConexaoLocal.fecharConexao();
        }
        return listaTemp;
    }
    
    /**
     * Retorna a serventia relacionada do tipo Preprocessual
     * 
     * @param id_Serventia,
     *            identificação da serventia principal
     * 
     * @author mmgomes
     * @throws Exception 
     */
    public ServentiaDt consultarServentiaPreprocessualRelacionada(String id_Serventia) throws Exception{
        return this.consultarServentiaRelacionadaSubTipo(id_Serventia, String.valueOf(ServentiaSubtipoDt.PREPROCESSUAL));
    }
    
    /**
     * Consulta quais são as serventias de um tipo relacionadas a serventia que foi passada
     * como parâmetro
     * 
     * @param id_Serventia:
     *            identificação da serventia
     * 
     * @author aamoraes
     */
    public List consultarServentiasRelacionadas(String id_Serventia, int tipoServentia) throws Exception {
        List tempList = null;
        
        FabricaConexao obFabricaConexao =null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarServentiasRelacionadas(id_Serventia,tipoServentia);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }
    
    /**
     * Atualiza a ordem que a Serventia Relacionada (Gabinete) para a formação das Turmas Julgadoras nas Sessões
     * 
     * @param id_SeventiaPrincipal
     * @param id_SeventiaRelacao
     * @throws Exception
     * @author aamoraes
     */
    public void limpaOrdemTurmaJulgadora(String id_SeventiaPrincipal) throws Exception{
    	 FabricaConexao obFabricaConexao =null;
         try{
             obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
             obFabricaConexao.iniciarTransacao();
             
             ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
             obPersistencia.limpaOrdemTurmaJulgadora(id_SeventiaPrincipal);
             
             obFabricaConexao.finalizarTransacao();
         } catch(Exception e) {
        	 if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
             throw e;
         } finally{
             obFabricaConexao.fecharConexao();
         }
    }
    
    /**
     * Atualiza a ordem que a Serventia Relacionada (Gabinete) para a formação das Turmas Julgadoras nas Sessões
     * 
     * @param id_SeventiaPrincipal
     * @param id_SeventiaRelacao
     * @throws Exception
     * @author aamoraes
     */
    public void atualizeOrdemTurmaJulgadora(String id_SeventiaPrincipal, String id_SeventiaRelacao, String ordem_turma) throws Exception{
    	 FabricaConexao obFabricaConexao =null;
         try{
             obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
             obFabricaConexao.iniciarTransacao();
             
             ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
             obPersistencia.atualizeOrdemTurmaJulgadora(id_SeventiaPrincipal, id_SeventiaRelacao, ordem_turma);
             
             obFabricaConexao.finalizarTransacao();
         } catch(Exception e) {
        	 if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
             throw e;
         } finally{
             obFabricaConexao.fecharConexao();
         }
    }
    
    /**
     * Consulta quais são as serventias relacionadas a serventia que foi passada
     * como parâmetro
     * 
     * @param id_Serventia:
     *            identificação da serventia
     * 
     * @author msapaula
     */
    public ServentiaRelacionadaDt consultarId_ServentiaRelacionada(String id_serventia_principal, String id_serventia_relacionada) throws Exception {
    	ServentiaRelacionadaDt dtTempList = null;
        
        FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        try{
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            dtTempList = obPersistencia.consultarId_ServentiaRelacionada(id_serventia_principal, id_serventia_relacionada);
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return dtTempList;
    }
    
    /**
     * Consulta quais são as serventias relacionadas a serventia que foi passada
     * como parâmetro
     * 
     * @param id_Serventia:
     *            identificação da serventia
     * 
     * @author jrcorrea
     */
    public List<ServentiaRelacionadaDt> consultarServentiasRelacionadas(String id_Serventia) throws Exception {
        List<ServentiaRelacionadaDt> tempList = null;
        
        FabricaConexao obFabricaConexao =null;

        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaRelacionadaPs obPersistencia = new ServentiaRelacionadaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarServentiasRelacionadas(id_Serventia);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }
}