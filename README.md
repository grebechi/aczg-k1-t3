# ACZG-Hero JAVA CLI

Este é o projeto final da trilha de JAVA (K1-T3) do ACZG10. A minha ideia é construir um sistema de linha de comando (CLI) construído em Java puro (Vanilla), sem uso de frameworks externos.

## Funcionalidades da Infraestrutura até o momento

- **Motor de Roteamento:** Comandos separados por contexto (Global vs. Local).
- **Prompt Dinâmico:** Estilo shell/Git (ex: `[quadro-atual] aczg@grebechi >>`).
- **Histórico Estilo Unix:** Navegação em pilha e reexecução rápida via atalhos (`!!` para o último, `!1` para o mais recente).
- **Sistema de Tutoriais de Comandos:** Manuais embutidos em cada comando através da flag `-t`.
- **Cores ANSI Nativo:** Interface rica e interativa no próprio terminal.

## Como Executar

Certifique-se de ter o Java instalado e execute na raiz do projeto:

```bash
./gradlew run -q