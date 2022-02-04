<fieldset class="formEdicao"> 
		<legend class="formEdicaoLegenda"> Endereço </legend>
		    				
		<label class="formEdicaoLabel" for="Logradouro">*Logradouro</label><br> 
		<input class="formEdicaoInput" name="Logradouro" id="Logradouro"  type="text" size="106" maxlength="100" value="" onkeyup=" autoTab(this,60)">
		<br />
    				
    	<label class="formEdicaoLabel" for="Numero">Número</label><br> 
    	<input class="formEdicaoInput" name="Numero" id="Numero"  type="text" size="11" maxlength="11" value="" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
				
		<label class="formEdicaoLabel" for="Complemento">Complemento</label><br> 
		<input class="formEdicaoInput" name="Complemento" id="Complemento"  type="text" size="59" maxlength="100" value="" onkeyup=" autoTab(this,255)">
		<br />
				
		<label class="formEdicaoLabel" for="Bairro">*Bairro
		<input class="FormEdicaoimgLocalizar" id="imaLocalizarBairro" name="imaLocalizarBairro" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','1742')" >
		</label><br>  
		  
		<input class="formEdicaoInputSomenteLeitura"  readonly name="Bairro" id="Bairro" type="text" size="80" maxlength="100" value="">
		<br />

		<label class="formEdicaoLabel" for="Cidade">Cidade</label><br>  
		<input class="formEdicaoInputSomenteLeitura" readonly name="Cidade" id="Cidade" type="text" size="40" maxlength="60" value="">
					
   		<label class="formEdicaoLabel" for="Uf">UF</label><br> 
		<input class="formEdicaoInputSomenteLeitura" readonly name="Estado" id="Uf"  type="text" size="10" maxlength="10" value="">
		<br />
		
		<label class="formEdicaoLabel" for="Cep">*CEP</label><br> 
		<input class="formEdicaoInput" name="Cep" id="Cep"  type="text" size="11" maxlength="8" value="" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
		<br />
</fieldset>

<div id="divBotoesCentralizados" class="divBotoesCentralizados">
	<input name="imgInserir" type="submit" value="Inserir" onclick="AlterarValue('PaginaAtual','-1');AlterarValue('PassoEditar','3');"> 
	<input name="imgInserir" type="submit" value="Cancelar" onclick="AlterarValue('PaginaAtual','-1');AlterarValue('PassoEditar','-1');">
</div>