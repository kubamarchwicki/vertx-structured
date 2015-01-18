package pl.marchwicki.vertx.vanilla;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class Deployment extends Verticle {

    final static Integer PORT = 8080;

    public void start() {

        container.deployModule("io.vertx~mod-web-server~2.0.0-final",
                new JsonObject().putNumber("port", PORT));

    }
}
