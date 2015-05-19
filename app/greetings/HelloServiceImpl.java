package greetings;

import javax.inject.Singleton;

@Singleton
final class HelloServiceImpl implements HelloService {
    private final String greeting;

    public HelloServiceImpl(final String greeting) {
        this.greeting = greeting;
    }

    @Override
    public String getGreeting() {
        return greeting;
    }
}
