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

        return seq(bind(SphereClient.class).to(new SphereClientProvider(configuration)));
    }

    private static final class SphereClientProvider implements Provider<SphereClient> {
        private Configuration configuration;

        //here we need field injection, the constructor needs to be called explicitly
        @Inject
        private ApplicationLifecycle applicationLifecycle;

        public SphereClientProvider(final Configuration configuration) {
            this.configuration = configuration;
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
