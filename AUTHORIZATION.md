# PROJECT ACCESS CONTROL MODEL
This document establishes the formal authorization specification rigorously implemented by the service layer of the Internal-Hub project, mapping access control over corporate database resources.

## 1. ADOPTED MODEL
The system adopts the RBAC (Role-Based Access Control) model. Under this architecture, operational permissions are strictly tied to logical system functions (roles) rather than individual users, ensuring consistency, auditability, and ease of maintenance throughout the credential lifecycle.

For the current scope, a simplified RBAC model was chosen. Permissions and business rules are fixed and programmatically validated at runtime within the service layer (pure Java/JDBC code), instead of being stored in dynamic ACL/permission tables in the database.

## 2. The Two Axes of Authorization
The security architecture operates on a two-dimensional access control matrix, combining vertical and horizontal restrictions to mitigate the risks of data leaks and unauthorized modifications:
* **Vertical Axis (Role):** Determines the granularity of actions (what the user *can* do). It is defined by the `role_type` field in the `roles` table (mapped to the `com.re4n.internalhub.enums.RoleType` ENUM), establishing the system's functional hierarchy (e.g., write privileges vs. read-only privileges).
* **Horizontal Axis (Context/Department):** Determines the scope boundary of the action (whom the action can affect). It is based on the `department` field in the `users` table. A user in a management role only holds jurisdiction over records that share the exact same department.

*Example:* A user with the `MANAGER` role allocated to the `PRODUCT` department has the vertical authority to view salaries, but their horizontal restriction prevents them from reading or modifying employee records in the `FINANCE` department. They can only see their own team.

## 3. System Roles
Resolving the user's security context requires fetching information from two distinct sources in the relational model: vertical access type comes from the `role_type` field in the `roles` table (obtained via the `role_id` relationship), while the department for horizontal validation comes directly from the logged-in user's record in the `users` table (`department`).
* **`EMPLOYEE`:** Represents the standard collaborator. Has access limited to the scope of their own personal and professional data.
* **`MANAGER`:** Line of business manager. Has operational oversight privileges restricted exclusively to their own department.
* **`HR`:** Human Resources operator. Centralizes record creation and the execution of personnel policies, operating cross-departmentally, except on global technical governance routes.
* **`ADMIN`:** System Administrator. Responsible for the access infrastructure, parameterization of salary ranges, global auditing, and technical custody of system credentials.

## 4. Permission Matrix
| Action / Operation | EMPLOYEE | MANAGER | HR | ADMIN |
| :--- | :--- | :--- | :--- | :--- |
| **READ_OWN_PROFILE** | Allowed | Allowed | Allowed | Allowed |
| **READ_ANY_USER** | Denied | Conditional<br>*Only if belonging to the same department.* | Allowed<br>*Cross-departmental access required for hiring and management.* | Allowed<br>*Global access required for auditing and compliance.* |
| **UPDATE_SALARY** | Denied | Denied | Allowed<br>*Contractual adjustment logic delegated to HR.* | Denied<br>*Segregation of Duties (SoD); IT administration does not modify payroll.* |
| **ASSIGN_ROLE** | Denied | Denied | Conditional<br>*Allowed only to assign EMPLOYEE, MANAGER, and HR.* | Conditional<br>*The only role authorized to grant or revoke ADMIN privileges.* |
| **CREATE_USER** | Denied | Denied | Allowed<br>*Formal onboarding and corporate hiring process.* | Denied<br>*Segregation of Duties (SoD); the ADMIN manages infrastructure, not employee intake.* |
| **DISABLE_USER** | Denied | Denied | Allowed<br>*Except for themselves.* | Denied<br>*Prevines the risk of accidental or intentional record purging by infrastructure operators.* |
| **MANAGE_ROLES** | Denied | Denied | Denied<br>*Parameterization of salary ranges (min/max_salary) requires IT governance and ADMIN.* | Allowed<br>*Data infrastructure maintenance and enforcement of system business rules.* |
| **CHANGE_OWN_ACCESS** | Denied | Denied | Denied | Denied<br>*Universally blocked to prevent the risk of self-promotion and fraud.* |

## 5. Fail-Closed Principle
Internal-Hub adopts the *Fail-Closed* security principle. By default, any and all access to any API resource is explicitly denied (*Deny All by Default*). An operation is only cleared if, and only if, there is an explicit, positive rule in the code authorizing the requesting user's (Role, Department) pair.

Data integrity anomalies—such as a user with a `NULL` `role_id` field (no assigned role), a corrupted role value not mapped in the ENUM, or new routes not explicitly defined in the service layer—trigger the default denial immediately, and the operation is rejected by the authorization service/exception. The system blocks the unknown to ensure active data protection.

## 6. Privilege Escalation Prevention
The `CHANGE_OWN_ACCESS` operation is universally blocked by architectural design. No user in the system, including the `ADMIN`, has the authority to directly alter their own access level, their own role (`role_id`), or their own salary (`salary`).

**This restriction eliminates the critical risk of vertical privilege escalation.** If a management or administrative account is compromised by an attacker, this malicious actor remains strictly confined to the original boundaries of that credential, unable to self-promote or alter their own audit parameters. Additionally, indirect escalation loopholes are mitigated by restricting the assignment of new `ADMIN` profiles exclusively to users who already hold the `ADMIN` role, preventing `HR` from fabricating administrators and ensuring a trustworthy chain of custody.
