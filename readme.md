Especificação do Trabalho Prático (Implementação) 

Problema:
Implementar um pseudo SO multiprogramado, composto por um Gerenciador de Processos, por um Gerenciador de Memória, por um Gerenciador de E/S e por um Gerenciador de Arquivos. O gerenciador de processos deve ser capaz de agrupar os processos em quatro níveis de prioridades. 

O gerenciador de memória deve garantir que um processo não acesse as regiões de memória de um outro processo. E o gerenciador de E/S deve ser responsável por administrar a alocação e a liberação de todos os
recursos disponíveis, garantindo uso exclusivo dos mesmos. E o gerenciador de arquivos deve permitir que os processos possam criar e deletar arquivos, de acordo com o modelo de alocação determinado. 

### Módulos implementados do Sistema:

- Dispatcher Window 
    - `Interface gráfica onde é exibido as informações geradas pelo sistema`
- Manipulador de Arquivos 
    - `Classe criada para manipular os arquivos de texto`
- Módulo SO 
    - `Classe gerente que executa o papel de SO`
- Módulo Disco 
    - `Classe responsável por todo acesso ao disco`

Java JDK 1.8+
