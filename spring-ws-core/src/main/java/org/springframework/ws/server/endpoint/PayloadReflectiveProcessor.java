package org.springframework.ws.server.endpoint;

import org.springframework.aot.hint.BindingReflectionHintsRegistrar;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.annotation.ReflectiveProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * TODO Fix docu based on HttpExchangeReflectiveProcessor and ControllerMappingReflectiveProcessor
 * {@link ReflectiveProcessor} implementation for {@link Controller} and
 * controller-specific annotated methods. In addition to registering reflection
 * hints for invoking the annotated method, this implementation handles:
 *
 * <ul>
 *     <li>Return types annotated with {@link ResponseBody}</li>
 *     <li>Parameters annotated with {@link RequestBody}, {@link ModelAttribute} and {@link RequestPart}</li>
 *     <li>{@link HttpEntity} return types and parameters</li>
 * </ul>
 *
 * @author Stephane Nicoll
 * @author Sebastien Deleuze
 * @since 6.0
 */
public class PayloadReflectiveProcessor implements ReflectiveProcessor {
    private final BindingReflectionHintsRegistrar bindingRegistrar = new BindingReflectionHintsRegistrar();


    @Override
    public void registerReflectionHints(ReflectionHints hints, AnnotatedElement element) {
        if (element instanceof Method method) {
            registerMethodHints(hints, method);
        }
    }

    protected void registerMethodHints(ReflectionHints hints, Method method) {
        hints.registerMethod(method, ExecutableMode.INVOKE);
        for (Parameter parameter : method.getParameters()) {
            registerParameterTypeHints(hints, MethodParameter.forParameter(parameter));
        }
        registerReturnTypeHints(hints, MethodParameter.forExecutable(method, -1));
    }

    protected void registerParameterTypeHints(ReflectionHints hints, MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(RequestPayload.class)) {
            this.bindingRegistrar.registerReflectionHints(hints, methodParameter.getGenericParameterType());
        }
    }

    protected void registerReturnTypeHints(ReflectionHints hints, MethodParameter returnTypeParameter) {
        if (returnTypeParameter.hasMethodAnnotation(ResponsePayload.class)) {
            this.bindingRegistrar.registerReflectionHints(hints, returnTypeParameter.getGenericParameterType());
        }
    }
}
