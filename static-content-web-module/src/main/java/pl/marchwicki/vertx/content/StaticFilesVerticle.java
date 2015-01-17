package pl.marchwicki.vertx.content;

import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

import java.io.InputStream;
import java.util.Scanner;

public class StaticFilesVerticle extends Verticle {

    final static String HANDLER_NAME = "staticFiles";

    public void start() {

        //AsyncInputStream.java
        //https://gist.github.com/cescoffier/cc0275058cc5bceb4e46

        //InputStream is inherently blocking.
        //If you want to write from a file to a http request use the Vert.x file system api to
        // open the file asynchronously and pump it to the request.

        vertx.eventBus().registerHandler(HANDLER_NAME, (Message<String> event) -> {
            String path = event.body();

            InputStream stream = StaticFilesVerticle.class.getResourceAsStream("/META-INF/resources" + path);
            if (stream != null) {
                container.logger().info("File " + path + " found");

                String output = new Scanner(stream, "UTF-8").useDelimiter("\\A").next();
                event.reply(output);
            } else {
                event.fail(-1, "File not found");
            }
        });

    }

}
