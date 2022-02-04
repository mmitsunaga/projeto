package br.gov.go.tj.projudi.dt.relatorios;

public class ServentiaProcessosDt {
    String Serventia ="";
    String Id_Serventia ="";
    Long QtdProcessosDistribuidos;
    Long QtdProcessosArquivados;
    Long QtdProcessosAtivos;
    
    public String getServentia() {
        return Serventia;
    }
    public void setServentia(String serventia) {
        Serventia = serventia;
    }
    public String getId_Serventia() {
        return Id_Serventia;
    }
    public void setId_Serventia(String id_Serventia) {
        Id_Serventia = id_Serventia;
    }
    public long getQtdProcessosDistribuidos() {
        return QtdProcessosDistribuidos;
    }
    public void setQtdProcessosDistribuidos(long qtdProcessosDistribuidos) {
        QtdProcessosDistribuidos = qtdProcessosDistribuidos;
    }
    public long getQtdProcessosArquivados() {
        return QtdProcessosArquivados;
    }
    public void setQtdProcessosArquivados(long qtdProcessosArquivados) {
        QtdProcessosArquivados = qtdProcessosArquivados;
    }
    public long getQtdProcessosAtivos() {
        return QtdProcessosAtivos;
    }
    public void setQtdProcessosAtivos(long qtdProcessosAtivos) {
        QtdProcessosAtivos = qtdProcessosAtivos;
    }
    
}
