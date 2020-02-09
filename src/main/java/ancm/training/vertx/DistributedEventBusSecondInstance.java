package ancm.training.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ancm.training.vertx.eventBus.verticles.HeatSensorVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class DistributedEventBusSecondInstance {

  String name;

  private static final Logger logger =
      LoggerFactory.getLogger(DistributedEventBusSecondInstance.class);


  public static void main(String[] args) {
    Vertx.clusteredVertx(new VertxOptions(), ar -> {
      if (ar.succeeded()) {
        logger.info("Second instance has been started");
        Vertx vertx = ar.result();
        vertx.deployVerticle(HeatSensorVerticle.class, new DeploymentOptions().setInstances(4));
        vertx.deployVerticle("ancm.training.vertx.eventBus.verticles.ListenerVerticle");
        vertx.deployVerticle("ancm.training.vertx.eventBus.verticles.SensorDataVerticle");
        JsonObject conf = new JsonObject().put("port", 8081);
        vertx.deployVerticle("ancm.training.vertx.eventBus.verticles.HttpServerVerticle",
            new DeploymentOptions().setConfig(conf));


      } else {
        logger.error("Could not start", ar.cause());
      }
    });
  }

}
