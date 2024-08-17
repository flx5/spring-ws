package org.springframework.ws.wsdl.wsdl11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultWsdl11DefinitionRuntimeHintsTest {

    private DefaultWsdl11Definition.RuntimeHints runtimeHints;

    @BeforeEach
    public void setUp() throws Exception {
        runtimeHints = new DefaultWsdl11Definition.RuntimeHints();
    }

    @Test
    void test() {
        RuntimeHints hints = new RuntimeHints();
        runtimeHints.registerHints(hints, getClass().getClassLoader());

        assertThat(RuntimeHintsPredicates.reflection()
                .onType(com.ibm.wsdl.extensions.schema.SchemaImpl.class)
                .withMemberCategory(MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS))
                .accepts(hints);

        /* TODO https://docs.spring.io/spring-framework/reference/core/aot.html#aot.hints.testing
        // Invoke the relevant piece of code we want to test within a recording lambda
        RuntimeHintsInvocations invocations = RuntimeHintsRecorder.record(() -> {
            SampleReflection sample = new SampleReflection();
            sample.performReflection();
        });
        // assert that the recorded invocations are covered by the contributed hints
        assertThat(invocations).match(runtimeHints); */
    }
}
