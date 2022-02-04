package br.gov.go.tj.projudi.ne;

import java.util.Date;

import br.gov.go.tj.projudi.dt.QuantidadeDias;
import br.gov.go.tj.projudi.ps.EstatisticaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaNe extends Negocio {

    private static final long serialVersionUID = 3401524484150532434L;        

    public EstatisticaNe() {       

        obLog = new LogNe();         

    }
    
    public void gerarEstatisticaMes(int anoEstatistica, int mesEstatistica) throws Exception{
        
            FabricaConexao obFabricaConexao = null;
            int anoFinal =anoEstatistica;
            int mesFinal =mesEstatistica+1;
            
            //se o mes final for 13 passar para o mes 1 do proximo ano
            if( mesFinal ==13){
            	mesFinal=1;
            	anoFinal = anoEstatistica +1;
            }                                   
            
            try{
                obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
                EstatisticaPs obPersistencia = new EstatisticaPs(obFabricaConexao.getConexao());

                obPersistencia.gerarEstatisticaMes(anoEstatistica, mesEstatistica, anoFinal, mesFinal);
            
            } finally{
                obFabricaConexao.fecharConexao();
            }            
        }
    
     
    public void gerarEstatisticaResolucao76(int ano, int mes) throws Exception{
        FabricaConexao obFabricaConexao = null;
        Date dataInicial = null, dataFinal = null;
        
        if(mes == 1){
            mes = 12;
            ano = ano - 1;
        }else{
            mes = mes - 1;                
        }
        dataInicial = Funcoes.getPrimeiroDiaMes(mes, ano);
        dataFinal = Funcoes.getUltimoDiaHoraMinutoSegundoMes(String.valueOf(mes), String.valueOf(ano));
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstatisticaPs obPersistencia = new  EstatisticaPs(obFabricaConexao.getConexao());
        
        obPersistencia.gerarEstatisticaResolucao76(dataInicial, dataFinal, String.valueOf(ano), String.valueOf(mes));
        
        } finally{
            obFabricaConexao.fecharConexao();
        }            
    }

	public String gerarJsonDistribuicaoProcessoXDiasComarcaServentiaServentiaCargo(String id_serv,  QuantidadeDias qtdDias) throws Exception {
        FabricaConexao obFabricaConexao = null;
        String stTemp = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstatisticaPs obPersistencia = new  EstatisticaPs(obFabricaConexao.getConexao());
        
            stTemp=obPersistencia.gerarJsonDistribuicaoProcessoXDiasComarcaServentiaServentiaCargo(id_serv,qtdDias);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
	}

	public String gerarJsonDistribuicaoProcessoXDiasComarcaServentia(String id_serv,  QuantidadeDias qtdDias) throws Exception {
        FabricaConexao obFabricaConexao = null;
        String stTemp = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstatisticaPs obPersistencia = new  EstatisticaPs(obFabricaConexao.getConexao());
        
            stTemp=obPersistencia.gerarJsonDistribuicaoProcessoXDiasComarcaServentia(id_serv,qtdDias);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
	}
            
	public String gerarJsonDistribuicaoProcessoXDiasComarca(String id_serv,  QuantidadeDias qtdDias) throws Exception {
        FabricaConexao obFabricaConexao = null;
        String stTemp = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            EstatisticaPs obPersistencia = new  EstatisticaPs(obFabricaConexao.getConexao());
        
            stTemp=obPersistencia.gerarJsonDistribuicaoProcessoXDiasComarca(id_serv,qtdDias);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
	}
	
}
