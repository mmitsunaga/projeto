<%@page language="java" contentType="text/xml" pageEncoding="iso-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>

<?xml version="1.0" encoding="iso-8859-1"?>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<%List responsaveis = (List) request.getAttribute("ListaResponsaveis");
	List advogados = (List) request.getAttribute("ListaAdvogados");
	if(responsaveis != null && responsaveis.size() > 0){%>
	<responsaveis>
		<%for(int i=0;i<responsaveis.size();i++){
		ServentiaCargoDt serventiaCargoDt = (ServentiaCargoDt) responsaveis.get(i);%>
		<responsavel>
			<cargo><%=serventiaCargoDt.getCargoTipo()%></cargo>
			<nome><%=serventiaCargoDt.getNomeUsuario()%></nome>
			<serventia><%=serventiaCargoDt.getServentia()%></serventia>
		</responsavel>
	<%}%>
	</responsaveis>
	<%}
	if(advogados != null && advogados.size() > 0){%>
	<advogados>
	<%for(int i=0;i<advogados.size();i++){
	ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) advogados.get(i);%>
		<advogado>
			<advogado><%=processoParteAdvogadoDt.getNomeAdvogado()%></advogado>
			<oabMatricula><%=processoParteAdvogadoDt.getOabNumero()+ " " + processoParteAdvogadoDt.getOabComplemento()%></oabMatricula>			                  	
			<dativo><%=processoParteAdvogadoDt.mostrarDativo()%></dativo>
			<recebeIntimacao><%=processoParteAdvogadoDt.mostrarRecebeIntimacao()%></recebeIntimacao>
			<dataHabilitacao><%=processoParteAdvogadoDt.getDataEntrada()%></dataHabilitacao>
			<serventia><%=processoParteAdvogadoDt.getServentiaHabilitacao()%></serventia>
			<parte><%=processoParteAdvogadoDt.getNomeParte()+ " - " +processoParteAdvogadoDt.getProcessoParteTipo()%></parte>
		</advogado>
	<%}%>
	</advogados>
	<%}%>	
</resposta>