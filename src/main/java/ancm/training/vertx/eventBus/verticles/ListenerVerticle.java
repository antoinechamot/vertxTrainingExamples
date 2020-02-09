package ancm.training.vertx.eventBus.verticles;

import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class ListenerVerticle extends AbstractVerticle {

  String name;

  private final Logger logger = LoggerFactory.getLogger(ListenerVerticle.class);
  private final DecimalFormat format = new DecimalFormat("#.##");

  public void start() {
    vertx.eventBus().<JsonObject>consumer("sensor.updates", msg -> {
      JsonObject body = msg.body();
      String id = body.getString("id");
      String temp = format.format(body.getDouble("temp"));
      logger.info("{} reports a temperature of {}C", id, temp);
    });
  }


}
