Para testar a aplicação:
Utilizando o Eclipse, execute a classe pseudoSO como Java Application.
Em “menu” selecione o processes.txt em “Processos” depois selecione files.txt em “Arquivos” e depois selecione “Iniciar”

Models (Modelos dos objetos do sistema):
 
Arquivo.java – Essa classe mantem o arquivo no sistema, então o arquivo.txt (A partir da Linha 3 até Linha n + 2) é lido e se cria objetos dessa classe.
 
Operacao.java – Essa classe mantem a operação no sistema, então o arquivo.txt (A partir da linha n + 3) é lido e se cria objetos dessa classe.

Processo.java – Essa classe mantem o processo no sistema, então o processo.txt é lido e se cria objetos dessa classe.
 
Modules (Módulos do sistema que controlam o funcionamento do sistema operacional):
 
Disco.java – Essa classe controla a lógica do Disco Rígido do sistema operacional, ele contém três métodos principais que criam, deletam arquivos e executam as operações, que criam e deletam arquivos do disco.

Filas.java – Essa classe extende java.lang.Thread e contém o método run() que vai determinar cada etapa que o objeto Thread vai seguir, esse método é o principal da aplicação pois seguindo cada linha dele é possível entender como o pseudoSO funciona. E isso só é possível graças ao objeto thread que limita o compilador a utilizar apenas uma thread para processar os dados, dessa forma podemos travar os métodos e variáveis através do método synchronized.

Interface.java – Essa classe controla o terminal do sistema operacional, ela tem um método principal actionPerformed (Método sobrescrito da interface ActionListener) que escuta ações que acontece no terminal relativas a barra do menu.

LeitorDeArquivos.java – Essa classe que controla a leitura dos arquivos de texto que são fornecidos ao sistema operacional.

Memoria.java – Essa classe controla a lógica a respeito da memória principal, ele contém dois métodos principais que alocam, desalojam processos na memória principal.

Processos.java – Essa classe controla a lógica a respeito dos processos e a prioridade deles, ele contém métodos que adiciona processos vindos do texto na fila de processos, remove esses processos, aumenta e diminui a prioridade dos processos; método que administra o processo que está bloqueado por um recurso, trazendo-o para primeiro da fila que ele está para ser novamente testado no próximo “clock”.

Recursos.java – Essa classe mantem os recursos de entrada e saída no sistema.

Semaforo.java – Essa classe faz a logica de bloquear os recursos quando esses estão sendo utilizados acontecer, através de uma variável dos recursos chamada semáforo os métodos da classe determinam o destino do processo.

PseudoSO.java – Essa classe controla os arquivos, chamando os métodos do LeitorDeArquivos.java e caso sejam arquivos válidos, adicionam o arquivo ao contexto do sistema operacional, e inicia o sistema operacional, de acordo com as ações que ocorrem no terminal.
 
Util (Constantes e Métodos auxiliares):

Constantes.java – Essa classe guarda todas as constantes utilizadas no sistema.

Util.java – Essa classe contém métodos auxiliares principalmente relacionado a construção de textos.

 
 
 

