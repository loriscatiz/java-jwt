package com.loriscatiz.exception;

import com.fasterxml.jackson.core.JacksonException;
import com.loriscatiz.exception.auth.ForbiddenException;
import com.loriscatiz.exception.auth.UnauthorizedException;
import com.loriscatiz.exception.badrequest.BadRequestException;
import com.loriscatiz.exception.server.ServerErrorException;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final Javalin app;

    public GlobalExceptionHandler(Javalin app) {
        this.app = app;
    }

    public void register(){
        app.exception(JacksonException.class, (e, ctx) -> {
            log.error("e: ", e);
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of("error", "malformed json"));
        });

        app.exception(BadRequestException.class, (e, ctx) -> {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of("error", e.getMessage()));
        });

        app.exception(ServerErrorException.class, (e, ctx) -> {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            log.error("e: ", e);
            ctx.json(Map.of("error", "server error"));
        });

        app.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(HttpStatus.UNAUTHORIZED);
            ctx.json(Map.of("error", e.getMessage()));
        });

        app.exception(ForbiddenException.class, (e, ctx) -> {
            ctx.status(HttpStatus.FORBIDDEN);
            ctx.json(Map.of("error", e.getMessage()));
        });

    }
}
