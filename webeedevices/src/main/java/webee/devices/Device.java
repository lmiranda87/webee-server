package webee.devices;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/device")
public class Device {
    @GET
    @Path("/{param}")
    public Response getDeviceById(@PathParam("param") String msg) {
        String output = "Getting device : " + msg;
        return Response.status(200).entity(output).build();
    }

    @POST
    @Path("/{param}")
    public Response createDevice(@PathParam("param") String msg) {
        String output = "Creating Device : " + msg;
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/all")
    public Response getAllDevices(@PathParam("param") String msg) {
        String output = "Getting all devices : " + msg;
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/mac/{param}")
    public Response getDeviceByMac(@PathParam("param") String msg) {
        String output = "Getting all devices : " + msg;
        return Response.status(200).entity(output).build();
    }
}
