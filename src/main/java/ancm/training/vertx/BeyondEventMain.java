package ancm.training.vertx;


import ancm.training.vertx.beyondEventBus.DataVerticle;
import ancm.training.vertx.beyondEventBus.HeatSensorVerticle;
import ancm.training.vertx.beyondEventBus.SensorDataService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class BeyondEventMain {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(HeatSensorVerticle.class, new DeploymentOptions().setInstances(4));
    vertx.deployVerticle(new DataVerticle(), id -> {
      SensorDataService service = SensorDataService.createProxy(vertx, "sensor.data-service");
      vertx.setTimer(5000, s -> {
        service.average(ar -> {
          if (ar.succeeded()) {
            System.out.println("Average = " + ar.result());
          } else {
            ar.cause().printStackTrace();
          }
        });
      });

    });

  }

}
