package pl.marchwicki.vertx.vanilla;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class Deployment extends Verticle {

    final static Integer PORT = 8080;

    public void start() {

        container.deployVerticle(StoreRepositoryVerticle.class.getName());
        container.deployVerticle(TodoMVCVerticle.class.getName(),
                new JsonObject().putNumber("port", PORT));
    }
}
