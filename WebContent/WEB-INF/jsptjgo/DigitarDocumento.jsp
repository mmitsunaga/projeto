  		
   	<input type="hidden" id="Id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=request.getAttribute("Id_ArquivoTipo") %>">	
   	<input type="hidden" id="ArquivoTipo" name="ArquivoTipo" value="<%=request.getAttribute("ArquivoTipo") %>">	
   	
   	<div id="Editar">
		<fieldset id="Editar">
			<legend>Digitar Documento </legend>
			
			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<input name="imgSubmeter" type="submit" value="Submeter" onclick="AlterarValue('PassoEditor','2')">
			</div>
		</fieldset>
	</div>
