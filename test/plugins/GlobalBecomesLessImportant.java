package plugins;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import greetings.HelloService;
import greetings.HelloServiceImpl;
import play.Application;
import play.Configuration;
import play.Mode;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.collection.Seq;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.File;
import java.lang.reflect.Method;
import static play.inject.Bindings.*;
import static play.inject.Bindings.bind;

public class GlobalBecomesLessImportant {
    public abstract class GlobalSettingsLikeInPlay23 {

        /*
            don't use it, use dependency injection instead
         */
        public abstract void beforeStart(Application app);
        public abstract void onStart(Application app);

        private class DemoController extends Controller {
            private final HelloService helloService;

            @Inject
            public DemoController(final HelloService helloService) {
                this.helloService = helloService;
            }

            public Result hello() {
                return ok(helloService.getGreeting());
            }
        }

        //examples for lazy binding
        private class LazyBindingModuleExample extends Module {
            @Override
            public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
                return seq(
                        bind(HelloService.class).to(HelloServiceImpl.class)
                );
            }
        }

        private class LazyBindingGuiceModule extends AbstractModule {
            @Override
            protected void configure() {
                bind(HelloService.class).to(HelloServiceImpl.class);
            }
        }

        //examples for eager binding
        private class EagerBindingModuleExample extends Module {
            @Override
            public Seq<Binding<?>> bindings(final Environment environment, final play.api.Configuration configuration) {
                return seq(
                        bind(HelloService.class).to(HelloServiceImpl.class).eagerly(),
                        //alternative: instances are also eager, only use one of the examples here!
                        bind(HelloService.class).toInstance(new HelloServiceImpl())
                );
            }
        }

        private class EagerBindingGuiceModule extends AbstractModule {
            @Override
            protected void configure() {
                bind(HelloService.class).to(HelloServiceImpl.class).asEagerSingleton();
                //alternative: instances are also eager, only use one of the examples here!
                bind(HelloService.class).toInstance(new HelloServiceImpl());
            }
        }

        //don't use it, use dependency injection and add a shutdown hook
        public abstract void onStop(Application app);

        //example from https://www.playframework.com/documentation/2.4.x/JavaDependencyInjection
        //this has flaws
        // - it only works with classes you posses
        // - you need an import for a Play class, so the class is bound to play
        // - it returns a promise, but is still a blocking operation
        @Singleton
        public class MessageQueueConnection {
            private final MessageQueue connection;

            @Inject
            public MessageQueueConnection(ApplicationLifecycle lifecycle) {
                connection = MessageQueue.connect();

                lifecycle.addStopHook(() -> {
                    connection.stop();
                    return F.Promise.pure(null);
                });
            }

            // ...
        }

        //suggestion, use a provider instead
        @Singleton
        public class MessageQueueProvider implements Provider<MessageQueue> {
            private final ApplicationLifecycle lifecycle;

            public MessageQueueProvider(final ApplicationLifecycle lifecycle) {
                this.lifecycle = lifecycle;
            }

            @Override
            public MessageQueue get() {
                final MessageQueue messageQueue = MessageQueue.connect();

                lifecycle.addStopHook(() -> F.Promise.promise(() -> {
                    messageQueue.stop();
                    return null;
                }));

                return messageQueue;
            }
        }

        public abstract F.Promise<Result> onError(Http.RequestHeader request, Throwable t);

        public abstract Action onRequest(Http.Request request, Method actionMethod);

        public abstract play.api.mvc.Handler onRouteRequest(Http.RequestHeader request);

        public abstract F.Promise<Result> onHandlerNotFound(Http.RequestHeader request);

        public abstract F.Promise<Result> onBadRequest(Http.RequestHeader request, String error);

        public abstract <A> A getControllerInstance(Class<A> controllerClass) throws Exception;

        public abstract Configuration onLoadConfig(Configuration config, File path, ClassLoader classloader);

        public abstract Configuration onLoadConfig(Configuration config, File path, ClassLoader classloader, Mode mode);

        public abstract <T extends play.api.mvc.EssentialFilter> Class<T>[] filters();
    }


}
