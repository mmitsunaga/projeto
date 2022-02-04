<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.ne.UsuarioNe"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%><html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<link href="./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112" type="text/css" rel="stylesheet" media="screen" />
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
	
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type='text/javascript' src='./js/checks.js'></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$("#dataInicialInicio").mask("99/99/9999");
			$("#dataFinalInicio").mask("99/99/9999");
			
			$("#nomeBusca").focus();
		});
		
		//Preenche o campo filtro e clica no botao
		function filtrar(numero){
			$("#nomeBusca").val(numero);
			
			$("#formLocalizarBotao").trigger("click");
		}
		
		/**
		 * Cria ou retira a combo a partir do checkbox
		 * @author Ronneesley Moura Teles
		 * @since 09/05/2008 16:49
		 * @return void
		 */
		function distribuir(checkbox){
			if (checkbox.checked == true){
				$("#pendencia" + checkbox.value).html("Carregando aguarde...");		
				clonar(checkbox.value);
			} else
				$("#pendencia" + checkbox.value).html("");
		}
		
		/**
		 * Cria uma combo virtual do elemento
		 * @author Ronneesley Moura Teles
		 * @since 09/05/2008 16:47
		 * @return void
		 */
		function clonar(pendencia){
			var usuarios = $("#usuarios");
			var id = "pendencia" + pendencia;
			
			$("#" + id).html(usuarios.html());
			
			$("#" + id + " select").eq(0).attr("name", "pendencia[" + pendencia + "]" );
		}
	</script>
</head>
<body>
  <div class="divCorpo">
	<form action="Pendencia" method="post" name="Formulario" id="Formulario">
		<div class="area"><h2>&raquo; Distribuir / Solucionar Pend&ecirc;ncias</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
	  
		<input id="tempBuscaId" name="tempBuscaId" type="hidden" value="" />
		<input id="tempBuscaDescricao" name="tempBuscaDescricao" type="hidden" value="" />

		<%@ include file="./js/Paginacao.js"%>
		<%@ include file="js/PendenciaDistribuicao.js"%>
		
		<div id="divLocalizar" class="divLocalizar" >
			<fieldset id="formLocalizar" class="formLocalizar"> 
		    	<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Filtro de Pend&ecirc;ncias</legend>
		    	
		       	<label id="formLocalizarLabel" class="formLocalizarLabel">Processo
		       		<input type="image"  src="./imagens/16x16/edit-clear.png" onclick="AlterarValue('numeroProcesso', ''); return false;" title="Limpar o n&uacute;mero do processo" />
		       	</label><br>
		       	
		       	<input id="numeroProcesso" class="formLocalizarInput" name="numeroProcesso" type="text" value="<%=request.getParameter("numeroProcesso") != null?request.getParameter("numeroProcesso"):""%>" maxlength="60" /> 
		       <br>
		       
		       	<input type="checkbox" name="prioridade" id="prioridade" 
		       		<%=(Funcoes.StringToBoolean(request.getAttribute("prioridade").toString())?"checked":"")%> 
		       		value="1" />Organizar por prioridade
		       	<br />
		       	
				<input type="hidden" id="Id_PendenciaTipo" name="Id_PendenciaTipo" value="<%=request.getAttribute("Id_PendenciaTipo")%>" />
				
				<label class="lbl">Tipo de Pend&ecirc;ncia
				<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
					onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');
							AlterarValue('tempBuscaId', 'Id_PendenciaTipo'); 
							AlterarValue('tempBuscaDescricao', 'PendenciaTipo');" 
								title="Selecione a pend&ecirc;ncia" />
				<input class="FormEdicaoimgLocalizar" name="imaLimparPendenciaTipo" type="image"  src="./imagens/16x16/edit-clear.png"
					onclick="LimparChaveEstrangeira('Id_PendenciaTipo', 'PendenciaTipo'); return false;" 
								title="Limpar a Tipo da pend&ecirc;ncia" />
				</label><br>
				
				<input class="somenteLeitura" name="PendenciaTipo" id="PendenciaTipo" readonly type="text" 
					size="50" maxlength="50" value="<%=request.getAttribute("PendenciaTipo")%>" />	
				
				<br />
				
				<input type="hidden" id="Id_PendenciaStatus" name="Id_PendenciaStatus" value="<%=request.getAttribute("Id_PendenciaStatus")%>" />
				
				<label class="lbl">Status da Pend&ecirc;ncia
				<input class="FormEdicaoimgLocalizar" name="imaLocalizarPendenciaStatus" type="image"  src="./imagens/imgLocalizarPequena.png" 
					onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(PendenciaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');
							AlterarValue('tempBuscaId', 'Id_PendenciaStatus'); 
							AlterarValue('tempBuscaDescricao', 'PendenciaStatus');" 
								title="Selecione a pend&ecirc;ncia" />
				<input class="FormEdicaoimgLocalizar" name="imaLimparPendenciaStatus" type="image"  src="./imagens/16x16/edit-clear.png"
					onclick="LimparChaveEstrangeira('Id_PendenciaStatus', 'PendenciaStatus'); return false;" 
								title="Limpar o status da pend&ecirc;ncia" />
				</label><br>
				
				<input class="somenteLeitura" name="PendenciaStatus" id="PendenciaStatus" readonly type="text" 
					size="50" maxlength="50" value="<%=request.getAttribute("PendenciaStatus")%>" />	
				
				<br />
				
				<label class="formLocalizarLabel">Data de In&iacute;cio</label><br> 
				<input class="formLocalizarInput" name="dataInicialInicio" id="dataInicialInicio" type="text" value="" maxlength="60" title="Data inicial do inicio" />
				<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
						alt="Calendário" onclick="displayCalendar(document.forms[0].dataInicialInicio,'dd/mm/yyyy',this)" />
				     
				<label> a </label> 
				<input class="formLocalizarInput" name="dataFinalInicio" id="dataFinalInicio" type="text" value="" maxlength="60" title="Data final do inicio" />
				<img src="./imagens/dlcalendar_2.gif" class="calendario" title="Calendário" 
					alt="Calendário" onclick="displayCalendar(document.forms[0].dataFinalInicio,'dd/mm/yyyy',this)" />
				<br />
				<%String filtroTipo = (String) request.getAttribute("filtroTipo");%>
		       	<input id="filtroTipo1" type="radio" name="filtroTipo" value="1" <%if (filtroTipo.equals("1")){%> checked<%}%>  /> <label for="filtroTipo1">Somente de processo</label><br> 
		       	<input id="filtroTipo2" type="radio" name="filtroTipo" value="2" <%if (filtroTipo.equals("2")){%> checked<%}%> /> <label for="filtroTipo2">Somente normais</label><br>
		       	<input id="filtroTipo3" type="radio" name="filtroTipo" value="3" <%if (filtroTipo.equals("3")){%> checked<%}%> /> <label for="filtroTipo3">Todas</label><br>
		       			   
		        <%UsuarioNe UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao"); 
		          if (UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()!= null && 
		        		  Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL) { %>    		
					<br /><br />
					<%String filtroCivelCriminal = (String) request.getAttribute("filtroCivelCriminal");%>
			       	<input id="filtroCivelCriminal1" type="radio" name="filtroCivelCriminal" value="1" <%if (filtroCivelCriminal.equals("1")){%> checked<%}%>  /><label for="filtroCivelCriminal1">Somente Civeis</label><br>
			       	<input id="filtroCivelCriminal2" type="radio" name="filtroCivelCriminal" value="2" <%if (filtroCivelCriminal.equals("2")){%> checked<%}%> /><label for="filtroCivelCriminal2">Somente Criminais</label><br>
			       	<input id="filtroCivelCriminal3" type="radio" name="filtroCivelCriminal" value="3" <%if (filtroCivelCriminal.equals("3")){%> checked<%}%> /><label for="filtroCivelCriminal3">Todas</label><br>
		        <%} else { %>
					<%String filtroCivelCriminal = "";%>
						<input type="hidden" id="filtroCivelCriminal1" type="radio" name="filtroCivelCriminal" value="0"/>
						<input type="hidden" id="filtroCivelCriminal1" type="radio" name="filtroCivelCriminal" value="0"/>
						<input type="hidden" id="filtroCivelCriminal1" type="radio" name="filtroCivelCriminal" value="0"/>
		       	<%} %>     		
				<br />
				
				
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">		       	
					<button id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"
						onclick="buscaDados(0,<%=Configuracao.TamanhoRetornoConsulta %>); return false;"
						title="Consultar as pend&ecirc;ncia">
						<!-- <img src="imagens/22x22/btn_pesquisar.png" alt="Consultar publica&ccedil;&otilde;es" /> -->
						Consultar
					</button>
					
					<button type="submit" name="operacao" value="Distribuir"
						title="Distribuir as pend&ecirc;ncias selecionadas">
						<!-- <img src="imagens/22x22/btn_encaminhar.png" alt="Distribuir" /> -->
						Distribuir
					</button>
				</div>
		   	</fieldset>
		   	
		   	<div id="usuarios" style="display: none;">
			   	<select style="width: 100%;">
			   		<option value="0">Selecione um usu&aacute;rio</option>
			   		<%
			   		if (request.getAttribute("usuarios") != null){
				   		List lista = (List) request.getAttribute("usuarios");
				   		Iterator it = lista.iterator();
				   		while (it.hasNext()){
				   			UsuarioServentiaDt usuario = (UsuarioServentiaDt)it.next();
				   		%>
				   		<option value="<%=usuario.getId()%>"><%=usuario.getUsuarioServentia()%></option>
			   		<%	}
			   		}%>
			   	</select>
		   	</div>
		
			<div id="divTabela" class="divTabela" > 
			   	<table id="Tabela" class="Tabela">
			       	<thead>
			           	<tr>
			           		<th class="colunaMinima"></th>
			           		<th class="colunaMinima">N&uacute;m</th>
			                <th>Processo</th>
			                <th width="200">Tipo</th>
			                <th width="125" align="center">In&iacute;cio</th>
			                <th width="200">Status</th>
			           		<th class="colunaMinima">Distribuir</th>
			                <th>Realizador</th>
			                <th class="colunaMinima">Op&ccedil;&otilde;es</th>
						</tr>
					</thead>
					
					<%if (request.getAttribute("ListaPendencia") != null){
						List lista = (List)request.getAttribute("ListaPendencia");
						Iterator it = lista.iterator();
						int qtd = 0;
						%>
						<tbody id="tabListaDados">
							<%
							int contador = 1;
							while (it.hasNext()){
								PendenciaDt obj = (PendenciaDt)it.next();
							%>
								<tr class="TabelaLinha<%=(qtd++%2 + 1)%>">
									<td align ="center" ><%=contador%></td>
									<td class="colunaMinima lista_id"><%=obj.getId()%></td>
									<td width="160" align="center">
										<%if (!obj.getId_Processo().equals("")){%>
											<a href="BuscaProcesso?Id_Processo=<%=obj.getId_Processo()%>">
												<%=obj.getProcessoNumero()%>
											</a>
										<%}%>
									</td>
									<td><%=obj.getPendenciaTipo()%></td>
									<td class="lista_data"><%=obj.getDataInicio()%></td>
									<td><%=obj.getPendenciaStatus()%></td>
									<td align="center">
										<%if (!obj.getSomenteMarcarLeitura()){%>
											<input type="checkbox" name="pendencia[]" value="<%=obj.getId()%>" onclick="distribuir(this)"
												title="Clique para escolher a pessoa que receber&aacute; a pend&ecirc;ncia" />
										<%}%>
									</td>
									<td id="pendencia<%=obj.getId()%>" width="350"></td>
									<td class="colunaMinima">							
										<%
											String img = "22x22/ico_solucionar.png";
											String titulo = "Solucionar a pend&ecirc;ncia";
										%>									
												
										<a href="Pendencia?PaginaAtual=<%=Configuracao.Editar%>&amp;pendencia=<%=obj.getId()%>&amp;fluxo=1&amp;NovaPesquisa=true&amp;CodigoPendencia=<%=obj.getHash()%>">
											<img src="imagens/<%=img%>" alt="Solucionar" title="<%=titulo%>" />
										</a>
									</td>
								</tr>
								<%contador +=1; %>
							<%}%>
						</tbody>
					<%} else { %>
						<tbody>
							<tr>
								<td colspan="8">N&atilde;o h&aacute; pend&ecirc;ncias</td>
							</tr>
						</tbody>
					<%}%>
				</table>
				
				<%@ include file="./Padroes/Paginacao.jspf"%>
			</div>
		</div>
  </form>
  <%@ include file="Padroes/Mensagens.jspf" %>
 </div>
</body>
</html>