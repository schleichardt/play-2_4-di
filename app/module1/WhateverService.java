package module1;

import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WhateverService {
    @Inject
    public WhateverService(final ApplicationLifecycle lifecycle) {
        System.err.println(WhateverService.this + " starts now.");

        lifecycle.addStopHook(() -> {

            System.err.println(WhateverService.this + " stops now.");

            return F.Promise.pure(null);
        });
    }
}
