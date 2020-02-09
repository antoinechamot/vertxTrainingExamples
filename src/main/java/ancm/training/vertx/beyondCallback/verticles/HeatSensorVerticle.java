package ancm.training.vertx.beyondCallback.verticles;

import java.util.Random;
import java.util.UUID;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public class HeatSensorVerticle extends AbstractVerticle {
  private final Random random = new Random();
  private final String id = UUID.randomUUID().toString();
  private double temp = 21.0;


  public void start() {
    vertx.createHttpServer().requestHandler(this::handleRequest)
        .listen(config().getInteger("http.port", 3000));
    scheduleNextUpdate();
  }

  private void handleRequest(HttpServerRequest req) {
    JsonObject data = new JsonObject().put("id", id).put("temp", temp);
    req.response().putHeader("Content-type", "application/json").end(data.encode());
  }

  private void scheduleNextUpdate() {
    vertx.setTimer(random.nextInt(5000) + 1000, this::update);
  }

  private void update(long tid) {
    temp = temp + (delta() / 10);

    JsonObject payload = new JsonObject().put("id", id).put("temp", temp);
    vertx.eventBus().publish("sensor.updates", payload);
    scheduleNextUpdate();
  }

  private double delta() {
    if (random.nextInt() > 0) {
      return random.nextGaussian();
    } else {
      return -random.nextGaussian();
    }
  }
}
