package br.gov.go.tj.projudi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.ne.EscalaNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoEscalaNe;
import br.gov.go.tj.utils.Funcoes;

public class  DistribuicaoMandado {

	private Map mapDistribuicao = null;
	private Map mapPonteiro;
	private Map mapProximoFixo;
	
	private static DistribuicaoMandado objeto = null;

	private DistribuicaoMandado() throws Exception{
		lerDados();
	}

	public static DistribuicaoMandado getInstance() throws Exception {
		if (objeto == null) 
			objeto = new DistribuicaoMandado();

		return objeto;
	}
	
	public void setDistribuicaoFixa(String id_escala, String id_usuarioserventia) {
	    List lisTemp = null;
		if (mapProximoFixo.get(id_escala) == null){
		    lisTemp = new ArrayList();
		    lisTemp.add(Funcoes.StringToInt( id_usuarioserventia));
		    mapProximoFixo.put(id_escala, lisTemp);
		} else{
		    lisTemp = (List) mapProximoFixo.get(id_escala);
		    lisTemp.add(id_usuarioserventia);
		}		
	}

	public synchronized String getDistribuicao(String id_escala, Integer id_oficial) throws Exception {
		String stRetorno = "";			
		
		//vejo se existe alguma distribuição fixa, se existir pego a primeira da lista, depois limpo essa possicao, se a lista ficar 
        // vazia igualo ela a null
		if (mapProximoFixo.get(id_escala) != null) {
		    List lisTemp = (List) mapProximoFixo.get(id_escala);			    
			stRetorno = (String) lisTemp.get(0);
			lisTemp.remove(0);
			if (lisTemp.size()==0) lisTemp = null;
			//mapProximoFixo.put(id_areadistribuicao, null);
		} else {

			List Lista = (List) mapDistribuicao.get(id_escala);

			int inPonteiro = (Integer) mapPonteiro.get(id_escala);
			
			//Quando for mandado com companheiro não pode repetir
			int inPonteiroAux = inPonteiro;
			if( id_oficial != null ) {
				if (Lista != null) {
					for (int i = --inPonteiro; i < Lista.size(); i++) {
						if( i < 0)
							i = 0;
						int idOficial = (Integer) Lista.get(i);
						if (idOficial == id_oficial) {
							inPonteiroAux++;
							if( inPonteiroAux >= Lista.size() )
								inPonteiroAux = 0;
						}
						else
							break;
					}
				}
			}
			
			//proxima distribuicao
			stRetorno = String.valueOf(Lista.get(inPonteiroAux));
			
			//passo para a proxima posição
			inPonteiro++;
			
			//se não existir passo para a primeira
			if (inPonteiro >= Lista.size()) 
				inPonteiro = 0;
			//guardo a nova posição do ponteiro
			mapPonteiro.put(id_escala, inPonteiro);
		}
		
		return stRetorno;
	}
	
	public synchronized  void lerDados() throws Exception {
		List tempListaEscala = null;
		List tempListaUsuariosEscala = null;
		// vetor que contem a ultima vara a receber processo e a quantidade de
		// processos ja recebidos.
		int UltimaDistribuicao[];			   
		
		EscalaNe neEscala = new EscalaNe();
		// pego todas as areas de distribuicao de processo
		tempListaEscala = neEscala.consultarEscalasAtivas();

		mapDistribuicao = new HashMap();
		mapPonteiro = new HashMap();
		mapProximoFixo = new HashMap();
		
		//UsuarioServentiaEscalaNe para consultar os usuários de cada escala
		ServentiaCargoEscalaNe neUsuarioServentiaEscala = new ServentiaCargoEscalaNe();
		
		//PendenciaNe para consultar a última distribuição da escala
		PendenciaNe nePendencia = new PendenciaNe();
		
		// para cada area de distribuicao pego todas as serventias
		for (int i = 0; i < tempListaEscala.size(); i++) {

			List relacaoEscalas = new ArrayList();
			String id_escala = ((EscalaDt) tempListaEscala.get(i)).getId();
			//consulta os usuarios da escala
			tempListaUsuariosEscala = neUsuarioServentiaEscala.consultarUsuariosServentiaEscala(id_escala);
			//consulta a ultima Escala a receber uma pendencia, e quantos ela recebe
			UltimaDistribuicao = nePendencia.consultarUltimaDistribuicaoMandado(id_escala);
			// incluo cada serventia o numero de vezes que ela tem de
			// distribuicao

			for (int j = 0; j < tempListaUsuariosEscala.size(); j++) {
				
				ServentiaCargoEscalaDt dtUsuarioServentiaEscala = (ServentiaCargoEscalaDt) tempListaUsuariosEscala.get(j);

				for (int k = 0; k < Funcoes.StringToInt(dtUsuarioServentiaEscala.getQuantidadeDistribuicao()); k++)
					relacaoEscalas.add(Funcoes.StringToInt(dtUsuarioServentiaEscala.getId()));
				
			}

			int inPonteiro = 0;
			//verifico o ponteiro a proxima serventia a receber um processo
			int inQtdVaras = relacaoEscalas.size();
			for (int j = 0; j < inQtdVaras; j++) {
				int id_serventia = (Integer) relacaoEscalas.get(j);
				// coloco o ponteiro na posição da proxima distribuição.										
				// quando encontrar o primeiro, verifica se acrescentando a quantidade que ja foi distribuida 
				// para ele, se o ponteiro ainda está dentro do vetor de distribuição
				// Ex. [aaaabbbcdef]
                // procurando o 'a' entao o j será 0 e ja foi distribuido para o 'a' 2
				// assim 0 + 2 é menor que 11
				// o ponteiro passa para 2 que ainda é 'a'
				// agora se ja estive distribuido 4 para o a o ponteiro iria para 4 e a distribuição para o b
				// se fosse 'f' e já distribuido 1 o ponteiro iria para 0 ou seja voltaria para o 'a'
				if ((UltimaDistribuicao[0] == id_serventia) && ((j + (UltimaDistribuicao[1])) < inQtdVaras)) 
				    inPonteiro = j + (UltimaDistribuicao[1]);
			}

			// adiciono a relacao de varas de uma area distribuicao
			mapDistribuicao.put(id_escala, relacaoEscalas);
			// adiciono a relacao de area com a proxima vara a receber
			mapPonteiro.put(id_escala, inPonteiro);
			// caso ocorra algum erro adiciono no map Proximo fixo a proxima distribuicao
			mapProximoFixo.put(id_escala, null);

		}
	}
	
	public Map getMapDistribuicao() {
		return this.mapDistribuicao;
	}
	
	public Map getMapPonteiro() {
		return this.mapPonteiro;
	}
	
	public Map getMapProximoFixo() {
		return this.mapProximoFixo;
	}
}
