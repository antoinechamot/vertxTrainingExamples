package ancm.training.vertx.eventBus.verticles;

import java.util.Random;
import java.util.UUID;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class HeatSensorVerticle extends AbstractVerticle {

  private final Random random = new Random();
  private final String id = UUID.randomUUID().toString();
  private double temp = 21.0;


  public void start() {
    scheduleNextUpdate();
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
