
   	<input type="hidden" id="Id_ArquivoTipo" name="Id_ArquivoTipo" value="<%=request.getAttribute("Id_ArquivoTipo") %>">	
   	<input type="hidden" id="ArquivoTipo" name="ArquivoTipo" value="<%=request.getAttribute("ArquivoTipo") %>">	

	<div id="Editar">
		<fieldset id="Editar">
			<legend>Assinar Documento </legend>
				
			 <input type="hidden" name="assinatura" id="assinatura" value="" />
  			    
			<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				<object name='AssinadorTJGO'     id='AssinadorTJGO'   class='AssinadorTJGO'    type="application/x-java-applet" mayscript='true' scriptable='true'>       
					<param name="code" value="br.jus.tjgo.Applet.Assinador">				     	           
			       	<param name="archive" value="./applet/AssinadorTJGO-05-11-2019.jar">
			        <param name="mayscript" value="true">
			        <param name="scriptable" value="true">
			        <param name="name" value="AssinadorTJGO">
			        		        
			        <param name="java_arguments" value="-Djnlp.packEnabled=true"/>		            		
			    </object>	 		             
			</div>	
		
		</fieldset>
	</div>