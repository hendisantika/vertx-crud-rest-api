package id.my.hendisantika.vertx_crud_rest_api;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends VerticleBase {
  private static final String SERVICE_ADDRESS = "crud.service";

  @Override
  public Future<?> start() {
    Router router = Router.router(vertx);

    // Body handler for parsing request bodies
    router.route().handler(BodyHandler.create());

    // GET endpoint for reading all items
    router.get("/items").handler(rc ->
      vertx.eventBus().<JsonArray>request(SERVICE_ADDRESS, new JsonObject())
        .onComplete(reply -> respond(rc, reply)));

    // POST endpoint for creating new items
    router.post("/items").handler(rc -> {
      JsonObject item = rc.body().asJsonObject();
      vertx.eventBus().<JsonObject>request(SERVICE_ADDRESS, item)
        .onComplete(reply -> respond(rc, reply));
    });

    // PUT endpoint for updating items
    router.put("/items/:id").handler(rc -> {
      String id = rc.pathParam("id");
      JsonObject body = rc.body().asJsonObject();
      JsonObject item = (body == null ? new JsonObject() : body).put("id", id);
      vertx.eventBus().<JsonObject>request(SERVICE_ADDRESS, item)
        .onComplete(reply -> respond(rc, reply));
    });

    // DELETE endpoint for deleting items
    router.delete("/items/:id").handler(rc -> {
      String id = rc.pathParam("id");
      JsonObject message = new JsonObject().put("action", "delete").put("id", id);
      vertx.eventBus().<Void>request(SERVICE_ADDRESS, message)
        .onComplete(reply -> {
          if (reply.succeeded()) {
            rc.response().end();
          } else {
            rc.fail(500, reply.cause());
          }
        });
    });

    return vertx.createHttpServer().requestHandler(router).listen(8080);
  }

  private static void respond(RoutingContext rc, AsyncResult<? extends Message<?>> reply) {
    if (reply.succeeded()) {
      rc.response()
        .putHeader("Content-Type", "application/json")
        .end(String.valueOf(reply.result().body()));
    } else {
      rc.fail(500, reply.cause());
    }
  }
}
