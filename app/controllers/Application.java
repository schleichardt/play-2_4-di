package controllers;

import module1.WhateverService;
import play.*;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Application extends Controller {

    @Inject
    public Application(final WhateverService service) {
    }

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
