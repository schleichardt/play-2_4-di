package module1;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import play.Logger;
import play.api.*;
import play.api.inject.*;
import play.libs.F;
import scala.collection.Seq;

import javax.inject.*;
import java.util.concurrent.CompletableFuture;

//provides bindings and needs to be registered!
public class SphereModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(final Environment environment, final Configuration configuration) {
        //here is access possible to fetch the credentials with configuration

        return seq(bind(SphereClient.class).toProvider(SphereClientProvider.class));
    }

    private static final class SphereClientProvider implements Provider<SphereClient> {

        private final Configuration configuration;
        private final ApplicationLifecycle applicationLifecycle;

        @Inject
        public SphereClientProvider(final Configuration configuration, final ApplicationLifecycle applicationLifecycle) {
            this.configuration = configuration;
            this.applicationLifecycle = applicationLifecycle;
        }

        @Override
        public SphereClient get() {
            Logger.info("create sphere client");

            final SphereClient sphereClient = createClient();

            applicationLifecycle.addStopHook(() ->
                F.Promise.promise(() -> {
                    sphereClient.close();
                    return null;//Promise is of type F.Promise<Void>
                })
            );

            return sphereClient;
        }

        private SphereClient createClient() {
            return new SphereClient() {
                        @Override
                        public <T> CompletableFuture<T> execute(final SphereRequest<T> sphereRequest) {
                            final CompletableFuture<T> future = new CompletableFuture<>();
                            future.completeExceptionally(new UnsupportedOperationException("just a demo"));
                            return future;
                        }

                        @Override
                        public void close() {
                            Logger.info("shutdown sphere client");
                        }
                    };
        }
    }
}
