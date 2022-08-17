package com.server.db.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Access
@Documented
public @interface Confirmation {
    String value();
}
