package dev.vrailas.qrbill.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.assertj.core.api.Assertions.assertThat;

/** Executable architecture: the hexagon's dependency rule is enforced on every build. */
class ArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("dev.vrailas.qrbill");
        assertThat(classes.size()).as("classes imported").isGreaterThan(10);
    }

    @Test
    void domainDependsOnNothingElse() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage("..application..", "..adapter..", "..config..",
                        "org.springframework..", "com.google.zxing..", "jakarta..")
                .check(classes);
    }

    @Test
    void applicationIsFrameworkFree() {
        noClasses().that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAnyPackage("..adapter..", "..config..",
                        "org.springframework..", "com.google.zxing..", "jakarta..")
                .check(classes);
    }

    @Test
    void inboundAdaptersDoNotDependOnOutboundAdapters() {
        noClasses().that().resideInAPackage("..adapter.in..")
                .should().dependOnClassesThat().resideInAPackage("..adapter.out..")
                .check(classes);
    }
}
