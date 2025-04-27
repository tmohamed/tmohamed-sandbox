**Chapter 7: Foundational Architecture Patterns**

---

### Overview
Chapter 7 introduces **foundational architecture patterns**, the simplest and most common architectural approaches used in software systems. These patterns—**Big Ball of Mud**, **Monolithic**, and **Modular Monolithic**—serve as starting points for understanding more complex patterns (covered in Chapters 8–15). The chapter explores each pattern’s structure, characteristics, trade-offs, and use cases, providing architects with a framework to evaluate their suitability for specific contexts. It emphasizes the importance of aligning patterns with **architecture characteristics** (e.g., scalability, maintainability) and business needs, setting the stage for the book’s deeper dive into advanced architectural styles.

---

### 1. Importance of Foundational Patterns (pp. 113–116)

- **Why Foundational Patterns Matter**:
    - Foundational patterns are the **simplest** and most **widely used** architectural approaches, forming the basis for many systems, especially in early development stages or legacy environments.
    - They align with **Gall’s Law** (Chapter 1), which advocates starting with simple systems that work and evolving them as complexity grows.
    - Understanding these patterns helps architects:
        - Choose appropriate structures for new systems based on requirements and constraints.
        - Refactor or modernize legacy systems by recognizing their patterns and limitations.
        - Appreciate trade-offs when transitioning to advanced patterns (e.g., microservices).
    - **Example**: A startup building an e-commerce platform might start with a **Monolithic** pattern for simplicity, later evolving to microservices for scalability, guided by an understanding of foundational patterns.

- **Role in the Book**:
    - Chapter 7 marks the beginning of the book’s exploration of architectural styles, following the conceptual groundwork in Chapters 1–6 (e.g., modularity, characteristics, fitness functions).
    - It provides a baseline for comparing more complex patterns (e.g., microservices, event-driven) in subsequent chapters.
    - **Connection to Previous Chapters**:
        - **Chapter 1 (Introduction)**: Introduced architecture as components, relationships, and principles, which foundational patterns embody.
        - **Chapter 2 (Architectural Thinking)**: Emphasized trade-off analysis, critical for evaluating patterns.
        - **Chapter 3 (Modularity)**: Highlighted cohesive, loosely coupled designs, relevant to **Modular Monolithic**.
        - **Chapter 4 (Architecture Characteristics)**: Defined characteristics (e.g., scalability, maintainability) that patterns support or hinder.
        - **Chapter 5 (Identifying Architecture Characteristics)**: Provided techniques to identify characteristics, which guide pattern selection.
        - **Chapter 6 (Measuring and Governing Architecture)**: Introduced fitness functions to validate pattern alignment with characteristics.

- **Key Considerations**:
    - Each pattern has **strengths** and **weaknesses**, impacting architecture characteristics like **simplicity**, **maintainability**, **scalability**, and **deployability**.
    - Architects must evaluate patterns based on **business context** (e.g., startup vs. enterprise), **team size**, and **technical constraints**.
    - **Example**: A small team with limited resources might choose a **Monolithic** pattern for rapid development, while a large enterprise might avoid it due to scalability needs.

---

### 2. Big Ball of Mud (pp. 116–120)

- **Definition**:
    - The **Big Ball of Mud** is an **anti-pattern**, characterized by a system with no clear structure, high coupling, low cohesion, and haphazard design. It emerges from ad-hoc development, lack of planning, or uncontrolled growth.
    - **Structure**: A chaotic mix of components with tangled dependencies, often resulting in a single, unorganized codebase.
    - **Metaphor**: Like a tangled ball of yarn, where pulling one thread affects the entire structure.
    - **Example**: A legacy enterprise application with intertwined business logic, database access, and UI code, making changes risky and time-consuming.

- **Characteristics**:
    - **High Coupling**: Components are tightly interdependent, so changes propagate widely.
        - **Example**: Modifying a payment function affects unrelated inventory code due to shared global variables.
    - **Low Cohesion**: Modules have mixed responsibilities, violating modularity (Chapter 3).
        - **Example**: A single class handles user authentication, order processing, and logging.
    - **Poor Maintainability**: Changes are error-prone due to complexity and lack of clear boundaries.
    - **Poor Testability**: Tangled code is hard to test in isolation, reducing quality.
    - **Low Scalability**: The system struggles to handle increased load due to monolithic, unstructured design.
    - **Example**: A Big Ball of Mud e-commerce system crashes during a sale because its database access code is embedded in UI logic, preventing scaling.

- **When It Occurs**:
    - **Ad-Hoc Development**: Rapid prototyping or “hacking” without architectural planning.
    - **Technical Debt**: Accumulating quick fixes without refactoring.
    - **Team Misalignment**: Lack of coordination, violating **Conway’s Law** (Chapter 1).
    - **Example**: A startup rushing to launch an MVP without modular design ends up with a Big Ball of Mud, complicating future updates.

- **Trade-offs**:
    - **Advantages**:
        - **Speed**: Allows rapid development in early stages or prototypes.
        - **Simplicity (Initially)**: No need for upfront design, appealing for small teams.
        - **Example**: A proof-of-concept app built in a hackathon is functional but unstructured.
    - **Disadvantages**:
        - **Fragility**: Changes introduce bugs due to tangled dependencies.
        - **Costly Maintenance**: Fixing issues or adding features is slow and expensive.
        - **Scalability Limits**: The system cannot handle growth without significant rework.
        - **Example**: A Big Ball of Mud system requires weeks to add a new payment method, delaying market response.

- **Use Cases**:
    - Rarely intentional; suitable only for **temporary prototypes** or **throwaway systems** where speed trumps quality.
    - **Example**: A demo app for a pitch, discarded after use.
    - **Caution**: Prolonged use leads to technical debt, requiring refactoring or replacement.

- **Modern Relevance (2025)**:
    - Big Ball of Mud systems are common in **legacy systems** or poorly managed startups, especially in industries slow to adopt cloud-native practices.
    - **Example**: A 1990s-era banking system with spaghetti code struggles to integrate with modern APIs, necessitating modernization.
    - **Refactoring**: Tools like **SonarQube** or **ArchUnit** help identify and address coupling issues, aligning with **modularity** (Chapter 3).

- **Governance**:
    - Use **fitness functions** (Chapter 6) to detect and prevent Big Ball of Mud tendencies (e.g., cyclic dependencies, high complexity).
    - **Example**: A fitness function fails a build if code complexity exceeds a threshold, encouraging refactoring.

---

### 3. Monolithic Architecture (pp. 120–126)

- **Definition**:
    - The **Monolithic** architecture is a single, unified application where all components (e.g., UI, business logic, data access) are tightly integrated into one codebase and deployed as a single unit.
    - **Structure**: A single executable or deployable artifact (e.g., a WAR file in Java) containing all functionality.
    - **Metaphor**: Like a single, large building housing all functions, as opposed to separate structures.
    - **Example**: A traditional e-commerce application with a single codebase handling UI (HTML/CSS), business logic (Java), and database access (SQL), deployed on a single server.

- **Characteristics**:
    - **Simplicity**: Easy to develop, test, and deploy in early stages due to a single codebase.
        - **Example**: A monolithic app can be built and deployed with one pipeline.
    - **Tight Coupling**: Components are interdependent, often sharing a single database or memory space.
        - **Example**: UI logic directly queries the database, coupling it to data access code.
    - **High Cohesion (Initially)**: All functionality is centralized, but cohesion degrades as the system grows.
    - **Centralized Deployment**: A single deployment updates the entire system, simplifying operations but risking downtime.
    - **Example**: A monolithic retail system deploys a new feature by redeploying the entire application, potentially disrupting users.

- **Architecture Characteristics Supported**:
    - **Simplicity**: Easy to understand and manage for small teams or simple applications.
    - **Cost Efficiency**: Lower operational overhead than distributed systems (e.g., no need for complex orchestration).
    - **Maintainability (Early)**: A single codebase is manageable until complexity grows.
    - **Example**: A startup’s monolithic CRM system is cost-effective and simple to maintain with a small team.

- **Architecture Characteristics Hindered**:
    - **Scalability**: Scaling requires replicating the entire application, wasting resources on unneeded components.
        - **Example**: Scaling the UI requires scaling the database logic, increasing costs.
    - **Deployability**: Full redeployments are slow and risky, impacting **agility**.
        - **Example**: A new feature deployment takes hours and risks breaking unrelated functionality.
    - **Maintainability (Long-Term)**: As the codebase grows, tangled dependencies reduce maintainability.
    - **Testability**: Testing the entire system is complex, as components are not isolated.
    - **Example**: A monolithic system’s test suite takes hours to run, slowing development.

- **Trade-offs**:
    - **Advantages**:
        - **Ease of Development**: Single codebase simplifies coding, debugging, and tooling.
        - **Unified Deployment**: One deployment process reduces operational complexity.
        - **Transaction Consistency**: Easier to maintain data consistency within a single database.
        - **Example**: A monolithic banking app ensures ACID transactions across accounts without distributed complexity.
    - **Disadvantages**:
        - **Scalability Limits**: Cannot scale individual components independently.
        - **Deployment Bottlenecks**: Large deployments slow release cycles.
        - **Technical Debt**: Growth leads to complexity, reducing maintainability.
        - **Team Scaling**: Large teams struggle with a shared codebase, violating **Conway’s Law** (Chapter 1).
        - **Example**: A monolithic e-commerce system struggles to scale during a sale, requiring a full server upgrade.

- **Use Cases**:
    - **Small Systems**: Ideal for startups or MVPs with simple requirements and small teams.
    - **Low Traffic**: Suitable for applications with predictable, low user loads.
    - **Tightly Coupled Domains**: Where functionality is inherently interconnected (e.g., financial systems needing strong consistency).
    - **Example**: A small business’s inventory management system, with predictable usage and a single team, thrives as a monolith.

- **Modern Relevance (2025)**:
    - Monoliths remain relevant for **small-scale** or **greenfield** projects, especially in resource-constrained environments.
    - **Cloud Context**: Monoliths can run in containers (e.g., Docker) or cloud platforms (e.g., AWS EC2), but scaling is less efficient than microservices.
    - **Example**: A startup’s monolithic SaaS app runs on AWS ECS, providing simplicity but facing scalability challenges as users grow.
    - **Refactoring**: Many organizations refactor monoliths into microservices, guided by **fitness functions** (Chapter 6) to maintain characteristics like **modularity**.

- **Governance**:
    - Use **fitness functions** to monitor **maintainability** (e.g., code complexity <10) and **testability** (e.g., >80% coverage).
    - **Architecture Decision Records (ADRs)** document the choice of a monolith, justifying trade-offs.
    - **Example**: A fitness function fails if a monolith’s deployment time exceeds 10 minutes, prompting optimization.

---

### 4. Modular Monolithic Architecture (pp. 126–131)

- **Definition**:
    - The **Modular Monolithic** architecture is a refined version of the monolithic pattern, where the codebase is organized into well-defined, cohesive modules with clear boundaries, but still deployed as a single unit.
    - **Structure**: A single application with logical modules (e.g., packages, namespaces) separated by domain or function, often using frameworks to enforce boundaries.
    - **Metaphor**: Like a single building with distinct rooms, each with a specific purpose but sharing infrastructure.
    - **Example**: A Java-based e-commerce monolith with separate packages for `inventory`, `payments`, and `users`, using Spring to enforce module boundaries, deployed as one JAR file.

- **Characteristics**:
    - **Improved Modularity**: Modules are cohesive (focused on one domain) and loosely coupled (minimal dependencies), aligning with **modularity** principles (Chapter 3).
        - **Example**: The `inventory` module handles stock updates independently of the `payments` module.
    - **Single Deployment**: Like a monolith, the entire application is deployed together, simplifying operations.
    - **Centralized Database**: Typically uses one database, but modules access it through defined interfaces.
    - **Enhanced Maintainability**: Clear boundaries make changes easier than in a standard monolith.
    - **Example**: A modular monolithic CRM system allows developers to update the `customer` module without affecting `reports`, improving maintainability.

- **Architecture Characteristics Supported**:
    - **Maintainability**: Modular structure simplifies updates and reduces side effects.
    - **Testability**: Modules can be tested in isolation, improving quality.
    - **Simplicity**: Retains the operational simplicity of a monolith (single deployment, single database).
    - **Cost Efficiency**: Lower infrastructure costs than distributed systems.
    - **Example**: A modular monolithic retail system is easy to maintain and deploy, with low cloud costs.

- **Architecture Characteristics Hindered**:
    - **Scalability**: Still limited by single-unit deployment, requiring full replication to scale.
        - **Example**: Scaling the `inventory` module requires scaling the entire application.
    - **Deployability**: Full redeployments are slower than microservices, impacting **agility**.
    - **Team Autonomy**: Large teams may face coordination issues, though less severe than in a standard monolith.
    - **Example**: A modular monolith’s deployment takes 15 minutes, slowing feature releases compared to microservices.

- **Trade-offs**:
    - **Advantages**:
        - **Improved Maintainability**: Modular boundaries reduce complexity compared to a standard monolith.
        - **Ease of Refactoring**: Modules can be extracted into microservices later, supporting **evolutionary architecture** (Chapter 6).
        - **Simplicity**: Single deployment and database reduce operational overhead.
        - **Transaction Consistency**: Easier to maintain data integrity within a single database.
        - **Example**: A modular monolithic banking system ensures ACID transactions across modules while being easier to maintain than a Big Ball of Mud.
    - **Disadvantages**:
        - **Scalability Limits**: Cannot scale modules independently, like microservices.
        - **Deployment Bottlenecks**: Single deployment slows release cycles compared to distributed systems.
        - **Complexity Management**: Requires discipline to maintain module boundaries, or it risks becoming a Big Ball of Mud.
        - **Example**: A modular monolith struggles to scale during a traffic spike, requiring a full server upgrade.

- **Use Cases**:
    - **Medium-Sized Systems**: Suitable for applications with moderate complexity and team sizes, where microservices are overkill.
    - **Transition to Microservices**: Acts as a stepping stone, allowing modularization before splitting into services.
    - **Domains Requiring Consistency**: Where strong data consistency is critical (e.g., financial systems).
    - **Example**: A mid-sized SaaS application uses a modular monolith to balance maintainability and simplicity, planning to extract modules as microservices later.

- **Modern Relevance (2025)**:
    - Modular monoliths are gaining traction as a **pragmatic alternative** to microservices, especially for teams seeking maintainability without distributed system complexity.
    - **Cloud Context**: Run efficiently in containers (e.g., Kubernetes) or serverless platforms, though scaling is less granular than microservices.
    - **Example**: A modular monolithic healthcare system runs on Kubernetes, with modules for `patients` and `billing`, providing maintainability and planning for microservices migration.
    - **Frameworks**: Tools like **Spring Modulith** (Java) or **Django with apps** (Python) enforce module boundaries, aligning with **modularity** (Chapter 3).

- **Governance**:
    - Use **fitness functions** to enforce **modularity** (e.g., ArchUnit to prevent cross-module dependencies) and **maintainability** (e.g., SonarQube for code quality).
    - **ADRs** document module boundaries and trade-offs.
    - **Example**: A fitness function fails if a module accesses another’s internal data, ensuring **modularity**.

---

### 5. Comparing Foundational Patterns (pp. 131–133)

- **Big Ball of Mud**:
    - **Structure**: Unstructured, chaotic codebase.
    - **Strengths**: Rapid development for prototypes.
    - **Weaknesses**: Poor maintainability, scalability, and testability.
    - **Use Case**: Temporary prototypes or throwaway systems.
    - **Example**: A hackathon app, discarded after use.

- **Monolithic**:
    - **Structure**: Single, unified codebase and deployment.
    - **Strengths**: Simplicity, cost efficiency, transaction consistency.
    - **Weaknesses**: Limited scalability, slow deployments, long-term maintainability issues.
    - **Use Case**: Small systems, low-traffic apps, or domains needing strong consistency.
    - **Example**: A small business’s inventory system.

- **Modular Monolithic**:
    - **Structure**: Single codebase with modular boundaries, single deployment.
    - **Strengths**: Improved maintainability, testability, and refactoring potential; retains monolith simplicity.
    - **Weaknesses**: Scalability and deployability limitations compared to distributed systems.
    - **Use Case**: Medium-sized systems, transition to microservices, or consistency-driven domains.
    - **Example**: A SaaS app planning microservices migration.

- **Trade-off Analysis**:
    - **Simplicity**: Big Ball of Mud > Monolithic > Modular Monolithic.
    - **Maintainability**: Modular Monolithic > Monolithic > Big Ball of Mud.
    - **Scalability**: None excel; Modular Monolithic is slightly better due to refactoring potential.
    - **Deployability**: All limited by single-unit deployment, worst in Big Ball of Mud.
    - **Example**: A startup chooses a **Modular Monolithic** pattern for maintainability and future scalability, avoiding the chaos of a Big Ball of Mud and the limitations of a standard monolith.

- **Selecting a Pattern**:
    - **Business Context**: Startups favor **Monolithic** or **Modular Monolithic** for speed; enterprises may avoid **Big Ball of Mud** due to maintenance costs.
    - **Team Size**: Small teams suit **Monolithic**; larger teams benefit from **Modular Monolithic** for coordination.
    - **Scalability Needs**: Low-traffic systems tolerate monoliths; high-traffic systems may need to evolve to microservices.
    - **Example**: A fintech startup with a small team and strong consistency needs chooses a **Modular Monolithic** pattern, planning to extract microservices as it grows.

---

### 6. Practical Implications and Architectural Considerations

- **Role of the Architect**:
    - Architects **evaluate patterns**, selecting foundational patterns based on business goals, team capabilities, and technical constraints.
    - They **document decisions**, using **ADRs** to justify choices and trade-offs (Chapter 19).
    - Architects **govern**, using **fitness functions** to ensure patterns maintain characteristics like **modularity** and **maintainability**.
    - **Example**: An architect chooses a **Modular Monolithic** pattern for a retail system, documenting trade-offs in an ADR and implementing ArchUnit checks for modularity.

- **Impact on System Design**:
    - Foundational patterns influence **architecture characteristics** (Chapter 4):
        - **Big Ball of Mud**: Hinders most characteristics, risking technical debt.
        - **Monolithic**: Supports **simplicity** and **cost efficiency** but limits **scalability**.
        - **Modular Monolithic**: Balances **maintainability** and **simplicity**, with refactoring potential.
    - They guide **technology choices**:
        - **Big Ball of Mud**: No specific tools; ad-hoc frameworks.
        - **Monolithic**: Traditional frameworks like Spring, Django.
        - **Modular Monolithic**: Frameworks like Spring Modulith, Ruby on Rails with engines.
    - **Example**: A modular monolithic SaaS app uses Spring Modulith to enforce module boundaries, supporting **maintainability**.

- **Alignment with Business Goals**:
    - Patterns align with **business drivers** (Chapter 2):
        - **Speed to Market**: **Monolithic** or **Big Ball of Mud** for rapid development.
        - **Maintainability**: **Modular Monolithic** for long-term efficiency.
        - **Cost Efficiency**: Monoliths reduce infrastructure costs.
    - **Example**: A startup’s **Modular Monolithic** CRM aligns with goals of rapid development and future scalability.

- **Team and Organizational Impact**:
    - **Conway’s Law** (Chapter 1): Team structures must align with pattern boundaries:
        - **Big Ball of Mud**: Misaligned teams exacerbate chaos.
        - **Monolithic**: Single team or tightly coordinated teams.
        - **Modular Monolithic**: Teams can own modules, improving coordination.
    - **Example**: A modular monolithic system assigns teams to specific modules (e.g., `inventory`, `payments`), reducing conflicts.

- **Evolutionary Architecture**:
    - Foundational patterns support **evolutionary architecture** (Chapter 6):
        - **Big Ball of Mud**: Hard to evolve, requiring a rewrite.
        - **Monolithic**: Can evolve to **Modular Monolithic** or microservices with effort.
        - **Modular Monolithic**: Designed for evolution, enabling module extraction.
    - **Example**: A modular monolithic system evolves to microservices by extracting the `payments` module, validated by fitness functions.

- **Modern Relevance (2025)**:
    - **Monoliths** remain viable for **small-scale** or **consistency-driven** systems, running in containers or serverless environments.
    - **Modular Monoliths** are a **pragmatic choice** for teams avoiding microservices complexity, supported by frameworks like **Spring Modulith**.
    - **Big Ball of Mud** persists in **legacy systems**, requiring modernization with tools like **SonarQube** or **OpenTelemetry** for observability.
    - **Observability**: Critical for monoliths, requiring integration of tools like **OpenTelemetry** to monitor performance.
        - **Example**: A modular monolithic system uses OpenTelemetry to trace module interactions, ensuring **observability**.
    - **Sustainability**: Monoliths reduce cloud resource usage compared to distributed systems, aligning with green computing.
        - **Example**: A monolithic app on AWS ECS minimizes energy costs.

- **Common Pitfalls**:
    - **Choosing Big Ball of Mud**: Leads to technical debt unless strictly temporary.
    - **Sticking with Monolithic**: Fails to scale for high-traffic systems.
    - **Poor Modularization**: A **Modular Monolithic** becomes a Big Ball of Mud without strict boundaries.
    - **Ignoring Governance**: Lack of fitness functions allows drift.
    - **Example**: A monolithic system grows without modularization, becoming unmaintainable, highlighting the need for governance.

---

### 7. Self-Assessment Questions (p. 374)

The chapter includes self-assessment questions to reinforce concepts, such as:
- What are the key differences between Big Ball of Mud, Monolithic, and Modular Monolithic patterns?
- How do these patterns support or hinder architecture characteristics like scalability and maintainability?
- When is each pattern most appropriate, and what are their trade-offs?
- How can architects use fitness functions to govern foundational patterns?
- What challenges arise when evolving from a monolithic to a modular monolithic or distributed architecture?

These questions encourage architects to evaluate patterns in real-world contexts.

---

### Key Takeaways

- **Foundational Patterns**: **Big Ball of Mud**, **Monolithic**, and **Modular Monolithic** are simple, widely used architectures, each with distinct structures and trade-offs.
- **Big Ball of Mud**: An anti-pattern with chaotic structure, suitable only for temporary prototypes, hindering most characteristics.
- **Monolithic**: A single-unit architecture offering simplicity and cost efficiency but limited scalability and deployability.
- **Modular Monolithic**: A refined monolith with modular boundaries, balancing maintainability and simplicity, ideal for medium-sized systems or microservices transitions.
- **Trade-offs**: Patterns vary in **simplicity**, **maintainability**, **scalability**, and **deployability**, requiring context-driven selection.
- **Governance**: **Fitness functions** and **ADRs** ensure patterns align with characteristics, preventing drift.
- **Architect’s Role**: Evaluate patterns, document decisions, and govern to maintain quality, aligning with business and technical needs.

---

### Context and Relevance

Chapter 7 builds on:
- **Chapter 1 (Introduction)**: Introduced architecture principles (e.g., Conway’s Law, Gall’s Law) guiding pattern selection.
- **Chapter 2 (Architectural Thinking)**: Trade-off analysis informs pattern choice.
- **Chapter 3 (Modularity)**: **Modular Monolithic** aligns with cohesive, loosely coupled designs.
- **Chapter 4 (Architecture Characteristics)**: Patterns support or hinder characteristics like **scalability**.
- **Chapter 5 (Identifying Architecture Characteristics)**: Characteristics guide pattern selection.
- **Chapter 6 (Measuring and Governing Architecture)**: Fitness functions validate pattern alignment.

It prepares for:
- **Chapters 8–15 (Advanced Patterns)**: Compares foundational patterns to complex ones (e.g., microservices, event-driven).
- **Chapter 19 (Architecture Decision Records)**: ADRs document pattern choices.
- **Chapters 16–24 (Governance, Soft Skills)**: Emphasize collaboration in pattern implementation.

In 2025, foundational patterns remain relevant for **small-scale**, **legacy**, or **transitional** systems. **Modular Monoliths** are a popular choice for balancing simplicity and maintainability, supported by modern frameworks like **Spring Modulith**. **Monoliths** suit resource-constrained environments, while **Big Ball of Mud** persists in legacy systems, requiring modernization. **Observability** (e.g., OpenTelemetry) and **sustainability** (e.g., efficient cloud usage) enhance these patterns, aligning with **DevOps**, **agile**, and **green computing** trends.

---

### Additional Notes
- **Modern Relevance**: In 2025, **Modular Monoliths** are a pragmatic alternative to microservices, supported by tools like **Spring Modulith** or **Django**. **Observability** is critical, with **OpenTelemetry** ensuring monoliths monitor performance. **Sustainability** favors monoliths for lower cloud costs.
- **Practical Example**: For a smart home IoT system:
    - **Business Goal**: Rapid development, moderate scalability.
    - **Pattern**: **Modular Monolithic** with modules for `sensors`, `analytics`, and `users`.
    - **Characteristics**: **Maintainability** (modular boundaries), **simplicity** (single deployment), **testability** (isolated modules).
    - **Fitness Functions**: ArchUnit for **modularity**, JaCoCo for **testability** (>80% coverage).
    - **Governance**: ADRs document module boundaries; teams align with modules per **Conway’s Law**.
    - **Evolution**: Plan to extract `analytics` as a microservice, supported by modular design.
- **Connection to Modularity**: **Modular Monolithic** directly applies **modularity** principles (Chapter 3), ensuring cohesive, loosely coupled modules.
- **Connection to Characteristics**: Patterns support or hinder characteristics (Chapter 4), validated by fitness functions (Chapter 6).