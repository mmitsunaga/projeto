<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.DetalhesUsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="usuarioDt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>

<html>
<head>
	<title>Usuários Cadastrados</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
	</style>
    
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
    <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	<script type="text/javascript">
	function submeter(id_Usuario){
		AlterarValue('PaginaAtual' , <%=Configuracao.Editar%>); 
		AlterarValue('Id_Usuario',id_Usuario);
		FormSubmit('Formulario');	
	}
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
  		<div class="area"><h2>&raquo; Usuários Cadastrados </h2></div>
		<form action="DadosUsuario" method="post" name="Formulario" id="Formulario">
		  <input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
		  <input id="Id_Usuario" name="Id_Usuario" type="hidden" value=""/>
		  
		  <input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
		  <div id="divPortaBotoes" class="divPortaBotoes">
		  	  <input id="imgNovo"  class="imgNovo" title="Novo Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
		  	  <input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	
		      <a class="divPortaBotoesLink" href="Ajuda/EstatisticaMovimentacaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" /> </a>
		  </div/><br />
		  <div id="divEditar" class="divEditar">
			<fieldset class="formEdicao"> 
			    <legend class="formEdicaoLegenda">Filtro da Consulta de Usuários</legend>
			    
			    <div class="col50">
			    <label class="formEdicaoLabel" for="Id_Grupo">Grupo
			    <input class="FormEdicaoimgLocalizar" id="imaLocalizarGrupo" name="imaLocalizarGrupo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(GrupoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				<input class="FormEdicaoimgLocalizar" id="imaLimparGrupo" name="imaLimparGrupo" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Grupo','Grupo'); return false;" />
			    </label><br>  
			    
				<input type="hidden" id="Id_Grupo" name="Id_Grupo" value="<%=usuarioDt.getId_Grupo()%>">
			    <input class="formEdicaoInputSomenteLeitura" id="Grupo" name="Grupo"  readonly type="text" size="60" maxlength="60" value="<%=usuarioDt.getGrupo()%>"/>
			    </div>
			    
			    <div class="col50">
			    <label class="formEdicaoLabel" for="Id_Serventia">Serventia
			    <input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				<input class="FormEdicaoimgLocalizar" id="imaLimparServentia" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" />
			    </label><br>  
			    
				<input type="hidden" id="Id_Serventia" name="Id_Serventia" value="<%=usuarioDt.getId_Serventia()%>">
			    <input class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia"  readonly type="text" size="60" maxlength="60" value="<%=usuarioDt.getServentia()%>"/>
			    </div>
			    
			    <div class="col50 clear">
				<label id="formLocalizarLabel" class="formLocalizarLabel">Nome Usuário</label><br> 
				<input id="Nome" class="formLocalizarInput" name="Nome" type="text" size="64" maxlength="60"  value="<%=usuarioDt.getNome()%>"/>
				</div>
				
				<div class="col15 clear space">
				
	       		<input class="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Consultar"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')">
			</div>
			</fieldset>
						
			<fieldset class="formEdicao">
		     	<legend class="formEdicaoLegenda">Usuários Cadastrados</legend> 
				<div id="divTabela" class="divTabela" > 
  						<table id="Tabela" class="Tabela">
	           			<thead>
				        	<tr class="TituloColuna">
				            	<th></th>
				                <th>Nome</th> 
				                <th>Usuário</th>
				                <th width="15%">Grupo</th>               
				                <th>Doc</th>
				                <th>E-Mail</th>
				                <th>Situacao</th>
				                <th>Editar</th>
				            </tr>
				       	</thead>
				        <tbody id="tabListaUsuario">
						<%
							List liTemp = (List) request.getSession().getAttribute("ListaUsuario");
						  	DetalhesUsuarioDt objTemp;
						  	boolean boLinha = false;
						  	for(int i = 0 ; i< liTemp.size();i++) {
						    	objTemp = (DetalhesUsuarioDt)liTemp.get(i); %>
								
								<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
						        	<td><%=i+1%></td>
						           	<td onclick="submeter('<%=objTemp.getId()%>');"><%=objTemp.getNome()%></td>
						            <td onclick="submeter('<%=objTemp.getId()%>');"><%=objTemp.getUsuario()%></td>
						            <td onclick="submeter('<%=objTemp.getId()%>');"><%=objTemp.getGrupo()%></td>
						            <td onclick="submeter('<%=objTemp.getId()%>');"><%=objTemp.getDoc()%></td>               
						            <td onclick="submeter('<%=objTemp.getId()%>');"><%=objTemp.getEmail()%></td>
						           	<%if (objTemp.getSituacao().equalsIgnoreCase("A")) {%>
						            <td>ATIVO</td>
						           	<%} else{%>
						            <td>INATIVO</td>
						           	<%} %>  
						           	<td>
						           		<input name="formLocalizarimgEditar" type="image" src="./imagens/imgEditar.png" onclick="submeter('<%=objTemp.getId()%>');" />     
						           	</td>
						       	</tr>
						<%	}	%>
						</tbody>
					</table>
				</div> 
		  	</fieldset>
			</div>
		</form>
	</div>
<%@ include file="Padroes/Mensagens.jspf" %> 
</body>
</html>