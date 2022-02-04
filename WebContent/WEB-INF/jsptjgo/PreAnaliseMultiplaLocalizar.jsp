<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>

<jsp:useBean id="UsuarioSessao" class= "br.gov.go.tj.projudi.ne.UsuarioNe" scope="session"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Autos Conclusos  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo">
		<div class="area"><h2>&raquo; Busca de Pré-Análises Múltiplas</h2> </div>
		<div id="divLocalizar" class="divLocalizar"> 
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>">

				<div id="divTabela" class="divTabela" > 
					<table id="Tabela" class="Tabela">
						<thead>
							<tr class="TituloColuna">
								<th width="3%"></th>
								<th width="15%">Data/Hora</th>
								<th>Usuário Responsável</th>
								<th>Processos Pré-analisados</th>
								<th align="center" width="20%" colspan="4">Ações</th>								
							</tr>
						</thead>
						<tbody>	
						<%
						List liTemp = (List)request.getAttribute("ListaPreAnalises");
						String estiloLinha="TabelaLinha1";
						String id_Arquivo = "";
						String id_ArquivoAnterior = "";
						int contLotes = 0;
						int contPendencias = 0;
						if (liTemp !=null){
							for(int i = 0 ; i< liTemp.size();i++) {
								PendenciaArquivoDt objTemp = (PendenciaArquivoDt)liTemp.get(i); 
								
								id_Arquivo = objTemp.getArquivoDt().getId();
								
								if (!id_ArquivoAnterior.equals(id_Arquivo)){
									contLotes++;
									contPendencias = 1;
									int j = i+1;
									while (j < liTemp.size()){
										PendenciaArquivoDt objAux = (PendenciaArquivoDt)liTemp.get(j); 
										if (objAux.getArquivoDt().getId().equals(id_Arquivo)){
											contPendencias++;
										} else break;
										j++;
									}
						%>
							<tr align="center" class="<%=estiloLinha%>">
								<td rowspan="<%=contPendencias%>"> <%=contLotes%></td>
		                   		<td rowspan="<%=contPendencias%>"><%= objTemp.getArquivoDt().getDataInsercao()%></td>
		                   		<td rowspan="<%=contPendencias%>"><%= (!objTemp.getAssistenteResponsavel().equals("")?objTemp.getAssistenteResponsavel():objTemp.getJuizResponsavel())%></td>
		                   		<td><%=objTemp.getProcessoNumero()%></td>
								<% 	if (request.getAttribute("podeAnalisar").toString().equalsIgnoreCase("true")){ %>
		                   		<td rowspan="<%=contPendencias%>">
		                   			<a href="AnalisarConclusao?PaginaAtual=<%=Configuracao.Curinga6%>&Id_Arquivo=<%=objTemp.getArquivoDt().getId()%>&preAnalise=true">Analisar</a>     
		                   		</td>
		                   		<% } %>
		                   		<td rowspan="<%=contPendencias%>">
		                   			<a href="PreAnalisarConclusao?PaginaAtual=<%=Configuracao.Curinga8%>&Id_Arquivo=<%=objTemp.getArquivoDt().getId()%>">Refazer</a>     
		                   		</td>
		                   		<td rowspan="<%=contPendencias%>">
		                   			<a href="PreAnalisarConclusao?PaginaAtual=<%=Configuracao.Excluir%>&Id_Arquivo=<%=objTemp.getArquivoDt().getId()%>">Descartar</a>     
		                   		</td>
		                   		<td rowspan="<%=contPendencias%>">
			                   		<% if (UsuarioSessao.isPodeTrocarResponsavelDistribuicao()){ %>
			                   			<a href="DistribuicaoPendencia?PaginaAtual=<%=Configuracao.Novo%>&Id_Arquivo=<%=objTemp.getArquivoDt().getId()%>">Distribuir</a>    
	                  				<% } %>
                  				</td>                   				                   		
	               			</tr>
						<%
								} else { 
						%>
								<tr class="<%=estiloLinha%>">
									<td align="center"><%=objTemp.getProcessoNumero()%></td>
								</tr>
						<%			
								}
								id_ArquivoAnterior = id_Arquivo;
							}
						}
						%>
						</tbody>
					</table>
				</div> 
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>  
	</div>
</body>
</html>