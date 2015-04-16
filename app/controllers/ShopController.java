package controllers;

import io.sphere.sdk.client.SphereClient;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShopController extends Controller {

    private final SphereClient client;

    @Inject
    public ShopController(final SphereClient client) {
        this.client = client;
    }

    public Result index() {
        return ok("whatever");
    }
}
