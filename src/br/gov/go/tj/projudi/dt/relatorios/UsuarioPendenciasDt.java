package br.gov.go.tj.projudi.dt.relatorios;

public class UsuarioPendenciasDt {
    long Id_Serventia;
    String Serventia;
    int Ano;
    int Mes;
    String Id_Usuario;
    String Usuario;
    String Pendencia;
    long Quantidade;
    final private String[] MES = {"","Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
    
    public long getId_Serventia() {
        return Id_Serventia;
    }
    public void setId_Serventia(long id_Serventia) {
        Id_Serventia = id_Serventia;
    }
    public String getServentia() {
        return Serventia;
    }
    public void setServentia(String serventia) {
        Serventia = serventia;
    }
    public int getAno() {
        return Ano;
    }
    public void setAno(int ano) {
        Ano = ano;
    }
    public String getMes() {
        return MES[Mes];
    }
    public void setMes(int mes) {
        Mes = mes;
    }
    public String getId_Usuario() {
        return Id_Usuario;
    }
    public void setId_Usuario(String id_Usuario) {
        Id_Usuario = id_Usuario;
    }
    public String getUsuario() {
        return Usuario;
    }
    public void setUsuario(String usuario) {
        Usuario = usuario;
    }
    public String getPendencia() {
        return Pendencia;
    }
    public void setPendencia(String pendencia) {
        Pendencia = pendencia;
    }
    public long getQuantidade() {
        return Quantidade;
    }
    public void setQuantidade(long quantidade) {
        Quantidade = quantidade;
    }

}
