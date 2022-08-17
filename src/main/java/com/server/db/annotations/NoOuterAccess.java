package com.server.db.annotations;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Access
@Documented
@Tag(name = "no access")
public @interface NoOuterAccess {}
