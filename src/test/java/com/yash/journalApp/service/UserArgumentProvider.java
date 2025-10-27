package com.yash.journalApp.service;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import com.yash.journalApp.entity.User;

import java.util.stream.Stream;

public class UserArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(User.builder().userName("sonu").password("shyam").build()),
                Arguments.of(User.builder().userName("monu").password("").build())
        );
    }
}
