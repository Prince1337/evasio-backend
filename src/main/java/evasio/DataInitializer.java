package evasio;

import evasio.model.Module;
import evasio.model.QuestionType;
import evasio.model.Quiz;
import evasio.model.Role;
import evasio.model.Topic;
import evasio.model.User;
import evasio.repositories.ModuleRepository;
import evasio.repositories.QuizRepository;
import evasio.repositories.RoleRepository;
import evasio.repositories.TopicRepository;
import evasio.repositories.UserRepository;
import evasio.services.JwtService;
import evasio.auth.AuthenticationService;
import evasio.auth.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void run(String... args) throws Exception {
        // Initialisiere grundlegende Roles
        initializeRoles();
        
        // Initialisiere Admin-Account
        initializeAdminAccount();
        
        // Prüfe ob bereits Daten vorhanden sind
        if (topicRepository.count() > 0) {
            System.out.println("Daten bereits vorhanden, überspringe Initialisierung");
            return;
        }

        System.out.println("Initialisiere Sample-Daten...");

        // Topic 1: Java Grundlagen
        Topic javaBasics = Topic.builder()
                .title("Java Grundlagen")
                .description("Lerne die Grundlagen der Java-Programmierung")
                .category("Programmierung")
                .difficulty("Anfänger")
                .build();

        javaBasics = topicRepository.save(javaBasics);

        // Module für Java Grundlagen
        Module javaIntro = Module.builder()
                .title("Einführung in Java")
                .content("Java ist eine objektorientierte Programmiersprache, die 1995 von Sun Microsystems entwickelt wurde. " +
                        "Sie ist plattformunabhängig und wird für die Entwicklung von Desktop-Anwendungen, Web-Anwendungen " +
                        "und mobilen Apps verwendet.")
                .topic(javaBasics)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaBasics.addModule(javaIntro);
        javaIntro = moduleRepository.save(javaIntro);
        topicRepository.save(javaBasics);

        // Quiz für Java Einführung
        Quiz javaQuiz1 = Quiz.builder()
                .question("Was ist Java?")
                .options(Arrays.asList(
                        "Eine Programmiersprache",
                        "Ein Betriebssystem",
                        "Eine Datenbank",
                        "Ein Webbrowser"
                ))
                .correctAnswer("Eine Programmiersprache")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaIntro)
                .build();

        Quiz javaQuiz2 = Quiz.builder()
                .question("Welches Jahr wurde Java veröffentlicht?")
                .options(Arrays.asList(
                        "1990",
                        "1995",
                        "2000",
                        "2005"
                ))
                .correctAnswer("1995")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaIntro)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaIntro.addQuiz(javaQuiz1);
        javaIntro.addQuiz(javaQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(javaQuiz1);
        quizRepository.save(javaQuiz2);
        
        // Speichere Module
        moduleRepository.save(javaIntro);

        Module javaVariables = Module.builder()
                .title("Variablen und Datentypen")
                .content("In Java gibt es verschiedene Datentypen wie int, double, String, boolean. " +
                        "Variablen müssen vor der Verwendung deklariert werden. " +
                        "Beispiel: int zahl = 10; String text = \"Hallo\";")
                .topic(javaBasics)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaBasics.addModule(javaVariables);
        javaVariables = moduleRepository.save(javaVariables);
        topicRepository.save(javaBasics);

        Quiz variablesQuiz1 = Quiz.builder()
                .question("Welcher Datentyp wird für ganze Zahlen verwendet?")
                .options(Arrays.asList(
                        "double",
                        "int",
                        "String",
                        "boolean"
                ))
                .correctAnswer("int")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaVariables)
                .build();

        Quiz variablesQuiz2 = Quiz.builder()
                .question("Wie deklariert man eine Variable in Java?")
                .options(Arrays.asList(
                        "variable = 10;",
                        "int variable = 10;",
                        "var variable = 10;",
                        "let variable = 10;"
                ))
                .correctAnswer("int variable = 10;")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaVariables)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaVariables.addQuiz(variablesQuiz1);
        javaVariables.addQuiz(variablesQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(variablesQuiz1);
        quizRepository.save(variablesQuiz2);
        
        // Speichere Module
        moduleRepository.save(javaVariables);

        Module javaControlStructures = Module.builder()
                .title("Kontrollstrukturen")
                .content("Kontrollstrukturen in Java umfassen if-else Statements, Schleifen (for, while, do-while) " +
                        "und switch-Statements. Sie ermöglichen es, den Programmablauf zu steuern und " +
                        "bedingungenabhängige Ausführungen zu implementieren.")
                .topic(javaBasics)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaBasics.addModule(javaControlStructures);
        javaControlStructures = moduleRepository.save(javaControlStructures);
        topicRepository.save(javaBasics);

        Quiz controlQuiz1 = Quiz.builder()
                .question("Welche Schleifenart wird mindestens einmal ausgeführt?")
                .options(Arrays.asList(
                        "for-Schleife",
                        "while-Schleife",
                        "do-while-Schleife",
                        "if-Schleife"
                ))
                .correctAnswer("do-while-Schleife")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaControlStructures)
                .build();

        Quiz controlQuiz2 = Quiz.builder()
                .question("Welches Schlüsselwort wird für bedingte Ausführung verwendet?")
                .options(Arrays.asList(
                        "loop",
                        "if",
                        "switch",
                        "case"
                ))
                .correctAnswer("if")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaControlStructures)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaControlStructures.addQuiz(controlQuiz1);
        javaControlStructures.addQuiz(controlQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(controlQuiz1);
        quizRepository.save(controlQuiz2);
        
        // Speichere Module
        moduleRepository.save(javaControlStructures);

        Module javaMethods = Module.builder()
                .title("Methoden und Funktionen")
                .content("Methoden sind wiederverwendbare Codeblöcke in Java. Sie können Parameter " +
                        "entgegennehmen und Werte zurückgeben. Methoden helfen dabei, Code zu strukturieren " +
                        "und Redundanz zu vermeiden.")
                .topic(javaBasics)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaBasics.addModule(javaMethods);
        javaMethods = moduleRepository.save(javaMethods);
        topicRepository.save(javaBasics);

        Quiz methodsQuiz1 = Quiz.builder()
                .question("Was ist eine Methode in Java?")
                .options(Arrays.asList(
                        "Ein Datentyp",
                        "Ein wiederverwendbarer Codeblock",
                        "Eine Variable",
                        "Ein Kommentar"
                ))
                .correctAnswer("Ein wiederverwendbarer Codeblock")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaMethods)
                .build();

        Quiz methodsQuiz2 = Quiz.builder()
                .question("Welches Schlüsselwort definiert eine Methode?")
                .options(Arrays.asList(
                        "function",
                        "method",
                        "void",
                        "public"
                ))
                .correctAnswer("public")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(javaMethods)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        javaMethods.addQuiz(methodsQuiz1);
        javaMethods.addQuiz(methodsQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(methodsQuiz1);
        quizRepository.save(methodsQuiz2);
        
        // Speichere Module
        moduleRepository.save(javaMethods);

        // Topic 2: Spring Framework
        Topic springFramework = Topic.builder()
                .title("Spring Framework")
                .description("Lerne das Spring Framework für Java-Entwicklung")
                .category("Framework")
                .difficulty("Fortgeschritten")
                .build();

        springFramework = topicRepository.save(springFramework);

        // Module für Spring Framework
        Module springIntro = Module.builder()
                .title("Einführung in Spring")
                .content("Spring Framework ist ein beliebtes Java-Framework für die Entwicklung von " +
                        "Unternehmensanwendungen. Es bietet Dependency Injection, AOP und viele weitere Features.")
                .topic(springFramework)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springFramework.addModule(springIntro);
        springIntro = moduleRepository.save(springIntro);
        topicRepository.save(springFramework);

        Quiz springQuiz1 = Quiz.builder()
                .question("Was ist Spring Framework?")
                .options(Arrays.asList(
                        "Ein Java-Framework",
                        "Eine Datenbank",
                        "Ein Betriebssystem",
                        "Ein Webbrowser"
                ))
                .correctAnswer("Ein Java-Framework")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springIntro)
                .build();

        Quiz springQuiz2 = Quiz.builder()
                .question("Welches Feature bietet Spring für Dependency Management?")
                .options(Arrays.asList(
                        "Dependency Injection",
                        "Garbage Collection",
                        "Memory Management",
                        "Thread Management"
                ))
                .correctAnswer("Dependency Injection")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springIntro)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springIntro.addQuiz(springQuiz1);
        springIntro.addQuiz(springQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(springQuiz1);
        quizRepository.save(springQuiz2);
        
        // Speichere Module
        moduleRepository.save(springIntro);

        Module springBoot = Module.builder()
                .title("Spring Boot")
                .content("Spring Boot vereinfacht die Entwicklung von Spring-Anwendungen durch " +
                        "Auto-Konfiguration und eingebettete Server. Es reduziert Boilerplate-Code erheblich.")
                .topic(springFramework)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springFramework.addModule(springBoot);
        springBoot = moduleRepository.save(springBoot);
        topicRepository.save(springFramework);

        Quiz bootQuiz1 = Quiz.builder()
                .question("Was ist Spring Boot?")
                .options(Arrays.asList(
                        "Ein Auto-Konfigurations-Framework",
                        "Eine Datenbank",
                        "Ein Webserver",
                        "Ein Build-Tool"
                ))
                .correctAnswer("Ein Auto-Konfigurations-Framework")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springBoot)
                .build();

        Quiz bootQuiz2 = Quiz.builder()
                .question("Welchen Vorteil bietet Spring Boot?")
                .options(Arrays.asList(
                        "Mehr Boilerplate-Code",
                        "Weniger Boilerplate-Code",
                        "Langsamere Entwicklung",
                        "Komplexere Konfiguration"
                ))
                .correctAnswer("Weniger Boilerplate-Code")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springBoot)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springBoot.addQuiz(bootQuiz1);
        springBoot.addQuiz(bootQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(bootQuiz1);
        quizRepository.save(bootQuiz2);
        
        // Speichere Module
        moduleRepository.save(springBoot);

        Module springSecurity = Module.builder()
                .title("Spring Security")
                .content("Spring Security ist ein leistungsstarkes Framework für Authentifizierung und " +
                        "Autorisierung in Spring-Anwendungen. Es bietet umfassende Sicherheitsfunktionen " +
                        "wie Passwort-Verschlüsselung, JWT-Token und Rollen-basierte Zugriffskontrolle.")
                .topic(springFramework)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springFramework.addModule(springSecurity);
        springSecurity = moduleRepository.save(springSecurity);
        topicRepository.save(springFramework);

        Quiz securityQuiz1 = Quiz.builder()
                .question("Was ist Spring Security?")
                .options(Arrays.asList(
                        "Ein Build-Tool",
                        "Ein Sicherheits-Framework",
                        "Eine Datenbank",
                        "Ein Webserver"
                ))
                .correctAnswer("Ein Sicherheits-Framework")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springSecurity)
                .build();

        Quiz securityQuiz2 = Quiz.builder()
                .question("Welche Funktion bietet Spring Security?")
                .options(Arrays.asList(
                        "Datenbankverbindung",
                        "Authentifizierung und Autorisierung",
                        "Build-Management",
                        "Logging"
                ))
                .correctAnswer("Authentifizierung und Autorisierung")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springSecurity)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springSecurity.addQuiz(securityQuiz1);
        springSecurity.addQuiz(securityQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(securityQuiz1);
        quizRepository.save(securityQuiz2);
        
        // Speichere Module
        moduleRepository.save(springSecurity);

        Module springData = Module.builder()
                .title("Spring Data JPA")
                .content("Spring Data JPA vereinfacht die Datenbankzugriffe durch automatische " +
                        "Repository-Implementierungen. Es reduziert Boilerplate-Code für CRUD-Operationen " +
                        "und bietet erweiterte Abfragefunktionen.")
                .topic(springFramework)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springFramework.addModule(springData);
        springData = moduleRepository.save(springData);
        topicRepository.save(springFramework);

        Quiz dataQuiz1 = Quiz.builder()
                .question("Was ist Spring Data JPA?")
                .options(Arrays.asList(
                        "Ein Datenbank-System",
                        "Ein Repository-Framework",
                        "Ein Build-Tool",
                        "Ein Webserver"
                ))
                .correctAnswer("Ein Repository-Framework")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springData)
                .build();

        Quiz dataQuiz2 = Quiz.builder()
                .question("Welchen Vorteil bietet Spring Data JPA?")
                .options(Arrays.asList(
                        "Mehr Boilerplate-Code",
                        "Weniger Boilerplate-Code",
                        "Langsamere Abfragen",
                        "Komplexere Konfiguration"
                ))
                .correctAnswer("Weniger Boilerplate-Code")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(springData)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        springData.addQuiz(dataQuiz1);
        springData.addQuiz(dataQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(dataQuiz1);
        quizRepository.save(dataQuiz2);
        
        // Speichere Module
        moduleRepository.save(springData);

        // Topic 3: Datenbanken
        Topic databases = Topic.builder()
                .title("Datenbanken")
                .description("Grundlagen der Datenbankentwicklung und SQL")
                .category("Datenbank")
                .difficulty("Mittel")
                .build();

        databases = topicRepository.save(databases);

        // Module für Datenbanken
        Module sqlBasics = Module.builder()
                .title("SQL Grundlagen")
                .content("SQL (Structured Query Language) ist die Standardsprache für relationale Datenbanken. " +
                        "Mit SQL können Sie Daten abfragen, einfügen, aktualisieren und löschen.")
                .topic(databases)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        databases.addModule(sqlBasics);
        sqlBasics = moduleRepository.save(sqlBasics);
        topicRepository.save(databases);

        Quiz sqlQuiz1 = Quiz.builder()
                .question("Was bedeutet SQL?")
                .options(Arrays.asList(
                        "Structured Query Language",
                        "Simple Query Language",
                        "Standard Query Language",
                        "System Query Language"
                ))
                .correctAnswer("Structured Query Language")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(sqlBasics)
                .build();

        Quiz sqlQuiz2 = Quiz.builder()
                .question("Welches SQL-Kommando wird zum Abfragen von Daten verwendet?")
                .options(Arrays.asList(
                        "INSERT",
                        "SELECT",
                        "UPDATE",
                        "DELETE"
                ))
                .correctAnswer("SELECT")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(sqlBasics)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        sqlBasics.addQuiz(sqlQuiz1);
        sqlBasics.addQuiz(sqlQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(sqlQuiz1);
        quizRepository.save(sqlQuiz2);
        
        // Speichere Module
        moduleRepository.save(sqlBasics);

        Module jpa = Module.builder()
                .title("JPA und Hibernate")
                .content("JPA (Java Persistence API) ist eine Java-Spezifikation für ORM. " +
                        "Hibernate ist die beliebteste JPA-Implementierung und ermöglicht die " +
                        "objektorientierte Arbeit mit Datenbanken.")
                .topic(databases)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        databases.addModule(jpa);
        jpa = moduleRepository.save(jpa);
        topicRepository.save(databases);

        Quiz jpaQuiz1 = Quiz.builder()
                .question("Was bedeutet JPA?")
                .options(Arrays.asList(
                        "Java Persistence API",
                        "Java Programming Application",
                        "Java Platform Architecture",
                        "Java Performance Analysis"
                ))
                .correctAnswer("Java Persistence API")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(jpa)
                .build();

        Quiz jpaQuiz2 = Quiz.builder()
                .question("Was ist Hibernate?")
                .options(Arrays.asList(
                        "Eine JPA-Implementierung",
                        "Eine Datenbank",
                        "Ein Webserver",
                        "Ein Build-Tool"
                ))
                .correctAnswer("Eine JPA-Implementierung")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(jpa)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        jpa.addQuiz(jpaQuiz1);
        jpa.addQuiz(jpaQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(jpaQuiz1);
        quizRepository.save(jpaQuiz2);
        
        // Speichere Module
        moduleRepository.save(jpa);

        Module databaseDesign = Module.builder()
                .title("Datenbankdesign")
                .content("Datenbankdesign umfasst die Planung und Strukturierung von Datenbanken. " +
                        "Wichtige Konzepte sind Normalisierung, Primär- und Fremdschlüssel, sowie " +
                        "die Beziehungen zwischen Tabellen (1:1, 1:N, N:M).")
                .topic(databases)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        databases.addModule(databaseDesign);
        databaseDesign = moduleRepository.save(databaseDesign);
        topicRepository.save(databases);

        Quiz designQuiz1 = Quiz.builder()
                .question("Was ist Normalisierung in der Datenbank?")
                .options(Arrays.asList(
                        "Ein Prozess zur Optimierung der Datenbankstruktur",
                        "Ein Backup-Verfahren",
                        "Ein Verschlüsselungsverfahren",
                        "Ein Indexierungsverfahren"
                ))
                .correctAnswer("Ein Prozess zur Optimierung der Datenbankstruktur")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(databaseDesign)
                .build();

        Quiz designQuiz2 = Quiz.builder()
                .question("Welche Beziehung existiert zwischen Kunde und Bestellung?")
                .options(Arrays.asList(
                        "1:1 Beziehung",
                        "1:N Beziehung",
                        "N:M Beziehung",
                        "Keine Beziehung"
                ))
                .correctAnswer("1:N Beziehung")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(databaseDesign)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        databaseDesign.addQuiz(designQuiz1);
        databaseDesign.addQuiz(designQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(designQuiz1);
        quizRepository.save(designQuiz2);
        
        // Speichere Module
        moduleRepository.save(databaseDesign);

        Module databaseTransactions = Module.builder()
                .title("Transaktionen und ACID")
                .content("Transaktionen sind logische Einheiten der Datenbankarbeit. ACID steht für " +
                        "Atomicity, Consistency, Isolation und Durability. Diese Eigenschaften " +
                        "gewährleisten die Datenintegrität und Zuverlässigkeit.")
                .topic(databases)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        databases.addModule(databaseTransactions);
        databaseTransactions = moduleRepository.save(databaseTransactions);
        topicRepository.save(databases);

        Quiz transactionQuiz1 = Quiz.builder()
                .question("Was bedeutet ACID in Datenbanken?")
                .options(Arrays.asList(
                        "Atomicity, Consistency, Isolation, Durability",
                        "Access, Control, Input, Data",
                        "Application, Connection, Interface, Database",
                        "Analysis, Configuration, Integration, Development"
                ))
                .correctAnswer("Atomicity, Consistency, Isolation, Durability")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(databaseTransactions)
                .build();

        Quiz transactionQuiz2 = Quiz.builder()
                .question("Was bedeutet Atomicity in ACID?")
                .options(Arrays.asList(
                        "Alle oder keine Operationen werden ausgeführt",
                        "Daten bleiben konsistent",
                        "Transaktionen sind isoliert",
                        "Daten sind dauerhaft gespeichert"
                ))
                .correctAnswer("Alle oder keine Operationen werden ausgeführt")
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .module(databaseTransactions)
                .build();

        // Verwende die Helper-Methode für korrekte bidirektionale Beziehung
        databaseTransactions.addQuiz(transactionQuiz1);
        databaseTransactions.addQuiz(transactionQuiz2);
        
        // Speichere Quizzes explizit
        quizRepository.save(transactionQuiz1);
        quizRepository.save(transactionQuiz2);
        
        // Speichere Module
        moduleRepository.save(databaseTransactions);

        System.out.println("Sample-Daten erfolgreich erstellt!");
        System.out.println("Erstellte Topics: " + topicRepository.count());
        System.out.println("Erstellte Module: " + moduleRepository.count());
        System.out.println("Erstellte Quizzes: " + quizRepository.count());
    }

    private void initializeRoles() {
        // Prüfe ob bereits Roles vorhanden sind
        if (roleRepository.count() > 0) {
            System.out.println("Roles bereits vorhanden, überspringe Role-Initialisierung");
            return;
        }

        System.out.println("Initialisiere grundlegende Roles...");

        // Erstelle grundlegende Roles
        Role adminRole = Role.builder()
                .name("ADMIN")
                .build();

        Role userRole = Role.builder()
                .name("USER")
                .build();

        Role moderatorRole = Role.builder()
                .name("MODERATOR")
                .build();

        roleRepository.saveAll(Arrays.asList(adminRole, userRole, moderatorRole));

        System.out.println("Grundlegende Roles erstellt: ADMIN, USER, MODERATOR");
    }

    private void initializeAdminAccount() {
        // Prüfe ob bereits Admin-Account vorhanden ist
        if (userRepository.findByUsername("admin") != null) {
            System.out.println("Admin-Account bereits vorhanden, überspringe Admin-Initialisierung");
            return;
        }

        System.out.println("Initialisiere Admin-Account...");

        // Erstelle RegisterRequest für Admin-Account
        RegisterRequest adminRequest = RegisterRequest.builder()
                .firstname("Max")
                .lastname("Mustermann")
                .email("admin@evasio.com")
                .password("password")
                .username("admin")
                .roles(Set.of("ADMIN"))
                .build();

        try {
            // Verwende AuthenticationService für korrekte Stripe-Integration
            authenticationService.register(adminRequest);
            
            System.out.println("Admin-Account erstellt:");
            System.out.println("Username: admin");
            System.out.println("Password: password");
            System.out.println("Name: Max Mustermann");
            System.out.println("Email: admin@evasio.com");
            System.out.println("Role: ADMIN");
            System.out.println("Stripe Customer wurde automatisch erstellt");
        } catch (Exception e) {
            System.err.println("Fehler beim Erstellen des Admin-Accounts: " + e.getMessage());
        }
    }
} 