package module1;

import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WhateverService {

    @Inject//important!
    public WhateverService(final play.inject.ApplicationLifecycle lifecycle) {
        System.err.println(WhateverService.this + " starts now.");

        lifecycle.addStopHook(() -> {

            System.err.println(WhateverService.this + " stops now.");

            return F.Promise.pure(null);
        });
    }
}
