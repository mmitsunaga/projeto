<?xml version="1.0" encoding="iso-8859-1"?>
<%@page import="java.util.List"%>
<resposta versao='1' operacao='<%=(String)request.getAttribute("Operacao")%>'>
	<situacao>OK</situacao>
	<mensagem>	
		<%if(request.getAttribute("Paginacao")!=null){%>
		<paginacao><%=((String)request.getAttribute("Paginacao"))!=null?(String)request.getAttribute("Paginacao"):""%></paginacao><%}%>	
		<lista>
		<%	String[] stAtributos = (String[])request.getAttribute("AtributosTag");
			List lisValores = (List)request.getAttribute("Valores");
			for (int i=0; i < lisValores.size();i++){
				out.print("\t<" +(String)request.getAttribute("Tag")+ ">\n");
				String[] stValor =(String[])lisValores.get(i);
				for (int j=0; j < stAtributos.length;j++){ 
					out.print("\t\t\t\t<" + stAtributos[j] + ">" + stValor[j] + "</" + stAtributos[j] + ">\n");
				}
				out.print("\t\t\t</" +(String)request.getAttribute("Tag")+ ">\n\t\t");			
			}
		%>
		</lista>
	</mensagem>
</resposta>
<%-- Operação - número correspondente a resposta da operação
     Tag - tag do xml de resposta da mensagem
     AtributosTag - vetor de strig com os nomes dos atributos da Tag
     Valores - lista de vetores de string com cada valor de cada atribudo da Tag
     Paginação - quantidade de páginas existentes --%>	