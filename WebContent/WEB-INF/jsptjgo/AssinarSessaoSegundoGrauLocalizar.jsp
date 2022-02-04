<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ClassificadorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaArquivoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.SessaoSegundoGrauProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Pré-Análises aguardando assinatura  </title>
	
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<link href="./css/Paginacao.css"  type="text/css"  rel="stylesheet" />
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/checks.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Pré-Análises aguardando assinatura </h2></div>
		<div id="divLocalizar" class="divLocalizar"> 
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">			
				<%@ include file="js/InsercaoArquivo.js"%>	
				<%@ include file="js/AssinarPendencia.js"%>			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="acaoSalvar" name="acaoSalvar" type="hidden" value="" />
				<input id="tipoEnvio" name="tipoEnvio" type="hidden" value="ser" />

				<div id="divTabela" class="divTabela" > 
					<table id="Tabela" class="Tabela">
						<thead id="CabecalhoTabela">
							<tr class="TituloColuna">
								<th width="3%"> </th>
								<th width="3%"><input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')" title="Marcar/Desmarcar todos os itens da lista" /> </th>
								<th width="15%">Processo</th>
								<th width="30%">Sessão</th>
								<th width="15%">Data Pré-Análise</th>
								<th width="20%">Usuário Pré-Análise</th>								
								<th width="10%">Visualizar Voto</th>
								<th width="10%">Visualizar Ementa</th>																
							</tr>
						</thead>				
					<tbody>
						<%
						List liTemp = (List)request.getAttribute("ListaSessoes");
						ProcessoDt processoDt;
						String estiloLinha="TabelaLinha1";
						if (liTemp !=null){
							for(int i = 0 ; i< liTemp.size();i++) {
								SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt = (SessaoSegundoGrauProcessoDt)liTemp.get(i);
								AudienciaProcessoDt audienciaProcessoDt = sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt();
								processoDt = audienciaProcessoDt.getProcessoDt();
								AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = null;
								PendenciaArquivoDt pendenciaArquivoDtRelatorioEVoto = sessaoSegundoGrauProcessoDt.getPendenciaArquivoDtRelatorioEVoto();
								PendenciaArquivoDt pendenciaArquivoDtEmenta = sessaoSegundoGrauProcessoDt.getPendenciaArquivoDtEmenta();
								if (processoDt == null && audienciaProcessoDt instanceof AudienciaProcessoFisicoDt) {									
									audienciaProcessoFisicoDt = (AudienciaProcessoFisicoDt)audienciaProcessoDt;
								}
								%>
								<tr class="<%=estiloLinha%>">
									<td align="center"><%=i+1 %></td>
									<td align="center">
										<input class="formEdicaoCheckBox" name="pendencias" type="checkbox" value="<%=audienciaProcessoDt.getId()%>">
									</td>
									<td align="left">
										<%if (processoDt != null ) {%>
		                   				<a href="BuscaProcesso?Id_Processo=<%=processoDt.getId()%>">		                   				
				                   			<%=Funcoes.formataNumeroProcesso(audienciaProcessoDt.getProcessoDt().getProcessoNumero())%>	              							
				                   		</a>		                   				
		                   				<%} else if (audienciaProcessoFisicoDt != null) {%>
  	                   						<%=audienciaProcessoFisicoDt.getProcessoNumero()%>&nbsp;F
	                   					<%}else{%>
  	                   						-
 	                   					<%}%>
		                   			</td>	
		                   			<td align="left">
			                   			<%=audienciaProcessoDt.getAudienciaDt().getDataAgendada()%>
			                   			<%if(audienciaProcessoDt.getProcessoTipo() != null && audienciaProcessoDt.getProcessoTipo().trim().length() > 0){%>
	              							&nbsp;<%=audienciaProcessoDt.getProcessoTipo()%>
	              						<%}%>
		                   			</td>
		                   			<td align="center"><%=pendenciaArquivoDtRelatorioEVoto.getDataPreAnalise()%></td>        			
	                   				<td><%=(pendenciaArquivoDtRelatorioEVoto.getAssistenteResponsavel().length() > 0?pendenciaArquivoDtRelatorioEVoto.getAssistenteResponsavel():pendenciaArquivoDtRelatorioEVoto.getJuizResponsavel())%></td>		                   			
		                   			<td align="center"><a target="_blank" title="Visualizar Pré-Análise" href="PendenciaArquivo?PaginaAtual=6&Id_PendenciaArquivo=<%=pendenciaArquivoDtRelatorioEVoto.getId()%>&hash=<%=pendenciaArquivoDtRelatorioEVoto.getHash()%>"><img src="./imagens/32x32/btn_localizar.png" alt="Visualizar Pré-Análise Voto" title="Visualizar Pré-Análise Voto" /></a></td>
		                   			<td align="center"><a target="_blank" title="Visualizar Pré-Análise" href="PendenciaArquivo?PaginaAtual=6&Id_PendenciaArquivo=<%=pendenciaArquivoDtEmenta.getId()%>&hash=<%=pendenciaArquivoDtEmenta.getHash()%>"><img src="./imagens/32x32/btn_localizar.png" alt="Visualizar Pré-Análise Ementa" title="Visualizar Pré-Análise Ementa" /></a></td>
		                   		</tr>	
               				<% } 
               				}
               			%>
               			</tbody>
					</table>
					<br />
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgAssinar" type="button" value="Assinar Selecionadas" onclick="$('#senhaCertificado').val(''); senhaDigitadaAssinarPendencia(); return false;" />						
						<input name="imgDescartar" type="submit" value="Voltar para pré-análise" onclick="AlterarAction('Formulario','AssinarSessaoSegundoGrau');AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('acaoSalvar','<%=Configuracao.Curinga7%>');" />						
					</div>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div> 
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>   
	</div>
</body>
</html>