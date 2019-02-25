package webee.devices;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.json.simple.parser.ParseException;
import webee.devices.dao.DeviceDao;
import webee.devices.dao.Device;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

@Path("/device")
public class DeviceServlet {

    private static final String DATE = "2018-01-01 00:00:00.000";

    @GET
    @Path("/{param}")
    public Response getDeviceById(@PathParam("param") String id) {
        String output = null;
        DeviceDao deviceDao = null;
        try {
            deviceDao = new DeviceDao();
        } catch (IOException e) {
            output = "Device not found";
            return Response.status(404).entity(output).build();
        }
        Device dev = deviceDao.readDevice(Long.valueOf(id));
        if (dev != null) {
            JSONObject json = new JSONObject();
            json.put("id", dev.getId());
            json.put("mac", dev.getMac());
            json.put("timestamp", dev.getTimestamp());
            output = json.toString();
        } else {
            return Response.status(404).entity("No record").build();
        }

        return Response.status(200).entity(output).build();
    }

    @POST
    public Response createDevice(String req) {
        String output = null;
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        SimpleDateFormat dateFormat = null;
        Date parsedDate = null;
        Timestamp tstamp = null;
        Timestamp tstamp_req = null;
        DeviceDao deviceDao = null;

        try {

            json = (JSONObject) parser.parse(req.toString());
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            parsedDate = dateFormat.parse(this.DATE);
            tstamp = new java.sql.Timestamp(parsedDate.getTime());
            Long val = Long.decode(json.get("timestamp").toString());
            System.out.println("Current Long value is:" + val.toString());
            tstamp_req = new java.sql.Timestamp(val*1000);
            deviceDao = new DeviceDao();

        } catch (java.text.ParseException e) {
            System.out.println("Something went wrong");
        } catch (ParseException e) {
            System.out.println("Something went wrong");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }

        Device dev = deviceDao.findDeviceByMac(json.get("mac").toString());
        if (dev != null) {
            return Response.status(400).entity("Device already exist").build();
        } else {

            if (tstamp_req.after(tstamp)) {
                Device dev_ds = new Device.Builder().build();
                dev_ds.setMac(json.get("mac").toString());
                dev_ds.setTimestamp(tstamp_req.toString());
                Long id = deviceDao.createDevice(dev_ds);
                JSONObject json_res = new JSONObject();
                json_res.put("id", id.toString());
                output = json_res.toString();
            } else {
                return Response.status(400).entity("Bad timestamp").build();
            }
        }

        return Response.status(201).entity(output).build();
    }

    @GET
    @Path("/all")
    public Response getAllDevices() {
        String output = "Not implemented";
        return Response.status(200).entity(output).build();
    }

    @GET
    @Path("/mac/{param}")
    public Response getDeviceByMac(@PathParam("param") String mac) {
        String output = null;
        DeviceDao deviceDao = null;
        try {
            deviceDao = new DeviceDao();
        } catch (IOException e) {
            output = "Device not found";
            return Response.status(404).entity(output).build();
        }
        Device dev = deviceDao.findDeviceByMac(mac);
        if (dev != null) {
            JSONObject json = new JSONObject();
            json.put("id", dev.getId());
            json.put("mac", dev.getMac());
            json.put("timestamp", dev.getTimestamp());
            output = json.toString();
        } else {
            return Response.status(404).entity("No record").build();
        }

        return Response.status(200).entity(output).build();
    }

}
