function getMensagem(id_mensagem){
	var vetor = new Array();
	//Peticionamento
	vetor[0] = "-Para inserir arquivos � necess�rio primeiramente informar o tipo do arquivo. Ap�s isso informe o local onde o arquivo se encontra em seu computador, ou se desejar utilize o Editor de Texto do Projudi para redigir seu documento.<br> -Ap�s selecionar/redigir o documento clique em <b>Assinar</b> para assinar digitalmente o mesmo, ou se tratar de um arquivo j� certificado clique em Inserir.<br> -O Usu�rio tamb�m pode utilizar modelos previamente cadastrados ao inserir arquivos.";
	
	vetor[1] = "abajur";
	vetor[2] = "chap�u";
	vetor[3] = "lago";
	vetor[4] = "�rvore";
	//Importa��o Dados Criminal
	vetor[5] = "-Selecione o arquivo com extens�o (.tco) clicando em <strong>Arquivo</strong>. Esse arquivo se refere aquele que foi gerado pelo sistema da Secretaria de Seguran�a P�blica atrav�s da op��o &quot;<strong>Gerar Arquivo</strong>&quot; e que cont�m os dados referentes ao TCO que ser� enviado ao Judici�rio. </p><p>Ap&oacute;s selecionar o arquivo clique em <b>INSERIR.</b> N�o existindo qualquer restri��o, o sistema exibir� a tela do pr�ximo passo.</p>";
	return vetor[id_mensagem];
}