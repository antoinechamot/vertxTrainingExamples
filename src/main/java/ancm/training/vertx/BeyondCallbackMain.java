package ancm.training.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class BeyondCallbackMain {


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle("ancm.training.vertx.beyondCallback.verticles.HeatSensorVerticle",
        new DeploymentOptions().setConfig(new JsonObject().put("http.port", 3000)));
    vertx.deployVerticle("ancm.training.vertx.beyondCallback.verticles.HeatSensorVerticle",
        new DeploymentOptions().setConfig(new JsonObject().put("http.port", 3001)));
    vertx.deployVerticle("ancm.training.vertx.beyondCallback.verticles.HeatSensorVerticle",
        new DeploymentOptions().setConfig(new JsonObject().put("http.port", 3002)));

    vertx.deployVerticle("ancm.training.vertx.beyondCallback.verticles.SnapshotServiceVerticle");
    vertx.deployVerticle("ancm.training.vertx.beyondCallback.verticles.CollectorServiceVerticle");


  }
}
