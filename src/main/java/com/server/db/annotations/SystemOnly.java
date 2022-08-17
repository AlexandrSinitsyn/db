package com.server.db.annotations;

import com.server.db.Tools;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Access
@Documented
// @Hidden
@Tag(name = Tools.SYSTEM_PARAMETER)
public @interface SystemOnly {}
