function getMensagem(id_mensagem){
	var vetor = new Array();
	//Peticionamento
	vetor[0] = "-Para inserir arquivos é necessário primeiramente informar o tipo do arquivo. Após isso informe o local onde o arquivo se encontra em seu computador, ou se desejar utilize o Editor de Texto do Projudi para redigir seu documento.<br> -Após selecionar/redigir o documento clique em <b>Assinar</b> para assinar digitalmente o mesmo, ou se tratar de um arquivo já certificado clique em Inserir.<br> -O Usuário também pode utilizar modelos previamente cadastrados ao inserir arquivos.";
	
	vetor[1] = "abajur";
	vetor[2] = "chapéu";
	vetor[3] = "lago";
	vetor[4] = "árvore";
	//Importação Dados Criminal
	vetor[5] = "-Selecione o arquivo com extensão (.tco) clicando em <strong>Arquivo</strong>. Esse arquivo se refere aquele que foi gerado pelo sistema da Secretaria de Segurança Pública através da opção &quot;<strong>Gerar Arquivo</strong>&quot; e que contém os dados referentes ao TCO que será enviado ao Judiciário. </p><p>Ap&oacute;s selecionar o arquivo clique em <b>INSERIR.</b> Não existindo qualquer restrição, o sistema exibirá a tela do próximo passo.</p>";
	return vetor[id_mensagem];
}