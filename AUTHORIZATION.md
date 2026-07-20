# MODELO DE CONTROLE DO PROJETO
Este documento estabelece a especificação formal de autorização que a camada de serviço do projeto Internal-Hub implementa rigorosamente, 
mapeando o controle de acesso sobre os recursos da base de dados corporativa.

## 1. MODELO ADOTADO
O sistema adota o modelo RBAC (Role-Based Access Control / Controle de Acesso Baseado em Papéis). Sob esta arquitetura, as permissões operacionais são estritamente 
vinculadas a funções lógicas do sistema (papéis) e não a usuários individuais, garantindo consistência, auditabilidade e facilidade de manutenção no ciclo de vida das credenciais.

Para o escopo atual, optou-se por um modelo RBAC simplificado. As permissões e regras de negócio são fixadas e validadas programaticamente em tempo de execução na 
camada de serviço (código Java/JDBC puro), em vez de armazenadas em tabelas dinâmicas de acl/permissões no banco de dados.

## 2. Os Dois Eixos de Autorização
A arquitetura de segurança opera sobre uma matriz bidimensional de controle de acesso, combinando restrições verticais e horizontais para mitigar riscos de vazamento e alteração indevida de dados:
* **Eixo Vertical (Papel):** Determina a granularidade das ações (o *que* o usuário pode fazer). É definido pelo campo `role_type` da tabela `roles` (mapeado no ENUM `com.re4n.internalhub.enums.RoleType`), estabelecendo a hierarquia funcional do sistema (ex.: Privilégios de escrita vs. Privilégios de leitura).
* **Eixo Horizontal (Contexto/Departamento):** Determina o limite de abrangência da ação (sobre quem a ação pode agir). Baseia-se no campo `department` da tabela `users`. Um usuário em um papel de gestão só possui jurisdição sobre os registros que compartilham o mesmo departamento exato.

*Exemplo:* Um usuário com o papel de `MANAGER` alocado no departamento de `PRODUCT` possui autoridade vertical para visualizar salários, mas sua restrição horizontal o impede de ler ou modificar os registros de funcionários do departamento de `FINANCE`. Ela enxerga apenas sua própria equipe.

## 3. Papéis do Sistema (Roles)
A resolução do contexto de segurança do usuário exige a busca de informações em duas origens distintas do modelo relacional:o tipo de acesso vertical provém do campo `role_type` na tabela `roles` (obtivo através do relacionamento `role_id`), enquanto o departamento para validação horizontal provém diretamente do registro do usuário logado na tabela `users` (`department`).
*   **`EMPLOYEE`:** Representa o colaborador padrão. Possui acesso limitado ao escopo de seus próprios dados pessoais e profissionais.
*   **`MANAGER`:** Gestor de linha de negócios. Possui privilégios de supervisão operacional restritos exclusivamente ao seu próprio departamento.
*    **`HR`:** Operador de Recursos Humanos. Centraliza a criação de registros e a execução de políticas de pessoal, operando transversalmente entre departamentos, exceto em rotas de governança técnica global.
*    **`ADMIN`:** Administrador do Sistema. Responsável pela infraestrutura de acesso, parametrização de faixas salariais, auditoria global e custódia técnica das credenciais do sistema.

## 4. Matriz de Permissões
| Ação / Operação | EMPLOYEE | MANAGER | HR | ADMIN |
| :--- | :--- | :--- | :--- | :--- |
| **READ_OWN_PROFILE** | Permitido | Permitido | Permitido | Permitido |
| **READ_ANY_USER** | Negado | Condicional<br>*Apenas se pertencer ao mesmo departamento.* | Permitido<br>*Acesso transversal necessário para contratação e gestão.* | Permitido<br>*Acesso global exigido para auditoria e conformidade.* |
| **UPDATE_SALARY** | Negado | Negado | Permitido<br>*Lógica de reajustes contratuais delegada ao RH.* | Negado<br>*Segregação de Funções (SoD); a administração de TI não altera folha de pagamento.* |
| **ASSIGN_ROLE** | Negado | Negado | Condicional<br>*Permitido apenas para atribuir EMPLOYEE, MANAGER e HR.* | Condicional<br>*Único papel autorizado a conceder ou revogar o privilégio de ADMIN.* |
| **CREATE_USER** | Negado | Negado | Permitido<br>*Processo formal de onboarding e contratação corporativa.* | Negado<br>*Segregação de Funções (SoD); o ADMIN gerencia a infraestrutura, não a admissão de funcionários.* |
| **DISABLE_USER** | Negado | Negado | Permitido<br>*Exceto a si mesmo.* | Negado<br>*Previne o risco de expurgo acidental ou intencional de registros por operadores de infraestrutura.* |
| **MANAGE_ROLES** | Negado | Negado | Negado<br>*A parametrização de faixas salariais (min/max_salary) exige governança de TI e ADMIN.* | Permitido<br>*Manutenção da infraestrutura de dados e aplicação das regras de negócio do sistema.* |
| **CHANGE_OWN_ACCESS** | Negado | Negado | Negado | Negado<br>*Bloqueado universalmente para prevenir o risco de auto-promoção e fraude.* |

## 5. Princípio Fail-Closed
O Internal-Hub adota o princípio de segurança *Fail-Closed* (Falha Segura com Fechamento Completo). Por padrão, todo e qualquer acesso a qualquer recurso da API é explicitamente negado (*Deny All by Default*). Uma operação só é liberada se, e somente se, houver uma regra explícita e positiva no código autorizando o par (Papel, Departamento) do usuário requisitante.
Cenários de anomalias de integridade na base de dados — tais como um usuário com o campo `role_id` avaliado como `NULL` (sem cargo associado), um valor de papel corrompido ou não mapeado no ENUM, ou rotas novas não explicitadas na camada de serviço — acionam a negação padrão imediatamente, a operação é recusada pelo `service/exceção` de autorização. O sistema bloqueia o desconhecido para garantir a proteção dos dados ativos.

## 6. Prevenção ao Escalonamento de Privilégios
A operação `CHANGE_OWN_ACCESS` é universalmente bloqueada por design arquitetural. Nenhum usuário no sistema, incluindo o `ADMIN`, possui autoridade para alterar seu próprio nível de acesso, seu próprio papel (`role_id`) ou seu próprio salário (`salary`) de forma direta.

**Esta restrição elimina o risco crítico de escalonamento vertical de privilégios.** Se uma conta administrativa ou de gestão for comprometida por um atacante, este agente malicioso estará estritamente confinado aos limites originais daquela credencial, sendo incapaz de se auto-promover ou alterar seus próprios parâmetros de auditoria. Adicionalmente, o buraco de escalonamento indireto é mitigado restringindo a atribuição de novos perfis de `ADMIN` exclusivamente a usuários que já possuam o papel de `ADMIN`, impedindo que o `HR` fabrique administradores e garantindo uma trilha de custódia idônea.
