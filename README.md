# ACZG-Hero JAVA CLI

Este é o projeto final da trilha de JAVA (K1-T3) do ACZG10. A ideia central é construir um sistema de gerenciamento de tarefas via linha de comando (CLI) construído em Java puro (Vanilla), sem o uso de frameworks externos.

## 🚀 Funcionalidades Principais (Core)

O sistema de gestão de tarefas e quadros permite organizar seu fluxo de trabalho diretamente pelo terminal:

*   **Gerenciamento de Quadros:** Crie e alterne entre diferentes ambientes de trabalho (quadros) para isolar seus projetos.
*   **CRUD Completo de Tarefas:** Comandos fáceis para criar, listar, ler, editar e remover tarefas.
*   **Assistente de Criação/Edição Interativo:** Sistema guiado que auxilia no preenchimento dos dados, oferecendo valores padrão (como assumir o dia de hoje ao pressionar `ENTER`).
*   **Listagem e Filtros Avançados:**
    *   **Padrão:** Ordenação inteligente com foco nas prioridades mais altas.
    *   **Por Categoria (`-c`):** Agrupamento e ordenação por categorias.
    *   **Por Status (`-s`):** Agrupamento por status (TODO, DOING, DONE).
    *   **Por Data (`-d`):** Filtro interativo para exibir apenas tarefas a partir de uma data específica ou do dia atual.
*   **Validação Segura de Dados:** Tratamento robusto para entradas de datas (DD/MM/AAAA) e opções numéricas.

## 🛠️ Funcionalidades da Infraestrutura

- **Motor de Roteamento:** Comandos separados por contexto (Global vs. Local).
- **Prompt Dinâmico:** Estilo shell/Git indicando o contexto atual (ex: `[meu-quadro] aczg@grebechi >>`).
- **Histórico Estilo Unix:** Navegação em pilha e reexecução rápida via atalhos (`!!` para o último, `!1` para o mais recente).
- **Sistema de Tutoriais:** Manuais detalhados embutidos em cada comando, acessíveis através da flag `-t`.
- **Cores ANSI Nativo:** Interface rica, interativa e colorida no próprio terminal para facilitar a leitura.

## 💻 Exemplos de Uso

Uma vez dentro do terminal da aplicação, você pode utilizar atalhos como `t` para gerenciar tarefas:

    # Visualizar o manual interativo do comando de tarefas
    t -t
    
    # Iniciar o assistente de criação de nova tarefa
    t criar
    
    # Listar todas as tarefas a partir do dia de hoje (ou de uma data específica)
    t listar -d
    
    # Visualizar todos os detalhes e destaques da tarefa de ID 1
    t 1

## Como Executar

Certifique-se de ter o Java instalado e execute na raiz do projeto:

```bash
./gradlew run -q