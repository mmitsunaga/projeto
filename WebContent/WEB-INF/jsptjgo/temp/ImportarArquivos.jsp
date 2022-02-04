<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=latin1" />
<title>Importa&ccedil;&atilde;o Arquivo</title>
<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />

<script language="JavaScript">
	setTimeout('document.Formulario2.submit()',10000);
</script>
 
</head>

<body>
<div id="divCorpo" class="divCorpo" >
	<div align="right">
    	<form action="ArquivoImportar"  method="post" name="Formulario2" id="Formulario2">
    		<input type="hidden" name="PaginaAtual" value="-1" />
    		<input type="submit" name="Atualizar" value="Atualizar" />
    	 </form>
   	</div>
	<div id="divEditar" class="divEditar">
		<fieldset> <legend> Situa&ccedil;&atilde;o da Importa&ccedil;&atilde;o </legend>
	    	<label>Qtd Arquivos....: <label> <%=request.getAttribute("QtdTotal")%><br />
	       	<label>Qtd Prontos.....: <label> <%=request.getAttribute("QtdPronto")%> <br />
	        <label>Hora Início.....: <label> <%=request.getAttribute("Inicio")%> <br />
	        <label>Hora Atual......: <label> <%=request.getAttribute("Atual")%><br />
	        <label>Previsão........: <label> <%=request.getAttribute("Previsao")%><br />
	        <label>Tempo Leitura...: <label> <%=request.getAttribute("Leitura")%><br />
	        <label>Tempo Escrita...: <label> <%=request.getAttribute("Escrita")%><br />
	        <label>Tempo Assinatua.: <label> <%=request.getAttribute("Assinatura")%><br />
	        <label>Tempo Total.....: <label> <%=request.getAttribute("Total")%><br />	        
	       	<label>Status..........: <label> <%=request.getAttribute("Status")%><br />
	       	<label>Qtd Threads.....: <label> <%=request.getAttribute("QtdThreads")%><br />
			<form action="ArquivoImportar"  method="post" name="Formulario1" id="Formulario1" >	       	
	       	<label>Threads.....: <label> <input type="text" value='1' name="QtdThreads"/> <br />
	    </fieldset>
  	</div>
  	<div class="Centralizado">
  		
    		<input type="hidden" name="PaginaAtual" value="4" />
    		<input type="submit" name="Iniciar" value="Iniciar" />
    	</form>
        <form action="ArquivoImportar"  method="post" name="Formulario3" id="Formulario3" >
    		<input type="hidden" name="PaginaAtual" value="6" />
    		<input type="submit" name="Iniciar" value="Parar" />
    	</form>
    </div>
</div>    	 
</body>
</html>
