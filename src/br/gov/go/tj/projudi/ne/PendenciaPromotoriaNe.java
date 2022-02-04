package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.ps.PendenciaPromotoriaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class PendenciaPromotoriaNe {

    public List consultarIntimacoesPromotoria(UsuarioNe usuarioNe, String procNum) throws Exception {
        List pendencias = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            PendenciaPromotoriaPs ps = new PendenciaPromotoriaPs(obFabricaConexao.getConexao());
            pendencias = ps.consultarIntimacoesPromotoriaCompleto(usuarioNe, procNum);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencias;
    }
    
    public PendenciaDt acrescentaProcessoDt(PendenciaDt pendencia) throws Exception {
        FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
        try {
            PendenciaPromotoriaPs ps = new PendenciaPromotoriaPs(obFabricaConexao.getConexao());
            pendencia = ps.acrescentaProcessoDt(pendencia);
        } finally {
            obFabricaConexao.fecharConexao();
        }

        return pendencia;
    }
    
}
