**Chapter 5: Identifying Architecture Characteristics**

---

### Overview
Chapter 5 builds on Chapter 4’s introduction to architecture characteristics (non-functional requirements like scalability, performance, security) by providing a **practical methodology** for identifying these qualities in real-world projects. It outlines a structured approach to extract both **explicit** (e.g., availability) and **implicit** (e.g., maintainability) characteristics from various sources, refine them into measurable metrics, and prioritize them to align with stakeholder needs and business goals. The chapter emphasizes stakeholder collaboration, domain knowledge, and iterative refinement, making it essential for architects transitioning from understanding characteristics to applying them effectively.

---

### 1. The Importance of Identifying Architecture Characteristics (pp. 73–75)

- **Why Identification Matters**:
   - Architecture characteristics define the **quality attributes** of a system (e.g., scalability, reliability, agility), ensuring it meets business, user, and operational expectations beyond functional requirements (e.g., “process an order”).
   - Proper identification ensures architects focus on the right qualities, avoiding systems that are technically functional but fail to deliver value (e.g., a system that processes orders but crashes under load).
   - **Explicit Characteristics**: Stated in requirements (e.g., “99.9% uptime” → availability) and tied to immediate needs.
   - **Implicit Characteristics**: Often unstated but critical for long-term success (e.g., maintainability, testability), preventing technical debt or operational issues.
   - **Example**: An e-commerce system prioritizing only scalability (explicit) might neglect testability (implicit), leading to unreliable updates and costly bugs.

- **Challenges in Identification**:
   - **Vague Requirements**: Stakeholders often provide ambiguous goals (e.g., “the system must be secure”), requiring architects to quantify and clarify.
   - **Stakeholder Misalignment**: Business leaders, users, developers, and operations teams prioritize different characteristics (e.g., cost vs. maintainability), necessitating negotiation.
   - **Overlooking Implicit Characteristics**: Without proactive effort, qualities like supportability or deployability may be ignored, undermining system health.
   - **Example**: A stakeholder demand for “fast performance” without latency thresholds can lead to misaligned expectations unless clarified as “<200ms API response time.”

- **Role of the Architect**:
   - Architects act as **translators**, converting vague or incomplete requirements into actionable, measurable characteristics.
   - They must **advocate** for implicit characteristics to ensure long-term viability, even when stakeholders focus on immediate, explicit needs.
   - Architects bridge technical and business domains, aligning characteristics with organizational goals (e.g., scalability for market growth).
   - **Example**: An architect might identify observability as essential for a microservices system, convincing stakeholders of its role in reducing downtime.

- **Connection to Previous Chapters**:
   - **Chapter 1 (Introduction)**: Established characteristics as a core component of architecture, aligning systems with stakeholder expectations.
   - **Chapter 2 (Architectural Thinking)**: Emphasized trade-off analysis and business alignment, critical for prioritizing characteristics.
   - **Chapter 3 (Modularity)**: Noted that modularity supports characteristics like maintainability and testability through cohesive, loosely coupled designs.
   - **Chapter 4 (Architecture Characteristics Defined)**: Provided a taxonomy (explicit, implicit, cross-cutting) and introduced the need for identification, which Chapter 5 operationalizes.

---

### 2. Sources of Architecture Characteristics (pp. 75–79)

The chapter identifies **five key sources** from which architects can extract architecture characteristics, ensuring a comprehensive approach to capture both explicit and implicit needs.

#### a. Business Requirements
- **Description**: Organizational goals, such as increasing revenue, reducing costs, improving customer satisfaction, or meeting regulatory standards.
- **Examples**:
   - Goal: “Support 1 million users” → **Scalability**, **performance**.
   - Goal: “Reduce infrastructure costs” → **Cost efficiency**, **simplicity**.
   - Goal: “Comply with PCI-DSS” → **Security**, **auditability**.
- **Identification Process**:
   - Engage with business stakeholders (e.g., executives, product owners) through interviews or workshops to understand strategic objectives.
   - Translate goals into technical characteristics by asking, “What qualities enable this outcome?”
   - **Example**: A goal of “launch new features weekly” translates to **agility** (rapid iteration) and **deployability** (frequent deployments).
- **Significance**: Business requirements ensure the system aligns with organizational priorities, driving value delivery.

#### b. User Needs
- **Description**: End-user expectations for functionality, performance, and experience, often tied to satisfaction and retention.
- **Examples**:
   - Expectation: “Instant search results” → **Performance**, **usability**.
   - Expectation: “Secure data storage” → **Security**, **reliability**.
- **Identification Process**:
   - Gather feedback via surveys, usability testing, or direct user interviews.
   - Focus on qualities impacting user experience, such as responsiveness, accessibility, or data privacy.
   - **Example**: Users reporting slow load times on a retail website highlight **performance** and **scalability** needs.
- **Significance**: User needs ensure the system is user-centric, supporting adoption and satisfaction.

#### c. Operational Needs
- **Description**: Requirements from IT, DevOps, or operations teams, focusing on system management, deployment, monitoring, and reliability.
- **Examples**:
   - Need: “Zero-downtime deployments” → **Deployability**, **availability**.
   - Need: “Real-time error tracking” → **Supportability**, **observability**.
- **Identification Process**:
   - Collaborate with operations teams to understand infrastructure, monitoring, and maintenance requirements.
   - Identify characteristics that ensure operational efficiency, such as logging, fault tolerance, or scalability.
   - **Example**: A DevOps requirement for “quick recovery from outages” drives **resilience** and **fault tolerance**.
- **Significance**: Operational needs ensure the system is manageable and reliable in production.

#### d. Domain Constraints
- **Description**: Industry-specific regulations, standards, or operational realities that mandate certain characteristics.
- **Examples**:
   - Regulation: HIPAA (healthcare) → **Security**, **auditability**.
   - Standard: PCI-DSS (payments) → **Security**, **reliability**.
   - Domain Reality: High-traffic social media → **Scalability**, **elasticity**.
- **Identification Process**:
   - Research domain-specific regulations, standards, or competitive benchmarks.
   - Consult domain experts to uncover constraints not explicitly stated in requirements.
   - **Example**: A financial system’s compliance with anti-money laundering laws requires **auditability** and **traceability**.
- **Significance**: Domain constraints ensure legal compliance and market competitiveness.

#### e. Existing Systems and Constraints
- **Description**: Legacy systems, integrations, or technical constraints (e.g., budget, hardware) that impose requirements or limit choices.
- **Examples**:
   - Constraint: Integrate with a legacy ERP system → **Interoperability**, **modularity**.
   - Constraint: Limited cloud budget → **Cost efficiency**, **simplicity**.
- **Identification Process**:
   - Analyze existing systems to identify compatibility, performance, or scalability requirements.
   - Consider constraints like vendor lock-in, outdated APIs, or resource limitations.
   - **Example**: A system integrating with a slow legacy database might require **caching** (performance) and **modularity** to isolate new components.
- **Significance**: Existing systems ensure the architecture is feasible within technical and financial constraints.

- **Practical Application**:
   - Architects should systematically review all sources using a **checklist** to avoid missing characteristics.
   - **Example**: For a logistics system:
      - Business: “Track 10,000 shipments daily” → **Scalability**.
      - Users: “Real-time tracking updates” → **Performance**.
      - Operations: “Monitor fleet status” → **Observability**.
      - Domain: “Comply with transport regulations” → **Auditability**.
      - Legacy: “Integrate with old GPS system” → **Interoperability**.

---

### 3. Techniques for Identifying Characteristics (pp. 79–84)

The chapter outlines **five practical techniques** to extract architecture characteristics, ensuring architects capture both explicit and implicit qualities through structured and collaborative methods.

#### a. Stakeholder Workshops
- **Description**: Facilitated sessions with stakeholders (business, users, developers, operations) to elicit requirements and uncover characteristics.
- **Process**:
   - Organize workshops to discuss goals, pain points, and expectations.
   - Use techniques like **user story mapping** (to identify user needs) or **goal-question-metric (GQM)** (to link goals to metrics).
   - Pose probing questions to surface implicit needs:
      - “What happens during peak traffic?” → **Scalability**, **performance**.
      - “How will we maintain the system?” → **Maintainability**, **supportability**.
   - **Example**: A workshop for a streaming platform reveals a need to “support 100,000 concurrent streams” (scalability) and “quickly resolve playback issues” (supportability).
- **Benefits**:
   - Captures diverse perspectives, ensuring comprehensive characteristic coverage.
   - Builds stakeholder alignment and buy-in early in the process.
- **Challenges**:
   - Managing conflicting priorities (e.g., business wants low costs, developers want testability).
   - Requires facilitation skills to keep discussions focused.
   - **Example**: A business leader’s focus on cost might overshadow a developer’s need for automated testing, requiring mediation.

#### b. Requirement Analysis
- **Description**: Reviewing requirement documents, user stories, or specifications to extract explicit and implicit characteristics.
- **Process**:
   - Parse documents for explicit characteristics (e.g., “99.99% uptime” → availability).
   - Infer implicit characteristics by identifying gaps (e.g., no mention of debugging → supportability).
   - Use domain knowledge to spot regulatory or industry needs (e.g., GDPR → security).
   - Validate findings with stakeholders to ensure accuracy.
   - **Example**: A user story stating “users can log in quickly” implies **performance** (<1-second latency) and **reliability**, while the absence of maintenance plans suggests **maintainability**.
- **Benefits**:
   - Leverages existing documentation, saving time.
   - Formalizes vague requirements into measurable characteristics.
- **Challenges**:
   - Documents may be incomplete, outdated, or ambiguous, requiring follow-up.
   - **Example**: A requirement for “secure transactions” needs clarification on encryption standards.

#### c. Domain Analysis
- **Description**: Researching the business domain to identify characteristics driven by regulations, standards, or competitive pressures.
- **Process**:
   - Study industry regulations (e.g., HIPAA, PCI-DSS) to identify mandatory characteristics (e.g., security, auditability).
   - Analyze competitors or benchmarks to uncover expected qualities (e.g., low latency in e-commerce).
   - Consult domain experts to validate regulatory or operational constraints.
   - **Example**: In healthcare, domain analysis reveals **security** (patient data privacy) and **auditability** (tracking access) as non-negotiable characteristics.
- **Benefits**:
   - Ensures compliance with legal and industry standards.
   - Aligns the system with market expectations.
- **Challenges**:
   - Requires architects to quickly learn domain nuances, which can be time-intensive.
   - **Example**: Misinterpreting a regulation could over-emphasize security at the expense of performance.

#### d. Prototyping and Spikes
- **Description**: Building small prototypes or conducting technical spikes to explore technical constraints and uncover characteristics.
- **Process**:
   - Create a proof-of-concept to test assumptions (e.g., can a database handle 10,000 queries per second? → performance).
   - Use spikes to investigate integration challenges or scalability limits.
   - Identify characteristics based on findings (e.g., need for elasticity in variable workloads).
   - **Example**: A prototype for a real-time chat system might reveal a need for **elasticity** to handle peak user activity and **latency** for responsive messaging.
- **Benefits**:
   - Provides empirical data to validate requirements.
   - Uncovers implicit characteristics like interoperability or resilience.
- **Challenges**:
   - Time-consuming and requires careful scoping to avoid over-investment.
   - **Example**: A prototype focused on scalability might miss maintainability needs if not balanced.

#### e. Asking the Right Questions
- **Description**: Using targeted questions to uncover implicit or cross-cutting characteristics that stakeholders may not articulate.
- **Examples of Questions**:
   - “What happens if a component fails?” → **Resilience**, **fault tolerance**.
   - “How often will features change?” → **Agility**, **maintainability**.
   - “How will we monitor system health?” → **Observability**, **supportability**.
   - “What compliance requirements apply?” → **Security**, **auditability**.
- **Process**:
   - Ask questions during workshops, interviews, or reviews to surface hidden needs.
   - Focus on operational, maintenance, and failure scenarios to identify implicit characteristics.
   - **Example**: Asking “How will we handle a traffic surge?” for a gaming platform reveals **scalability** and **elasticity** requirements.
- **Benefits**:
   - Ensures comprehensive coverage, especially for implicit characteristics.
   - Encourages stakeholders to consider long-term needs.
- **Challenges**:
   - Requires tact to avoid overwhelming stakeholders with technical questions.
   - **Example**: Business stakeholders may not understand “observability” unless explained as “tracking system health like a car dashboard.”

- **Practical Application**:
   - Combine techniques for optimal results: use workshops to gather initial needs, requirement analysis to refine them, domain analysis for regulatory constraints, prototyping for technical validation, and questions to uncover gaps.
   - Document findings in a **characteristics catalog** (e.g., a table mapping sources to characteristics) to track and validate them.
   - **Example**: For a banking system, a workshop identifies **security** (explicit), requirement analysis adds **maintainability** (implicit), domain analysis confirms **auditability** (cross-cutting), and a prototype validates **performance**.

---

### 4. Refining and Quantifying Characteristics (pp. 84–87)

After identification, characteristics must be **refined** into specific, measurable qualities and **prioritized** to guide architectural decisions and ensure alignment with stakeholder priorities.

#### a. Making Characteristics Specific
- **Description**: Vague or qualitative characteristics (e.g., “fast,” “secure”) must be translated into precise, actionable definitions.
- **Process**:
   - Define **metrics** or **thresholds** to quantify characteristics:
      - **Performance**: “API latency <100ms for 95% of requests.”
      - **Scalability**: “Handle 10,000 concurrent users with <10% resource increase.”
      - **Availability**: “99.99% uptime over 30 days.”
      - **Maintainability**: “Cyclomatic complexity <10 for 90% of code” (a proxy metric).
   - Use **SLAs** (Service Level Agreements) or **SLOs** (Service Level Objectives) to formalize expectations.
   - Validate metrics with stakeholders to ensure they reflect priorities.
   - **Example**: A vague requirement for “high availability” becomes “99.9% uptime with <1 hour of downtime annually.”
- **Benefits**:
   - Specific characteristics are testable, enabling validation via monitoring tools or fitness functions (Chapter 6).
   - Reduces ambiguity and aligns expectations.
- **Challenges**:
   - Stakeholders may resist quantifying vague terms, requiring negotiation to balance feasibility and ambition.
   - **Example**: A business leader’s demand for “maximum security” needs clarification on acceptable latency trade-offs.

#### b. Prioritizing Characteristics
- **Description**: Due to trade-offs (e.g., security vs. performance) and resource constraints (e.g., budget, time), architects must prioritize characteristics based on context.
- **Process**:
   - Use prioritization frameworks:
      - **MoSCoW**: Must-have, Should-have, Could-have, Won’t-have.
      - **Weighted Scoring**: Assign weights based on business impact, cost, or technical feasibility.
   - Engage stakeholders to rank characteristics:
      - **Example**: A healthcare system prioritizes **security** and **auditability** over **agility** due to regulatory requirements.
      - **Example**: A startup prioritizes **agility** and **scalability** to support rapid growth.
   - Consider trade-offs explicitly (building on Chapter 4):
      - **Security** may increase latency, impacting **performance**.
      - **Scalability** may add complexity, reducing **maintainability**.
   - **Example**: A social media platform might prioritize **availability** and **scalability** over **consistency**, choosing eventual consistency to ensure uptime during traffic spikes.
- **Benefits**:
   - Focuses resources on critical characteristics, avoiding over-engineering.
   - Aligns the architecture with business and stakeholder priorities.
- **Challenges**:
   - Conflicting priorities (e.g., developers want **testability**, executives want **cost efficiency**) require careful mediation.
   - **Example**: A developer’s push for automated testing may conflict with a tight budget, necessitating compromise.

#### c. Documenting Characteristics
- **Description**: Record characteristics, their metrics, and prioritization rationale to ensure transparency and facilitate governance.
- **Process**:
   - Use **Architecture Decision Records (ADRs)** (Chapter 19) to document:
      - Identified characteristics (e.g., scalability, security).
      - Metrics and thresholds (e.g., “latency <200ms”).
      - Prioritization rationale and trade-offs (e.g., “prioritized scalability over consistency for user growth”).
   - Create a **characteristics catalog** or **traceability matrix** to map characteristics to sources (e.g., business goals, regulations).
   - **Example**: An ADR for a logistics system might document prioritizing **scalability** (“handle 10,000 shipments daily”) and **observability** (“real-time monitoring”) to support operational efficiency, with metrics and trade-offs noted.
- **Benefits**:
   - Provides a reference for stakeholders, developers, and future architects.
   - Supports governance by enabling validation (e.g., via fitness functions).
- **Challenges**:
   - Requires ongoing maintenance to reflect evolving requirements.
   - **Example**: Outdated ADRs can cause misalignment if new regulations change priorities.

---

### 5. Practical Considerations and Challenges (pp. 87–89)

- **Iterative Identification**:
   - Identifying characteristics is an **ongoing process**, as requirements, markets, or technologies evolve.
   - Architects must revisit characteristics during system evolution, aligning with **evolutionary architecture** (Chapter 6).
   - **Example**: A system initially prioritizing **performance** might later emphasize **security** as cyber threats increase.

- **Stakeholder Engagement**:
   - Effective collaboration is critical to capture diverse perspectives and resolve conflicts.
   - Architects must communicate technical concepts (e.g., observability) to non-technical stakeholders using analogies or simple explanations.
   - **Example**: Explaining observability as “a dashboard for system health” helps executives understand its value.

- **Domain Knowledge**:
   - Understanding the business domain (e.g., finance, healthcare) is essential to identify regulatory or competitive characteristics.
   - Architects may need to research or consult domain experts to uncover constraints.
   - **Example**: In a retail system, domain knowledge reveals the need for **elasticity** to handle seasonal traffic spikes.

- **Balancing Explicit and Implicit Characteristics**:
   - Architects must advocate for implicit characteristics (e.g., maintainability, testability) to prevent long-term issues, even when stakeholders prioritize explicit ones (e.g., scalability).
   - **Example**: Convincing a team to invest in testability by highlighting reduced bug-fixing costs.

- **Avoiding Over-Specification**:
   - Prioritizing too many characteristics can lead to over-engineered systems, violating **Gall’s Law** (simplicity, from Chapter 1).
   - Focus on the “least worst” set of characteristics for the context.
   - **Example**: A startup building an MVP might prioritize **agility** and **scalability**, deferring **auditability** until regulatory needs arise.

- **Tools and Techniques**:
   - Use collaborative tools (e.g., Miro, Confluence) for workshops to capture stakeholder inputs.
   - Leverage requirement management tools (e.g., Jira, DOORS) to track characteristics and their sources.
   - Employ monitoring tools (e.g., Prometheus) to validate characteristics post-identification.
   - **Example**: A traceability matrix in Confluence maps **scalability** to a business goal of “support 1 million users,” aiding stakeholder alignment.

- **Common Challenges**:
   - **Incomplete Requirements**: Stakeholders may omit implicit needs, requiring proactive questioning.
   - **Conflicting Priorities**: Balancing business, user, and operational needs demands negotiation.
   - **Lack of Domain Expertise**: Architects unfamiliar with the domain may miss critical characteristics.
   - **Over-Reliance on One Source**: Focusing only on business requirements might neglect user or operational needs.
   - **Example**: A system over-optimized for **performance** due to user demands might sacrifice **maintainability**, complicating future updates.

---

### 6. Practical Implications and Architectural Considerations

- **Role of the Architect**:
   - Architects are **facilitators**, orchestrating stakeholder inputs to identify and prioritize characteristics.
   - They **advocate** for implicit characteristics to ensure long-term system health, balancing short-term demands.
   - Architects bridge technical and business domains, justifying characteristics to stakeholders.
   - **Example**: An architect might demonstrate that **observability** reduces downtime costs, convincing executives to prioritize it alongside **performance**.

- **Impact on System Design**:
   - Identified characteristics drive **architectural styles** (Chapters 7–15):
      - **Microservices**: For scalability, agility.
      - **Event-driven**: For elasticity, responsiveness.
      - **Layered**: For simplicity, maintainability.
   - They influence **technology choices**:
      - **Redis**: For low-latency performance.
      - **Kubernetes**: For scalability, deployability.
   - Characteristics connect to **modularity** (Chapter 3), as cohesive, loosely coupled modules support **maintainability** and **testability**.
   - **Example**: A system prioritizing **deployability** adopts microservices with CI/CD pipelines, leveraging modularity for independent deployments.

- **Alignment with Business Goals**:
   - Characteristics translate **business drivers** (Chapter 2) into technical requirements, ensuring value delivery:
      - **Scalability**: Supports market expansion.
      - **Security**: Ensures compliance.
      - **Agility**: Enables rapid iteration.
   - Domain knowledge is critical to justify characteristics.
   - **Example**: In a healthcare system, prioritizing **security** and **auditability** aligns with regulatory compliance goals.

- **Evolutionary Architecture**:
   - Characteristics evolve with requirements, aligning with **evolutionary architecture** (Chapter 6).
   - Governance (e.g., fitness functions, ADRs) ensures characteristics remain relevant during incremental changes.
   - **Example**: A system initially focused on **performance** might later prioritize **elasticity** as it scales to cloud infrastructure.

- **Team and Organizational Impact**:
   - Characteristics influence **team processes**:
      - **Testability**: Requires automated testing expertise.
      - **Deployability**: Demands CI/CD pipelines.
   - **Conway’s Law** (Chapter 1) suggests aligning team structures with characteristics (e.g., autonomous teams for scalable microservices).
   - **Example**: A team managing a scalable service must own its monitoring tools to ensure **observability**.

- **Common Pitfalls**:
   - **Missing Implicit Characteristics**: Focusing only on explicit needs (e.g., scalability) leads to unmaintainable systems.
   - **Over-Prioritization**: Optimizing too many characteristics results in complex designs.
   - **Vague Specifications**: Unquantified characteristics (e.g., “secure”) are untestable.
   - **Stakeholder Disconnect**: Failing to engage stakeholders causes misaligned priorities.
   - **Example**: A system over-optimized for **security** might sacrifice **performance**, frustrating users.

---

### 7. Self-Assessment Questions (p. 374)

The chapter includes self-assessment questions to reinforce key concepts, such as:
- Why is identifying architecture characteristics critical for effective architecture?
- What are the primary sources for extracting architecture characteristics, and how do they differ?
- How do techniques like stakeholder workshops or prototyping aid in identifying characteristics?
- Why is quantifying and prioritizing characteristics important, and what methods can architects use?
- What challenges do architects face in identifying characteristics, and how can they overcome them?

These questions encourage architects to apply the identification process to real-world scenarios, fostering practical skills.

---

### Key Takeaways

- **Critical Role**: Identifying architecture characteristics ensures systems meet stakeholder needs, capturing both explicit (e.g., scalability) and implicit (e.g., maintainability) qualities.
- **Sources**: Characteristics are derived from business requirements, user needs, operational needs, domain constraints, and existing systems, requiring a comprehensive approach.
- **Techniques**: Stakeholder workshops, requirement analysis, domain analysis, prototyping, and targeted questions uncover and refine characteristics.
- **Refinement and Prioritization**: Characteristics must be specific (e.g., “latency <100ms”), prioritized (e.g., via MoSCoW), and documented (e.g., in ADRs) to guide design.
- **Architect’s Role**: Architects translate requirements, advocate for implicit needs, and align with business goals through stakeholder collaboration.
- **Practical Impact**: Characteristics drive architectural styles, technology choices, and team processes, supporting evolutionary architecture and long-term system health.

---

### Context and Relevance

Chapter 5 is a practical extension of Chapter 4, which defined architecture characteristics and their taxonomy (explicit, implicit, cross-cutting). It provides actionable techniques to operationalize the identification process, making it essential for architects working on real-world projects. The chapter’s focus on stakeholder collaboration and iterative refinement aligns with modern agile and DevOps practices, where cross-functional teamwork and adaptability are critical.

In 2025, the chapter’s emphasis on identifying characteristics like **observability** (crucial for microservices monitoring) and **elasticity** (vital for cloud-native scaling) is highly relevant, as organizations increasingly adopt distributed, cloud-based architectures. The techniques for uncovering implicit characteristics (e.g., through prototyping or questioning) are particularly valuable in complex systems where long-term maintainability and operational efficiency are as critical as immediate performance.

The chapter sets the stage for:
- **Chapter 6 (Architecture Fitness Functions)**: Measuring and validating identified characteristics.
- **Chapters 7–15 (Architecture Styles)**: Selecting styles based on prioritized characteristics.
- **Chapters 16–24 (Governance and Soft Skills)**: Documenting and governing characteristics through ADRs and stakeholder communication.

---

### Additional Notes
- **Modern Relevance**: In 2025, emerging characteristics like **sustainability** (e.g., energy-efficient cloud usage) and **resilience** (e.g., handling cyber threats) are critical in domains like IoT, fintech, and green tech. The chapter’s flexible identification process supports these evolving needs.
- **Practical Example**: For a ride-sharing platform:
   - **Business**: “Support 1 million rides daily” → **Scalability**, **performance**.
   - **Users**: “Match drivers in <5 seconds” → **Performance**, **usability**.
   - **Operations**: “Monitor driver locations” → **Observability**, **supportability**.
   - **Domain**: “Comply with transport laws” → **Auditability**, **security**.
   - **Legacy**: “Integrate with old payment system” → **Interoperability**.
   - **Workshop Outcome**: Prioritizes **scalability** and **performance**, with **observability** as an implicit need, documented in an ADR.
- **Connection to Modularity**: Characteristics like **maintainability** and **testability** rely on modularity (Chapter 3), as cohesive, loosely coupled modules are easier to update and test.