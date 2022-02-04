<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoEspecialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoCriminalDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteTipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AreaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<html>
	<head>
		<title>Dados Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./css/menusimples.css');
			@import url('./css/jquery.tabs.css');
		</style>
		      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
      	<%@ include file="js/buscarArquivos.js"%>
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type='text/javascript' src='./js/tabelaArquivos.js'></script>
		<script type="text/javascript" src="./js/acessibilidadeMenu.js"></script>
		<script type="text/javascript" src="./js/filtro_movimentacoes.js"></script>
		
		
		<script type="text/javascript">
			//variavel para limitar a busca pelos dados da navegação
			//se for diferente de null, mosta a navegação
			//caso contrario faz a busca			
			var DadosNavegacao=null;		
				
			function mostrarTodasPartes() {
				AlterarValue('PaginaAtual', '<%=Configuracao.Editar%>');
				AlterarValue('PassoEditar', '11');								
				document.Formulario.submit();
			}
			
		</script>
		
	</head>

	<body>		    					
  		
  			<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
  				<body class="fundo">
  				<%@ include file="/CabecalhoPublico.html" %>
  			<% } else{%>
  				<body>
  				<%} %> 
  			<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; Dados do Processo (Segredo de Justiça)</h2></div>
  			
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
  				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="" />
  				<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>">
  				
  				<% 	if (UsuarioSessao.isPodePeticionar()){ %>
  				<div>
  					<input name="imgMultipla" type="submit" value="Peticionar" onclick="AlterarAction('Formulario','Peticionamento');AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');AlterarValue('TipoConsulta','<%=request.getAttribute("TipoConsulta")%>');"/>
  				</div>
  				<% 	} %>
  				
	  			<% if (processoDt != null && processoDt.getProcessoStatusCodigo()!= null && processoDt.getProcessoStatusCodigo().length() > 0 && Funcoes.StringToInt(processoDt.getProcessoStatusCodigo())==ProcessoStatusDt.ARQUIVADO){ %>
	  				<img src="imagens/img_processoarquivado.png" name="arquivado" class="imagemArquivado">
	 			<%} else if ( processoDt != null && processoDt.getProcessoTipoCodigo() != null && processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){%>
	  				<img src="imagens/img_processofisico1.png" name="calculo" class="imagemArquivado">
				<%} %>
	 	
				<br>
				<div id="divEditar" class="divEditar">
						<div>
							N&uacute;mero: <span class="bold"> <%=processoDt.getProcessoNumeroCompleto()%></span><br>
							Área: <span class="bold"> <%=processoDt.getArea()%></span>
						</div>
						<!-- PROMOVENTES -->
					 	<fieldset id="VisualizaDados" class="VisualizaDados" <%=((processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0)?"style='background-color:#eee'":"")%>>
							<legend> Polo Ativo | <%=processoDt.getDescricaoPoloAtivo()%> </legend>
					   	<%
					   		List listaPromoventes = processoDt.getListaPolosAtivos();
					   				   	    if (listaPromoventes != null){
					   				   	    	for (int i=0; i<listaPromoventes.size();i++){
					   				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);   			 	 
					   				   				// Se existirem apenas 6 partes vinculadas são exibidas todas, caso seja um número superior serão exibidas apenas as 5 primeiras
					   					       		if ((i >= Configuracao.QtdPartesDadosProcesso) && (listaPromoventes.size() != (Configuracao.QtdPartesDadosProcesso + 1))){
					   	%>
						       			<div> </div>	       									       				       			 
						       			<span class="span1">
						       				<ul><li>
						       						<a href="javascript:mostrarTodasPartes()">
													e outros
													</a>
											</li></ul>	       				
						       			</span>	       			
						       		<%break;
						       		}%>
						       	<div> Nome </div> 
						       	<span class="span1">
						       		<%if( parteDt != null && parteDt.getNome() != null ) {%>
						       			<%=parteDt.getNome()%>
						       		<%} %>
						       		<% if (parteDt != null && parteDt.getAusenciaProcessoParte() != null && !parteDt.getAusenciaProcessoParte().equals("")){ %> <font color="red"><%=parteDt.getAusenciaProcessoParte()%></font> <%} %>
						       	</span>
						       	
								<br />
						       	<div style="width:100%; margin:3px 0"></div>
					       	<%
					       		}
					   		}
					   		%>
						</fieldset>
						
						<!-- PROMOVIDOS -->
						<fieldset id="VisualizaDados" class="VisualizaDados" <%=((processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0)?"style='background-color:#eee'":"")%>>
					   		<legend> Polo Passivo | <%=processoDt.getDescricaoPoloPassivo()%> </legend>
					   	<%
					   		List listaPromovidos = processoDt.getListaPolosPassivos();
					   				   		if (listaPromovidos != null){
					   				   			for (int i=0; i<listaPromovidos.size();i++){
					   				   			  	ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
					   				   				// Se existirem apenas 6 partes vinculadas são exibidas todas, caso seja um número superior serão exibidas apenas as 5 primeiras
					   					       		if ((i >= Configuracao.QtdPartesDadosProcesso) && (listaPromovidos.size() != (Configuracao.QtdPartesDadosProcesso + 1))){
					   	%>
						       			<div> </div>	       									       				       			 
						       			<span class="span1">
						       				<ul><li>
						       						<a href="javascript:mostrarTodasPartes()">
													e outros
													</a>
											</li></ul>	       				
						       			</span>	          			
						       		<%break;
						       		}%>
						    	<div> Nome </div> 
						    	<span class="span1">
						    	<%if( parteDt != null && parteDt.getNome() != null ) {%>
						    		<%=parteDt.getNome()%>
						    	<%} %>
						    	<% if (parteDt != null && parteDt.getAusenciaProcessoParte() != null && !parteDt.getAusenciaProcessoParte().equals("")){ %> <font color="red"><%=parteDt.getAusenciaProcessoParte()%></font> <%} %>
						    	</span>
								<br />
								<div style="width:100%; margin:3px 0"></div>
								
					       	<% 	
					   			}
					   		}
					   		%>
						</fieldset> 
						
				    	<fieldset id="VisualizaDados" class="VisualizaDados">
				    		<legend> Outras Informações</legend>
				    		
					   		<div> Serventia </div>
							<span class="span1"><%= processoDt.getServentia()%></span><br />
							
							<div> Magistrado </div>
							<%if(processoDt.getServentiaCargoResponsavelDt() != null) { %>
								<span style="width:50%; margin:3px 0"><%= processoDt.getServentiaCargoResponsavelDt().getNomeUsuario()%> (<%= processoDt.getServentiaCargoResponsavelDt().getServentia()%>) </span><br />
							<%} else { %>
								<span style="width:50%; margin:3px 0">Informação não disponível</span><br />
							<%} %>
						</fieldset>	
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
				    		<legend> Dados do(s) Advogado(s) do Processo</legend>
				    		<% 	
				    		String controle = UsuarioSessao.isUsuarioInterno()?"BuscaProcesso":"BuscaProcessoUsuarioExterno";
				    		%>
				    	  	<button type="submit" name="operacao" value="Confirmar" onclick="AlterarAction('Formulario','<%=controle%>');AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>');AlterarValue('PassoBusca','3');" >
								Consultar
							</button>		
							<%
							List listaAdvogados = (List)request.getAttribute("listaAdvogados");
							boolean boLinha=false; 
   	   						 if (listaAdvogados != null && listaAdvogados.size() > 0){
   	   						%>	 
   	   							
   	   							<table id="Tabela" class="Tabela">
								<thead>
									<tr>
										<th width="65%">Advogado</th>
										<th width="35%">OAB/Matrícula</th>
									</tr>
								</thead>
								<tbody id="tabListaAdvogados">
   	   							 
   	   						<%	 
						    	for (int i=0;i < listaAdvogados.size();i++){
						    		ProcessoParteAdvogadoDt advogadoDt = (ProcessoParteAdvogadoDt)listaAdvogados.get(i);
					   		%>
	       						<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
									<td> <%=advogadoDt.getNomeAdvogado()%></td>
    								<td> <%=advogadoDt.getOabNumero()%></td>
       		 					</tr>
       						<%} %>
       							</tbody>
				    	  		</table>	
       						<%
       						}
       						%>			    	  	
						</fieldset>	
					
				</div>
			</form>			
		</div>
		<%@include file="Padroes/Mensagens.jspf"%>
	</body>
</html>