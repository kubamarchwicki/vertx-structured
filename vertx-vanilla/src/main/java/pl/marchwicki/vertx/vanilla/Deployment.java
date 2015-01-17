package pl.marchwicki.vertx.vanilla;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.ReplyException;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

public class Deployment extends Verticle {

    final static Integer PORT = 8080;

    public void start() {

        container.deployModule("pl.marchwicki.vertx~static-content-web-module~1.0-SNAPSHOT");

        RouteMatcher matcher = new RouteMatcher();
        matcher.getWithRegEx(".*", (req) -> {
            String path;
            if (req.path().equals("/")) {
                path = "/index.html";
            } else {
                path = req.path();
            }

            vertx.fileSystem().exists("web/" + path, (AsyncResult<Boolean> exists) -> {
                if (exists.result()) {
                    req.response().sendFile("web/" + path);
                } else {
                    container.logger().info("Static file for path " + path);

                    vertx.eventBus().sendWithTimeout("staticFiles", path, 3000, (AsyncResult<Message<String>> event) -> {
                        if (event.failed()) {
                            ReplyException ex = (ReplyException) event.cause();
                            req.response().setStatusCode(404).end(ex.failureType().toString());
                            return;
                        }

                        req.response().end(event.result().body());
                    });
                }
            });
        });

        vertx.createHttpServer().requestHandler(matcher).listen(PORT);
    }
}
