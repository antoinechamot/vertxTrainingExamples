package ancm.training.vertx;

import ancm.training.vertx.eventBus.verticles.HeatSensorVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class LocalEventBusMain {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(HeatSensorVerticle.class, new DeploymentOptions().setInstances(4));
    vertx.deployVerticle("ancm.training.vertx.eventBus.verticles.ListenerVerticle");
    vertx.deployVerticle("ancm.training.vertx.eventBus.verticles.SensorDataVerticle");
    vertx.deployVerticle("ancm.training.vertx.eventBus.verticles.HttpServerVerticle");
  }

}
