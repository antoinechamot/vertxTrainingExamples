package ancm.training.vertx.beyondCallback.verticles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;

public class CollectorServiceVerticle extends AbstractVerticle {

  private final Logger logger = LoggerFactory.getLogger(CollectorServiceVerticle.class);
  private WebClient webClient;

  public void start() {
    webClient = WebClient.create(vertx);

    vertx.createHttpServer().requestHandler(this::handleRequest).listen(8080);
  }


  public void handleRequest(HttpServerRequest request) {
    List<JsonObject> responses = new ArrayList<>();

    AtomicInteger counter = new AtomicInteger();
    for (int i = 0; i < 3; i++) {
      webClient.get(3000 + i, "localhost", "/")
          // throw error if answer not in 2xxx range
          .expect(ResponsePredicate.SC_SUCCESS)
          // convert automatically to jsonObject
          .as(BodyCodec.jsonObject()).send(ar -> {
            if (ar.succeeded()) {
              responses.add(ar.result().body());
            } else {
              logger.error("Sensor down ?", ar.cause());
            }
            if (counter.incrementAndGet() == 3) {
              JsonObject data = new JsonObject().put("data", new JsonArray(responses));
              sendToSnapshot(request, data);
            }
          });
    }
  }

  private void sendToSnapshot(HttpServerRequest request, JsonObject data) {
    webClient.post(4000, "localhost", "/").expect(ResponsePredicate.SC_SUCCESS).sendJsonObject(data,
        ar -> {
          if (ar.succeeded()) {
            sendResponse(request, data);
          } else {
            logger.error("Snapshot down?", ar.cause());
            request.response().setStatusCode(500).end();
          }
        });
  }

  private void sendResponse(HttpServerRequest request, JsonObject data) {
    request.response().putHeader("Content-type", "application/json").end(data.encode());
  }

}
