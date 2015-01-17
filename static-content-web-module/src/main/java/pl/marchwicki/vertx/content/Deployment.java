package pl.marchwicki.vertx.content;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class Deployment extends Verticle {

    public void start() {
        container.deployWorkerVerticle(StaticFilesVerticle.class.getName(), new JsonObject(), 5, true, (AsyncResult<String> result) -> {
            container.logger().info("StaticFilesVerticle deployed? " + (result.succeeded() ? "ok" : result.result()));
        });
    }
}
