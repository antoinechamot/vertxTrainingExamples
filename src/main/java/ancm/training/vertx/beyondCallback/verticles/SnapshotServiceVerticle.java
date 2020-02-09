package ancm.training.vertx.beyondCallback.verticles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;

public class SnapshotServiceVerticle extends AbstractVerticle {

  String name;

  private final Logger logger = LoggerFactory.getLogger(SnapshotServiceVerticle.class);


  public void start() {
    vertx.createHttpServer().requestHandler(req -> {
      if (badRequest(req)) {
        req.response().setStatusCode(400).end();
      } else {
        req.bodyHandler(buffer -> {
          logger.info("Latest temperature : {}", buffer.toJsonObject().encodePrettily());
          req.response().end();
        });
      }
    }).listen(config().getInteger("http.port", 4000));
  }


  private boolean badRequest(HttpServerRequest request) {
    return !request.method().equals(HttpMethod.POST)
        || !"application/json".equals(request.getHeader("Content-type"));
  }

}
